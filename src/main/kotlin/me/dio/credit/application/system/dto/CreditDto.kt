package me.dio.credit.application.system.dto

import me.dio.credit.application.system.model.MaxMonths
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import me.dio.credit.application.system.model.Credit
import me.dio.credit.application.system.model.Customer
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Invalid credit value") val creditValue: BigDecimal,
    @field:FutureOrPresent(message = "The date of first Installment must be on present or future") @field:MaxMonths(
        value = 3,
        message = "The date of first Installment must be a maximum of 3 months from the start date"
    ) val dayFirstInstallment: LocalDate,
    @field:Min(value = 1, message = "The minimum number of installments is 1") @field:Max(
        value = 48,
        message = "The maximum number of installments is 48"
    ) val numberOfInstallments: Int,
    @field:NotNull(message = "Invalid customer ID") val customerId: Long
) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
