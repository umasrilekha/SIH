package com.govmesh.food.soap.fault;

import com.govmesh.food.soap.exception.SoapServiceException;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.server.endpoint.AbstractSoapFaultDefinitionExceptionResolver;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;

@Component
public class SoapFaultDefinitionExceptionResolver extends AbstractSoapFaultDefinitionExceptionResolver {

    @Override
    protected SoapFaultDefinition getFaultDefinition(Object endpoint, Exception ex) {
        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.CLIENT);

        if (ex instanceof SoapServiceException) {
            SoapServiceException soapEx = (SoapServiceException) ex;
            faultDefinition.setFaultStringOrReason(soapEx.getFaultCode() + ": " + soapEx.getMessage());
        } else {
            faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
            faultDefinition.setFaultStringOrReason("SERVICE_ERROR: An internal service processing error occurred.");
        }

        return faultDefinition;
    }

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        // Ensure stack traces, SQL errors, internal class names are never exposed in SOAP fault body
    }
}
