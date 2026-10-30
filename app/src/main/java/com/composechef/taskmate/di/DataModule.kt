package com.composechef.taskmate.di

import com.composechef.taskmate.data.AuthRepository
import com.composechef.taskmate.data.firebase.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.composechef.taskmate.data.ListRepository
import com.composechef.taskmate.data.firebase.FirestoreListRepository
import com.composechef.taskmate.data.TaskRepository
import com.composechef.taskmate.data.firebase.FirestoreTaskRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(repository: FirebaseAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindListRepository(repository: FirestoreListRepository): ListRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(repository: FirestoreTaskRepository): TaskRepository
}
