import React, { useState, useEffect } from 'react';
import { rationService } from '../services/rationService';
import { RationRecord } from '../types';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { StatusBadge } from '../components/common/StatusBadge';
import { LoadingState } from '../components/common/LoadingState';
import { ErrorState } from '../components/common/ErrorState';
import { EmptyState } from '../components/common/EmptyState';
import { Search, RefreshCw, CheckCircle, AlertCircle, Eye } from 'lucide-react';
import { Link } from 'react-router-dom';

export const RationRecordsPage: React.FC = () => {
  const [records, setRecords] = useState<RationRecord[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [query, setQuery] = useState('');
  const [district, setDistrict] = useState('');
  const [taluka, setTaluka] = useState('');

  const fetchRecords = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await rationService.getRationRecords(query, district, taluka);
      setRecords(data);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Failed to fetch ration records from database.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecords();
  }, [district, taluka]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    fetchRecords();
  };

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb items={[{ label: 'Officer Services' }, { label: 'Ration Records' }]} />

      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <h1 className="text-base font-extrabold text-blue-950">Ration Card Master Store (RCMS)</h1>
          <p className="text-xs text-slate-600 mt-0.5">
            Department 2 Master Database Records — Food, Civil Supplies & Consumer Protection Department
          </p>
        </div>
        <button
          onClick={fetchRecords}
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-slate-700 bg-white hover:bg-slate-50 border border-slate-300 rounded shadow-xs transition shrink-0"
        >
          <RefreshCw className="w-3.5 h-3.5 mr-1.5 text-slate-500" />
          Refresh Database
        </button>
      </div>

      {/* Filter & Search Bar */}
      <div className="bg-white p-3 rounded border border-slate-300 shadow-xs flex flex-col md:flex-row md:items-center justify-between gap-2">
        <form onSubmit={handleSearch} className="flex-1 flex items-center gap-2">
          <div className="relative flex-1">
            <Search className="w-4 h-4 absolute left-2.5 top-2 text-slate-400" />
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Search Ration Card Number or Holder Name..."
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
          <select
            value={district}
            onChange={(e) => setDistrict(e.target.value)}
            className="px-2 py-1.5 border border-slate-300 rounded bg-white text-slate-800 font-medium focus:outline-none focus:ring-2 focus:ring-blue-900"
          >
            <option value="">All Districts</option>
            <option value="DIST-PUN">Pune (DIST-PUN)</option>
            <option value="DIST-NSK">Nashik (DIST-NSK)</option>
            <option value="DIST-THN">Thane (DIST-THN)</option>
            <option value="DIST-KLP">Kolhapur (DIST-KLP)</option>
            <option value="DIST-NGP">Nagpur (DIST-NGP)</option>
            <option value="DIST-CSN">Sambhajinagar (DIST-CSN)</option>
          </select>

          <select
            value={taluka}
            onChange={(e) => setTaluka(e.target.value)}
            className="px-2 py-1.5 border border-slate-300 rounded bg-white text-slate-800 font-medium focus:outline-none focus:ring-2 focus:ring-blue-900"
          >
            <option value="">All Talukas</option>
            <option value="TAL-PUN-04">Pune City (TAL-PUN-04)</option>
            <option value="TAL-HAV-02">Haveli (TAL-HAV-02)</option>
            <option value="TAL-NSK-01">Nashik (TAL-NSK-01)</option>
            <option value="TAL-THN-01">Thane (TAL-THN-01)</option>
          </select>
        </div>
      </div>

      {/* Master Store Data Table */}
      {loading ? (
        <LoadingState message="Fetching master ration card records..." />
      ) : error ? (
        <ErrorState message={error} onRetry={fetchRecords} />
      ) : records.length === 0 ? (
        <EmptyState title="No Records Found" description="No ration card entries match your search query." />
      ) : (
        <div className="bg-white rounded border border-slate-300 shadow-xs overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-xs border-collapse">
              <thead>
                <tr className="bg-slate-900 text-slate-200 uppercase font-bold tracking-wider border-b border-slate-800">
                  <th className="py-2.5 px-3 border-r border-slate-800">Ration Card No.</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Card Holder Name</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">House Address</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">District</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Taluka</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Verification</th>
                  <th className="py-2.5 px-3 border-r border-slate-800">Status</th>
                  <th className="py-2.5 px-3 text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200">
                {records.map((rec) => (
                  <tr key={rec.id} className="hover:bg-slate-50 transition">
                    <td className="py-2 px-3 font-mono font-bold text-slate-900">{rec.rationCardNo}</td>
                    <td className="py-2 px-3 font-semibold text-slate-900">{rec.holderName}</td>
                    <td className="py-2 px-3 text-slate-700 max-w-xs truncate" title={rec.houseAddress}>
                      {rec.houseAddress}
                    </td>
                    <td className="py-2 px-3 text-slate-700 font-mono">{rec.districtCode}</td>
                    <td className="py-2 px-3 text-slate-700 font-mono">{rec.talukaCode}</td>
                    <td className="py-2 px-3">
                      {rec.verificationFlag ? (
                        <span className="inline-flex items-center text-emerald-800 font-semibold">
                          <CheckCircle className="w-3.5 h-3.5 mr-1 text-emerald-600" />
                          Verified
                        </span>
                      ) : (
                        <span className="inline-flex items-center text-amber-800 font-semibold">
                          <AlertCircle className="w-3.5 h-3.5 mr-1 text-amber-600" />
                          Unverified
                        </span>
                      )}
                    </td>
                    <td className="py-2 px-3">
                      <StatusBadge status={rec.updateStatus} />
                    </td>
                    <td className="py-2 px-3 text-right">
                      <Link
                        to={`/ration-records/${rec.id}`}
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
