package org.agrotechnology.FarmAdditional.Equipments;

import org.agrotechnology.FarmAdditional.Equipment;

public class Hoe extends Equipment {

    public Hoe(){
        super("hoe");
        this.durability = 500;
    }

    @Override
    public String useMe(){
        if (this.durability >= 2) {
            this.durability -= 2;
            return "do work...";
        } else {
            this.durability = 0;
            return "do w... oopps hoe brokes...";
        }
    }
}
