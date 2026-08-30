export interface IntegrationTransaction {
  id: number;
  applicationId: string;
  correlationId: string;
  sourceDepartment: string;
  targetDepartment: string;
  operation: string;
  sourceProtocol: string;
  targetProtocol: string;
  status: 'RECEIVED' | 'TRANSFORMING' | 'SENDING' | 'PROCESSING' | 'SUCCESS' | 'FAILED' | 'BLOCKED' | string;
  consentStatus?: 'ALLOWED' | 'BLOCKED' | string;
  consentFailureReason?: string;
  consentId?: string;
  startedAt: string;
  completedAt?: string;
  errorCode?: string;
  errorMessage?: string;
  rawSourceJson?: string;
  rawCanonicalJson?: string;
  rawSoapRequestXml?: string;
  rawSoapResponseXml?: string;
}

export interface CanonicalAddressUpdateRequest {
  applicationId: string;
  sourceDepartment: string;
  targetDepartment: string;
  correlationId: string;
  purpose?: string;
  requestedFields?: string[];
  citizen: {
    reference: string;
    name: string;
    address: {
      line: string;
      district: string;
      taluka: string;
    };
  };
  verification: {
    status: string;
    source: string;
  };
  consent: {
    id: string;
  };
}

export interface CanonicalAddressUpdateResponse {
  applicationId: string;
  status: 'SUCCESS' | 'FAILED' | 'BLOCKED' | string;
  message: string;
  correlationId: string;
  targetDepartment: string;
  errorCode?: string;
}
