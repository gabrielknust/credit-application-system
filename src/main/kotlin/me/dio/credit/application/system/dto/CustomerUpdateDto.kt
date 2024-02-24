package me.dio.credit.application.system.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.model.Customer
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "Empty first name") val firstName: String,
    @field:NotEmpty(message = "Empty last name") val lastName: String,
    @field:NotNull(message = "Invalid income") val income: BigDecimal,
    @field:NotEmpty(message = "Empty zip code") val zipCode: String,
    @field:NotEmpty(message = "Empty street") val street: String
){
    fun toEntity(customer: Customer) : Customer{
        customer.firstName = this.firstName
        customer.lastName= this.lastName
        customer.income = this.income
        customer.address.zipCode = this.zipCode
        customer.address.street = this.street
        return customer
    }
}


