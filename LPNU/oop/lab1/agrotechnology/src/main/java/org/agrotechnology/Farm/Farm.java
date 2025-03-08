package org.agrotechnology.Farm;

import java.util.ArrayList;

import org.agrotechnology.HasReport;
import org.agrotechnology.WareHouse.WareHouse;
import org.agrotechnology.Worker.Worker;
import org.agrotechnology.utils.terminal;

public abstract class Farm implements HasReport {
    protected int size;
    protected String location;
    protected String name;
    private int id;

    private static int BUDGET = 0;

    // dependence injection
    protected WareHouse wareHouse;
    protected ArrayList<Worker> workers;

    protected Farm(
            String name,
            String location,
            int size,
            WareHouse wareHouse,
            ArrayList<Worker> workers) {

        this.name = name;
        this.location = location;
        this.size = size;
        this.wareHouse = wareHouse;
        this.workers = workers;
        this.id = (int) (Math.random() * 10000);
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

    public static int getBudget() { // !
        return BUDGET;
    }

    @Override
    public String report() {
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName(this.name));
        str.append(terminal.formatDataValue("budget", getBudget()));
        str.append(terminal.formatDataValue("location", this.location));
        str.append(terminal.formatDataValue("size", this.size + " m²"));

        str.append(this.wareHouse.report());

        for (Worker worker : workers) {
            str.append(worker.report());
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

}
