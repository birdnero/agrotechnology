package org.agrotechnology.Worker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.agrotechnology.Farm.Farm;
import org.agrotechnology.Worker.Person.Sex;
import org.agrotechnology.utils.terminal;

public class WorkerUtil {

    private static String[] names = null;
    private static String[] positions = null;
    private static String[] duties = null;

    private static String[] readFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + path.toAbsolutePath());
        }
        return Files.readString(path).split(", ");
    }

    // ! write comments
    private static String birthdayGenerate() {
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
    private static ArrayList<Worker> workerArrGenerator(int amount, String workPlace) {
        ArrayList<Worker> workers = new ArrayList<Worker>();

        try {
            SecureRandom r = new SecureRandom();
            if (positions == null) {
                positions = readFile(Path.of("src/main/java/org/agrotechnology/Worker/data/positions.csv"));
            }
            if (names == null) {
                names = readFile(Path.of("src/main/java/org/agrotechnology/Worker/data/names.csv"));
            }
            if (duties == null) {
                duties = readFile(Path.of("src/main/java/org/agrotechnology/Worker/data/duties.csv"));
            }

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
        } catch (Exception e) {
            terminal.print("ERROR in file reading in WorkerUtil -> ");
            e.printStackTrace();
        }
        return workers;
    }

    // ? </-- CONSOLE METHODS --/>

    public static ArrayList<Worker> consoleCreateWorkers(String workPlace) {
        Integer[] workersAmount = new Integer[10];
        for (int i = 0; i < 10; i++) {
            workersAmount[i] = i + 1;
        }
        int[] workersAm = terminal.initOptions(new String[] { "workers to hire" }, workersAmount,
                new int[] { 0 },
                () -> {
                }, null);

        return WorkerUtil.workerArrGenerator(workersAm[1] + 1, workPlace);
    }

    public static void consoleActions(Farm farm) {
        ArrayList<Worker> workers = farm.getWorkers();
        while (true) {
            String[] workerNames = new String[workers.size() + 1];
            workerNames[0] = "hire";
            int[] included = new int[workers.size()];
            for (int i = 0; i < workers.size(); i++) {
                workerNames[i + 1] = workers.get(i).getFullName();
                included[i] = i + 1;
            }

            String[] options = new String[] {
                    "salary",
                    "position",
                    "duty"
            };

            int[] selected = terminal.initOptions(
                    workerNames,
                    options,
                    included,
                    null,
                    () -> terminal.print(terminal.colorize("\tWORKERS:\n", 0, true)));

            if (selected[0] == -1) {
                return;
            } else if (selected[0] == 0) {
                consoleHireSomeOne(farm);

            } else {
                try {
                    switch (selected[1]) {
                        case 0:
                            String[] allowedSalary = new String[50];
                            for (int i = 1; i <= 50; i++) {
                                allowedSalary[i-1] = (i * 2500) + "";
                            }

                            int[] newSalary = terminal.initOptions(new String[] { "salary: " }, allowedSalary,
                                    new int[] { 0 }, null, null);

                            if (newSalary[0] == -1) {
                                break;
                            }
                            workers.get(selected[0] - 1).salary = (newSalary[1] + 1) * 2500;
                            terminal.statusMessage(true, "changed");
                            break;

                        case 1:
                            if (positions == null) {
                                positions = readFile(
                                        Path.of("src/main/java/org/agrotechnology/Worker/data/positions.csv"));
                            }

                            int position = terminal.initOptions(positions, null,
                                    () -> terminal.print(terminal.colorize("\tSelect position\n", 0, false)));

                            if (position == -1) {
                                break;
                            }
                            workers.get(selected[0] - 1).position = positions[position];
                            terminal.statusMessage(true, "changed");
                            break;

                        case 2:
                            if (duties == null) {
                                duties = readFile(
                                        Path.of("src/main/java/org/agrotechnology/Worker/data/duties.csv"));
                            }

                            int duty = terminal.initOptions(duties, null,
                                    () -> terminal.print(terminal.colorize("\tSelect duty\n", 0, false)));

                                    
                            if (duty == -1) {
                                break;
                            }
                            workers.get(selected[0] - 1).duty = duties[duty];
                            terminal.statusMessage(true, "changed");

                            break;

                        default:
                            break;
                    }

                } catch (IOException e) {
                    terminal.errorExit("file reding error");
                }
            }

        }

    }

    private static void consoleHireSomeOne(Farm farm) {
        try {
            if (positions == null) {
                positions = readFile(
                        Path.of("src/main/java/org/agrotechnology/Worker/data/positions.csv"));
            }

            String[] allowedSalary = new String[50];
            for (int i = 1; i <= 50; i++) {
                allowedSalary[i-1] = (i * 2500) + "";
            }

            if (duties == null) {
                duties = readFile(
                        Path.of("src/main/java/org/agrotechnology/Worker/data/duties.csv"));
            }

            int position = terminal.initOptions(positions, null,
                    () -> terminal.print(terminal.colorize("\tSelect position\n", 0, false)));

            if (position == -1) {
                return;
            }

            int duty = terminal.initOptions(duties, null,
                    () -> terminal.print(terminal.colorize("\tSelect duty\n", 0, false)));

            if (duty == -1) {
                return;
            }

            int[] newSalary = terminal.initOptions(new String[] { "salary: " }, allowedSalary,
                    new int[] { 0 }, null, null);

            if (newSalary[0] == -1) {
                return;
            }

            if (names == null) {
                names = readFile(Path.of("src/main/java/org/agrotechnology/Worker/data/names.csv"));
            }

            SecureRandom r = new SecureRandom();

            farm.getWorkers().add(new Worker(
                    names[r.nextInt(names.length)],
                    (r.nextInt(2) == 0 ? Sex.MALE : Sex.FEMALE),
                    birthdayGenerate(),
                    positions[position],
                    r.nextInt(10),
                    (newSalary[1] + 1) * 2500,
                    duties[duty],
                    farm.getName()));
            terminal.statusMessage(true, "hired");

        } catch (IOException e) {
            terminal.errorExit("consoleHireSomeOne error FileReader");
        }

    }

}
