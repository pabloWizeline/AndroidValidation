package com.wizeline.androidvalidation.data.cache

import com.wizeline.androidvalidation.domain.model.User
import javax.inject.Inject

class UserMemoryCache @Inject constructor() {

    private val cachedUsers = mutableListOf<User>()

    fun replaceAll(users: List<User>) {
        cachedUsers.clear()
        cachedUsers.addAll(users)
    }

    fun put(user: User) {
        cachedUsers.removeAll { it.id == user.id }
        cachedUsers.add(user)
    }

    fun findById(id: Int): User? = cachedUsers.firstOrNull()
}
