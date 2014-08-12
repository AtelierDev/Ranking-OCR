package ranking;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Compare and rank a text document to a original document.
 *
 * @author Nils Ryter
 */
public class SimpleRanker implements Ranker {

    /**
     * Create a new SimpleRanker
     */
    public SimpleRanker() {
    }

    @Override
    public double compare(String s1, String s2) {
        init(s1, s2);
        compare();
        return (double) nbError / nbWord * 100;
    }

    /**
     * Return the number of error encountered in the comparison.
     *
     * @return Number of error
     */
    public int getNbError() {
        return nbError;
    }

    /**
     * Initialize the ranker with documents.
     *
     * @param original Original document
     * @param comparative Document to compare to original
     */
    private void init(String original, String comparative) {
        //Clear the context
        this.comparative.clear();
        this.original.clear();
        nbError = 0;
        //Clear the string
        String clearTxt1 = original.replaceAll("[^0-9a-zA-Z]+", " ");
        String clearTxt2 = comparative.replaceAll("[^0-9a-zA-Z]+", " ");
        clearTxt1 = clearTxt1.replaceAll("[éèêë]", "e");
        clearTxt2 = clearTxt2.replaceAll("[éèêë]", "e");
        clearTxt1 = clearTxt1.replaceAll("[àäâ]", "a");
        clearTxt2 = clearTxt2.replaceAll("[àäâ]", "a");
        //Split the string by space
        this.original.addAll(Arrays.asList(clearTxt1.split("[ ]+")));
        this.comparative.addAll(Arrays.asList(clearTxt2.split("[ ]+")));
        nbWord = this.original.size();
    }

    /**
     * Compare the original document to the comparative document.
     */
    private void compare() {
        while (original.size() > 0 && comparative.size() > 0) {
            if (original.getFirst().equals(comparative.getFirst())) {
                comparative.removeFirst();
                original.removeFirst();
            } else {
                syncComparativeToOriginal(CHECK_RANGE);
            }
        }
        while (original.size() > 0) {
            original.removeFirst();
            ++nbError;
        }
        while (comparative.size() > 0) {
            comparative.removeFirst();
            ++nbError;
        }
    }

    /**
     * Sync comparative document to original.
     *
     * @param syncRange Number of word to check when comparing begin of the two
     * document
     */
    private void syncComparativeToOriginal(int syncRange) {
        /* Search in the comparative document if the current original value 
         * appears, meaning the current value from comparative document 
         * is a word which doesn't exist in the original document.
         */
        int posCinO = original.indexOf(comparative.getFirst());
        int posOinC = comparative.indexOf(original.getFirst());
        if (posCinO <= syncRange && posCinO >= 0
                && (posOinC >= 0 && posCinO < posOinC || posOinC < 0)) {
            //Sync between the both document
            for (int i = posCinO; i > 0; --i) {
                original.removeFirst();
                ++nbError;
            }
        } else if (posOinC <= syncRange && posOinC >= 0
                && (posCinO >= 0 && posOinC < posCinO || posCinO < 0)) {
            for (int i = posOinC; i > 0; --i) {
                comparative.removeFirst();
                ++nbError;
            }
        } else {
            comparative.removeFirst();
            original.removeFirst();
            ++nbError;
        }
    }

    //Number of word to check when comparing begin of the two documents
    public final int CHECK_RANGE = 6;
    //Number of word in original document
    private int nbWord = 0;
    //Number of mistakes in compare document
    private int nbError = 0;
    //Document to compare
    private final LinkedList<String> comparative = new LinkedList();
    //Original document
    private final LinkedList<String> original = new LinkedList();
}
