package org.agrotechnology.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.agrotechnology.Farm.Farm;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import sun.misc.Signal;
import sun.misc.SignalHandler;

//створює опції вибору в терміналі (просто красиво)
final public class terminal {

    /**
     * <h3>Ініціалізація вибору вертикально</h3>
     * @param options   опції до відображення
     * @param topAсtion зазвичай для задання label для опцій, викликається перед опціями і не зникає
     * @return повертає індекс обраного варіанту або -1 при backspace = null
     */
    public static <T> int initOptions(T[] options, Runnable backSpaceAction, Runnable topAction) {
        int selected = 0;
        clean();
        while (true) {
            try {
                cursorOnOff(false);
                printOptions(options, selected, null, 0, new int[] {}, topAction);

                //jLine - бібліотека для зчитування клавіш
                Terminal console = TerminalBuilder.builder().system(true).build();  
                                                                                   
                // метод для зчитування
                int key = console.reader().read(); 

                switch (key) {
                    case 65: // Key arrow up
                        if (selected > 0)
                            selected--;
                        break;
                    case 66: // Key arrow down
                        if (selected < options.length - 1)
                            selected++;
                        break;
                    case 10: // Enter
                        cursorOnOff(true);
                        clean();
                        return selected;

                    case 127: //backspace
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return -1;

                    default:
                        break;
                }

            } catch (IOException e) {
                System.out.println("Error in terminalOptions :/ --> " + e);
                continue;
            }
            cursorOnOff(true);
        }
    }

    /**
     * <h3>Ініціалізація вибору вертикально + горизонтально (одинаковий для всіх)</h3>
     * @param options1 вертикальні опції
     * @param options2 горизонтальні опції
     * @param usedTo   індекси для яких буде використовуватися горизонтальні опції
     * @return повертає масив - [ індекс опції1, індекс опції2 ] при відсутності
     *         опції2 повертає для другого елемента -1, на backspace == null повертає [-1, -1]
     */
    public static <T, V> int[] initOptions(T[] options1, V[] options2, int[] usedTo, Runnable backSpaceAction,
            Runnable topAction) {
        Arrays.sort(usedTo);
        int selected1 = 0;
        int selected2 = 0;

        while (true) {
            try {
                cursorOnOff(false);

                printOptions(options1, selected1, options2, selected2, usedTo, topAction);

                Terminal terminal = TerminalBuilder.builder().system(true).build(); // jLine - бібліотека для зчитування
                                                                                    // клавіш
                int key = terminal.reader().read(); // метод для зчитування

                switch (key) {
                    case 65: // Key arrow up
                        if (selected1 > 0) {
                            selected1--;
                        }
                        break;
                    case 66: // Key arrow down
                        if (selected1 < options1.length - 1) {
                            selected1++;
                        }
                        break;

                    case 67: // Key arrow right
                        if (selected2 < options2.length - 1) {
                            selected2++;
                        }
                        break;
                    case 68: // Key arrow left
                        if (selected2 > 0) {
                            selected2--;
                        }
                        break;

                    case 127: // backspace
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return new int[] { -1, -1 };

                    case 10: // Enter
                        cursorOnOff(true);
                        clean();
                        if (Arrays.binarySearch(usedTo, selected1) >= 0) {
                            return new int[] { selected1, selected2 };
                        } else {
                            return new int[] { selected1, -1 };
                        }

                    default:
                        break;
                }

            } catch (IOException e) {
                System.out.println("Error in terminalOptions :/ --> " + e);
                continue;
            }
            cursorOnOff(true);
        }
    }

    /**
     * <h3>внутрішній метод для відображення списку</h3> (вертикальний та горизонтальний одинаковий)
     * @param options1 - вертикальні опції
     * @param selected1 - індекс вибраного у вертикальних опціях
     * @param options2 - горизонтальні опції (null якщо тільки вертикальний)
     * @param selected2 - індекс вибраного у горизонтальних опціях
     * @param usedTo - масив індексів до яких вертикальних опцій застосовувати горизонтальні опції (null якщо тільки вертикальний)
     * @param topAction - дія перед відображення опцій
     */
    private static <T, V> void printOptions(T[] options1, int selected1, V[] options2, int selected2, int[] usedTo,
            Runnable topAction) {
        clean();
        if (topAction != null) {
            topAction.run();
        }

        for (int i = 0; i < options1.length; i++) {
            if (i == selected1) {
                if (options2 == null || options2.length == 0 || Arrays.binarySearch(usedTo, selected1) < 0) {
                    print("\033[35m> ", options1[i], "\033[0m", "\n");
                } else {
                    print("\033[35m> ", options1[i], "\t\t", "\033[33m", "> ", options2[selected2], " <",
                            "\033[0m", "\n");

                }
            } else {
                print("  ", options1[i], "\n");
            }
        }
    }

    //////////////////////////////////////////////////////////


    /**
     * <h3>Ініціалізація вибору вертикально + горизонтально (різний для всіх)</h3>
     * @param options1 вертикальні опції
     * @param matrix2opt горизонтальні опції (кожен масив для відповідного рядка) [пусті масив або null якщо горизонтального вибору непотрібно]
     * @return повертає масив - [ індекс опції1, індекс опції2 ] при відсутності горизонтального для поточного вибору друге значення = -1
     *           на backspace == null повертає [-1, -1]
     */
    public static <T, V> int[] initOptions(
            T[] options1,
            String[][] matrix2opt,
            Runnable backSpaceAction,
            Runnable topAction) {
        int selected1 = 0;
        int selected2 = 0;

        if (options1.length != matrix2opt.length) {
            terminal.errorExit("list initOptions error");
        }

        while (true) {
            try {
                cursorOnOff(false);

                printOptions(options1, selected1, matrix2opt, selected2, topAction);

                Terminal terminal = TerminalBuilder.builder().system(true).build(); // jLine - бібліотека для зчитування
                                                                                    // клавіш
                int key = terminal.reader().read(); // метод для зчитування

                switch (key) {
                    case 65: // Key arrow up
                        if (selected1 > 0) {
                            selected1--;
                            selected2 = 0;
                        }
                        break;
                    case 66: // Key arrow down
                        if (selected1 < options1.length - 1) {
                            selected1++;
                            selected2 = 0;
                        }
                        break;

                    case 67: // Key arrow right
                        if (selected2 < matrix2opt[selected1].length - 1) {
                            selected2++;
                        }
                        break;
                    case 68: // Key arrow left
                        if (selected2 > 0) {
                            selected2--;
                        }
                        break;

                    case 127: // backspace
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return new int[] { -1, -1 };

                    case 10: // Enter
                        cursorOnOff(true);
                        clean();
                        if (matrix2opt[selected1] == null || matrix2opt[selected1].length == 0) {
                            return new int[] { selected1, -1 };
                        } else {
                            return new int[] { selected1, selected2 };
                        }

                    default:
                        break;
                }

            } catch (IOException e) {
                System.out.println("Error in terminalOptions :/ --> " + e);
                continue;
            }
            cursorOnOff(true);
        }
    }

     /**
     * <h3>внутрішній метод для відображення списку</h3> (горизонтальний різний)
     * @param options1 - вертикальні опції
     * @param selected1 - індекс вибраного у вертикальних опціях
     * @param options2 - горизонтальні опції 
     * @param selected2 - індекс вибраного у горизонтальних опціях
     * @param topAction - дія перед відображення опцій
     */
    private static <T, V> void printOptions(T[] options1, int selected1, V[][] options2, int selected2,
            Runnable topAction) {
        clean();
        if (topAction != null) {
            topAction.run();
        }

        for (int i = 0; i < options1.length; i++) {
            if (i == selected1) {
                if (options2 == null || options2[i] == null || options2[i].length == 0) {
                    print("\033[35m> ", options1[i], "\033[0m", "\n");
                } else {
                    print("\033[35m> ", options1[i], "\t\t", "\033[33m", "> ", options2[i][selected2], " <",
                            "\033[0m", "\n");

                }
            } else {
                print("  ", options1[i], "\n");
            }
        }
    }

    //////////////////////////////////////////////////////////

    /**
     * <h3>відображення в терміналі тексту</h3>
     * @param objs - через кому будь які обєкти
     */
    public static void print(Object... objs) {
        for (Object obj : objs) {
            System.out.print(obj);
        }
    }

    public static <T, V> String formatDataValue(T data, V value) {
        StringBuilder str = new StringBuilder();
        str.append("\033[36m");
        str.append(data);
        str.append("\033[0m: ");

        str.append("\033[33m");
        str.append(value);
        str.append("\033[0m\n");
        return str.toString();
    }

    public static String formatName(String name) {
        return "\033[34m\t\t" + name + ":\n\033[0m";
    }

    private static String getCentered(String text, int offset) {
        return (" ".repeat(Math.max(((80 - text.length()) / 2) + offset, 0)) + text);
    }

    /**
     * Виводить анімоване повідомлення в термінал.
     *
     * @param message Повідомлення, яке буде виведено.
     * @param color   0 - рожевий, 1 - червоний, 2 - зелений.
     */
    public static void previewing(String message, int color) {
        String[] colors = { "\033[35m", "\033[31m", "\033[32m" };

        cleanBudgetToo();
        cursorOnOff(false);
        int length = message.length() + 10;

        String[] lines = {
                getCentered("*".repeat(length), 0),
                getCentered("*    " + colors[color] + message + "\033[0m   *  ", 5),
                getCentered("*".repeat(length), 0),
        };
        try {
            for (int i = length - 1; i >= 0; i--) {
                Thread.sleep(40 + i * 2);
                cleanBudgetToo();

                print(lines[0].substring(0, lines[0].length() - 1 - i), "\n");
                print(lines[1].substring(0, lines[1].length() - 1 - i), "\n");
                print("\033[0m");
                print(lines[2].substring(0, lines[2].length() - 1 - i), "\n");
            }
            Thread.sleep(200);
            clean();
        } catch (Exception e) {
            print(e);
        }
        cursorOnOff(true);
    }

    /**
     * очищує консоль але залишає бюджет
     */
    public static void clean() {
        print("\033[H\033[J", "\n");// чистить консоль
        System.out.flush();
        printBudget();
    }

    /**
     * повністю очищує консоль
     */
    private static void cleanBudgetToo() {
        print("\033[H\033[J", "\n");// чистить консоль
        System.out.flush();
    }

    public static void cursorOnOff(boolean onOff) {
        try {
            if (onOff) {
                new ProcessBuilder("sh", "-c", "stty echo icanon").inheritIO().start();// те саме тільки включає
                print("\033[?25h"); // повертає курсор
            } else {
                print("\033[?25l"); // ховає курсор
                new ProcessBuilder("sh", "-c", "stty -echo -icanon").inheritIO().start();
                /*
                 * запускає зовнішній процес
                 * (stty
                 * -echo -icanon) що приховує символи
                 * .inheritIO перенаправляє потік
                 * консолі через цю команду і запускає
                 * [просто знайшов скопіював і
                 * вставив, бо інакше не міг приховати
                 * символи]
                 */
            }
        } catch (IOException e) {
            errorExit("cursor on/off error");
        }
    }

    /**
     * EventListener для клавіш
     * 
     * @param keyId  127 - backspace
     */
    public static boolean keyAction(int keyId) {
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).build(); // jLine - бібліотека для зчитування
                                                                                // клавіш
            cursorOnOff(false);
            int key = terminal.reader().read(); // метод для зчитування
            cursorOnOff(true);
            if (key == keyId) {
                return true;
            }
        } catch (IOException e) {
            errorExit("key Action error");
        }
        return false;
    }

    public static void errorExit(String message) {
        previewing(message, 1);
        cleanBudgetToo();
        System.exit(1);
        return;
    }

    public static void exit() {
        previewing("thanks :)", 0);
        cleanBudgetToo();
        System.exit(0);
        return;
    }

    /**
     * Контролює Ctrl C
     * 
     * @param onOff true - enable exit on Ctrl C, false - disable
     */
    public static void CtrlC(boolean onOff) {
        if (onOff) {
            Signal.handle(new Signal("INT"), new SignalHandler() {
                @Override
                public void handle(Signal signal) {
                    exit();
                }
            });
        } else {
            Signal.handle(new Signal("INT"), new SignalHandler() {
                @Override
                public void handle(Signal signal) {
                }
            });
        }
    }

    public static String input(String message) {
        clean();
        cursorOnOff(true);
        String str = "";
        try {
            while (str.isEmpty()) {
                Terminal console = TerminalBuilder.terminal();
                LineReader reader = LineReaderBuilder.builder().terminal(console).build();

                str = reader.readLine(colorize(message, 0, false));
            }
            clean();
            return str;

        } catch (IOException e) {
            return input(message);
        }
    }

    public static int inputNumber(String message) {
        clean();
        print(colorize(message, 0, false));
        cursorOnOff(true);
        try {
            System.in.skip(System.in.available());
            Scanner scanner = new Scanner(System.in);
            int data = scanner.nextInt();
            clean();
            return data;

        } catch (Exception e) {
            return inputNumber(message);
        }
    }

    /**
     * 
     * @param color    0 - pink, 1 - red, 2 - blueNeon, 3 - yellow, 4 - green
     * @param boldness показує чи потрібно робити жирний шрифт
     */
    public static String colorize(String text, int color, boolean boldness) {
        String[] colors = { "\033[35m", "\033[31m", "\033[36m", "\033[33m", "\033[32m" };
        String[] colorsBold = { "\033[1;35m", "\033[1;31m", "\033[1;36m", "\033[1;33m", "\033[1;32m" };
        if (boldness) {
            return colorsBold[color] + text + "\033[0m";

        }
        return colors[color] + text + "\033[0m";
    }

    /**
     * просто потрібно завжди бачити бюджет
     */
    private static void printBudget() {
        StringBuilder str = new StringBuilder();
        str.append(colorize("BUDGET", 2, true));
        str.append(": \t\t\t");
        str.append(colorize(Farm.getBudget() + "$", 3, true));
        str.append("\n");
        terminal.print(str.toString());
    }

    /**
     * preview typized
     * @param state "seccesfully {state}"
     */
    public static void statusMessage(boolean status, String state) {
        if (status) {
            terminal.previewing("seccefully " + state, 2);
        } else {
            terminal.previewing("something went wrong", 1);
        }
    }
}
