package org.agrotechnology.Worker;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import com.google.gson.annotations.Expose;

public abstract class Person {
    enum Sex {
        MALE,
        FEMALE
    };

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int age;

    @Expose
    private String birthDay;
    @Expose
    protected Sex sex;
    @Expose
    protected String fullName;

    public Person(String fullName, Sex sex, String birthDay) {
        this.birthDay = birthDay;
        this.age = Period.between(LocalDate.parse(birthDay, FORMAT), LocalDate.now()).getYears();
        this.sex = sex;
        this.fullName = fullName;
    }

    protected int getAge() {
        return age;
    }

    public String getBirthDay() {
        return this.birthDay;
    }

    public Sex getSex() {
        return sex;
    }

    public String getFullName() {
        return fullName;
    }
}
