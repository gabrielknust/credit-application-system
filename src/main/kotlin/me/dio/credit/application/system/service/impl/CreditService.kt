package me.dio.credit.application.system.service.impl

import me.dio.credit.application.system.model.Credit
import me.dio.credit.application.system.repository.CredityRepository
import me.dio.credit.application.system.service.ICreditService
import java.util.*

class CreditService(private val credityRepository: CredityRepository, private val customerService: CustomerService) :
    ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.credityRepository.save(credit)
    }

    override fun findAllByCustomer(customerId: Long): List<Credit> = this.credityRepository.findAllByCustomerId(customerId)

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = (this.credityRepository.findByCreditCode(creditCode)
            ?: throw RuntimeException("Creditcode $creditCode not found"))
        return if(credit.customer?.id == customerId) credit else throw RuntimeException("Contact admin")
    }

}