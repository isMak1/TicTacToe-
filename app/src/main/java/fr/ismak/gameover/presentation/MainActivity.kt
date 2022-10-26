package fr.ismak.gameover.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import fr.ismak.gameover.presentation.game.GameScreen
import fr.ismak.gameover.presentation.add_edit_player.AddEditPlayerScreen
import fr.ismak.gameover.presentation.players.PlayersScreen
import fr.ismak.gameover.presentation.util.Screens
import fr.ismak.gameover.ui.theme.GameOverTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameOverTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController =navController,
                        startDestination = Screens.PlayersScreens.route
                    ) {
                        composable(route = Screens.PlayersScreens.route) {
                            PlayersScreen(navController = navController)
                        }
                        composable(route = Screens.GameScreens.route) {
                            GameScreen(navController = navController)
                        }
                        composable(
                            route = Screens.AddEditPlayerScreens.route +
                                    "?playerId={playerId}&playerColor={playerColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "playerId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "playerColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val color = it.arguments?.getInt("playerColor") ?: -1
                            AddEditPlayerScreen(
                                navController = navController,
                                playerColor = color
                            )
                        }
                    }
                }
            }
        }
    }
}

