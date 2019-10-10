package tf_29_actors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * Keeps the word frequency data
 */
class WordFrequencyManager extends ActiveWFObject {

    private Map<String, Integer> wordFreqs = new HashMap<>();

    @Override
    protected void dispatch(Object[] message) {
        if ("word".equals(message[0])) {
            incrementCount(Arrays.copyOfRange(message, 1, message.length));
        } else if ("top25".equals(message[0])) {
            top25(Arrays.copyOfRange(message, 1, message.length));
        }
    }

    private void incrementCount(Object[] message) {
        String word = (String) message[0];
        wordFreqs.merge(word, 1, Integer::sum);
    }

    private void top25(Object[] message) {
        ActiveWFObject recipient = (ActiveWFObject) message[0];
        List<Map.Entry<String, Integer>> freqsSorted = wordFreqs.entrySet().stream()
                .sorted(comparing(Map.Entry<String, Integer>::getValue).reversed())
                .limit(25)
                .collect(Collectors.toList());
        send(recipient, new Object[]{"top25", freqsSorted});
    }
}
