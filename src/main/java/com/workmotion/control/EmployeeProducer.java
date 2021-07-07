package com.workmotion.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workmotion.entitiy.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Logger;

@Service
public class EmployeeProducer {

    private static final Logger LOGGER = Logger.getLogger(EmployeeProducer.class.getName());
    private static final String CREATE_TOPIC = "createdEmployee";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public void produceEmployee(Employee employee) {
        try {
            String payload = objectMapper.writeValueAsString(employee);
            LOGGER.info(() -> "#### -> Creating Employee -> " + payload);
            this.kafkaTemplate.send(CREATE_TOPIC, payload);
        } catch (JsonProcessingException e) {
            LOGGER.severe(e::getMessage);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Data is not processable", e);
        }
    }
}
