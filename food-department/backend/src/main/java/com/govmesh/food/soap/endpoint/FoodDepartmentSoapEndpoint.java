package com.govmesh.food.soap.endpoint;

import com.govmesh.food.service.ApplicationService;
import com.govmesh.food.soap.dto.ObjectFactory;
import com.govmesh.food.soap.dto.SoapResultDTO;
import com.govmesh.food.soap.dto.UpdateRationAddress;
import com.govmesh.food.soap.dto.UpdateRationAddressCommand;
import com.govmesh.food.soap.dto.UpdateRationAddressResponse;
import com.govmesh.food.soap.exception.SoapServiceException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class FoodDepartmentSoapEndpoint {

    private static final String NAMESPACE_URI = "http://govmesh.example/food";

    private final ApplicationService applicationService;
    private final ObjectFactory objectFactory;

    public FoodDepartmentSoapEndpoint(ApplicationService applicationService) {
        this.applicationService = applicationService;
        this.objectFactory = new ObjectFactory();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "UpdateRationAddress")
    @ResponsePayload
    public UpdateRationAddressResponse updateRationAddress(@RequestPayload UpdateRationAddress request) {
        if (request == null) {
            throw new SoapServiceException("INVALID_REQUEST", "UpdateRationAddress payload element is null.");
        }

        UpdateRationAddressCommand command = UpdateRationAddressCommand.builder()
                .applicationId(request.getApplicationId())
                .citizenName(request.getCitizenName())
                .rationCardNo(request.getRationCardNo())
                .address(request.getAddress())
                .districtCode(request.getDistrictCode())
                .talukaCode(request.getTalukaCode())
                .revenueVerified(request.isRevenueVerified())
                .consentId(request.getConsentId())
                .correlationId(request.getCorrelationId())
                .build();

        SoapResultDTO result = applicationService.processSoapAddressUpdate(command);

        UpdateRationAddressResponse response = objectFactory.createUpdateRationAddressResponse();
        response.setApplicationId(result.getApplicationId());
        response.setStatus(result.getStatus());
        response.setMessage(result.getMessage());
        response.setCorrelationId(result.getCorrelationId());

        return response;
    }
}
