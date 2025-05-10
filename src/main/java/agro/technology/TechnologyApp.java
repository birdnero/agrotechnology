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
		// щоб при тестуванні не запускалось

		farmService.load();

		if (Arrays.asList(args).contains("--test"))

			return;

		// інтро
		terminal.previewing("Agrotechnology project", Colors.PINK);

		// Ініціалізація основного меню
		mainControlerService.initMainChoice();

	}

}
/**
 * ? патерни та їх реалізація в коді:
 *
 * ? Породжувальні патерни (Creational Patterns):
 * 
 * * Абстрактна фабрика (Abstract Factory) - виклик фабрики в залежності від вхідних даних
 * FarmService.createFarm() | WareHouseService.create()
 * 
 * *Будівельник (Builder) мати конструктор і готові конфігурації
 * AnimalService | PlantService | WorkerFactory
 * 
 * !Фабричний метод (Factory Method) мати фігню що створює потрібний клас
 * !Прототип (Prototype) - створення копії
 * !Одинак (Singleton) - аля static class
 * використовую, але не я безпосередньо а Spring boot in IoC 
 *
 * 
 * ? Структурні патерни (Structural Patterns):
 * * Адаптер (Adapter) - проміжна фігня сумісності різних штук
 * Product | ProductService
 * 
 * !Міст (Bridge) - взаємодія interface<->interface
 * 
 * * Компоновщик (Composite) - дерево (делегування спуск дії до низу залежності) (вертикально)
 * методи .report() & .load() але немає спільного інтерфейсу з
 * причини того, що по них так просто не поітеруєшся
 * 
 * Декоратор (Decorator)
 * * Фасад (Facade) - інтерфейс що агрегує інші інтерфейси (ендпоінт створення чогось, що чкладається з багатьох елементів)
 * FarmCLI.consoleCreateFarm()
 * 
 * * Легковаговик (Flyweight) - винесення initial state або загальних даних в окремий клас або сервіс
 * Field.type + FieldService + FieldCLI --> Plant + PlantService
 * Barn.type + BarnService + BarnCLI --> Animal.type + AnimalService
 * 
 * Проксі (Proxy)
 *
 * ? Поведінкові патерни (Behavioral Patterns):
 * !Ланцюг відповідальності (Chain of Responsibility) - обробка різними процесами по черзі (горизонтально)
 * 
 * * Команда (Command) - виконання певної дії з інкапсульованими в неї залежностями та параметрами (ActionsList[i].execute())
 *  AnimalCLI <-- ConsoleEditable --> MainControlerService
 * 
 * !Інтерпретатор (Interpreter) - дискретна математика. граматики. об'єкти - термінальні (і не термінальні) символи, ну і парсер який отримує * * щось кінцеве з них
 * !Ітератор (Iterator) - тупо стандартний ітератор з hasNext() & next()
 * !Посередник (Mediator) - виконує взаємодію між різними об'єктами (у мене був би антипатерном)
 * 
 * ?Хранитель (Memento) - інкапсульовано зберігає стан до якого можна повернутися
 * у мене є BudgetService який зберігає свій стан (хоча з іншою метою і в інший спосіб). реалізовує мінімальну ідею memento але зовсім не так, як у патерні
 * 
 * !Спостерігач (Observer) - для обєктів observers викликає update() коли змінює свій стан
 * !Стан (State) - стрьомна штука, яку я б використовував якби дізнався раніше, а так у мене switch-case (дозволяє чітко розділити логіку при різних станах обє'кта)
 * 
 * * Стратегія (Strategy) - на основі даних з одного обєкта робити дії в другому (буквально всюди по троху, мені лінь шукати, але я знаю що воно там є)
 * 
 * Шаблонний метод (Template Method) - типу в тебе є база, а ти дозбирай що там треба
 * Відвідувач (Visitor) - якась дич або перечитати або мені не сподобалося
 *
 * ! Антпатерни (не код, а кринж)
 * 
 * !  Spagetti code 
 * ProductService.getAllProducts() - фаворит
 * 
 */