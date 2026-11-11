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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nourfadaa.data.ChildEntity
import com.nourfadaa.ui.theme.NourColors

@Composable
fun ChildPickerScreen(children: List<ChildEntity>, onPick: (Long) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(NourColors.Cream).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("نُور وفداء 🌟", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = NourColors.Teal)
        Spacer(Modifier.height(8.dp))
        Text("من سيتعلم اليوم؟", fontSize = 24.sp, color = NourColors.Ink)
        Spacer(Modifier.height(32.dp))
        if (children.isEmpty()) CircularProgressIndicator()
        children.forEachIndexed { index, child ->
            Button(
                onClick = { onPick(child.id) },
                modifier = Modifier.fillMaxWidth().height(96.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (index % 2 == 0) NourColors.Pink else NourColors.Lavender
                )
            ) {
                Text("👧 ${child.name} — ${child.ageYears} سنوات", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
