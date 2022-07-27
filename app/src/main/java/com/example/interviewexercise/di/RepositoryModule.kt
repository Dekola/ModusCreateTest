package com.example.interviewexercise.di

import com.example.interviewexercise.repository.IMovieRepository
import com.example.interviewexercise.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideGalleryRepository(movieRepository: MovieRepository): IMovieRepository
}