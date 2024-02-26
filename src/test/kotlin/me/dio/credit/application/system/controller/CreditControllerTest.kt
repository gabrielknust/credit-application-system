package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.dio.credit.application.system.dto.CreditDto
import me.dio.credit.application.system.dto.CustomerDto
import me.dio.credit.application.system.model.Credit
import me.dio.credit.application.system.model.Customer
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditControllerTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @AfterEach

    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun `should create a credit and return 201 status`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val creditDto:CreditDto = builderCreditDto(id = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(creditDto.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(creditDto.numberOfInstallments))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customer.income))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not create credit by invalid customer id and return 400 status and BusinessException`() {
        //given
        customerRepository.save(builderCustomerDto().toEntity())
        val fakeId: Long = Random().nextLong()
        val creditDto:CreditDto = builderCreditDto(id = fakeId)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").value("Id $fakeId not found"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not create credit with a low number of installments and return 400 status and MethodArgumentNotValidException`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val creditDto:CreditDto = builderCreditDto(numberOfInstallments = 0, id = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").value("The minimum number of installments is 1"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not create credit with a high number of installments and return 400 status and MethodArgumentNotValidException`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val creditDto:CreditDto = builderCreditDto(numberOfInstallments = 100, id = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").value("The maximum number of installments is 48"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not create credit with a past day on datFirstInstallment and return 400 status and MethodArgumentNotValidException`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val creditDto:CreditDto = builderCreditDto(dayFirstInstallment = LocalDate.now().minusMonths(1) , id = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").value("The date of first Installment must be on present or future"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not create credit with a future day on datFirstInstallment and return 400 status and MethodArgumentNotValidException`() {
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val creditDto:CreditDto = builderCreditDto(dayFirstInstallment = LocalDate.now().plusYears(1) , id = customer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.web.bind.MethodArgumentNotValidException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").value("The date of first Installment must be a maximum of 3 months from the start date"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find all credits by a costumer id and return 200 status`(){
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit1: Credit = creditRepository.save(builderCreditDto(id = customer.id!!).toEntity())
        val credit2: Credit = creditRepository.save(builderCreditDto(id = customer.id!!).toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.*").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value(credit1.creditValue.toInt()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].creditValue").value(credit2.creditValue.toInt()))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].creditCode").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfInstallments").value(credit1.numberOfInstallments))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].numberOfInstallments").value(credit2.numberOfInstallments))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find credits by a invalid costumer id and return 400 status with empty body`(){
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val fakeId: Long = Random().nextLong()
        creditRepository.save(builderCreditDto(id = customer.id!!).toEntity())
        creditRepository.save(builderCreditDto(id = customer.id!!).toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=$fakeId")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class me.dio.credit.application.system.exception.BusinessException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").value("Id $fakeId not found"))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find no credits for a costumer and return 200 status with a empty body`(){
        //given
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find a credit by its credit code and customer id and return 200 status`(){
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit: Credit = creditRepository.save(builderCreditDto(id = customer.id!!).toEntity())
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=${customer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(credit.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditCode").value(credit.creditCode.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfInstallments").value(credit.numberOfInstallments))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(credit.status.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.emailCustomer").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.incomeCustomer").value(customer.income))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find a credit by its credit code but with a invalid customer id and return 400 status with IllegalArgumentException `(){
        val customer: Customer = customerRepository.save(builderCustomerDto().toEntity())
        val credit: Credit = creditRepository.save(builderCreditDto(id = customer.id!!).toEntity())
        val fakeid: Long = Random().nextLong()
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${credit.creditCode}?customerId=$fakeid")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class java.lang.IllegalArgumentException")
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").value("You don't have permission to access this credit. Please contact the admins."))
            .andDo(MockMvcResultHandlers.print())
    }

    fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal(10000),
        dayFirstInstallment: LocalDate = LocalDate.now().plusMonths(2),
        numberOfInstallments: Int = 12,
        id:Long
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = id
    )

    fun builderCustomerDto(
        firstName: String = "Gabriel",
        lastName: String = "Knust",
        cpf: String = "28475934625",
        email: String = "gabriel@email.com",
        income: BigDecimal = BigDecimal.valueOf(1000),
        password: String = "1234",
        zipCode: String = "000000",
        street: String = "Rua José, 123",
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        income = income,
        password = password,
        zipCode = zipCode,
        street = street
    )
}