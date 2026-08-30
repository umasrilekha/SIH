import React from 'react';
import { Loader2 } from 'lucide-react';

interface LoadingStateProps {
  message?: string;
}

export const LoadingState: React.FC<LoadingStateProps> = ({ message = 'Loading department data from database...' }) => {
  return (
    <div className="flex flex-col items-center justify-center p-12 bg-white rounded-lg border border-slate-200 shadow-sm text-center">
      <Loader2 className="w-8 h-8 text-amber-600 animate-spin mb-3" />
      <p className="text-sm font-semibold text-slate-700">{message}</p>
      <p className="text-xs text-slate-400 mt-1">GovMesh Department 2 API Service</p>
    </div>
  );
};
