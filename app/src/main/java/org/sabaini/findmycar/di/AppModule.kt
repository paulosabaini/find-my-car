package org.sabaini.findmycar.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.sabaini.findmycar.contract.FindMyCarContract
import org.sabaini.findmycar.model.Model
import org.sabaini.findmycar.model.api.DirectionsApi
import org.sabaini.findmycar.model.db.LocationDao
import org.sabaini.findmycar.model.db.LocationDatabase
import org.sabaini.findmycar.utilities.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLocationDatabase(@ApplicationContext context: Context): LocationDatabase {
        return Room.databaseBuilder(
            context,
            LocationDatabase::class.java,
            "locations"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideLocationDao(database: LocationDatabase): LocationDao {
        return database.locationDao()
    }

    @Singleton
    @Provides
    fun provideDirectionsApi(): DirectionsApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(DirectionsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideModel(
        locationDao: LocationDao,
        directionsApi: DirectionsApi
    ): FindMyCarContract.Model {
        return Model(locationDao, directionsApi)
    }
}