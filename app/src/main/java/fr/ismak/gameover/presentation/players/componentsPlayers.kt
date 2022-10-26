package fr.ismak.gameover.presentation.players

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import fr.ismak.gameover.domain.model.Player
import fr.ismak.gameover.domain.util.OrderType
import fr.ismak.gameover.domain.util.PlayerOrder


@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.onBackground
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}

@Composable
fun PlayerItem(
    player: Player,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .matchParentSize(),
        ) {
            val clipPath = Path().apply {
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                lineTo(size.width, cutCornerSize.toPx())
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(if (!isSelected){
                        Color(
                            ColorUtils.blendARGB(player.color, 0x000000, 0.60f)
                        ).toArgb()
                    }  else player.color),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(player.color, 0x000000, 0.45f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() +100f, cutCornerSize.toPx() +100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(
                text = player.name.capitalize(),
                style = MaterialTheme.typography.h6,
                color = Color(if (!isSelected) player.color else White.toArgb()),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Team: ${player.symbol}'s      Score: ${player.score}",
                style = MaterialTheme.typography.h6,
                color = Color(if (!isSelected) player.color else Black.toArgb()),
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Player"
            )
        }
        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Player"
            )
        }
    }
}

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    playerOrder: PlayerOrder = PlayerOrder.Name(OrderType.Ascending),
    onOrderChange: (PlayerOrder) -> Unit
) {

    Column(
        modifier = modifier
    ) {
        Row(modifier = modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text ="Name",
                selected = playerOrder is PlayerOrder.Name,
                onSelect = { onOrderChange(PlayerOrder.Name(playerOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text ="Team",
                selected = playerOrder is PlayerOrder.Symbol,
                onSelect = { onOrderChange(PlayerOrder.Symbol(playerOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text ="Score",
                selected = playerOrder is PlayerOrder.Score,
                onSelect = { onOrderChange(PlayerOrder.Score(playerOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row( modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text ="Ascending",
                selected = playerOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(playerOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text ="Descending",
                selected = playerOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(playerOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}