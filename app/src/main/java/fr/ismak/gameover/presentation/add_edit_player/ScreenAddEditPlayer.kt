package fr.ismak.gameover.presentation.add_edit_player

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.presentation.util.Screens
import fr.ismak.gameover.ui.theme.BlueCustom
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditPlayerScreen(
    navController: NavController,
    playerColor: Int,
    viewModel: ViewModelAddEditPlayer = hiltViewModel()
) {

    val nameState = viewModel.playerName.value
    val symbolState = viewModel.playerSymbol.value

    val scaffoldState = rememberScaffoldState()
    var selectedItem by remember {
        mutableStateOf("")
    }

    val playerBackgroundAnimatable = remember {
        Animatable(Color(if(playerColor != -1 ) playerColor else viewModel.playerColor.value))
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true ) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is ViewModelAddEditPlayer.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                ViewModelAddEditPlayer.UiEvent.SavePlayer -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                cutoutShape = RoundedCornerShape(50),
                content = {
                    BottomNavigation {
                        BottomNavigationItem(
                            selected = selectedItem == toString(),
                            onClick = {
                                navController.navigate(Screens.PlayersScreens.route)
                                selectedItem = toString()
                            },
                            icon = {
                                Icon(Icons.Filled.Home, contentDescription = "home")
                            },
                            label = { Text(text = "Home") },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            selected = selectedItem == toString(),
                            onClick = {
                                navController.navigate(Screens.AddEditPlayerScreens.route)
                                selectedItem = toString()
                            },
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
            FloatingActionButton(onClick = {
                viewModel.onEvent(EventAddEditPlayer.SavePlayerAddEditPlayer)
            },
                backgroundColor = BlueCustom,
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save Player"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        scaffoldState = scaffoldState
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(playerBackgroundAnimatable.value)
            .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Player.playerColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(15.dp, CircleShape)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = 3.dp,
                                color = if (viewModel.playerColor.value == colorInt) {
                                    Color.Black
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    playerBackgroundAnimatable.animateTo(
                                        targetValue = Color(colorInt),
                                        animationSpec = tween(
                                            durationMillis = 500
                                        )
                                    )
                                }
                                viewModel.onEvent(EventAddEditPlayer.ChangeColor(colorInt))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text =nameState.text,
                hint =nameState.hint,
                onValueChange ={
                               viewModel.onEvent(EventAddEditPlayer.EnteredName(it))
                },
                onFocusChange = {
                    viewModel.onEvent(EventAddEditPlayer.ChangeNameFocus(it))
                },
                isHintVisible = nameState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text =symbolState.text,
                hint =symbolState.hint,
                onValueChange ={
                               viewModel.onEvent(EventAddEditPlayer.EnteredSymbol(it))
                },
                onFocusChange = {
                    viewModel.onEvent(EventAddEditPlayer.ChangeSymbolFocus(it))
                },
                isHintVisible = symbolState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.h5,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}