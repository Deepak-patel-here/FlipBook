package com.midnightcoder.animationnotebook.presentation.screens.canvasscreen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class StrokeModel(
    val points: MutableList<Offset>,
    val color: Color,
    val strokeWidth: Float,
    val isEraser: Boolean
)
