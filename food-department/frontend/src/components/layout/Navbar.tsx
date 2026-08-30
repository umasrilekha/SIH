import React, { useState } from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { ChevronDown, Shield, LayoutDashboard, Inbox, FileText, FolderKanban, Network, ShieldCheck, ClipboardList, Bell, Activity, UserCheck } from 'lucide-react';

export const Navbar: React.FC = () => {
  const location = useLocation();
  const [officerMenuOpen, setOfficerMenuOpen] = useState(false);

  const officerLinks = [
    { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { to: '/requests', label: 'Incoming Requests Queue', icon: Inbox },
    { to: '/ration-records', label: 'Ration Card Records', icon: FileText },
    { to: '/applications', label: 'Applications Search', icon: FolderKanban },
    { to: '/integration', label: 'Interoperability Monitor', icon: Network },
    { to: '/consent', label: 'Consent & Compliance', icon: ShieldCheck },
    { to: '/audit-logs', label: 'Department Audit Logs', icon: ClipboardList },
    { to: '/notifications', label: 'Notifications Feed', icon: Bell },
    { to: '/system-health', label: 'System Diagnostics', icon: Activity },
    { to: '/profile', label: 'Officer Profile', icon: UserCheck },
  ];

  const isOfficerRoute = officerLinks.some(link => location.pathname.startsWith(link.to));

  return (
    <nav className="bg-blue-950 text-white text-xs border-y border-blue-900 shadow-xs sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 flex items-center justify-between">
        {/* Main Navigation Links */}
        <div className="flex items-center space-x-1 font-semibold">
          <NavLink
            to="/dashboard"
            className={({ isActive }) =>
              `px-3 py-2.5 transition flex items-center ${
                isActive ? 'bg-amber-600 text-white font-bold' : 'hover:bg-blue-900 text-slate-100'
              }`
            }
          >
            HOME
          </NavLink>

          <span className="px-2 py-2.5 text-slate-400 cursor-default hover:text-white transition">
            ABOUT DEPARTMENT
          </span>

          <span className="px-2 py-2.5 text-slate-400 cursor-default hover:text-white transition">
            SERVICES
          </span>

          <span className="px-2 py-2.5 text-slate-400 cursor-default hover:text-white transition">
            SCHEMES & PROGRAMS
          </span>

          <span className="px-2 py-2.5 text-slate-400 cursor-default hover:text-white transition">
            DOCUMENTS / RTI
          </span>

          {/* OFFICER SERVICES Dropdown Menu */}
          <div className="relative">
            <button
              onClick={() => setOfficerMenuOpen(!officerMenuOpen)}
              onMouseEnter={() => setOfficerMenuOpen(true)}
              className={`px-3 py-2.5 flex items-center space-x-1.5 transition font-bold ${
                isOfficerRoute ? 'bg-amber-600 text-white' : 'bg-blue-900 text-amber-300 hover:bg-blue-800'
              }`}
            >
              <Shield className="w-3.5 h-3.5" />
              <span>OFFICER SERVICES</span>
              <ChevronDown className="w-3.5 h-3.5" />
            </button>

            {officerMenuOpen && (
              <div
                onMouseLeave={() => setOfficerMenuOpen(false)}
                className="absolute left-0 mt-0 w-64 bg-white text-slate-900 rounded-b shadow-xl border border-slate-300 py-1 z-50 text-xs"
              >
                <div className="px-3 py-1.5 bg-slate-100 font-bold text-[10px] text-slate-500 uppercase tracking-wider border-b border-slate-200">
                  Officer Administrative Tasks
                </div>

                {officerLinks.map((link) => {
                  const Icon = link.icon;
                  return (
                    <NavLink
                      key={link.to}
                      to={link.to}
                      onClick={() => setOfficerMenuOpen(false)}
                      className={({ isActive }) =>
                        `flex items-center px-3 py-2 font-medium transition ${
                          isActive
                            ? 'bg-blue-50 text-blue-950 font-bold border-l-4 border-blue-900'
                            : 'text-slate-700 hover:bg-slate-100 hover:text-slate-900'
                        }`
                      }
                    >
                      <Icon className="w-3.5 h-3.5 mr-2 text-slate-500 shrink-0" />
                      <span>{link.label}</span>
                    </NavLink>
                  );
                })}
              </div>
            )}
          </div>
        </div>

        {/* Quick Officer Navigation Pills */}
        <div className="hidden md:flex items-center space-x-1 text-[11px]">
          <NavLink
            to="/requests"
            className={({ isActive }) =>
              `px-2.5 py-1 rounded transition ${
                isActive ? 'bg-amber-600 text-white font-bold' : 'bg-blue-900/60 hover:bg-blue-900 text-slate-200'
              }`
            }
          >
            Queue
          </NavLink>
          <NavLink
            to="/integration"
            className={({ isActive }) =>
              `px-2.5 py-1 rounded transition ${
                isActive ? 'bg-amber-600 text-white font-bold' : 'bg-blue-900/60 hover:bg-blue-900 text-slate-200'
              }`
            }
          >
            Interoperability
          </NavLink>
          <NavLink
            to="/consent"
            className={({ isActive }) =>
              `px-2.5 py-1 rounded transition ${
                isActive ? 'bg-amber-600 text-white font-bold' : 'bg-blue-900/60 hover:bg-blue-900 text-slate-200'
              }`
            }
          >
            Consent
          </NavLink>
          <NavLink
            to="/audit-logs"
            className={({ isActive }) =>
              `px-2.5 py-1 rounded transition ${
                isActive ? 'bg-amber-600 text-white font-bold' : 'bg-blue-900/60 hover:bg-blue-900 text-slate-200'
              }`
            }
          >
            Audit Logs
          </NavLink>
        </div>
      </div>
    </nav>
  );
};
