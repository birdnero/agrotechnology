package org.agrotechnology.Worker;

import java.time.LocalDate;
import java.time.Period;

public abstract class Person {
    enum Sex{
        MALE,
        FEMALE
    };

    private String birthDay;
    private int age = (birthDay == null ? Period.between(LocalDate.parse(birthDay), LocalDate.now()).getYears() : 0);
    protected Sex sex;
    protected String fullName;

    
    public Person(String fullName,Sex sex, String birthDay) {
        this.birthDay = birthDay;
        this.sex = sex;
        this.fullName = fullName;
    }

    protected int getAge(){
        return age;
    }
}
