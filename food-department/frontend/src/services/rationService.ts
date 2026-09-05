import { api } from './api';
import { Application, RationRecord } from '../types';
import { FALLBACK_RATION_RECORDS, FALLBACK_APPLICATIONS } from './mockData';

export const rationService = {
  getRationRecords: async (query?: string, district?: string, taluka?: string): Promise<RationRecord[]> => {
    try {
      const response = await api.get<RationRecord[]>('/ration-records', {
        params: { query, district, taluka },
      });
      if (Array.isArray(response.data) && response.data.length > 0) {
        return response.data;
      }
      return FALLBACK_RATION_RECORDS;
    } catch (e) {
      console.warn('Backend unavailable, using fallback ration records:', e);
      let filtered = [...FALLBACK_RATION_RECORDS];
      if (query && query.trim()) {
        const q = query.trim().toLowerCase();
        filtered = filtered.filter(
          (r) =>
            r.rationCardNo.toLowerCase().includes(q) ||
            r.holderName.toLowerCase().includes(q) ||
            r.houseAddress.toLowerCase().includes(q)
        );
      }
      return filtered;
    }
  },

  getRationRecordById: async (id: number): Promise<RationRecord> => {
    try {
      const response = await api.get<RationRecord>(`/ration-records/${id}`);
      return response.data;
    } catch (e) {
      console.warn(`Backend unavailable, using fallback for ration record ${id}:`, e);
      return FALLBACK_RATION_RECORDS.find((r) => r.id === id) || FALLBACK_RATION_RECORDS[0];
    }
  },

  getRationRecordByCardNo: async (cardNo: string): Promise<RationRecord> => {
    try {
      const response = await api.get<RationRecord>(`/ration-records/card/${cardNo}`);
      return response.data;
    } catch (e) {
      console.warn(`Backend unavailable, using fallback for ration card ${cardNo}:`, e);
      return FALLBACK_RATION_RECORDS.find((r) => r.rationCardNo === cardNo) || FALLBACK_RATION_RECORDS[0];
    }
  },

  getRationRecordApplications: async (id: number): Promise<Application[]> => {
    try {
      const response = await api.get<Application[]>(`/ration-records/${id}/applications`);
      return response.data;
    } catch (e) {
      return FALLBACK_APPLICATIONS.filter((a) => a.id === id || a.rationCardNo === 'MH12-2026-000124');
    }
  },
};
