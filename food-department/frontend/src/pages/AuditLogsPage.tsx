import React, { useState, useEffect } from 'react';
import { auditService } from '../services/auditService';
import { AuditLog } from '../types';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { StatusBadge } from '../components/common/StatusBadge';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { EmptyState } from '../components/common/EmptyState';
import { formatDate } from '../utils/formatters';
import { Search, Filter, RefreshCw } from 'lucide-react';

export const AuditLogsPage: React.FC = () => {
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [appId, setAppId] = useState('');
  const [action, setAction] = useState('');
  const [result, setResult] = useState('');

  const fetchLogs = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await auditService.getAuditLogs(appId, action, result);
      setLogs(data);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to fetch audit log trail.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLogs();
  }, [action, result]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    fetchLogs();
  };

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb items={[{ label: 'Officer Services' }, { label: 'Audit Logs' }]} />

      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <h1 className="text-base font-extrabold text-blue-950">Department System Audit Logs</h1>
          <p className="text-xs text-slate-600 mt-0.5">
            Immutable Officer Action Trail & Security Event Monitor (Phase 1 Baseline)
          </p>
        </div>
        <button
          onClick={fetchLogs}
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <RefreshCw className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Refresh Trail
        </button>
      </div>

      {/* Filter Bar */}
      <div className="bg-white p-3 rounded border border-slate-300 shadow-xs flex flex-col md:flex-row md:items-center justify-between gap-2">
        <form onSubmit={handleSearch} className="flex-1 flex items-center gap-2">
          <div className="relative flex-1">
            <Search className="w-4 h-4 absolute left-2.5 top-2 text-slate-400" />
            <input
              type="text"
              value={appId}
              onChange={(e) => setAppId(e.target.value)}
              placeholder="Search Application ID (e.g. GM-2026-000124)..."
              className="w-full pl-8 pr-3 py-1.5 text-xs border border-slate-300 rounded bg-slate-50 focus:outline-none focus:ring-2 focus:ring-blue-900"
            />
          </div>
          <button
            type="submit"
            className="px-3 py-1.5 text-xs font-bold text-white bg-slate-900 hover:bg-slate-800 rounded transition"
          >
            Search
          </button>
        </form>

        <div className="flex items-center gap-2 shrink-0">
          <div className="flex items-center gap-1 text-slate-500 font-medium">
            <Filter className="w-3.5 h-3.5" />
            <span>Action:</span>
          </div>

          <select
            value={action}
            onChange={(e) => setAction(e.target.value)}
            className="px-2 py-1.5 border border-slate-300 rounded bg-white text-slate-800 font-medium focus:outline-none focus:ring-2 focus:ring-blue-900"
          >
            <option value="">All Actions</option>
            <option value="USER_LOGIN">USER_LOGIN</option>
            <option value="VIEW_APPLICATION">VIEW_APPLICATION</option>
            <option value="STATUS_UPDATE">STATUS_UPDATE</option>
            <option value="RECORD_VERIFICATION">RECORD_VERIFICATION</option>
            <option value="SYSTEM_VALIDATION">SYSTEM_VALIDATION</option>
          </select>

          <select
            value={result}
            onChange={(e) => setResult(e.target.value)}
            className="px-2 py-1.5 border border-slate-300 rounded bg-white text-slate-800 font-medium focus:outline-none focus:ring-2 focus:ring-blue-900"
          >
            <option value="">All Results</option>
            <option value="SUCCESS">SUCCESS</option>
            <option value="FAILED">FAILED</option>
          </select>
        </div>
      </div>

      {/* Audit Trail Table */}
      {loading ? (
        <LoadingState message="Fetching audit logs from database..." />
      ) : error ? (
        <ErrorState message={error} onRetry={fetchLogs} />
      ) : logs.length === 0 ? (
        <EmptyState title="No Audit Logs Found" description="No audit trail entries match your filter criteria." />
      ) : (
        <div className="bg-white rounded border border-slate-300 shadow-xs overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-900 text-slate-200 uppercase font-bold tracking-wider border-b border-slate-800">
                  <th className="py-2.5 px-3 border-r border-slate-800">Timestamp</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Application ID</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Officer</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Action</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Result</th>
                  <th className="py-2.5 px-3">Description</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200">
                {logs.map((log) => (
                  <tr key={log.id} className="hover:bg-slate-50 transition">
                    <td className="py-2 px-3 text-slate-500 whitespace-nowrap font-mono">{formatDate(log.timestamp)}</td>
                    <td className="py-2 px-3 font-mono font-bold text-slate-900">{log.applicationId || '—'}</td>
                    <td className="py-2 px-3 text-slate-800 font-semibold">{log.officerName}</td>
                    <td className="py-2 px-3 font-mono font-bold text-slate-800">{log.action}</td>
                    <td className="py-2 px-3">
                      <StatusBadge status={log.result} />
                    </td>
                    <td className="py-2 px-3 text-slate-700 max-w-xs leading-snug">{log.description}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};
