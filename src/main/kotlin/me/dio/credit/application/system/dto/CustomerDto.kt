package me.dio.credit.application.system.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.model.Address
import me.dio.credit.application.system.model.Customer
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "Empty first name") val firstName: String,
    @field:NotEmpty(message = "Empty last name") val lastName: String,
    @field:NotEmpty(message = "Empty CPF")
    @field:CPF(message = "Invalid CPF") val cpf: String,
    @field:NotNull(message = "Invalid income") val income: BigDecimal,
    @field:Email(message = "Invalid email")
    @field:NotEmpty(message = "Empty email") val email: String,
    @field:NotEmpty(message = "Empty password") val password: String,
    @field:NotEmpty(message = "Empty zip code") val zipCode: String,
    @field:NotEmpty(message = "Empty street") val street: String
) {
    fun toEntity(): Customer = Customer(
        firstName = this.firstName,
        lastName = this.lastName,
        cpf = this.cpf,
        password = this.password,
        income = this.income,
        address = Address(zipCode = this.zipCode, street = this.street),
        email = this.email
    )
}
