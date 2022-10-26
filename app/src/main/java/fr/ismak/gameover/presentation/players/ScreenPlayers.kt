package fr.ismak.gameover.presentation.players

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.ismak.gameover.presentation.util.Screens
import kotlinx.coroutines.launch

@Composable
fun PlayersScreen(
    navController: NavController,
    viewModel: ViewModelPlayers = hiltViewModel()
) {

    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var selectedItem by remember {
        mutableStateOf("")
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                cutoutShape = RoundedCornerShape(50),
                content = {
                    BottomNavigation {
                        BottomNavigationItem(
                            selected = selectedItem == Screens.PlayersScreens.toString(),
                            onClick = {
                                navController.navigate(Screens.PlayersScreens.route)
                                selectedItem = Screens.PlayersScreens.toString()
                            },
                            icon = {
                                Icon(Icons.Filled.Home, contentDescription = "home")
                            },
                            label = { Text(text = "Home") },
                            alwaysShowLabel = false
                        )

                        BottomNavigationItem(
                            selected = selectedItem == Screens.AddEditPlayerScreens.toString(),
                            onClick = {
                                navController.navigate(Screens.AddEditPlayerScreens.route)
                                selectedItem = Screens.AddEditPlayerScreens.toString()
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
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        if(state.playersInGame.size != 2) {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Choose Exactly Two Players!"
                            )
                        }else{
                            for(player in state.playersInGame) {
                                viewModel.addSelectedToRoom(player)
                                selectedItem = Screens.GameScreens.toString()
                            }

                            navController.navigate(Screens.GameScreens.route)
                        }
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "New Game"
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
                .padding(16.dp)
                .padding(bottom = 30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text ="Players List",
                    style = MaterialTheme.typography.h3
                )
                IconButton(onClick = {
                    viewModel.onEvent(EventPlayers.ToggleOrderSection)
                },
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort"
                    )
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    playerOrder = state.playerOrder,
                    onOrderChange = {
                        viewModel.onEvent(EventPlayers.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 15.dp)
            ) {
                items(state.players) { player ->
                    PlayerItem(
                        player = player,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    when {
                                        state.playersInGame.contains(player) -> {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = "${player.name} already selected!"
                                            )
                                        }
                                        state.playersInGame.size == 1 && player.symbol == state.playersInGame.first().symbol -> {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = "Please pick The second Player From the Opposite Team!"
                                            )
                                        }
                                        state.playersInGame.size == 2 -> {
                                            for (playerInGame in state.playersInGame) {
                                                if (playerInGame.symbol == player.symbol) {
                                                    player.isSelected = true
                                                    playerInGame.isSelected = false
                                                    viewModel.addSelectedToRoom(player)
                                                    viewModel.addSelectedToRoom(playerInGame)
                                                    scaffoldState.snackbarHostState.showSnackbar(
                                                        message = "${player.name} has replaced ${playerInGame.name}!"
                                                    )
                                                }
                                            }
                                        }
                                        else -> {
                                            player.isSelected = true
                                            viewModel.addSelectedToRoom(player)
                                        }
                                    }
                                }
                            },
                        isSelected = state.playersInGame.contains(player),
                        onEditClick = {
                                      navController.navigate(
                                          Screens.AddEditPlayerScreens.route +
                                                  "?playerId=${player.id}&playerColor=${player.color}"
                                      )
                        },
                        onDeleteClick = {
                            viewModel.onEvent(EventPlayers.DeletePlayer(player))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Player Deleted",
                                    actionLabel = "Undo"
                                )
                                if(result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(EventPlayers.RestorePlayer)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}