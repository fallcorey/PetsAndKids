package com.example.petsandkids

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Главное окно приложения
 * Здесь создаётся нижнее меню и навигация
 */
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Показываем экран
        
        // Настраиваем нижнее меню
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        
        // Настройка верхней панели (необязательно)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,  // Главная
                R.id.navigation_family,  // Все члены семьи
                R.id.navigation_diary,  // Дневник (пока не используется)
                R.id.navigation_settings  // Настройки (пока не используется)
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
