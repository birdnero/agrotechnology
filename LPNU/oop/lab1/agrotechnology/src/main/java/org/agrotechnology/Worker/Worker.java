package org.agrotechnology.Worker;

import org.agrotechnology.utils.HasReport;
import org.agrotechnology.utils.terminal;

import com.google.gson.annotations.Expose;

public class Worker extends Person implements HasReport {

    @Expose
    protected String position;
    @Expose
    protected int expirience;
    @Expose
    protected int salary;
    @Expose
    protected String duty;
    @Expose
    protected String workPlace;

    public Worker(String fullName, Sex sex, String birtday, String position, int expirience, int salary, String duty,
            String workPlace) {
        super(fullName, sex, birtday);
        this.position = position;
        this.expirience = expirience;
        this.salary = salary;
        this.duty = duty;
        this.workPlace = workPlace;
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName(this.fullName));
        str.append(terminal.formatDataValue("sex", (this.sex == Sex.MALE ? "male" : "female")));
        str.append(terminal.formatDataValue("age", this.getAge()));
        str.append(terminal.formatDataValue("position", this.position));
        str.append(terminal.formatDataValue("salary", this.salary));
        str.append(terminal.formatDataValue("expirience", this.expirience));
        str.append(terminal.formatDataValue("duty", this.duty));
        str.append("\n");

        return str.toString();
    }

    public String getPosition() {
        return position;
    }

    public int getExpirience() {
        return expirience;
    }

    public String getDuty() {
        return duty;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public int getSalary() {
        return salary;
    }
}
