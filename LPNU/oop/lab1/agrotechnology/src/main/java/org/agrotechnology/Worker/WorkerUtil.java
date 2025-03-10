package org.agrotechnology.Worker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.agrotechnology.Worker.Person.Sex;
import org.agrotechnology.utils.terminal;

public class WorkerUtil {

    private static String[] readFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + path.toAbsolutePath());
        }
        return Files.readString(path).split(", ");
    }

    //! write comments
    private static String birthdayGenerate(){
        LocalDate start = LocalDate.of(1960, 1, 1);
        LocalDate end = LocalDate.now().minusYears(18);

        long daysBtw = ChronoUnit.DAYS.between(start, end);
        SecureRandom r = new SecureRandom();
        long days = r.nextLong(daysBtw + 1);
        
        return start.plusDays(days).format(Person.FORMAT);
    }

    /**
     * 
     * @param amount    of workers
     * @param workPlace - name of Farm
     * @return ArrayList<Worker>
     */
    public static ArrayList<Worker> workerArrGenerator(int amount, String workPlace) {
        ArrayList<Worker> workers = new ArrayList<Worker>();
        
        try {
            SecureRandom r = new SecureRandom();
            String[] names = readFile(Path.of("src/main/java/org/agrotechnology/Worker/data/names.csv"));
            String[] positions = readFile(Path.of("src/main/java/org/agrotechnology/Worker/data/positions.csv"));
            String[] duties = readFile(Path.of("src/main/java/org/agrotechnology/Worker/data/duties.csv"));

            for (int i = 0; i < amount; i++) {
                workers.add(new Worker(
                        names[r.nextInt(names.length)],
                        (r.nextInt(2) == 0 ? Sex.MALE : Sex.FEMALE),
                        birthdayGenerate(),
                        positions[r.nextInt(positions.length)],
                        r.nextInt(10),
                        r.nextInt(50000) + 30000,
                        duties[r.nextInt(duties.length)],
                        workPlace));
            }
        } catch (Exception e) {
            terminal.print("ERROR in file reading in WorkerUtil -> ");
            e.printStackTrace();
        }
        return workers;
    }
}
