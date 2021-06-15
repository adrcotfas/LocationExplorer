package com.adrcotfas.locationexplorer.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.adrcotfas.locationexplorer.business.LocationProvider
import com.adrcotfas.locationexplorer.retrofit.FlickrApi
import com.adrcotfas.locationexplorer.business.PhotoUrlManager
import com.adrcotfas.locationexplorer.room.PhotoUrlDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): PhotoUrlDatabase =
        Room.databaseBuilder(app, PhotoUrlDatabase::class.java, PhotoUrlDatabase.DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(FlickrApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideFlickrApi(retrofit: Retrofit): FlickrApi =
        retrofit.create(FlickrApi::class.java)

    @Provides
    @Singleton
    fun providePhotoManager(flickrApi: FlickrApi, db: PhotoUrlDatabase) = PhotoUrlManager(flickrApi, db)

    @Provides
    @Singleton
    fun provideLocationProvider(@ApplicationContext appContext: Context, listener: PhotoUrlManager) =
        LocationProvider(appContext, listener)
}