package com.midnightcoder.animationnotebook.presentation.viewmodels

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock

data class DrawingState(
    val selectedColor: Color=Color.Black,
    val currentPath: PathData?=null,
    val paths:List<PathData> = emptyList()
)

data class PathData(
    val id:String,
    val color:Color,
    val path: List<Offset>
)

val allColors=listOf<Color>(
    Color.Black,
    Color.White,
    Color.Red,
    Color.Blue,
    Color.Yellow,
    Color.Magenta,
    Color.Gray,
    Color.Green,
)

sealed interface DrawingAction{
    data object OnNewPathStart: DrawingAction
    data class OnDraw(val offset: Offset): DrawingAction
    data object OnDrawEnd : DrawingAction
    data object OnClearCanvas : DrawingAction
    data class OnSelectColor(val color:Color): DrawingAction
}

@HiltViewModel
class DrawingViewModel @Inject constructor(): ViewModel() {
    private val _state= MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    fun onAction(action: DrawingAction){
        when(action) {
            is DrawingAction.OnDraw -> onDraw(action.offset)
            DrawingAction.OnClearCanvas -> onClearCanvas()
            DrawingAction.OnDrawEnd -> onDrawEnd()
            DrawingAction.OnNewPathStart -> onNewPathStart()
            is DrawingAction.OnSelectColor -> onSelectColor(action.color)
        }
    }

    private fun onSelectColor(color: Color) {
        _state.update { it.copy(
            selectedColor = color
        ) }
    }

    private fun onNewPathStart() {
        _state.update {
            it.copy(
                currentPath = PathData(
                    id = System.currentTimeMillis().toString(),
                    color = it.selectedColor,
                    path = emptyList()
                )
            )
        }
    }

    private fun onDrawEnd() {
        val currentPathData= state.value.currentPath?:return
        _state.update {
            it.copy(
                currentPath =null,
                paths = it.paths+currentPathData
            )
        }
    }

    private fun onClearCanvas() {
        _state.update { it.copy(
            currentPath = null,
            paths = emptyList()
        ) }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData= state.value.currentPath?:return
        _state.update {
            it.copy(
                currentPath = currentPathData.copy(
                    path = currentPathData.path+offset
                )
            )
        }
    }

}