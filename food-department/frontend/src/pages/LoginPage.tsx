import React, { useState, useEffect } from 'react';
import { useAuth } from '../hooks/useAuth';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import { AlertCircle, User, Lock, ShieldCheck, Building2, ChevronRight, Home } from 'lucide-react';

export const LoginPage: React.FC = () => {
  const { login, user } = useAuth();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [expiredMsg, setExpiredMsg] = useState(false);

  useEffect(() => {
    if (user) {
      navigate('/dashboard');
    }
    if (searchParams.get('expired') === 'true') {
      setExpiredMsg(true);
    }
  }, [user, navigate, searchParams]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      await login(username, password);
      navigate('/dashboard');
    } catch (err: any) {
      const msg = err?.response?.data?.message || 'Invalid username or password.';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  const handleDemoFill = (u: string, p: string) => {
    setUsername(u);
    setPassword(p);
    setError(null);
  };

  return (
    <div className="w-full max-w-4xl bg-white border border-slate-300 rounded shadow-xs overflow-hidden">
      {/* Government Institutional Header Banner */}
      <div className="bg-slate-900 text-white p-4 border-b-2 border-amber-600 flex flex-col sm:flex-row sm:items-center justify-between gap-2">
        <div>
          <p className="text-[10px] font-bold uppercase tracking-wider text-slate-300">Government of Maharashtra • महाराष्ट्र शासन</p>
          <h1 className="text-base font-extrabold text-white leading-tight mt-0.5">
            अन्न, नागरी पुरवठा व ग्राहक संरक्षण विभाग
          </h1>
          <p className="text-xs font-bold text-amber-400">
            Food, Civil Supplies & Consumer Protection Department
          </p>
        </div>

        <div className="text-right shrink-0">
          <span className="text-[10px] uppercase font-bold px-2 py-0.5 rounded bg-slate-800 text-slate-300 border border-slate-700 font-mono">
            Department 2 Portal
          </span>
        </div>
      </div>

      {/* Government Breadcrumb Bar */}
      <div className="bg-slate-100 px-4 py-1.5 border-b border-slate-200 text-xs text-slate-600 flex items-center space-x-1 font-medium">
        <Home className="w-3.5 h-3.5 mr-1 text-slate-500" />
        <span>Home</span>
        <ChevronRight className="w-3 h-3 text-slate-400" />
        <span className="text-slate-900 font-semibold">Officer Login</span>
      </div>

      {/* Two-Column Government Content Area */}
      <div className="p-6 grid grid-cols-1 md:grid-cols-5 gap-6">
        {/* Left 3 Cols: Officer Login Form */}
        <div className="md:col-span-3 space-y-4 border-r border-slate-200 md:pr-6">
          <div className="border-b border-slate-200 pb-2">
            <h2 className="text-sm font-bold text-blue-950 uppercase tracking-wide">Officer Login</h2>
            <p className="text-xs text-slate-500">Authorized departmental personnel authentication</p>
          </div>

          {expiredMsg && (
            <div className="p-2.5 bg-amber-50 border border-amber-300 rounded text-xs text-amber-900 flex items-center gap-2">
              <AlertCircle className="w-4 h-4 text-amber-700 shrink-0" />
              <span>Your session has expired. Please sign in again.</span>
            </div>
          )}

          {error && (
            <div className="p-2.5 bg-rose-50 border border-rose-300 rounded text-xs text-rose-800 flex items-center gap-2">
              <AlertCircle className="w-4 h-4 text-rose-700 shrink-0" />
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4 text-xs">
            <div>
              <label className="block font-semibold text-slate-700 uppercase mb-1">
                Officer Username
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-2.5 flex items-center pointer-events-none text-slate-400">
                  <User className="w-4 h-4" />
                </div>
                <input
                  type="text"
                  required
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder="Enter official username (e.g. food.officer)"
                  className="w-full pl-8 pr-3 py-1.5 border border-slate-300 rounded text-xs focus:outline-none focus:ring-2 focus:ring-blue-900 bg-slate-50"
                />
              </div>
            </div>

            <div>
              <label className="block font-semibold text-slate-700 uppercase mb-1">
                Password
              </label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-2.5 flex items-center pointer-events-none text-slate-400">
                  <Lock className="w-4 h-4" />
                </div>
                <input
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="••••••••"
                  className="w-full pl-8 pr-3 py-1.5 border border-slate-300 rounded text-xs focus:outline-none focus:ring-2 focus:ring-blue-900 bg-slate-50"
                />
              </div>
            </div>

            <div className="flex items-center justify-between text-slate-600">
              <label className="flex items-center">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="rounded border-slate-300 text-blue-900 focus:ring-blue-900 mr-1.5"
                />
                Remember this device
              </label>
              <span className="text-slate-400 cursor-pointer hover:underline">Help / Support</span>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-2 px-4 bg-slate-900 hover:bg-slate-800 text-white font-bold text-xs rounded transition flex items-center justify-center gap-2 border border-slate-800 shadow-xs"
            >
              <ShieldCheck className="w-4 h-4 text-amber-400" />
              {loading ? 'Authenticating Officer...' : 'SIGN IN'}
            </button>
          </form>
        </div>

        {/* Right 2 Cols: Officer Portal Information Panel */}
        <div className="md:col-span-2 space-y-4 text-xs">
          <div className="border-b border-slate-200 pb-2">
            <h3 className="text-xs font-bold text-slate-900 uppercase tracking-wide">
              OFFICER PORTAL SPECIFICATION
            </h3>
            <p className="text-[11px] text-slate-500">Department 2 — GovMesh Demonstration</p>
          </div>

          <div className="space-y-2 text-slate-700 bg-slate-50 p-3 rounded border border-slate-200">
            <p className="font-semibold text-slate-900">Authorized department officers can access:</p>
            <ul className="space-y-1 text-[11px] list-disc list-inside text-slate-600">
              <li>Cross-departmental application processing</li>
              <li>Master Ration Record (RCMS) search & inspection</li>
              <li>Incoming interoperability request queue</li>
              <li>System audit trail logs & security monitoring</li>
            </ul>
          </div>

          {/* Compact Demo Accounts Notice */}
          <div className="pt-2">
            <div className="flex items-center justify-between text-[11px] font-bold text-slate-700 mb-1.5">
              <span className="flex items-center gap-1">
                <Building2 className="w-3.5 h-3.5 text-slate-500" />
                DEMO ENVIRONMENT ACCOUNTS
              </span>
            </div>

            <div className="space-y-1 text-[11px]">
              <button
                type="button"
                onClick={() => handleDemoFill('food.officer', 'Food@123')}
                className="w-full p-1.5 bg-white border border-slate-200 rounded text-left hover:bg-blue-50 hover:border-blue-300 transition flex justify-between items-center"
              >
                <span className="font-semibold text-slate-800">Food Supply Officer</span>
                <span className="font-mono text-slate-500 text-[10px]">food.officer</span>
              </button>

              <button
                type="button"
                onClick={() => handleDemoFill('senior.officer', 'Senior@123')}
                className="w-full p-1.5 bg-white border border-slate-200 rounded text-left hover:bg-blue-50 hover:border-blue-300 transition flex justify-between items-center"
              >
                <span className="font-semibold text-slate-800">Senior Officer</span>
                <span className="font-mono text-slate-500 text-[10px]">senior.officer</span>
              </button>

              <button
                type="button"
                onClick={() => handleDemoFill('food.admin', 'Admin@123')}
                className="w-full p-1.5 bg-white border border-slate-200 rounded text-left hover:bg-blue-50 hover:border-blue-300 transition flex justify-between items-center"
              >
                <span className="font-semibold text-slate-800">Department Admin</span>
                <span className="font-mono text-slate-500 text-[10px]">food.admin</span>
              </button>

              <button
                type="button"
                onClick={() => handleDemoFill('auditor', 'Auditor@123')}
                className="w-full p-1.5 bg-white border border-slate-200 rounded text-left hover:bg-blue-50 hover:border-blue-300 transition flex justify-between items-center"
              >
                <span className="font-semibold text-slate-800">Auditor (Read-Only)</span>
                <span className="font-mono text-slate-500 text-[10px]">auditor</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
