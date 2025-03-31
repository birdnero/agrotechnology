package agro.technology;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import agro.technology.utils.MainControlerService;
import agro.technology.utils.CLI.Colors;
import agro.technology.Farms.FarmService;
import agro.technology.utils.CLI;

@SpringBootApplication
public class TechnologyApp implements CommandLineRunner {

    private final FarmService farmService;

	private final CLI terminal;
	private final MainControlerService mainControlerService;

	public TechnologyApp(CLI terminal, MainControlerService mainControlerService, FarmService farmService) {
		this.terminal = terminal;
		this.mainControlerService = mainControlerService;
		this.farmService = farmService;
	}

	public static void main(String[] args) {
		SpringApplication.run(TechnologyApp.class, args);
	}

	@Override
	public void run(String... args) {
		//щоб при тестуванні не запускалось

		farmService.load();

		if (Arrays.asList(args).contains("--test")) 
		
			return;
		

		// інтро
		terminal.previewing("Agrotechnology project", Colors.PINK);

		// Ініціалізація основного меню
		mainControlerService.initMainChoice();

	}

}
