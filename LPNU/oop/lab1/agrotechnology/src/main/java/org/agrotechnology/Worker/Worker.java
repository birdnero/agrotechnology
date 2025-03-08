package org.agrotechnology.Worker;

import org.agrotechnology.HasReport;
import org.agrotechnology.utils.terminal;

public class Worker extends Person implements HasReport {    
    protected String position;
    protected int expirience;
    protected String duty;
    protected String workPlace;
    protected int salary;

    public Worker(String fullName, Sex sex, String birtday, String position, int expirience, int salary, String duty, String workPlace){
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
        str.append(terminal.formatDataValue("sex", (this.sex == Sex.MALE ? "male": "female")));
        str.append(terminal.formatDataValue("age", this.getAge()));
        str.append(terminal.formatDataValue("position", this.position));
        str.append(terminal.formatDataValue("salary", this.salary));
        str.append(terminal.formatDataValue("expirience", this.expirience));
        str.append(terminal.formatDataValue("duty", this.duty));
        str.append("\n");
        
        return str.toString();
    }
}
