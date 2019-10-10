package tf_29_actors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

class WordFrequencyController extends ActiveWFObject {

    private ActiveWFObject storageManager;

    @Override
    protected void dispatch(Object[] message) {
        if ("run".equals(message[0])) {
            run(Arrays.copyOfRange(message, 1, message.length));
        } else if ("top25".equals(message[0])) {
            display(Arrays.copyOfRange(message, 1, message.length));
        } else {
            throw new RuntimeException("Message not understood " + message[0]);
        }
    }

    private void run(Object[] message) {
        storageManager = (ActiveWFObject) message[0];
        send(storageManager, new Object[]{"send_word_freqs", this});
    }

    private void display(Object[] message) {
        List<Map.Entry<String, Integer>> wordFreqs = (List<Map.Entry<String, Integer>>) message[0];
        wordFreqs.forEach(e -> System.out.println(e.getKey() + " - " + e.getValue()));
        send(storageManager, new Object[]{"die"});
        this.stopMe = true;
    }
}
