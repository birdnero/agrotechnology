package org.agrotechnology.FarmAdditional;

import org.agrotechnology.HasReport;
import org.agrotechnology.utils.terminal;

public abstract class Equipment implements HasReport {
    protected int durability;
    private String type;

    public Equipment(String type){
        this.type = type;
    }

    public abstract String useMe();

    @Override
    public String report(){
        StringBuilder str = new StringBuilder();
        str.append(terminal.formatName(type));
        str.append(terminal.formatDataValue("durability", this.durability));
        str.append(reportHook());
        str.append("\n");

        return str.toString();
    };

    protected String reportHook(){
        return "";
    }
}
