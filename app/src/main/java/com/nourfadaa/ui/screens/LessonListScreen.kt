package com.nourfadaa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nourfadaa.domain.Lesson
import com.nourfadaa.ui.theme.NourColors

@Composable
fun LessonListScreen(
    title: String,
    lessons: List<Lesson>,
    completedIds: Set<String>,
    onOpen: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(NourColors.Cream)) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = NourColors.Teal,
                modifier = Modifier.weight(1f))
            TextButton(onClick = onBack) { Text("رجوع") }
        }
        if (lessons.isEmpty()) {
            Text("الدروس قادمة قريبًا 🌟", fontSize = 20.sp, modifier = Modifier.padding(24.dp))
        }
        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(lessons) { index, lesson ->
                val done = lesson.id in completedIds
                Card(
                    onClick = { onOpen(lesson.id) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = NourColors.Tiles[index % NourColors.Tiles.size]
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(lesson.emoji, fontSize = 36.sp)
                        Spacer(Modifier.height(0.dp).padding(start = 12.dp))
                        Text(
                            lesson.title, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                            color = NourColors.Ink, modifier = Modifier.weight(1f).padding(start = 12.dp)
                        )
                        if (done) Text("✅", fontSize = 26.sp)
                    }
                }
            }
        }
    }
}
