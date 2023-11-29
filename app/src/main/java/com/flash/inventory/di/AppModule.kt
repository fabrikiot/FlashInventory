package com.flash.inventory.di

import android.content.Context
import com.flash.inventory.data.network.ApiInterface
import com.flash.inventory.data.network.RemoteDataSource
import com.flash.inventory.data.repository.CommonRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }

    @Singleton
    @Provides
    fun provideApiInterface(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): ApiInterface {
        return remoteDataSource.buildApi(ApiInterface::class.java, context)
    }

    @Provides
    fun provideCommonRepo(
        apiInterface: ApiInterface
    ): CommonRepo {
        return CommonRepo(apiInterface)
    }

}