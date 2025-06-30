package com.example.burgerkingpredict

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Konfigurasi untuk menangani notch dan cutout
        NotchHelper.setupNotchSupport(this)

        // Tampilkan layout splash
        setContentView(R.layout.activity_splash)

        // Delay 2 detik, lalu pindah ke MainActivity
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // supaya tidak bisa kembali ke splash
        }
    }
}
