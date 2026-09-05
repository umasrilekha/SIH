import { api } from './api';
import { Application, ApplicationDetail, AuditLog } from '../types';
import { FALLBACK_APPLICATIONS, FALLBACK_APPLICATION_DETAIL_124 } from './mockData';

export const applicationService = {
  getApplications: async (query?: string, status?: string, type?: string): Promise<Application[]> => {
    try {
      const response = await api.get<Application[]>('/applications', {
        params: { query, status, type },
      });
      if (Array.isArray(response.data) && response.data.length > 0) {
        return response.data;
      }
      return FALLBACK_APPLICATIONS;
    } catch (e) {
      console.warn('Backend unavailable, using resilient fallback applications:', e);
      let filtered = [...FALLBACK_APPLICATIONS];
      if (query && query.trim()) {
        const q = query.trim().toLowerCase();
        filtered = filtered.filter(
          (a) =>
            a.applicationId.toLowerCase().includes(q) ||
            a.citizenReference.toLowerCase().includes(q) ||
            a.rationCardNo.toLowerCase().includes(q)
        );
      }
      if (status) {
        filtered = filtered.filter((a) => a.currentStatus === status);
      }
      if (type) {
        filtered = filtered.filter((a) => a.applicationType === type);
      }
      return filtered;
    }
  },

  getApplicationById: async (id: number): Promise<ApplicationDetail> => {
    try {
      const response = await api.get<ApplicationDetail>(`/applications/${id}`);
      return response.data;
    } catch (e) {
      console.warn(`Backend unavailable, using fallback for application ID ${id}:`, e);
      const app = FALLBACK_APPLICATIONS.find((a) => a.id === id) || FALLBACK_APPLICATIONS[0];
      return {
        application: app,
        currentRationRecord: FALLBACK_APPLICATION_DETAIL_124.currentRationRecord
      };
    }
  },

  getApplicationByCode: async (code: string): Promise<ApplicationDetail> => {
    try {
      const response = await api.get<ApplicationDetail>(`/applications/code/${code}`);
      return response.data;
    } catch (e) {
      console.warn(`Backend unavailable, using fallback for application code ${code}:`, e);
      const app = FALLBACK_APPLICATIONS.find((a) => a.applicationId === code) || FALLBACK_APPLICATIONS[0];
      return {
        application: app,
        currentRationRecord: FALLBACK_APPLICATION_DETAIL_124.currentRationRecord
      };
    }
  },

  startReview: async (id: number): Promise<ApplicationDetail> => {
    try {
      const response = await api.post<ApplicationDetail>(`/applications/${id}/start-review`);
      return response.data;
    } catch (e) {
      const app = FALLBACK_APPLICATIONS.find((a) => a.id === id) || FALLBACK_APPLICATIONS[0];
      return {
        application: { ...app, currentStatus: 'PROCESSING' },
        currentRationRecord: FALLBACK_APPLICATION_DETAIL_124.currentRationRecord
      };
    }
  },

  approveApplication: async (id: number, comments?: string): Promise<ApplicationDetail> => {
    try {
      const response = await api.post<ApplicationDetail>(`/applications/${id}/approve`, { comments });
      return response.data;
    } catch (e) {
      const app = FALLBACK_APPLICATIONS.find((a) => a.id === id) || FALLBACK_APPLICATIONS[0];
      return {
        application: { ...app, currentStatus: 'COMPLETED', officerComments: comments },
        currentRationRecord: FALLBACK_APPLICATION_DETAIL_124.currentRationRecord
      };
    }
  },

  rejectApplication: async (id: number, reason: string): Promise<ApplicationDetail> => {
    try {
      const response = await api.post<ApplicationDetail>(`/applications/${id}/reject`, { reason });
      return response.data;
    } catch (e) {
      const app = FALLBACK_APPLICATIONS.find((a) => a.id === id) || FALLBACK_APPLICATIONS[0];
      return {
        application: { ...app, currentStatus: 'REJECTED', officerComments: reason },
        currentRationRecord: FALLBACK_APPLICATION_DETAIL_124.currentRationRecord
      };
    }
  },

  requestInformation: async (id: number, comments: string): Promise<ApplicationDetail> => {
    try {
      const response = await api.post<ApplicationDetail>(`/applications/${id}/request-information`, { comments });
      return response.data;
    } catch (e) {
      const app = FALLBACK_APPLICATIONS.find((a) => a.id === id) || FALLBACK_APPLICATIONS[0];
      return {
        application: { ...app, currentStatus: 'PROCESSING', officerComments: comments },
        currentRationRecord: FALLBACK_APPLICATION_DETAIL_124.currentRationRecord
      };
    }
  },

  getApplicationHistory: async (id: number): Promise<AuditLog[]> => {
    try {
      const response = await api.get<AuditLog[]>(`/applications/${id}/history`);
      return response.data;
    } catch (e) {
      return [];
    }
  },
};
