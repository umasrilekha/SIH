import { api } from './api';
import { AuditLog } from '../types';

export const auditService = {
  getAuditLogs: async (appId?: string, action?: string, result?: string): Promise<AuditLog[]> => {
    const response = await api.get<AuditLog[]>('/audit-logs', {
      params: { appId, action, result },
    });
    return response.data;
  },
};
