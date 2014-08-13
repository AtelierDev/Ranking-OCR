/*
 * Copyright (C) 2014 Nils Ryter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ranking;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Compare and rank a text document to a original document.
 * <p>
 * The comparison and ranking is done as follows: <br>
 * <ol>
 * <li>replace all charters which aren't letter and number by space, replace all
 * accented charters by un-accented charters. </li>
 * <li>Split the documents in words (based on the space) </li>
 * <li>Compare documents words pairwise. If the words don't match, try to sync
 * de two documents. </li>
 * </ol>
 * <p>
 * @author Nils Ryter
 */
public class SimpleRanker implements Ranker {

    //Number of word to check when comparing begin of the two documents
    private final int CHECK_RANGE = 6;
    //Document to compare
    private final LinkedList<String> comparative = new LinkedList();
    //Number of mistakes in compare document
    private int nbError = 0;
    //Number of word in original document
    private int nbWord = 0;
    //Original document
    private final LinkedList<String> original = new LinkedList();

    /**
     * Create a new SimpleRanker.
     */
    public SimpleRanker() {
    }

    @Override
    public double compare(String original, String comparative) {
        init(original, comparative);
        compare();
        return (double) nbError / nbWord * 100;
    }

    /**
     * Return the number of error encountered in the comparison.
     * <p>
     * @return Number of error
     */
    public int getNbError() {
        return nbError;
    }

    /**
     * Compare the original document to the comparative document.
     */
    private void compare() {
        //Compare original word to comparative word pairwise
        while (original.size() > 0 && comparative.size() > 0) {
            if (original.getFirst().equals(comparative.getFirst())) {
                //Same words, so remove them
                comparative.removeFirst();
                original.removeFirst();
            } else {
                //Try to sync the documents
                syncComparativeAndOriginal(CHECK_RANGE);
            }
        }
        //Count remaining words as error
        while (original.size() > 0) {
            original.removeFirst();
            ++nbError;
        }
        //Count remaining words as error
        while (comparative.size() > 0) {
            comparative.removeFirst();
            ++nbError;
        }
    }

    /**
     * Initialize the ranker with documents.
     * <p>
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
        clearTxt1 = removeAccents(clearTxt1);
        clearTxt2 = removeAccents(clearTxt2);
        //Split the string by space
        this.original.addAll(Arrays.asList(clearTxt1.split("[ ]+")));
        this.comparative.addAll(Arrays.asList(clearTxt2.split("[ ]+")));
        nbWord = this.original.size();
    }

    /**
     * Replace all accented charters by their un-accented charter.
     * <p>
     * @param s Text to convert
     * @return Text without accent
     */
    private String removeAccents(String s) {
        if (s == null) {
            return "";
        } else {
            return Normalizer.normalize(s, Form.NFD).replaceAll(
                    "\\p{InCombiningDiacriticalMarks}+", "");
        }
    }

    /**
     * Sync comparative document with original.
     * <p>
     * @param syncRange Number of word to check when comparing begin of the two
     * document
     */
    private void syncComparativeAndOriginal(int syncRange) {
        /*
         * Search in the comparative document if the current original value
         * appears, meaning the current value from comparative document is a
         * word which doesn't exist in the original document.
         */
        //First, find the current word in other document
        int posCinO = original.indexOf(comparative.getFirst());
        int posOinC = comparative.indexOf(original.getFirst());

        /*
         * Second, check if the less desctructive sync is : -aligned original to
         * comparative -aligned comparative to original -remove the word in both
         */
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
}
