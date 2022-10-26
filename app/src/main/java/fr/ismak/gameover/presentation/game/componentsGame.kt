package fr.ismak.gameover.presentation.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import fr.ismak.gameover.domain.model.Player


@Composable
fun BoardBase() {
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        drawLine(
            color = Color.Gray,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width / 3, y = 0f),
            end = Offset(x = size.width / 3, y = size.height)
        )
        drawLine(
            color = Color.Gray,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 2 / 3, y = 0f),
            end = Offset(x = size.width * 2 / 3, y = size.height)
        )
        drawLine(
            color = Color.Gray,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height / 3),
            end = Offset(x = size.width, y = size.height / 3)
        )
        drawLine(
            color = Color.Gray,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 2 / 3),
            end = Offset(x = size.width, y = size.height * 2 / 3)
        )
    }
}


@Composable
fun Circle(player: Player) {
    Canvas(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        drawCircle(
            color = Color(player.color),
            style = Stroke(width = 20f)
        )
    }
}

@Composable
fun Cross(player: Player) {
    Canvas(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        drawLine(
            color = Color(player.color),
            strokeWidth = 20f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = size.height)
        )
        drawLine(
            color = Color(player.color),
            strokeWidth = 20f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height),
            end = Offset(x = size.width, y = 0f)
        )
    }
}




@Composable
fun WinVerticalLine1() {
    Canvas(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width*2/15, y = 0f),
            end = Offset(x = size.width*2/15, y = size.height)
        )
    }
}

@Composable
fun WinVerticalLine2() {
    Canvas(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width*1/2, y = 0f),
            end = Offset(x = size.width*1/2, y = size.height)
        )
    }
}

@Composable
fun WinVerticalLine3() {
    Canvas(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width*6/7, y = 0f),
            end = Offset(x = size.width*6/7, y = size.height)
        )
    }
}

@Composable
fun WinHorizontalLine1() {
    Canvas(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()
    ) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height*2/15),
            end = Offset(x = size.width, y = size.height*2/15)
        )
    }
}

@Composable
fun WinHorizontalLine2() {
    Canvas(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()
    ) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height*1/2),
            end = Offset(x = size.width, y = size.height*1/2)
        )
    }
}

@Composable
fun WinHorizontalLine3() {
    Canvas(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()
    ) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height*6/7),
            end = Offset(x = size.width, y = size.height*6/7)
        )
    }
}

@Composable
fun WinDiagonalLine1() {
    Canvas(modifier = Modifier.padding(20.dp).fillMaxSize()) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = size.height)
        )
    }
}

@Composable
fun WinDiagonalLine2() {
    Canvas(modifier = Modifier.padding(20.dp).fillMaxSize()) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height),
            end = Offset(x = size.width, y = 0f)
        )
    }
}
