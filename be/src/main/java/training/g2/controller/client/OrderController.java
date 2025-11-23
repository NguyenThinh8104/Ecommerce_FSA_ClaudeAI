package training.g2.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import training.g2.dto.Request.Shipping.CreateShippingOrderRequest;
import training.g2.dto.Response.Order.CreateOrderResponse;
import training.g2.dto.Response.Order.UpdatePaymentStatusReq;
import training.g2.model.ApiResponse;
import training.g2.model.Order;
import training.g2.model.User;
import training.g2.model.enums.PaymentMethodEnum;
import training.g2.service.OrderService;
//import training.g2.service.VNPayService;
import training.g2.util.SecurityUtil;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final SecurityUtil securityUtil;
//    private  final VNPayService vnPayService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(
            @RequestBody CreateShippingOrderRequest request, HttpServletRequest req) throws UnsupportedEncodingException {
        User user = securityUtil.getCurrentUser();
        Order order = orderService.createOrder(request, user);
        String paymentUrl = null;

        if(request.getPaymentMethod().equals(PaymentMethodEnum.VN_PAY) ) {
            double amount = order.getTotalPrice() + order.getGhnFee();
            String paymentRef = order.getUuid();
//            String ip = vnPayService.getIpAddress(req);
//            paymentUrl = vnPayService.generateVNPayURL(amount,paymentRef,ip);
        }
        CreateOrderResponse data = new CreateOrderResponse(order, paymentUrl);
        ApiResponse<CreateOrderResponse> result = new ApiResponse<>("Order thành công", data);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-payment-status")
    public ResponseEntity<Void> updatePaymentStatus(@RequestBody UpdatePaymentStatusReq req) {
            orderService.updateOrderPaymentStatus(req.getUuid(), req.getStatus());
        return ResponseEntity.ok().body(null);
    }
}
