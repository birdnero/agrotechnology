package agro.technology.SellManager;

import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class RequestHandler {

    private final PauseService pauseService;
    private final CLI terminal;
    private Supplier<Boolean> request;

    public RequestHandler(CLI cli, PauseService pauseService) {
        this.request = null;
        this.terminal = cli;
        this.pauseService = pauseService;
    }

    public void doRequest() {
        if (request.get()) {
            requestHook(null);
            hotMessageHook(null);
            pauseService.resume();
        }
    }

    public boolean isRequest(){
        return request != null;
    }

    public void requestHook(Supplier<Boolean> request) {
        this.request = request;
        pauseService.pause();
    }

    public void hotMessageHook(String message) {
        if (message != null)
            terminal.topInfoHook("request",
                    () -> terminal.colorize("\n! " + message + " !\n", Colors.BACKGROUND_YELLOW, true));
        else
            terminal.topInfoHook("request", null);
    }
}
