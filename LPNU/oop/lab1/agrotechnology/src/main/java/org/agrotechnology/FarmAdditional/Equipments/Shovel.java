package org.agrotechnology.FarmAdditional.Equipments;

import org.agrotechnology.FarmAdditional.Equipment;

public class Shovel extends Equipment {

    public Shovel() {
        super("shovel");
        this.durability = 1000;
    }

    @Override
    public String useMe() {
        if (this.durability >= 20) {
            this.durability -= 20;
            return "digging, digging, digging...";
        } else {
            this.durability = 0;
            return "diggi... oopps shovel brokes...";
        }
    }
}
