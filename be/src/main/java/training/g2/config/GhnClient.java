package training.g2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import training.g2.dto.Request.Ghn.GhnCreateOrderRequest;
import training.g2.dto.Request.Ghn.GhnFeeRequest;
import training.g2.dto.Request.Ghn.GhnLeadtimeRequest;
import training.g2.dto.Response.Ghn.GhnCreateOrderResponse;
import training.g2.dto.Response.Ghn.GhnFeeResponse;
import training.g2.dto.Response.Ghn.GhnLeadtimeResponse;
import training.g2.exception.common.BusinessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GhnClient {

    @Value("${ghn.base-url-pro}")
    private String baseUrl;

    @Value("${ghn.token-pro}")
    private String token;

    @Value("${ghn.shop-id-pro}")
    private Integer shopId;

    private final RestTemplate restTemplate;

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.set("ShopId", String.valueOf(shopId));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private <T> T post(String path, Object body, Class<T> responseType) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(body, defaultHeaders());
            String url = baseUrl + path;
            ResponseEntity<T> res = restTemplate.postForEntity(url, entity, responseType);
            return res.getBody();
        } catch (HttpClientErrorException ex) {
            String raw = ex.getResponseBodyAsString();
            String msg = extractMessage(raw);
            if (raw.contains("PRICE_DECLARE_OVER_LIMIT")) {
                throw new BusinessException("Giá trị khai giá vượt mức cho phép. Vui lòng giảm xuống dưới 8.000.000đ.");
            }
            throw new BusinessException(msg != null ? msg : "GHN lỗi. Vui lòng thử lại sau.");
        } catch (Exception e) {
            throw new BusinessException("Không thể kết nối GHN. Vui lòng thử lại sau.");
        }
    }

    private String extractMessage(String raw) {
        if (raw == null)
            return null;
        int idx = raw.indexOf("\"message\":\"");
        if (idx < 0)
            return null;
        int start = idx + "\"message\":\"".length();
        int end = raw.indexOf("\"", start);
        return (end > start) ? raw.substring(start, end) : null;
    }

    public GhnFeeResponse calculateFee(GhnFeeRequest req) {
        return post("/v2/shipping-order/fee", req, GhnFeeResponse.class);
    }

    public GhnLeadtimeResponse getLeadtime(GhnLeadtimeRequest req) {
        return post("/v2/shipping-order/leadtime", req, GhnLeadtimeResponse.class);
    }

    public GhnCreateOrderResponse createOrder(GhnCreateOrderRequest req) {
        return post("/v2/shipping-order/create", req, GhnCreateOrderResponse.class);
    }

    public boolean cancelOrders(List<String> orderCodes) {

        // Lọc rỗng
        List<String> validCodes = orderCodes.stream()
                .filter(code -> code != null && !code.isBlank())
                .toList();

        if (validCodes.isEmpty()) return false;

        // BODY ĐÚNG CHUẨN GHN
        Map<String, Object> body = new HashMap<>();
        body.put("order_codes", validCodes);


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, defaultHeaders());
        String url = baseUrl + "/v2/switch-status/cancel";
        try {
            ResponseEntity<Map> res = restTemplate.postForEntity(url, entity, Map.class);
            Integer code = (Integer) res.getBody().get("code");
            return code != null && code == 200;

        } catch (Exception e) {
            System.err.println(" Lỗi khi hủy đơn GHN: " + e.getMessage());
            return false;
        }
    }
}
