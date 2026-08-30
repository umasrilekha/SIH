import React from 'react';
import { Link } from 'react-router-dom';
import { ChevronRight, Home } from 'lucide-react';

export interface BreadcrumbItem {
  label: string;
  to?: string;
}

interface BreadcrumbProps {
  items: BreadcrumbItem[];
}

export const Breadcrumb: React.FC<BreadcrumbProps> = ({ items }) => {
  return (
    <nav className="bg-slate-100/70 px-4 py-1.5 border-b border-slate-200 text-xs text-slate-600 mb-4 flex items-center space-x-1 font-medium">
      <Link to="/dashboard" className="flex items-center text-slate-600 hover:text-blue-900 transition">
        <Home className="w-3.5 h-3.5 mr-1 text-slate-500" />
        <span>Home</span>
      </Link>

      {items.map((item, idx) => (
        <React.Fragment key={idx}>
          <ChevronRight className="w-3 h-3 text-slate-400 shrink-0" />
          {item.to ? (
            <Link to={item.to} className="text-slate-600 hover:text-blue-900 transition">
              {item.label}
            </Link>
          ) : (
            <span className="text-slate-900 font-semibold">{item.label}</span>
          )}
        </React.Fragment>
      ))}
    </nav>
  );
};
