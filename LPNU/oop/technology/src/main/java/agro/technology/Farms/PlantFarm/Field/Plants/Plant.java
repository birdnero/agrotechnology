package agro.technology.Farms.PlantFarm.Field.Plants;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class Plant {
    @Expose
    private final String name;
    @Expose
    private final int buyPrice;
    @Expose
    private final int selPrice;
    @Expose
    private final double dabler;
}
