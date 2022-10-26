package fr.ismak.gameover.presentation.game

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.ismak.gameover.presentation.util.Screens
import fr.ismak.gameover.ui.theme.BlueCustom

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GameScreen(
    viewModel: ViewModelGame = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.state

    val playerXName = viewModel.state.playerXName
    val playerXScore = viewModel.state.playerXScore

    val playerOName = viewModel.state.playerOName
    val playerOScore = viewModel.state.playerOScore

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                cutoutShape = RoundedCornerShape(50),
                content = {
                    BottomNavigation {
                        BottomNavigationItem(
                            selected = false,
                            onClick = {},
                            icon = {
                                Icon(Icons.Filled.Home, contentDescription = "home")
                            },
                            label = { Text(text = "Home") },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            selected = false,
                            onClick = {},
                            icon = {
                                Icon(Icons.Default.PersonAdd, contentDescription = "Add Player")
                            },
                            label = { Text(text = "New Player") },
                            alwaysShowLabel = false
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Screens.PlayersScreens.route)
                    viewModel.onEvent(EventGame.Exit)
                },
                backgroundColor = BlueCustom,
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Exit"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,

        scaffoldState = scaffoldState
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp)
                .background(state.backGroundColor),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$playerOName: $playerOScore", fontSize = 35.sp)
                Text(text = "Draw: ${state.drawCount}", fontSize = 35.sp)
                Text(text = "$playerXName: $playerXScore", fontSize = 35.sp)
            }
            Text(
                text = "Tic Tac Toe",
                fontSize = 75.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Cursive,
                color = BlueCustom
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(
                        elevation = 25.dp,
                        shape = RoundedCornerShape(20.dp),
                    )
                    .background(Color.Black)
                    .clip(RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                BoardBase()
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    viewModel.boardItems.forEach { (cellNo, boardCellValue) ->
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) {
                                        viewModel.onEvent(
                                            EventGame.BoardTapped(cellNo)
                                        )
                                    },
                            ) {
                                AnimatedVisibility(
                                    visible = viewModel.boardItems[cellNo] != BoardCellValue.NONE,
                                    enter = scaleIn(tween(700))
                                ) {
                                    if (boardCellValue == BoardCellValue.CIRCLE) {
                                        Circle(state.playerO)
                                    } else if (boardCellValue == BoardCellValue.CROSS) {
                                        Cross(state.playerX)
                                    }
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.matchParentSize(),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(
                        visible = state.hasWon,
                        enter = fadeIn(tween(2000))
                    ) {
                        DrawVictoryLine(state = state)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.hintText,
                    fontSize = 38.sp,
                    fontStyle = FontStyle.Italic
                )
                Button(
                    onClick = {
                        viewModel.onEvent(
                            EventGame.NewGame
                        )
                    },
                    shape = RoundedCornerShape(5.dp),
                    elevation = ButtonDefaults.elevation(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BlueCustom,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Play Again",
                        fontSize = 38.sp
                    )
                }

            }
        }
    }
}
@Composable
fun DrawVictoryLine(
    state: GameState
) {
    when (state.victoryType) {
        VictoryType.HORIZONTAL1 -> WinHorizontalLine1()
        VictoryType.HORIZONTAL2 -> WinHorizontalLine2()
        VictoryType.HORIZONTAL3 -> WinHorizontalLine3()
        VictoryType.VERTICAL1 -> WinVerticalLine1()
        VictoryType.VERTICAL2 -> WinVerticalLine2()
        VictoryType.VERTICAL3 -> WinVerticalLine3()
        VictoryType.DIAGONAL1 -> WinDiagonalLine1()
        VictoryType.DIAGONAL2 -> WinDiagonalLine2()
        VictoryType.NONE -> {}
    }
}
