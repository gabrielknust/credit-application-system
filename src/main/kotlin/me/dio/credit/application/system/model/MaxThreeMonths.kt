package com.example.customvalidator.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MaxThreeMonthsValidator::class])
@MustBeDocumented
annotation class MaxThreeMonths(
    val message: String = "the maximum starting date is 3 months in advance.",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)