import React, { useState, useEffect } from 'react';
import { applicationService } from '../services/applicationService';
import { Application } from '../types';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { StatusBadge } from '../components/common/StatusBadge';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { EmptyState } from '../components/common/EmptyState';
import { formatDate } from '../utils/formatters';
import { Link } from 'react-router-dom';
import { Search, Filter, RefreshCw, Eye } from 'lucide-react';

export const RequestsPage: React.FC = () => {
  const [applications, setApplications] = useState<Application[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [typeFilter, setTypeFilter] = useState('');

  const fetchRequests = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await applicationService.getApplications(searchQuery, statusFilter, typeFilter);
      setApplications(data);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to fetch incoming service requests.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRequests();
  }, [statusFilter, typeFilter]);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    fetchRequests();
  };

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb items={[{ label: 'Officer Services' }, { label: 'Incoming Requests' }]} />

      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <h1 className="text-base font-extrabold text-blue-950">Incoming Requests Queue</h1>
          <p className="text-xs text-slate-600 mt-0.5">
            Interoperability Requests from External Department Systems (Revenue / Land Records)
          </p>
        </div>
        <button
          onClick={fetchRequests}
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <RefreshCw className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Refresh Queue
        </button>
      </div>

      {/* Administrative Filter Bar */}
      <div className="bg-white p-3 rounded border border-slate-300 shadow-xs flex flex-col md:flex-row md:items-center justify-between gap-2">
        <form onSubmit={handleSearchSubmit} className="flex-1 flex items-center gap-2">
          <div className="relative flex-1">
            <Search className="w-4 h-4 absolute left-2.5 top-2 text-slate-400" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search Application ID, Citizen Ref, or Ration Card No..."
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
            <span>Status:</span>
          </div>

          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="px-2 py-1.5 border border-slate-300 rounded bg-white text-slate-800 font-medium focus:outline-none focus:ring-2 focus:ring-blue-900"
          >
            <option value="">All Statuses</option>
            <option value="PENDING">PENDING</option>
            <option value="PROCESSING">PROCESSING</option>
            <option value="COMPLETED">COMPLETED</option>
            <option value="REJECTED">REJECTED</option>
            <option value="FAILED">FAILED</option>
          </select>

          <select
            value={typeFilter}
            onChange={(e) => setTypeFilter(e.target.value)}
            className="px-2 py-1.5 border border-slate-300 rounded bg-white text-slate-800 font-medium focus:outline-none focus:ring-2 focus:ring-blue-900"
          >
            <option value="">All Types</option>
            <option value="ADDRESS_UPDATE">ADDRESS_UPDATE</option>
            <option value="MEMBER_ADDITION">MEMBER_ADDITION</option>
            <option value="CARD_CATEGORY_CHANGE">CARD_CATEGORY_CHANGE</option>
            <option value="CARD_TRANSFER">CARD_TRANSFER</option>
          </select>
        </div>
      </div>

      {/* Queue Data Table */}
      {loading ? (
        <LoadingState message="Loading incoming requests queue..." />
      ) : error ? (
        <ErrorState message={error} onRetry={fetchRequests} />
      ) : applications.length === 0 ? (
        <EmptyState title="No Applications Found" description="No request payloads match your active filters." />
      ) : (
        <div className="bg-white rounded border border-slate-300 shadow-xs overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-900 text-slate-200 font-bold uppercase tracking-wider border-b border-slate-800">
                  <th className="py-2.5 px-3 border-r border-slate-800">Application ID</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Citizen Ref</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Ration Card No.</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Application Type</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Source Dept</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Status</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Received On</th>
                  <th className="py-2.5 px-3 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200">
                {applications.map((app) => (
                  <tr key={app.id} className="hover:bg-slate-50 transition">
                    <td className="py-2 px-3">
                      <div className="font-mono font-bold text-slate-900">{app.applicationId}</div>
                      {app.correlationId && (
                        <div className="text-[10px] text-blue-700 font-mono mt-0.5 flex items-center gap-1">
                          <span className="px-1 py-0.2 bg-blue-50 border border-blue-200 rounded text-[9px] font-semibold">GovMesh</span>
                          <span className="truncate max-w-[130px]">{app.correlationId}</span>
                        </div>
                      )}
                    </td>
                    <td className="py-2 px-3 font-mono text-slate-600">{app.citizenReference}</td>
                    <td className="py-2 px-3 font-mono text-slate-800 font-semibold">{app.rationCardNo}</td>
                    <td className="py-2 px-3 text-slate-700 font-medium">{app.applicationType.replace(/_/g, ' ')}</td>
                    <td className="py-2 px-3">
                      <span className="inline-flex items-center px-1.5 py-0.5 rounded text-[10px] font-bold bg-slate-100 text-slate-800 border border-slate-300">
                        {app.sourceDepartment}
                      </span>
                    </td>
                    <td className="py-2 px-3">
                      <StatusBadge status={app.currentStatus} />
                    </td>
                    <td className="py-2 px-3 text-slate-500 whitespace-nowrap">
                      <div>{formatDate(app.receivedAt || app.createdAt)}</div>
                      {app.receivedAt && (
                        <div className="text-[9px] font-mono text-emerald-700">✓ Auth Ingress</div>
                      )}
                    </td>
                    <td className="py-2 px-3 text-right">
                      <Link
                        to={`/applications/${app.id}`}
                        className="inline-flex items-center px-2 py-0.5 text-[11px] font-semibold text-slate-800 bg-slate-100 hover:bg-slate-200 border border-slate-300 rounded transition"
                      >
                        <Eye className="w-3 h-3 mr-1 text-slate-600" />
                        Inspect
                      </Link>
                    </td>
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
