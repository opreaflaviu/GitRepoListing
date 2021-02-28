package com.example.gitreposlistingapp.di

import com.example.gitreposlistingapp.data.api.GitApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(GitApi.BASE_URL)
            .build()

    @Provides
    fun provideGitApi(retrofit: Retrofit): GitApi =
        retrofit.create(GitApi::class.java)

}