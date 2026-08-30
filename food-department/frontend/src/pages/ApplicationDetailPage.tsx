import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { applicationService } from '../services/applicationService';
import { ApplicationDetail, AuditLog } from '../types';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { StatusBadge } from '../components/common/StatusBadge';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { useAuth } from '../hooks/useAuth';
import { formatDate, getRoleTitle } from '../utils/formatters';
import {
  ArrowLeft,
  FileText,
  UserCheck,
  AlertCircle,
  Play,
  CheckCircle2,
  XCircle,
  HelpCircle,
  Clock,
  ShieldAlert,
  ArrowRight
} from 'lucide-react';

export const ApplicationDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { user } = useAuth();

  const [detail, setDetail] = useState<ApplicationDetail | null>(null);
  const [history, setHistory] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [actionLoading, setActionLoading] = useState(false);
  const [actionError, setActionError] = useState<string | null>(null);

  // Modal States
  const [modalType, setModalType] = useState<'APPROVE' | 'REJECT' | 'REQUEST_INFO' | null>(null);
  const [modalInput, setModalInput] = useState('');
  const [validationErr, setValidationErr] = useState<string | null>(null);

  const fetchDetail = async () => {
    if (!id) return;
    setLoading(true);
    setError(null);
    try {
      const [appData, historyData] = await Promise.all([
        applicationService.getApplicationById(Number(id)),
        applicationService.getApplicationHistory(Number(id)),
      ]);
      setDetail(appData);
      setHistory(historyData);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to retrieve application details.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDetail();
  }, [id]);

  const handleStartReview = async () => {
    if (!id) return;
    setActionLoading(true);
    setActionError(null);
    try {
      const updated = await applicationService.startReview(Number(id));
      setDetail(updated);
      const updatedHistory = await applicationService.getApplicationHistory(Number(id));
      setHistory(updatedHistory);
    } catch (err: any) {
      setActionError(err?.response?.data?.message || 'Failed to start review process.');
    } finally {
      setActionLoading(false);
    }
  };

  const handleConfirmAction = async () => {
    if (!id || !modalType) return;

    if ((modalType === 'REJECT' || modalType === 'REQUEST_INFO') && !modalInput.trim()) {
      setValidationErr(modalType === 'REJECT' ? 'A rejection reason is mandatory.' : 'Information request details cannot be empty.');
      return;
    }

    setActionLoading(true);
    setActionError(null);
    setValidationErr(null);

    try {
      let updated: ApplicationDetail;
      if (modalType === 'APPROVE') {
        updated = await applicationService.approveApplication(Number(id), modalInput);
      } else if (modalType === 'REJECT') {
        updated = await applicationService.rejectApplication(Number(id), modalInput);
      } else {
        updated = await applicationService.requestInformation(Number(id), modalInput);
      }

      setDetail(updated);
      const updatedHistory = await applicationService.getApplicationHistory(Number(id));
      setHistory(updatedHistory);
      setModalType(null);
      setModalInput('');
    } catch (err: any) {
      setActionError(err?.response?.data?.message || 'Action failed. Please verify permissions and application state.');
    } finally {
      setActionLoading(false);
    }
  };

  if (loading) return <LoadingState message="Loading application record specification..." />;
  if (error || !detail) return <ErrorState message={error || 'Application record not found'} />;

  const { application, currentRationRecord } = detail;
  const isAuditor = user?.role === 'AUDITOR';
  const isOfficer = user?.role === 'FOOD_SUPPLY_OFFICER';
  const canApproveOrReject = user?.role === 'SENIOR_OFFICER' || user?.role === 'DEPARTMENT_ADMIN';

  const isTerminalState = application.currentStatus === 'APPROVED' || application.currentStatus === 'REJECTED';
  const currentAddr = currentRationRecord?.houseAddress || 'N/A';
  const reqAddr = application.requestedAddress || currentAddr;
  const isAddressDifferent = application.applicationType === 'ADDRESS_UPDATE' && currentAddr !== reqAddr;

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb
        items={[
          { label: 'Officer Services' },
          { label: 'Incoming Requests', to: '/requests' },
          { label: `Application ${application.applicationId}` },
        ]}
      />

      {/* Header Bar */}
      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <div className="flex items-center gap-2">
            <h1 className="text-base font-extrabold text-blue-950">
              Application Details: {application.applicationId}
            </h1>
            <StatusBadge status={application.currentStatus} />
          </div>
          <p className="text-xs text-slate-600 mt-0.5">
            Interoperability Payload Record — Food, Civil Supplies & Consumer Protection Department
          </p>
        </div>

        <Link
          to="/requests"
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <ArrowLeft className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Back to Queue
        </Link>
      </div>

      {actionError && (
        <div className="p-3 bg-rose-50 border border-rose-300 rounded text-xs text-rose-800 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <AlertCircle className="w-4 h-4 text-rose-700 shrink-0" />
            <span>{actionError}</span>
          </div>
          <button onClick={() => setActionError(null)} className="text-rose-900 font-bold text-xs">Dismiss</button>
        </div>
      )}

      {/* Main Administrative Layout */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Left 2 Cols: Application Info & Address Comparison & Officer Action */}
        <div className="lg:col-span-2 space-y-4">
          {/* Section 1: Application Information */}
          <div className="bg-white rounded border border-slate-300 shadow-xs p-4 space-y-3">
            <div className="flex items-center justify-between border-b border-slate-200 pb-2">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide flex items-center gap-1.5">
                <FileText className="w-4 h-4 text-slate-700" />
                <span>APPLICATION INFORMATION</span>
              </h2>
              <span className="text-[10px] font-mono text-slate-500">
                Source: <strong className="text-slate-800 font-bold">{application.sourceDepartment}</strong>
              </span>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-2.5">
              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Application ID</span>
                <span className="font-mono font-bold text-slate-900 text-xs mt-0.5 block">{application.applicationId}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Citizen Reference</span>
                <span className="font-mono font-bold text-slate-900 text-xs mt-0.5 block">{application.citizenReference}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Ration Card Number</span>
                <span className="font-mono font-bold text-slate-900 text-xs mt-0.5 block">{application.rationCardNo}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Application Type</span>
                <span className="font-bold text-slate-900 text-xs mt-0.5 block">{application.applicationType.replace(/_/g, ' ')}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Received On</span>
                <span className="font-medium text-slate-800 text-xs mt-0.5 block">{formatDate(application.createdAt)}</span>
              </div>

              <div className="bg-slate-50 p-2.5 rounded border border-slate-200">
                <span className="text-[10px] text-slate-500 font-bold uppercase block">Last Updated</span>
                <span className="font-medium text-slate-800 text-xs mt-0.5 block">{formatDate(application.updatedAt)}</span>
              </div>
            </div>

            {application.officerComments && (
              <div className="p-2.5 bg-amber-50/70 border border-amber-300 rounded space-y-0.5">
                <div className="text-[10px] font-bold text-amber-900 uppercase">Officer Remarks / Comments</div>
                <p className="text-slate-800 leading-snug">{application.officerComments}</p>
                {application.reviewedByOfficer && (
                  <p className="text-[10px] text-slate-500 font-mono pt-1 border-t border-amber-200">
                    Reviewed By: {application.reviewedByOfficer}
                  </p>
                )}
              </div>
            )}
          </div>

          {/* Section 2: Current Record vs Requested Update Comparison */}
          <div className="bg-white rounded border border-slate-300 shadow-xs p-4 space-y-3">
            <div className="flex items-center justify-between border-b border-slate-200 pb-2">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
                RECORD ADDRESS COMPARISON SPECIFICATION
              </h2>
              {isAddressDifferent && (
                <span className="text-[10px] font-bold uppercase px-2 py-0.5 rounded bg-amber-100 text-amber-900 border border-amber-300 font-mono">
                  CHANGE REQUESTED
                </span>
              )}
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {/* Current Address Card */}
              <div className="p-3 bg-slate-50 border border-slate-300 rounded space-y-1.5">
                <div className="flex items-center justify-between border-b border-slate-200 pb-1">
                  <span className="font-bold text-slate-700 uppercase text-[10px]">CURRENT RECORD ADDRESS</span>
                  <span className="text-[10px] text-slate-400 font-mono">Food Dept Master</span>
                </div>
                <p className="text-slate-900 font-medium leading-relaxed bg-white p-2 rounded border border-slate-200">
                  {currentAddr}
                </p>
              </div>

              {/* Requested Address Card */}
              <div className="p-3 bg-blue-50/40 border border-blue-300 rounded space-y-1.5">
                <div className="flex items-center justify-between border-b border-blue-200 pb-1">
                  <span className="font-bold text-blue-950 uppercase text-[10px]">REQUESTED UPDATE ADDRESS</span>
                  <span className="text-[10px] text-blue-800 font-mono font-bold">Revenue Payload</span>
                </div>
                <p className="text-blue-950 font-bold leading-relaxed bg-white p-2 rounded border border-blue-200">
                  {reqAddr}
                </p>
              </div>
            </div>
          </div>

          {/* Section 3: Officer Decision & Action Panel */}
          <div className="bg-white rounded border border-slate-300 shadow-xs p-4 space-y-3">
            <div className="flex items-center justify-between border-b border-slate-200 pb-2">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
                OFFICER REVIEW & WORKFLOW ACTIONS
              </h2>
              <span className="text-[10px] text-slate-500 font-mono">
                Officer Role: <strong className="text-slate-800 font-bold">{user?.role}</strong>
              </span>
            </div>

            {isTerminalState ? (
              <div className="p-3 bg-slate-100 border border-slate-300 rounded text-slate-700 flex items-center justify-between">
                <div>
                  <p className="font-bold">Application Completed / Processed</p>
                  <p className="text-[11px] text-slate-500">
                    This application is in terminal state <strong>{application.currentStatus}</strong> and cannot be modified.
                  </p>
                </div>
                <StatusBadge status={application.currentStatus} />
              </div>
            ) : isAuditor ? (
              <div className="p-3 bg-slate-100 border border-slate-300 rounded text-slate-600 flex items-center gap-2">
                <ShieldAlert className="w-4 h-4 text-slate-500 shrink-0" />
                <span>Auditor role has read-only access. Workflow action buttons are restricted.</span>
              </div>
            ) : (
              <div className="space-y-3">
                {application.currentStatus === 'PENDING' && (
                  <div className="flex items-center justify-between bg-amber-50/50 p-3 rounded border border-amber-200">
                    <div>
                      <p className="font-bold text-slate-900">Application Pending Review</p>
                      <p className="text-[11px] text-slate-600">
                        Clicking Start Review will assign the application to your officer session and change state to UNDER_REVIEW.
                      </p>
                    </div>
                    <button
                      onClick={handleStartReview}
                      disabled={actionLoading}
                      className="px-3.5 py-1.5 text-xs font-bold text-white bg-slate-900 hover:bg-slate-800 rounded shadow-xs transition flex items-center gap-1.5 shrink-0"
                    >
                      <Play className="w-3.5 h-3.5 text-amber-400" />
                      {actionLoading ? 'Updating State...' : 'Start Review'}
                    </button>
                  </div>
                )}

                {(application.currentStatus === 'UNDER_REVIEW' || application.currentStatus === 'INFORMATION_REQUIRED' || application.currentStatus === 'PENDING') && (
                  <div className="pt-1 flex flex-wrap items-center gap-2">
                    <button
                      onClick={() => {
                        setModalType('APPROVE');
                        setModalInput('');
                        setValidationErr(null);
                      }}
                      disabled={actionLoading || !canApproveOrReject}
                      title={!canApproveOrReject ? 'Only Senior Officers or Admins can Approve' : 'Approve Address Update'}
                      className={`px-4 py-2 font-bold text-xs rounded shadow-xs transition flex items-center gap-1.5 ${
                        canApproveOrReject
                          ? 'bg-emerald-800 hover:bg-emerald-900 text-white'
                          : 'bg-slate-200 text-slate-500 cursor-not-allowed border border-slate-300'
                      }`}
                    >
                      <CheckCircle2 className="w-4 h-4" />
                      Approve Update
                    </button>

                    <button
                      onClick={() => {
                        setModalType('REJECT');
                        setModalInput('');
                        setValidationErr(null);
                      }}
                      disabled={actionLoading || !canApproveOrReject}
                      title={!canApproveOrReject ? 'Only Senior Officers or Admins can Reject' : 'Reject Application'}
                      className={`px-4 py-2 font-bold text-xs rounded shadow-xs transition flex items-center gap-1.5 ${
                        canApproveOrReject
                          ? 'bg-rose-800 hover:bg-rose-900 text-white'
                          : 'bg-slate-200 text-slate-500 cursor-not-allowed border border-slate-300'
                      }`}
                    >
                      <XCircle className="w-4 h-4" />
                      Reject Application
                    </button>

                    <button
                      onClick={() => {
                        setModalType('REQUEST_INFO');
                        setModalInput('');
                        setValidationErr(null);
                      }}
                      disabled={actionLoading}
                      className="px-4 py-2 font-bold text-xs text-slate-800 bg-white hover:bg-slate-100 border border-slate-300 rounded shadow-xs transition flex items-center gap-1.5"
                    >
                      <HelpCircle className="w-4 h-4 text-slate-600" />
                      Request Information
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>

        {/* Right Col: Linked Master Record + Application Timeline */}
        <div className="space-y-4">
          {/* Linked Master Ration Entry */}
          <div className="bg-white rounded border border-slate-300 shadow-xs p-4 space-y-3">
            <div className="border-b border-slate-200 pb-2 flex justify-between items-center">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide flex items-center gap-1.5">
                <UserCheck className="w-4 h-4 text-slate-700" />
                <span>Linked Master Ration Record</span>
              </h2>
              {currentRationRecord && (
                <Link
                  to={`/ration-records/${currentRationRecord.id}`}
                  className="text-[10px] font-bold text-blue-900 hover:underline"
                >
                  View Master Card
                </Link>
              )}
            </div>

            {currentRationRecord ? (
              <div className="space-y-2 text-xs">
                <div>
                  <span className="text-[10px] text-slate-500 uppercase font-bold block">Holder Name</span>
                  <span className="font-bold text-slate-900 block">{currentRationRecord.holderName}</span>
                </div>

                <div>
                  <span className="text-[10px] text-slate-500 uppercase font-bold block">Master Address</span>
                  <span className="text-slate-700 bg-slate-50 p-2 rounded border border-slate-200 text-[11px] leading-snug mt-0.5 block">
                    {currentRationRecord.houseAddress}
                  </span>
                </div>

                <div className="grid grid-cols-2 gap-2 text-[11px]">
                  <div>
                    <span className="text-[10px] text-slate-500 uppercase font-bold block">District</span>
                    <span className="font-mono font-semibold text-slate-800">{currentRationRecord.districtCode}</span>
                  </div>
                  <div>
                    <span className="text-[10px] text-slate-500 uppercase font-bold block">Taluka</span>
                    <span className="font-mono font-semibold text-slate-800">{currentRationRecord.talukaCode}</span>
                  </div>
                </div>

                <div className="flex items-center justify-between pt-2 border-t border-slate-200">
                  <span className="text-slate-500 font-medium">Record Status:</span>
                  <StatusBadge status={currentRationRecord.updateStatus} />
                </div>
              </div>
            ) : (
              <div className="p-3 bg-slate-50 text-slate-600 text-xs text-center rounded border border-slate-200">
                <AlertCircle className="w-5 h-5 text-slate-400 mx-auto mb-1" />
                <p className="font-semibold">No Master Card Record Found</p>
                <p className="text-[11px] mt-0.5 text-slate-500">Ration Card {application.rationCardNo} is not present in local database.</p>
              </div>
            )}
          </div>

          {/* Application Timeline / Audit History */}
          <div className="bg-white rounded border border-slate-300 shadow-xs p-4 space-y-3">
            <div className="border-b border-slate-200 pb-2">
              <h2 className="text-xs font-bold text-slate-900 uppercase tracking-wide flex items-center gap-1.5">
                <Clock className="w-4 h-4 text-slate-700" />
                <span>APPLICATION TIMELINE</span>
              </h2>
            </div>

            {history.length === 0 ? (
              <p className="text-slate-500 text-[11px]">No timeline events recorded yet.</p>
            ) : (
              <div className="relative border-l-2 border-slate-200 ml-2 space-y-3 py-1">
                {history.map((item, idx) => (
                  <div key={item.id || idx} className="ml-3 relative text-xs">
                    <div className="absolute -left-[19px] top-1.5 w-2.5 h-2.5 rounded-full bg-blue-900 border-2 border-white shadow-xs"></div>
                    <div className="font-bold text-slate-900 text-[11px]">{item.action}</div>
                    <p className="text-slate-600 leading-tight text-[11px] mt-0.5">{item.description}</p>
                    <div className="text-[10px] text-slate-400 font-mono mt-1 flex justify-between">
                      <span>{item.officerName}</span>
                      <span>{formatDate(item.timestamp)}</span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Confirmation Modal */}
      {modalType && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-xs flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg border border-slate-300 shadow-xl max-w-md w-full p-5 space-y-4">
            <div className="border-b border-slate-200 pb-2.5">
              <h3 className="text-sm font-bold text-slate-900 uppercase tracking-wide">
                {modalType === 'APPROVE' && 'Approve Address Update?'}
                {modalType === 'REJECT' && 'Reject Application?'}
                {modalType === 'REQUEST_INFO' && 'Request Additional Information?'}
              </h3>
            </div>

            {modalType === 'APPROVE' && (
              <div className="space-y-3 text-xs">
                <div className="p-3 bg-amber-50 border border-amber-300 rounded text-amber-950 leading-relaxed font-medium">
                  <strong>Notice:</strong> Approval will transactionally update the Food Department's master ration record in the database.
                </div>

                <div className="space-y-1">
                  <span className="font-bold text-slate-600 text-[10px] uppercase block">Current Master Address</span>
                  <p className="p-2 bg-slate-100 rounded text-slate-800 font-mono text-[11px]">{currentAddr}</p>
                </div>

                <div className="space-y-1">
                  <span className="font-bold text-blue-900 text-[10px] uppercase block">New Address To Apply</span>
                  <p className="p-2 bg-blue-50 border border-blue-200 rounded text-blue-950 font-mono font-bold text-[11px]">{reqAddr}</p>
                </div>

                <div>
                  <label className="block font-semibold text-slate-700 mb-1 uppercase text-[10px]">
                    Approval Remarks (Optional)
                  </label>
                  <textarea
                    rows={2}
                    value={modalInput}
                    onChange={(e) => setModalInput(e.target.value)}
                    placeholder="Enter approval notes or officer comments..."
                    className="w-full p-2 border border-slate-300 rounded text-xs focus:ring-2 focus:ring-blue-900"
                  />
                </div>
              </div>
            )}

            {modalType === 'REJECT' && (
              <div className="space-y-3 text-xs">
                <p className="text-slate-600 leading-relaxed">
                  Rejecting this request will mark application <strong>{application.applicationId}</strong> as REJECTED. The master ration record will not be modified.
                </p>

                <div>
                  <label className="block font-bold text-slate-800 mb-1 uppercase text-[10px]">
                    Rejection Reason (Mandatory) *
                  </label>
                  <textarea
                    rows={3}
                    required
                    value={modalInput}
                    onChange={(e) => {
                      setModalInput(e.target.value);
                      if (e.target.value.trim()) setValidationErr(null);
                    }}
                    placeholder="Provide specific rejection reason (e.g. Invalid proof document)..."
                    className="w-full p-2 border border-slate-300 rounded text-xs focus:ring-2 focus:ring-rose-800"
                  />
                  {validationErr && <p className="text-rose-700 text-[11px] font-bold mt-1">{validationErr}</p>}
                </div>
              </div>
            )}

            {modalType === 'REQUEST_INFO' && (
              <div className="space-y-3 text-xs">
                <p className="text-slate-600 leading-relaxed">
                  Change state to <strong>INFORMATION_REQUIRED</strong> and issue an internal query for missing documentation.
                </p>

                <div>
                  <label className="block font-bold text-slate-800 mb-1 uppercase text-[10px]">
                    Information Request Message (Mandatory) *
                  </label>
                  <textarea
                    rows={3}
                    required
                    value={modalInput}
                    onChange={(e) => {
                      setModalInput(e.target.value);
                      if (e.target.value.trim()) setValidationErr(null);
                    }}
                    placeholder="Specify required documentation (e.g. Please upload Revenue 7/12 extract)..."
                    className="w-full p-2 border border-slate-300 rounded text-xs focus:ring-2 focus:ring-blue-900"
                  />
                  {validationErr && <p className="text-rose-700 text-[11px] font-bold mt-1">{validationErr}</p>}
                </div>
              </div>
            )}

            <div className="pt-3 border-t border-slate-200 flex justify-end gap-2 text-xs">
              <button
                type="button"
                onClick={() => setModalType(null)}
                disabled={actionLoading}
                className="px-3.5 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-100 border border-slate-300 rounded transition"
              >
                Cancel
              </button>

              <button
                type="button"
                onClick={handleConfirmAction}
                disabled={actionLoading}
                className={`px-4 py-1.5 text-xs font-bold text-white rounded transition shadow-xs ${
                  modalType === 'APPROVE'
                    ? 'bg-emerald-800 hover:bg-emerald-900'
                    : modalType === 'REJECT'
                    ? 'bg-rose-800 hover:bg-rose-900'
                    : 'bg-slate-900 hover:bg-slate-800'
                }`}
              >
                {actionLoading ? 'Processing...' : 'Confirm Action'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
