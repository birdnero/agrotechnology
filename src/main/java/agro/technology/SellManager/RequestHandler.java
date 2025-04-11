package agro.technology.SellManager;

import java.util.function.Supplier;

import org.springframework.stereotype.Service;

import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class RequestHandler {

    private final CLI terminal;

    volatile private boolean onHandle = false;
    private Supplier<Boolean> request;

    public RequestHandler(CLI cli) {
        this.request = null;
        this.terminal = cli;
    }

    public RequestHandler doRequest() {
        if (request.get()) {
            requestHook(null);
            hotMessageHook(null);
            onHandle = false;
        }
        return this;
    }

    public boolean isRequest() {
        return request != null;
    }

    public RequestHandler waiting() {
        while (onHandle)
            ;
        return this;
    }

    public RequestHandler requestHook(Supplier<Boolean> request) {
        this.request = request;
        onHandle = true;
        return this;
    }

    public RequestHandler hotMessageHook(String message) {
        if (message != null)
            terminal.topInfoHook("request",
                    () -> terminal.colorize("\n! " + message + " !\n", Colors.BACKGROUND_YELLOW, true));
        else
            terminal.topInfoHook("request", null);
        return this;
    }
}
