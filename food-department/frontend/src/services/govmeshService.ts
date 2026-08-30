import { api } from './api';
import {
  IntegrationTransaction,
  CanonicalAddressUpdateRequest,
  CanonicalAddressUpdateResponse,
} from '../types/integration';

export const govmeshService = {
  getTransactions: async (): Promise<IntegrationTransaction[]> => {
    const response = await api.get<IntegrationTransaction[]>('/govmesh/transactions');
    return response.data;
  },

  getTransactionByCorrelationId: async (correlationId: string): Promise<IntegrationTransaction> => {
    const response = await api.get<IntegrationTransaction>(`/govmesh/transactions/${correlationId}`);
    return response.data;
  },

  simulateInteroperability: async (
    payload: CanonicalAddressUpdateRequest
  ): Promise<CanonicalAddressUpdateResponse> => {
    const response = await api.post<CanonicalAddressUpdateResponse>(
      '/govmesh/interoperability/address-update',
      payload
    );
    return response.data;
  },
};
