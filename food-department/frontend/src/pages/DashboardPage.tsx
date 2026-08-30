import React, { useState, useEffect } from 'react';
import { dashboardService } from '../services/dashboardService';
import { Application, DashboardSummary, RecentActivity } from '../types';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { StatusBadge } from '../components/common/StatusBadge';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { formatDate } from '../utils/formatters';
import { Link } from 'react-router-dom';
import { RefreshCw, ArrowRight } from 'lucide-react';

export const DashboardPage: React.FC = () => {
  const [summary, setSummary] = useState<DashboardSummary | null>(null);
  const [recentApps, setRecentApps] = useState<Application[]>([]);
  const [recentActivities, setRecentActivities] = useState<RecentActivity[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchDashboardData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [sumData, appsData, actData] = await Promise.all([
        dashboardService.getSummary(),
        dashboardService.getRecentApplications(),
        dashboardService.getRecentActivities(),
      ]);
      setSummary(sumData);
      setRecentApps(appsData);
      setRecentActivities(actData);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to connect to department backend service.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDashboardData();
  }, []);

  if (loading) return <LoadingState message="Loading department operations summary..." />;
  if (error) return <ErrorState message={error} onRetry={fetchDashboardData} />;

  return (
    <div className="space-y-4 text-xs">
      {/* Government Breadcrumb Navigation */}
      <Breadcrumb items={[{ label: 'Officer Services' }, { label: 'Dashboard' }]} />

      {/* Page Title & Subtitle */}
      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <h1 className="text-base font-extrabold text-blue-950">Officer Dashboard</h1>
          <p className="text-xs text-slate-600 mt-0.5">
            Food, Civil Supplies & Consumer Protection Department — Department 2 Operations Control
          </p>
        </div>
        <button
          onClick={fetchDashboardData}
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <RefreshCw className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Refresh Summary
        </button>
      </div>

      {/* Application Summary Grid */}
      <div className="bg-white rounded border border-slate-300 shadow-xs p-3">
        <div className="text-[11px] font-bold text-slate-800 uppercase tracking-wider mb-2 pb-1 border-b border-slate-200">
          APPLICATION SUMMARY
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-2 text-center">
          <div className="p-2 bg-slate-50 border border-slate-200 rounded">
            <span className="text-[10px] font-semibold text-slate-500 uppercase block">Incoming Total</span>
            <span className="text-lg font-bold text-slate-900">{summary?.totalIncomingRequests ?? 0}</span>
          </div>

          <div className="p-2 bg-amber-50/60 border border-amber-200 rounded">
            <span className="text-[10px] font-semibold text-amber-800 uppercase block">Pending</span>
            <span className="text-lg font-bold text-amber-900">{summary?.pendingCount ?? 0}</span>
          </div>

          <div className="p-2 bg-blue-50/60 border border-blue-200 rounded">
            <span className="text-[10px] font-semibold text-blue-800 uppercase block">Processing</span>
            <span className="text-lg font-bold text-blue-900">{summary?.processingCount ?? 0}</span>
          </div>

          <div className="p-2 bg-emerald-50/60 border border-emerald-200 rounded">
            <span className="text-[10px] font-semibold text-emerald-800 uppercase block">Completed</span>
            <span className="text-lg font-bold text-emerald-900">{summary?.completedCount ?? 0}</span>
          </div>

          <div className="p-2 bg-rose-50/60 border border-rose-200 rounded">
            <span className="text-[10px] font-semibold text-rose-800 uppercase block">Rejected</span>
            <span className="text-lg font-bold text-rose-900">{summary?.rejectedCount ?? 0}</span>
          </div>

          <div className="p-2 bg-slate-100 border border-slate-300 rounded">
            <span className="text-[10px] font-semibold text-slate-600 uppercase block">Failed</span>
            <span className="text-lg font-bold text-slate-900">{summary?.failedCount ?? 0}</span>
          </div>
        </div>
      </div>

      {/* Main Grid: Recent Applications + Service Status */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Left 2 Cols: Recent Applications Table */}
        <div className="lg:col-span-2 space-y-4">
          <div className="bg-white rounded border border-slate-300 shadow-xs p-3.5">
            <div className="flex items-center justify-between border-b border-slate-200 pb-2 mb-2.5">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
                RECENT APPLICATIONS (LATEST 5)
              </h2>
              <Link to="/requests" className="text-xs font-semibold text-blue-900 hover:underline flex items-center gap-1">
                <span>Full Queue</span>
                <ArrowRight className="w-3 h-3" />
              </Link>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs border-collapse">
                <thead>
                  <tr className="bg-slate-100 text-slate-700 font-bold border-b border-slate-300">
                    <th className="py-2 px-2.5 border-r border-slate-200">Application ID</th>
                    <th className="py-2 px-2.5 border-r border-slate-200">Type</th>
                    <th className="py-2 px-2.5 border-r border-slate-200">Ration Card No.</th>
                    <th className="py-2 px-2.5 border-r border-slate-200">Source Dept</th>
                    <th className="py-2 px-2.5 border-r border-slate-200">Status</th>
                    <th className="py-2 px-2.5 text-right">Action</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-200">
                  {recentApps.map((app) => (
                    <tr key={app.id} className="hover:bg-slate-50 transition">
                      <td className="py-2 px-2.5 font-mono font-bold text-slate-900">{app.applicationId}</td>
                      <td className="py-2 px-2.5 text-slate-700">{app.applicationType.replace(/_/g, ' ')}</td>
                      <td className="py-2 px-2.5 font-mono text-slate-800">{app.rationCardNo}</td>
                      <td className="py-2 px-2.5 text-slate-700 font-semibold">{app.sourceDepartment}</td>
                      <td className="py-2 px-2.5">
                        <StatusBadge status={app.currentStatus} />
                      </td>
                      <td className="py-2 px-2.5 text-right">
                        <Link
                          to={`/applications/${app.id}`}
                          className="px-2 py-0.5 text-[11px] font-semibold text-slate-800 bg-slate-100 hover:bg-slate-200 border border-slate-300 rounded transition"
                        >
                          Inspect
                        </Link>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Department Service Status Table */}
          <div className="bg-white rounded border border-slate-300 shadow-xs p-3.5">
            <div className="border-b border-slate-200 pb-2 mb-2.5">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
                DEPARTMENT SERVICE STATUS
              </h2>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-left text-xs border-collapse">
                <thead>
                  <tr className="bg-slate-100 text-slate-700 font-bold border-b border-slate-300">
                    <th className="py-2 px-3 border-r border-slate-200">Service</th>
                    <th className="py-2 px-3 border-r border-slate-200">Specification</th>
                    <th className="py-2 px-3 text-right">Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-200">
                  {summary?.serviceStatus.map((item, idx) => (
                    <tr key={idx} className="hover:bg-slate-50 transition">
                      <td className="py-2 px-3 font-semibold text-slate-900">{item.name}</td>
                      <td className="py-2 px-3 text-slate-600">{item.note}</td>
                      <td className="py-2 px-3 text-right">
                        <StatusBadge status={item.status} />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {/* Right Col: Recent Activity List */}
        <div className="space-y-4">
          <div className="bg-white rounded border border-slate-300 shadow-xs p-3.5">
            <div className="flex items-center justify-between border-b border-slate-200 pb-2 mb-2.5">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
                RECENT ACTIVITY
              </h2>
              <Link to="/audit-logs" className="text-[11px] font-semibold text-slate-500 hover:text-slate-900">
                Audit Log
              </Link>
            </div>

            <div className="space-y-2">
              {recentActivities.map((act) => (
                <div key={act.id} className="p-2 bg-slate-50 border border-slate-200 rounded text-xs space-y-1">
                  <div className="flex items-center justify-between text-[11px]">
                    <span className="font-bold text-slate-800">{act.action}</span>
                    <span className="text-slate-400 font-mono text-[10px]">{act.timeAgo}</span>
                  </div>
                  <p className="text-slate-700 leading-tight text-[11px]">{act.description}</p>
                  <div className="text-[10px] text-slate-500 pt-1 border-t border-slate-200 flex justify-between font-mono">
                    <span>{act.officerName}</span>
                    <span className="font-semibold text-slate-700">{act.result}</span>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
