-- SQL Schema for BarberFlow SaaS
-- Target: PostgreSQL (Supabase)

-- Enable UUID extension
create extension if not exists "uuid-ossp";

-- 1. Table: users (Auth details synced with Supabase Auth)
create table public.users (
  id uuid references auth.users on delete cascade primary key,
  email text not null,
  full_name text,
  business_name text,
  avatar_url text,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 2. Table: barbers (Barbers in the shop)
create table public.barbers (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) on delete cascade not null,
  name text not null,
  phone text,
  commission_percent integer default 50 not null,
  avatar_name text,
  is_active boolean default true not null,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 3. Table: clients (Registered recurring clients)
create table public.clients (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) on delete cascade not null,
  name text not null,
  phone text not null,
  birthday text, -- MM-DD format
  notes text,
  frequency integer default 0 not null,
  last_cut_date date,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 4. Table: services (Catalog of services)
create table public.services (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) on delete cascade not null,
  name text not null,
  description text,
  price numeric(10, 2) not null,
  duration_minutes integer default 30 not null,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 5. Table: appointments (Agenda bookings)
create table public.appointments (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) on delete cascade not null,
  barber_id uuid references public.barbers(id) on delete set null,
  client_id uuid references public.clients(id) on delete set null, -- Optional index for regular clients
  guest_name text not null,
  guest_phone text not null,
  service_name text not null,
  cost numeric(10, 2) not null,
  date_string text not null, -- YYYY-MM-DD
  time_slot text not null, -- HH:MM
  status text default 'Pendente' not null, -- 'Pendente', 'Confirmado', 'Finalizado', 'Falta', 'Cancelado'
  requires_signal boolean default false not null,
  signal_amount numeric(10, 2) default 0.00 not null,
  signal_paid boolean default false not null,
  paid_amount numeric(10, 2) default 0.00 not null,
  payment_method text, -- 'Pix', 'Dinheiro', 'Cartao'
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 6. Table: payments (Financial logs)
create table public.payments (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) on delete cascade not null,
  appointment_id uuid references public.appointments(id) on delete set null,
  barber_id uuid references public.barbers(id) on delete set null,
  description text not null,
  amount numeric(10, 2) not null,
  type text not null, -- 'Receita' (Income) or 'Despesa' (Expense)
  category text not null, -- 'Corte', 'Barba', 'Suprimentos', 'Comissão', 'Aluguel', etc.
  date_string text not null, -- YYYY-MM-DD
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- 7. Table: settings (Barbershop operating parameters & customized SaaS links)
create table public.settings (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) on delete cascade unique not null,
  opening_time text default '08:00' not null,
  closing_time text default '19:30' not null,
  pivot_whatsapp text,
  pix_key text,
  logo_url text,
  theme text default 'dark' not null,
  created_at timestamp with time zone default timezone('utc'::text, now()) not null
);

-- Enable Row Level Security (RLS)
alter table public.users enable row level security;
alter table public.barbers enable row level security;
alter table public.clients enable row level security;
alter table public.services enable row level security;
alter table public.appointments enable row level security;
alter table public.payments enable row level security;
alter table public.settings enable row level security;

-- Policies (Each user only interacts with their own barbershop tenant)
create policy "Users can modify their own record" on public.users 
  for all using (auth.uid() = id);

create policy "Users can manage their own barbers" on public.barbers 
  for all using (auth.uid() = user_id);

create policy "Users can manage their own clients" on public.clients 
  for all using (auth.uid() = user_id);

create policy "Users can manage their own services" on public.services 
  for all using (auth.uid() = user_id);

create policy "Users can manage their own appointments" on public.appointments 
  for all using (auth.uid() = user_id);

create policy "Users can manage their own payments" on public.payments 
  for all using (auth.uid() = user_id);

create policy "Users can manage their own settings" on public.settings 
  for all using (auth.uid() = user_id);

-- Seeding Helper (Initial Brazilian Barber services)
-- Example of execution in Supabase console:
-- insert into public.services (user_id, name, description, price, duration_minutes) values 
-- ('<user-id>', 'Corte Degradê', 'Corte moderno degradê na navalha ou máquina', 45.00, 30),
-- ('<user-id>', 'Barba Espatulada', 'Barba com toalha quente e óleos essenciais', 30.00, 30),
-- ('<user-id>', 'Combo Cabelo + Barba', 'Corte moderno e barba completa com hidratante', 70.00, 60);
