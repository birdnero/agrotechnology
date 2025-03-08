package org.agrotechnology.Worker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;

import org.agrotechnology.Worker.Person.Sex;
import org.agrotechnology.utils.terminal;

public class WorkerUtil {

    /**
     * 
     * @param amount    of workers
     * @param workPlace - name of Farm
     * @return ArrayList<Worker>
     */
    public static ArrayList<Worker> workerArrGenerator(int amount, String workPlace) {
        ArrayList<Worker> workers = new ArrayList<Worker>();
        SecureRandom r = new SecureRandom();
        try {
            String[] names = Files.readString(Path.of("/src/main/java/org/agrotechnology/Worker/data/names.csv"))
                    .split(", ");
            String[] positions = Files
                    .readString(Path.of("/src/main/java/org/agrotechnology/Worker/data/positions.csv")).split(", ");
            String[] duties = Files.readString(Path.of("/src/main/java/org/agrotechnology/Worker/data/duties.csv"))
                    .split(", ");

            for (int i = 0; i < amount; i++) {
                workers.add(new Worker(
                        names[r.nextInt(names.length)],
                        (r.nextInt(2) == 0 ? Sex.MALE : Sex.FEMALE),
                        ((r.nextInt(12) + 1) + "/" + (r.nextInt(29) + 1) + "/" + (r.nextInt(49) + 1960)),
                        positions[r.nextInt(positions.length)],
                        r.nextInt(10),
                        r.nextInt(50000) + 30000,
                        duties[r.nextInt(duties.length)],
                        workPlace));
            }
        } catch (Exception e) {
            terminal.print("ERROR in file reading in WorkerUtil -> ", e);
        }
        return workers;
    }
}
