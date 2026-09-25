package com.wizeline.androidvalidation.domain.repository

import com.wizeline.androidvalidation.domain.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUser(id: Int): Result<User>
}
