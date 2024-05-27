package sejong.teemo.champion.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChampionImageDownloader {

    private static final String BASE_URL = "https://ddragon.leagueoflegends.com/cdn/14.10.1/img/champion/";
    private static final String SAVE_DIRECTORY = "/Users/hayoon/Desktop/champions/";

    public void download(Map<Integer, String> champions) throws Exception {

        Files.createDirectories(Paths.get(SAVE_DIRECTORY));

        for (Map.Entry<Integer, String> entry : champions.entrySet()) {
            String championName = entry.getValue().replaceAll("\\s+","");
            String imageUrl = BASE_URL + championName + ".png";

            String championId = String.valueOf(entry.getKey());
            String savePath = SAVE_DIRECTORY + championId + ".png";

            downloadImage(imageUrl, savePath);
        }
    }

    private static void downloadImage(String imageUrl, String savePath) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(savePath)) {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        }
    }
}

