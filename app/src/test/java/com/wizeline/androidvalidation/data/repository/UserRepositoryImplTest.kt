package com.wizeline.androidvalidation.data.repository

import com.wizeline.androidvalidation.data.api.UserApi
import com.wizeline.androidvalidation.data.cache.UserMemoryCache
import com.wizeline.androidvalidation.data.remote.dto.AddressDto
import com.wizeline.androidvalidation.data.remote.dto.CompanyDto
import com.wizeline.androidvalidation.data.remote.dto.UserDto
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserRepositoryImplTest {

    private val user1Dto = UserDto(
        id = 1,
        name = "Leanne Graham",
        username = "Bret",
        email = "Sincere@april.biz",
        phone = "1-770-736-8031 x56442",
        website = "hildegard.org",
        company = CompanyDto("Romaguera-Crona", "Multi-layered client-server neural-net", "harness real-time e-markets"),
        address = AddressDto("Kulas Light", "Apt. 556", "Gwenborough", "92998-3874")
    )

    private val user2Dto = UserDto(
        id = 2,
        name = "Ervin Howell",
        username = "Antonette",
        email = "Shanna@melissa.tv",
        phone = "010-692-6593 x09125",
        website = "anastasia.net",
        company = CompanyDto("Deckow-Crist", "Proactive didactic contingency", "synergize scalable supply-chains"),
        address = AddressDto("Victor Plains", "Suite 879", "Wisokyburgh", "90566-7771")
    )

    private val api: UserApi = mock()

    private fun repository() = UserRepositoryImpl(api, UserMemoryCache())

    @Test
    fun `getUsers maps every user of the response`() = runTest {
        whenever(api.getUsers()) doReturn listOf(user1Dto, user2Dto)

        val users = repository().getUsers().getOrThrow()

        assertEquals(2, users.size)
        assertEquals(listOf(1, 2), users.map { it.id })
        assertEquals(listOf("Leanne Graham", "Ervin Howell"), users.map { it.name })
        assertEquals(listOf("Bret", "Antonette"), users.map { it.username })
        assertEquals(
            listOf("1-770-736-8031 x56442", "010-692-6593 x09125"),
            users.map { it.phone }
        )
        assertEquals(listOf("Gwenborough", "Wisokyburgh"), users.map { it.address.city })
    }

    @Test
    fun `getUsers replaces the users cached by a previous call`() = runTest {
        whenever(api.getUsers()) doReturn listOf(user1Dto)
        val repository = repository()
        repository.getUsers()

        whenever(api.getUsers()) doReturn listOf(user1Dto, user2Dto)

        val users = repository.getUsers().getOrThrow()

        assertEquals(listOf(1, 2), users.map { it.id })
    }

    @Test
    fun `getUser serves the requested user from the cache`() = runTest {
        whenever(api.getUsers()) doReturn listOf(user1Dto, user2Dto)
        val repository = repository()
        repository.getUsers()

        val user = repository.getUser(1).getOrThrow()

        assertEquals(1, user.id)
        assertEquals("Leanne Graham", user.name)
        assertEquals("Gwenborough", user.address.city)
    }

    @Test
    fun `getUser requests the api when the requested user is not cached`() = runTest {
        whenever(api.getUser(2)) doReturn user2Dto

        val user = repository().getUser(2).getOrThrow()

        assertEquals(2, user.id)
        assertEquals("Ervin Howell", user.name)
        assertEquals("Wisokyburgh", user.address.city)
    }
}
