package com.example.customvalidator.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [MaxThreeMonthsValidator::class])
@MustBeDocumented
annotation class MaxMonths(
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
    val value: Int,
    val message: String = "The starting date is wrong, please verify if the date is in the max range.",
)