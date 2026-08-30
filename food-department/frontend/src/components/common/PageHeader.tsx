import React from 'react';

interface PageHeaderProps {
  title: string;
  subtitle?: string;
  badgeText?: string;
  action?: React.ReactNode;
}

export const PageHeader: React.FC<PageHeaderProps> = ({
  title,
  subtitle,
  badgeText = 'SIMULATED ENVIRONMENT • GOVMESH SIH 2026',
  action,
}) => {
  return (
    <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs mb-5 flex flex-col md:flex-row md:items-center justify-between gap-3">
      <div>
        <div className="flex items-center gap-2 mb-0.5">
          <span className="text-[10px] uppercase font-bold tracking-wider px-2 py-0.5 rounded bg-slate-100 text-slate-700 border border-slate-200 font-mono">
            {badgeText}
          </span>
          <span className="text-xs text-slate-500 font-medium">Department 2 — Maharashtra</span>
        </div>
        <h1 className="text-base font-bold text-slate-900 leading-tight">{title}</h1>
        {subtitle && <p className="text-xs text-slate-600 mt-0.5">{subtitle}</p>}
      </div>

      {action && <div className="shrink-0">{action}</div>}
    </div>
  );
};
