import React from 'react';
import { Outlet } from 'react-router-dom';

export const AuthLayout: React.FC = () => {
  return (
    <div className="min-h-screen flex flex-col justify-between bg-[#f8fafc] text-slate-900">
      {/* Institutional Top Bar */}
      <div className="bg-slate-900 text-white text-xs px-4 py-1.5 flex items-center justify-between border-b border-slate-800">
        <div className="flex items-center space-x-2 font-semibold">
          <span>GOVERNMENT OF MAHARASHTRA • महाराष्ट्र शासन</span>
          <span className="text-slate-500">|</span>
          <span className="text-slate-300">Food, Civil Supplies & Consumer Protection Department</span>
        </div>
        <div className="text-amber-400 font-mono text-[10px] uppercase font-bold">
          SIMULATED ENVIRONMENT • GOVMESH SIH 2026
        </div>
      </div>

      <main className="flex-1 flex items-center justify-center p-4 my-4">
        <Outlet />
      </main>

      <footer className="py-3 text-center text-xs text-slate-500 border-t border-slate-300 bg-white">
        <p className="font-semibold text-slate-700">Government of Maharashtra — Food, Civil Supplies & Consumer Protection Department</p>
        <p className="text-[10px] text-slate-500 font-mono mt-0.5">GovMesh SIH 2026 Department 2 Prototype • Officer Authentication System</p>
      </footer>
    </div>
  );
};
