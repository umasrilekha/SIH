import React, { useState } from 'react';
import { useAuth } from '../../hooks/useAuth';
import { getRoleTitle } from '../../utils/formatters';
import { Bell, User as UserIcon, LogOut, ChevronDown, CheckCircle2, Globe } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';

interface HeaderProps {
  unreadNotificationCount?: number;
}

export const Header: React.FC<HeaderProps> = ({ unreadNotificationCount = 2 }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const [fontSize, setFontSize] = useState<'sm' | 'base' | 'lg'>('base');

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-white border-b border-slate-300 shadow-xs">
      {/* Top Thin Government Utility Bar */}
      <div className="bg-slate-900 text-slate-200 text-[11px] px-4 py-1 flex items-center justify-between border-b border-slate-800">
        <div className="flex items-center space-x-3">
          <a href="#main-content" className="hover:underline text-slate-300">
            Skip to main content
          </a>
          <span className="text-slate-700">|</span>
          <div className="flex items-center space-x-1.5 font-bold">
            <span className="text-slate-400">Accessibility:</span>
            <button
              onClick={() => setFontSize('sm')}
              className={`px-1 rounded ${fontSize === 'sm' ? 'bg-amber-600 text-white' : 'hover:bg-slate-800'}`}
              title="Decrease text size"
            >
              A-
            </button>
            <button
              onClick={() => setFontSize('base')}
              className={`px-1 rounded ${fontSize === 'base' ? 'bg-amber-600 text-white' : 'hover:bg-slate-800'}`}
              title="Normal text size"
            >
              A
            </button>
            <button
              onClick={() => setFontSize('lg')}
              className={`px-1 rounded ${fontSize === 'lg' ? 'bg-amber-600 text-white' : 'hover:bg-slate-800'}`}
              title="Increase text size"
            >
              A+
            </button>
          </div>
          <span className="text-slate-700">|</span>
          <div className="flex items-center space-x-1 text-slate-300 font-medium">
            <Globe className="w-3 h-3 text-slate-400" />
            <span>मराठी</span>
            <span className="text-slate-600">/</span>
            <span className="font-bold text-amber-400">English</span>
          </div>
        </div>

        <div className="flex items-center space-x-3">
          <span className="text-amber-400 font-mono text-[10px] uppercase font-bold tracking-wide">
            SIMULATED ENVIRONMENT • GOVMESH SIH 2026
          </span>
        </div>
      </div>

      {/* Main Government Header */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 py-2.5 flex items-center justify-between">
        {/* Left: Department Bilingual Institutional Title */}
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded border border-slate-300 bg-slate-100 flex flex-col items-center justify-center text-slate-800 font-extrabold text-[10px] leading-tight shrink-0 shadow-xs">
            <span className="text-amber-700 font-black">GOV</span>
            <span className="text-slate-900">MH</span>
          </div>

          <div className="border-l-2 border-amber-600 pl-3">
            <p className="text-[10px] font-bold text-slate-500 uppercase tracking-wider">
              GOVERNMENT OF MAHARASHTRA • महाराष्ट्र शासन
            </p>
            <h1 className="text-sm sm:text-base font-extrabold text-blue-950 leading-tight">
              अन्न, नागरी पुरवठा व ग्राहक संरक्षण विभाग
            </h1>
            <p className="text-xs font-bold text-slate-800">
              Food, Civil Supplies & Consumer Protection Department
            </p>
          </div>
        </div>

        {/* Right: Officer Profile & Quick Session Status */}
        {user ? (
          <div className="flex items-center space-x-3">
            <Link
              to="/notifications"
              className="relative p-1.5 text-slate-600 hover:text-blue-950 hover:bg-slate-100 rounded transition"
              title="Notifications Feed"
            >
              <Bell className="w-4 h-4" />
              {unreadNotificationCount > 0 && (
                <span className="absolute top-0.5 right-0.5 bg-amber-600 text-white text-[9px] font-bold w-3.5 h-3.5 rounded-full flex items-center justify-center">
                  {unreadNotificationCount}
                </span>
              )}
            </Link>

            <div className="relative">
              <button
                onClick={() => setDropdownOpen(!dropdownOpen)}
                className="flex items-center space-x-2 text-left bg-slate-50 hover:bg-slate-100 px-2.5 py-1 rounded border border-slate-300 transition"
              >
                <div className="w-5 h-5 rounded bg-slate-900 text-amber-400 flex items-center justify-center font-bold text-[11px]">
                  {user?.fullName?.charAt(0) || 'O'}
                </div>
                <div className="hidden sm:block">
                  <div className="text-xs font-bold text-slate-900 leading-none">{user?.fullName || 'Officer'}</div>
                  <div className="text-[10px] text-slate-500 font-medium">{getRoleTitle(user?.role)}</div>
                </div>
                <ChevronDown className="w-3 h-3 text-slate-500" />
              </button>

              {dropdownOpen && (
                <div className="absolute right-0 mt-1 w-56 bg-white text-slate-900 rounded shadow-lg border border-slate-300 py-1 z-50 text-xs">
                  <div className="px-3 py-1.5 border-b border-slate-200 bg-slate-50">
                    <p className="text-[10px] text-slate-400 font-semibold uppercase">Employee ID</p>
                    <p className="font-mono font-bold text-slate-800">{user?.employeeId}</p>
                    <p className="text-[10px] text-slate-500">{user?.department}</p>
                  </div>

                  <Link
                    to="/profile"
                    onClick={() => setDropdownOpen(false)}
                    className="flex items-center px-3 py-1.5 text-slate-700 hover:bg-slate-100 transition"
                  >
                    <UserIcon className="w-3.5 h-3.5 mr-2 text-slate-500" />
                    Officer Profile
                  </Link>

                  <Link
                    to="/system-health"
                    onClick={() => setDropdownOpen(false)}
                    className="flex items-center px-3 py-1.5 text-slate-700 hover:bg-slate-100 transition"
                  >
                    <CheckCircle2 className="w-3.5 h-3.5 mr-2 text-slate-500" />
                    System Diagnostics
                  </Link>

                  <button
                    onClick={handleLogout}
                    className="w-full text-left flex items-center px-3 py-1.5 text-rose-700 hover:bg-rose-50 border-t border-slate-100 font-semibold"
                  >
                    <LogOut className="w-3.5 h-3.5 mr-2 text-rose-600" />
                    Sign Out
                  </button>
                </div>
              )}
            </div>
          </div>
        ) : (
          <div className="text-right text-xs">
            <span className="text-slate-500 font-medium">Department Officer Portal</span>
            <div className="text-[10px] text-amber-700 font-mono font-bold">GovMesh Demonstration</div>
          </div>
        )}
      </div>
    </header>
  );
};
