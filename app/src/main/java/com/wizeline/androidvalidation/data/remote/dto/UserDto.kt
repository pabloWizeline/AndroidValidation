package com.wizeline.androidvalidation.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val company: CompanyDto,
    val address: AddressDto
)

data class CompanyDto(
    val name: String,
    @SerializedName("catchPhrase")
    val catchPhrase: String,
    val bs: String
)

data class AddressDto(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String
)
