import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Main {
    public static void main(String[] args) throws Exception {
        Path path = FileSystems.getDefault().getPath("2017-06-08.txt");
        byte[] bytes = Files.readAllBytes(path);
        String text = new String(bytes);

        Map<String, Map<String, List<String>>> platforms = Parser.parse(text);
        System.out.println("Platforms: " + platforms.size());

        int totalGames = 0;
        for (Map<String, List<String>> categories : platforms.values()) {
            for (List games : categories.values()) {
                totalGames += games.size();
            }
        }
        System.out.println("Games: " + totalGames);
        System.out.println();
        System.out.println(platforms.keySet());
        System.out.println(platforms);

//        import json
//        json.dump(platforms, open('games.json', mode='w', encoding='utf-8'), ensure_ascii=False, indent=4)
    }
}
