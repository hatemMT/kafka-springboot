package com.workmotion.boundary;

import com.workmotion.control.EmployeeProducer;
import com.workmotion.control.EmployeesRepository;
import com.workmotion.entitiy.Employee;
import com.workmotion.entitiy.Status;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/employees")
@OpenAPIDefinition(
        info = @Info(title = "Tech Challenge", description = "Endpoints for handling the creating of employees")
)
public class EmployeesBoundary {

    private EmployeesRepository repository;
    private EmployeeProducer empProducer;

    public EmployeesBoundary(EmployeesRepository repository, EmployeeProducer empProducer) {
        this.repository = repository;
        this.empProducer = empProducer;
    }

    @Operation(description = "Fetch all the employees with their current state")
    @GetMapping(produces = "application/json")
    public List<Employee> getAll() {
        return repository.getList();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public String createEmployee(@RequestBody Employee employee) {
        String generatedCode = UUID.randomUUID().toString();
        employee.setCode(generatedCode);
        employee.setStatus(Status.ADDED);
        empProducer.produceEmployee(employee);
        return generatedCode;
    }

    @PostMapping(path = "{code}", consumes = "application/json", produces = "application/json")
    public void updateStatus(@PathVariable("code") String code, @RequestBody Status status) {
        repository.updateStatus(code, status);
    }
}
