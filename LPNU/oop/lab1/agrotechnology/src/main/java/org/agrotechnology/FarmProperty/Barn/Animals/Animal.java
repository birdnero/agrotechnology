package org.agrotechnology.FarmProperty.Barn.Animals;

public interface Animal {

    public static Animal defineType(String typeName){
        if(typeName.equals(Cow.class.getSimpleName())){
            return new Cow();
        } 
        if(typeName.equals(Sheep.class.getSimpleName())){
            return new Sheep();
        }
        if(typeName.equals(Pig.class.getSimpleName())){
            return new Pig();
        }
        if(typeName.equals(Chiken.class.getSimpleName())){
            return new Chiken();
        }

        return null;
    }

    public static String[] getTypes(){
        return new String[]{
            Cow.class.getSimpleName(),
            Sheep.class.getSimpleName(),
            Pig.class.getSimpleName(),
            Chiken.class.getSimpleName(),
        };
    } 
    
    public int getPrice();
    public String getName();

    public String[] getProducts();

    public String[] canEat();
}
 