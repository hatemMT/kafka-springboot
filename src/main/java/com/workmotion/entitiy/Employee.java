package com.workmotion.entitiy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
@ToString
public class Employee {
    private String code;
    @NotEmpty
    private String name;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private BigDecimal salary;
    private Status status;
}
