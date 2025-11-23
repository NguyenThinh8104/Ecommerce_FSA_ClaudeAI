package training.g2.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import training.g2.config.GhnClient;
import training.g2.dto.Request.Ghn.GhnFeeRequest;
import training.g2.dto.Request.Ghn.GhnLeadtimeRequest;
import training.g2.dto.Request.Shipping.ShippingQuoteRequest;
import training.g2.dto.Response.Ghn.GhnFeeResponse;
import training.g2.dto.Response.Ghn.GhnLeadtimeResponse;
import training.g2.dto.Response.Shipping.ShippingQuoteResponse;
import training.g2.exception.common.BusinessException;
import training.g2.model.District;
import training.g2.model.UserAddress;
import training.g2.model.Ward;
import training.g2.repository.DistrictRepository;
import training.g2.repository.UserAddressRepository;
import training.g2.repository.WardRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {

    private final GhnClient ghnClient;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final UserAddressRepository userAddressRepository;

    @Value("${ghn.shop-district-id-pro}")
    private Integer shopDistrictId; // GHN DistrictID của shop

    @Value("${ghn.shop-ward-code-pro}")
    private String shopWardCode; // GHN WardCode của shop

    public ShippingQuoteResponse getQuote(ShippingQuoteRequest req) throws JsonProcessingException {

        UserAddress userAddress = userAddressRepository.findById(req.getAddressId())
                .orElseThrow(() -> new BusinessException("Không tồn tại địa chỉ"));

        // 1. Lấy District & Ward nhận hàng từ DB
        District toDistrict = districtRepository.findById(userAddress.getDistrict().getId())
                .orElseThrow(() -> new BusinessException("District not found"));

        Ward toWard = wardRepository.findById(userAddress.getWard().getId())
                .orElseThrow(() -> new BusinessException("Ward not found"));

        Integer toDistrictGhnId = toDistrict.getGhnDistrictId();
        String toWardGhnCode = toWard.getGhnWardCode();

        // 2. Build request cho GHN
        GhnFeeRequest ghnReq = new GhnFeeRequest();
        ghnReq.setFromDistrictId(shopDistrictId);
        ghnReq.setFromWardCode(shopWardCode);

        ghnReq.setToDistrictId(toDistrictGhnId);
        ghnReq.setToWardCode(toWardGhnCode);

        ghnReq.setServiceTypeId(2);

        ghnReq.setWeight(100);

        ghnReq.setCoupon(null);

        // 3. Call GHN
        GhnFeeResponse ghnRes = ghnClient.calculateFee(ghnReq);

        if (ghnRes == null || !ghnRes.getCode().equals(200)) {
            throw new BusinessException("GHN fee api error: " +
                    (ghnRes != null ? ghnRes.getMessage() : "no response"));
        }

        GhnFeeResponse.GhnFeeData data = ghnRes.getData();
        ObjectMapper o = new ObjectMapper();
        log.info(o.writeValueAsString(ghnReq));

        // 4. Map về response cho FE
        ShippingQuoteResponse response = new ShippingQuoteResponse();
        response.setFee(data.getTotal());
        response.setServiceFee(data.getService_fee());
        response.setInsuranceFee(data.getInsurance_fee());

        // 5. Call GHN Leadtime để lấy thời gian dự kiến giao
        try {
            GhnLeadtimeRequest leadReq = new GhnLeadtimeRequest();
            leadReq.setFromDistrictId(shopDistrictId);
            leadReq.setFromWardCode(shopWardCode);
            leadReq.setToDistrictId(toDistrictGhnId);
            leadReq.setToWardCode(toWardGhnCode);
            leadReq.setServiceId(53320); // rất quan trọng

            GhnLeadtimeResponse leadRes = ghnClient.getLeadtime(leadReq);

            if (leadRes != null
                    && leadRes.getCode() != null
                    && leadRes.getCode().equals(200)
                    && leadRes.getData() != null
                    && leadRes.getData().getLeadtimeOrder() != null
                    && leadRes.getData().getLeadtimeOrder().getToEstimateDate() != null) {

                String toEstimate = leadRes.getData().getLeadtimeOrder().getToEstimateDate();
                // ví dụ: "2025-11-19T16:59:59Z"

                ZonedDateTime utcTime = ZonedDateTime.parse(toEstimate);
                ZonedDateTime vnTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String etaStr = vnTime.format(formatter);

                response.setExpectedDeliveryTime(etaStr);
            } else {
                response.setExpectedDeliveryTime(null);
                log.warn("GHN leadtime api error: {}",
                        leadRes != null ? leadRes.getMessage() : "no response");
            }
        } catch (Exception e) {
            log.error("Error calling GHN leadtime API", e);
            response.setExpectedDeliveryTime(null);
        }

        return response;
    }

}
