import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { consentService } from '../services/consentService';
import { ConsentRecord } from '../types/consent';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { EmptyState } from '../components/common/EmptyState';
import { formatDate } from '../utils/formatters';
import { ShieldCheck, Eye, RefreshCw } from 'lucide-react';

export const ConsentListPage: React.FC = () => {
  const [consents, setConsents] = useState<ConsentRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchConsents = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await consentService.getConsents();
      setConsents(data);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to fetch consent records.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchConsents();
  }, []);

  const getStatusBadge = (status: string) => {
    const s = status ? status.toUpperCase() : 'UNKNOWN';
    if (s === 'ACTIVE') {
      return (
        <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-emerald-100 text-emerald-800 border border-emerald-300">
          ACTIVE
        </span>
      );
    } else if (s === 'EXPIRED') {
      return (
        <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-amber-100 text-amber-800 border border-amber-300">
          EXPIRED
        </span>
      );
    } else if (s === 'REVOKED') {
      return (
        <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-red-100 text-red-800 border border-red-300">
          REVOKED
        </span>
      );
    } else {
      return (
        <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-slate-100 text-slate-800 border border-slate-300">
          {s}
        </span>
      );
    }
  };

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb items={[{ label: 'System' }, { label: 'Consent & Data Access' }]} />

      {/* Header Bar */}
      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-base font-extrabold text-blue-950 flex items-center gap-1.5">
              <ShieldCheck className="w-5 h-5 text-blue-900" />
              Consent & Data Access Management
            </h1>
            <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-blue-100 text-blue-900 border border-blue-300 font-mono">
              GOVMESH GATEKEEPER
            </span>
          </div>
          <p className="text-xs text-slate-600 mt-0.5">
            Verified citizen consent registries and inter-departmental data access governance policies.
          </p>
        </div>

        <button
          onClick={fetchConsents}
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <RefreshCw className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Refresh Registry
        </button>
      </div>

      {/* Structured Government-Style Consent Table */}
      {loading ? (
        <LoadingState message="Fetching registered consent records from GovMesh gatekeeper..." />
      ) : error ? (
        <ErrorState message={error} onRetry={fetchConsents} />
      ) : consents.length === 0 ? (
        <EmptyState
          title="No Consent Records Found"
          description="No consent records are registered in the system database."
        />
      ) : (
        <div className="bg-white rounded border border-slate-300 shadow-xs overflow-hidden space-y-2">
          <div className="p-3 border-b border-slate-200 flex justify-between items-center bg-slate-50">
            <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
              CITIZEN CONSENT REGISTRY RECORDS
            </h2>
            <span className="text-[10px] text-slate-500 font-mono">Total Consents: {consents.length}</span>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-900 text-slate-200 uppercase font-bold tracking-wider border-b border-slate-800">
                  <th className="py-2.5 px-3 border-r border-slate-800">Consent ID</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Citizen Reference</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Requesting Dept</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Receiving Dept</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Purpose</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Status</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Expires</th>
                  <th className="py-2.5 px-3 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200">
                {consents.map((consent) => (
                  <tr key={consent.id} className="hover:bg-slate-50 transition">
                    <td className="py-2.5 px-3 font-mono font-bold text-blue-950">{consent.consentId}</td>
                    <td className="py-2.5 px-3 font-mono font-bold text-slate-900">{consent.citizenReference}</td>
                    <td className="py-2.5 px-3 font-semibold text-slate-800">{consent.requestingDepartment}</td>
                    <td className="py-2.5 px-3 font-semibold text-slate-800">{consent.receivingDepartment}</td>
                    <td className="py-2.5 px-3 font-mono text-[11px] text-slate-700">{consent.purpose}</td>
                    <td className="py-2.5 px-3">{getStatusBadge(consent.status)}</td>
                    <td className="py-2.5 px-3 text-slate-600 font-medium">{formatDate(consent.expiresAt)}</td>
                    <td className="py-2.5 px-3 text-right">
                      <Link
                        to={`/consent/${consent.consentId}`}
                        className="inline-flex items-center px-2 py-0.5 text-[11px] font-semibold text-blue-900 bg-blue-50 hover:bg-blue-100 border border-blue-300 rounded transition"
                      >
                        <Eye className="w-3 h-3 mr-1" />
                        View Details
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
