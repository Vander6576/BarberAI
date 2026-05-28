package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.theme.*
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarberFlowApp(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Observe DB states
    val barbersData by viewModel.barbers.collectAsStateWithLifecycle()
    val customersData by viewModel.customers.collectAsStateWithLifecycle()
    val appointmentsData by viewModel.appointments.collectAsStateWithLifecycle()
    val financialData by viewModel.financialRecords.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()

    var showQuickAboutSaaS by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Bespoke stylized comb/scissors logo in gold canvas
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(
                                    Brush.linearGradient(listOf(BarberGold, BarberGoldLight)),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                tint = MidnightBlack,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "BarberFlow",
                            color = CleanWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showQuickAboutSaaS = true },
                        modifier = Modifier.testTag("about_saas_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Estratégia SaaS",
                            tint = BarberGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MidnightBlack,
                    titleContentColor = CleanWhite
                )
            )
        },
        bottomBar = {
            // Elegant micro-navigation bar designed mobile-first like Nubank & simple to scan
            NavigationBar(
                containerColor = DeepGraphite,
                tonalElevation = 8.dp,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                NavigationBarItem(
                    selected = currentTab == "dashboard",
                    onClick = { viewModel.setTab("dashboard") },
                    icon = { Icon(Icons.Default.Menu, "Início") },
                    label = { Text("Métricas", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = SubduedGray,
                        unselectedTextColor = SubduedGray
                    )
                )
                NavigationBarItem(
                    selected = currentTab == "agenda",
                    onClick = { viewModel.setTab("agenda") },
                    icon = { Icon(Icons.Default.DateRange, "Agenda") },
                    label = { Text("Agenda", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = SubduedGray,
                        unselectedTextColor = SubduedGray
                    )
                )
                NavigationBarItem(
                    selected = currentTab == "clientes",
                    onClick = { viewModel.setTab("clientes") },
                    icon = { Icon(Icons.Default.Person, "Clientes") },
                    label = { Text("Clientes", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = SubduedGray,
                        unselectedTextColor = SubduedGray
                    )
                )
                NavigationBarItem(
                    selected = currentTab == "financeiro",
                    onClick = { viewModel.setTab("financeiro") },
                    icon = { Icon(Icons.Default.Favorite, "Caixa") }, // Ledger card indicator
                    label = { Text("Financeiro", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = SubduedGray,
                        unselectedTextColor = SubduedGray
                    )
                )
                NavigationBarItem(
                    selected = currentTab == "booking_simulate",
                    onClick = { viewModel.setTab("booking_simulate") },
                    icon = { Icon(Icons.Default.ShoppingCart, "Ver App") },
                    label = { Text("Simular App", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MidnightBlack,
                        selectedTextColor = BarberGold,
                        indicatorColor = BarberGold,
                        unselectedIconColor = SubduedGray,
                        unselectedTextColor = SubduedGray
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MidnightBlack)
                .padding(innerPadding)
        ) {
            when (currentTab) {
                "dashboard" -> DashboardScreen(
                    viewModel = viewModel,
                    barbers = barbersData,
                    customers = customersData,
                    appointments = appointmentsData,
                    financialRecords = financialData
                )
                "agenda" -> AgendaScreen(
                    viewModel = viewModel,
                    barbers = barbersData,
                    customers = customersData,
                    appointments = appointmentsData,
                    selectedDate = selectedDate
                )
                "clientes" -> ClientesScreen(
                    viewModel = viewModel,
                    customers = customersData
                )
                "financeiro" -> FinanceiroScreen(
                    viewModel = viewModel,
                    financialRecords = financialData,
                    barbers = barbersData
                )
                "booking_simulate" -> ClientBookingFlowScreen(
                    viewModel = viewModel,
                    barbers = barbersData,
                    customers = customersData
                )
                "saas" -> SaasBlueprintScreen(
                    viewModel = viewModel,
                    barbers = barbersData
                )
            }

            // Quick Strategy overlay dialog
            if (showQuickAboutSaaS) {
                SaaSStrategyDialog(
                    onDismiss = { showQuickAboutSaaS = false },
                    onNavigateToDetails = {
                        showQuickAboutSaaS = false
                        viewModel.setTab("saas")
                    }
                )
            }
        }
    }
}

// FORMAT HELPER
fun formatBRL(amount: Double): String {
    val df = DecimalFormat("#,##0.00")
    return "R$ " + df.format(amount)
}

// --- SUB-SCREEN 1: DASHBOARD ---
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    barbers: List<Barber>,
    customers: List<Customer>,
    appointments: List<Appointment>,
    financialRecords: List<FinancialRecord>
) {
    val context = LocalContext.current
    val today = viewModel.getTodayDateString()
    
    // Filter today's appointments
    val todayAppointments = appointments.filter { it.dateString == today }
    
    // Sum Faturamento Real do Dia (completed appointments)
    val todayIncomes = financialRecords
        .filter { it.dateString == today && it.type == "Receita" }
        .sumOf { it.amount }

    // Sum today's expenses
    val todayExpenses = financialRecords
        .filter { it.dateString == today && it.type == "Despesa" }
        .sumOf { it.amount }

    val todaySchedulesCount = todayAppointments.size
    val todayDoneCount = todayAppointments.count { it.status == "Finalizado" }
    
    // Misrate calculation (Faltas / total today)
    val missedCount = todayAppointments.count { it.status == "Falta" }
    val noShowPercentage = if (todaySchedulesCount > 0) {
        (missedCount.toDouble() / todaySchedulesCount.toDouble() * 100).toInt()
    } else 0

    // IA quick slot generator display
    val aiResultText by viewModel.aiResultText.collectAsStateWithLifecycle()
    val aiLoading by viewModel.aiLoading.collectAsStateWithLifecycle()
    var showAiResultModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header & Shortcuts
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Olá, Barbeiro! 💈",
                    color = CleanWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Painel consolidado para lucros hoje",
                    color = SubduedGray,
                    fontSize = 14.sp
                )
            }
            // Smart Pill indicating active simulation state
            Box(
                modifier = Modifier
                    .background(LightGraySlate, RoundedCornerShape(12.dp))
                    .border(1.dp, BarberGold, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "PLAN: PREMIUM",
                    color = BarberGold,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- SECTION: NUMBERS HUB (Nubank style cards) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(115.dp)
                    .testTag("today_balance_card"),
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = EmeraldLive,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(text = "Hoje Líquido", color = SubduedGray, fontSize = 11.sp)
                        Text(
                            text = formatBRL(todayIncomes - todayExpenses),
                            color = CleanWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(115.dp),
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = BarberGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(text = "Agenda Hoje", color = SubduedGray, fontSize = 11.sp)
                        Text(
                            text = "$todayDoneCount / $todaySchedulesCount",
                            color = CleanWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(115.dp),
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (noShowPercentage > 15) RubyRed else AlertOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Column {
                        Text(text = "Faltas (No-Show)", color = SubduedGray, fontSize = 11.sp)
                        Text(
                            text = "$noShowPercentage%",
                            color = if (noShowPercentage > 15) RubyRed else CleanWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // --- AI BRAIN RECOMMENDATION BOX (SaaS value!) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = LightGraySlate),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BarberGold.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(BarberGold, RoundedCornerShape(6.dp))
                            .padding(4.dp)
                    ) {
                        Text(text = "IA", color = MidnightBlack, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Sugerir Encaixes por IA",
                        color = CleanWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "A Inteligência Artificial analisa sua agenda de hoje e prescreve os melhores momentos livres para fazer campanhas relâmpago de encaixe.",
                    color = SubduedGray,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Button(
                    onClick = {
                        viewModel.askAiAboutEmptySlots(todayAppointments, "Hoje (Formatado)")
                        showAiResultModal = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .testTag("ai_suggest_slots_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Executar Análise de Encaixes", color = MidnightBlack, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // --- ACTIVE QUEUE / TODAY ACTION ROOM ---
        Text(
            text = "Próximos Clientes na Fila",
            color = CleanWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        if (todayAppointments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .border(
                        BorderStroke(1.dp, LightGraySlate),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Nenhum cliente agendado para hoje.", color = SubduedGray, fontSize = 13.sp)
                    Text("Simule como cliente no botão do menu!", color = BarberGold, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                todayAppointments.sortedBy { it.timeSlot }.forEach { appointment ->
                    val customerNotes = customers.find { it.id == appointment.customerId }?.notes ?: ""
                    val currentBarber = barbers.find { it.id == appointment.barberId }
                    
                    AppointmentActionCard(
                        appointment = appointment,
                        barber = currentBarber,
                        customerNotes = customerNotes,
                        onComplete = {
                            viewModel.updateAppointmentStatus(appointment, "Finalizado")
                            Toast.makeText(context, "Atendimento Finalizado! Caixa e repasse de comissão registrados.", Toast.LENGTH_SHORT).show()
                        },
                        onMiss = {
                            viewModel.updateAppointmentStatus(appointment, "Falta")
                        },
                        onDelete = {
                            viewModel.deleteAppointment(appointment)
                        },
                        onRecall = {
                            // Custom WhatsApp action
                            val text = "Olá ${appointment.guestName}, tudo bem? Confirmando hoje seu horário de ${appointment.serviceName} às ${appointment.timeSlot} com o barbeiro ${currentBarber?.name ?: "Profissional"}. Podemos confirmar seu corte?"
                            try {
                                val encoded = URLEncoder.encode(text, "UTF-8")
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${appointment.guestPhone}&text=$encoded"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Abrindo link do WhatsApp", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    // Modal display for AI slots recommendation results
    if (showAiResultModal) {
        Dialog(onDismissRequest = { showAiResultModal = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💡 Recomendador IA",
                            color = BarberGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showAiResultModal = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = CleanWhite)
                        }
                    }

                    if (aiLoading) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(color = BarberGold)
                            Text("A Inteligência Artificial do BarberFlow está calculando buracos estratégicos e compondo mensagens de captação...", color = SubduedGray, fontSize = 12.sp, textAlign = TextAlign.Center)
                        }
                    } else {
                        Text(
                            text = aiResultText,
                            color = CleanWhite,
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        )

                        val clipboard = LocalClipboardManager.current
                        Button(
                            onClick = {
                                clipboard.setText(AnnotatedString(aiResultText))
                                Toast.makeText(context, "Dicas copiadas!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Copiar Recomendações", color = MidnightBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// Today Active Appointment Queuers Action row
@Composable
fun AppointmentActionCard(
    appointment: Appointment,
    barber: Barber?,
    customerNotes: String,
    onComplete: () -> Unit,
    onMiss: () -> Unit,
    onDelete: () -> Unit,
    onRecall: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DeepGraphite)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header: Slot & Name & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(LightGraySlate, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = appointment.timeSlot,
                            color = BarberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = appointment.guestName,
                        color = CleanWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Status bubble
                val (color, text) = when (appointment.status) {
                    "Finalizado" -> Pair(EmeraldLive, "Cerrado")
                    "Confirmado" -> Pair(BarberGold, "Marcado")
                    "Falta" -> Pair(RubyRed, "Faltou")
                    else -> Pair(AlertOrange, "Pendente")
                }

                Box(
                    modifier = Modifier
                        .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = text,
                        color = color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Sub: Details & Services & Barber commissioned
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${appointment.serviceName} • ${formatBRL(appointment.cost)}",
                        color = CleanWhite,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Profissional: ${barber?.name ?: "Indefinido"}",
                        color = SubduedGray,
                        fontSize = 11.sp
                    )
                    if (customerNotes.isNotEmpty()) {
                        Text(
                            text = "💡 Gosto: $customerNotes",
                            color = BarberGoldLight.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
                
                // Signal Tag Tracker
                if (appointment.requiresSignal) {
                    Box(
                        modifier = Modifier
                            .background(
                                if (appointment.signalPaid) EmeraldLive.copy(alpha = 0.15f) else AlertOrange.copy(alpha = 0.15f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (appointment.signalPaid) "Sinal Pago Pix" else "Sinal Solicitado",
                            color = if (appointment.signalPaid) EmeraldLive else AlertOrange,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Quick Actions Room! (iFood style row)
            if (appointment.status != "Finalizado" && appointment.status != "Falta") {
                Divider(color = LightGraySlate, thickness = 1.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldLive),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(36.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, "Atendido", tint = MidnightBlack, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Fechar", color = MidnightBlack, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = onRecall,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BarberGold),
                        border = BorderStroke(1.dp, BarberGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(36.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Share, "WhatsApp", tint = BarberGold, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Confirmar", color = BarberGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = onMiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SubduedGray),
                        border = BorderStroke(1.dp, LightGraySlate),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(0.7f).height(36.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Faltou", color = RubyRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            } else {
                // If finalized or closed, allow deleting / undo action
                Divider(color = LightGraySlate, thickness = 1.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = RubyRed.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}


// --- SUB-SCREEN 2: JORNADA DA AGENDA (CALENDAR & AGENDAMENTO EXCLUSIVE) ---
@Composable
fun AgendaScreen(
    viewModel: MainViewModel,
    barbers: List<Barber>,
    customers: List<Customer>,
    appointments: List<Appointment>,
    selectedDate: String
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }

    // Carousel dates generators around today
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val sdfDay = SimpleDateFormat("dd", Locale.getDefault())
    val sdfDayWeek = SimpleDateFormat("EEE", Locale.getDefault())

    val dateRange = remember {
        val list = mutableListOf<String>()
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -2) // Yesterday & past day
        for (i in 0..6) {
            list.add(sdf.format(cal.time))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    // Filter appointments for the selected date
    val filteredAppointments = appointments.filter { it.dateString == selectedDate }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Agenda Comercial 📅",
                    color = CleanWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Selecione o dia para conferir encaixes",
                    color = SubduedGray,
                    fontSize = 12.sp
                )
            }

            // Quick Create Client appointment button
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = BarberGold,
                contentColor = MidnightBlack,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("add_appointment_fab"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, "Agendar")
            }
        }

        // --- CALENDAR CAROUSEL ROW ---
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dateRange) { dateStr ->
                // Parse date characteristics
                val dateObj = sdf.parse(dateStr) ?: Date()
                val isSelected = dateStr == selectedDate
                val dayNum = sdfDay.format(dateObj)
                val dayLabel = sdfDayWeek.format(dateObj).uppercase(Locale.getDefault())

                Card(
                    modifier = Modifier
                        .size(width = 54.dp, height = 75.dp)
                        .clickable { viewModel.setSelectedDate(dateStr) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) BarberGold else DeepGraphite
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dayLabel,
                            color = if (isSelected) MidnightBlack else SubduedGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = dayNum,
                            color = if (isSelected) MidnightBlack else CleanWhite,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // Selected Date Label Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightGraySlate, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dia Selecionado: $selectedDate",
                    color = CleanWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Text(
                    text = "${filteredAppointments.size} Agendados",
                    color = BarberGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // Appointments lists
        if (filteredAppointments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(
                        BorderStroke(1.dp, LightGraySlate),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Sem compromissos nesta data.", color = SubduedGray, fontSize = 14.sp)
                    Text("Clique no '+' acima para agendar manualmente!", color = BarberGold, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredAppointments.sortedBy { it.timeSlot }) { appointment ->
                    val customerNotes = customers.find { it.id == appointment.customerId }?.notes ?: ""
                    val currentBarber = barbers.find { it.id == appointment.barberId }
                    
                    AppointmentActionCard(
                        appointment = appointment,
                        barber = currentBarber,
                        customerNotes = customerNotes,
                        onComplete = {
                            viewModel.updateAppointmentStatus(appointment, "Finalizado")
                            Toast.makeText(context, "Atendimento Finalizado!", Toast.LENGTH_SHORT).show()
                        },
                        onMiss = {
                            viewModel.updateAppointmentStatus(appointment, "Falta")
                        },
                        onDelete = {
                            viewModel.deleteAppointment(appointment)
                        },
                        onRecall = {
                            // Custom WhatsApp action
                            val text = "Oi ${appointment.guestName}! Confirmando horário amanhã seu horário de ${appointment.serviceName} às ${appointment.timeSlot} com ${currentBarber?.name ?: "Barbeiro"}. Tudo certo?"
                            try {
                                val encoded = URLEncoder.encode(text, "UTF-8")
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${appointment.guestPhone}&text=$encoded"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Gerando link de WhatsApp", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    // CREATE MANUALLY DIALOG
    if (showAddDialog) {
        var clientName by remember { mutableStateOf("") }
        var clientPhone by remember { mutableStateOf("") }
        var serviceInput by remember { mutableStateOf("Corte Degradê") }
        var costInput by remember { mutableStateOf("45.00") }
        var selectedBarberId by remember { mutableStateOf(if (barbers.isNotEmpty()) barbers.first().id else 0) }
        var timeSlotInput by remember { mutableStateOf("09:00") }
        var requireSignalInput by remember { mutableStateOf(false) }

        val servicesList = listOf(
            Pair("Corte Degradê", 45.0),
            Pair("Barba Espatulada", 30.0),
            Pair("Combo Cabelo+Barba", 70.0),
            Pair("Alinhamento/Degradação", 25.0)
        )

        Dialog(onDismissRequest = { showAddDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Novo Agendamento Rápido 💈",
                        color = BarberGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Nome do Cliente", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite,
                            unfocusedTextColor = CleanWhite
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("add_appt_name")
                    )

                    OutlinedTextField(
                        value = clientPhone,
                        onValueChange = { clientPhone = it },
                        label = { Text("Telefone WhatsApp", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite,
                            unfocusedTextColor = CleanWhite
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("add_appt_phone")
                    )

                    // Quick service picker
                    Text("Escolha o Serviço:", color = CleanWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        servicesList.forEach { (name, price) ->
                            val isSelected = serviceInput == name
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) BarberGold else LightGraySlate,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        serviceInput = name
                                        costInput = price.toString()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "$name (${formatBRL(price)})",
                                    color = if (isSelected) MidnightBlack else CleanWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = timeSlotInput,
                        onValueChange = { timeSlotInput = it },
                        label = { Text("Horário (ex: 14:30)", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite,
                            unfocusedTextColor = CleanWhite
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("add_appt_time")
                    )

                    // Barber picker
                    Text("Escolha o Barbeiro:", color = CleanWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        barbers.forEach { barber ->
                            val isSelected = selectedBarberId == barber.id
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) BarberGold else LightGraySlate,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedBarberId = barber.id }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = barber.name,
                                    color = if (isSelected) MidnightBlack else CleanWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Toggles requireSignal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Cobrar Sinal Antecipado Pix", color = CleanWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("Pede 30% via Pix para confirmar", color = SubduedGray, fontSize = 10.sp)
                        }
                        Switch(
                            checked = requireSignalInput,
                            onCheckedChange = { requireSignalInput = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = BarberGold, checkedTrackColor = LightGraySlate)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showAddDialog = false },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanWhite),
                            border = BorderStroke(1.dp, LightGraySlate),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Fechar")
                        }

                        Button(
                            onClick = {
                                if (clientName.isEmpty() || clientPhone.isEmpty()) {
                                    Toast.makeText(context, "Preencha nome e telefone!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.addAppointment(
                                    customerId = 0,
                                    guestName = clientName,
                                    guestPhone = clientPhone,
                                    barberId = selectedBarberId,
                                    serviceName = serviceInput,
                                    cost = costInput.toDoubleOrNull() ?: 45.0,
                                    dateString = selectedDate,
                                    timeSlot = timeSlotInput,
                                    requiresSignal = requireSignalInput,
                                    signalPercent = 30
                                )
                                Toast.makeText(context, "Marcado com sucesso!", Toast.LENGTH_SHORT).show()
                                showAddDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
                            modifier = Modifier.weight(1.3f).testTag("save_manual_appt_btn")
                        ) {
                            Text("Adendar Vaga", color = MidnightBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


// --- SUB-SCREEN 3: DATABASE DE CLIENTES & AI RECALL ---
@Composable
fun ClientesScreen(
    viewModel: MainViewModel,
    customers: List<Customer>
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }

    var aiRecallTargetCustomer by remember { mutableStateOf<Customer?>(null) }
    val aiResultText by viewModel.aiResultText.collectAsStateWithLifecycle()
    val aiLoading by viewModel.aiLoading.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Base de Clientes 👥",
                    color = CleanWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Melhore a retenção e lute contra faltas",
                    color = SubduedGray,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(38.dp).testTag("add_new_customer_btn")
            ) {
                Text("Adicionar", color = MidnightBlack, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }

        // Sub description
        Text(
            text = "Aniversários, frequência de agendamentos acumulada e observações de comportamento. Toque em 'Fidelizar IA' para disparar mensagens de recall automáticas.",
            color = SubduedGray,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )

        if (customers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(BorderStroke(1.dp, LightGraySlate), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Sem clientes catalogados.", color = SubduedGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(customers) { customer ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DeepGraphite)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Header name & Birthday marker
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = customer.name,
                                        color = CleanWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = "WhatsApp: ${customer.phone}",
                                        color = SubduedGray,
                                        fontSize = 12.sp
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(LightGraySlate, CircleShape)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Aniv: ${customer.birthday}",
                                            color = BarberGold,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    
                                    IconButton(
                                        onClick = { viewModel.deleteCustomer(customer) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Deletar",
                                            tint = RubyRed.copy(alpha = 0.6f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            // Statistics row (frequency and last cut)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(LightGraySlate.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Frequência: ${customer.frequency} cortes",
                                        color = CleanWhite,
                                        fontSize = 11.sp
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(LightGraySlate.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "Último corte: ${customer.lastCutDate.ifEmpty { "Nunca" }}",
                                        color = CleanWhite,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            // Notes details
                            if (customer.notes.isNotEmpty()) {
                                Text(
                                    text = "💡 Obs: ${customer.notes}",
                                    color = BarberGoldLight,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }

                            // AI Recall button trigger
                            Button(
                                onClick = {
                                    aiRecallTargetCustomer = customer
                                    viewModel.generateAiRecallText(customer, 25)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = LightGraySlate),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, BarberGold.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("Fidelizar via IA Recall 🤖", color = BarberGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ADD MANUALLY CLIENT DIALOG
    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var birthday by remember { mutableStateOf("10/05") }
        var notes by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showAddDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Cadastrar Cliente no SaaS 👥",
                        color = BarberGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome Completo", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("add_cust_name")
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Telefone (DDI + DDD)", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("add_cust_phone")
                    )

                    OutlinedTextField(
                        value = birthday,
                        onValueChange = { birthday = it },
                        label = { Text("Aniversário (ex: 12/04)", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Preferências & Notas pessoais", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showAddDialog = false },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanWhite),
                            border = BorderStroke(1.dp, LightGraySlate),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Fechar")
                        }

                        Button(
                            onClick = {
                                if (name.isEmpty() || phone.isEmpty()) {
                                    Toast.makeText(context, "Nome e telefone obrigatórios!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.addCustomer(name, phone, birthday, notes)
                                Toast.makeText(context, "Cliente cadastrado!", Toast.LENGTH_SHORT).show()
                                showAddDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
                            modifier = Modifier.weight(1.2f).testTag("save_customer_btn")
                        ) {
                            Text("Salvar Ficha", color = MidnightBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // AI RECALL RESULT VIEW MODAL
    if (aiRecallTargetCustomer != null) {
        Dialog(onDismissRequest = { aiRecallTargetCustomer = null }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Draft Inteligente Recorrência 🤖",
                            color = BarberGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { aiRecallTargetCustomer = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Fechar", tint = CleanWhite)
                        }
                    }

                    Text(
                        text = "Gerando mensagem persuasiva para reconquistar: ${aiRecallTargetCustomer?.name}",
                        color = SubduedGray,
                        fontSize = 11.sp
                    )

                    if (aiLoading) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(color = BarberGold)
                            Text("A IA do BarberFlow está montando um recall amigável baseado nas preferências de corte e data de inatividade...", color = SubduedGray, fontSize = 12.sp, textAlign = TextAlign.Center)
                        }
                    } else {
                        // Display generated text
                        Box(
                            modifier = Modifier
                                .background(LightGraySlate, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = aiResultText,
                                color = CleanWhite,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }

                        val clipboard = LocalClipboardManager.current
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    clipboard.setText(AnnotatedString(aiResultText))
                                    Toast.makeText(context, "Texto Copiado!", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Copiar Clipes", color = MidnightBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    try {
                                        val encoded = URLEncoder.encode(aiResultText, "UTF-8")
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${aiRecallTargetCustomer?.phone}&text=$encoded"))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Link gerado!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldLive),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Disparar Whats", color = MidnightBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}


// --- SUB-SCREEN 4: LIVRO FINANCEIRO (INCOMES/EXPENSES LEDGER & BARBER COMMISSIONS) ---
@Composable
fun FinanceiroScreen(
    viewModel: MainViewModel,
    financialRecords: List<FinancialRecord>,
    barbers: List<Barber>
) {
    val context = LocalContext.current
    var showExpenseDialog by remember { mutableStateOf(false) }

    // Ledger mathematics
    val incomes = financialRecords.filter { it.type == "Receita" }.sumOf { it.amount }
    val expenses = financialRecords.filter { it.type == "Despesa" }.sumOf { it.amount }
    val balance = incomes - expenses

    // Commissions specifically
    val commissionPaidTotal = financialRecords
        .filter { it.type == "Despesa" && it.category == "Comissão" }
        .sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Movimentação Financeira 💰",
                    color = CleanWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Controle de repasses e caixa",
                    color = SubduedGray,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = { showExpenseDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = RubyRed),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(38.dp)
            ) {
                Text("Despesa", color = CleanWhite, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }

        // --- MASTER BALANCE CARD ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DeepGraphite),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "SALDO EM CAIXA", color = SubduedGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                
                Text(
                    text = formatBRL(balance),
                    color = if (balance >= 0) EmeraldLive else RubyRed,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Divider(color = LightGraySlate, thickness = 1.dp)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Faturamento Total (+)", color = SubduedGray, fontSize = 11.sp)
                        Text(formatBRL(incomes), color = EmeraldLive, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Despesas & Payouts (-)", color = SubduedGray, fontSize = 11.sp)
                        Text(formatBRL(expenses), color = RubyRed, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Commissions overview
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = LightGraySlate.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total Pago de Comissões aos Barbeiros", color = SubduedGray, fontSize = 11.sp)
                    Text(formatBRL(commissionPaidTotal), color = BarberGoldLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .background(BarberGold.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Split Auto", color = BarberGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Text(
            text = "Lancamentos de Caixa",
            color = CleanWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        if (financialRecords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(BorderStroke(1.dp, LightGraySlate), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Sem registros hoje.", color = SubduedGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(financialRecords) { record ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DeepGraphite, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = record.description,
                                color = CleanWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier.padding(top = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(LightGraySlate, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(record.category, color = BarberGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Text(record.dateString, color = SubduedGray, fontSize = 11.sp)
                            }
                        }

                        val sign = if (record.type == "Receita") "+" else "-"
                        val colorText = if (record.type == "Receita") EmeraldLive else RubyRed
                        Text(
                            text = "$sign ${formatBRL(record.amount)}",
                            color = colorText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // MANUAL LOG DESPESA DIALOG (Suprimentos, Aluguel, Agua)
    if (showExpenseDialog) {
        var description by remember { mutableStateOf("") }
        var amountInput by remember { mutableStateOf("") }
        var categorySelected by remember { mutableStateOf("Aluguel") }

        val categories = listOf("Aluguel", "Produtos", "Suprimentos", "Marketing", "Energia/Agua", "Outros")

        Dialog(onDismissRequest = { showExpenseDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Lançar Despesa da Barbearia 🧾",
                        color = RubyRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Nome da Despesa", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RubyRed,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("add_expense_description")
                    )

                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = { amountInput = it },
                        label = { Text("Valor (Ex: 80.00)", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RubyRed,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("add_expense_amount")
                    )

                    Text("Categoria da Despesa:", color = CleanWhite, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSelected = categorySelected == cat
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) RubyRed else LightGraySlate,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { categorySelected = cat }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = CleanWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showExpenseDialog = false },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanWhite),
                            border = BorderStroke(1.dp, LightGraySlate),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Fechar")
                        }

                        Button(
                            onClick = {
                                if (description.isEmpty() || amountInput.isEmpty()) {
                                    Toast.makeText(context, "Nome e valor obrigatórios!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.addExpenseManual(
                                    description = description,
                                    amount = amountInput.toDoubleOrNull() ?: 10.0,
                                    category = categorySelected
                                )
                                Toast.makeText(context, "Despesa lançada!", Toast.LENGTH_SHORT).show()
                                showExpenseDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RubyRed),
                            modifier = Modifier.weight(1.2f).testTag("save_expense_btn")
                        ) {
                            Text("Salvar Débito", color = CleanWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


// --- SUB-SCREEN 5: INTERACTIVE CLIENT BOOKING FLOW (THE REAL USER FACING CORTE REQUISITIONS) ---
@Composable
fun ClientBookingFlowScreen(
    viewModel: MainViewModel,
    barbers: List<Barber>,
    customers: List<Customer>
) {
    val context = LocalContext.current
    var selectedService by remember { mutableStateOf("Corte Degradê") }
    var serviceCost by remember { mutableStateOf(45.0) }
    var selectedBarberId by remember { mutableStateOf(if (barbers.isNotEmpty()) barbers.first().id else 0) }
    var chosenTimeSlot by remember { mutableStateOf("10:00") }
    
    var clientNameInput by remember { mutableStateOf("") }
    var clientPhoneInput by remember { mutableStateOf("") }
    var paySignalSelected by remember { mutableStateOf(true) } // True by default for SaaS value!

    var showPixReceiptDialog by remember { mutableStateOf(false) }

    val services = listOf(
        Triple("Corte Degradê", 45.0, "Corte moderno degradê na gilete ou maquinas."),
        Triple("Barba Espatulada", 30.0, "Barba completa com vaporizador de ozônio e toalha quente."),
        Triple("Combo Cabelo + Barba", 70.0, "O mais escolhido! Degradê + Alinhamento completo da barba."),
        Triple("Tratamento Progressivo", 80.0, "Relaxamento e selagem profissional dos fios.")
    )

    val timeSlots = listOf("08:00", "09:00", "10:00", "11:00", "13:30", "14:30", "15:30", "16:30", "17:30", "18:30")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Explanatory Banner of Client Booking Flow
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BarberGold.copy(alpha = 0.15f)),
            border = BorderStroke(1.dp, BarberGold)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "👇 APP DO CLIENTE (PORTAL DE AGENDAMENTOS)",
                    color = BarberGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Este é o link que o barbeiro envia no WhatsApp para os clientes. Faça o agendamento simulado aqui e confirme abaixo. Ele aparecerá imediatamente na agenda oficial do Barbeiro!",
                    color = CleanWhite,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Text(
            text = "PASSO 1: Escolha o Serviço",
            color = CleanWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        services.forEach { (name, cost, desc) ->
            val isSelected = selectedService == name
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedService = name
                        serviceCost = cost
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) LightGraySlate else DeepGraphite
                ),
                border = BorderStroke(1.dp, if (isSelected) BarberGold else Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = name, color = CleanWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = desc, color = SubduedGray, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                    Text(
                        text = formatBRL(cost),
                        color = if (isSelected) BarberGold else CleanWhite,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        Text(
            text = "PASSO 2: Quem vai te atender?",
            color = CleanWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            barbers.forEach { barber ->
                val isSelected = selectedBarberId == barber.id
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedBarberId = barber.id },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) LightGraySlate else DeepGraphite
                    ),
                    border = BorderStroke(1.dp, if (isSelected) BarberGold else Color.Transparent)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(BarberGold.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                barber.name.take(1).uppercase(),
                                color = BarberGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = barber.name.substringBefore(" "),
                            color = CleanWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Text(
            text = "PASSO 3: Selecione o Horário",
            color = CleanWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            timeSlots.forEach { slot ->
                val isSelected = chosenTimeSlot == slot
                Box(
                    modifier = Modifier
                        .background(
                            if (isSelected) BarberGold else DeepGraphite,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { chosenTimeSlot = slot }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = slot,
                        color = if (isSelected) MidnightBlack else CleanWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Text(
            text = "PASSO 4: Identificação para WhatsApp",
            color = CleanWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = clientNameInput,
            onValueChange = { clientNameInput = it },
            label = { Text("Seu Nome", color = CleanWhite) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BarberGold,
                unfocusedBorderColor = LightGraySlate,
                focusedTextColor = CleanWhite
            ),
            modifier = Modifier.fillMaxWidth().testTag("booking_client_name")
        )

        OutlinedTextField(
            value = clientPhoneInput,
            onValueChange = { clientPhoneInput = it },
            label = { Text("Seu WhatsApp (DDI + DDD + Num)", color = CleanWhite) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BarberGold,
                unfocusedBorderColor = LightGraySlate,
                focusedTextColor = CleanWhite
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth().testTag("booking_client_phone")
        )

        // Signal Toggle Selector
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DeepGraphite)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Pagar Sinal de reserva via Pix (30%)", color = CleanWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Garante o horário, combate faltas e o barbeiro recebe no ato!", color = SubduedGray, fontSize = 11.sp, lineHeight = 14.sp)
                }
                Switch(
                    checked = paySignalSelected,
                    onCheckedChange = { paySignalSelected = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = BarberGold, checkedTrackColor = LightGraySlate)
                )
            }
        }

        val signalValue = serviceCost * 30 / 100.0

        // CTA: AGENDAR COM SINAL COPIA E COLA PIX
        Button(
            onClick = {
                if (clientNameInput.isEmpty() || clientPhoneInput.isEmpty()) {
                    Toast.makeText(context, "Por favor, digite seu nome e WhatsApp para agendar!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (paySignalSelected) {
                    showPixReceiptDialog = true
                } else {
                    // Create directly as confirmed or pending
                    viewModel.addAppointment(
                        customerId = 0,
                        guestName = clientNameInput,
                        guestPhone = clientPhoneInput,
                        barberId = selectedBarberId,
                        serviceName = selectedService,
                        cost = serviceCost,
                        dateString = viewModel.getTodayDateString(),
                        timeSlot = chosenTimeSlot,
                        requiresSignal = false
                    )
                    Toast.makeText(context, "Agendamento reservado com sucesso!", Toast.LENGTH_LONG).show()
                    viewModel.setTab("dashboard")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(48.dp)
                .testTag("submit_booking_confirm_btn"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (paySignalSelected) "Gerar QR Code Pix de ${formatBRL(signalValue)}" else "Confirmar Agendamento Grátis",
                color = MidnightBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }

    // PIX CODE DIALOGUE
    if (showPixReceiptDialog) {
        val signalValue = serviceCost * 30 / 100.0
        val pixCodeString = "00020101021126580014br.gov.bcb.pix0136barberflow-99a382cf-e81a-4f51-866d5204000053039865405${String.format("%.2f", signalValue).replace(".", "")}5802BR5915BarberFlowSaaS6009SaoPaulo62070503***6304D12C"
        val clipboard = LocalClipboardManager.current

        Dialog(onDismissRequest = { showPixReceiptDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "🔒 Pague o Sinal Garantia",
                        color = BarberGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Para garantir seu horário de $chosenTimeSlot, pague o sinal Pix de 30% (${formatBRL(signalValue)}). O código Copia e Cola está gerado abaixo.",
                        color = CleanWhite,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 15.sp
                    )

                    // BEAUTIFUL CUSTOM GRAPHICAL CANVAS PIX QR CODE
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(CleanWhite, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Draw simulated QR code pattern blocks with Canvas
                            val size = size.width
                            val cellSize = size / 6
                            
                            // Corners markers
                            drawRect(color = MidnightBlack, topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(cellSize * 1.8f, cellSize * 1.8f), style = Stroke(cellSize * 0.4f))
                            drawRect(color = MidnightBlack, topLeft = Offset(size - cellSize * 1.8f, 0f), size = androidx.compose.ui.geometry.Size(cellSize * 1.8f, cellSize * 1.8f), style = Stroke(cellSize * 0.4f))
                            drawRect(color = MidnightBlack, topLeft = Offset(0f, size - cellSize * 1.8f), size = androidx.compose.ui.geometry.Size(cellSize * 1.8f, cellSize * 1.8f), style = Stroke(cellSize * 0.4f))
                            
                            // Random pattern dots simulated
                            drawRect(color = MidnightBlack, topLeft = Offset(cellSize * 2.5f, cellSize * 1f), size = androidx.compose.ui.geometry.Size(cellSize * 0.8f, cellSize * 0.8f))
                            drawRect(color = MidnightBlack, topLeft = Offset(cellSize * 3.5f, cellSize * 2.5f), size = androidx.compose.ui.geometry.Size(cellSize * 0.8f, cellSize * 0.8f))
                            drawRect(color = MidnightBlack, topLeft = Offset(cellSize * 1f, cellSize * 3.5f), size = androidx.compose.ui.geometry.Size(cellSize * 0.8f, cellSize * 0.8f))
                            drawRect(color = MidnightBlack, topLeft = Offset(cellSize * 4f, cellSize * 4f), size = androidx.compose.ui.geometry.Size(cellSize * 1.4f, cellSize * 0.8f))
                            drawRect(color = MidnightBlack, topLeft = Offset(cellSize * 2.5f, cellSize * 4.5f), size = androidx.compose.ui.geometry.Size(cellSize * 0.8f, cellSize * 0.8f))
                        }
                    }

                    // Copia e Cola field
                    Card(
                        colors = CardDefaults.cardColors(containerColor = LightGraySlate),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pixCodeString,
                                color = SubduedGray,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                clipboard.setText(AnnotatedString(pixCodeString))
                                Toast.makeText(context, "Código Copia e Cola copiado!", Toast.LENGTH_SHORT).show()
                            }) {
                                Icon(Icons.Default.Share, "Copiar", tint = BarberGold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            // Register in database
                            viewModel.addAppointment(
                                customerId = 0,
                                guestName = clientNameInput,
                                guestPhone = clientPhoneInput,
                                barberId = selectedBarberId,
                                serviceName = selectedService,
                                cost = serviceCost,
                                dateString = viewModel.getTodayDateString(),
                                timeSlot = chosenTimeSlot,
                                requiresSignal = true
                            )
                            Toast.makeText(context, "Sinal Pago! Vaga Agendada com Sucesso!", Toast.LENGTH_LONG).show()
                            showPixReceiptDialog = false
                            
                            // Return to dashboard control
                            viewModel.setTab("dashboard")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldLive),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Simular Pagamento Confirmado", color = MidnightBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { showPixReceiptDialog = false },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanWhite),
                        border = BorderStroke(1.dp, LightGraySlate),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}


// --- SUB-SCREEN 6: SAAS BLUEPRINT & CONFIG (THE REVENUE CALCULATORS & COMMISSION RATIO ADJUSTMENTS) ---
@Composable
fun SaasBlueprintScreen(
    viewModel: MainViewModel,
    barbers: List<Barber>
) {
    val context = LocalContext.current
    var showAddBarberDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Infra SaaS & Configurações ⚙️",
                color = CleanWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Modelagem e crescimento de receita no Brasil",
                color = SubduedGray,
                fontSize = 12.sp
            )
        }

        // --- SUB SECTION: GESTÃO DO CORPO DE BARBEIROS (DIRETAMENTE ASSOCIADOS COM % COMISSÃO NO BANCO) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DeepGraphite)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Configurações dos Barbeiros (% Comissão)", color = BarberGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    IconButton(
                        onClick = { showAddBarberDialog = true },
                        modifier = Modifier
                            .size(32.dp)
                            .background(BarberGold, RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Add, "Adicionar", tint = MidnightBlack, modifier = Modifier.size(18.dp))
                    }
                }

                Text(
                    text = "Abaixo constam os barbeiros cadastrados no sistema. A comissão incide na finalização do corte, gerando débito automático no faturamento.",
                    color = SubduedGray,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    barbers.forEach { barber ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(LightGraySlate, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(barber.name, color = CleanWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Comissão Acordada: ${barber.commissionPercent}%", color = BarberGold, fontSize = 11.sp)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(barber.phone, color = SubduedGray, fontSize = 11.sp)
                                IconButton(
                                    onClick = { viewModel.deleteBarber(barber) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Delete, "Deletar", tint = RubyRed.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- BRAZILIAN MONETIZATION BLUEPRINT EXPLANATORY SECTION ---
        Text(
            text = "Estratégia Micro SaaS & Economia Financeira",
            color = CleanWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        // Math metrics display: Custo operacional vs faturamento
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = LightGraySlate),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Quanto Cobrar dos Barbeiros & Lucro Potencial", color = CleanWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                
                Text(
                    text = "No Brasil, existem mais de 500 mil barbearias informais. O Micro SaaS pode monetizar cobrando mensalidades baseadas no tamanho do estabelecimento.",
                    color = SubduedGray,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                // Plan levels
                PlanDetailRow(title = "Plano Bronze Autônomo", price = "R$ 49,90/mês", detail = "Até 1 Barbeiro da cadeira. Excelente para micro empreendedores de favela e bairros populares.")
                PlanDetailRow(title = "Plano Ouro Barbearia", price = "R$ 89,90/mês", detail = "Até 3 Barbeiros, inclui split do Pix em contas separadas para repasse do estabelecimento.")
                PlanDetailRow(title = "Plano Black Platinum Prestige", price = "R$ 149,90/mês", detail = "Barbeiros ilimitados, IA Inteligente sugerindo horários de encaixes automáticos de fila de espera.")

                Divider(color = DeepGraphite, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                Column {
                    Text("CUSTO OPERACIONAL ALVO (100 Clientes B2B): R$ 850,00/mês (Banco + Servidor + API)", color = SubduedGray, fontSize = 10.sp)
                    Text("FATURAMENTO ESTIMADO (100 Clientes B2B x R$89,90): R$ 8.990,00/mês", color = EmeraldLive, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("MARGEM DE LUCRO LÍQUIDA CALCULADA: 90.5%", color = BarberGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // SaaS Acquisition ideas card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DeepGraphite)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Estratégias de Crescimento Viral de Marketing", color = BarberGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                
                MarketingGrowthBullet(bulletNum = "1", title = "Desafios do Tik Tok & Instagram Reels", text = "Gere vídeos curtos ironizando a desorganização de agendas do barbeiro antigo (aquele que anota em folha rasgada). Mostre a simplicidade do BarberFlow acelerando agendas!")
                MarketingGrowthBullet(bulletNum = "2", title = "Indicação Premiada", text = "Ofereça 1 mês de isenção na fatura para barbeiros que indicarem colegas de barbearias vizinhas.")
                MarketingGrowthBullet(bulletNum = "3", title = "Funil de Vendas WhatsApp B2B", text = "Procure no Google Maps barbearias na sua cidade de médio porte e envie o link de testes do app (aba simulação) gratuito!")
            }
        }
    }

    // MANUALLY REGISTER BARBER DIALOG
    if (showAddBarberDialog) {
        var barberName by remember { mutableStateOf("") }
        var barberPhone by remember { mutableStateOf("") }
        var commissionPctInput by remember { mutableStateOf("50") }

        Dialog(onDismissRequest = { showAddBarberDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DeepGraphite),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Cadastrar Barbeiro Parceiro 💈",
                        color = BarberGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = barberName,
                        onValueChange = { barberName = it },
                        label = { Text("Nome do Barbeiro", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("add_barber_name")
                    )

                    OutlinedTextField(
                        value = barberPhone,
                        onValueChange = { barberPhone = it },
                        label = { Text("WhatsApp Celular", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("add_barber_phone")
                    )

                    OutlinedTextField(
                        value = commissionPctInput,
                        onValueChange = { commissionPctInput = it },
                        label = { Text("Porcentagem Comissão (ex: 60)", color = CleanWhite) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BarberGold,
                            unfocusedBorderColor = LightGraySlate,
                            focusedTextColor = CleanWhite
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("add_barber_commission")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showAddBarberDialog = false },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanWhite),
                            border = BorderStroke(1.dp, LightGraySlate),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Fechar")
                        }

                        Button(
                            onClick = {
                                if (barberName.isEmpty() || barberPhone.isEmpty()) {
                                    Toast.makeText(context, "Nome e telefone obrigatórios!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.addBarber(
                                    name = barberName,
                                    phone = barberPhone,
                                    commissionPercent = commissionPctInput.toIntOrNull() ?: 50
                                )
                                Toast.makeText(context, "Barbeiro adicionado!", Toast.LENGTH_SHORT).show()
                                showAddBarberDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
                            modifier = Modifier.weight(1.2f).testTag("save_barber_btn")
                        ) {
                            Text("Salvar Perfil", color = MidnightBlack, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanDetailRow(title: String, price: String, detail: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, color = BarberGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(text = price, color = CleanWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Text(text = detail, color = SubduedGray, fontSize = 10.sp, lineHeight = 13.sp, modifier = Modifier.padding(top = 2.dp))
    }
}

@Composable
fun MarketingGrowthBullet(bulletNum: String, title: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .background(BarberGold, CircleShape)
                .size(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = bulletNum, color = MidnightBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = CleanWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = text, color = SubduedGray, fontSize = 10.sp, lineHeight = 14.sp, modifier = Modifier.padding(top = 2.dp))
        }
    }
}


// --- BLUEPRINT STRATEGY FLOATING OVERLAY DIALOG ---
@Composable
fun SaaSStrategyDialog(
    onDismiss: () -> Unit,
    onNavigateToDetails: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepGraphite),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "🚀 BarberFlow Startup Blueprint",
                    color = BarberGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Apresentação Estratégica",
                    color = CleanWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = "Nome Forte: BarberFlow\nSlogan: \"Sua Cadeira Sempre Cheia\"\n\nIdentidade: Dark & Gold Premium.\n\nModelo de Negócio: SaaS freemium com asfalto técnico robusto, onde barbeiros assinam mensalmente para liberar encaixe preditivo por IA e cobrança Pix split comissionado automático.",
                    color = SubduedGray,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Button(
                    onClick = onNavigateToDetails,
                    colors = ButtonDefaults.buttonColors(containerColor = BarberGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Detalhes do SaaS & Configs", color = MidnightBlack, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CleanWhite),
                    border = BorderStroke(1.dp, LightGraySlate),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fechar")
                }
            }
        }
    }
}
