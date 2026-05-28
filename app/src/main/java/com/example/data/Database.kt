package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BarberDao {
    @Query("SELECT * FROM barbers ORDER BY name ASC")
    fun getAllBarbers(): Flow<List<Barber>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarber(barber: Barber): Long

    @Delete
    suspend fun deleteBarber(barber: Barber)

    @Query("SELECT * FROM barbers WHERE id = :id")
    suspend fun getBarberById(id: Int): Barber?
}

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE name LIKE :query OR phone LIKE :query ORDER BY name ASC")
    fun searchCustomers(query: String): Flow<List<Customer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer): Long

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Int): Customer?
    
    @Query("UPDATE customers SET frequency = frequency + 1, lastCutDate = :cutDate WHERE id = :id")
    suspend fun incrementFrequency(id: Int, cutDate: String)
}

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY dateString DESC, timeSlot ASC")
    fun getAllAppointments(): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE dateString = :date ORDER BY timeSlot ASC")
    fun getAppointmentsByDate(date: String): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE barberId = :barberId ORDER BY dateString DESC, timeSlot ASC")
    fun getAppointmentsForBarber(barberId: Int): Flow<List<Appointment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: Appointment): Long

    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    @Query("SELECT * FROM appointments WHERE id = :id")
    suspend fun getAppointmentById(id: Int): Appointment?

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    @Query("UPDATE appointments SET signalPaid = :paid WHERE id = :id")
    suspend fun updateSignalPaid(id: Int, paid: Boolean)
}

@Dao
interface FinancialRecordDao {
    @Query("SELECT * FROM financial_records ORDER BY timestamp DESC")
    fun getAllFinancials(): Flow<List<FinancialRecord>>

    @Query("SELECT * FROM financial_records WHERE dateString = :date ORDER BY timestamp DESC")
    fun getFinancialsByDate(date: String): Flow<List<FinancialRecord>>

    @Query("SELECT * FROM financial_records WHERE dateString BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getFinancialsInDateRange(startDate: String, endDate: String): Flow<List<FinancialRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinancial(record: FinancialRecord): Long

    @Delete
    suspend fun deleteFinancial(record: FinancialRecord)

    @Query("DELETE FROM financial_records WHERE appointmentId = :appointmentId")
    suspend fun deleteFinancialByAppointment(appointmentId: Int)
}

@Database(
    entities = [Barber::class, Customer::class, Appointment::class, FinancialRecord::class],
    version = 1,
    exportSchema = false
)
abstract class BarberFlowDatabase : RoomDatabase() {
    abstract fun barberDao(): BarberDao
    abstract fun customerDao(): CustomerDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun financialRecordDao(): FinancialRecordDao

    companion object {
        @Volatile
        private var INSTANCE: BarberFlowDatabase? = null

        fun getDatabase(context: Context): BarberFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BarberFlowDatabase::class.java,
                    "barber_flow_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
