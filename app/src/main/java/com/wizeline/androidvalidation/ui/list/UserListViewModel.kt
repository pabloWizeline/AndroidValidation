package com.wizeline.androidvalidation.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wizeline.androidvalidation.domain.model.User
import com.wizeline.androidvalidation.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserListState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)

sealed class UserListIntent {
    data object LoadUsers : UserListIntent()
    data object Refresh : UserListIntent()
    data class SelectUser(val userId: Int) : UserListIntent()
    data object DismissError : UserListIntent()
}

sealed class UserListEffect {
    data class ShowError(val message: String) : UserListEffect()
    data class NavigateToDetail(val userId: Int) : UserListEffect()
}

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserListState())
    val state: StateFlow<UserListState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<UserListEffect>(replay = 1)
    val effect: SharedFlow<UserListEffect> = _effect.asSharedFlow()

    init {
        handleIntent(UserListIntent.LoadUsers)
    }

    fun handleIntent(intent: UserListIntent) {
        when (intent) {
            UserListIntent.LoadUsers -> loadUsers()
            UserListIntent.Refresh -> loadUsers()
            is UserListIntent.SelectUser -> selectUser(intent.userId)
            UserListIntent.DismissError -> dismissError()
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val users = getUsersUseCase().getOrThrow()
                _state.update { it.copy(isLoading = false, users = users) }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (error: Exception) {
                val message = error.message ?: "Unknown error"
                _state.update { it.copy(isLoading = false, error = message) }
                _effect.emit(UserListEffect.ShowError(message))
            }
        }
    }

    private fun selectUser(userId: Int) {
        viewModelScope.launch {
            _effect.emit(UserListEffect.NavigateToDetail(userId))
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}
