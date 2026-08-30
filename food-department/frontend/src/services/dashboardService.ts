import { api } from './api';
import { Application, DashboardSummary, RecentActivity } from '../types';

export const dashboardService = {
  getSummary: async (): Promise<DashboardSummary> => {
    const response = await api.get<DashboardSummary>('/dashboard/summary');
    return response.data;
  },

  getRecentApplications: async (): Promise<Application[]> => {
    const response = await api.get<Application[]>('/dashboard/recent-applications');
    return response.data;
  },

  getRecentActivities: async (): Promise<RecentActivity[]> => {
    const response = await api.get<RecentActivity[]>('/dashboard/recent-activity');
    return response.data;
  },
};
