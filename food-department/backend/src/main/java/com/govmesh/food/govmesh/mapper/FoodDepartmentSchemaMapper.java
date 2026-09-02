package com.govmesh.food.govmesh.mapper;

import com.govmesh.food.entity.Application;
import com.govmesh.food.govmesh.dto.CanonicalAddressUpdateRequest;
import com.govmesh.food.repository.ApplicationRepository;
import com.govmesh.food.soap.dto.ObjectFactory;
import com.govmesh.food.soap.dto.UpdateRationAddress;
import org.springframework.stereotype.Component;

@Component
public class FoodDepartmentSchemaMapper {

    private final ApplicationRepository applicationRepository;
    private final ObjectFactory objectFactory;

    public FoodDepartmentSchemaMapper(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
        this.objectFactory = new ObjectFactory();
    }

    public UpdateRationAddress mapCanonicalToSoapRequest(CanonicalAddressUpdateRequest canonical) {
        if (canonical == null) {
            throw new IllegalArgumentException("Canonical address update request cannot be null.");
        }

        UpdateRationAddress soapRequest = objectFactory.createUpdateRationAddress();
        soapRequest.setApplicationId(canonical.getApplicationId());

        if (canonical.getCitizen() != null) {
            soapRequest.setCitizenName(canonical.getCitizen().getName());
            if (canonical.getCitizen().getAddress() != null) {
                soapRequest.setAddress(canonical.getCitizen().getAddress().getLine());
                soapRequest.setDistrictCode(canonical.getCitizen().getAddress().getDistrict());
                soapRequest.setTalukaCode(canonical.getCitizen().getAddress().getTaluka());
            }
        }

        boolean isRevenueVerified = canonical.getVerification() != null
                && ("VALID".equalsIgnoreCase(canonical.getVerification().getStatus())
                || "VERIFIED".equalsIgnoreCase(canonical.getVerification().getStatus())
                || Boolean.TRUE.equals(canonical.getVerification().getVerified()));
        soapRequest.setRevenueVerified(isRevenueVerified);

        if (canonical.getConsent() != null) {
            soapRequest.setConsentId(canonical.getConsent().getId());
        } else {
            soapRequest.setConsentId("N/A");
        }

        soapRequest.setCorrelationId(canonical.getCorrelationId());

        // Resolve RationCardNo from target department master application table if present
        String rationCardNo = "MH12-2026-000124";
        if (canonical.getApplicationId() != null) {
            Application app = applicationRepository.findByApplicationId(canonical.getApplicationId()).orElse(null);
            if (app != null && app.getRationCardNo() != null) {
                rationCardNo = app.getRationCardNo();
            }
        }
        soapRequest.setRationCardNo(rationCardNo);

        return soapRequest;
    }
}
