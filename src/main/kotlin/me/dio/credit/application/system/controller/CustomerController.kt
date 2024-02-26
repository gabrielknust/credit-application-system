package me.dio.credit.application.system.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.dio.credit.application.system.dto.CustomerDto
import me.dio.credit.application.system.dto.CustomerUpdateDto
import me.dio.credit.application.system.dto.CustomerView
import me.dio.credit.application.system.model.Customer
import me.dio.credit.application.system.service.impl.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/customers")
@Tag(name = "Customer")
class CustomerController(private val customerService: CustomerService) {

    @Operation(
        description = "This is the function to create a new customer.",
        summary = "Create a new customer.",
        responses = [
            ApiResponse(
                description = "Created with sucess",
                responseCode = "201",
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400"
            ),
            ApiResponse(
                description = "Conflict",
                responseCode = "409"
            )
        ]

    )
    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<CustomerView> {
        val savedCustomer = this.customerService.save(customerDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerView(savedCustomer))
    }

    @Operation(
        description = "This is the function to get a customer by his id.",
        summary = "Get a customer by his id.",
        responses = [
            ApiResponse(
                description = "Sucess",
                responseCode = "200",
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400"
            )
        ]

    )
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(CustomerView(customer))
    }

    @Operation(
        description = "This is the function to delete a customer by his id.",
        summary = "Delete a customer by his id.",
        responses = [
            ApiResponse(
                description = "No content",
                responseCode = "204",
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400"
            )
        ]

    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Long) = this.customerService.delete(id)

    @Operation(
        description = "This is the function to update a customer.",
        summary = "Update a customer.",
        responses = [
            ApiResponse(
                description = "Sucess",
                responseCode = "200",
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400"
            )
        ]

    )
    @PatchMapping()
    fun updateCustomer(
        @RequestParam(value = "customerId") id: Long,
        @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<CustomerView> {
        val customer = this.customerService.findById(id)
        val customerToUpdate = customerUpdateDto.toEntity(customer)
        val customerUpdated = this.customerService.save(customerToUpdate)
        return ResponseEntity.status(HttpStatus.OK).body(CustomerView(customerUpdated))
    }
}