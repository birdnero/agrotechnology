package agro.technology;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import agro.technology.utils.MainControlerService;
import agro.technology.utils.CLI.Colors;
import agro.technology.utils.CLI;

@SpringBootApplication
public class TechnologyApp implements CommandLineRunner {

	private final CLI terminal;
	private final MainControlerService mainControlerService;

	public TechnologyApp(CLI terminal, MainControlerService mainControlerService) {
		this.terminal = terminal;
		this.mainControlerService = mainControlerService;
	}

	public static void main(String[] args) {
		SpringApplication.run(TechnologyApp.class, args);
	}

	@Override
	public void run(String... args) {
		//щоб при тестуванні не запускалось
		if (Arrays.asList(args).contains("--test")) 
			return;
		

		// інтро
		terminal.previewing("Agrotechnology project", Colors.PINK);

		// Ініціалізація основного меню
		mainControlerService.initMainChoice();

	}

}
