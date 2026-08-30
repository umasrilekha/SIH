package com.govmesh.food.soap.dto;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public UpdateRationAddress createUpdateRationAddress() {
        return new UpdateRationAddress();
    }

    public UpdateRationAddressResponse createUpdateRationAddressResponse() {
        return new UpdateRationAddressResponse();
    }
}
