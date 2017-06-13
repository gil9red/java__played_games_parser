import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Класс для парсинга списка игр.</p>
 */
public class Parser {
    public final static String FINISHED_GAME = "FINISHED_GAME";
    public final static String NOT_FINISHED_GAME = "NOT_FINISHED_GAME";
    public final static String FINISHED_WATCHED = "FINISHED_WATCHED";
    public final static String NOT_FINISHED_WATCHED = "NOT_FINISHED_WATCHED";

    // Регулярка вытаскивает выражения вида: 1, 2, 3 или 1-3, или римские цифры: III, IV
    private final static Pattern PARSE_GAME_NAME_PATTERN = Pattern.compile("(\\d+(, *?\\d+)+)|(\\d+ *?- *?\\d+)|([MDCLXVI]+(, ?[MDCLXVI]+)+)", Pattern.CASE_INSENSITIVE);

    /**
     * <p>Функция парсит переданный текст с списком игр.</p>
     *
     * @param text Текст с списком игр
     * @return Словарь платформ с словарем категорий с списком игр.
     */
    public static Map<String, Map<String, List<String>>> parse(String text) {
        Map<String, Map<String, List<String>>> platforms = new LinkedHashMap<>();
        Map<String, List<String>> platform = null;

        for (String line : text.split("\n")) {
            // Analog rtrim / strip
            line = line.replaceAll("\\s+$","");
            if (line.isEmpty()) {
                continue;
            }

            boolean hasFlag1 = " -@".contains(String.valueOf(line.charAt(0)));
            boolean hasFlag2 = " -@".contains(String.valueOf(line.charAt(1)));

            if (!hasFlag1 && !hasFlag2 && line.endsWith(":")) {
                String platformName = line.substring(0, line.length() - 1);

                platform = new LinkedHashMap<>();
                platform.put(FINISHED_GAME, new LinkedList<>());
                platform.put(NOT_FINISHED_GAME, new LinkedList<>());
                platform.put(FINISHED_WATCHED, new LinkedList<>());
                platform.put(NOT_FINISHED_WATCHED, new LinkedList<>());

                platforms.put(platformName, platform);

                continue;
            }

            if (platform == null) {
                continue;
            }

            final String flag = line.substring(0, 2);
            String categoryName = null;

            if (flag.equals("  ")) {
                categoryName = FINISHED_GAME;

            } else if (flag.equals(" -") || flag.equals("- ")) {
                categoryName = NOT_FINISHED_GAME;

            } else if (flag.equals(" @") || flag.equals("@ ")) {
                categoryName = FINISHED_WATCHED;

            } else if (flag.equals("@-") || flag.equals("-@")) {
                categoryName = NOT_FINISHED_WATCHED;
            }

            if (categoryName == null) {
                System.out.println(String.format("Странный формат строки: \"%s\"", line));
                continue;
            }

            List<String> category = platform.get(categoryName);

            List<String> games = parseGameName(line.substring(2));
            for (String game : games) {
                if (category.contains(game)) {
                    System.out.println(String.format("Предотвращено добавление дубликата игры \"%s\"", game));
                    continue;
                }

                category.add(game);
            }
        }

        return platforms;
    }

    /**
     * <p>Функция принимает название игры и пытается разобрать его, после возвращает список названий.</p>
     * <p>У некоторых игр в названии может указываться ее части или диапазон частей, поэтому для правильного
     * составления списка игр такие случаи нужно обрабатывать.</p>
     * <p></p>
     * <p>Пример:</p>
     * <pre>
     * "Resident Evil 4, 5, 6" -> ["Resident Evil 4", "Resident Evil 5", "Resident Evil 6"]
     * "Resident Evil 1-3"     -> ["Resident Evil", "Resident Evil 2", "Resident Evil 3"]
     * "Resident Evil 4"       -> ["Resident Evil 4"]
     * </pre>
     *
     * @param gameName Вертикаль, на которой находится фигура (1=a, 8=h)
     * @return Список названий игр.
     */
    private static List<String> parseGameName(String gameName) {
        Matcher match = PARSE_GAME_NAME_PATTERN.matcher(gameName);
        if (!match.find()) {
            return Collections.singletonList(gameName);
        }

        String seqStr = match.group();

        // "Resident Evil 4,  5,   6" -> "Resident Evil"
        // For not valid "Trollface Quest 1-7-8" -> "Trollface Quest"
        int index = gameName.indexOf(seqStr);
        String baseName = gameName.substring(0, index).trim();

        // "4,  5,   6" -> "4,5,6"
        seqStr = seqStr.replace(" ", "");

        List<String> seq = new ArrayList<>();

        if (seqStr.contains(",")) {
            Collections.addAll(seq, seqStr.split(","));

        } else if (seqStr.contains("-")) {
            // "1-7" -> ["1", "7"] -> ["1", "2", "3", "4", "5", "6", "7"]
            String[] parts = seqStr.split("-");

            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);

            for (; a <= b; a++) {
                seq.add(String.valueOf(a));
            }

        } else {
            return Collections.singletonList(gameName);
        }

        List<String> games = new ArrayList<>();

        for (String num : seq) {
            if (num.equals("1")) {
                games.add(baseName);
            } else {
                // Example: "Resident Evil" + " " + "4"
                games.add(baseName + " " + num);
            }
        }

        return games;
    }
}
