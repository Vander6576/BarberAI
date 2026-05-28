'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { 
  Scissors, 
  Mail, 
  Lock, 
  Chrome, 
  ArrowRight,
  Sparkles,
  AlertCircle
} from 'lucide-react';

export default function AdminLoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !password) {
      setError('Por favor, preencha todos os campos!');
      return;
    }
    
    setError('');
    setLoading(true);

    // Simulate safe B2B authentication login
    setTimeout(() => {
      setLoading(false);
      // Simulating redirecting to the B2B dashboard
      window.location.href = '/admin/dashboard';
    }, 1500);
  };

  const handleGoogleLogin = () => {
    setLoading(true);
    setTimeout(() => {
      setLoading(false);
      window.location.href = '/admin/dashboard';
    }, 1200);
  };

  return (
    <main className="min-h-screen bg-background text-foreground flex flex-col justify-center items-center p-4 max-w-md mx-auto relative shadow-2xl border-x border-card">
      
      {/* Visual background ambient details */}
      <div className="absolute top-1/4 left-1/4 w-32 h-32 bg-gold/5 rounded-full filter blur-2xl pointer-events-none" />
      <div className="absolute bottom-1/4 right-1/4 w-32 h-32 bg-gold/5 rounded-full filter blur-2xl pointer-events-none" />

      <div className="w-full space-y-8 relative z-10">
        
        {/* Brand Header */}
        <div className="flex flex-col items-center space-y-3 text-center">
          <div className="w-12 h-12 bg-gradient-to-tr from-gold to-gold-light rounded-2xl flex items-center justify-center text-background shadow-lg shadow-gold/25">
            <Scissors className="w-6 h-6 text-[#0d0e12]" />
          </div>
          <div className="space-y-1">
            <h1 className="text-2xl font-black text-white tracking-tight">BarberFlow SaaS</h1>
            <p className="text-xs text-gray-400">Sistema Operacional & Hub Financeiro de Gigantes</p>
          </div>
        </div>

        {/* Card Frame containing Email/Password */}
        <div className="bg-card border border-gray-800 rounded-2xl p-6 shadow-xl space-y-6">
          <div className="flex border-b border-gray-800 pb-4 justify-between items-center">
            <h2 className="text-sm font-bold text-gray-200">Acesse sua Conta</h2>
            <span className="text-[10px] text-gray-400 tracking-wider">B2B TENANT SECURE</span>
          </div>

          {error && (
            <div className="bg-red-500/10 border border-red-500/30 text-red-400 text-xs p-3 rounded-lg flex items-center gap-2">
              <AlertCircle className="w-4 h-4 shrink-0" />
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleLogin} className="space-y-4">
            <div className="space-y-1.5">
              <label className="text-[10px] text-gray-400 font-bold tracking-wider uppercase block">E-mail Corporativo</label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
                <input 
                  type="email" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="seu@email.com"
                  className="w-full bg-background border border-gray-800 rounded-xl p-3 pl-10 text-xs text-gray-100 focus:outline-none focus:border-gold placeholder:text-gray-600"
                />
              </div>
            </div>

            <div className="space-y-1.5">
              <label className="text-[10px] text-gray-400 font-bold tracking-wider uppercase block">Senha</label>
              <div className="relative">
                <Lock className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-500" />
                <input 
                  type="password" 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="******"
                  className="w-full bg-background border border-gray-800 rounded-xl p-3 pl-10 text-xs text-gray-100 focus:outline-none focus:border-gold placeholder:text-gray-600"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-gold hover:bg-gold-light disabled:bg-gold/45 text-background font-bold py-3.5 rounded-xl text-xs flex items-center justify-center gap-2 transition-all mt-6 shadow-lg shadow-gold/10"
            >
              {loading ? 'Acessando Banco...' : 'Acessar Painel Master'}
              {!loading && <ArrowRight className="w-4 h-4" />}
            </button>
          </form>

          {/* Social login divider line */}
          <div className="flex items-center gap-3 py-2 text-[10px] text-gray-500 font-bold uppercase tracking-wider">
            <div className="h-[1px] bg-gray-800 flex-grow" />
            <span>ou continue com</span>
            <div className="h-[1px] bg-gray-800 flex-grow" />
          </div>

          <button
            onClick={handleGoogleLogin}
            disabled={loading}
            className="w-full bg-background hover:bg-card/40 text-gray-200 border border-gray-800 font-bold py-3 rounded-xl text-xs flex items-center justify-center gap-2 transition-all"
          >
            <Chrome className="w-4 h-4 text-red-400" />
            Acessar com Google Account
          </button>
        </div>

        {/* Sub-footer instructions on registration */}
        <p className="text-center text-[11px] text-gray-500">
          Não possui uma assinatura BarberFlow?{' '}
          <a href="/" className="text-gold underline hover:text-gold-light">
            Simular link de agendamentos
          </a>
        </p>

      </div>
    </main>
  );
}
