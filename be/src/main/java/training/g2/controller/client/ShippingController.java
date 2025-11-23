package training.g2.controller.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import training.g2.dto.Request.Shipping.ShippingQuoteRequest;
import training.g2.dto.Response.Shipping.ShippingQuoteResponse;
import training.g2.service.impl.ShippingService;

@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final ShippingService shippingService;

    @PostMapping("/quote")
    public ShippingQuoteResponse getQuote(@RequestBody ShippingQuoteRequest request) throws JsonProcessingException {
        return shippingService.getQuote(request);
    }
}