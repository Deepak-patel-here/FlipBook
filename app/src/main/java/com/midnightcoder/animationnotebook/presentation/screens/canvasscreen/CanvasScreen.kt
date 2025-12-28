package com.midnightcoder.animationnotebook.presentation.screens.canvasscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.midnightcoder.animationnotebook.presentation.viewmodels.DrawingAction
import com.midnightcoder.animationnotebook.presentation.viewmodels.DrawingViewModel
import com.midnightcoder.animationnotebook.presentation.viewmodels.allColors

@Composable
fun CanvasScreen(
    drawingViewModel: DrawingViewModel = hiltViewModel<DrawingViewModel>(),
    modifier: Modifier = Modifier
) {
    val state = drawingViewModel.state.collectAsStateWithLifecycle()
    Column(modifier = modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally) {
        DrawCanvas(
            paths = state.value.paths,
            currentPath = state.value.currentPath,
            onAction = drawingViewModel::onAction,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            allColors.fastForEach { color ->
                Box(modifier = Modifier.size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = 2.dp,
                        color = if(state.value.selectedColor==color) Color.Black else color,
                        shape = CircleShape
                        )
                    .clickable(onClick = {drawingViewModel.onAction(DrawingAction.OnSelectColor(color))})
                )
            }

        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {drawingViewModel.onAction(DrawingAction.OnClearCanvas)}) {
            Text("Clear Canvas")
        }
    }


}