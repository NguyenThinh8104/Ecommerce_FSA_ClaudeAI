package training.g2.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import training.g2.config.GhnClient;
import training.g2.dto.Request.Ghn.*;
import training.g2.dto.Request.Shipping.CreateShippingOrderRequest;
import training.g2.dto.Request.Shipping.OrderItemDto;
import training.g2.dto.Response.Ghn.GhnCreateOrderResponse;
import training.g2.exception.common.BusinessException;
import training.g2.model.*;
import training.g2.model.enums.OrderStatusEnum;
import training.g2.model.enums.PaymentMethodEnum;
import training.g2.model.enums.PaymentStatusEnum;
import training.g2.repository.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserAddressRepository userAddressRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final GhnClient ghnClient;
    private final CartDetailRepository cartDetailRepository;

    @Transactional
    public Order createOrder(CreateShippingOrderRequest req, User currentUser) {
        try {
            UserAddress address = userAddressRepository.findById(req.getAddressId())
                    .orElseThrow(() -> new RuntimeException("UserAddress not found: " + req.getAddressId()));

            String receiverName = address.getFullName();
            String receiverPhone = address.getPhone();
            String receiverAddress = address.getAddressDetail();

            District district = districtRepository.findById(address.getDistrict().getId())
                    .orElseThrow(() -> new RuntimeException("District not found: " + address.getDistrict().getId()));

            Ward ward = wardRepository.findById(address.getWard().getId())
                    .orElseThrow(() -> new RuntimeException("Ward not found: " + address.getWard().getId()));

            Integer toDistrictGhnId = district.getGhnDistrictId();
            String toWardGhnCode = ward.getGhnWardCode();

            List<OrderItemDto> itemReqs = req.getItems();
            List<Long> variantIds = itemReqs.stream()
                    .map(OrderItemDto::getVariantId)
                    .toList();

            List<ProductVariant> variants = productVariantRepository.findAllById(variantIds);

            Map<Long, ProductVariant> variantMap = variants.stream()
                    .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));

            if (variantMap.size() != variantIds.size()) {
                throw new RuntimeException("Some product variants not found");
            }

            double totalPrice = 0d;
            List<GhnItem> ghnItems = new ArrayList<>();

            for (OrderItemDto itemReq : itemReqs) {
                Long variantId = itemReq.getVariantId();
                int quantity = itemReq.getQuantity();

                ProductVariant variant = Optional.ofNullable(variantMap.get(variantId))
                        .orElseThrow(() -> new RuntimeException("Variant not found: " + variantId));

                if (variant.getStock() <= 0) {
                    throw new BusinessException("Sản phẩm "
                            + variant.getProduct().getName() + " " + variant.getName() + " đã hết hàng");
                }

                if (variant.getStock() < quantity) {
                    throw new BusinessException("Sản phẩm "
                            + variant.getProduct().getName()
                            + variant.getStock() + " sản phẩm");
                }

                totalPrice += req.getItemsValue();

                GhnItem item = new GhnItem();
                item.setName(variant.getProduct().getName());
                item.setCode(variant.getSku());
                item.setQuantity(quantity);
                item.setPrice((int) itemReq.getPrice());
                item.setWeight(100);

                ghnItems.add(item);
            }

            GhnCreateOrderRequest ghnReq = new GhnCreateOrderRequest();

            PaymentMethodEnum paymentMethod = req.getPaymentMethod();
            switch (paymentMethod) {
                case CASH -> {
                    ghnReq.setPaymentTypeId(2);
                    ghnReq.setCodAmount(req.getCodAmount());
                }
                case VN_PAY -> {
                    ghnReq.setPaymentTypeId(1);
                    ghnReq.setCodAmount(0);
                }
                default -> throw new RuntimeException("Unsupported payment method: " + paymentMethod);
            }

            ghnReq.setNote("Đơn hàng từ hệ thống");
            ghnReq.setRequiredNote("KHONGCHOXEMHANG");

            ghnReq.setToName(receiverName);
            ghnReq.setToPhone(receiverPhone);
            ghnReq.setToAddress(receiverAddress);
            ghnReq.setToDistrictId(toDistrictGhnId);
            ghnReq.setToWardCode(toWardGhnCode);

            ghnReq.setContent("Đơn hàng mới của " + receiverName);

            ghnReq.setWeight(100);

            ghnReq.setPickStationId(null);
            ghnReq.setDeliverStationId(null);

            ghnReq.setInsuranceValue(req.getItemsValue() != null ? req.getItemsValue() : 0);

            ghnReq.setServiceId(0);
            ghnReq.setServiceTypeId(2);

            ghnReq.setCoupon(null);
            ghnReq.setItems(ghnItems);

            log.info("GHN createOrder request: {}", ghnReq);

            GhnCreateOrderResponse ghnRes = ghnClient.createOrder(ghnReq);

            if (ghnRes == null || ghnRes.getCode() == null || !ghnRes.getCode().equals(200)) {
                String errMsg = (ghnRes != null ? ghnRes.getMessage() : "no response from GHN");
                log.error("GHN createOrder error: {}", errMsg);
                throw new RuntimeException("GHN tạo đơn lỗi: " + errMsg);
            }

            GhnCreateOrderResponse.DataResponse ghnData = ghnRes.getData();
            String ghnOrderCode = ghnData.getOrderCode();
            Integer totalFee = ghnData.getTotalFee();
            String expectedDeliveryStr = ghnData.getExpectedDeliveryTime();

            log.info("GHN createOrder success. orderCode={}, totalFee={}, etd={}",
                    ghnOrderCode, totalFee, expectedDeliveryStr);

            Order order = new Order();
            order.setUser(currentUser);
            order.setTotalPrice(totalPrice);
            order.setPaymentMethod(paymentMethod);

            order.setPaymentStatus(PaymentStatusEnum.PENDING);

            order.setOrderStatus(OrderStatusEnum.PROCESSING);
            order.setGhnOrderCode(ghnOrderCode);
            order.setGhnFee(totalFee);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            order.setUuid(uuid);

            if (expectedDeliveryStr != null) {
                ZonedDateTime utcTime = ZonedDateTime.parse(expectedDeliveryStr);
                ZonedDateTime vnTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));
                LocalDateTime etd = vnTime.toLocalDateTime();
                order.setGhnExpectedDelivery(etd);
            }

            order = orderRepository.save(order);

            for (OrderItemDto itemReq : itemReqs) {
                Long variantId = itemReq.getVariantId();
                ProductVariant variant = variantMap.get(variantId);

                if (variant == null) {
                    throw new RuntimeException("Variant not found when saving OrderDetail: " + variantId);
                }

                if (variant.getStock() < itemReq.getQuantity()) {
                    throw new BusinessException("Số lượng trong kho không đủ: " + variant.getSku());
                }

                cartDetailRepository.deleteByUserIdAndVariantId(currentUser.getId(), variantId);

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProductVariant(variant);
                detail.setQuantity(itemReq.getQuantity());
                detail.setPrice(itemReq.getPrice());
                variant.setStock(variant.getStock() - itemReq.getQuantity());
                variant.setSold(variant.getSold() + itemReq.getQuantity());

                orderDetailRepository.save(detail);
            }

            return order;
        } catch (BusinessException e) {
            throw e;
        }

    }

    public void updateOrderPaymentStatus(String uuid,String status) {
        Order order = orderRepository.findOrderByUuid(uuid);
        if (order == null) {
            throw  new RuntimeException("Order not found");
        }

        switch (status) {
            case "SUCCESS":
                order.setPaymentStatus(PaymentStatusEnum.PAID);
                break;

            case "FAILED":
                order.setPaymentStatus(PaymentStatusEnum.FAILED);
                restoreInventory(order);
                List<String> list = new ArrayList<>();
                list.add(order.getGhnOrderCode());
                ghnClient.cancelOrders(list);
                order.setOrderStatus(OrderStatusEnum.CANCELLED);
                break;

            case "COD":
                order.setPaymentStatus(PaymentStatusEnum.PENDING);
                break;
        }
        orderRepository.save(order);
    }


    private void restoreInventory(Order order) {
        List<OrderDetail> items = order.getItems();

        if (items == null || items.isEmpty()) {
            return;
        }

        for (OrderDetail item : items) {
            ProductVariant variant = item.getProductVariant();
            int qty = item.getQuantity();

            if (variant != null  && qty > 0) {
                variant.setStock(variant.getStock() + qty);
                productVariantRepository.save(variant);
            }
        }
    }


}
