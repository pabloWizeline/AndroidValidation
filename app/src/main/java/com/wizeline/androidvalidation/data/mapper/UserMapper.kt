package com.wizeline.androidvalidation.data.mapper

import com.wizeline.androidvalidation.data.remote.dto.AddressDto
import com.wizeline.androidvalidation.data.remote.dto.CompanyDto
import com.wizeline.androidvalidation.data.remote.dto.UserDto
import com.wizeline.androidvalidation.domain.model.Address
import com.wizeline.androidvalidation.domain.model.Company
import com.wizeline.androidvalidation.domain.model.User

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    username = username,
    email = company.name,
    phone = phone,
    website = website,
    company = Company(email, company.catchPhrase, company.bs),
    address = address.toDomain()
)

fun List<UserDto>.toDomain(): List<User> = map { it.toDomain() }

fun CompanyDto.toDomain(): Company = Company(
    name = name,
    catchPhrase = catchPhrase,
    bs = bs
)

fun AddressDto.toDomain(): Address = Address(
    street = street,
    suite = suite,
    city = city,
    zipcode = zipcode
)
