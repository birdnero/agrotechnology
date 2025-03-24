package agro.technology;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import agro.technology.utils.MainControlerService;
import agro.technology.utils.terminal;

@SpringBootApplication
public class TechnologyApp implements CommandLineRunner {

	private final terminal terminal;
	private final MainControlerService mainControlerService;

	public TechnologyApp(terminal terminal,	MainControlerService mainControlerService) {
		this.terminal = terminal;
		this.mainControlerService = mainControlerService;
	}

	public static void main(String[] args) {
		SpringApplication.run(TechnologyApp.class, args);
	}

	@Override
	public void run(String... args) {
		// інтро
		terminal.previewing("Agrotechnology project", 0);

		// Ініціалізація основного меню
		mainControlerService.initMainChoice();

	}

}
