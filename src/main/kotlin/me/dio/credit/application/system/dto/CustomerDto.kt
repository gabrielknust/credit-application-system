package me.dio.credit.application.system.dto

import me.dio.credit.application.system.model.Address
import me.dio.credit.application.system.model.Customer
import java.math.BigDecimal

data class CustomerDto(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val password: String,
    val zipcode: String,
    val street: String,
    val email: String
) {
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        password = this.password,
        income = this.income,
        address = Address(zipCode = this.zipcode, street = this.street),
        email = this.email
    )
}
