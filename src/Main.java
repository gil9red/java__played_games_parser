import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/*
def parse_played_games(text: str) -> dict:
    """
    Функция для парсинга списка игр.

    """

    FINISHED_GAME = 'FINISHED_GAME'
    NOT_FINISHED_GAME = 'NOT_FINISHED_GAME'
    FINISHED_WATCHED = 'FINISHED_WATCHED'
    NOT_FINISHED_WATCHED = 'NOT_FINISHED_WATCHED'

    # Регулярка вытаскивает выражения вида: 1, 2, 3 или 1-3, или римские цифры: III, IV
    import re
    PARSE_GAME_NAME_PATTERN = re.compile(r'(\d+(, ?\d+)+)|(\d+ *?- *?\d+)|([MDCLXVI]+(, ?[MDCLXVI]+)+)',
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

    from collections import OrderedDict
    platforms = OrderedDict()
    platform = None

    for line in text.splitlines():
        line = line.rstrip()
        if not line:
            continue

        if line[0] not in ' -@' and line[1] not in ' -@' and line.endswith(':'):
            platform_name = line[:-1]

            platform = {
                FINISHED_GAME: list(),
                NOT_FINISHED_GAME: list(),
                FINISHED_WATCHED: list(),
                NOT_FINISHED_WATCHED: list(),
            }
            platforms[platform_name] = platform

            continue

        if not platform:
            continue

        flag = line[:2]
        games = parse_game_name(line[2:])

        if flag == '  ':
            platform[FINISHED_GAME] += games

        elif flag == ' -' or flag == '- ':
            platform[NOT_FINISHED_GAME] += games

        elif flag == ' @' or flag == '@ ':
            platform[FINISHED_WATCHED] += games

        elif flag == '@-' or flag == '-@':
            platform[NOT_FINISHED_WATCHED] += games

    return platforms


if __name__ == '__main__':
    text = open('2017-06-08.txt', encoding='utf-8').read()
    platforms = parse_played_games(text)
    print(', '.join(platforms.keys()))

    import json
    json.dump(platforms, open('games.json', mode='w', encoding='utf-8'), ensure_ascii=False, indent=4)
*/


public class Main {
    public static void main(String[] args) throws Exception {
        Path path = FileSystems.getDefault().getPath("2017-06-08.txt");
        byte[] bytes = Files.readAllBytes(path);
        String text = new String(bytes);


        final String FINISHED_GAME = "FINISHED_GAME";
        final String NOT_FINISHED_GAME = "NOT_FINISHED_GAME";
        final String FINISHED_WATCHED = "FINISHED_WATCHED";
        final String NOT_FINISHED_WATCHED = "NOT_FINISHED_WATCHED";

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
//            List<String> games = parse_game_name(line[2:]);
            List<String> games = Arrays.asList(line.substring(2));

            if (flag.equals("  "))
                platform.get(FINISHED_GAME).addAll(games);

            else if (flag.equals(" -") || flag.equals("- "))
                platform.get(NOT_FINISHED_GAME).addAll(games);

            else if (flag.equals(" @") || flag.equals("@ "))
                platform.get(FINISHED_WATCHED).addAll(games);

            else if (flag.equals("@-") || flag.equals("-@"))
                platform.get(NOT_FINISHED_WATCHED).addAll(games);
        }

        System.out.println(platforms.keySet());
        System.out.println(platforms);
    }
}
