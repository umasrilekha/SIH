package com.govmesh.food.govmesh.router;

import com.govmesh.food.govmesh.adapter.FoodDepartmentAdapter;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateRequest;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IntegrationRouter {

    private final FoodDepartmentAdapter foodDepartmentAdapter;
    private final String defaultSoapEndpointUrl;

    public IntegrationRouter(FoodDepartmentAdapter foodDepartmentAdapter,
                              @Value("${govmesh.food.soap.endpoint-url:http://localhost:${server.port:8081}/ws}") String defaultSoapEndpointUrl) {
        this.foodDepartmentAdapter = foodDepartmentAdapter;
        this.defaultSoapEndpointUrl = defaultSoapEndpointUrl;
    }

    public CanonicalAddressUpdateResponse routeAddressUpdate(CanonicalAddressUpdateRequest canonicalRequest) {
        if (canonicalRequest == null || canonicalRequest.getTargetDepartment() == null) {
            throw new IllegalArgumentException("Target department must be specified in the canonical request.");
        }

        String target = canonicalRequest.getTargetDepartment().toUpperCase();

        if ("FOOD".equals(target) || "FOOD_SUPPLIES".equals(target) || "DEPT2".equals(target)) {
            return foodDepartmentAdapter.sendAddressUpdate(canonicalRequest, defaultSoapEndpointUrl);
        } else {
            return CanonicalAddressUpdateResponse.builder()
                    .applicationId(canonicalRequest.getApplicationId())
                    .status("FAILED")
                    .message("No integration adapter registered for target department: " + canonicalRequest.getTargetDepartment())
                    .correlationId(canonicalRequest.getCorrelationId())
                    .targetDepartment(target)
                    .errorCode("UNKNOWN_TARGET_DEPARTMENT")
                    .build();
        }
    }
}
