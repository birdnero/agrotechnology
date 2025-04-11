package agro.technology.Product;


import com.google.gson.annotations.Expose;

import agro.technology.Product.Item.Item;
import lombok.Getter;

@Getter
// по суті своїй адаптер) виконує функцію поєднання двох напів сумісних елементів
public class Product extends Item {
    @Expose
    protected Integer amount;

    protected Product(String name, Integer amount, Integer price) {
        super(name, price);
        this.amount = amount;
    }
}
