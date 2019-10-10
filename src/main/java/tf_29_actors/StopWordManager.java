package tf_29_actors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Models the stop word filter
 */
class StopWordManager extends ActiveWFObject {

    private Set<String> stopWords = new HashSet<>();
    private ActiveWFObject wordFreqsManager;

    @Override
    protected void dispatch(Object[] message) {
        if ("init".equals(message[0])) {
            init(Arrays.copyOfRange(message, 1, message.length));
        } else if ("filter".equals(message[0])) {
            filter(Arrays.copyOfRange(message, 1, message.length));
        } else {
            // forward
            send(wordFreqsManager, message);
        }
    }

    private void init(Object[] message) {
        Path path = Paths.get("src/main/resources", "stop_words.txt");
        try {
            byte[] bytes = Files.readAllBytes(path);
            String s = new String(bytes, StandardCharsets.UTF_8);
            stopWords.addAll(Arrays.asList(s.split(",")));
            for (char ch = 'a'; ch <= 'z'; ch++) {
                stopWords.add(String.valueOf(ch));
            }
            wordFreqsManager = (ActiveWFObject) message[0];
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void filter(Object[] message) {
        String word = (String) message[0];
        if (!stopWords.contains(word)) {
            send(wordFreqsManager, new Object[]{"word", word});
        }
    }
}
