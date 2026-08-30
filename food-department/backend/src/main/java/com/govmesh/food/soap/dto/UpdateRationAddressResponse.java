package com.govmesh.food.soap.dto;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "applicationId",
    "status",
    "message",
    "correlationId"
})
@XmlRootElement(name = "UpdateRationAddressResponse", namespace = "http://govmesh.example/food")
public class UpdateRationAddressResponse {

    @XmlElement(name = "ApplicationId", namespace = "http://govmesh.example/food", required = true)
    protected String applicationId;

    @XmlElement(name = "Status", namespace = "http://govmesh.example/food", required = true)
    protected String status;

    @XmlElement(name = "Message", namespace = "http://govmesh.example/food", required = true)
    protected String message;

    @XmlElement(name = "CorrelationId", namespace = "http://govmesh.example/food", required = true)
    protected String correlationId;

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String value) { this.applicationId = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public String getMessage() { return message; }
    public void setMessage(String value) { this.message = value; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String value) { this.correlationId = value; }
}
