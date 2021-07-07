package com.workmotion;

import com.workmotion.boundary.EmployeesBoundary;
import com.workmotion.control.EmployeesRepository;
import com.workmotion.entitiy.Employee;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@SpringBootTest
class EmployeesIntegrationTest {
    private static final Properties kafkaProps;

    static {
        kafkaProps = new Properties();
        try (InputStream is = EmployeesIntegrationTest.class.getResourceAsStream("/kafka_test.properties")) {
            kafkaProps.load(is);
            kafkaProps.put("group.id", "TEST_G_ID");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static final String EMP_NAME = "Some sample name";
    @Autowired
    EmployeesBoundary boundary;

    @Autowired
    EmployeesRepository employeesRepository;

    @Test
    void givenAnEmpEntity_whenSendingIt_shouldBeConsumedUsingBroker() {

        Employee employee = new Employee();
        employee.setDateOfBirth(LocalDate.of(1990, 4, 1));
        employee.setName(EMP_NAME);
        employee.setSalary(BigDecimal.valueOf(5000));
        String empCode = boundary.createEmployee(employee);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaProps,
                new StringDeserializer(), new StringDeserializer());
        consumer.subscribe(Set.of("createdEmployee"));
        ConsumerRecords<String, String> records;
        List<String> events = new ArrayList<>();
        try {
            // consumer.seekToEnd(new TopicPartition(serviceEvent.name(),));
            records = consumer.poll(Duration.ofSeconds(30));
            records.forEach(r -> events.add(r.value()));
            consumer.commitSync();
        } finally {
            consumer.close();
        }
        Assertions.assertTrue(events.stream()
                                    .anyMatch(empStr -> empStr.contains(empCode)), "Employee should be created!");
    }


}
