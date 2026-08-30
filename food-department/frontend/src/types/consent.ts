export interface ConsentRecord {
  id: number;
  consentId: string;
  citizenReference: string;
  requestingDepartment: string;
  receivingDepartment: string;
  purpose: string;
  status: 'ACTIVE' | 'EXPIRED' | 'REVOKED' | string;
  issuedAt: string;
  expiresAt: string;
  revokedAt?: string;
}

export interface ConsentDetail {
  consent: ConsentRecord;
  permittedData: string[];
  restrictedData: string[];
}

export interface ConsentValidationResult {
  status: 'ALLOWED' | 'BLOCKED' | string;
  reason: string;
  consentId: string;
  purpose: string;
  requestedFields: string[];
  allowedFields: string[];
  timestamp: string;
}
