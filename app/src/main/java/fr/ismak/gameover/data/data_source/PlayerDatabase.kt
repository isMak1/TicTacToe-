package fr.ismak.gameover.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.ismak.gameover.domain.model.Player

@Database(
    entities = [Player::class],
    version = 1,
)
abstract class PlayerDatabase: RoomDatabase() {

    abstract val playerDao: PlayerDao

    companion object {
        const val DATABASE_NAME = "players_db"
    }
}