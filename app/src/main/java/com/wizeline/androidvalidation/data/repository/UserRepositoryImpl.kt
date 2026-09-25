package com.wizeline.androidvalidation.data.repository

import com.wizeline.androidvalidation.data.api.UserApi
import com.wizeline.androidvalidation.data.cache.UserMemoryCache
import com.wizeline.androidvalidation.data.mapper.toDomain
import com.wizeline.androidvalidation.domain.model.User
import com.wizeline.androidvalidation.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val cache: UserMemoryCache
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> = runCatching {
        api.getUsers()
            .toDomain()
            .also { cache.replaceAll(it) }
    }

    override suspend fun getUser(id: Int): Result<User> = runCatching {
        cache.findById(id) ?: api.getUser(id).toDomain().also { cache.put(it) }
    }
}
