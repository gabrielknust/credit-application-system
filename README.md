﻿# Credit Application System
Esse projeto foi desenvolvido durante o bootcamp "Desenvolvimento backend com kotlin" da DIO (https://www.dio.me/bootcamp/desenvolvimento-backend-com-kotlin) e consiste em uma API desenvolvida em Kotlin utilizando Spring Boot 3.

A API conta com duas entidades: Customer e Credit. Na entidade Customer foram desenvolvidas as funcionalidades de create, delete, get e update. Já na entidade Credit foram desenvolvidas as funcionalidades de create e duas funcionalidades de get. Uma funcionalidade get retorna uma lista contendo todos os credits de um customer e outra retorna um credit específico a partir do seu id e do id do customer que o representa.

Tambem foram desenvolvidos testes nas classes de Service e Controller de ambas as entidades e na classe Repository da entidade Credit.

O banco de dados utilizado foi o H2.

Para utilizar a API basta subir a aplicação dando start na classe CreditApplicationSystemApplication.kt e acessar http://localhost:8080/swagger-ui/index.html para a documentação e http://localhost:8080/h2-console para o painel do banco de dados com username: admin e sem password.
