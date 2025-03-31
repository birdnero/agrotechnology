package agro.technology.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import agro.technology.utils.CLI.CLIException;
import agro.technology.utils.CLI.Colors;

@SpringBootTest(args = "--test")
public class TerminalTest {

    private CLITest terminal;

    private class CLITest extends CLI {
        private List<Integer> keyList;
        private int index;

        public CLITest() {
            super();
        }

        @Override
        protected int readKey() throws IOException {
            if (keyList.isEmpty())
                errorExit("TESTS KEY NOT INITIALZED!");
            int key = keyList.get(index);
            if (index + 1 < keyList.size())
                index++;
            return key;
        }

        public void setKey(List<Integer> keyList) {
            this.keyList = keyList;
            this.index = 0;
        }

    }

    @BeforeEach
    void setUp() {
        terminal = new CLITest();
    }

    // ? <--initOptions flat-->

    @Test
    // standart
    void CLI00() {
        terminal.setKey(List.of(66, 66, 10)); // 0 1 2
        String[] actionList = { "0", "1", "2", "3" };
        int selected = terminal.initOptions(actionList, null, null);

        assertEquals(selected, 2);
    }

    @Test
    // up arrow
    void CLI01() {
        terminal.setKey(List.of(66, 65, 10)); // 0 1 2
        String[] actionList = { "0", "1", "2", "3" };
        int selected = terminal.initOptions(actionList, null, null);

        assertEquals(selected, 0);
    }

    @Test
    // to end
    void CLI02() {
        terminal.setKey(List.of(66, 66, 66, 66, 10));

        int test = terminal.initOptions(List.of("0", "1").toArray(), null, null);

        assertEquals(test, 1);
    }

    @Test
    // different values
    void CLI03() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        int test = terminal.initOptions(List.of("0", "1").toArray(), null, null);

        assertEquals(test, 1);
    }

    @Test
    // null options
    void CLI04() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        assertThrows(NullPointerException.class,
                () -> terminal.initOptions(null, null, null));
    }

    @Test
    // null option
    void CLI04_2() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        assertEquals(terminal.initOptions(new String[] { "sf", null, "dgg" }, null, null), -1);
    }

    @Test
    // Object option
    void CLI05() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        int test = terminal.initOptions((Object[]) (List.of("0", "1").toArray()), null, null);

        assertEquals(test, 1);
    }

    // ! не знаю і не уявляю як зробити щоб працювали (всі тести після них не
    // відбуваються)
    // @Test
    // // don't input anything
    // void CLI06() {
    // terminal.setKey(List.of());
    // try {
    // assertTimeoutPreemptively(Duration.ofSeconds(1),
    // () -> terminal.initOptions(List.of("0", "1").toArray(), null, null));
    // fail("Test should fail due to timeout");
    // } catch (AssertionError e) {
    // // * все ок
    // }

    // }

    // @Test
    // // don't input enter
    // void CLI07() {
    // terminal.setKey(List.of(34, 66, 65, 66, 66, 65));
    // try {
    // assertTimeoutPreemptively(Duration.ofSeconds(1),
    // () -> terminal.initOptions(List.of("0", "1").toArray(), null, null));
    // fail("Test should fail due to timeout");
    // } catch (AssertionError e) {
    // // * все ок
    // }

    // }

    @Test
    // input backspace while null
    void CLI08() {
        terminal.setKey(List.of(6, 636, 127, 166, 10));

        int test = terminal.initOptions((Object[]) (List.of("0", "1").toArray()), null, null);

        assertEquals(test, -1);
    }

    @Test
    // empty list
    // *відловив потенційну помилку
    void CLI09() {
        terminal.setKey(List.of(10));

        assertEquals(terminal.initOptions(new Integer[] {}, null, null), -1);
    }

    // ? <--initOptions horizontal-->

    @Test
    // standart
    void CLI10() {
        terminal.setKey(List.of(66, 66, 67, 67, 10));

        int[] selected = terminal.initOptions(
                List.of(0, 1, 2, 3).toArray(),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 2, 3 },
                null, null);

        assertArrayEquals(selected, new int[] { 2, 2 });
    }

    @Test
    // to end
    void CLI11() {
        terminal.setKey(List.of(66, 66, 66, 66, 66, 67, 67, 67, 67, 67, 67, 10));

        int[] selected = terminal.initOptions(
                List.of(0, 1, 2, 3).toArray(),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 2, 3 },
                null, null);

        assertArrayEquals(selected, new int[] { 3, 3 });
    }

    @Test
    // revert direction
    void CLI12() {
        terminal.setKey(List.of(67, 67, 67, 66, 66, 10));

        int[] selected = terminal.initOptions(
                List.of(0, 1, 2, 3).toArray(),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 2, 3 },
                null, null);

        assertArrayEquals(selected, new int[] { 2, 3 });
    }

    @Test
    // different values
    void CLI13() {
        terminal.setKey(List.of(6, 67, 636, 66, 166, 10));

        int[] selected = terminal.initOptions(
                List.of(0, 1, 2, 3).toArray(),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 2, 3 },
                null, null);

        assertArrayEquals(selected, new int[] { 1, 1 });
    }

    @Test
    // null options
    void CLI14() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        assertThrows(NullPointerException.class,
                () -> terminal.initOptions(null, null, new int[] { 0, 1, 32 }, null, null));
    }

    @Test
    // null option1
    void CLI14_2() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        assertArrayEquals(
                terminal.initOptions(new String[] { "sf", null, "dgg" }, null, new int[] { 1, 0, 2 }, null, null),
                new int[] { -1, -1 });
    }

    @Test
    // null option2
    void CLI14_3() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        assertArrayEquals(
                terminal.initOptions(new String[] { "sf", null, "dgg" }, new String[] { "sf", null, "dgg" },
                        new int[] { 0, 1, 2 }, null, null),
                new int[] { -1, -1 });
    }

    @Test
    // Object option
    void CLI15() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        int[] test = terminal.initOptions(
                (Object[]) (List.of("0", "1").toArray()),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 2, 3 },
                null, null);

        assertArrayEquals(test, new int[] { 1, 0 });

    }

    // ! не знаю і не уявляю як зробити щоб працювали (всі тести після них не
    // відбуваються)
    // @Test
    // // don't input anything
    // void CLI16() {
    // terminal.setKey(List.of());
    // try {
    // assertTimeoutPreemptively(Duration.ofSeconds(1),
    // () -> terminal.initOptions();
    // fail("Test should fail due to timeout");
    // } catch (AssertionError e) {
    // // * все ок
    // }

    // }

    // @Test
    // // don't input enter
    // void CLI17() {
    // terminal.setKey(List.of(34, 66, 65, 66, 66, 65));
    // try {
    // assertTimeoutPreemptively(Duration.ofSeconds(1),
    // () -> terminal.initOptions();
    // fail("Test should fail due to timeout");
    // } catch (AssertionError e) {
    // // * все ок
    // }

    // }

    @Test
    // input backspace while null
    void CLI18() {
        terminal.setKey(List.of(6, 67, 636, 66, 166, 127, 13));

        int[] selected = terminal.initOptions(
                List.of(0, 1, 2, 3).toArray(),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 2, 3 },
                null, null);

        assertArrayEquals(selected, new int[] { -1, -1 });
    }

    @Test
    // empty list
    void CLI19() {
        terminal.setKey(List.of(10));

        int[] selected = terminal.initOptions(
                new Integer[] {},
                new Integer[] {},
                new int[] { 0, 1, 2, 3 },
                null, null);

        assertArrayEquals(selected, new int[] { -1, -1 });
    }

    @Test
    // null options1
    void CLI20() {
        terminal.setKey(List.of(10));

        assertThrows(NullPointerException.class,
                () -> terminal.initOptions(null, List.of(0, 1, 2, 3).toArray(), new int[] { 0, 1, 32 }, null, null));
    }

    @Test
    // null options2
    void CLI21() {
        terminal.setKey(List.of(10));

        int[] selected = terminal.initOptions(List.of(0, 1, 2, 3).toArray(), null, new int[] { 0, 1, 32 }, null, null);

        assertArrayEquals(selected, new int[] { 0, -1 });

    }

    @Test
    // not used to
    void CLI22() {
        terminal.setKey(List.of(66, 66, 67, 10));

        int[] selected = terminal.initOptions(
                List.of(0, 1, 2, 3).toArray(),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 32 },
                null, null);

        assertArrayEquals(selected, new int[] { 2, -1 });

    }

    @Test
    // up arrow
    void CLI23() {
        terminal.setKey(List.of(66, 66, 65, 67, 10));

        int[] selected = terminal.initOptions(
                List.of(0, 1, 2, 3).toArray(),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 32 },
                null, null);

        assertArrayEquals(selected, new int[] { 1, 1 });

    }

    @Test
    // left arrow
    void CLI24() {
        terminal.setKey(List.of(66, 66, 67, 68, 65, 67, 68, 10));

        int[] selected = terminal.initOptions(
                List.of(0, 1, 2, 3).toArray(),
                List.of(0, 1, 2, 3).toArray(),
                new int[] { 0, 1, 32 },
                null, null);

        assertArrayEquals(selected, new int[] { 1, 0 });

    }

    // ? <--initOptions full-->

    private Integer[] opt1 = new Integer[] { 0, 1, 2, 3, 4, 5 };
    private String[][] opt2 = new String[][] {
            { "1", "2", "3" },
            { "2.1", "2.2", "2.3" },
            {},
            null,
            { null, "2", "3" },
            { "2.1", "2.2", "2.3" },
    };

    @Test
    // standart
    void CLI25() {
        terminal.setKey(List.of(66, 67, 10));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { 1, 1 });
    }

    @Test
    // to end
    void CLI26() {
        terminal.setKey(List.of(66, 66, 66, 66, 66, 66, 66, 67, 67, 67, 67, 67, 67, 10));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { 5, 2 });
    }

    @Test
    // revert direction
    void CLI27() {
        terminal.setKey(List.of(67, 67, 67, 66, 10));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { 1, 2 });
    }

    @Test
    // different values
    void CLI28() {
        terminal.setKey(List.of(6, 67, 636, 66, 166, 10));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { 1, 1 });
    }

    @Test
    // null options
    void CLI29() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        assertThrows(CLIException.class,
                () -> terminal.initOptions(null, null, null, null));
    }

    @Test
    // Object option
    void CLI30() {
        terminal.setKey(List.of(6, 636, 66, 166, 10));

        int[] test = terminal.initOptions(
                (Object[]) (List.of(opt1).toArray()),
                opt2,
                null, null);

        assertArrayEquals(test, new int[] { 1, 0 });

    }

    // ! не знаю і не уявляю як зробити щоб працювали (всі тести після них не
    // відбуваються)
    // @Test
    // // don't input anything
    // void CLI31() {
    // terminal.setKey(List.of());
    // try {
    // assertTimeoutPreemptively(Duration.ofSeconds(1),
    // () -> terminal.initOptions(opt1, opt2, null, null));
    // fail("Test should fail due to timeout");
    // } catch (AssertionError e) {
    // // * все ок
    // }

    // }

    // @Test
    // // don't input enter
    // void CLI32() {
    // terminal.setKey(List.of(34, 66, 65, 66, 66, 65));
    // try {
    // assertTimeoutPreemptively(Duration.ofSeconds(1),
    // () -> terminal.initOptions(opt1, opt2, null, null));
    // fail("Test should fail due to timeout");
    // } catch (AssertionError e) {
    // // * все ок
    // }

    // }

    @Test
    // input backspace while null
    void CLI33() {
        terminal.setKey(List.of(6, 67, 636, 66, 166, 127, 13));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { -1, -1 });
    }

    @Test
    // empty list
    void CLI34() {
        terminal.setKey(List.of(10));

        int[] selected = terminal.initOptions(
                new Integer[] {},
                new String[][] {},
                null, null);

        assertArrayEquals(selected, new int[] { -1, -1 });
    }

    @Test
    // null options1
    void CLI35() {
        terminal.setKey(List.of(10));

        assertThrows(CLIException.class,
                () -> terminal.initOptions(null, opt2, null, null));
    }

    @Test
    // null options2
    void CLI36() {
        terminal.setKey(List.of(10));

        assertThrows(CLIException.class,
                () -> terminal.initOptions(opt1, null, null, null));
    }

    @Test
    // null 2 optioN row
    void CLI37() {
        terminal.setKey(List.of(66, 66, 67, 10));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { -1, -1 });

    }

    @Test
    // null 2 optioN in row
    void CLI38() {
        terminal.setKey(List.of(66, 66, 66, 66, 10));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { -1, -1 });

    }

    @Test
    // null 1 optioN
    void CLI38_2() {
        terminal.setKey(List.of(66, 66, 67, 10));

        int[] selected = terminal.initOptions(new String[] { "dfg", "gfhgf", null, "gfh", null, null }, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { -1, -1 });

    }

    @Test
    // up arrow
    void CLI39() {
        terminal.setKey(List.of(66, 66, 65, 67, 10));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { 1, 1 });

    }

    @Test
    // left arrow
    void CLI40() {
        terminal.setKey(List.of(66, 66, 67, 68, 65, 67, 68, 10));

        int[] selected = terminal.initOptions(opt1, opt2,
                null, null);

        assertArrayEquals(selected, new int[] { 1, 0 });

    }

    // ? <--ELSE-->

    @Test
    // errorExit test
    void errorExit() {
        assertThrows(CLIException.class, () -> terminal.errorExit("test"));
    }

    @Test
    void formatName01() {
        assertNotEquals(terminal.formatName("test"), null);
    }

    @Test
    void formatName02() {
        assertEquals(terminal.formatName(null), terminal.formatName("--?--"));
    }

    @Test
    void colorize01() {
        assertNotEquals(terminal.colorize("test", Colors.BLUE, false), null);
    }

    @Test
    void colorize02() {
        assertEquals(terminal.colorize(null, Colors.BLUE, false), terminal.colorize("--?--", Colors.BLUE, false));
    }

    @Test
    void colorize03() {
        assertEquals(terminal.colorize("test", null, false), terminal.colorize("test", Colors.NONE, false));
    }

    @Test
    void colorize04() {
        assertEquals(terminal.colorize(null, null, false), terminal.colorize("--?--", Colors.NONE, false));
    }

    @Test
    void optionsLabel01(){
        assertNotEquals(terminal.optionsLabel("test"), null);
    }

    @Test
    void optionsLabel02(){
        assertEquals(terminal.optionsLabel(null), terminal.optionsLabel("--?--"));
    }
}
