package tf_29_actors;

class Main {
    public static void main(String[] args) {
        ActiveWFObject wordFreqManager = new WordFrequencyManager();

        ActiveWFObject stopWordManager = new StopWordManager();
        ActiveWFObject.send(stopWordManager, new Object[]{"init", wordFreqManager});

        ActiveWFObject storageManager = new DataStorageManager();
        ActiveWFObject.send(storageManager, new Object[]{"init", args[0], stopWordManager});

        ActiveWFObject wordFreqController = new WordFrequencyController();
        ActiveWFObject.send(wordFreqController, new Object[]{"run", storageManager});

        // Wait for the active objects to finish
        try {
            wordFreqManager.join();
            stopWordManager.join();
            storageManager.join();
            wordFreqController.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
