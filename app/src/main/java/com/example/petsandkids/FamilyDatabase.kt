package com.example.petsandkids

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

/**
 * Это база данных
 * Она сохраняет всех членов семьи на телефоне
 */
@Database(
    entities = [FamilyMember::class],  // Храним объекты FamilyMember
    version = 1,  // Версия базы данных
    exportSchema = false
)
abstract class FamilyDatabase : RoomDatabase() {
    
    abstract fun familyMemberDao(): FamilyMemberDao  // Доступ к данным
    
    companion object {
        @Volatile
        private var INSTANCE: FamilyDatabase? = null
        
        // Получить базу данных (создать если нет)
        fun getDatabase(context: Context): FamilyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FamilyDatabase::class.java,
                    "family_database"  // Имя файла базы данных
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/**
 * DAO = Data Access Object (объект доступа к данным)
 * Здесь написано, как сохранять и получать данные
 */
@androidx.room.Dao
interface FamilyMemberDao {
    // Получить всех членов семьи (сортировка по ID)
    @androidx.room.Query("SELECT * FROM family_members ORDER BY id DESC")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<FamilyMember>>
    
    // Добавить нового члена семьи
    @androidx.room.Insert
    suspend fun insert(member: FamilyMember)
    
    // Удалить члена семьи
    @androidx.room.Delete
    suspend fun delete(member: FamilyMember)
}
