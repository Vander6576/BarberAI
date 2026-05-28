package com.example.ui.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiService
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = BarberFlowDatabase.getDatabase(application)
    private val barberDao = database.barberDao()
    private val customerDao = database.customerDao()
    private val appointmentDao = database.appointmentDao()
    private val financialRecordDao = database.financialRecordDao()

    // Screen State Navigation (custom simple, rock solid, edge-to-edge friendly)
    private val _currentTab = MutableStateFlow("dashboard") // dashboard, agenda, clientes, financeiro, saas, chatbot, booking_simulate
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    fun setTab(tab: String) {
        _currentTab.value = tab
    }

    // Database Streams
    val barbers = barberDao.getAllBarbers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val customers = customerDao.getAllCustomers().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val appointments = appointmentDao.getAllAppointments().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val financialRecords = financialRecordDao.getAllFinancials().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI Selected Date
    private val _selectedDate = MutableStateFlow(getTodayDateString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    // AI States
    private val _aiResultText = MutableStateFlow("")
    val aiResultText: StateFlow<String> = _aiResultText.asStateFlow()

    private val _aiLoading = MutableStateFlow(false)
    val aiLoading: StateFlow<Boolean> = _aiLoading.asStateFlow()

    init {
        // Run database seeding on background
        viewModelScope.launch {
            seedDatabaseIfEmpty()
        }
    }

    // Helper to get formatted today string
    fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private suspend fun seedDatabaseIfEmpty() {
        // Collect first list
        val currentBarbers = barbers.first()
        if (currentBarbers.isEmpty()) {
            Log.d("MainViewModel", "Database is empty! Seeding Initial Brazilian Barbershop Data...")
            
            // Seed Barbers
            val barber1Id = barberDao.insertBarber(Barber(name = "Carlinhos Degradê", phone = "(11) 98877-6655", commissionPercent = 50, avatarName = "avatar_1")).toInt()
            val barber2Id = barberDao.insertBarber(Barber(name = "Thiago Navalha", phone = "(11) 97766-5544", commissionPercent = 60, avatarName = "avatar_2")).toInt()
            val barber3Id = barberDao.insertBarber(Barber(name = "Mateus Alinhado", phone = "(11) 96655-4433", commissionPercent = 50, avatarName = "avatar_3")).toInt()

            // Seed Customers
            val c1 = customerDao.insertCustomer(Customer(name = "Marcos Ribeiro", phone = "(11) 99123-4567", birthday = "12/04", notes = "Prefere degradê navalhado. Gosta de café expresso sem açúcar.", frequency = 8, lastCutDate = getOffsetDateString(-7))).toInt()
            val c2 = customerDao.insertCustomer(Customer(name = "Felipe Souza", phone = "(11) 98124-7654", birthday = "25/08", notes = "Cabelo e barba. Alérgico a lâminas frias, usar loção pós-barba de menta.", frequency = 3, lastCutDate = getOffsetDateString(-14))).toInt()
            val c3 = customerDao.insertCustomer(Customer(name = "Rodrigo Antunes", phone = "(11) 97125-1122", birthday = "03/10", notes = "Atrasa as vezes. Prefere corte clássico na tesoura.", frequency = 1, lastCutDate = getOffsetDateString(-30))).toInt()
            val c4 = customerDao.insertCustomer(Customer(name = "Lucas Santos", phone = "(11) 99988-1234", birthday = "14/01", notes = "Corta semanalmente. Estilo surfista bem despojado.", frequency = 14, lastCutDate = getOffsetDateString(-5))).toInt()
            val c5 = customerDao.insertCustomer(Customer(name = "Gabriel Mendes", phone = "(12) 99341-2299", birthday = "18/06", notes = "Cliente novo. Quer experimentar pigmentação na barba.", frequency = 0, lastCutDate = "")).toInt()

            // Seed Financial History (Incomes/Expenses in the past week)
            for (i in 1..7) {
                val offsetDate = getOffsetDateString(-i)
                // Incomes
                financialRecordDao.insertFinancial(FinancialRecord(description = "Serviço Corte Degradê (Marcos)", amount = 45.0, type = "Receita", category = "Corte", dateString = offsetDate))
                financialRecordDao.insertFinancial(FinancialRecord(description = "Serviço Combo Cabelo+Barba (Lucas)", amount = 70.0, type = "Receita", category = "Combo", dateString = offsetDate))
                // Explict Commission Payouts as expense
                financialRecordDao.insertFinancial(FinancialRecord(description = "Comissão Thiago Navalha (Corte/Barba)", amount = 42.0, type = "Despesa", category = "Comissão", dateString = offsetDate, barberId = barber2Id))
                
                if (i == 4) {
                    // Operational expenses
                    financialRecordDao.insertFinancial(FinancialRecord(description = "Compra de Produtos Gel & Gilette", amount = 120.0, type = "Despesa", category = "Suprimentos", dateString = offsetDate))
                }
            }

            // Seed Future & Past Appointments around today
            val today = getTodayDateString()
            val tomorrow = getOffsetDateString(1)
            val yesterday = getOffsetDateString(-1)

            // Today Appointments
            appointmentDao.insertAppointment(Appointment(
                customerId = c1,
                guestName = "Marcos Ribeiro",
                guestPhone = "(11) 99123-4567",
                barberId = barber1Id,
                serviceName = "Corte Degradê",
                cost = 45.0,
                dateString = today,
                timeSlot = "09:00",
                status = "Finalizado",
                paidAmount = 45.0,
                paymentMethod = "Pix"
            ))
            // Register matching Today finished income
            financialRecordDao.insertFinancial(FinancialRecord(
                description = "Corte Degradê - Marcos (Finalizado)",
                amount = 45.0,
                type = "Receita",
                category = "Corte",
                dateString = today
            ))
            financialRecordDao.insertFinancial(FinancialRecord(
                description = "Comissão - Carlinhos Degradê (50%)",
                amount = 22.5,
                type = "Despesa",
                category = "Comissão",
                dateString = today,
                barberId = barber1Id
            ))

            appointmentDao.insertAppointment(Appointment(
                customerId = c2,
                guestName = "Felipe Souza",
                guestPhone = "(11) 98124-7654",
                barberId = barber2Id,
                serviceName = "Combo Cabelo + Barba",
                cost = 70.0,
                dateString = today,
                timeSlot = "10:30",
                status = "Confirmado",
                requiresSignal = true,
                signalAmount = 20.0,
                signalPaid = true,
                paymentMethod = ""
            ))

            appointmentDao.insertAppointment(Appointment(
                customerId = 0,
                guestName = "Carlos Encaixe (Walk-in)",
                guestPhone = "(11) 99000-1111",
                barberId = barber2Id,
                serviceName = "Barba Alinhada",
                cost = 30.0,
                dateString = today,
                timeSlot = "13:00",
                status = "Pendente"
            ))

            appointmentDao.insertAppointment(Appointment(
                customerId = c3,
                guestName = "Rodrigo Antunes",
                guestPhone = "(11) 97111-2222",
                barberId = barber3Id,
                serviceName = "Corte Clássico",
                cost = 40.0,
                dateString = today,
                timeSlot = "15:00",
                status = "Falta"
            ))

            appointmentDao.insertAppointment(Appointment(
                customerId = c4,
                guestName = "Lucas Santos",
                guestPhone = "(11) 99988-1234",
                barberId = barber1Id,
                serviceName = "Corte Degradê",
                cost = 45.0,
                dateString = today,
                timeSlot = "17:30",
                status = "Pendente"
            ))

            // Tomorrow Appointments
            appointmentDao.insertAppointment(Appointment(
                customerId = c4,
                guestName = "Lucas Santos",
                guestPhone = "(11) 99988-1234",
                barberId = barber1Id,
                serviceName = "Corte Degradê",
                cost = 45.0,
                dateString = tomorrow,
                timeSlot = "09:30",
                status = "Confirmado"
            ))

            appointmentDao.insertAppointment(Appointment(
                customerId = c5,
                guestName = "Gabriel Mendes",
                guestPhone = "(12) 99341-2299",
                barberId = barber2Id,
                serviceName = "Pigmentação",
                cost = 35.0,
                dateString = tomorrow,
                timeSlot = "11:00",
                status = "Confirmado",
                requiresSignal = true,
                signalAmount = 15.0,
                signalPaid = false
            ))
        }
    }

    private fun getOffsetDateString(daysOffset: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysOffset)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    // --- MUTATIONS ---

    // 1. Add Barber
    fun addBarber(name: String, phone: String, commissionPercent: Int) {
        viewModelScope.launch {
            val randomAvatar = "avatar_${(1..4).random()}"
            barberDao.insertBarber(Barber(name = name, phone = phone, commissionPercent = commissionPercent, avatarName = randomAvatar))
        }
    }

    // 2. Add Customer
    fun addCustomer(name: String, phone: String, birthday: String, notes: String) {
        viewModelScope.launch {
            customerDao.insertCustomer(Customer(name = name, phone = phone, birthday = birthday, notes = notes))
        }
    }

    // 3. Add Appointment
    fun addAppointment(
        customerId: Int,
        guestName: String,
        guestPhone: String,
        barberId: Int,
        serviceName: String,
        cost: Double,
        dateString: String,
        timeSlot: String,
        requiresSignal: Boolean = false,
        signalPercent: Int = 30
    ) {
        viewModelScope.launch {
            val signalAmount = if (requiresSignal) (cost * signalPercent / 100.0) else 0.0
            val app = Appointment(
                customerId = customerId,
                guestName = guestName,
                guestPhone = guestPhone,
                barberId = barberId,
                serviceName = serviceName,
                cost = cost,
                dateString = dateString,
                timeSlot = timeSlot,
                status = "Pendente",
                requiresSignal = requiresSignal,
                signalAmount = signalAmount,
                signalPaid = false
            )
            appointmentDao.insertAppointment(app)
        }
    }

    // 4. Update Appointment Status (with automatic ledger/comission accounting!)
    fun updateAppointmentStatus(appointment: Appointment, newStatus: String) {
        viewModelScope.launch {
            if (newStatus == "Finalizado" && appointment.status != "Finalizado") {
                // Determine names
                val resolvedName = if (appointment.customerId != 0) {
                    val cust = customerDao.getCustomerById(appointment.customerId)
                    if (cust != null) {
                        // Increment frequency on Customer
                        customerDao.incrementFrequency(cust.id, getTodayDateString())
                        cust.name
                    } else {
                        appointment.guestName
                    }
                } else {
                    appointment.guestName
                }

                // Retrieve Barber commission info
                val barber = barberDao.getBarberById(appointment.barberId)
                val rate = barber?.commissionPercent ?: 50
                val barberCommission = (appointment.cost * (rate / 100.0))

                // Insert Income Financial Record
                financialRecordDao.insertFinancial(FinancialRecord(
                    description = "Corte ${appointment.serviceName} ($resolvedName)",
                    amount = appointment.cost,
                    type = "Receita",
                    category = "Corte",
                    dateString = appointment.dateString,
                    appointmentId = appointment.id
                ))

                // Insert Commission Payout Record (Automatically tracked expense!)
                financialRecordDao.insertFinancial(FinancialRecord(
                    description = "Comissão ${barber?.name ?: "Barbeiro"} (${rate}%)",
                    amount = barberCommission,
                    type = "Despesa",
                    category = "Comissão",
                    dateString = appointment.dateString,
                    barberId = appointment.barberId,
                    appointmentId = appointment.id
                ))

                // Update fully paid state
                val updatedApp = appointment.copy(
                    status = "Finalizado",
                    paidAmount = appointment.cost,
                    paymentMethod = "Pix"
                )
                appointmentDao.insertAppointment(updatedApp)
            } else if (newStatus == "Cancelado" || newStatus == "Falta") {
                // If it was previously finalized, remove records first to secure ledger
                if (appointment.status == "Finalizado") {
                    financialRecordDao.deleteFinancialByAppointment(appointment.id)
                }
                appointmentDao.updateStatus(appointment.id, newStatus)
            } else {
                appointmentDao.updateStatus(appointment.id, newStatus)
            }
        }
    }

    // 5. Update Appointment Signal Paid (e.g. signal pre-payment verification)
    fun updateSignalPaid(appointmentId: Int, paid: Boolean) {
        viewModelScope.launch {
            appointmentDao.updateSignalPaid(appointmentId, paid)
        }
    }

    // 6. Delete Appointment
    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            if (appointment.status == "Finalizado") {
                financialRecordDao.deleteFinancialByAppointment(appointment.id)
            }
            appointmentDao.deleteAppointment(appointment)
        }
    }

    // 7. Add Expense Financial Record Manually (Rent, energy, supplies)
    fun addExpenseManual(description: String, amount: Double, category: String) {
        viewModelScope.launch {
            financialRecordDao.insertFinancial(FinancialRecord(
                description = description,
                amount = amount,
                type = "Despesa",
                category = category,
                dateString = getTodayDateString()
            ))
        }
    }

    // 8. Delete Barber
    fun deleteBarber(barber: Barber) {
        viewModelScope.launch {
            barberDao.deleteBarber(barber)
        }
    }

    // 9. Delete Customer
    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.deleteCustomer(customer)
        }
    }

    // --- GEMINI AI FLOW PROCESSORS ---

    // Option A: Ask Gemini about suggested spots
    fun askAiAboutEmptySlots(existingAppointments: List<Appointment>, dateLabel: String) {
        viewModelScope.launch {
            _aiLoading.value = true
            _aiResultText.value = ""
            
            val formattedSchedule = if (existingAppointments.isEmpty()) {
                "Nenhum agendamento marcado para este dia."
            } else {
                existingAppointments.joinToString("\n") { 
                    "- ${it.timeSlot}: ${it.serviceName} (${it.guestName}) com Barbeiro ID ${it.barberId}" 
                }
            }

            val prompt = """
                Como assistente inteligente BarberFlow para uma barbearia brasileira, analise o espelho de agendamentos reais da data $dateLabel:
                
                $formattedSchedule
                
                Os barbeiros funcionam das 08:00h às 19:30h. Recomende de forma consultiva em português:
                1. Quais as 3 melhores janelas/horários de encaixe de 45 minutos que estão vazios.
                2. Explique em uma frase curta por que cada um desses horários é estratégico (ex: ideal para executivos pré-almoço, pico pós-trabalho, etc).
                3. Dê uma dica curta de marketing para preencher essa vaga via WhatsApp.
                
                Escreva em tom de parceiro de negócios, direto e motivador.
            """.trimIndent()

            val systemInstruction = "Você é o cérebro de inteligência de negócios do BarberFlow, o melhor Micro SaaS do Brasil para barbearias."
            
            val result = GeminiService.generateText(prompt, systemInstruction)
            _aiResultText.value = result
            _aiLoading.value = false
        }
    }

    // Option B: Generate WhatsApp AI client recall text
    fun generateAiRecallText(customer: Customer, daysInactive: Int) {
        viewModelScope.launch {
            _aiLoading.value = true
            _aiResultText.value = ""

            val prompt = """
                Escreva uma mensagem de WhatsApp para reconquistar o cliente ${customer.name}.
                Informações dele:
                - Frequência anterior de cortes: ${customer.frequency} vezes
                - Data do último corte: ${customer.lastCutDate.ifEmpty { "Mais de 30 dias atrás" }}
                - Observações de estilo/gosto dele: ${customer.notes.ifEmpty { "Nenhuma observação cadastrada." }}
                
                Ela deve ser curta, extremamente simpática, conter emojis adequados ao público de barbearia masculina no Brasil, mencionar o fato de fazer tempinho que ele não vê a gente, e incluir de forma bonita o link simulado 'https://barberflow.com.br/agendar' para ele escolher um horário.
                Personalize com base na preferência catalogada dele (${customer.notes}) para que pareça que o barbeiro lembrou pessoalmente dele de forma natural!
                
                Escreva apenas a mensagem pronta para copiar e colar.
            """.trimIndent()

            val systemInstruction = "Você é um mestre barbeiro super camarada que escreve mensagens autênticas no WhatsApp para clientes."
            val result = GeminiService.generateText(prompt, systemInstruction)
            _aiResultText.value = result
            _aiLoading.value = false
        }
    }
}
