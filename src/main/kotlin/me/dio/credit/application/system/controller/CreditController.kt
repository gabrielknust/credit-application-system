package me.dio.credit.application.system.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.dio.credit.application.system.dto.CreditDto
import me.dio.credit.application.system.dto.CreditView
import me.dio.credit.application.system.dto.CreditViewList
import me.dio.credit.application.system.model.Credit
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("api/credits")
@Tag(name = "Credit")
class CreditController(private val creditService: CreditService,private val customerService: CustomerService) {

    @Operation(
        description = "This is the function to create a new credit.",
        summary = "Create a credit for a customer.",
        responses = [
            ApiResponse(
                description = "Created with success",
                responseCode = "201",
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400"
            )
        ]

    )
    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<CreditView> {
        val credit: Credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CreditView(credit))
    }

    @Operation(
        description = "This is the function to get a list containing all the credits of a customer.",
        summary = "Get all credits from a customer by his id.",
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400"
            )
        ]

    )
    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditViewList>> {
        this.customerService.findById(customerId)
        val creditViewList = this.creditService.findAllByCustomer(customerId).stream()
            .map { credit: Credit -> CreditViewList(credit) }
            .collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(creditViewList)
    }

    @Operation(
        description = "This is the function to get a credit by his id. This function also needs the customer owner of the credit to return the right credit",
        summary = "Get a credit by his id.",
        responses = [
            ApiResponse(
                description = "Success",
                responseCode = "200",
            ),
            ApiResponse(
                description = "Bad Request",
                responseCode = "400"
            )
        ]

    )
    @GetMapping("/{creditId}")
    fun findByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditId: UUID
    ): ResponseEntity<CreditView> {
        val credit = this.creditService.findByCreditCode(customerId, creditId)
        return ResponseEntity.status(HttpStatus.OK).body(CreditView(credit))
    }

}