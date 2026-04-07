package com.example.petsandkids

import androidx.room.Entity  // Помечаем как таблицу в базе данных
import androidx.room.PrimaryKey  // Уникальный ID

/**
 * Это класс "Член семьи"
 * Каждый ребёнок или питомец - это объект этого класса
 */
@Entity(tableName = "family_members")  // Сохраняем в таблицу "family_members"
data class FamilyMember(
    @PrimaryKey(autoGenerate = true)  // ID создаётся автоматически
    val id: Long = 0,  // Уникальный номер (0, 1, 2...)
    
    val name: String,  // Имя (например "Алиса" или "Бобик")
    
    val type: String,  // Тип: "child" (ребёнок) или "pet" (питомец)
    
    val petType: String?,  // Если питомец: "dog", "cat" и т.д.
    
    val breed: String?,  // Порода (для питомцев)
    
    val birthDate: String  // Дата рождения (в формате "dd.MM.yyyy")
)
