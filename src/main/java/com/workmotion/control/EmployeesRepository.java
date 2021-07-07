package com.workmotion.control;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmotion.entitiy.Employee;
import com.workmotion.entitiy.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(SCOPE_SINGLETON)
public class EmployeesRepository {
    public static final Logger LOGGER = Logger.getLogger(EmployeesRepository.class.getName());
    private Map<String, Employee> database = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper;

    @Autowired
    public EmployeesRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(id = "create-id", topics = "createdEmployee",
                   groupId = "#{T(java.util.UUID).randomUUID().toString()}")
    public void listen(String jsonPayload) {
        LOGGER.log(Level.FINEST, jsonPayload);
        try {
            Employee employee = objectMapper.reader().readValue(jsonPayload, Employee.class);
            database.put(employee.getCode(), employee);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e::getMessage);
        }
    }

    public List<Employee> getList() {
        return new ArrayList<>(database.values());
    }

    public void updateStatus(String code, Status status) {
        database.get(code).setStatus(status);
    }
}
