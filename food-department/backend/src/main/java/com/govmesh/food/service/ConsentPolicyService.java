package com.govmesh.food.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsentPolicyService {

    private static final Map<String, List<String>> ALLOWED_FIELDS_BY_PURPOSE = new HashMap<>();
    private static final Map<String, List<String>> RESTRICTED_FIELDS_BY_PURPOSE = new HashMap<>();

    static {
        // Purpose: RATION_ADDRESS_UPDATE
        ALLOWED_FIELDS_BY_PURPOSE.put("RATION_ADDRESS_UPDATE", Arrays.asList(
                "citizen.name",
                "citizen.address",
                "citizen.address.district",
                "citizen.address.taluka",
                "verification.status"
        ));

        RESTRICTED_FIELDS_BY_PURPOSE.put("RATION_ADDRESS_UPDATE", Arrays.asList(
                "citizen.phone",
                "citizen.email",
                "citizen.financialInformation",
                "citizen.identityDocuments"
        ));

        // Purpose: MEMBER_ADDITION (example secondary purpose)
        ALLOWED_FIELDS_BY_PURPOSE.put("MEMBER_ADDITION", Arrays.asList(
                "citizen.name",
                "citizen.address",
                "verification.status"
        ));

        RESTRICTED_FIELDS_BY_PURPOSE.put("MEMBER_ADDITION", Arrays.asList(
                "citizen.phone",
                "citizen.email",
                "citizen.financialInformation"
        ));
    }

    public List<String> getAllowedFields(String purpose) {
        if (purpose == null) return Collections.emptyList();
        return ALLOWED_FIELDS_BY_PURPOSE.getOrDefault(purpose.toUpperCase(), Collections.emptyList());
    }

    public List<String> getRestrictedFields(String purpose) {
        if (purpose == null) return Collections.emptyList();
        return RESTRICTED_FIELDS_BY_PURPOSE.getOrDefault(purpose.toUpperCase(), Arrays.asList(
                "citizen.phone",
                "citizen.email",
                "citizen.financialInformation",
                "citizen.identityDocuments"
        ));
    }

    public boolean isFieldAllowed(String purpose, String field) {
        List<String> allowed = getAllowedFields(purpose);
        return allowed.contains(field);
    }
}
