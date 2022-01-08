package com.yukenautomation.currencyconversionservice.conversion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convert(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        log.info("currency-conversion-service to convert {} to {} in {} places", from, to, quantity);
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> response = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables
        );
        CurrencyConversion datum = response.getBody();
        return new CurrencyConversion(
                datum.getId(), from, to, quantity,
                datum.getConversionMultiple(),
                quantity.multiply(datum.getConversionMultiple()),
                datum.getEnvironment()
        );
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convert_feign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        log.info("currency-conversion-service to convert {} to {} in {} places", from, to, quantity);
        CurrencyConversion datum = proxy.getExchange(from, to);
        return new CurrencyConversion(
                datum.getId(), from, to, quantity,
                datum.getConversionMultiple(),
                quantity.multiply(datum.getConversionMultiple()),
                datum.getEnvironment()
        );
    }

    @GetMapping
    public String getSimple() {
        return "Welcome to currency conversion service";
    }
}
