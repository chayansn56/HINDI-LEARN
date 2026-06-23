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
                    .fillMaxWidth(0.6f)
                    .padding(bottom = 12.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Fit
            )
            Text(
                "Welcome to Vietana Academy!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Select your learning course / Chọn khóa học của bạn:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
            
            LanguageCard(title = "Học tiếng Hindi \uD83C\uDDEE\uD83C\uDDF3", subtitle = "Dành cho người Việt học tiếng Hindi") {
                UserManager.updateLanguage("VI")
                UserManager.progress.selectedCourse = "HINDI"
                UserManager.save()
                onLanguageSelected()
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            LanguageCard(title = "Học tiếng Anh \uD83C\uDDEC\uD83C\uDDE7", subtitle = "Dành cho người Việt học tiếng Anh") {
                UserManager.updateLanguage("VI")
                UserManager.progress.selectedCourse = "ENGLISH"
                UserManager.save()
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
