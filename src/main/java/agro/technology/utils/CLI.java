package agro.technology.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.stereotype.Service;
import sun.misc.Signal;
import sun.misc.SignalHandler;

//створює опції вибору в терміналі (просто красиво)
@Service
public class CLI {

    // jLine - бібліотека для зчитування клавіш
    private Terminal terminal;

    public CLI() {
        try {
            this.topInfo = new HashMap<>();
            this.terminal = TerminalBuilder.builder().system(true).build();
            ;
        } catch (IOException e) {
            errorExit("terminal initialization error(");
        }
    }

    public static class CLIException extends RuntimeException {
        public CLIException(String message) {
            super(message);
        }
    }

    public static enum Colors {
        BOLD(1),
        UNDERLINE(4),
        NEGATIVE(7),

        BLACK(30),
        RED(31),
        GREEN(32),
        YELLOW(33),
        BLUE(34),
        PINK(35),
        BlUE_NEON(36),

        BACKGROUND_RED(41),
        BACKGROUND_GREEN(42),
        BACKGROUND_YELLOW(43),
        BACKGROUND_BLUE(44),
        BACKGROUND_PINK(45),
        BACKGROUND_BLUE_NEON(46),
        BACKGROUND_WHITE(47),

        NONE(0);

        private final int code;

        Colors(int code) {
            this.code = code;
        }

        public int getSimpleCode() {
            return code;
        }

        public String getColor() {
            return "\033[" + code + "m";
        }

        public String getColorBold() {
            return "\033[1;" + code + "m";
        }
    }

    public static enum Keys {
        UP(65),
        DOWN(66),
        RIGHT(67),
        LEFT(68),
        ENTER(10),
        BACKSPACE(127),
        UNDEFINED(-1);

        private final int code;

        Keys(int code) {
            this.code = code;
        }

        public int getKey() {
            return code;
        }

        public static Keys defineKey(int code) {
            for (Keys key : Keys.values()) {
                if (key.getKey() == code) {
                    return key;
                }
            }
            return UNDEFINED;
        }
    }

    // метод для зчитування
    protected int readKey() throws IOException {
        return this.terminal.reader().read();
    }

    /**
     * <h3>Ініціалізація вибору вертикально</h3>
     * 
     * @param options   опції до відображення
     * @param topAсtion зазвичай для задання label для опцій, викликається перед
     *                  опціями і не зникає
     * @return повертає індекс обраного варіанту або -1 при backspace = null
     */
    public <T> int initOptions(T[] options, Runnable backSpaceAction, Runnable topAction) throws NullPointerException {
        int selected = 0;
        if (options.length == 0)
            return -1;
        clean();
        while (true) {
            try {
                cursorOnOff(false);
                printOptions(options, selected, null, 0, new int[] {}, topAction);

                int key = readKey();

                switch (Keys.defineKey(key)) {
                    case Keys.UP -> {
                        if (selected > 0)
                            selected--;
                    }
                    case Keys.DOWN -> {
                        if (selected < options.length - 1)
                            selected++;
                    }
                    case Keys.ENTER -> {
                        cursorOnOff(true);
                        clean();
                        if (options[selected] != null)
                            return selected;
                        else
                            return -1;
                    }

                    case Keys.BACKSPACE -> {
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return -1;

                    }

                    default -> {
                    }
                }

            } catch (IOException e) {
                previewing("cli: printOptions1 error", Colors.RED);
                continue;
            }
            cursorOnOff(true);
        }
    }

    /**
     * <h3>Ініціалізація вибору вертикально + горизонтально (одинаковий для
     * всіх)</h3>
     * 
     * @param options1 вертикальні опції
     * @param options2 горизонтальні опції
     * @param usedTo   індекси для яких буде використовуватися горизонтальні опції
     * @return повертає масив - [ індекс опції1, індекс опції2 ] при відсутності
     *         опції2 повертає для другого елемента -1, на backspace == null
     *         повертає [-1, -1]
     */
    public <T, V> int[] initOptions(T[] options1, V[] options2, int[] usedTo, Runnable backSpaceAction,
            Runnable topAction) throws NullPointerException {
        Arrays.sort(usedTo);
        int selected1 = 0;
        int selected2 = 0;
        if (options1.length == 0)
            return new int[] { -1, -1 };

        while (true) {
            try {
                cursorOnOff(false);

                printOptions(options1, selected1, options2, selected2, usedTo, topAction);

                // клавіш
                int key = readKey(); // метод для зчитування

                switch (Keys.defineKey(key)) {
                    case Keys.UP:
                        if (selected1 > 0) {
                            selected1--;
                        }
                        break;
                    case Keys.DOWN:
                        if (selected1 < options1.length - 1) {
                            selected1++;
                        }
                        break;

                    case Keys.RIGHT:
                        if (selected2 < options2.length - 1) {
                            selected2++;
                        }
                        break;
                    case Keys.LEFT:
                        if (selected2 > 0) {
                            selected2--;
                        }
                        break;

                    case Keys.BACKSPACE:
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return new int[] { -1, -1 };

                    case Keys.ENTER:
                        cursorOnOff(true);
                        clean();
                        if (options1[selected1] == null)
                            return new int[] { -1, -1 };
                        if (Arrays.binarySearch(usedTo, selected1) >= 0 && options2 != null && options2.length > 0) {
                            return new int[] { selected1, selected2 };
                        } else {
                            return new int[] { selected1, -1 };
                        }

                    default:
                        break;
                }

            } catch (IOException e) {
                previewing("cli: printOptions2 error", Colors.RED);
                continue;
            }
            cursorOnOff(true);
        }
    }

    /**
     * <h3>внутрішній метод для відображення списку</h3> (вертикальний та
     * горизонтальний одинаковий)
     * 
     * @param options1  - вертикальні опції
     * @param selected1 - індекс вибраного у вертикальних опціях
     * @param options2  - горизонтальні опції (null якщо тільки вертикальний)
     * @param selected2 - індекс вибраного у горизонтальних опціях
     * @param usedTo    - масив індексів до яких вертикальних опцій застосовувати
     *                  горизонтальні опції (null якщо тільки вертикальний)
     * @param topAction - дія перед відображення опцій
     */
    private <T, V> void printOptions(T[] options1, int selected1, V[] options2, int selected2, int[] usedTo,
            Runnable topAction) {
        clean();
        if (topAction != null) {
            topAction.run();
        }

        for (int i = 0; i < options1.length; i++) {
            if (i == selected1) {
                if (options2 == null || options2.length == 0 || Arrays.binarySearch(usedTo, selected1) < 0) {
                    print(Colors.PINK.getColor(), "> ", (options1[i] == null ? "--?--" : options1[i]),
                            Colors.NONE.getColor(), "\n");
                } else {
                    print(Colors.PINK.getColor(), "> ", (options1[i] == null ? "--?--" : options1[i]), "\t\t",
                            Colors.YELLOW.getColor(), "> ",
                            (options2[selected2] == null ? "--?--" : options2[selected2]), " <",
                            Colors.NONE.getColor(), "\n");

                }
            } else {
                print("  ", options1[i], "\n");
            }
        }
    }

    //////////////////////////////////////////////////////////

    /**
     * <h3>Ініціалізація вибору вертикально + горизонтально (різний для всіх)</h3>
     * 
     * @param options1   вертикальні опції
     * @param matrix2opt горизонтальні опції (кожен масив для відповідного рядка)
     *                   [пусті масив або null якщо горизонтального вибору
     *                   непотрібно]
     * @return повертає масив - [ індекс опції1, індекс опції2 ] при відсутності
     *         горизонтального для поточного вибору друге значення = -1
     *         на backspace == null повертає [-1, -1]
     */
    public <T, V> int[] initOptions(
            T[] options1,
            String[][] matrix2opt,
            Runnable backSpaceAction,
            Runnable topAction) {
        int selected1 = 0;
        int selected2 = 0;

        if ((options1 == null || matrix2opt == null) || options1.length != matrix2opt.length) {
            errorExit("list initOptions error");
        }
        if (options1.length == 0)
            return new int[] { -1, -1 };

        while (true) {
            try {
                cursorOnOff(false);

                printOptions(options1, selected1, matrix2opt, selected2, topAction);

                int key = readKey(); // метод для зчитування

                switch (Keys.defineKey(key)) {
                    case Keys.UP:
                        if (selected1 > 0) {
                            selected1--;
                            if (matrix2opt[selected1] == null)
                                selected2 = -1;
                            if (matrix2opt[selected1] != null && matrix2opt[selected1].length - 1 < selected2)
                                selected2 = matrix2opt[selected1].length - 1;
                            else if (matrix2opt[selected1] != null && matrix2opt[selected1].length > 0
                                    && selected2 == -1)
                                selected2 = 0;
                        }
                        break;
                    case Keys.DOWN:
                        if (selected1 < options1.length - 1) {
                            selected1++;
                            if (matrix2opt[selected1] == null)
                                selected2 = -1;
                            else if (matrix2opt[selected1].length - 1 < selected2)
                                selected2 = matrix2opt[selected1].length - 1;
                            else if (matrix2opt[selected1].length > 0 && selected2 == -1)
                                selected2 = 0;
                        }
                        break;

                    case Keys.RIGHT:
                        if (matrix2opt[selected1] != null && selected2 < matrix2opt[selected1].length - 1) {
                            selected2++;
                        }
                        break;
                    case Keys.LEFT:
                        if (selected2 > 0) {
                            selected2--;
                        }
                        break;

                    case Keys.BACKSPACE:
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return new int[] { -1, -1 };

                    case Keys.ENTER:
                        cursorOnOff(true);
                        clean();
                        if (options1[selected1] == null)
                            return new int[] { -1, -1 };
                        if (matrix2opt[selected1] == null ||
                                matrix2opt[selected1].length == 0 ||
                                matrix2opt[selected1].length < selected2 + 1
                                || matrix2opt[selected1][selected2] == null) {
                            return new int[] { selected1, -1 };
                        }
                        return new int[] { selected1, selected2 };

                    default:
                        break;
                }

            } catch (IOException e) {
                previewing("cli: printOptions3 error", Colors.RED);
                continue;
            }
            cursorOnOff(true);
        }
    }

    /**
     * <h3>внутрішній метод для відображення списку</h3> (горизонтальний різний)
     * 
     * @param options1  - вертикальні опції
     * @param selected1 - індекс вибраного у вертикальних опціях
     * @param options2  - горизонтальні опції
     * @param selected2 - індекс вибраного у горизонтальних опціях
     * @param topAction - дія перед відображення опцій
     */
    private <T, V> void printOptions(T[] options1, int selected1, V[][] options2, int selected2,
            Runnable topAction) {
        clean();
        if (topAction != null) {
            topAction.run();
        }

        for (int i = 0; i < options1.length; i++) {
            if (i == selected1) {
                if (options2 == null || options2[i] == null || options2[i].length == 0) {
                    print(Colors.PINK.getColor() + "> ", options1[i], Colors.NONE.getColor(), "\n");
                } else {
                    print(Colors.PINK.getColor(),
                            "> ",
                            options1[i],
                            "\t\t",
                            Colors.YELLOW.getColor(),
                            "> ",
                            (options2[i][selected2] == null ? "--?--" : options2[i][selected2]),
                            " <",
                            Colors.NONE.getColor(), "\n");

                }
            } else {
                print("  ", options1[i], "\n");
            }
        }
    }

    /**
     * <h3>Ініціалізація мульти-вибору вертикально + горизонтально (різний для
     * всіх)</h3>
     * 
     * @param options1   вертикальні опції
     * @param matrix2opt горизонтальні опції (кожен масив для відповідного рядка)
     *                   [пусті масив або null якщо горизонтального вибору
     *                   непотрібно]
     * @return повертає масив - [ індекс опцій2 ]
     *         [-1] при null або різних розмірах або backspace
     *         масив вибраних у кожному рядку
     */
    public <T, V> List<Integer> multiOptions(
            T[] options1,
            String[][] matrix2opt,
            Runnable backSpaceAction,
            Runnable topAction) {

        int selected1 = 0;
        Integer[] selected2 = IntStream
                .iterate(-1, s -> s)
                .limit(matrix2opt.length)
                .boxed()
                .toArray(Integer[]::new);

        if ((options1 == null ||
                matrix2opt == null) ||
                options1.length != matrix2opt.length ||
                options1.length == 0)
            return List.of(-1);

        while (true) {
            try {
                cursorOnOff(false);

                if (selected2[selected1] == -1)
                    selected2[selected1] = 0;
                printOptions(options1, selected1, matrix2opt, List.of(selected2), topAction);

                int key = readKey(); // метод для зчитування

                switch (Keys.defineKey(key)) {
                    case Keys.UP:
                        if (selected1 > 0)
                            selected1--;
                        break;
                    case Keys.DOWN:
                        if (options1 != null && selected1 < options1.length - 1)
                            selected1++;
                        break;

                    case Keys.RIGHT:
                        if (matrix2opt[selected1] != null && selected2[selected1] < matrix2opt[selected1].length - 1) {
                            selected2[selected1]++;
                        }
                        break;
                    case Keys.LEFT:
                        if (selected2[selected1] > 0) {
                            selected2[selected1]--;
                        }
                        break;

                    case Keys.BACKSPACE:
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return List.of(-1);

                    case Keys.ENTER:
                        cursorOnOff(true);
                        clean();

                        if (options1[selected1] == null ||
                                matrix2opt[selected1] == null ||
                                matrix2opt[selected1].length == 0 ||
                                matrix2opt[selected1].length < selected2[selected1] + 1) {
                            return List.of(-1);
                        }
                        List<Integer> selectedList = List.of(selected2);
                        if (selectedList.contains(-1)) {
                            previewing("unsetted " + options1[selectedList.indexOf(-1)], Colors.YELLOW);
                        } else
                            return List.of(selected2);

                    default:
                        break;
                }

            } catch (IOException e) {
                previewing("cli: printOptions3 error", Colors.RED);
                continue;
            }
            cursorOnOff(true);
        }
    }

    /**
     * <h3>внутрішній метод для відображення мульти списку</h3> (горизонтальний
     * множинний різний)
     * 
     * @param options1  - вертикальні опції
     * @param selected1 - індекс вибраного у вертикальних опціях
     * @param options2  - горизонтальні опції
     * @param selected2 - індекс вибраного у горизонтальних опціях
     * @param topAction - дія перед відображення опцій
     */
    private <T, V> void printOptions(T[] options1, int selected1, V[][] options2, List<Integer> selected2,
            Runnable topAction) {
        clean();
        if (topAction != null) {
            topAction.run();
        }

        for (int i = 0; i < options1.length; i++) {
            if (selected1 == i) {

            }
            if (options2 == null || options2[i] == null || options2[i].length == 0) {
                if (selected1 == i)
                    print(Colors.PINK.getColor() + "> ", options1[i], Colors.NONE.getColor(), "\n");
                else
                    print("  ", options1[i], "\n");

            } else {
                String selected2element = "--?--";
                if (selected2.get(i) == -1)
                    selected2element = "";

                else if (options2[i][selected2.get(i)] != null)
                    selected2element = options2[i][selected2.get(i)].toString();

                if (selected1 == i)
                    print(Colors.PINK.getColor(),
                            "> ",
                            options1[i],
                            "\t\t",
                            Colors.NONE.getColor(),
                            Colors.YELLOW.getColor(),
                            "> ",
                            selected2element,
                            " <",
                            Colors.NONE.getColor(),
                            "\n");
                else
                    print("  ",
                            Colors.NONE.getColor(),
                            options1[i],
                            "\t\t  ",
                            selected2element,
                            "  \n");

            }

        }
    }

    //////////////////////////////////////////////////////////

    /**
     * <h3>відображення в терміналі тексту</h3>
     * 
     * @param objs - через кому будь які обєкти
     */
    public void print(Object... objs) {
        for (Object obj : objs) {
            System.out.print(obj);
        }
    }

    public <T, V> String formatDataValue(T data, V value) {
        StringBuilder str = new StringBuilder();
        str.append(Colors.BlUE_NEON.getColor());
        str.append(data);
        str.append(Colors.NONE.getColor());
        str.append(": ");

        str.append(Colors.YELLOW.getColor());
        str.append(value);
        str.append(Colors.NONE.getColor());
        str.append("\n");
        return str.toString();
    }

    public String formatName(String name) {
        if (name == null)
            name = "--?--";
        return Colors.BlUE_NEON.getColor() + "\t\t" + name + ":\n" + Colors.NONE.getColor();
    }

    public String getCentered(String text, int offset) {
        return (" ".repeat(Math.max(((80 - text.length()) / 2) + offset, 0)) + text);
    }

    /**
     * Виводить анімоване повідомлення в термінал.
     *
     * @param message Повідомлення, яке буде виведено.
     * @param color   0 - рожевий, 1 - червоний, 2 - зелений.
     */
    public void previewing(String message, Colors color) {

        cleanWithTopAction();
        cursorOnOff(false);
        int length = message.length() + 10;

        String[] lines = {
                getCentered("*".repeat(length), 0),
                getCentered("*    " + color.getColor() + message + Colors.NONE.getColor() + "  *  ", 5),
                getCentered("*".repeat(length), 0),
        };
        try {
            for (int i = length - 1; i >= 0; i--) {
                Thread.sleep(40 + i * 2);
                cleanWithTopAction();

                print(lines[0].substring(0, lines[0].length() - 1 - i), "\n");
                print(lines[1].substring(0, lines[1].length() - 1 - i), "\n");
                print(Colors.NONE.getColor());
                print(lines[2].substring(0, lines[2].length() - 1 - i), "\n");
            }
            Thread.sleep(200);
            clean();
        } catch (Exception e) {
            print(e);
        }
        cursorOnOff(true);
    }

    private Map<String, Supplier<String>> topInfo = null;

    public void topInfoHook(String name, Supplier<String> hook) {
        this.topInfo.put(name, hook);
    }

    /**
     * очищує консоль виводить верхню інформацію
     */
    public void clean() {
        print("\033[H\033[J", "\n");// чистить консоль
        System.out.flush();
        this.topInfo.values().stream().forEach(fn -> {
            if (fn != null)
                print(fn.get());
        });

    }

    /**
     * повністю очищує консоль
     */
    private void cleanWithTopAction() {
        print("\033[H\033[J", "\n");// чистить консоль
        System.out.flush();
    }

    /**
     * відключить курсор якщо передати false і включить якщо true
     */
    public void cursorOnOff(boolean onOff) {
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
     * @param keyId 127 - backspace
     */
    public boolean keyAction(int keyId) {
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

    /**
     * вихід через помилку з виводом
     */
    public void errorExit(String message) {
        previewing(message, Colors.RED);
        cleanWithTopAction();
        throw new CLIException(message);
    }

    /**
     * мирний вихід з програми
     */
    public void exit() {
        previewing("thanks :)", Colors.PINK);
        cleanWithTopAction();
        System.exit(0);
        return;
    }

    /**
     * Контролює Ctrl C
     * 
     * @param onOff true - enable exit on Ctrl C, false - disable
     */
    public void CtrlC(boolean onOff) {
        if (onOff) {
            // я забув як це працює (короче чат джпт сказав що воно блокує CtrC вихід, я
            // перевірив і реально блокує)
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

    public String input(String message) {
        clean();
        cursorOnOff(true);
        String str = "";
        try {
            while (str.isEmpty()) {
                Terminal console = TerminalBuilder.terminal();
                LineReader reader = LineReaderBuilder.builder().terminal(console).build();

                str = reader.readLine(colorize(message, Colors.PINK, false));
            }
            clean();
            return str;

        } catch (IOException e) {
            clean();
            return input(message);
        }
    }

    public String input(Runnable topAction) {
        clean();
        cursorOnOff(true);
        String str = "";
        try {
            while (str.isEmpty()) {
                Terminal console = TerminalBuilder.terminal();
                LineReader reader = LineReaderBuilder.builder().terminal(console).build();
                if (topAction != null)
                    topAction.run();
                str = reader.readLine();// colorize(message, 0, false)
            }
            clean();
            return str;

        } catch (IOException e) {
            clean();
            return input(topAction);
        }
    }

    public int inputNumber(String message) {
        clean();
        print(colorize(message, Colors.PINK, false));
        cursorOnOff(true);
        try {
            Terminal console = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(console).build();
            int data = Integer.parseInt(reader.readLine());
            clean();
            return data;

        } catch (Exception e) {
            return inputNumber(message);
        }
    }

    public int inputNumber(Runnable topAction) {
        clean();
        // print(colorize(message, 0, false));
        if (topAction != null)
            topAction.run();
        cursorOnOff(true);
        try {
            Terminal console = TerminalBuilder.terminal();
            LineReader reader = LineReaderBuilder.builder().terminal(console).build();
            int data = Integer.parseInt(reader.readLine());
            clean();
            return data;

        } catch (Exception e) {
            return inputNumber(topAction);
        }
    }

    /**
     * 
     * @param boldness показує чи потрібно робити жирний шрифт
     */
    public String colorize(String text, Colors color, boolean boldness) {
        if (text == null)
            text = "--?--";
        if (color == null)
            color = Colors.NONE;
        return (boldness ? color.getColorBold() : color.getColor()) + text + Colors.NONE.getColor();
    }

    /**
     * preview typized
     * 
     * @param state "seccesfully {state}"
     */
    public void statusMessage(boolean status, String state) {
        if (status) {
            previewing("seccefully " + state, Colors.GREEN);
        } else {
            previewing("something went wrong", Colors.RED);
        }
    }

    /**
     * шаблон для label для опцій
     */
    public String optionsLabel(String label) {
        if (label == null)
            label = "--?--";
        return colorize("\t" + label.toUpperCase() + "\n", Colors.PINK, true);
    }

    public int initBoolOptions(String question,
            Runnable backSpaceAction,
            Runnable topAction) {
        return initBinarOptions(question, "yes", "no", Colors.GREEN, Colors.RED, backSpaceAction, topAction);
    }

    public int initBinarOptions(
            String question,
            String opt1,
            String opt2,
            Colors color1,
            Colors color2,
            Runnable backSpaceAction,
            Runnable topAction) {
        boolean selected = true;

        while (true) {
            try {
                cursorOnOff(false);
                clean();
                if (topAction != null) {
                    topAction.run();
                }
                print((question == null ? "" : question), "\t");
                if (!selected)
                    print(opt1, " or ", colorize(opt2, color2, false));
                else
                    print(colorize(opt1, color1, false), " or ", opt2);
                // клавіш
                int key = readKey(); // метод для зчитування

                switch (Keys.defineKey(key)) {
                    case Keys.RIGHT:
                        selected = false;
                        break;
                    case Keys.LEFT:
                        selected = true;
                        break;

                    case Keys.BACKSPACE:
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return -1;

                    case Keys.ENTER:
                        cursorOnOff(true);
                        clean();
                        return (selected ? 0 : 1);

                    default:
                        break;
                }

            } catch (IOException e) {
                previewing("cli: initBoolOptions error", Colors.RED);
                continue;
            }
            cursorOnOff(true);
        }
    }

    public <T, V> int[] initHotOptions(Supplier<T[]> options1, Supplier<V[][]> options2,
            Runnable backSpaceAction,
            Runnable topAction) throws NullPointerException {
        int selected1 = 0;
        int selected2 = 0;

        while (true) {
            try {
                cursorOnOff(false);

                T[] opt1 = options1.get();
                V[][] opt2 = options2.get();

                printOptions(opt1, selected1, opt2, selected2, topAction);

                int key = readKey(); // метод для зчитування

                switch (Keys.defineKey(key)) {
                    case Keys.UP:
                        if (selected1 > 0) {
                            selected1--;
                            if (opt2[selected1] == null)
                                selected2 = -1;
                            if (opt2[selected1] != null && opt2[selected1].length - 1 < selected2)
                                selected2 = opt2[selected1].length - 1;
                            else if (opt2[selected1] != null && opt2[selected1].length > 0
                                    && selected2 == -1)
                                selected2 = 0;
                        }
                        break;
                    case Keys.DOWN:
                        if (selected1 < opt1.length - 1) {
                            selected1++;
                            if (opt2[selected1] == null)
                                selected2 = -1;
                            else if (opt2[selected1].length - 1 < selected2)
                                selected2 = opt2[selected1].length - 1;
                            else if (opt2[selected1].length > 0 && selected2 == -1)
                                selected2 = 0;
                        }
                        break;

                    case Keys.RIGHT:
                        if (selected2 < opt2[selected1].length - 1) {
                            selected2++;
                        }
                        break;
                    case Keys.LEFT:
                        if (selected2 > 0) {
                            selected2--;
                        }
                        break;

                    case Keys.BACKSPACE:
                        if (backSpaceAction != null) {
                            backSpaceAction.run();
                        }
                        return new int[] { -1, -1 };

                    case Keys.ENTER:
                        cursorOnOff(true);
                        clean();
                        if (opt1[selected1] == null)
                            return new int[] { -1, -1 };
                        if (opt2[selected1] == null ||
                                opt2[selected1].length == 0 ||
                                opt2[selected1].length < selected2 + 1
                                || opt2[selected1][selected2] == null) {
                            return new int[] { selected1, -1 };
                        }
                        return new int[] { selected1, selected2 };

                    default:
                        break;
                }

            } catch (IOException e) {
                previewing("cli: printOptions3 error", Colors.RED);
                continue;
            }
            cursorOnOff(true);
        }
    }
}
