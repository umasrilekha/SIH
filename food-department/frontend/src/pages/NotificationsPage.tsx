import React, { useState, useEffect } from 'react';
import { notificationService } from '../services/notificationService';
import { NotificationItem } from '../types';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { EmptyState } from '../components/common/EmptyState';
import { formatDate } from '../utils/formatters';
import { Bell, Check } from 'lucide-react';

export const NotificationsPage: React.FC = () => {
  const [notifications, setNotifications] = useState<NotificationItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchNotifications = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await notificationService.getNotifications();
      setNotifications(data);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to fetch notifications.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNotifications();
  }, []);

  const handleMarkAsRead = async (id: number) => {
    try {
      const updated = await notificationService.markAsRead(id);
      setNotifications((prev) => prev.map((n) => (n.id === id ? updated : n)));
    } catch (err) {
      console.error('Failed to mark notification as read:', err);
    }
  };

  const unreadCount = notifications.filter((n) => !n.isRead).length;

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb items={[{ label: 'Officer Services' }, { label: 'Notifications' }]} />

      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <h1 className="text-base font-extrabold text-blue-950">Officer Internal Notifications</h1>
          <p className="text-xs text-slate-600 mt-0.5">
            Department 2 Alert & System Messages ({unreadCount} Unread Notifications)
          </p>
        </div>
      </div>

      {loading ? (
        <LoadingState message="Loading notifications..." />
      ) : error ? (
        <ErrorState message={error} onRetry={fetchNotifications} />
      ) : notifications.length === 0 ? (
        <EmptyState title="No Notifications" description="You have no active or archived department notifications." />
      ) : (
        <div className="space-y-2">
          {notifications.map((item) => (
            <div
              key={item.id}
              className={`p-3 rounded border transition flex items-start justify-between gap-3 text-xs ${
                item.isRead
                  ? 'bg-white border-slate-200 opacity-85'
                  : 'bg-blue-50/50 border-blue-200 shadow-xs'
              }`}
            >
              <div className="flex items-start space-x-3">
                <div className="p-1.5 bg-white rounded border border-slate-200 shrink-0 text-slate-600 mt-0.5">
                  <Bell className="w-4 h-4" />
                </div>
                <div>
                  <div className="flex items-center gap-2">
                    <h3 className="font-bold text-slate-900">{item.title}</h3>
                    {!item.isRead && (
                      <span className="text-[10px] uppercase font-bold px-1.5 py-0.5 rounded bg-blue-900 text-white font-mono">
                        New
                      </span>
                    )}
                  </div>
                  <p className="text-slate-700 mt-0.5 leading-snug">{item.message}</p>
                  <p className="text-[10px] text-slate-400 font-mono mt-1">{formatDate(item.createdAt)}</p>
                </div>
              </div>

              {!item.isRead && (
                <button
                  onClick={() => handleMarkAsRead(item.id)}
                  className="shrink-0 px-2.5 py-1 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-100 border border-slate-300 rounded shadow-xs transition flex items-center gap-1"
                >
                  <Check className="w-3.5 h-3.5 text-slate-600" />
                  Mark Read
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
