package org.agrotechnology.Farm;

import java.util.ArrayList;

import org.agrotechnology.HasReport;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.Worker.Worker;
import org.agrotechnology.utils.terminal;

import com.google.gson.annotations.Expose;

public abstract class Farm implements HasReport {
    @Expose
    private String type;
    @Expose
    protected String location;
    @Expose
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    private static int BUDGET = 1000; // ! організуваи в окремий файл

    // dependence injection
    @Expose
    protected WareHouse wareHouse;
    @Expose
    protected ArrayList<Worker> workers;

    protected Farm(
            String type,
            String location,
            String name,
            WareHouse wareHouse,
            ArrayList<Worker> workers) {

        this.type = type;
        this.name = name;
        this.location = location;
        this.wareHouse = wareHouse;
        this.workers = workers;
    }

    /**
     * 
     * @param value - сума яка додастся або відніметься
     * @return - повертає оновлений бюджет або -1 якщо грошей недостатньо
     */
    public static int updateBudget(int value) {
        if (BUDGET + value > 0) {
            BUDGET += value;
            return BUDGET;
        }
        return -1;
    }

    public static int getBudget() { 
        return BUDGET;
    }

    public void initProcess(){
        wareHouse.process();
        initProcessHook();
    }

    abstract protected void  initProcessHook();

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName("Farm \"" + this.name + "\""));
        str.append(terminal.formatDataValue("budget", getBudget()));
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

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public static int getBUDGET() {
        return BUDGET;
    }

    public WareHouse getWareHouse() {
        return wareHouse;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public String getType() {
        return type;
    }

}
