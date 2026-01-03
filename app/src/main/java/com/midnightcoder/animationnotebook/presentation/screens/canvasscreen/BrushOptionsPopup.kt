package com.midnightcoder.animationnotebook.presentation.screens.canvasscreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup

@Composable
fun BrushOptionsCard(
    initialThickness: Float,
    initialColor: Color,
    onCancel: () -> Unit,
    onConfirm: (Float, Color) -> Unit
) {

    var thickness by remember { mutableStateOf(initialThickness) }
    var selectedColor by remember { mutableStateOf(initialColor) }
    var showColorPicker by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* -------- COLOR + SLIDER -------- */
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Color preview
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(selectedColor)
                        .border(
                            1.dp,
                            Color.Black.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            showColorPicker = !showColorPicker
                        }
                )

                Slider(
                    value = thickness,
                    onValueChange = { thickness = it },
                    valueRange = 2f..40f,
                    modifier = Modifier.weight(1f)
                )
            }

            /* -------- INLINE COLOR PICKER -------- */
            if (showColorPicker) {
                InlineColorPicker (
                    selectedColor = selectedColor,
                    onColorSelected = {
                        selectedColor = it
                        showColorPicker = false
                    }
                )
            }

            /* -------- ACTIONS -------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancel")
                }
                TextButton(
                    onClick = {
                        onConfirm(thickness, selectedColor)
                    }
                ) {
                    Text("OK")
                }
            }
        }
    }
}


