package tf_29_actors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Models the contents of the file
 */
class DataStorageManager extends ActiveWFObject {

    private String data;
    private ActiveWFObject stopWordManager;

    @Override
    protected void dispatch(Object[] message) {
        if ("init".equals(message[0])) {
            init(Arrays.copyOfRange(message, 1, message.length));
        } else if ("send_word_freqs".equals(message[0])) {
            processWords(Arrays.copyOfRange(message, 1, message.length));
        } else {
            // forward
            send(stopWordManager, message);
        }
    }

    private void init(Object[] message) {
        String pathToFile = (String) message[0];
        stopWordManager = (ActiveWFObject) message[1];
        Path path = Paths.get("src/main/resources", pathToFile);
        try {
            byte[] bytes = Files.readAllBytes(path);
            String s = new String(bytes, StandardCharsets.UTF_8);
            data = s.replaceAll("[\\W_]+", " ").toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void processWords(Object[] message) {
        ActiveWFObject recipient = (ActiveWFObject) message[0];
        String[] words = data.split("\\s+");
        for (String w : words) {
            send(stopWordManager, new Object[]{"filter", w});
        }
        send(stopWordManager, new Object[]{"top25", recipient});
    }
}
