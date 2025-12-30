package com.midnightcoder.animationnotebook.presentation.screens.canvasscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.midnightcoder.animationnotebook.presentation.viewmodels.DrawingAction
import com.midnightcoder.animationnotebook.presentation.viewmodels.PathData
import kotlin.math.abs

@Composable
fun DrawCanvas(
    paths: List<PathData>,
    currentPath: PathData?,
    onAction: (DrawingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clipToBounds()
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        onAction(DrawingAction.OnNewPathStart)
                    },
                    onDrag = { change, _ ->
                        onAction(DrawingAction.OnDraw(change.position))
                        change.consume()
                    },
                    onDragEnd = {
                        onAction(DrawingAction.OnDrawEnd)
                    },
                    onDragCancel = {
                        onAction(DrawingAction.OnDrawEnd)
                    }
                )
            }
    ) {
        drawIntoCanvas { canvas ->
            canvas.saveLayer(bounds = Rect(
                0f,
                0f,
                size.width,
                size.height
            ),paint = Paint())
            paths.forEach { pathData->
                drawPath(
                    path = pathData.path,
                    color = pathData.color,
                    isEraser = pathData.isEraser
                )
            }

            currentPath?.let {
                drawPath(
                    path = it.path,
                    color = it.color,
                    isEraser = it.isEraser
                )
            }
            canvas.restore()
        }

    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    isEraser:Boolean,
    thickness: Float = 10f,
) {

    val smoothPath = Path().apply {
        if (path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)
            val smoothness = 5

            for (i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)
                if(dx >= smoothness || dy >= smoothness) {
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,                                      //for smoothness we have to apply a curve function.
                        x2 = to.x,
                        y2 = to.y
                    )
                }
            }
        }


    }

    drawPath(
        path=smoothPath,
        color = if(isEraser)Color.Transparent else color,
        blendMode = if (isEraser) BlendMode.Clear else BlendMode.SrcOver,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}