package me.dio.credit.application.system

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(
	info = Info(
		contact = Contact(
			name = "Gabriel Silva Knust",
			email = "gabrielknust@id.uff.br"
		),
		description = "API developed during the backend development course with Kotlin",
		title = "Credit Application System",
		version = "1.0"
	)
)
class CreditApplicationSystemApplication

fun main(args: Array<String>) {
	runApplication<CreditApplicationSystemApplication>(*args)
}
