package me.dio.credit.application.system.model

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import me.dio.credit.application.system.model.MaxMonths
import java.time.LocalDate
import kotlin.properties.Delegates

class MaxThreeMonthsValidator : ConstraintValidator<MaxMonths, LocalDate> {

    private var months by Delegates.notNull<Int>()

    override fun initialize(constraintAnnotation: MaxMonths?) {
        months = constraintAnnotation!!.value
        super.initialize(constraintAnnotation)
    }
    override fun isValid(value: LocalDate, context: ConstraintValidatorContext?): Boolean {
        return (value.isBefore(LocalDate.now().plusMonths(3)) || value.isEqual(LocalDate.now().plusMonths(3)))
    }
}

