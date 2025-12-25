package com.midnightcoder.animationnotebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midnightcoder.animationnotebook.ui.theme.AnimationNoteBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationNoteBookTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val colors = listOf(Color(0xFF02b8f9), Color(0xFF0277fe))
                    Canvas(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {

                        val trianglePath = Path().let {
                            it.moveTo(this.size.width * .20f, this.size.height * .77f)
                            it.lineTo(this.size.width * .20f, this.size.height * 0.95f)
                            it.lineTo(this.size.width * .37f, this.size.height * 0.86f)
                            it.close()
                            it
                        }

                        val electricPath = Path().let {
                            it.moveTo(this.size.width * .20f, this.size.height * 0.60f)
                            it.lineTo(this.size.width * .45f, this.size.height * 0.35f)
                            it.lineTo(this.size.width * 0.56f, this.size.height * 0.46f)
                            it.lineTo(this.size.width * 0.78f, this.size.height * 0.35f)
                            it.lineTo(this.size.width * 0.54f, this.size.height * 0.60f)
                            it.lineTo(this.size.width * 0.43f, this.size.height * 0.45f)
                            it.close()
                            it
                        }

                        drawOval(
                            Brush.verticalGradient(colors = colors),
                            size = Size(this.size.width, this.size.height * 0.95f)
                        )

                        drawPath(
                            path = trianglePath,
                            Brush.verticalGradient(colors = colors),
                            style = Stroke(width = 15f, cap = StrokeCap.Round)
                        )

                        drawPath(path = electricPath, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimationNoteBookTheme {
        Greeting("Android")
    }
}