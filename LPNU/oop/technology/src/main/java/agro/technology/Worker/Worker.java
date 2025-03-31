package agro.technology.Worker;

import lombok.Getter;

import com.google.gson.annotations.Expose;

@Getter
public class Worker extends Person  {

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

    
}
