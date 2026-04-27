package com.eazybytes.gatewayserver.router;

import com.eazybytes.gatewayserver.handler.CustomerCompositionHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Configuration(proxyBeanMethods = false)
public class CustomerCompositionRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(CustomerCompositionHandler customerCompositionHandler) {

        return RouterFunctions.route(RequestPredicates.GET("/api/composite/fetchCustomerSummery")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON))
                        .and(RequestPredicates.queryParam("mobileNumber", StringUtils::isNotBlank)),
                customerCompositionHandler::fetchCustomerSummery);
    }

}
