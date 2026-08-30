import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { consentService } from '../services/consentService';
import { ConsentDetail } from '../types/consent';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { formatDate } from '../utils/formatters';
import { ArrowLeft, ShieldCheck, CheckCircle2, XCircle, Clock, Building2, User, FileText } from 'lucide-react';

export const ConsentDetailPage: React.FC = () => {
  const { consentId } = useParams<{ consentId: string }>();
  const [detail, setDetail] = useState<ConsentDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDetail = async () => {
      if (!consentId) return;
      setLoading(true);
      setError(null);
      try {
        const data = await consentService.getConsentById(consentId);
        setDetail(data);
      } catch (err: any) {
        setError(err?.response?.data?.message || 'Failed to fetch consent record details.');
      } finally {
        setLoading(false);
      }
    };
    fetchDetail();
  }, [consentId]);

  if (loading) return <LoadingState message="Loading consent record details..." />;
  if (error || !detail) return <ErrorState message={error || 'Consent record not found'} />;

  const { consent, permittedData, restrictedData } = detail;
  const status = consent.status ? consent.status.toUpperCase() : 'UNKNOWN';

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb
        items={[
          { label: 'System' },
          { label: 'Consent & Data Access', to: '/consent' },
          { label: `Consent ${consent.consentId}` },
        ]}
      />

      {/* Header Bar */}
      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-base font-extrabold text-blue-950 flex items-center gap-1.5">
              <ShieldCheck className="w-5 h-5 text-blue-900" />
              CONSENT SPECIFICATION & POLICY AUDIT
            </h1>
            {status === 'ACTIVE' && (
              <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-emerald-100 text-emerald-800 border border-emerald-300">
                ACTIVE
              </span>
            )}
            {status === 'EXPIRED' && (
              <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-amber-100 text-amber-800 border border-amber-300">
                EXPIRED
              </span>
            )}
            {status === 'REVOKED' && (
              <span className="px-2 py-0.5 rounded text-[10px] font-bold uppercase bg-red-100 text-red-800 border border-red-300">
                REVOKED
              </span>
            )}
          </div>
          <p className="text-xs text-slate-600 mt-0.5 font-mono">
            Consent ID: <strong className="text-slate-900 font-bold">{consent.consentId}</strong> • Citizen Ref: <strong className="text-slate-900 font-bold">{consent.citizenReference}</strong>
          </p>
        </div>

        <Link
          to="/consent"
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <ArrowLeft className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Back to Consent List
        </Link>
      </div>

      {/* Structured Details Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        {/* Left Column: Metadata */}
        <div className="bg-white rounded border border-slate-300 shadow-xs p-4 space-y-3">
          <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide border-b border-slate-200 pb-2 flex items-center gap-1.5">
            <FileText className="w-4 h-4 text-blue-900" />
            CONSENT METADATA & REPOSITORY RECORD
          </h2>

          <div className="space-y-2.5 divide-y divide-slate-100 text-xs">
            <div className="flex justify-between items-center pt-1">
              <span className="text-slate-600 font-medium flex items-center gap-1">
                <ShieldCheck className="w-3.5 h-3.5 text-slate-400" /> Consent ID:
              </span>
              <span className="font-mono font-bold text-blue-950">{consent.consentId}</span>
            </div>

            <div className="flex justify-between items-center pt-2">
              <span className="text-slate-600 font-medium flex items-center gap-1">
                <User className="w-3.5 h-3.5 text-slate-400" /> Citizen Reference:
              </span>
              <span className="font-mono font-bold text-slate-900">{consent.citizenReference}</span>
            </div>

            <div className="flex justify-between items-center pt-2">
              <span className="text-slate-600 font-medium flex items-center gap-1">
                <Building2 className="w-3.5 h-3.5 text-slate-400" /> Requesting Department:
              </span>
              <span className="font-bold text-slate-900">{consent.requestingDepartment}</span>
            </div>

            <div className="flex justify-between items-center pt-2">
              <span className="text-slate-600 font-medium flex items-center gap-1">
                <Building2 className="w-3.5 h-3.5 text-slate-400" /> Receiving Department:
              </span>
              <span className="font-bold text-slate-900">{consent.receivingDepartment}</span>
            </div>

            <div className="flex justify-between items-center pt-2">
              <span className="text-slate-600 font-medium flex items-center gap-1">
                <FileText className="w-3.5 h-3.5 text-slate-400" /> Purpose Specification:
              </span>
              <span className="font-mono font-bold text-slate-900">{consent.purpose}</span>
            </div>

            <div className="flex justify-between items-center pt-2">
              <span className="text-slate-600 font-medium flex items-center gap-1">
                <Clock className="w-3.5 h-3.5 text-slate-400" /> Issued Timestamp:
              </span>
              <span className="font-medium text-slate-800">{formatDate(consent.issuedAt)}</span>
            </div>

            <div className="flex justify-between items-center pt-2">
              <span className="text-slate-600 font-medium flex items-center gap-1">
                <Clock className="w-3.5 h-3.5 text-slate-400" /> Expiry Timestamp:
              </span>
              <span className="font-medium text-slate-800">{formatDate(consent.expiresAt)}</span>
            </div>

            {consent.revokedAt && (
              <div className="flex justify-between items-center pt-2 text-red-700">
                <span className="font-medium flex items-center gap-1">
                  <XCircle className="w-3.5 h-3.5 text-red-600" /> Revocation Timestamp:
                </span>
                <span className="font-bold">{formatDate(consent.revokedAt)}</span>
              </div>
            )}
          </div>
        </div>

        {/* Right Column: Data Minimization Policy (Permitted vs Restricted) */}
        <div className="space-y-4">
          {/* Permitted Data Box */}
          <div className="bg-emerald-50/60 rounded border border-emerald-300 shadow-xs p-4 space-y-2.5">
            <h3 className="text-xs font-bold text-emerald-950 uppercase tracking-wide flex items-center gap-1.5 border-b border-emerald-200 pb-2">
              <CheckCircle2 className="w-4 h-4 text-emerald-700" />
              PERMITTED DATA FIELDS (ALLOWED BY PURPOSE)
            </h3>
            <p className="text-[11px] text-emerald-900">
              Only these specific fields may be transferred under purpose <strong className="font-mono">{consent.purpose}</strong>:
            </p>
            <ul className="space-y-1.5 font-medium text-xs text-emerald-950">
              {permittedData && permittedData.length > 0 ? (
                permittedData.map((field) => (
                  <li key={field} className="flex items-center gap-2 bg-white px-2.5 py-1.5 rounded border border-emerald-200 font-mono">
                    <CheckCircle2 className="w-3.5 h-3.5 text-emerald-600 shrink-0" />
                    <span>{field}</span>
                  </li>
                ))
              ) : (
                <li className="flex items-center gap-2 bg-white px-2 py-1 rounded border border-emerald-200 font-mono">
                  <CheckCircle2 className="w-3.5 h-3.5 text-emerald-600 shrink-0" />
                  <span>citizen.name</span>
                </li>
              )}
            </ul>
          </div>

          {/* Restricted Data Box */}
          <div className="bg-red-50/60 rounded border border-red-300 shadow-xs p-4 space-y-2.5">
            <h3 className="text-xs font-bold text-red-950 uppercase tracking-wide flex items-center gap-1.5 border-b border-red-200 pb-2">
              <XCircle className="w-4 h-4 text-red-700" />
              RESTRICTED / MINIMIZED DATA FIELDS (DENIED)
            </h3>
            <p className="text-[11px] text-red-900">
              GovMesh strictly blocks transfer of non-permitted citizen fields:
            </p>
            <ul className="space-y-1.5 font-medium text-xs text-red-950">
              {restrictedData && restrictedData.length > 0 ? (
                restrictedData.map((field) => (
                  <li key={field} className="flex items-center gap-2 bg-white px-2.5 py-1.5 rounded border border-red-200 font-mono text-red-900">
                    <XCircle className="w-3.5 h-3.5 text-red-600 shrink-0" />
                    <span>{field}</span>
                  </li>
                ))
              ) : (
                <li className="flex items-center gap-2 bg-white px-2 py-1 rounded border border-red-200 font-mono text-red-900">
                  <XCircle className="w-3.5 h-3.5 text-red-600 shrink-0" />
                  <span>citizen.phone</span>
                </li>
              )}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};
