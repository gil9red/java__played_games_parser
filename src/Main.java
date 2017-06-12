import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(Parser.parseGameName("Resident Evil 4, 5, 6"));
        System.out.println(Parser.parseGameName("Resident Evil 4,  5,   6"));
        System.out.println(Parser.parseGameName("Resident Evil 1-3"));
        System.out.println(Parser.parseGameName("Resident Evil 1 -  3"));
        System.out.println(Parser.parseGameName("Heroes of Might and Magic III, IV"));

//        Path path = FileSystems.getDefault().getPath("2017-06-08.txt");
//        byte[] bytes = Files.readAllBytes(path);
//        String text = new String(bytes);
//
//        Map<String, Map<String, List<String>>> platforms = Parser.parse(text);
//
//        System.out.println(platforms.keySet());
//        System.out.println(platforms);
//
//        import json
//        json.dump(platforms, open('games.json', mode='w', encoding='utf-8'), ensure_ascii=False, indent=4)
    }
}
