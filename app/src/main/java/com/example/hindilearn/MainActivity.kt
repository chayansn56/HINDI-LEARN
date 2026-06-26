package com.example.hindilearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hindilearn.theme.HindiLearnTheme
import com.example.hindilearn.theme.HindiLearnTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    com.example.hindilearn.data.UserManager.init(this)
    com.example.hindilearn.data.SrsManager.init(this)

    enableEdgeToEdge()
    setContent {
      HindiLearnTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            MainNavigation()
        }
      }
    }
  }
}
