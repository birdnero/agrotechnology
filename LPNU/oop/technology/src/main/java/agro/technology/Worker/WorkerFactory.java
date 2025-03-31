package agro.technology.Worker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import agro.technology.Worker.Person.Sex;
import agro.technology.utils.CLI;
import lombok.Getter;

@Service
@Getter
public class WorkerFactory {

    private String[] names;
    private String[] positions;
    private String[] duties;

    private final CLI terminal;

    public WorkerFactory(CLI terminal) {
        this.terminal = terminal;
        try {
            positions = readFile(Path.of("src/main/java/agro/technology/Worker/data/positions.csv"));
            names = readFile(Path.of("src/main/java/agro/technology/Worker/data/names.csv"));
            duties = readFile(Path.of("src/main/java/agro/technology/Worker/data/duties.csv"));

        } catch (Exception e) {
            terminal.print("ERROR in file reading in WorkerUtil -> ");
            e.printStackTrace();
        }

    }

    private String[] readFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + path.toAbsolutePath());
        }
        return Files.readString(path).split(", ");
    }

    /**
     * <h3>генерує будь яку дату від 1960р до сьогодні - 18р</h3>
     * 
     * @return рядок дати
     */
    public String birthdayGenerate() {
        // робить дату 1960р
        LocalDate start = LocalDate.of(1960, 1, 1);
        // дата сьогодні - 18р
        LocalDate end = LocalDate.now().minusYears(18);

        // дістає кількість днів між датами
        long daysBtw = ChronoUnit.DAYS.between(start, end);
        // генерує рандомні числа
        SecureRandom r = new SecureRandom();
        long days = r.nextLong(daysBtw + 1);

        // повертає дату з 1960+ випадкове число днів в потрібному діапазоні
        return start.plusDays(days).format(Person.FORMAT);
    }

    /**
     * <h3>генератор масиву працівників</h3>
     * 
     * @param amount    of workers
     * @param workPlace - name of Farm
     */
    public ArrayList<Worker> workerArrGenerator(int amount, String workPlace) {
        ArrayList<Worker> workers = new ArrayList<Worker>();

        SecureRandom r = new SecureRandom();

        for (int i = 0; i < amount; i++) {
            workers.add(new Worker(
                    names[r.nextInt(names.length)],
                    (r.nextInt(2) == 0 ? Sex.MALE : Sex.FEMALE),
                    birthdayGenerate(),
                    positions[r.nextInt(positions.length)],
                    r.nextInt(10),
                    r.nextInt(145000) + 5000,
                    duties[r.nextInt(duties.length)],
                    workPlace));
        }

        return workers;
    }

    public ArrayList<Worker> load(JsonArray jsonArr) {

        ArrayList<Worker> workers = new ArrayList<>();
        for (var element : jsonArr) {
            JsonObject cellJson = element.getAsJsonObject();
            String position = cellJson.get("position").getAsString();
            int expirience = cellJson.get("expirience").getAsInt();
            int salary = cellJson.get("salary").getAsInt();
            String duty = cellJson.get("duty").getAsString();
            String workPlace = cellJson.get("workPlace").getAsString();

            String birthDay = cellJson.get("birthDay").getAsString();
            String sex = cellJson.get("sex").getAsString();
            String fullName = cellJson.get("fullName").getAsString();

            Worker worker = new Worker(
                    fullName, (sex.equals("MALE") ? Sex.MALE : Sex.FEMALE),
                    birthDay,
                    position,
                    expirience,
                    salary,
                    duty,
                    workPlace);

            workers.add(worker);
        }

        return workers;
    }

    
}
