package com.wizeline.androidvalidation.ui.list

import com.wizeline.androidvalidation.domain.model.Address
import com.wizeline.androidvalidation.domain.model.Company
import com.wizeline.androidvalidation.domain.model.User
import com.wizeline.androidvalidation.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var viewModel: UserListViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val testUsers = listOf(
        User(
            id = 1,
            name = "Leanne Graham",
            username = "Bret",
            email = "Sincere@april.biz",
            phone = "1-770-736-8031 x56442",
            website = "hildegard.org",
            company = Company("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"),
            address = Address("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874")
        ),
        User(
            id = 2,
            name = "Ervin Howell",
            username = "Antonette",
            email = "Shanna@melissa.tv",
            phone = "010-692-6593 x09125",
            website = "anastasia.net",
            company = Company("Deckow-Crist", "Proactive didactic contingency", "synergize scalable supply-chains"),
            address = Address("Victor Plains", "Suite 879", "Wisokyburgh", "90566-7771")
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getUsersUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `users are loaded when the view model is created`() = runTest {
        whenever(getUsersUseCase()) doReturn Result.success(testUsers)

        viewModel = UserListViewModel(getUsersUseCase)
        advanceUntilIdle()

        val state = viewModel.state.first { !it.isLoading }
        assertEquals(testUsers, state.users)
        assertNull(state.error)
        assertFalse(state.isLoading)
    }

    @Test
    fun `a failed load publishes the error message`() = runTest {
        whenever(getUsersUseCase()) doReturn Result.failure(IOException("Network error"))

        viewModel = UserListViewModel(getUsersUseCase)
        advanceUntilIdle()

        val state = viewModel.state.first { !it.isLoading }
        assertEquals("Network error", state.error)
        assertTrue(state.users.isEmpty())
        assertFalse(state.isLoading)
    }

    @Test
    fun `refresh requests the users again and publishes them`() = runTest {
        whenever(getUsersUseCase()) doReturn Result.success(testUsers)

        viewModel = UserListViewModel(getUsersUseCase)
        advanceUntilIdle()

        val updatedUsers = listOf(testUsers.last())
        whenever(getUsersUseCase()) doReturn Result.success(updatedUsers)

        viewModel.handleIntent(UserListIntent.Refresh)
        advanceUntilIdle()

        val state = viewModel.state.first { !it.isLoading }
        assertEquals(updatedUsers, state.users)
        assertFalse(state.isLoading)
    }

    @Test
    fun `refresh requests the users again after a failure`() = runTest {
        whenever(getUsersUseCase()) doReturn Result.failure(IOException("Network error"))

        viewModel = UserListViewModel(getUsersUseCase)
        advanceUntilIdle()

        assertNotNull(viewModel.state.value.error)

        whenever(getUsersUseCase()) doReturn Result.success(testUsers)

        viewModel.handleIntent(UserListIntent.Refresh)
        advanceUntilIdle()

        val state = viewModel.state.first { !it.isLoading }
        assertEquals(testUsers, state.users)
        assertFalse(state.isLoading)
    }

    @Test
    fun `selecting a user emits a navigate effect with that user id`() = runTest {
        whenever(getUsersUseCase()) doReturn Result.success(testUsers)

        viewModel = UserListViewModel(getUsersUseCase)
        advanceUntilIdle()

        viewModel.handleIntent(UserListIntent.SelectUser(2))

        val effect = viewModel.effect.first { it is UserListEffect.NavigateToDetail }
        assertTrue(effect is UserListEffect.NavigateToDetail)
        assertEquals(2, (effect as UserListEffect.NavigateToDetail).userId)
    }

    @Test
    fun `dismiss error clears the error message`() = runTest {
        whenever(getUsersUseCase()) doReturn Result.failure(IOException("Error occurred"))

        viewModel = UserListViewModel(getUsersUseCase)
        advanceUntilIdle()

        assertNotNull(viewModel.state.value.error)

        viewModel.handleIntent(UserListIntent.DismissError)

        assertNull(viewModel.state.value.error)
    }
}
