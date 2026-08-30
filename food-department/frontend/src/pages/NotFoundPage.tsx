import React from 'react';
import { Link } from 'react-router-dom';
import { ShieldAlert, ArrowLeft } from 'lucide-react';

export const NotFoundPage: React.FC = () => {
  return (
    <div className="flex flex-col items-center justify-center p-12 bg-white rounded-lg border border-slate-200 text-center my-8">
      <ShieldAlert className="w-12 h-12 text-amber-600 mb-3" />
      <h1 className="text-xl font-black text-slate-900">404 — Page Not Found</h1>
      <p className="text-xs text-slate-600 max-w-sm mt-1 mb-4">
        The requested officer portal route does not exist in Department 2.
      </p>
      <Link
        to="/dashboard"
        className="inline-flex items-center px-4 py-2 text-xs font-bold text-slate-900 bg-amber-500 hover:bg-amber-400 rounded transition shadow-sm"
      >
        <ArrowLeft className="w-4 h-4 mr-1.5" />
        Return to Operational Dashboard
      </Link>
    </div>
  );
};
