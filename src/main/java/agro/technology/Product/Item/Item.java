package agro.technology.Product.Item;


import com.google.gson.annotations.Expose;

import lombok.Getter;

@Getter
public class Item {
   @Expose
   private final String name;
   @Expose
    protected Integer price;

    public Item(String name, Integer price){
        this.name = name;
        this.price = price;
    }
}
