import React, { useState, useEffect } from 'react';
import { healthService } from '../services/healthService';
import { SystemHealth } from '../types';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { StatusBadge } from '../components/common/StatusBadge';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';

export const SystemHealthPage: React.FC = () => {
  const [health, setHealth] = useState<SystemHealth | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchHealth = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await healthService.getSystemHealth();
      setHealth(data);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to connect to department health service.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchHealth();
  }, []);

  if (loading) return <LoadingState message="Scanning infrastructure services..." />;
  if (error || !health) return <ErrorState message={error || 'System health data unavailable'} onRetry={fetchHealth} />;

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb items={[{ label: 'Officer Services' }, { label: 'System Diagnostics' }]} />

      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <h1 className="text-base font-extrabold text-blue-950">Department System Diagnostics</h1>
          <p className="text-xs text-slate-600 mt-0.5">
            Component Integration & Infrastructure Status Monitor (Phase 1 Baseline)
          </p>
        </div>
      </div>

      {/* Structured Health Table */}
      <div className="bg-white rounded border border-slate-300 shadow-xs overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-slate-900 text-slate-200 uppercase font-bold tracking-wider border-b border-slate-800">
                <th className="py-2.5 px-3 border-r border-slate-800">Component Name</th>
                <th className="py-2.5 px-3 border-r border-slate-800">Category</th>
                <th className="py-2.5 px-3 border-r border-slate-800">Diagnostic Specification</th>
                <th className="py-2.5 px-3 border-r border-slate-800">Configured</th>
                <th className="py-2.5 px-3 text-right">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {health.components.map((comp, idx) => (
                <tr key={idx} className="hover:bg-slate-50 transition">
                  <td className="py-2 px-3 font-semibold text-slate-900">{comp.name}</td>
                  <td className="py-2 px-3 text-slate-600 font-mono">{comp.category}</td>
                  <td className="py-2 px-3 text-slate-700">{comp.message}</td>
                  <td className="py-2 px-3 text-slate-700 font-mono font-semibold">
                    {comp.isConfigured ? 'YES' : 'NO (Phase 3)'}
                  </td>
                  <td className="py-2 px-3 text-right">
                    <StatusBadge status={comp.status} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Institutional Boundary Clarification Note */}
      <div className="bg-slate-900 text-white p-3.5 rounded border border-slate-800 space-y-1">
        <div className="font-bold text-amber-400 uppercase text-[11px]">
          INSTITUTIONAL INTEGRATION ROADMAP NOTE
        </div>
        <p className="leading-relaxed text-[11px] text-slate-300">
          In Phase 1, Department 2 operates standard <strong>REST / JSON</strong> endpoints. SOAP/XML WSDL interfaces are explicitly <strong>NOT CONFIGURED</strong> in Phase 1 and will be introduced in Phase 3.
        </p>
      </div>
    </div>
  );
};
