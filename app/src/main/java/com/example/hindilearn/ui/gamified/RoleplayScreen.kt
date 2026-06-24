package com.example.hindilearn.ui.gamified

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hindilearn.data.UserManager
import com.example.hindilearn.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleplayScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val progress = UserManager.progress
    val isVi = progress.selectedLanguage == "VI"
    
    var selectedScenario by remember { mutableStateOf<String?>(null) }

    PremiumBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (isVi) "Giao tiếp AI" else "AI Roleplay", fontWeight = FontWeight.Bold, color = TextDark) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Box(modifier = modifier.fillMaxSize().padding(padding)) {
                if (selectedScenario == null) {
                    ScenarioSelection(isVi = isVi) { scenario ->
                        selectedScenario = scenario
                    }
                } else {
                    ChatInterface(isVi = isVi, scenario = selectedScenario!!)
                }
            }
        }
    }
}

@Composable
fun ScenarioSelection(isVi: Boolean, onSelect: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isVi) "Chọn một tình huống" else "Choose a Scenario",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(24.dp))

        val scenarios = listOf(
            if (isVi) "Tại nhà hàng" else "At the Restaurant",
            if (isVi) "Gọi Taxi" else "Calling a Taxi",
            if (isVi) "Mặc cả ngoài chợ" else "Bargaining at the Market"
        )

        scenarios.forEach { scenario ->
            Card(
                onClick = { onSelect(scenario) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("🤖", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(scenario, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextDark)
                }
            }
        }
    }
}

@Composable
fun ChatInterface(isVi: Boolean, scenario: String) {
    val coroutineScope = rememberCoroutineScope()
    var messages by remember { 
        mutableStateOf(listOf(
            ChatMessage(
                if (scenario.contains("Restaurant") || scenario.contains("nhà hàng")) 
                    (if (isVi) "Chào mừng! Bạn muốn dùng gì?" else "Welcome! What would you like to order?")
                else if (scenario.contains("Taxi"))
                    (if (isVi) "Bạn muốn đi đâu?" else "Where do you want to go?")
                else
                    (if (isVi) "Cái này giá 500. Rất rẻ!" else "This is 500 rupees. Very cheap!"),
                false
            )
        )) 
    }
    var inputText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat History
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            reverseLayout = true
        ) {
            if (isTyping) {
                item {
                    ChatBubble(message = ChatMessage("...", false))
                }
            }
            items(messages.reversed()) { msg ->
                ChatBubble(message = msg)
            }
        }

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(24.dp)),
                placeholder = { Text(if (isVi) "Nói gì đó..." else "Say something...") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        val userText = inputText
                        inputText = ""
                        messages = messages + ChatMessage(userText, true)
                        
                        // Mock AI response
                        isTyping = true
                        coroutineScope.launch {
                            delay(1500)
                            isTyping = false
                            val reply = if (isVi) "Thật tuyệt vời! Còn gì nữa không?" else "That's great! Anything else?"
                            messages = messages + ChatMessage(reply, false)
                        }
                    }
                },
                modifier = Modifier.background(DeepSaffron, CircleShape).size(48.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { /* Handle Voice Input via intent in real app */ },
                modifier = Modifier.background(SoftGreen, CircleShape).size(48.dp)
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Mic", tint = RoyalBlue)
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(
                    topStart = 16.dp, 
                    topEnd = 16.dp, 
                    bottomStart = if (message.isUser) 16.dp else 0.dp, 
                    bottomEnd = if (message.isUser) 0.dp else 16.dp
                ))
                .background(if (message.isUser) DeepSaffron else Color.White)
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isUser) Color.White else TextDark,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
