package com.eazybytes.gatewayserver.handler;

import com.eazybytes.gatewayserver.dto.*;
import com.eazybytes.gatewayserver.service.client.CustomerSummeryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class CustomerCompositionHandler {

    private final CustomerSummeryClient customerSummeryClient;


    public Mono<ServerResponse> fetchCustomerSummery(ServerRequest serverRequest) {
        String mobileNumber = serverRequest.queryParam("mobileNumber").orElse("");
        if (mobileNumber.isBlank()) {
            return ServerResponse.badRequest().body(Mono.empty(), String.class);
        }

        Mono<ResponseEntity<CustomerDto>> customerDetails = customerSummeryClient.fetchCustomerDetails(mobileNumber)
                .onErrorReturn(ResponseEntity.ok(null));

        Mono<ResponseEntity<AccountsDto>> accountDetails = customerSummeryClient.fetchAccountDetails(mobileNumber)
                .onErrorReturn(ResponseEntity.ok(null));

        Mono<ResponseEntity<LoansDto>> loanDetails = customerSummeryClient.fetchLoanDetails(mobileNumber)
                .onErrorReturn(ResponseEntity.ok(null));

        Mono<ResponseEntity<CardsDto>> carsDetails = customerSummeryClient.fetchCarsDetails(mobileNumber)
                .onErrorReturn(ResponseEntity.ok(null));

        return Mono.zip(customerDetails, accountDetails, loanDetails, carsDetails)
                .flatMap(tuple -> {
                    CustomerDto customerDto = tuple.getT1().getBody();
                    AccountsDto accountsDto = tuple.getT2().getBody();
                    LoansDto loansDto = tuple.getT3().getBody();
                    CardsDto cardsDto = tuple.getT4().getBody();

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(new CustomerSummeryDto(customerDto, accountsDto, loansDto, cardsDto));

                });


    }

}
