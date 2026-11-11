package com.nourfadaa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nourfadaa.data.ChildEntity
import com.nourfadaa.domain.LessonReward
import com.nourfadaa.domain.Section
import com.nourfadaa.ui.theme.NourColors

@Composable
fun SectionScreen(
    section: Section,
    child: ChildEntity?,
    reward: LessonReward?,
    onCompleteDemo: () -> Unit,
    onBack: () -> Unit
) {
    val name = child?.name ?: ""
    Column(
        modifier = Modifier.fillMaxSize().background(NourColors.Cream).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(section.emoji, fontSize = 80.sp)
        Text(
            section.title, fontSize = 30.sp, fontWeight = FontWeight.Bold,
            color = NourColors.Teal, textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "هذا القسم قيد الإعداد. سيُضاف محتواه من مصادر موثوقة في المراحل القادمة.",
            fontSize = 18.sp, textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onCompleteDemo, shape = RoundedCornerShape(24.dp), modifier = Modifier.height(64.dp)) {
            Text("✨ درس تجريبي", fontSize = 20.sp)
        }
        if (reward != null) {
            Spacer(Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val text = if (reward.alreadyCompleted) {
                    "أحسنتِ يا $name! أكملتِ هذا الدرس من قبل 🌟"
                } else {
                    "أحسنتِ يا $name 🌟\nحصلتِ على ${reward.starsEarned} نجوم و${reward.xpEarned} نقطة" +
                        if (reward.leveledUp) "\nوصلتِ إلى المستوى ${reward.newLevel}! 🎉" else ""
                }
                Text(text, fontSize = 18.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp).fillMaxWidth())
            }
        }
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onBack) { Text("رجوع", fontSize = 18.sp) }
    }
}
