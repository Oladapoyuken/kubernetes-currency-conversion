package com.yukenautomation.currencyconversionservice.conversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange-service", url = "${CURRENCY_EXCHANGE_SERVICE_HOST:http://localhost}:8000")
@FeignClient(
        name = "currency-exchange-service",
        url = "${CURRENCY_EXCHANGE_URI:http://localhost}:8000")
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversion getExchange(@PathVariable String from, @PathVariable String to);
}

//Use the first link to use the default service discovery of kubernetes
//Second option is best as we can create a custom naming system for the service discovery
