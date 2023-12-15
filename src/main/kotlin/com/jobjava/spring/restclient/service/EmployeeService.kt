package com.jobjava.spring.restclient.service

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange("\${service.restapiexample.url}")
interface EmployeeService {
    @GetExchange("/api/v1/employees")
    fun getEmployees(): EmployeeListResponse

    @GetExchange("/api/v1/employee/{id}")
    fun getEmployee(@PathVariable("id") id: Int): EmployeeResponse
}

data class EmployeeListResponse(
    val status: String,
    val data: List<Employee>,
    val message: String
)

data class EmployeeResponse(
    val status: String,
    val data: Employee,
    val message: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Employee(
    val id: Int,
    val employeeName: String,
    val employeeSalary: Int,
    val employeeAge: Int,
    val profileImage: String
)
