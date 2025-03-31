package agro.technology.Farms.Farm;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.annotations.Expose;

import agro.technology.WareHouses.WareHouse.WareHouse;
import agro.technology.Worker.Worker;
import agro.technology.utils.HasReport;
import agro.technology.utils.CLI;
import lombok.Getter;
import lombok.Setter;

/**
 * основний абстрактний клас, у нього збираються всі деталі і вже ними звідси можна оперувати
 */
@Getter
@Setter
public abstract class Farm implements HasReport {
    // #lab використано інкапсуляцію (її використано практично всюди)
    private CLI terminal;

    @Expose
    private String type;
    @Expose
    protected String location;
    @Expose
    protected String name;
    @Expose
    private String specializationName;
    @Expose
    protected WareHouse wareHouse;
    @Expose
    protected ArrayList<Worker> workers;
    

    protected Farm(
            String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers,
            String specializationName) {

        this.type = type;
        this.name = name;
        this.location = location;
        this.wareHouse = wareHouse;
        this.workers = workers;
        this.specializationName = specializationName;
        initProcess();
    }

    @Autowired
    public Farm(CLI terminal){
        this.terminal = terminal;
    }

    public void initProcess() {
        wareHouse.process();
        initProcessHook();
    }

    abstract protected void initProcessHook();



    @Override
    public String report() {
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName("Farm \"" + this.name + "\""));
        str.append(terminal.formatDataValue("location", this.location));

        str.append(this.wareHouse.report());
        if (!workers.isEmpty()) {
            str.append(terminal.formatName("WORKERS"));

            for (Worker worker : workers) {
                str.append(worker.report());
            }
        }
        str.append(useReportHook());
        str.append("\n");

        return str.toString();
    }

    protected String useReportHook() {
        return "";
    }
}
