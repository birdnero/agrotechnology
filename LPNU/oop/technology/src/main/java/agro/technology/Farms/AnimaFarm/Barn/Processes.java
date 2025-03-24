package agro.technology.Farms.AnimaFarm.Barn;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import agro.technology.Farms.AnimaFarm.Barn.Animals.AnimalService;


public class Processes {

  private AnimalService animalService;

  @Autowired
  public void setAnimalService(AnimalService animalService) {
    this.animalService = animalService;
  }
 

  public Processes(Barn barn) {
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

    Runnable feed = () -> {
      if (barn.feedAmount > 0) {
        barn.feedAmount = Math.max(0, barn.feedAmount - 1 * barn.animalsAmount);
      }
    };

    Runnable animals = () -> {
      if (barn.feedAmount == 0) {
        barn.animalsAmount = Math.max(0, barn.animalsAmount - 1);
      }
      double chance = Math.random() + animalService.getAnimal(barn.getType()).getLife();
      if (chance < 0.3 + 0.7 * animalService.getAnimal(barn.getType()).getLife()) {
        barn.animalsAmount = Math.max(0, barn.animalsAmount - 1);
      }
      if (barn.productionAmount + barn.animalsAmount <= barn.animalsAmount * 5 && barn.feedAmount > 0) {
        barn.productionAmount += barn.animalsAmount;
      }
    };

    timer.scheduleAtFixedRate(feed, 50000, 100_000, TimeUnit.MILLISECONDS);
    timer.scheduleAtFixedRate(animals, 50000, 120_000, TimeUnit.MILLISECONDS);

  }

}
