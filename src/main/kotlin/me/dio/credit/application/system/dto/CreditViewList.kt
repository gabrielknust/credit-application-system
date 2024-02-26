package me.dio.credit.application.system.dto

import me.dio.credit.application.system.model.Credit
import java.math.BigDecimal
import java.util.*

data class CreditViewList(
    var creditCode: UUID,
    var creditValue: BigDecimal,
    var numberOfInstallments: Int
) {
    constructor(credit: Credit): this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberOfInstallments = credit.numberOfInstallments
    )
}
