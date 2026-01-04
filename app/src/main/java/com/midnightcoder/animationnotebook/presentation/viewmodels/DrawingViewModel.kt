package com.midnightcoder.animationnotebook.presentation.viewmodels

import android.R.attr.bitmap
import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midnightcoder.animationnotebook.presentation.screens.canvasscreen.ToolType
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DrawingState(
    val selectedColor: Color = Color.Black,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val selectedTool: ToolType = ToolType.BRUSH,
    val currentThickness: Float = 10f,
    val bitmap: Bitmap? = null
)

data class PathData(
    val id: String,
    val color: Color,
    val path: List<Offset>,
    val isEraser: Boolean = false,
    val thickness: Float = 10f
)


val allColors = listOf<Color>(
    Color.Black,
    Color.White,
    Color.Red,
    Color.Blue,
    Color.Yellow,
    Color.Magenta,
    Color.Gray,
    Color.Green,
)

sealed interface DrawingAction {
    data object OnNewPathStart : DrawingAction
    data class OnDraw(val offset: Offset) : DrawingAction
    data object OnDrawEnd : DrawingAction
    data object OnClearCanvas : DrawingAction
    data class OnSelectColor(val color: Color) : DrawingAction
    data class OnSelectedTool(val tool: ToolType) : DrawingAction
    data class OnThicknessChanged(val width: Float) : DrawingAction
    data class OnBucketFill(val offset: Offset) : DrawingAction
    data class InitBitmap(val w: Int, val h: Int) : DrawingAction
}

@HiltViewModel
class DrawingViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    fun onAction(action: DrawingAction) {
        when (action) {
            is DrawingAction.OnDraw -> onDraw(action.offset)
            DrawingAction.OnClearCanvas -> onClearCanvas()
            DrawingAction.OnDrawEnd -> onDrawEnd()
            DrawingAction.OnNewPathStart -> onNewPathStart()
            is DrawingAction.OnThicknessChanged -> onThicknessChange(action.width)
            is DrawingAction.OnSelectedTool -> onSelectedTool(action.tool)
            is DrawingAction.OnSelectColor -> onSelectColor(action.color)
            is DrawingAction.OnBucketFill-> onBucketFill(action.offset)
            is DrawingAction.InitBitmap -> initBitmap(action.w, action.h)
        }
    }

    private fun onBucketFill(offset: Offset) {
        val state = _state.value
        val bitmap = state.bitmap ?: return   // 🔥 safety

        val x = offset.x.toInt()
        val y = offset.y.toInt()

        if (x !in 0 until bitmap.width || y !in 0 until bitmap.height) return

        val targetColor = bitmap.getPixel(x, y)
        val newColor = state.selectedColor.toArgb()
        viewModelScope.launch (Dispatchers.Default){
            floodFill(bitmap, x, y, targetColor, newColor)
            _state.update {
                it.copy(bitmap = bitmap)
            }
        }


        // force recomposition
    }
    fun initBitmap(width: Int, height: Int) {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmp.eraseColor(android.graphics.Color.WHITE) // 👈 IMPORTANT
        _state.update { it.copy(bitmap = bmp) }
    }

    private fun onThicknessChange(width: Float) {
        _state.update {
            it.copy(
                currentThickness = width
            )
        }
    }

    private fun onSelectedTool(tool: ToolType) {
        _state.update {
            it.copy(
                selectedTool = tool,
                currentPath = null
            )
        }
    }


    private fun onSelectColor(color: Color) {
        _state.update {
            it.copy(
                selectedColor = color
            )
        }
    }

    private fun onNewPathStart() {
        val state = _state.value

        // Brush aur Eraser hi abhi draw karte hain
        if (state.selectedTool != ToolType.BRUSH &&
            state.selectedTool != ToolType.ERASER
        ) return
        _state.update {
            it.copy(
                currentPath = PathData(
                    id = System.currentTimeMillis().toString(),
                    color = it.selectedColor,
                    path = emptyList(),
                    isEraser = it.selectedTool == ToolType.ERASER,
                    thickness = it.currentThickness
                )
            )
        }
    }

    private fun onDrawEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = null,
                paths = it.paths + currentPathData
            )
        }
    }

    private fun onClearCanvas() {
        _state.update {
            it.copy(
                currentPath = null,
                paths = emptyList()
            )
        }
    }

    private fun onDraw(offset: Offset) {
        val state = _state.value

        if (state.selectedTool != ToolType.BRUSH &&
            state.selectedTool != ToolType.ERASER
        ) return

        val currentPathData = state.currentPath ?: return

        _state.update {
            it.copy(
                currentPath = currentPathData.copy(
                    path = currentPathData.path + offset
                )
            )
        }
    }

    fun floodFill(
        bitmap: Bitmap,
        x: Int,
        y: Int,
        targetColor: Int,
        replacementColor: Int
    ) {
        if (targetColor == replacementColor) return
        if (bitmap.getPixel(x, y) != targetColor) return

        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(x to y)

        while (queue.isNotEmpty()) {
            val (cx, cy) = queue.removeFirst()

            if (cx < 0 || cy < 0 ||
                cx >= bitmap.width || cy >= bitmap.height
            ) continue

            if (bitmap.getPixel(cx, cy) != targetColor) continue

            bitmap.setPixel(cx, cy, replacementColor)

            queue.add(cx + 1 to cy)
            queue.add(cx - 1 to cy)
            queue.add(cx to cy + 1)
            queue.add(cx to cy - 1)
        }
    }

}



