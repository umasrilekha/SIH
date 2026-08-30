import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { govmeshService } from '../services/govmeshService';
import { IntegrationTransaction, CanonicalAddressUpdateRequest } from '../types/integration';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { StatusBadge } from '../components/common/StatusBadge';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { EmptyState } from '../components/common/EmptyState';
import { formatDate } from '../utils/formatters';
import { Network, Play, RefreshCw, Eye, CheckCircle2, XCircle, ArrowRight, ShieldCheck } from 'lucide-react';

export const IntegrationMonitorPage: React.FC = () => {
  const [transactions, setTransactions] = useState<IntegrationTransaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [simulating, setSimulating] = useState(false);
  const [simMsg, setSimMsg] = useState<{ type: 'success' | 'blocked' | 'error'; text: string } | null>(null);

  const fetchTransactions = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await govmeshService.getTransactions();
      setTransactions(data);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to fetch integration transactions.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTransactions();
  }, []);

  const handleSimulateRequest = async (scenario: 'valid' | 'expired' | 'revoked' | 'purpose' | 'field' | 'mismatch') => {
    setSimulating(true);
    setSimMsg(null);
    setError(null);

    let reqPayload: CanonicalAddressUpdateRequest;

    const baseCitizen = {
      reference: 'CIT-MH-998811',
      name: 'Rajesh Kumar',
      address: {
        line: '44 Example Road, Shivajinagar, Pune - 411005',
        district: 'DIST-PUN',
        taluka: 'TAL-PUN-04',
      },
    };

    const baseVerification = {
      status: 'VALID',
      source: 'REVENUE',
    };

    if (scenario === 'valid') {
      reqPayload = {
        applicationId: 'GM-2026-000124',
        sourceDepartment: 'REVENUE',
        targetDepartment: 'FOOD',
        correlationId: `REQ-2026-${Math.floor(100000 + Math.random() * 900000)}`,
        purpose: 'RATION_ADDRESS_UPDATE',
        requestedFields: ['citizen.name', 'citizen.address', 'citizen.address.district', 'citizen.address.taluka', 'verification.status'],
        citizen: baseCitizen,
        verification: baseVerification,
        consent: { id: 'CONSENT-00124' },
      };
    } else if (scenario === 'expired') {
      reqPayload = {
        applicationId: 'GM-2026-000124',
        sourceDepartment: 'REVENUE',
        targetDepartment: 'FOOD',
        correlationId: `REQ-2026-${Math.floor(100000 + Math.random() * 900000)}`,
        purpose: 'RATION_ADDRESS_UPDATE',
        requestedFields: ['citizen.name', 'citizen.address'],
        citizen: baseCitizen,
        verification: baseVerification,
        consent: { id: 'CONSENT-EXPIRED-001' },
      };
    } else if (scenario === 'revoked') {
      reqPayload = {
        applicationId: 'GM-2026-000124',
        sourceDepartment: 'REVENUE',
        targetDepartment: 'FOOD',
        correlationId: `REQ-2026-${Math.floor(100000 + Math.random() * 900000)}`,
        purpose: 'RATION_ADDRESS_UPDATE',
        requestedFields: ['citizen.name', 'citizen.address'],
        citizen: baseCitizen,
        verification: baseVerification,
        consent: { id: 'CONSENT-REVOKED-001' },
      };
    } else if (scenario === 'purpose') {
      reqPayload = {
        applicationId: 'GM-2026-000124',
        sourceDepartment: 'REVENUE',
        targetDepartment: 'FOOD',
        correlationId: `REQ-2026-${Math.floor(100000 + Math.random() * 900000)}`,
        purpose: 'LOAN_APPLICATION',
        requestedFields: ['citizen.name', 'citizen.address'],
        citizen: baseCitizen,
        verification: baseVerification,
        consent: { id: 'CONSENT-00124' },
      };
    } else if (scenario === 'field') {
      reqPayload = {
        applicationId: 'GM-2026-000124',
        sourceDepartment: 'REVENUE',
        targetDepartment: 'FOOD',
        correlationId: `REQ-2026-${Math.floor(100000 + Math.random() * 900000)}`,
        purpose: 'RATION_ADDRESS_UPDATE',
        requestedFields: ['citizen.name', 'citizen.address', 'citizen.phone'],
        citizen: baseCitizen,
        verification: baseVerification,
        consent: { id: 'CONSENT-00124' },
      };
    } else {
      // party mismatch
      reqPayload = {
        applicationId: 'GM-2026-000124',
        sourceDepartment: 'PANCHAYAT',
        targetDepartment: 'FOOD',
        correlationId: `REQ-2026-${Math.floor(100000 + Math.random() * 900000)}`,
        purpose: 'RATION_ADDRESS_UPDATE',
        requestedFields: ['citizen.name', 'citizen.address'],
        citizen: baseCitizen,
        verification: baseVerification,
        consent: { id: 'CONSENT-00124' },
      };
    }

    try {
      const res = await govmeshService.simulateInteroperability(reqPayload);
      if (res.status === 'BLOCKED') {
        setSimMsg({
          type: 'blocked',
          text: `Request BLOCKED by Consent Gatekeeper! Reason: ${res.errorCode || res.message}. Correlation ID: ${res.correlationId}`,
        });
      } else {
        setSimMsg({
          type: 'success',
          text: `Request ALLOWED & Processed via SOAP! Correlation ID: ${res.correlationId}`,
        });
      }
      await fetchTransactions();
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to execute simulated request.');
    } finally {
      setSimulating(false);
    }
  };

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb items={[{ label: 'System' }, { label: 'Interoperability Monitor' }]} />

      {/* Header Bar */}
      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-base font-extrabold text-blue-950 flex items-center gap-1.5">
              <Network className="w-5 h-5 text-blue-900" />
              GovMesh Interoperability Monitor
            </h1>
            <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-blue-100 text-blue-900 border border-blue-300 font-mono">
              REST ➔ CONSENT ➔ SOAP GATEWAY
            </span>
          </div>
          <p className="text-xs text-slate-600 mt-0.5">
            GovMesh inter-departmental data exchange monitor with integrated consent & data minimization gatekeeper.
          </p>
        </div>

        <button
          onClick={fetchTransactions}
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <RefreshCw className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Refresh
        </button>
      </div>

      {/* Interactive Simulation Scenarios Control Bar */}
      <div className="bg-white rounded border border-slate-300 shadow-xs p-3 space-y-2">
        <div className="flex items-center justify-between">
          <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide flex items-center gap-1.5">
            <ShieldCheck className="w-4 h-4 text-blue-900" />
            SIMULATE INTEROPERABILITY & CONSENT GATEKEEPER SCENARIOS
          </h2>
          <span className="text-[10px] text-slate-500 font-mono">Select a scenario to test gatekeeper enforcement</span>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-2">
          <button
            onClick={() => handleSimulateRequest('valid')}
            disabled={simulating}
            className="px-2.5 py-2 rounded text-[11px] font-bold bg-emerald-700 hover:bg-emerald-800 text-white flex items-center justify-center gap-1 transition shadow-xs"
          >
            <Play className="w-3 h-3 text-amber-300" />
            1. Valid Consent
          </button>

          <button
            onClick={() => handleSimulateRequest('expired')}
            disabled={simulating}
            className="px-2.5 py-2 rounded text-[11px] font-bold bg-amber-600 hover:bg-amber-700 text-white flex items-center justify-center gap-1 transition shadow-xs"
          >
            <Play className="w-3 h-3 text-white" />
            2. Expired Consent
          </button>

          <button
            onClick={() => handleSimulateRequest('revoked')}
            disabled={simulating}
            className="px-2.5 py-2 rounded text-[11px] font-bold bg-red-700 hover:bg-red-800 text-white flex items-center justify-center gap-1 transition shadow-xs"
          >
            <Play className="w-3 h-3 text-white" />
            3. Revoked Consent
          </button>

          <button
            onClick={() => handleSimulateRequest('purpose')}
            disabled={simulating}
            className="px-2.5 py-2 rounded text-[11px] font-bold bg-purple-700 hover:bg-purple-800 text-white flex items-center justify-center gap-1 transition shadow-xs"
          >
            <Play className="w-3 h-3 text-white" />
            4. Wrong Purpose
          </button>

          <button
            onClick={() => handleSimulateRequest('field')}
            disabled={simulating}
            className="px-2.5 py-2 rounded text-[11px] font-bold bg-slate-700 hover:bg-slate-800 text-white flex items-center justify-center gap-1 transition shadow-xs"
          >
            <Play className="w-3 h-3 text-white" />
            5. Field Violation
          </button>

          <button
            onClick={() => handleSimulateRequest('mismatch')}
            disabled={simulating}
            className="px-2.5 py-2 rounded text-[11px] font-bold bg-blue-900 hover:bg-blue-950 text-white flex items-center justify-center gap-1 transition shadow-xs"
          >
            <Play className="w-3 h-3 text-amber-400" />
            6. Party Mismatch
          </button>
        </div>
      </div>

      {simMsg && (
        <div
          className={`p-3 rounded text-xs flex items-center justify-between border ${
            simMsg.type === 'success'
              ? 'bg-emerald-50 border-emerald-300 text-emerald-900'
              : simMsg.type === 'blocked'
              ? 'bg-red-50 border-red-300 text-red-900'
              : 'bg-slate-100 border-slate-300 text-slate-900'
          }`}
        >
          <div className="flex items-center gap-2">
            {simMsg.type === 'success' ? (
              <CheckCircle2 className="w-4 h-4 text-emerald-700 shrink-0" />
            ) : (
              <XCircle className="w-4 h-4 text-red-700 shrink-0" />
            )}
            <span className="font-bold">{simMsg.text}</span>
          </div>
          <button onClick={() => setSimMsg(null)} className="font-bold text-xs underline">
            Dismiss
          </button>
        </div>
      )}

      {/* Service Connection Summary Table */}
      <div className="bg-white rounded border border-slate-300 shadow-xs p-3.5 space-y-2">
        <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
          SERVICE CONNECTION & GATEKEEPER ARCHITECTURE
        </h2>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs border-collapse">
            <thead>
              <tr className="bg-slate-100 text-slate-700 uppercase font-bold text-[10px] border-b border-slate-300">
                <th className="py-2 px-3 border-r border-slate-200">Department Platform / Node</th>
                <th className="py-2 px-3 border-r border-slate-200">Protocol / Format</th>
                <th className="py-2 px-3 border-r border-slate-200">Interoperability & Gatekeeper Role</th>
                <th className="py-2 px-3">Gateway Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200 text-slate-800 font-medium">
              <tr>
                <td className="py-2 px-3 font-bold text-slate-900 border-r border-slate-200">Revenue Department</td>
                <td className="py-2 px-3 font-mono border-r border-slate-200">REST / JSON</td>
                <td className="py-2 px-3 border-r border-slate-200 text-slate-600">Source Department</td>
                <td className="py-2 px-3">
                  <span className="inline-flex items-center text-emerald-800 font-semibold text-[11px]">
                    <CheckCircle2 className="w-3.5 h-3.5 mr-1 text-emerald-600" />
                    Connected
                  </span>
                </td>
              </tr>
              <tr>
                <td className="py-2 px-3 font-bold text-slate-900 border-r border-slate-200">GovMesh Gatekeeper Engine</td>
                <td className="py-2 px-3 font-mono border-r border-slate-200">Consent & Policy Validator</td>
                <td className="py-2 px-3 border-r border-slate-200 text-slate-600">Rules 1–6 Enforcement (BEFORE SOAP)</td>
                <td className="py-2 px-3">
                  <span className="inline-flex items-center text-blue-900 font-semibold text-[11px]">
                    <ShieldCheck className="w-3.5 h-3.5 mr-1 text-blue-700" />
                    Active Gatekeeper
                  </span>
                </td>
              </tr>
              <tr>
                <td className="py-2 px-3 font-bold text-slate-900 border-r border-slate-200">Food, Civil Supplies Dept (Dept 2)</td>
                <td className="py-2 px-3 font-mono border-r border-slate-200">SOAP / XML</td>
                <td className="py-2 px-3 border-r border-slate-200 text-slate-600">Target Department Adapter</td>
                <td className="py-2 px-3">
                  <span className="inline-flex items-center text-emerald-800 font-semibold text-[11px]">
                    <CheckCircle2 className="w-3.5 h-3.5 mr-1 text-emerald-600" />
                    Connected (/ws)
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      {/* Recent Integration Transactions Table */}
      {loading ? (
        <LoadingState message="Fetching GovMesh integration activity records..." />
      ) : error ? (
        <ErrorState message={error} onRetry={fetchTransactions} />
      ) : transactions.length === 0 ? (
        <EmptyState
          title="No Integration Transactions Recorded"
          description="Use scenario buttons above to simulate inter-departmental requests."
        />
      ) : (
        <div className="bg-white rounded border border-slate-300 shadow-xs overflow-hidden space-y-2">
          <div className="p-3 border-b border-slate-200 flex justify-between items-center bg-slate-50">
            <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
              RECENT INTEROPERABILITY TRANSACTIONS & CONSENT DECISIONS
            </h2>
            <span className="text-[10px] text-slate-500 font-mono">Total Traces: {transactions.length}</span>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-900 text-slate-200 uppercase font-bold tracking-wider border-b border-slate-800">
                  <th className="py-2.5 px-3 border-r border-slate-800">Correlation ID</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">App ID</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Source ➔ Target</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Consent Check</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Pipeline Status</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Started At</th>
                  <th className="py-2.5 px-3 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200">
                {transactions.map((tx) => (
                  <tr key={tx.id} className="hover:bg-slate-50 transition">
                    <td className="py-2 px-3 font-mono font-bold text-blue-950">{tx.correlationId}</td>
                    <td className="py-2 px-3 font-mono font-bold text-slate-900">{tx.applicationId}</td>
                    <td className="py-2 px-3 font-semibold text-slate-800">
                      {tx.sourceDepartment} <ArrowRight className="w-3 h-3 inline text-slate-400 mx-0.5" /> {tx.targetDepartment}
                    </td>
                    <td className="py-2 px-3">
                      {tx.status === 'BLOCKED' ? (
                        <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-red-100 text-red-800 border border-red-300 font-mono">
                          BLOCKED ({tx.consentFailureReason || tx.errorCode || 'DENIED'})
                        </span>
                      ) : (
                        <span className="px-2 py-0.5 rounded text-[10px] font-bold bg-emerald-100 text-emerald-800 border border-emerald-300 font-mono">
                          ✓ ALLOWED
                        </span>
                      )}
                    </td>
                    <td className="py-2 px-3">
                      <StatusBadge status={tx.status} />
                    </td>
                    <td className="py-2 px-3 text-slate-600 font-medium">{formatDate(tx.startedAt)}</td>
                    <td className="py-2 px-3 text-right">
                      <Link
                        to={`/integration/${tx.correlationId}`}
                        className="inline-flex items-center px-2 py-0.5 text-[11px] font-semibold text-blue-900 bg-blue-50 hover:bg-blue-100 border border-blue-300 rounded transition"
                      >
                        <Eye className="w-3 h-3 mr-1" />
                        View Trace
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
