import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { rationService } from '../services/rationService';
import { Application, RationRecord } from '../types';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { StatusBadge } from '../components/common/StatusBadge';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { formatDate } from '../utils/formatters';
import { ArrowLeft, UserCheck, FileText, CheckCircle, AlertCircle, Eye } from 'lucide-react';

export const RationRecordDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();

  const [record, setRecord] = useState<RationRecord | null>(null);
  const [apps, setApps] = useState<Application[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      if (!id) return;
      setLoading(true);
      setError(null);
      try {
        const [recData, appsData] = await Promise.all([
          rationService.getRationRecordById(Number(id)),
          rationService.getRationRecordApplications(Number(id)),
        ]);
        setRecord(recData);
        setApps(appsData);
      } catch (err: any) {
        setError(err?.response?.data?.message || 'Failed to retrieve ration record details.');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  if (loading) return <LoadingState message="Loading master ration card record..." />;
  if (error || !record) return <ErrorState message={error || 'Ration record not found'} />;

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb
        items={[
          { label: 'Officer Services' },
          { label: 'Ration Records', to: '/ration-records' },
          { label: `Card ${record.rationCardNo}` },
        ]}
      />

      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-base font-extrabold text-blue-950">
              Ration Card Record: {record.rationCardNo}
            </h1>
            <StatusBadge status={record.updateStatus} />
          </div>
          <p className="text-xs text-slate-600 mt-0.5">
            Department 2 Master Card Specification — Food, Civil Supplies & Consumer Protection Department
          </p>
        </div>

        <Link
          to="/ration-records"
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <ArrowLeft className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Back to Records
        </Link>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Left 2 Cols: Master Card Specification */}
        <div className="lg:col-span-2 space-y-4">
          <div className="bg-white rounded border border-slate-300 shadow-xs p-4 space-y-4">
            <div className="border-b border-slate-200 pb-2 flex items-center justify-between">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide flex items-center gap-1.5">
                <UserCheck className="w-4 h-4 text-slate-700" />
                <span>MASTER CARD SPECIFICATION</span>
              </h2>
              {record.verificationFlag ? (
                <span className="inline-flex items-center text-emerald-800 font-semibold text-[11px]">
                  <CheckCircle className="w-3.5 h-3.5 mr-1 text-emerald-600" />
                  Verified Record
                </span>
              ) : (
                <span className="inline-flex items-center text-amber-800 font-semibold text-[11px]">
                  <AlertCircle className="w-3.5 h-3.5 mr-1 text-amber-600" />
                  Pending Verification
                </span>
              )}
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Ration Card Number</span>
                <span className="font-mono font-bold text-slate-900 text-xs mt-0.5 block">{record.rationCardNo}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Holder Name</span>
                <span className="font-bold text-slate-900 text-xs mt-0.5 block">{record.holderName}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">District Code</span>
                <span className="font-mono font-bold text-slate-900 text-xs mt-0.5 block">{record.districtCode}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Taluka Code</span>
                <span className="font-mono font-bold text-slate-900 text-xs mt-0.5 block">{record.talukaCode}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200 sm:col-span-2">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Registered House Address</span>
                <span className="font-semibold text-slate-900 text-xs mt-0.5 block leading-relaxed">{record.houseAddress}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Record Initialized On</span>
                <span className="font-medium text-slate-800 text-xs mt-0.5 block">{formatDate(record.createdAt)}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Last Record Update</span>
                <span className="font-medium text-slate-800 text-xs mt-0.5 block">{formatDate(record.updatedAt)}</span>
              </div>
            </div>
          </div>
        </div>

        {/* Right Col: Linked Application History */}
        <div className="space-y-4">
          <div className="bg-white rounded border border-slate-300 shadow-xs p-4 space-y-3">
            <div className="border-b border-slate-200 pb-2">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide flex items-center gap-1.5">
                <FileText className="w-4 h-4 text-slate-700" />
                <span>LINKED APPLICATION HISTORY</span>
              </h2>
            </div>

            {apps.length === 0 ? (
              <p className="text-slate-500 text-[11px] p-2 bg-slate-50 rounded border border-slate-200 text-center">
                No past applications found for card {record.rationCardNo}.
              </p>
            ) : (
              <div className="space-y-2">
                {apps.map((app) => (
                  <div key={app.id} className="p-2.5 bg-slate-50 border border-slate-200 rounded text-xs space-y-1.5">
                    <div className="flex items-center justify-between">
                      <span className="font-mono font-bold text-slate-900">{app.applicationId}</span>
                      <StatusBadge status={app.currentStatus} />
                    </div>
                    <p className="text-slate-700 text-[11px]">Type: {app.applicationType.replace(/_/g, ' ')}</p>
                    <div className="flex items-center justify-between text-[10px] text-slate-500 pt-1 border-t border-slate-200">
                      <span>Received: {formatDate(app.createdAt)}</span>
                      <Link
                        to={`/applications/${app.id}`}
                        className="font-bold text-blue-900 hover:underline flex items-center gap-0.5"
                      >
                        <Eye className="w-3 h-3" />
                        Inspect
                      </Link>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
