import { api } from './api';
import { Application, RationRecord } from '../types';

export const rationService = {
  getRationRecords: async (query?: string, district?: string, taluka?: string): Promise<RationRecord[]> => {
    const response = await api.get<RationRecord[]>('/ration-records', {
      params: { query, district, taluka },
    });
    return response.data;
  },

  getRationRecordById: async (id: number): Promise<RationRecord> => {
    const response = await api.get<RationRecord>(`/ration-records/${id}`);
    return response.data;
  },

  getRationRecordByCardNo: async (cardNo: string): Promise<RationRecord> => {
    const response = await api.get<RationRecord>(`/ration-records/card/${cardNo}`);
    return response.data;
  },

  getRationRecordApplications: async (id: number): Promise<Application[]> => {
    const response = await api.get<Application[]>(`/ration-records/${id}/applications`);
    return response.data;
  },
};
