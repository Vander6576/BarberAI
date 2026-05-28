package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "barbers")
data class Barber(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val commissionPercent: Int, // e.g. 60
    val avatarName: String = "avatar_1"
) : Serializable

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val birthday: String, // "dd/mm"
    val notes: String = "",
    val frequency: Int = 0,
    val lastCutDate: String = ""
) : Serializable

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerId: Int, // 0 if walk-in guest
    val guestName: String = "", // Used if customerId is 0 or independent
    val guestPhone: String = "",
    val barberId: Int,
    val serviceName: String,
    val cost: Double,
    val dateString: String, // "YYYY-MM-DD"
    val timeSlot: String, // "hh:mm"
    val status: String = "Pendente", // "Pendente", "Confirmado", "Finalizado", "Falta"
    val requiresSignal: Boolean = false,
    val signalAmount: Double = 0.0,
    val signalPaid: Boolean = false,
    val paidAmount: Double = 0.0,
    val paymentMethod: String = "" // "Pix", "Dinheiro", "Cartão"
) : Serializable

@Entity(tableName = "financial_records")
data class FinancialRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Double,
    val type: String, // "Receita" or "Despesa"
    val category: String, // "Corte", "Barba", "Combo", "Comissão", "Aluguel", "Produtos", "Suprimentos", "Outros"
    val dateString: String, // "YYYY-MM-DD"
    val timestamp: Long = System.currentTimeMillis(),
    val barberId: Int? = null, // Associated barber for comission record
    val appointmentId: Int? = null // Associated appointment
) : Serializable
