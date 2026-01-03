package com.midnightcoder.animationnotebook.presentation.screens.canvasscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.midnightcoder.animationnotebook.R

data class ToolModel(
    val image: Int? = null,
    val imageVector: ImageVector? = null,
    val isSelected: Boolean,
    val onClick: () -> Unit,
    val name: ToolType
)

@Composable
fun TopToolBar(
    selectedTool: ToolType,
    onToolSelected: (ToolType) -> Unit,
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit,
    onThicknessChanged: (Float) -> Unit,
    initialThickness: Float,
    initialColor: Color
) {

    var showBrushOptions by remember { mutableStateOf(false) }


    val listOfTools = listOf<ToolModel>(
        ToolModel(
            image = R.drawable.paint_brush,
            isSelected = selectedTool == ToolType.BRUSH,
            onClick = { onToolSelected(ToolType.BRUSH) },
            name =ToolType.BRUSH
        ),
        ToolModel(
            image = R.drawable.eraser,
            isSelected = selectedTool == ToolType.ERASER,
            onClick = { onToolSelected(ToolType.ERASER) },
            name = ToolType.ERASER
        ),
        ToolModel(
            image = R.drawable.lasso,
            isSelected = selectedTool == ToolType.LASSO,
            onClick = { onToolSelected(ToolType.LASSO) },
            name = ToolType.LASSO
        ),
        ToolModel(
            image = R.drawable.paint_bucket,
            isSelected = selectedTool == ToolType.BUCKET,
            onClick = { onToolSelected(ToolType.BUCKET) },
            name = ToolType.BUCKET
        ),
        ToolModel(
            image = R.drawable.text,
            isSelected = selectedTool == ToolType.TEXT,
            onClick = { onToolSelected(ToolType.TEXT) },
            name =ToolType.TEXT
        )
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                listOfTools.forEachIndexed { index, tool ->
                    val shape = when (index) {
                        0 -> RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp
                        )

                        listOfTools.lastIndex -> RoundedCornerShape(
                            topEnd = 16.dp,
                            bottomEnd = 16.dp
                        )

                        else -> RoundedCornerShape(0.dp)
                    }
                    ToolButton(
                        image = tool.image,
                        isSelected = tool.isSelected,
                        onClick = tool.onClick,
                        shape = shape,
                        onLongPress = {
                            if (tool.name == ToolType.BRUSH) {
                                showBrushOptions = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

            AnimatedVisibility(
                visible = showBrushOptions,
                enter = slideInVertically(
                    initialOffsetY = { -it / 2 }
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it / 2 }
                ) + fadeOut()
            ) {
                BrushOptionsCard(
                    initialColor = initialColor,
                    initialThickness = initialThickness,
                    onConfirm = { width, color ->
                        onColorSelected(color)
                        onThicknessChanged(width)
                        showBrushOptions = false
                    },
                    onCancel = { showBrushOptions = false }
                )
            }



    }

}

@Composable
fun ToolButton(
    icon: ImageVector? = null,
    image: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongPress: (() -> Unit)? = null,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(shape)
            .background(
                if (isSelected) Color(0xFFFF5A7A)
                else Color.Transparent
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = {onLongPress?.invoke()}
            ) ,
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color.White else Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
        if (image != null) {
            Icon(
                painter = painterResource(image),
                contentDescription = null,
                tint = if (isSelected) Color.White else Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }

    }


}