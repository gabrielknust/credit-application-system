package me.dio.credit.application.system.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
data class Credit(
    @Column(nullable = false, unique = true) var creditCode: UUID = UUID.randomUUID(),
    @Column(nullable = false) var creditValue: BigDecimal = BigDecimal.ZERO,
    @Column(nullable = false) var dayFirstInstallment: LocalDate,
    @Column(nullable = false) var numberOfInstallments: Int = 0,
    @Enumerated var status: Status = Status.IN_PROGRESS,
    @ManyToOne var customer: Customer? = null,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

