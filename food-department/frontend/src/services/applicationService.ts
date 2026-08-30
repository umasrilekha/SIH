import { api } from './api';
import { Application, ApplicationDetail, AuditLog } from '../types';

export const applicationService = {
  getApplications: async (query?: string, status?: string, type?: string): Promise<Application[]> => {
    const response = await api.get<Application[]>('/applications', {
      params: { query, status, type },
    });
    return response.data;
  },

  getApplicationById: async (id: number): Promise<ApplicationDetail> => {
    const response = await api.get<ApplicationDetail>(`/applications/${id}`);
    return response.data;
  },

  getApplicationByCode: async (code: string): Promise<ApplicationDetail> => {
    const response = await api.get<ApplicationDetail>(`/applications/code/${code}`);
    return response.data;
  },

  startReview: async (id: number): Promise<ApplicationDetail> => {
    const response = await api.post<ApplicationDetail>(`/applications/${id}/start-review`);
    return response.data;
  },

  approveApplication: async (id: number, comments?: string): Promise<ApplicationDetail> => {
    const response = await api.post<ApplicationDetail>(`/applications/${id}/approve`, { comments });
    return response.data;
  },

  rejectApplication: async (id: number, reason: string): Promise<ApplicationDetail> => {
    const response = await api.post<ApplicationDetail>(`/applications/${id}/reject`, { reason });
    return response.data;
  },

  requestInformation: async (id: number, comments: string): Promise<ApplicationDetail> => {
    const response = await api.post<ApplicationDetail>(`/applications/${id}/request-information`, { comments });
    return response.data;
  },

  getApplicationHistory: async (id: number): Promise<AuditLog[]> => {
    const response = await api.get<AuditLog[]>(`/applications/${id}/history`);
    return response.data;
  },
};
