package com.midnightcoder.animationnotebook.presentation.screens.canvasscreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
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
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
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
    val logicalCanvasSize = Size(1080f, 1920f)
    val layerPaint = remember { Paint() }


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
                        val screenSize = size
                        val canvasTopLeft = Offset(
                            (screenSize.width - 1080f) / 2f,
                            (screenSize.height - 1920f) / 2f
                        )

                        val canvasPoint = change.position - canvasTopLeft

                        if (
                            canvasPoint.x in 0f..1080f &&
                            canvasPoint.y in 0f..1920f
                        ) {
                            onAction(DrawingAction.OnDraw(canvasPoint))
                        }
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

            val canvasTopLeft = Offset(
                (size.width - logicalCanvasSize.width) / 2f,
                (size.height - logicalCanvasSize.height) / 2f
            )

            val canvasRect = Rect(
                canvasTopLeft,
                logicalCanvasSize
            )

            val bounds = Rect(
                0f,
                0f,
                size.width,
                size.height
            )
            canvas.saveLayer(
                bounds, layerPaint
            )



            withTransform({
                translate(canvasTopLeft.x, canvasTopLeft.y)
            }) {
                drawRect(color = Color.White, size = logicalCanvasSize)

                paths.forEach { pathData ->
                    drawPath(
                        path = pathData.path,
                        color = pathData.color,
                        isEraser = pathData.isEraser,
                        thickness = pathData.thickness
                    )
                }

                currentPath?.let {
                    drawPath(
                        path = it.path,
                        color = it.color,
                        isEraser = it.isEraser,
                        thickness = it.thickness
                    )
                }
            }



            canvas.restore()
        }

    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    isEraser: Boolean,
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
                if (dx >= smoothness || dy >= smoothness) {
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
        path = smoothPath,
        color = if (isEraser) Color.Transparent else color,
        blendMode = if (isEraser) BlendMode.Clear else BlendMode.SrcOver,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}