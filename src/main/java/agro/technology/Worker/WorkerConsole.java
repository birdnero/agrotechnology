package agro.technology.Worker;

import java.security.SecureRandom;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import agro.technology.Farms.Farm;
import agro.technology.Worker.Person.Sex;
import agro.technology.utils.CLI;
import agro.technology.utils.CLI.Colors;

@Service
public class WorkerConsole {

    private final WorkerFactory workerFactory;
    private final CLI terminal;

    public WorkerConsole(CLI terminal, WorkerFactory workerFactory) {
        this.terminal = terminal;
        this.workerFactory = workerFactory;
    }

    public String report(Worker worker) {
        StringBuilder str = new StringBuilder();

        str.append(terminal.formatName(worker.fullName));
        str.append(terminal.formatDataValue("sex", (worker.sex == Sex.MALE ? "male" : "female")));
        str.append(terminal.formatDataValue("age", worker.getAge()));
        str.append(terminal.formatDataValue("position", worker.position));
        str.append(terminal.formatDataValue("salary", worker.salary));
        str.append(terminal.formatDataValue("expirience", worker.expirience));
        str.append(terminal.formatDataValue("duty", worker.duty));
        str.append("\n");

        return str.toString();
    }

    /**
     * <h3>майтер створення масиву працівників</h3>
     * 
     * @param workPlace - назва ферми
     * @return масив працівників
     */
    public ArrayList<Worker> createWorkers(String workPlace) {
        Integer[] workersAmount = new Integer[10];
        for (int i = 0; i < 10; i++) {
            workersAmount[i] = i + 1;
        }
        int[] workersAm = terminal.initOptions(new String[] { "workers to hire" }, workersAmount,
                new int[] { 0 },
                () -> {
                }, null);

        if (workersAm[0] == -1)
            return null;

        return workerFactory.workerArrGenerator(workersAm[1] + 1, workPlace);
    }

    /**
     * викликає меню доступних дій надпрацівниками на потрібній фермі
     */
    public void actions(Farm farm) {
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
                    () -> terminal.print(terminal.colorize("\tWORKERS:\n", Colors.PINK, true)));

            if (selected[0] == -1) {
                return;
            } else if (selected[0] == 0) {
                consoleHireSomeOne(farm);

            } else {
                switch (selected[1]) {
                    case 0:
                        String[] allowedSalary = new String[50];
                        for (int i = 1; i <= 50; i++) {
                            allowedSalary[i - 1] = (i * 2500) + "";
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
                        int position = terminal.initOptions(workerFactory.getPositions(), null,
                                () -> terminal.print(terminal.colorize("\tSelect position\n", Colors.PINK, false)));

                        if (position == -1) {
                            break;
                        }
                        workers.get(selected[0] - 1).position = workerFactory.getPositions()[position];
                        terminal.statusMessage(true, "changed");
                        break;

                    case 2:
                        int duty = terminal.initOptions(workerFactory.getDuties(), null,
                                () -> terminal.print(terminal.colorize("\tSelect duty\n", Colors.PINK, false)));

                        if (duty == -1) {
                            break;
                        }
                        workers.get(selected[0] - 1).duty = workerFactory.getDuties()[duty];
                        terminal.statusMessage(true, "changed");

                        break;

                    default:
                        break;
                }
            }

        }

    }

    /**
     * меню найму нового працівника на потрібну ферму
     */
    private void consoleHireSomeOne(Farm farm) {

        String[] allowedSalary = new String[50];
        for (int i = 1; i <= 50; i++) {
            allowedSalary[i - 1] = (i * 2500) + "";
        }

        int position = terminal.initOptions(workerFactory.getPositions(), null,
                () -> terminal.print(terminal.colorize("\tSelect position\n", Colors.PINK, false)));

        if (position == -1) {
            return;
        }

        int duty = terminal.initOptions(workerFactory.getDuties(), null,
                () -> terminal.print(terminal.colorize("\tSelect duty\n", Colors.PINK, false)));

        if (duty == -1) {
            return;
        }

        int[] newSalary = terminal.initOptions(new String[] { "salary: " }, allowedSalary,
                new int[] { 0 }, null, null);

        if (newSalary[0] == -1) {
            return;
        }

        SecureRandom r = new SecureRandom();

        farm.getWorkers().add(new Worker(
                workerFactory.getNames()[r.nextInt(workerFactory.getNames().length)],
                (r.nextInt(2) == 0 ? Sex.MALE : Sex.FEMALE),
                workerFactory.birthdayGenerate(),
                workerFactory.getPositions()[position],
                r.nextInt(10),
                (newSalary[1] + 1) * 2500,
                workerFactory.getDuties()[duty],
                farm.getName()));
        terminal.statusMessage(true, "hired");

    }

}
