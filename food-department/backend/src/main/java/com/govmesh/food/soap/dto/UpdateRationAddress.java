package com.govmesh.food.soap.dto;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "applicationId",
    "citizenName",
    "rationCardNo",
    "address",
    "districtCode",
    "talukaCode",
    "revenueVerified",
    "consentId",
    "correlationId"
})
@XmlRootElement(name = "UpdateRationAddress", namespace = "http://govmesh.example/food")
public class UpdateRationAddress {

    @XmlElement(name = "ApplicationId", namespace = "http://govmesh.example/food", required = true)
    protected String applicationId;

    @XmlElement(name = "CitizenName", namespace = "http://govmesh.example/food", required = true)
    protected String citizenName;

    @XmlElement(name = "RationCardNo", namespace = "http://govmesh.example/food", required = true)
    protected String rationCardNo;

    @XmlElement(name = "Address", namespace = "http://govmesh.example/food", required = true)
    protected String address;

    @XmlElement(name = "DistrictCode", namespace = "http://govmesh.example/food", required = true)
    protected String districtCode;

    @XmlElement(name = "TalukaCode", namespace = "http://govmesh.example/food", required = true)
    protected String talukaCode;

    @XmlElement(name = "RevenueVerified", namespace = "http://govmesh.example/food", required = true)
    protected boolean revenueVerified;

    @XmlElement(name = "ConsentId", namespace = "http://govmesh.example/food", required = true)
    protected String consentId;

    @XmlElement(name = "CorrelationId", namespace = "http://govmesh.example/food", required = true)
    protected String correlationId;

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String value) { this.applicationId = value; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String value) { this.citizenName = value; }

    public String getRationCardNo() { return rationCardNo; }
    public void setRationCardNo(String value) { this.rationCardNo = value; }

    public String getAddress() { return address; }
    public void setAddress(String value) { this.address = value; }

    public String getDistrictCode() { return districtCode; }
    public void setDistrictCode(String value) { this.districtCode = value; }

    public String getTalukaCode() { return talukaCode; }
    public void setTalukaCode(String value) { this.talukaCode = value; }

    public boolean isRevenueVerified() { return revenueVerified; }
    public void setRevenueVerified(boolean value) { this.revenueVerified = value; }

    public String getConsentId() { return consentId; }
    public void setConsentId(String value) { this.consentId = value; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String value) { this.correlationId = value; }
}
