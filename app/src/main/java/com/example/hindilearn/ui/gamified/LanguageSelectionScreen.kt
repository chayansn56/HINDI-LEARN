package com.example.hindilearn.ui.gamified

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.CircleShape
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.ui.gamified.PremiumBackground
import com.example.hindilearn.ui.gamified.GlassCard
import androidx.compose.foundation.shape.RoundedCornerShape
@Composable
fun LanguageSelectionScreen(
    onLanguageSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumBackground {
        Column(
            modifier = modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.example.hindilearn.R.drawable.vietana_logo),
                contentDescription = "Vietana Logo",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 16.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
            Text(
                "Welcome to Vietana Hindi Learning App!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Choose your native language / Chọn ngôn ngữ mẹ đẻ của bạn:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(48.dp))
            
            LanguageCard(title = "English \uD83C\uDDEC\uD83C\uDDE7", subtitle = "Learn Hindi from English") {
                UserManager.updateLanguage("EN")
                onLanguageSelected()
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LanguageCard(title = "Tiếng Việt \uD83C\uDDFB\uD83C\uDDF3", subtitle = "Học tiếng Hindi từ tiếng Việt") {
                UserManager.updateLanguage("VI")
                onLanguageSelected()
            }
            
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "© 2026 VIETANA GROUP. All Rights Reserved.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun LanguageCard(title: String, subtitle: String, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth().height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
