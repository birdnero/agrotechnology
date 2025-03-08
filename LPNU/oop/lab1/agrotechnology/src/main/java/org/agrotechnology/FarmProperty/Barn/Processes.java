package org.agrotechnology.FarmProperty.Barn;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Processes {
  public Processes(Barn barn) {
    ScheduledExecutorService timer = Executors.newScheduledThreadPool(2);

    Runnable feed = () -> {
      if (barn.feedAmount > 0) {
        barn.feedAmount = Math.max(0, barn.feedAmount - 1 * barn.animalsAmount);
      }
    };

    Runnable animals = () -> {
      if (barn.feedAmount == 0) {
        barn.animalsAmount = Math.max(0, barn.animalsAmount);
      }
    };

    timer.scheduleAtFixedRate(feed, 5000, 10_000, TimeUnit.MILLISECONDS);
    timer.scheduleAtFixedRate(animals, 5000, 60_000, TimeUnit.MILLISECONDS);

  }

}
