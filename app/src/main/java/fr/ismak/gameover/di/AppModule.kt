package fr.ismak.gameover.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.ismak.gameover.data.data_source.PlayerDatabase
import fr.ismak.gameover.data.repository.PlayerRepositoryImpl
import fr.ismak.gameover.domain.repository.PlayerRepository
import fr.ismak.gameover.domain.use_case.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //We use Provides when there is no constructor to Inject
    @Provides
    @Singleton
    fun providePlayerDatabase(app: Application): PlayerDatabase {
        return Room.databaseBuilder(
            app,
            PlayerDatabase::class.java,
            PlayerDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePlayerRepository(db: PlayerDatabase): PlayerRepository {
        return PlayerRepositoryImpl(db.playerDao)
    }

    @Provides
    @Singleton
    fun providePlayerUseCases(repository: PlayerRepository): PlayerUseCases {
        return PlayerUseCases(
            getPlayersUseCase = GetPlayersUseCase(repository),
            deletePlayerUseCase = DeletePlayerUseCase(repository),
            addPlayerUseCase = AddPlayerUseCase(repository),
            getPlayerUseCase = GetPlayerUseCase(repository)
        )
    }
}
