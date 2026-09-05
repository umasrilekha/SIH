import { api } from './api';
import { Application, DashboardSummary, RecentActivity } from '../types';
import { FALLBACK_DASHBOARD_SUMMARY, FALLBACK_APPLICATIONS, FALLBACK_RECENT_ACTIVITIES } from './mockData';

export const dashboardService = {
  getSummary: async (): Promise<DashboardSummary> => {
    try {
      const response = await api.get<DashboardSummary>('/dashboard/summary');
      return response.data || FALLBACK_DASHBOARD_SUMMARY;
    } catch (e) {
      console.warn('Backend unavailable, using fallback dashboard summary:', e);
      return FALLBACK_DASHBOARD_SUMMARY;
    }
  },

  getRecentApplications: async (): Promise<Application[]> => {
    try {
      const response = await api.get<Application[]>('/dashboard/recent-applications');
      if (Array.isArray(response.data) && response.data.length > 0) {
        return response.data;
      }
      return FALLBACK_APPLICATIONS.slice(0, 5);
    } catch (e) {
      console.warn('Backend unavailable, using fallback recent applications:', e);
      return FALLBACK_APPLICATIONS.slice(0, 5);
    }
  },

  getRecentActivities: async (): Promise<RecentActivity[]> => {
    try {
      const response = await api.get<RecentActivity[]>('/dashboard/recent-activity');
      if (Array.isArray(response.data) && response.data.length > 0) {
        return response.data;
      }
      return FALLBACK_RECENT_ACTIVITIES;
    } catch (e) {
      console.warn('Backend unavailable, using fallback recent activities:', e);
      return FALLBACK_RECENT_ACTIVITIES;
    }
  },
};
