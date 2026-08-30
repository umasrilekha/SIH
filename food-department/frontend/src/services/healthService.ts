import { api } from './api';
import { SystemHealth } from '../types';

export const healthService = {
  getSystemHealth: async (): Promise<SystemHealth> => {
    const response = await api.get<SystemHealth>('/system-health');
    return response.data;
  },
};
