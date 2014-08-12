package ranking;

/**
 * Common interface of the rankers.
 *
 * @author Nils Ryter
 */
public interface Ranker {

    /**
     * Compare the documents
     *
     * @param s1 Original document
     * @param s2 Document to compare to original
     * @return Error rate in percent
     */
    public double compare(String s1, String s2);
}
