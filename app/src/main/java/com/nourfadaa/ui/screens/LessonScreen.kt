package com.nourfadaa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nourfadaa.domain.Lesson
import com.nourfadaa.domain.LessonReward
import com.nourfadaa.ui.theme.NourColors

@Composable
fun LessonScreen(
    lesson: Lesson?,
    childName: String,
    reward: LessonReward?,
    onCorrect: () -> Unit,
    onBack: () -> Unit
) {
    if (lesson == null) {
        Column(Modifier.fillMaxSize().background(NourColors.Cream).padding(24.dp)) {
            Text("لم نجد هذا الدرس", fontSize = 20.sp)
            TextButton(onClick = onBack) { Text("رجوع") }
        }
        return
    }
    var step by remember { mutableIntStateOf(0) }
    var wrong by remember { mutableStateOf(false) }
    var showParent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().background(NourColors.Cream)
            .verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(lesson.emoji, fontSize = 72.sp)
        Text(
            lesson.title, fontSize = 28.sp, fontWeight = FontWeight.Bold,
            color = NourColors.Teal, textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))

        when (step) {
            0 -> {
                Text(lesson.story, fontSize = 22.sp, lineHeight = 34.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { step = 1 },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().height(64.dp)
                ) { Text("فهمت، التالي", fontSize = 20.sp) }
            }
            1 -> {
                Text(lesson.question, fontSize = 24.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                lesson.options.forEachIndexed { index, option ->
                    Button(
                        onClick = {
                            if (index == lesson.answerIndex) {
                                wrong = false
                                step = 2
                                onCorrect()
                            } else {
                                wrong = true
                            }
                        },
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth().height(64.dp)
                    ) { Text(option, fontSize = 20.sp) }
                    Spacer(Modifier.height(12.dp))
                }
                if (wrong) {
                    Text("حاولي مرة أخرى 💛", fontSize = 20.sp, color = NourColors.Pink)
                }
            }
            else -> {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val msg = when {
                        reward == null -> "أحسنتِ يا $childName 🌟"
                        reward.alreadyCompleted -> "أحسنتِ يا $childName! أكملتِ هذا الدرس من قبل 🌟"
                        else -> "أحسنتِ يا $childName 🌟\nحصلتِ على ${reward.starsEarned} نجوم و${reward.xpEarned} نقطة" +
                            (if (reward.leveledUp) "\nوصلتِ إلى المستوى ${reward.newLevel}! 🎉" else "")
                    }
                    Text(msg, fontSize = 20.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp).fillMaxWidth())
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onBack,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().height(64.dp)
                ) { Text("العودة للدروس", fontSize = 20.sp) }
            }
        }

        Spacer(Modifier.height(24.dp))
        TextButton(onClick = { showParent = !showParent }) { Text("👨‍👩‍👧 للوالدين", fontSize = 16.sp) }
        if (showParent) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    if (lesson.parentNote.isNotBlank()) {
                        Text(lesson.parentNote, fontSize = 16.sp)
                        Spacer(Modifier.height(8.dp))
                    }
                    Text("المصادر:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    lesson.sources.forEach { s ->
                        val ref = listOf(s.book, s.number, s.grade).filter { it.isNotBlank() }.joinToString(" — ")
                        Spacer(Modifier.height(6.dp))
                        Text("• ${s.kind}: ${s.text}\n   $ref", fontSize = 15.sp)
                    }
                }
            }
        }
    }
}
