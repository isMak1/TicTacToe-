package fr.ismak.gameover.data.data_source

import androidx.room.*
import fr.ismak.gameover.domain.model.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {

    //This function cannot be suspend because it return a flow of multiple results
    @Query("SELECT * FROM player")
    fun getPlayers(): Flow<List<Player>>

    @Query("SELECT * FROM player WHERE id = :id")
    suspend fun getPlayerById(id: Int): Player?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    @Delete
    suspend fun deletePlayer(player: Player)
}