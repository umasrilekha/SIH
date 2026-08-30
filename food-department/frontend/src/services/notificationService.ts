import { api } from './api';
import { NotificationItem } from '../types';

export const notificationService = {
  getNotifications: async (): Promise<NotificationItem[]> => {
    const response = await api.get<NotificationItem[]>('/notifications');
    return response.data;
  },

  markAsRead: async (id: number): Promise<NotificationItem> => {
    const response = await api.patch<NotificationItem>(`/notifications/${id}/read`);
    return response.data;
  },
};
