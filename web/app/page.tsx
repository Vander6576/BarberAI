'use client';

import React, { useState } from 'react';
import { 
  Scissors, 
  Calendar, 
  User, 
  CheckCircle, 
  Smartphone, 
  CreditCard, 
  Copy, 
  Clock, 
  Sparkles,
  ChevronRight
} from 'lucide-react';

export default function ClientBookingPage() {
  const [step, setStep] = useState(1);
  const [service, setService] = useState('Corte Degradê');
  const [price, setPrice] = useState(45.0);
  const [barber, setBarber] = useState('Carlinhos Degradê');
  const [time, setTime] = useState('10:00');
  const [name, setName] = useState('');
  const [phone, setPhone] = useState('');
  const [paySignal, setPaySignal] = useState(true);
  const [showPix, setShowPix] = useState(false);
  const [pixCopied, setPixCopied] = useState(false);

  const services = [
    { name: 'Corte Degradê', price: 45.0, desc: 'Corte moderno degradê na régua e tesouras clássicas.' },
    { name: 'Barba Espatulada', price: 30.0, desc: 'Barba com toalha quente, vaporizador de ozônio e loção refrescante.' },
    { name: 'Combo Cabelo + Barba', price: 70.0, desc: 'Degradê impecável + Barboterapia completa com massagem fácil.' },
    { name: 'Pigmentação Premium', price: 35.0, desc: 'Pigmentação realista para cobrir falhas e alinhar barba ou cabelo.' }
  ];

  const barbers = [
    { name: 'Carlinhos Degradê', sub: 'Especialista em degrade na gilete' },
    { name: 'Thiago Navalha', sub: 'Mestre da barboterapia e toalha quente' },
    { name: 'Primeiro Disponível', sub: 'Horário mais rápido para você' }
  ];

  const timeSlots = [
    '08:00', '09:00', '10:00', '11:00', '12:00', 
    '13:30', '14:30', '15:30', '16:30', '17:30', '18:30'
  ];

  const pixValue = price * 0.3;
  const pixCode = `00020101021126580014br.gov.bcb.pix0136barberflow-99a382cf-e81a-4f51-866d5204000053039865405${pixValue.toFixed(2).replace('.', '')}5802BR5915BarberFlowSaaS6009SaoPaulo62070503***6304D12C`;

  const handleCopyPix = () => {
    navigator.clipboard.writeText(pixCode);
    setPixCopied(true);
    setTimeout(() => setPixCopied(false), 2000);
  };

  const handleFinishAppointment = () => {
    if (!name || !phone) {
      alert('Por favor, preencha seu nome e WhatsApp!');
      return;
    }
    if (paySignal) {
      setShowPix(true);
    } else {
      setStep(6); // Go directly to confirmation
    }
  };

  return (
    <main className="min-h-screen bg-background text-foreground flex flex-col justify-between max-w-md mx-auto relative shadow-2xl overflow-hidden border-x border-card">
      
      {/* PWA Simulated Splash Header */}
      <header className="p-4 bg-card flex justify-between items-center border-b border-gray-800">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-gold to-gold-light flex items-center justify-center text-background font-bold">
            <Scissors className="w-4 h-4 text-[#0d0e12]" />
          </div>
          <div>
            <h1 className="text-md font-bold text-gray-100 tracking-tight">BarberFlow Dev</h1>
            <p className="text-[10px] text-gray-400">PWA Link de Agendamento</p>
          </div>
        </div>
        <span className="text-[10px] text-gold border border-gold/30 bg-gold/5 px-2 py-0.5 rounded-full font-bold">
          CONEXÃO SEGURA 🔒
        </span>
      </header>

      <section className="p-4 flex-grow overflow-y-auto space-y-6">
        
        {/* Step Indicator Bullets */}
        {step < 6 && (
          <div className="flex items-center justify-between bg-card p-3 rounded-xl border border-gray-800">
            <div className="flex items-center gap-1.5">
              {[1, 2, 3, 4, 5].map((s) => (
                <div 
                  key={s} 
                  className={`h-1.5 rounded-full transition-all duration-300 ${
                    s === step ? 'w-6 bg-gold h-1.5' : s < step ? 'w-2 bg-emerald h-1.5' : 'w-1.5 bg-gray-700 h-1.5'
                  }`}
                />
              ))}
            </div>
            <span className="text-xs text-gold font-bold">Etapa {step} de 5</span>
          </div>
        )}

        {/* STEP 1: SELECT SERVICE */}
        {step === 1 && (
          <div className="space-y-4 animate-fade-in">
            <div className="space-y-1">
              <h2 className="text-xl font-bold text-white">Escolha o corte ou barba</h2>
              <p className="text-xs text-gray-400">Tratamentos masculinos de alta performance.</p>
            </div>
            <div className="space-y-3">
              {services.map((s) => (
                <button
                  key={s.name}
                  onClick={() => {
                    setService(s.name);
                    setPrice(s.price);
                    setStep(2);
                  }}
                  className={`w-full text-left p-4 rounded-xl transition-all border flex justify-between items-center ${
                    service === s.name 
                      ? 'bg-card border-gold shadow-md shadow-gold/10' 
                      : 'bg-card/40 border-gray-800 hover:border-gray-700'
                  }`}
                >
                  <div className="space-y-1">
                    <p className="font-bold text-sm text-gray-100">{s.name}</p>
                    <p className="text-xs text-gray-400 line-clamp-2 max-w-[280px]">{s.desc}</p>
                  </div>
                  <div className="text-right pl-3 flex items-center gap-2">
                    <span className="font-extrabold text-gold text-sm whitespace-nowrap">R$ {s.price.toFixed(2)}</span>
                    <ChevronRight className="w-4 h-4 text-gray-500" />
                  </div>
                </button>
              ))}
            </div>
          </div>
        )}

        {/* STEP 2: SELECT BARBER */}
        {step === 2 && (
          <div className="space-y-4 animate-fade-in">
            <div className="space-y-1">
              <h2 className="text-xl font-bold text-white">Selecione o Barbeiro</h2>
              <p className="text-xs text-gray-400 font-medium">Todos os profissionais são licenciados no Premium Hair Standard.</p>
            </div>
            <div className="space-y-3">
              {barbers.map((b) => (
                <button
                  key={b.name}
                  onClick={() => {
                    setBarber(b.name);
                    setStep(3);
                  }}
                  className={`w-full text-left p-4 rounded-xl border flex items-center gap-4 transition-all ${
                    barber === b.name 
                      ? 'bg-card border-gold shadow-md shadow-gold/5' 
                      : 'bg-card/40 border-gray-800 hover:border-gray-700'
                  }`}
                >
                  <div className="w-10 h-10 rounded-full bg-gold/10 text-gold font-bold flex items-center justify-center border border-gold/30">
                    {b.name.charAt(0)}
                  </div>
                  <div className="flex-grow">
                    <p className="font-bold text-sm text-gray-100">{b.name}</p>
                    <p className="text-xs text-gray-400">{b.sub}</p>
                  </div>
                  <ChevronRight className="w-4 h-4 text-gray-500" />
                </button>
              ))}
            </div>
            <button 
              onClick={() => setStep(1)} 
              className="text-xs text-gray-500 underline font-medium block pt-2"
            >
              ← Voltar para serviços
            </button>
          </div>
        )}

        {/* STEP 3: CHOOSE TIME */}
        {step === 3 && (
          <div className="space-y-4 animate-fade-in">
            <div className="space-y-1">
              <h2 className="text-xl font-bold text-white">Escolha um horário livre</h2>
              <p className="text-xs text-gray-400">Estes são os horários livres para hoje na agenda de {barber}</p>
            </div>
            <div className="grid grid-cols-4 gap-2.5">
              {timeSlots.map((slot) => (
                <button
                  key={slot}
                  onClick={() => {
                    setTime(slot);
                    setStep(4);
                  }}
                  className={`py-3 rounded-lg text-center text-xs font-bold border transition-all ${
                    time === slot 
                      ? 'bg-gold text-background border-gold' 
                      : 'bg-card border-gray-800 hover:border-gray-700 text-gray-200'
                  }`}
                >
                  {slot}
                </button>
              ))}
            </div>
            <button 
              onClick={() => setStep(2)} 
              className="text-xs text-gray-500 underline font-medium block pt-2"
            >
              ← Alterar barbeiro selecionado
            </button>
          </div>
        )}

        {/* STEP 4: VERIFY PERSONAL INFO FOR WHATSAPP */}
        {step === 4 && (
          <div className="space-y-4 animate-fade-in">
            <div className="space-y-1">
              <h2 className="text-xl font-bold text-white">Inicie sua Identificação</h2>
              <p className="text-xs text-gray-400">Não precisa de senhas. Apenas Nome e Telefone celular.</p>
            </div>
            
            <div className="space-y-3 bg-card p-4 rounded-xl border border-gray-800">
              <div className="space-y-1.5">
                <label className="text-xs font-bold text-gray-300">Digite seu Nome Completo</label>
                <input 
                  type="text" 
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Nome que o barbeiro te chamará"
                  className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-sm text-gray-100 focus:outline-none focus:border-gold"
                />
              </div>

              <div className="space-y-1.5">
                <label className="text-xs font-bold text-gray-300">WhatsApp Celular</label>
                <input 
                  type="tel" 
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  placeholder="(11) 99999-8888"
                  className="w-full bg-background border border-gray-800 rounded-lg p-2.5 text-sm text-gray-100 focus:outline-none focus:border-gold"
                />
              </div>
            </div>

            <button
              onClick={() => {
                if (!name || !phone) {
                  alert('Por favor, preencha nome e WhatsApp!');
                  return;
                }
                setStep(5);
              }}
              className="w-full bg-gold hover:bg-gold-light text-background font-bold p-3 rounded-xl text-sm flex items-center justify-center gap-2 mt-4"
            >
              Continuar para Pagamento <ChevronRight className="w-4 h-4" />
            </button>
            
            <button 
              onClick={() => setStep(3)} 
              className="text-xs text-gray-500 underline font-medium block pt-1 text-center w-full"
            >
              ← Alterar horário escolhido
            </button>
          </div>
        )}

        {/* STEP 5: SIGNAL OPTION PRE-PAYMENT */}
        {step === 5 && !showPix && (
          <div className="space-y-4 animate-fade-in">
            <div className="space-y-1">
              <h2 className="text-xl font-bold text-white">Garantir Horário Anticalote</h2>
              <p className="text-xs text-gray-400">Esta barbearia solicita sinal de reserva para garantir que você comparecerá.</p>
            </div>

            <div className="bg-card p-4 rounded-xl border border-gray-800 space-y-4">
              <div className="flex justify-between items-start border-b border-gray-800 pb-3">
                <div>
                  <h3 className="font-bold text-sm text-gray-200">Garantia Antecipada Pix</h3>
                  <p className="text-[10px] text-gray-400">Pague 30% do corte agora como sinal e o restante em dinheiro/cartão após o corte!</p>
                </div>
                <input 
                  type="checkbox"
                  checked={paySignal}
                  onChange={(e) => setPaySignal(e.target.checked)}
                  className="w-4 h-4 accent-gold"
                />
              </div>

              <div className="space-y-1.5">
                <div className="flex justify-between text-xs text-gray-300">
                  <span>Preço Total do Serviço</span>
                  <span>R$ {price.toFixed(2)}</span>
                </div>
                {paySignal && (
                  <div className="flex justify-between text-xs text-gold">
                    <span>Sinal Pix de Entrada (30%)</span>
                    <span className="font-bold">R$ {pixValue.toFixed(2)}</span>
                  </div>
                )}
                <div className="flex justify-between text-xs text-gray-300">
                  <span>Pagar na barbearia depois</span>
                  <span>R$ {paySignal ? (price * 0.7).toFixed(2) : price.toFixed(2)}</span>
                </div>
              </div>
            </div>

            <button
              onClick={handleFinishAppointment}
              className="w-full bg-gold hover:bg-gold-light text-background font-bold p-3.5 rounded-xl text-sm shadow-xl shadow-gold/10"
            >
              {paySignal ? `Gerar Pix de Reserva R$ ${pixValue.toFixed(2)}` : 'Confirmar Sem Sinal Garantia'}
            </button>
            <button 
              onClick={() => setStep(4)} 
              className="text-xs text-gray-500 underline font-medium block pt-1 text-center w-full"
            >
              ← Alterar dados de WhatsApp
            </button>
          </div>
        )}

        {/* STEP 5.1: SHOW PIX QR CODE AND COPY & PASTE CODE */}
        {step === 5 && showPix && (
          <div className="space-y-5 animate-fade-in">
            <div className="space-y-1 text-center">
              <h2 className="text-lg font-bold text-white">🔒 Pague o Sinal Pix de R$ {pixValue.toFixed(2)}</h2>
              <p className="text-xs text-gray-400 max-w-[320px] mx-auto">Sua reserva expira em 5 minutos. Copie o código Pix Copia e Cola abaixo ou escaneie o código dinâmico.</p>
            </div>

            <div className="flex flex-col items-center justify-center space-y-4">
              {/* Graphical QR Code block in Tailwind */}
              <div className="w-40 h-40 bg-white p-2.5 rounded-xl border border-gray-700 flex flex-col justify-between shadow-2xl relative">
                <div className="flex-grow flex flex-col justify-between">
                  <div className="flex justify-between">
                    <div className="w-10 h-10 border-4 border-black" />
                    <div className="w-10 h-10 border-4 border-black" />
                  </div>
                  <div className="self-center bg-black w-4 h-4 rounded-sm" />
                  <div className="flex justify-between">
                    <div className="w-10 h-10 border-4 border-black" />
                    <div className="w-12 h-6 bg-black" />
                  </div>
                </div>
                <span className="absolute inset-0 m-auto w-8 h-8 rounded-full bg-gold flex items-center justify-center font-bold text-[10px] text-background shadow">
                  PIX
                </span>
              </div>

              {/* Copia e Cola field */}
              <div className="w-full bg-card p-3 rounded-lg border border-gray-800 flex justify-between items-center gap-2">
                <p className="text-[10px] text-gray-400 whitespace-nowrap overflow-hidden text-ellipsis flex-grow">
                  {pixCode}
                </p>
                <button 
                  onClick={handleCopyPix}
                  className="bg-gold/15 hover:bg-gold/20 text-gold text-xs font-bold px-3 py-1.5 rounded-md flex items-center gap-1 shrink-0"
                >
                  <Copy className="w-3.5 h-3.5" />
                  {pixCopied ? 'Copiado!' : 'Copiar'}
                </button>
              </div>
            </div>

            <button
              onClick={() => setStep(6)}
              className="w-full bg-emerald hover:bg-emerald-600 text-[#0d0e12] font-bold p-3.5 rounded-xl text-sm"
            >
              Simular Confirmação do Pix
            </button>

            <button 
              onClick={() => setShowPix(false)} 
              className="text-xs text-gray-500 underline font-medium block pt-1 text-center w-full"
            >
              ← Cancelar e alterar valor
            </button>
          </div>
        )}

        {/* STEP 6: APPOINTMENT CONFIRMED FINALLY */}
        {step === 6 && (
          <div className="space-y-6 py-6 text-center animate-fade-in">
            <div className="w-16 h-16 rounded-full bg-emerald/10 border border-emerald/50 flex items-center justify-center mx-auto text-emerald">
              <CheckCircle className="w-8 h-8" />
            </div>

            <div className="space-y-1">
              <h2 className="text-2xl font-black text-white">Horário Marcado! 💈</h2>
              <p className="text-xs text-gray-400">Seu agendamento foi registrado com sucesso na agenda oficial de Carlinhos!</p>
            </div>

            <div className="bg-card p-4 rounded-xl border border-gray-800 text-left space-y-3">
              <p className="text-[10px] text-gray-400 font-bold tracking-wider">RESUMO DA RESERVA</p>
              <div className="flex justify-between border-b border-gray-800/40 pb-2 text-sm">
                <span className="text-gray-400 font-medium">Serviço</span>
                <span className="font-bold text-gray-200">{service}</span>
              </div>
              <div className="flex justify-between border-b border-gray-800/40 pb-2 text-sm">
                <span className="text-gray-400 font-medium">Barbeiro</span>
                <span className="font-bold text-gray-200">{barber}</span>
              </div>
              <div className="flex justify-between border-b border-gray-800/40 pb-2 text-sm">
                <span className="text-gray-400 font-medium">Horário Marcado</span>
                <span className="font-semibold text-gold">{time}</span>
              </div>
              <div className="flex justify-between border-b border-gray-800/40 pb-2 text-sm">
                <span className="text-gray-400 font-medium">Contato</span>
                <span className="font-bold text-gray-200">{phone}</span>
              </div>
              <div className="flex justify-between text-sm">
                <span className="text-gray-400 font-medium">Sinal de Entrada</span>
                <span className="font-bold text-gray-200">
                  {paySignal ? `R$ ${pixValue.toFixed(2)} Pago via Pix` : 'Sem Sinal'}
                </span>
              </div>
            </div>

            <p className="text-xs text-gray-500">
              Enviamos um lembrete do seu agendamento para o WhatsApp informado {phone}! Nos vemos na cadeira.
            </p>

            <button
              onClick={() => {
                setStep(1);
                setName('');
                setPhone('');
                setShowPix(false);
              }}
              className="w-full bg-card hover:bg-[#1f222d] text-gold border border-gold/40 font-bold p-3 rounded-xl text-sm"
            >
              Fazer Novo Agendamento
            </button>
          </div>
        )}

      </section>

      {/* PWA Bottom install banner simulate bar */}
      <footer className="p-3 bg-card/60 backdrop-blur border-t border-gray-800 flex items-center justify-between text-[11px] text-gray-400 font-medium">
        <div className="flex items-center gap-1.5 grayscale">
          <Sparkles className="w-3.5 h-3.5 text-gold" />
          <span>BarberFlow Web PWA v1.0</span>
        </div>
        <span>Desenvolvido no Brasil 🇧🇷</span>
      </footer>

    </main>
  );
}
