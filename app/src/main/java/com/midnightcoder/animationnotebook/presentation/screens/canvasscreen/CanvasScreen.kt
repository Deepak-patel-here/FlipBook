package com.midnightcoder.animationnotebook.presentation.screens.canvasscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.midnightcoder.animationnotebook.presentation.viewmodels.DrawingAction
import com.midnightcoder.animationnotebook.presentation.viewmodels.DrawingViewModel

@Composable
fun CanvasScreen(
    drawingViewModel: DrawingViewModel = hiltViewModel<DrawingViewModel>(),
    modifier: Modifier = Modifier
) {
    val state = drawingViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        drawingViewModel.onAction(DrawingAction.InitBitmap(1080,1920))
    }
    Column(modifier = modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally) {

            TopToolBar(
                selectedTool = state.value.selectedTool,
                onToolSelected = { drawingViewModel.onAction(DrawingAction.OnSelectedTool(it)) },
                onColorSelected = {
                    drawingViewModel.onAction(DrawingAction.OnSelectColor(it))
                },
                onThicknessChanged = {
                    drawingViewModel.onAction(DrawingAction.OnThicknessChanged(it))
                },
                initialThickness=state.value.currentThickness,
                initialColor = state.value.selectedColor
            )



        DrawCanvas(
            selectedTool = state.value.selectedTool,
            bitmap = state.value.bitmap?.asImageBitmap(),
            paths = state.value.paths,
            currentPath = state.value.currentPath,
            onAction = drawingViewModel::onAction,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
    }


}