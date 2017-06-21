import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("From local file:");
        String textLocalFile = getTextFromLocalFile();
        processText(textLocalFile);

        System.out.println("\n\n");

        System.out.println("From url file:");
        String textUrlFile = getTextFromUrlFile();
        processText(textUrlFile);
    }

    static void processText(String text) {
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
    }

    static String getTextFromLocalFile() throws Exception {
        Path path = FileSystems.getDefault().getPath("2017-06-08.txt");
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }

    static String getTextFromUrlFile() throws Exception {
        Function<String, String> getHtml = urlStr -> {
            StringBuilder resultHtml = new StringBuilder();

            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                char data[] = new char[1024];
                int count;
                while ((count = rd.read(data)) != -1) {
                    resultHtml.append(data, 0, count);
                }

                rd.close();

            } catch (Exception e) {
                return null;
            }

            return resultHtml.toString();
        };

        String urlGist = "https://gist.github.com/gil9red/2f80a34fb601cd685353";
        String html = getHtml.apply(urlGist);

        Pattern p = Pattern.compile("/gil9red/2f80a34fb601cd685353/raw/.+/gistfile1.txt");
        Matcher match = p.matcher(html);
        if (!match.find()) {
            return null;
        }

        String url = "https://gist.github.com" + match.group();
        String text = getHtml.apply(url);
        return text;
    }
}
