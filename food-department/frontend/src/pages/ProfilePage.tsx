import React from 'react';
import { useAuth } from '../hooks/useAuth';
import { Breadcrumb } from '../components/common/Breadcrumb';
import { getRoleTitle } from '../utils/formatters';
import { LogOut, CheckCircle } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export const ProfilePage: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="space-y-4 text-xs">
      <Breadcrumb items={[{ label: 'Officer Services' }, { label: 'Profile' }]} />

      <div className="bg-white p-3.5 rounded border border-slate-300 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <h1 className="text-base font-extrabold text-blue-950">Officer Credential Profile</h1>
          <p className="text-xs text-slate-600 mt-0.5">
            Department Officer Session Information & Role Privilege Specification
          </p>
        </div>
      </div>

      <div className="max-w-2xl bg-white rounded border border-slate-300 shadow-xs p-4 space-y-4">
        <div className="flex items-center space-x-3 pb-3 border-b border-slate-200">
          <div className="w-12 h-12 rounded bg-slate-900 text-amber-400 font-extrabold text-lg flex items-center justify-center border border-slate-700 shadow-xs shrink-0">
            {user?.fullName?.charAt(0) || 'O'}
          </div>
          <div>
            <h2 className="text-sm font-bold text-slate-900">{user?.fullName}</h2>
            <p className="text-xs font-semibold text-slate-700">{getRoleTitle(user?.role)}</p>
            <p className="text-[10px] text-slate-500">{user?.department}</p>
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <div className="p-2 bg-slate-50 border border-slate-200 rounded">
            <span className="text-[10px] text-slate-500 font-semibold uppercase">Username</span>
            <p className="font-mono font-bold text-slate-900 text-xs mt-0.5">{user?.username}</p>
          </div>

          <div className="p-2 bg-slate-50 border border-slate-200 rounded">
            <span className="text-[10px] text-slate-500 font-semibold uppercase">Employee ID</span>
            <p className="font-mono font-bold text-slate-900 text-xs mt-0.5">{user?.employeeId}</p>
          </div>

          <div className="p-2 bg-slate-50 border border-slate-200 rounded">
            <span className="text-[10px] text-slate-500 font-semibold uppercase">Role System Key</span>
            <p className="font-mono font-bold text-slate-800 text-xs mt-0.5">{user?.role}</p>
          </div>

          <div className="p-2 bg-slate-50 border border-slate-200 rounded">
            <span className="text-[10px] text-slate-500 font-semibold uppercase">Account Status</span>
            <div className="flex items-center gap-1 text-emerald-800 font-bold mt-0.5">
              <CheckCircle className="w-3.5 h-3.5 text-emerald-600" />
              <span>{user?.isActive ? 'ACTIVE OFFICER' : 'INACTIVE'}</span>
            </div>
          </div>
        </div>

        <div className="pt-3 border-t border-slate-200 flex justify-end">
          <button
            onClick={handleLogout}
            className="px-3 py-1.5 text-xs font-bold text-white bg-rose-800 hover:bg-rose-900 rounded shadow-xs transition flex items-center gap-1.5"
          >
            <LogOut className="w-3.5 h-3.5" />
            Sign Out Officer Account
          </button>
        </div>
      </div>
    </div>
  );
};
