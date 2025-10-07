package com.salaheddine.vitaltrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.FirebaseApp
import com.salaheddine.vitaltrack.auth.AuthViewModel
import com.salaheddine.vitaltrack.ui.AppRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val vm: AuthViewModel = viewModel()
            vm.checkSession()
            AppRoot(vm)
        }
    }
}
