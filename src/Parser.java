import java.util.*;

public class Parser {
    public final static String FINISHED_GAME = "FINISHED_GAME";
    public final static String NOT_FINISHED_GAME = "NOT_FINISHED_GAME";
    public final static String FINISHED_WATCHED = "FINISHED_WATCHED";
    public final static String NOT_FINISHED_WATCHED = "NOT_FINISHED_WATCHED";

    public static Map<String, Map<String, List<String>>> parse(String text) {
        Map<String, Map<String, List<String>>> platforms = new LinkedHashMap<>();
        Map<String, List<String>> platform = null;

        for (String line : text.split("\n")) {
            // Analog rtrim / strip
            line = line.replaceAll("\\s+$","");
            if (line.isEmpty())
                continue;

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

            if (platform == null)
                continue;

            final String flag = line.substring(0, 2);
            List<String> games = parseGameName(line.substring(2));

            if (flag.equals("  "))
                platform.get(FINISHED_GAME).addAll(games);

            else if (flag.equals(" -") || flag.equals("- "))
                platform.get(NOT_FINISHED_GAME).addAll(games);

            else if (flag.equals(" @") || flag.equals("@ "))
                platform.get(FINISHED_WATCHED).addAll(games);

            else if (flag.equals("@-") || flag.equals("-@"))
                platform.get(NOT_FINISHED_WATCHED).addAll(games);
        }

        return platforms;
    }

    public static List<String> parseGameName(String game) {

/*
    # Регулярка вытаскивает выражения вида: 1, 2, 3 или 1-3, или римские цифры: III, IV
    import re
    PARSE_GAME_NAME_PATTERN = re.compile(r'(\d+(, *?\d+)+)|(\d+ *?- *?\d+)|([MDCLXVI]+(, ?[MDCLXVI]+)+)',
                                         flags=re.IGNORECASE)

    def parse_game_name(game_name):
        """
        Функция принимает название игры и пытается разобрать его, после возвращает список названий.
        Т.к. в названии игры может находиться указание ее частей, то функция разберет их.

        Пример:
            "Resident Evil 4, 5, 6" станет:
                ["Resident Evil 4", "Resident Evil 5", "Resident Evil 6"]

            "Resident Evil 1-3" станет:
                ["Resident Evil", "Resident Evil 2", "Resident Evil 3"]

        """

        match = PARSE_GAME_NAME_PATTERN.search(game_name)
        if match is None:
            return [game_name]

        seq_str = match.group(0)
        short_name = game_name.replace(seq_str, '').strip()

        if ',' in seq_str:
            seq = seq_str.replace(' ', '').split(',')

        elif '-' in seq_str:
            seq = seq_str.replace(' ', '').split('-')
            if len(seq) == 2:
                seq = tuple(map(int, seq))
                seq = tuple(range(seq[0], seq[1] + 1))
        else:
            return [game_name]

        # Сразу проверяем номер игры в серии и если она первая, то не добавляем в названии ее номер
        return [short_name if str(num) == '1' else '{} {}'.format(short_name, num) for num in seq]
*/

        return Arrays.asList(game);
    }
}
