package agro.technology.Farms.AnimaFarm.Barn;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gson.annotations.Expose;

@AllArgsConstructor
@Getter
public class Barn {

    @Expose
    private String type;
    @Expose
    private int size;
    @Expose
    protected int animalsAmount;
    @Expose
    protected int feedAmount;
    @Expose
    protected int productionAmount;

}
