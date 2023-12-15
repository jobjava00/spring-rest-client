package com.jobjava.spring.restclient.service

import org.springframework.stereotype.Component
import org.springframework.web.service.invoker.HttpServiceProxyFactory


@Component
class EmployeeClient(
    httpServiceProxyFactory: HttpServiceProxyFactory
) {
    private var employeeService: EmployeeService = httpServiceProxyFactory.createClient(EmployeeService::class.java)

    fun getService(): EmployeeService = employeeService
}
