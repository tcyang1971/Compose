package tw.edu.pu.tcyang.compose

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import tw.edu.pu.tcyang.compose.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("多指觸控Compose實例")
                }
            }
        }
    }
}

data class Points(
    val x: Float,
    val y: Float
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Greeting(name: String) {
    var X = remember { mutableStateListOf(0f) }
    var Y = remember { mutableStateListOf(0f) }
    var Fingers = remember {  mutableStateOf (0)  }
    val handImage = ImageBitmap.imageResource(id = R.drawable.hand)

    var PaintColor:Color
    var colors = arrayListOf(
        ColorRed, ColorOrange, ColorYellow, ColorGreen,
        ColorBlue, ColorIndigo, ColorPurple
    )

    val paths = remember { mutableStateListOf<Points>() }

    Box(
        modifier = Modifier.fillMaxSize()
            .pointerInteropFilter { event ->
                Fingers.value = event.getPointerCount()
                X.clear()
                Y.clear()
                for (i in 0..Fingers.value - 1) {
                    X.add( event.getX(i))
                    Y.add (event.getY(i))
                }
                //true
                //true
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        paths.clear()
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        paths += Points(event.x, event.y)
                        true
                    }
                    else -> false
                }
            }
    ){
        Canvas(modifier = Modifier){
            //drawCircle(Color.Yellow, 100f, Offset(X.value, Y.value))
            //drawImage(handImage, Offset(X.value-handImage.width/2,Y.value-handImage.height/2))
            for (i in 0..Fingers.value - 1) {
                PaintColor = colors[i % 7]
                drawCircle(PaintColor, 100f, Offset(X[i], Y[i]))
            }

            val p = Path()
            //p.moveTo(500f, 300f)
            //p.lineTo(300f,600f)
            var j = 0
            for (path in paths) {
                if (j==0){  //第一筆不畫
                    p.moveTo(path.x, path.y)
                }
                else{
                    p.lineTo(path.x, path.y)
                }
                j++
            }
            drawPath(p, color = Color.Black,
                style = Stroke(width = 30f, join = StrokeJoin.Round)
            )

        }
    }

    Column {

        Row{
            Text(text = "$name",
                fontFamily = FontFamily(Font(R.font.kai)),
                fontSize = 25.sp,
                color = Color.Blue)

            Image(
                painter = painterResource(id = R.drawable.hand),
                contentDescription = "手掌圖片",
                alpha = 0.7f,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Blue)
            )
        }
        Text(text = "作者：楊子青")

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var count = remember { mutableStateOf(0) }
            Text(
                text = count.value.toString(),
                fontSize = 50.sp,
                modifier = Modifier.clickable { count.value += 1 }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTheme {
        Greeting("多指觸控Compose實例")
    }
}