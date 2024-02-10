package me.dio.credit.application.system.repository

import me.dio.credit.application.system.model.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface CredityRepository: JpaRepository<Credit,Long> {
    fun findByCreditCode(creditCode: UUID): Credit
    @Query(value = "SELECT * FROM CREDIT WHERE CUSTOMER_ID = ?1", nativeQuery = true)
    fun findAllByCustomerId(customerId:Long): List<Credit>
}