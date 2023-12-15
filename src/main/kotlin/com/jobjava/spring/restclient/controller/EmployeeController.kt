package com.jobjava.spring.restclient.controller

import com.jobjava.spring.restclient.service.EmployeeClient
import com.jobjava.spring.restclient.service.EmployeeListResponse
import com.jobjava.spring.restclient.service.EmployeeResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class EmployeeController(
    @Value("\${service.restapiexample.url}") val url: String,
    private val employeeClient: EmployeeClient
) {
    @GetMapping("/employees")
    fun employees(): EmployeeListResponse {
        return employeeClient.getService().getEmployees()
    }

    @GetMapping("/employee/{id}")
    fun employee(@PathVariable id: Int): EmployeeResponse {
        return employeeClient.getService().getEmployee(id)
    }
}
