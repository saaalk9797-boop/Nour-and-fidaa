package com.nourfadaa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.item
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import com.nourfadaa.domain.AgeBand
import com.nourfadaa.domain.GamificationEngine
import com.nourfadaa.domain.Section
import com.nourfadaa.ui.theme.NourColors

@Composable
fun ChildHomeScreen(child: ChildEntity?, onSection: (Section) -> Unit, onSwitchChild: () -> Unit) {
    if (child == null) {
        Box(Modifier.fillMaxSize().background(NourColors.Cream), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val band = AgeBand.fromAge(child.ageYears)
    val level = GamificationEngine.level(child.xp)
    val emojiSize = if (band == AgeBand.TODDLER) 56.sp else 44.sp
    val tileHeight = if (band == AgeBand.TODDLER) 150.dp else 130.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().background(NourColors.Cream),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("أهلًا يا ${child.name} 👋", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = NourColors.Ink)
                    TextButton(onClick = onSwitchChild) { Text("تبديل") }
                }
                Spacer(Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "المستوى $level — ${GamificationEngine.levelTitle(level)}",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { GamificationEngine.levelProgress(child.xp) },
                            modifier = Modifier.fillMaxWidth().height(12.dp),
                            color = NourColors.Gold,
                            trackColor = NourColors.Cream
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Stat("⭐", child.stars.toString(), "نجوم")
                            Stat("🌙", child.streak.toString(), "أيام النور")
                            Stat("✨", child.xp.toString(), "نقاط")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("ماذا سنتعلم اليوم؟", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        itemsIndexed(Section.values().toList()) { index, section ->
            Card(
                onClick = { onSection(section) },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NourColors.Tiles[index % NourColors.Tiles.size]),
                modifier = Modifier.fillMaxWidth().height(tileHeight)
            ) {
                Column(
                    Modifier.fillMaxSize().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(section.emoji, fontSize = emojiSize)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        section.title, fontSize = 17.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center, color = NourColors.Ink
                    )
                }
            }
        }
    }
}

@Composable
private fun Stat(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$emoji $value", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, fontSize = 13.sp)
    }
}
