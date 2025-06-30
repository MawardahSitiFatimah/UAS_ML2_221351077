package com.example.burgerkingpredict

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.ViewCompat

object NotchHelper {
    
    /**
     * Mengatur window untuk menangani notch dan cutout dengan benar
     */
    fun setupNotchSupport(activity: Activity) {
        // Menggunakan WindowCompat untuk menangani inset dengan benar
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        
        // Mengatur controller untuk menangani sistem UI
        val windowInsetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        
        // Deteksi apakah aplikasi dalam mode gelap
        val isDarkTheme = activity.resources.configuration.uiMode and 
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        
        // Set status bar dan navigation bar sesuai tema
        windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme
        windowInsetsController.isAppearanceLightNavigationBars = !isDarkTheme
        
        // Untuk Android API 28+ (Android 9.0+), atur layout untuk menangani cutout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            activity.window.attributes.layoutInDisplayCutoutMode = 
                android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }
    

    fun getNotchPadding(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val insets = activity.window.decorView.rootWindowInsets
            insets?.systemWindowInsetTop ?: getStatusBarHeight(activity)
        } else {
            getStatusBarHeight(activity)
        }
    }
    
   
    fun applyNotchPadding(view: View, activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setOnApplyWindowInsetsListener { v, insets ->
                val sysInsets = insets.systemWindowInsets
                v.setPadding(
                    sysInsets.left,
                    sysInsets.top,
                    sysInsets.right,
                    sysInsets.bottom
                )
                insets
            }
        } else {
            // Fallback untuk versi lama
            view.setPadding(0, getStatusBarHeight(activity), 0, 0)
        }
    }
    
    /**
     * Mendapatkan status bar height
     */
    fun getStatusBarHeight(activity: Activity): Int {
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            activity.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }
    
    /**
     * Mendapatkan navigation bar height
     */
    fun getNavigationBarHeight(activity: Activity): Int {
        val resourceId = activity.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            activity.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }
} 