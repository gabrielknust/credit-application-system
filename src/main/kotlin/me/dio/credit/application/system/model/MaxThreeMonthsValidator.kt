package com.example.customvalidator.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.LocalDate

class MaxThreeMonthsValidator : ConstraintValidator<MaxThreeMonths, LocalDate> {
    override fun isValid(value: LocalDate, context: ConstraintValidatorContext?): Boolean {
        return (value.isBefore(LocalDate.now().plusMonths(3)) || value.isEqual(LocalDate.now().plusMonths(3)))
    }
}

