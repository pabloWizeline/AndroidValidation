package com.wizeline.androidvalidation.domain.usecase

import com.wizeline.androidvalidation.domain.model.User
import com.wizeline.androidvalidation.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> = repository.getUsers()
}
