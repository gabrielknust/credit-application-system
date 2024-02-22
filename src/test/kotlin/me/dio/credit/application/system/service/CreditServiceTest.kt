package me.dio.credit.application.system.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import me.dio.credit.application.system.exception.BusinessException
import me.dio.credit.application.system.model.Credit
import me.dio.credit.application.system.model.Customer
import me.dio.credit.application.system.model.Status
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        creditService = CreditService(creditRepository, customerService)
    }

    @Test
    fun `should create credit`() {
        //given
        val fakeCredit: Credit = buildCredit()
        val fakeId: Long = 1L
        every { customerService.findById(any()) } returns fakeCredit.customer!!
        every { creditRepository.save(any()) } returns fakeCredit
        //when
        val actual = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { customerService.findById(fakeId) }
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should find all credits by customer`(){
        //given
        val fakeListOfCredit: List<Credit> = listOf(buildCredit())
        val fakeId: Long = 1L
        every { creditRepository.findAllByCustomerId(fakeId) } returns fakeListOfCredit
        //when
        val actual: List<Credit> = creditService.findAllByCustomer(fakeId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).isSameAs(fakeListOfCredit)
        verify(exactly = 1) { creditRepository.findAllByCustomerId(fakeId) }
    }

    @Test
    fun `should find a credit for the code`(){
        //given
        val fakeId: Long = 1L
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(customer = Customer(id = fakeId))
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //when
        val actual: Credit = creditService.findByCreditCode(fakeId,fakeCreditCode)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should throw BusinessException for invalid credit code`(){
        //given
        val fakeId: Long = 1L
        val fakeCreditCode: UUID = UUID.randomUUID()
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns null
        //when
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(fakeId,fakeCreditCode) }
            .isInstanceOf(BusinessException::class.java)
            .hasMessage("Creditcode $fakeCreditCode not found")
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `should throw IllegalArgumentException for invalid customer id`(){
        //given
        val fakeId: Long = 1L
        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(customer = Customer(id = 2L))
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit
        //when
        //then
        Assertions.assertThatThrownBy { creditService.findByCreditCode(fakeId,fakeCreditCode) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("You don't have permission to acess this credit. Please contact the admins.")
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }


    companion object {
        fun buildCredit(
            creditValue: BigDecimal = BigDecimal(10000),
            dayFirstInstallment: LocalDate = LocalDate.now(),
            numberOfInstallments: Int = 12,
            status: Status = Status.IN_PROGRESS,
            customer: Customer? = CustomerServiceTest.buildCustomer(),
            id: Long? = 1L
        ) = Credit(
            creditValue = creditValue,
            dayFirstInstallment = dayFirstInstallment,
            numberOfInstallments = numberOfInstallments,
            status = status,
            customer = customer,
            id = id
        )
    }
}