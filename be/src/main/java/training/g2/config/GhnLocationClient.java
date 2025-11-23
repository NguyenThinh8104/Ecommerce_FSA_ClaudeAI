package training.g2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import training.g2.dto.Response.Ghn.GhnDistrictResponse;
import training.g2.dto.Response.Ghn.GhnProvinceResponse;
import training.g2.dto.Response.Ghn.GhnWardResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class GhnLocationClient {

    @Value("${ghn.base-url-pro}")
    private String baseUrl;

    @Value("${ghn.token-pro}")
    private String token;

    private final RestTemplate restTemplate;



    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public GhnProvinceResponse getProvinces() {
        String url = baseUrl + "/master-data/province";
        HttpEntity<Void> entity = new HttpEntity<>(defaultHeaders());
        ResponseEntity<GhnProvinceResponse> res =
                restTemplate.exchange(url, HttpMethod.GET, entity, GhnProvinceResponse.class);
        return res.getBody();
    }

    public GhnDistrictResponse getDistricts(Integer provinceId) {
        String url = baseUrl + "/master-data/district";
        HttpHeaders headers = defaultHeaders();

        // GHN thường yêu cầu body POST cho district/ward, tuỳ docs bạn chỉnh:
        String bodyJson = "{\"province_id\":" + provinceId + "}";

        HttpEntity<String> entity = new HttpEntity<>(bodyJson, headers);
        ResponseEntity<GhnDistrictResponse> res =
                restTemplate.postForEntity(url, entity, GhnDistrictResponse.class);
        return res.getBody();
    }

    public GhnWardResponse getWards(Integer districtId) {
        String url = baseUrl + "/master-data/ward";
        HttpHeaders headers = defaultHeaders();
        String bodyJson = "{\"district_id\":" + districtId + "}";

        HttpEntity<String> entity = new HttpEntity<>(bodyJson, headers);
        ResponseEntity<GhnWardResponse> res =
                restTemplate.postForEntity(url, entity, GhnWardResponse.class);
        return res.getBody();
    }
}
