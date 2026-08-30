import React from 'react';
import { Outlet } from 'react-router-dom';
import { Header } from './Header';
import { Navbar } from './Navbar';
import { Sidebar } from './Sidebar';

export const MainLayout: React.FC = () => {
  return (
    <div className="min-h-screen flex flex-col bg-white text-slate-900">
      {/* Header */}
      <Header unreadNotificationCount={2} />

      {/* Horizontal Government Navigation Bar */}
      <Navbar />

      {/* Main Content Area */}
      <div className="flex-1 max-w-7xl w-full mx-auto my-0 sm:my-3 border-x sm:border border-slate-300 bg-white rounded-none sm:rounded overflow-hidden shadow-xs flex">
        {/* Secondary Sidebar (Visible on LG screens) */}
        <Sidebar />

        {/* Dynamic Router Outlet */}
        <main id="main-content" className="flex-1 p-3 sm:p-4 bg-[#f8fafc] overflow-y-auto min-h-[calc(100vh-10rem)]">
          <Outlet />
        </main>
      </div>

      {/* Official Government Footer */}
      <footer className="bg-slate-900 text-slate-300 text-xs py-4 border-t border-slate-800 mt-auto">
        <div className="max-w-7xl mx-auto px-4 grid grid-cols-1 sm:grid-cols-3 gap-4 pb-3 border-b border-slate-800">
          <div>
            <h3 className="font-bold text-white mb-1">Government of Maharashtra</h3>
            <p className="text-[11px] text-slate-400">
              Food, Civil Supplies & Consumer Protection Department
            </p>
            <p className="text-[10px] text-amber-400 font-mono mt-1">
              GovMesh SIH 2026 Interoperability Framework (Department 2 Prototype)
            </p>
          </div>

          <div>
            <h4 className="font-semibold text-slate-200 mb-1 text-[11px]">Important Department Links</h4>
            <ul className="text-[11px] text-slate-400 space-y-0.5">
              <li>• Public Distribution System (PDS / RCMS)</li>
              <li>• Right to Information Act (RTI)</li>
              <li>• Citizen Charter & Grievance Redressal</li>
              <li>• National Food Security Act (NFSA)</li>
            </ul>
          </div>

          <div>
            <h4 className="font-semibold text-slate-200 mb-1 text-[11px]">System Environment</h4>
            <p className="text-[11px] text-slate-400 leading-snug">
              This system is a simulated department demonstration node for the GovMesh SIH 2026 interoperability competition.
            </p>
            <p className="text-[10px] text-slate-500 font-mono mt-1">
              Version 1.0 • Built for Government Officer Workstations
            </p>
          </div>
        </div>

        <div className="max-w-7xl mx-auto px-4 pt-2 text-center text-[10px] text-slate-500">
          Website Content Managed by Food, Civil Supplies & Consumer Protection Department, Government of Maharashtra • Simulated Prototype Environment
        </div>
      </footer>
    </div>
  );
};
