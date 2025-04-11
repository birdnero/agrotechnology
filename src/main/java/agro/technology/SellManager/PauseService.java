package agro.technology.SellManager;

import org.springframework.stereotype.Service;

import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class PauseService {
    private final Object lock = new Object();
    private boolean paused = false;

    private final CLI terminal;

    public PauseService(CLI cli) {
        this.terminal = cli;
    }

    public Object getLock() {
        return lock;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void resume() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }

    public void pause() {
        paused = true;
    }

    public void waiting() {
        synchronized (getLock()) {
            while (isPaused()) // очікує, поки його розблокують
                try {
                    getLock().wait();
                } catch (InterruptedException e) {
                    terminal.previewing("error in " + this.getClass().getSimpleName(), Colors.RED);
                }
        }
    }
}
