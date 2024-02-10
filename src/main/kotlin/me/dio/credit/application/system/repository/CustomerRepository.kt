package me.dio.credit.application.system.repository

import me.dio.credit.application.system.model.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository: JpaRepository<Customer,Long>