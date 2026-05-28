'use client';

import React, { useState } from 'react';
import { 
  BarChart3, 
  Calendar, 
  Users, 
  Scissors, 
  DollarSign, 
  Settings, 
  Sparkles, 
  Check, 
  UserX, 
  Trash2, 
  Plus, 
  Send,
  HelpCircle,
  TrendingUp,
  CreditCard,
  MessageCircle,
  LogOut
} from 'lucide-react';

export default function AdminDashboard() {
  const [activeTab, setActiveTab] = useState('dashboard'); // dashboard, agenda, clientes, financeiro, configs
  
  // STATS STATE DYNAMIC CO-RELATIONS
  const [barbers, setBarbers] = useState([
    { id: 1, name: 'Carlinhos Degradê', phone: '(11) 98877-6655', commission: 50, avatar: 'C' },
    { id: 2, name: 'Thiago Navalha', phone: '(11) 97766-5544', commission: 60, avatar: 'T' }
  ]);

  const [services, setServices] = useState([
    { name: 'Corte Degradê', price: 45.0, duration: '30 min', category: 'Corte' },
    { name: 'Barba Espatulada', price: 30.0, duration: '30 min', category: 'Barba' },
    { name: 'Combo Cabelo + Barba', price: 70.0, duration: '60 min', category: 'Combo' },
    { name: 'Sobrancelha Navalhada', price: 15.0, duration: '15 min', category: 'Estética' }
  ]);

  const [customers, setCustomers] = useState([
    { id: 101, name: 'Marcos Ribeiro', phone: '(11) 99123-4567', birthday: '12/04', freq: 8, notes: 'Prefere degradê navalhado. Gosta de café expresso.', lastCut: '2026-05-21' },
    { id: 102, name: 'Felipe Souza', phone: '(11) 98124-7654', birthday: '25/08', freq: 3, notes: 'Usar pós-barba de menta. Cabelo crespo.', lastCut: '2026-05-14' },
    { id: 103, name: 'Lucas Santos', phone: '(11) 99988-1234', birthday: '14/01', freq: 14, notes: 'Corta semanalmente às segundas.', lastCut: '2026-05-23' }
  ]);

  const [appointments, setAppointments] = useState([
    { id: 1, guestName: 'Marcos Ribeiro', phone: '(11) 99123-4567', timeSlot: '09:00', serviceName: 'Corte Degradê', cost: 45.0, status: 'Finalizado', barberId: 1, paid: true },
    { id: 2, guestName: 'Felipe Souza', phone: '(11) 98124-7654', timeSlot: '10:30', serviceName: 'Combo Cabelo + Barba', cost: 70.0, status: 'Confirmado', barberId: 2, paid: false, requireSignal: true },
    { id: 3, guestName: 'Carlos Walk-in', phone: '(11) 99000-1111', timeSlot: '13:00', serviceName: 'Barba Espatulada', cost: 30.0, status: 'Pendente', barberId: 1, paid: false },
    { id: 4, guestName: 'Rodrigo Alinhado', phone: '(11) 97111-2222', timeSlot: '15:00', serviceName: 'Corte Degradê', cost: 45.0, status: 'Falta', barberId: 2, paid: false }
  ]);

  const [financials, setFinancials] = useState([
    { desc: 'Serviço Corte Degradê (Marcos)', amount: 45.0, type: 'Receita', category: 'Corte', date: 'Hoje' },
    { desc: 'Comissão Carlinhos Degradê (50%)', amount: 22.5, type: 'Despesa', category: 'Comissão', date: 'Hoje' },
    { desc: 'Luz & Energia Elétrica', amount: 150.0, type: 'Despesa', category: 'Infra', date: 'Hoje' }
  ]);

  // ADD MODAL STATES
  const [showAddAppt, setShowAddAppt] = useState(false);
  const [showAddCust, setShowAddCust] = useState(false);
  const [showAddBarber, setShowAddBarber] = useState(false);
  const [showAiSlots, setShowAiSlots] = useState(false);
  const [aiSlotsText, setAiSlotsText] = useState('');
  const [aiLoading, setAiLoading] = useState(false);

  // FORM OBJECTS
  const [newApptName, setNewApptName] = useState('');
  const [newApptPhone, setNewApptPhone] = useState('');
  const [newApptService, setNewApptService] = useState('Corte Degradê');
  const [newApptTime, setNewApptTime] = useState('09:30');
  const [newApptBarber, setNewApptBarber] = useState(1);
  const [newApptSignal, setNewApptSignal] = useState(false);

  const [newCustName, setNewCustName] = useState('');
  const [newCustPhone, setNewCustPhone] = useState('');
  const [newCustBirthday, setNewCustBirthday] = useState('15/10');
  const [newCustNotes, setNewCustNotes] = useState('');

  const [newBarberName, setNewBarberName] = useState('');
  const [newBarberPhone, setNewBarberPhone] = useState('');
  const [newBarberCommission, setNewBarberCommission] = useState(50);

  const [newExpenseName, setNewExpenseName] = useState('');
  const [newExpenseValue, setNewExpenseValue] = useState('');
  const [expenseCategory, setExpenseCategory] = useState('Suprimentos');

  // CONFIGURATIONS STATES
  const [openTime, setOpenTime] = useState('08:00');
  const [closeTime, setCloseTime] = useState('19:30');
  const [pixKey, setPixKey] = useState('comercial@barberflow.com');
  const [whatsPivot, setWhatsPivot] = useState('+5511988887777');

  // DERIVED QUANTITIES
  const totalReceita = appointments
    .filter(a => a.status === 'Finalizado')
    .reduce((sum, current) => sum + current.cost, 0);

  const totalDespesaManual = financials
    .filter(f => f.type === 'Despesa')
    .reduce((sum, current) => sum + current.amount, 0);

  const saldoLiquido = totalReceita - totalDespesaManual;

  const totalAgendasCompleto = appointments.length;
  const noShowCalculo = appointments.filter(a => a.status === 'Falta').length;
  const noShowPercentual = totalAgendasCompleto > 0 ? Math.round((noShowCalculo / totalAgendasCompleto) * 100) : 0;

  // ACTIONS
  const handleCompleteAppointment = (app: any) => {
    // 1. Update status to finalized
    setAppointments(prev => prev.map(a => a.id === app.id ? { ...a, status: 'Finalizado', paid: true } : a));
    
    // 2. Add income to ledger
    const newRecordIncome = {
      desc: `Corte ${app.serviceName} - ${app.guestName} (Finalizado)`,
      amount: app.cost,
      type: 'Receita',
      category: 'Corte',
      date: 'Hoje'
    };

    // 3. Subtract auto commission matching barber
    const allocatedBarber = barbers.find(b => b.id === app.barberId);
    const splitRatio = allocatedBarber ? allocatedBarber.commission : 50;
    const commissionDebit = app.cost * (splitRatio / 100);
    const newRecordExpense = {
      desc: `Comissão ${allocatedBarber?.name || 'Barbeiro'} (${splitRatio}%)`,
      amount: commissionDebit,
      type: 'Despesa',
      category: 'Comissão',
      date: 'Hoje'
    };

    setFinancials(prev => [...prev, newRecordIncome, newRecordExpense]);
  };

  const handleMissAppointment = (appId: number) => {
    setAppointments(prev => prev.map(a => a.id === appId ? { ...a, status: 'Falta' } : a));
  };

  const handleDeleteAppointment = (appId: number) => {
    setAppointments(prev => prev.filter(a => a.id !== appId));
  };

  const handleSaveAppt = () => {
    if (!newApptName || !newApptPhone) {
      alert('Digite nome e fone');
      return;
    }
    const matchedService = services.find(s => s.name === newApptService);
    const costValue = matchedService ? matchedService.price : 45.0;

    const newAppObj = {
      id: Date.now(),
      guestName: newApptName,
      phone: newApptPhone,
      timeSlot: newApptTime,
      serviceName: newApptService,
      cost: costValue,
      status: 'Confirmado',
      barberId: Number(newApptBarber),
      paid: false,
      requireSignal: newApptSignal
    };

    setAppointments(prev => [...prev, newAppObj]);
    setShowAddAppt(false);
    setNewApptName('');
    setNewApptPhone('');
  };

  const handleSaveCustomer = () => {
    if (!newCustName || !newCustPhone) {
      alert('Preencha nome e telefone!');
      return;
    }
    const newCust = {
      id: Date.now(),
      name: newCustName,
      phone: newCustPhone,
      birthday: newCustBirthday,
      freq: 0,
      notes: newCustNotes,
      lastCut: 'Nunca'
    };
    setCustomers(prev => [...prev, newCust]);
    setShowAddCust(false);
    setNewCustName('');
    setNewCustPhone('');
  };

  const handleSaveBarber = () => {
    if (!newBarberName || !newBarberPhone) {
      alert('Nome e fone obrigatórios!');
      return;
    }
    const n = {
      id: Date.now(),
      name: newBarberName,
      phone: newBarberPhone,
      commission: Number(newBarberCommission),
      avatar: newBarberName.charAt(0).toUpperCase()
    };
    setBarbers(prev => [...prev, n]);
    setShowAddBarber(false);
    setNewBarberName('');
    setNewBarberPhone('');
  };

  const handleAddExpenseManualInput = () => {
    if (!newExpenseName || !newExpenseValue) {
      alert('Nome e valor da despesa!');
      return;
    }
    const n = {
      desc: newExpenseName,
      amount: Number(newExpenseValue),
      type: 'Despesa',
      category: expenseCategory,
      date: 'Hoje'
    };
    setFinancials(prev => [...prev, n]);
    setNewExpenseName('');
    setNewExpenseValue('');
  };

  const handleAiRecommendSlots = () => {
    setAiLoading(true);
    setShowAiSlots(true);
    setTimeout(() => {
      setAiSlotsText(`💡 RECOMENDAÇÕES PARA HOJE:
- Janela 1: 14:00h às 14:45h (vaga deCarlinhos) - Estratégica para profissionais na hora de folga da tarde.
- Janela 2: 16:30h às 17:15h (vaga de Thiago) - Ótimo momento para disparar SMS para clientes que trabalham em home office vizinhos.
- Janela 3: 11:15h às 12:00h - Janela clássica para executivos que cortam minutos antes do almoço.

Análise finalizada: Lance uma campanha com o cupom "CRISEZERO" dando R$5 de desconto nesses 3 horários no WhatsApp!`);
      setLoading(false);
      setAiLoading(false);
    }, 1500);
  };

  return (
    <div className="min-h-screen bg-[#0d0e12] text-foreground flex flex-col md:flex-row max-w-6xl mx-auto shadow-2xl border-x border-gray-900">
      
      {/* SIDEBAR NAVIGATION PANEL */}
      <aside className="w-full md:w-64 bg-[#14161f] p-5 border-b md:border-b-0 md:border-r border-gray-900 flex flex-col justify-between shrink-0">
        <div className="space-y-8">
          
          {/* Main Logo Header */}
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-gold to-gold-light flex items-center justify-center text-background font-black shadow-lg shadow-gold/20">
              <Scissors className="w-5 h-5 text-[#0d0e12]" />
            </div>
            <div>
              <h2 className="text-md font-extrabold text-[#f3f4f6]">BarberFlow</h2>
              <p className="text-[10px] text-gold font-bold tracking-widest">SaaS DASHBOARD</p>
            </div>
          </div>

          {/* Navigation Menu Links */}
          <nav className="space-y-1.5">
            {[
              { id: 'dashboard', label: 'Monitor de Métricas', icon: BarChart3 },
              { id: 'agenda', label: 'Calendário & Agenda', icon: Calendar },
              { id: 'clientes', label: 'Fichas de Clientes', icon: Users },
              { id: 'financeiro', label: 'Livro de Caixa', icon: DollarSign },
              { id: 'configs', label: 'Configurações', icon: Settings }
            ].map((menu) => {
              const IconComp = menu.icon;
              return (
                <button
                  key={menu.id}
                  onClick={() => setActiveTab(menu.id)}
                  className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-xl text-xs font-semibold transition-all ${
                    activeTab === menu.id 
                      ? 'bg-gold text-background shadow shadow-gold/15' 
                      : 'text-gray-400 hover:text-gray-100 hover:bg-[#1a1c27]'
                  }`}
                >
                  <IconComp className="w-4 h-4" />
                  <span>{menu.label}</span>
                </button>
              );
            })}
          </nav>
        </div>

        {/* Workspace Footer Profile */}
        <div className="pt-4 border-t border-gray-800/60 mt-8 flex justify-between items-center text-xs">
          <div className="flex items-center gap-2">
            <div className="w-7 h-7 rounded-full bg-gray-800 flex items-center justify-center text-gold font-bold">
              B
            </div>
            <div>
              <p className="text-gray-200 font-bold">CEO Barbeiro</p>
              <p className="text-[9px] text-gray-500">Plan: Platinum Max</p>
            </div>
          </div>
          <button 
            onClick={() => window.location.href = '/admin'}
            className="p-1.5 hover:bg-gray-800 rounded-lg text-gray-400 hover:text-red-400"
          >
            <LogOut className="w-4 h-4" />
          </button>
        </div>
      </aside>

      {/* CORE FRAME CONTAINER FOR DYNAMIC TABS */}
      <section className="flex-grow p-6 space-y-6 overflow-y-auto">
        
        {/* UPPER TITLE ROW WITH INSTANT SHORTCUTS */}
        <div className="flex flex-col md:flex-row md:justify-between md:items-center gap-4 pb-4 border-b border-gray-900">
          <div>
            <h1 className="text-xl font-extrabold text-white flex items-center gap-2">
              {activeTab === 'dashboard' && 'Painel Master Consolidado 📊'}
              {activeTab === 'agenda' && 'Gestão de Horários & Encaixes 📅'}
              {activeTab === 'clientes' && 'CRM Relacionamento Recorrência 👥'}
              {activeTab === 'financeiro' && 'Caixa, Lucros & Split Auto 💰'}
              {activeTab === 'configs' && 'Operação & Setup B2B ⚙️'}
            </h1>
            <p className="text-xs text-gray-400">Dados síncronos da barbearia atualizados em tempo real.</p>
          </div>

          <div className="flex gap-2">
            <button
              onClick={() => handleAiRecommendSlots()}
              className="bg-purple-900/40 hover:bg-purple-900/60 text-purple-300 border border-purple-500/30 text-xs font-bold px-3 py-2 rounded-xl flex items-center gap-1.5 transition-all"
            >
              <Sparkles className="w-3.5 h-3.5" />
              Sugerir Encaixes IA
            </button>
            <button
              onClick={() => {
                if (activeTab === 'clientes') setShowAddCust(true);
                else setShowAddAppt(true);
              }}
              className="bg-gold hover:bg-gold-light text-background text-xs font-bold px-3.5 py-2 rounded-xl flex items-center gap-1.5 transition-all"
            >
              <Plus className="w-4 h-4" />
              Novo Lançamento
            </button>
          </div>
        </div>

        {/* TAB 1: DASHBOARD METRICS */}
        {activeTab === 'dashboard' && (
          <div className="space-y-6 animate-fade-in">
            {/* Nubank/iFood styled balance grids */}
            <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
              <div className="bg-[#16181f] p-4.5 rounded-2xl border border-gray-800 space-y-4 shadow flex flex-col justify-between h-[120px]">
                <div className="flex justify-between items-center text-xs">
                  <span className="text-gray-400 font-medium">Lucro Líquido Hoje</span>
                  <DollarSign className="w-4 h-4 text-emerald" />
                </div>
                <div>
                  <h3 className="text-md text-gray-400 text-[11px] uppercase tracking-wider">SALDO DO DIA</h3>
                  <p className="text-xl font-black text-white">R$ {saldoLiquido.toFixed(2)}</p>
                </div>
              </div>

              <div className="bg-[#16181f] p-4.5 rounded-2xl border border-gray-800 space-y-4 shadow flex flex-col justify-between h-[120px]">
                <div className="flex justify-between items-center text-xs">
                  <span className="text-gray-400 font-medium">Reclames Feitos</span>
                  <Calendar className="w-4 h-4 text-gold" />
                </div>
                <div>
                  <h3 className="text-md text-gray-400 text-[11px] uppercase tracking-wider">FILA ATIVA</h3>
                  <p className="text-xl font-black text-white">
                    {appointments.filter(a => a.status === 'Confirmado' || a.status === 'Pendente').length} / {totalAgendasCompleto}
                  </p>
                </div>
              </div>

              <div className="bg-[#16181f] p-4.5 rounded-2xl border border-gray-800 space-y-4 shadow flex flex-col justify-between h-[120px]">
                <div className="flex justify-between items-center text-xs">
                  <span className="text-gray-400 font-medium">Faltas (No-Show)</span>
                  <UserX className="w-4 h-4 text-ruby" />
                </div>
                <div>
                  <h3 className="text-md text-gray-400 text-[11px] uppercase tracking-wider">TAXA PERDIDA</h3>
                  <p className={`text-xl font-black ${noShowPercentual > 20 ? 'text-ruby' : 'text-orange-400'}`}>
                    {noShowPercentual}%
                  </p>
                </div>
              </div>

              <div className="bg-[#16181f] p-4.5 rounded-2xl border border-gray-800 space-y-4 shadow flex flex-col justify-between h-[120px]">
                <div className="flex justify-between items-center text-xs">
                  <span className="text-gray-400 font-medium">Faturamento Líquido</span>
                  <TrendingUp className="w-4 h-4 text-emerald" />
                </div>
                <div>
                  <h3 className="text-md text-gray-400 text-[11px] uppercase tracking-wider">ENTRADA GERAL</h3>
                  <p className="text-xl font-black text-emerald">R$ {totalReceita.toFixed(2)}</p>
                </div>
              </div>
            </div>

            {/* TWO COLUMN SUB-PANELS */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              
              {/* Left Column: Direct Action agenda list */}
              <div className="bg-[#16181f] p-5 rounded-2xl border border-gray-800 space-y-4">
                <h3 className="text-sm font-bold text-gray-200">Próximos Clientes para Atendimento</h3>
                <div className="space-y-3">
                  {appointments.filter(a => a.status !== 'Finalizado' && a.status !== 'Falta').map(app => (
                    <div 
                      key={app.id}
                      className="bg-[#0d0e12] p-3.5 rounded-xl border border-gray-800 flex justify-between items-center text-xs"
                    >
                      <div className="space-y-1">
                        <div className="flex items-center gap-2">
                          <span className="font-bold text-gold bg-gold/10 px-2 py-0.5 rounded text-[10px]">{app.timeSlot}</span>
                          <span className="font-bold text-gray-200">{app.guestName}</span>
                        </div>
                        <p className="text-gray-400">{app.serviceName} • R$ {app.cost.toFixed(2)} • Atendido por {barbers.find(b => b.id === app.barberId)?.name}</p>
                      </div>

                      <div className="flex gap-1.5 shrink-0">
                        <button
                          onClick={() => handleCompleteAppointment(app)}
                          className="bg-emerald/10 hover:bg-emerald/20 text-emerald p-2 rounded-lg border border-emerald/20 transition-all"
                          title="Finalizar atendimento"
                        >
                          <Check className="w-3.5 h-3.5" />
                        </button>
                        <button
                          onClick={() => handleMissAppointment(app.id)}
                          className="bg-ruby/15 hover:bg-ruby/25 text-ruby p-2 rounded-lg border border-ruby/15 transition-all"
                          title="Marcar falta"
                        >
                          <UserX className="w-3.5 h-3.5" />
                        </button>
                      </div>
                    </div>
                  ))}
                  {appointments.filter(a => a.status !== 'Finalizado' && a.status !== 'Falta').length === 0 && (
                    <p className="text-gray-500 text-sm text-center py-6">Nenhum atendimento na fila ativa.</p>
                  )}
                </div>
              </div>

              {/* Right Column: Dynamic financial entries log */}
              <div className="bg-[#16181f] p-5 rounded-2xl border border-gray-800 space-y-4">
                <h3 className="text-sm font-bold text-gray-200">Lançamentos Recentes em Caixa</h3>
                <div className="space-y-2.5 max-h-[290px] overflow-y-auto pr-1">
                  {financials.map((f, idx) => (
                    <div 
                      key={idx}
                      className="bg-[#0d0e12] p-3 rounded-xl border border-gray-800/60 flex justify-between items-center text-xs"
                    >
                      <div>
                        <p className="font-bold text-gray-100">{f.desc}</p>
                        <span className="text-[9px] text-gold bg-gold/10 px-1.5 py-0.5 rounded mr-1.5">{f.category}</span>
                        <span className="text-gray-500 text-[10px]">{f.date}</span>
                      </div>
                      <span className={`font-bold ${f.type === 'Receita' ? 'text-emerald' : 'text-ruby'}`}>
                        {f.type === 'Receita' ? '+' : '-'} R$ {f.amount.toFixed(2)}
                      </span>
                    </div>
                  ))}
                </div>
              </div>

            </div>
          </div>
        )}

        {/* TAB 2: CALENDAR & AGENDA DETAIL */}
        {activeTab === 'agenda' && (
          <div className="space-y-6 animate-fade-in">
            <div className="bg-[#16181f] p-5 rounded-2xl border border-gray-800 space-y-4">
              <div className="flex justify-between items-center">
                <h3 className="text-sm font-bold text-gray-200">Janelas Horárias de Trabalho</h3>
                <span className="text-xs text-gold">Funcionamento: {openTime} às {closeTime}</span>
              </div>

              <div className="space-y-3">
                {appointments.map(app => (
                  <div key={app.id} className="bg-[#0d0e12] p-4 rounded-xl border border-gray-800 flex justify-between items-center text-xs">
                    <div>
                      <div className="flex items-center gap-2">
                        <span className="font-bold text-gold bg-[#16181f] px-2 py-1 rounded text-[10px]">{app.timeSlot}</span>
                        <p className="font-extrabold text-white text-sm">{app.guestName}</p>
                      </div>
                      <p className="text-gray-400 mt-1">Celular: {app.phone} • Serviço: <strong className="text-gray-300">{app.serviceName}</strong> • Barber: {barbers.find(b => b.id === app.barberId)?.name}</p>
                      
                      {/* Signal pill if active */}
                      {app.requireSignal && (
                        <span className="text-[9.px] font-bold text-emerald bg-emerald/15 px-2 py-0.5 rounded block w-fit mt-1.5 border border-emerald/10">SINAL 30% CONFIRMADO PIX</span>
                      )}
                    </div>

                    <div className="flex items-center gap-2">
                      <span className={`px-2 py-0.5 rounded-[6px] font-bold text-[10px] ${
                        app.status === 'Finalizado' ? 'bg-emerald/10 text-emerald' : 
                        app.status === 'Falta' ? 'bg-ruby/15 text-ruby' : 'bg-orange-400/10 text-orange-400'
                      }`}>
                        {app.status}
                      </span>
                      <button 
                        onClick={() => handleDeleteAppointment(app.id)}
                        className="p-1.5 bg-card/40 border border-gray-800 hover:text-red-400 rounded-lg"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* TAB 3: CLIENT CRM */}
        {activeTab === 'clientes' && (
          <div className="space-y-6 animate-fade-in">
            <div className="bg-[#16181f] p-5 rounded-2xl border border-gray-800 space-y-4">
              <h3 className="text-sm font-bold text-gray-200">Base Ativa de Clientes</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {customers.map(c => {
                  const whatsText = `Fala ${c.name}! Blz? Notamos que fez ${c.freq} cortes e o último foi ${c.lastCut}. Que tal dar aquele trato no cabelo de novo? Agende aqui: https://barberflow.com/`;
                  const encodedText = encodeURIComponent(whatsText);
                  return (
                    <div key={c.id} className="bg-[#0d0e12] p-4.5 rounded-xl border border-gray-800 space-y-3">
                      <div className="flex justify-between items-start">
                        <div>
                          <h4 className="font-black text-white text-sm">{c.name}</h4>
                          <p className="text-[11px] text-gray-400">Fone: {c.phone} | Aniv: {c.birthday}</p>
                        </div>
                        <span className="text-[9px] font-bold text-gold bg-gold/10 px-2 py-0.5 rounded-full">
                          {c.freq} atendimentos
                        </span>
                      </div>

                      {c.notes && (
                        <p className="text-[11px] text-gray-400 bg-card/60 p-2 rounded-lg border border-gray-800 italic">
                          💡 Preferência: {c.notes}
                        </p>
                      )}

                      <div className="flex justify-between items-center text-xs pt-1">
                        <span className="text-gray-500 font-medium">Último corte: {c.lastCut}</span>
                        <a
                          href={`https://api.whatsapp.com/send?phone=${c.phone}&text=${encodedText}`}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="bg-emerald/15 hover:bg-emerald/20 text-emerald font-bold px-2.5 py-1.5 rounded-lg border border-emerald/20 flex items-center gap-1 text-[11px]"
                        >
                          <MessageCircle className="w-3.5 h-3.5" />
                          Cobrar whatsapp
                        </a>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        )}

        {/* TAB 4: FINANCIAL FLOW LOGGER */}
        {activeTab === 'financeiro' && (
          <div className="space-y-6 animate-fade-in">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              
              {/* Left Form: Log raw expenses */}
              <div className="bg-[#16181f] p-5 rounded-2xl border border-gray-800 space-y-4">
                <h3 className="text-sm font-bold text-gray-200 flex items-center gap-2">
                  <DollarSign className="w-4 h-4 text-ruby" />
                  Lançar Despesa Operacional
                </h3>

                <div className="space-y-3">
                  <div className="space-y-1">
                    <label className="text-xs text-gray-400">Nome do Débito</label>
                    <input 
                      type="text" 
                      value={newExpenseName}
                      onChange={(e) => setNewExpenseName(e.target.value)}
                      placeholder="Ex: Novo shampoo, Gel Cola, Aluguel"
                      className="w-full bg-background border border-gray-800 rounded-lg p-2 text-xs focus:outline-none focus:border-gold"
                    />
                  </div>

                  <div className="space-y-1">
                    <label className="text-xs text-gray-400">Valor (R$)</label>
                    <input 
                      type="number" 
                      value={newExpenseValue}
                      onChange={(e) => setNewExpenseValue(e.target.value)}
                      placeholder="Ex: 80.00"
                      className="w-full bg-background border border-gray-800 rounded-lg p-2 text-xs focus:outline-none focus:border-gold"
                    />
                  </div>

                  <div className="space-y-1">
                    <label className="text-xs text-gray-400">Categoria</label>
                    <select 
                      value={expenseCategory}
                      onChange={(e) => setExpenseCategory(e.target.value)}
                      className="w-full bg-background border border-gray-800 rounded-lg p-2 text-xs text-gray-200 focus:outline-none"
                    >
                      <option value="Suprimentos">Suprimentos</option>
                      <option value="Aluguel">Aluguel</option>
                      <option value="Serviços">Energia/Luz/Água</option>
                      <option value="Marketing">Marketing/Tráfego</option>
                    </select>
                  </div>

                  <button
                    onClick={() => handleAddExpenseManualInput()}
                    className="w-full bg-ruby/15 hover:bg-ruby/25 text-ruby border border-ruby/30 font-bold p-2.5 rounded-lg text-xs"
                  >
                    Salvar Saída Financeira
                  </button>
                </div>
              </div>

              {/* Right panel: SaaS monetization strategy metrics calculator */}
              <div className="bg-[#16181f] p-5 rounded-2xl border border-gray-800 space-y-4 flex flex-col justify-between">
                <div>
                  <h3 className="text-sm font-bold text-gray-200">SaaS Estimator de Planos Brasil</h3>
                  <p className="text-[11px] text-gray-400 mt-1 line-clamp-2">Monetização vendendo assinaturas B2B recorrentes mensais para barbearias tradicionais.</p>
                </div>

                <div className="space-y-3 bg-background/50 p-4 rounded-xl border border-gray-850">
                  <div className="flex justify-between border-b border-gray-800 pb-2 text-xs">
                    <span className="text-gray-400 font-medium">100 ASSINANTES B2B (Média R$89,90)</span>
                    <strong className="text-emerald">R$ 8.990,00/mês</strong>
                  </div>
                  <div className="flex justify-between border-b border-gray-800 pb-2 text-xs">
                    <span className="text-gray-400 font-medium">Margem Operacional Estimada</span>
                    <strong className="text-gold">91.5% Lucro Líquido</strong>
                  </div>
                  <div className="flex justify-between text-xs">
                    <span className="text-gray-400 font-medium">Custos Servidores + APIs Supabase</span>
                    <strong className="text-ruby">~ R$ 850,00/mês</strong>
                  </div>
                </div>

                <div className="text-[10px] text-gray-500 font-bold leading-relaxed bg-[#0d0e12] p-2 rounded">
                  💡 INFRA SAAS: Utilizando Next.js na Vercel (Gratuito inicialmente) e Supabase (Plano grátis até 10mil registros). O custo de escala é extremamente baixo.
                </div>
              </div>

            </div>
          </div>
        )}

        {/* TAB 5: CENTRAL CONFIGURATIONS */}
        {activeTab === 'configs' && (
          <div className="space-y-6 animate-fade-in">
            <div className="bg-[#16181f] p-5 rounded-2xl border border-gray-800 space-y-4">
              <h3 className="text-sm font-bold text-gray-200">Definições da Conta B2B</h3>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-1.5">
                  <label className="text-xs text-gray-400 font-bold">Chave Pix Recebimentos (Sinal de Reserva)</label>
                  <input 
                    type="text" 
                    value={pixKey}
                    onChange={(e) => setPixKey(e.target.value)}
                    className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-200 focus:outline-none"
                  />
                  <p className="text-[10px] text-gray-500">Pix para o qual os sinais automáticos dos clientes serão direcionados.</p>
                </div>

                <div className="space-y-1.5">
                  <label className="text-xs text-gray-400 font-bold">WhatsApp do Estabelecimento (Dono)</label>
                  <input 
                    type="text" 
                    value={whatsPivot}
                    onChange={(e) => setWhatsPivot(e.target.value)}
                    className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-200 focus:outline-none"
                  />
                  <p className="text-[10px] text-gray-500">Número utilizado para receber notificações das agendas finalizadas.</p>
                </div>
              </div>
            </div>
          </div>
        )}

      </section>

      {/* MODAL IA EMPTY SLOTS VIEWER */}
      {showAiSlots && (
        <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex justify-center items-center p-4 z-50">
          <div className="bg-[#16181f] border border-gray-800 rounded-2xl p-5 max-w-sm w-full space-y-4 shadow-2xl">
            <div className="flex justify-between items-center border-b border-gray-800 pb-2">
              <h3 className="font-extrabold text-white text-md flex items-center gap-1.5">
                <Sparkles className="w-4 h-4 text-gold" />
                Cérebro Analítico IA
              </h3>
              <button 
                onClick={() => setShowAiSlots(false)}
                className="text-gray-400 hover:text-white font-bold text-sm bg-[#0d0e12] px-2 py-0.5 rounded"
              >
                X
              </button>
            </div>

            {aiLoading ? (
              <div className="text-center py-6 space-y-3">
                <div className="w-8 h-8 rounded-full border-2 border-gold border-t-transparent animate-spin mx-auto" />
                <p className="text-xs text-gray-400">Processando e interpretando buracos de encaixe na agenda...</p>
              </div>
            ) : (
              <div className="space-y-3">
                <p className="text-xs text-gray-300 whitespace-pre-line leading-relaxed bg-[#0d0e12] p-3 rounded-xl border border-gray-850">
                  {aiSlotsText}
                </p>
                <button
                  onClick={() => {
                    navigator.clipboard.writeText(aiSlotsText);
                    alert('Recomendações copiadas!');
                  }}
                  className="w-full bg-gold hover:bg-gold-light text-[#0d0e12] font-black p-2.5 rounded-xl text-xs"
                >
                  Copiar Recomendações
                </button>
              </div>
            )}
          </div>
        </div>
      )}

      {/* MODAL QUICK BOOKING CREATOR */}
      {showAddAppt && (
        <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex justify-center items-center p-4 z-50">
          <div className="bg-[#16181f] border border-gray-800 rounded-2xl p-5 max-w-sm w-full space-y-4. shadow-2xl">
            <div className="flex justify-between items-center border-b border-gray-800 pb-2">
              <h3 className="font-extrabold text-[#f3f4f6] text-sm">Adicionar Agendamento Fila 💈</h3>
              <button onClick={() => setShowAddAppt(false)} className="text-gray-400 font-bold bg-[#0d0e12] px-2 py-0.5 rounded text-xs select-none">X</button>
            </div>

            <div className="space-y-3 pt-2">
              <input 
                type="text" 
                value={newApptName} 
                onChange={(e) => setNewApptName(e.target.value)} 
                placeholder="Nome do cliente" 
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              />
              <input 
                type="text" 
                value={newApptPhone} 
                onChange={(e) => setNewApptPhone(e.target.value)} 
                placeholder="WhatsApp fone" 
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              />
              <select 
                value={newApptService} 
                onChange={(e) => setNewApptService(e.target.value)}
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              >
                {services.map(s => <option key={s.name} value={s.name}>{s.name}</option>)}
              </select>
              <input 
                type="text" 
                value={newApptTime} 
                onChange={(e) => setNewApptTime(e.target.value)} 
                placeholder="Horário (Ex: 14:00)" 
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              />
              <select 
                value={newApptBarber} 
                onChange={(e) => setNewApptBarber(Number(e.target.value))}
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              >
                {barbers.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
              </select>

              <div className="flex justify-between items-center text-xs">
                <span>Cobrar sinal garantido Pix?</span>
                <input 
                  type="checkbox" 
                  checked={newApptSignal} 
                  onChange={(e) => setNewApptSignal(e.target.checked)} 
                  className="accent-gold w-4 h-4"
                />
              </div>

              <button
                onClick={handleSaveAppt}
                className="w-full bg-gold hover:bg-gold-light text-[#0d0e12] font-black p-3 rounded-xl text-xs transition-colors mt-2"
              >
                Salvar Horário Fila
              </button>
            </div>
          </div>
        </div>
      )}

      {/* MODAL QUICK NEW CUSTOMER CREATOR */}
      {showAddCust && (
        <div className="fixed inset-0 bg-background/80 backdrop-blur-sm flex justify-center items-center p-4 z-50">
          <div className="bg-[#16181f] border border-gray-800 rounded-2xl p-5 max-w-sm w-full space-y-4 shadow-2xl">
            <div className="flex justify-between items-center border-b border-gray-800 pb-2">
              <h3 className="font-extrabold text-[#f3f4f6] text-sm">Criar Ficha Cadastro 👥</h3>
              <button onClick={() => setShowAddCust(false)} className="text-gray-400 font-bold bg-[#0d0e12] px-2 py-0.5 rounded text-xs select-none">X</button>
            </div>

            <div className="space-y-3 pt-2">
              <input 
                type="text" 
                value={newCustName} 
                onChange={(e) => setNewCustName(e.target.value)} 
                placeholder="Nome Completo" 
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              />
              <input 
                type="text" 
                value={newCustPhone} 
                onChange={(e) => setNewCustPhone(e.target.value)} 
                placeholder="WhatsApp (Ex: 11988887777)" 
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              />
              <input 
                type="text" 
                value={newCustBirthday} 
                onChange={(e) => setNewCustBirthday(e.target.value)} 
                placeholder="Aniversário (Ex: 15/10)" 
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              />
              <input 
                type="text" 
                value={newCustNotes} 
                onChange={(e) => setNewCustNotes(e.target.value)} 
                placeholder="Preferências, estilo ou observação" 
                className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-xs text-gray-100 focus:outline-none"
              />

              <button
                onClick={handleSaveCustomer}
                className="w-full bg-gold hover:bg-gold-light text-[#0d0e12] font-black p-3 rounded-xl text-xs transition-colors mt-2"
              >
                Salvar Ficha Permanente
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
