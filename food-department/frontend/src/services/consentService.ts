import { api } from './api';
import { ConsentRecord, ConsentDetail, ConsentValidationResult } from '../types/consent';

export const consentService = {
  getConsents: async (): Promise<ConsentRecord[]> => {
    const response = await api.get<ConsentRecord[]>('/consent');
    return response.data;
  },

  getConsentById: async (consentId: string): Promise<ConsentDetail> => {
    const response = await api.get<ConsentDetail>(`/consent/${consentId}`);
    return response.data;
  },

  validateConsent: async (payload: {
    consentId: string;
    requestingDepartment: string;
    receivingDepartment: string;
    purpose: string;
    requestedFields?: string[];
  }): Promise<ConsentValidationResult> => {
    const response = await api.post<ConsentValidationResult>('/consent/validate', payload);
    return response.data;
  },
};
