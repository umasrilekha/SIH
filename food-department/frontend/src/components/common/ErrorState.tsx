import React from 'react';
import { AlertTriangle, RefreshCw } from 'lucide-react';

interface ErrorStateProps {
  title?: string;
  message?: string;
  onRetry?: () => void;
}

export const ErrorState: React.FC<ErrorStateProps> = ({
  title = 'Failed to Load Data',
  message = 'An unexpected error occurred while communicating with the department backend server.',
  onRetry,
}) => {
  return (
    <div className="flex flex-col items-center justify-center p-8 bg-rose-50/50 rounded-lg border border-rose-200 text-center">
      <div className="w-12 h-12 rounded-full bg-rose-100 flex items-center justify-center mb-3">
        <AlertTriangle className="w-6 h-6 text-rose-600" />
      </div>
      <h3 className="text-sm font-bold text-rose-900">{title}</h3>
      <p className="text-xs text-rose-700 max-w-md mt-1 mb-4">{message}</p>
      {onRetry && (
        <button
          onClick={onRetry}
          className="inline-flex items-center px-3 py-1.5 text-xs font-semibold text-white bg-slate-900 hover:bg-slate-800 rounded shadow-sm transition"
        >
          <RefreshCw className="w-3.5 h-3.5 mr-1.5" />
          Retry Request
        </button>
      )}
    </div>
  );
};
