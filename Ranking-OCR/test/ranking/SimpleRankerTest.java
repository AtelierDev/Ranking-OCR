
package ranking;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for SimpleRanker
 *
 * @author Nils Ryter
 */
public class SimpleRankerTest {
    
    /**
     * Test of class SimpleRanker.
     */
    @Test
    public void testSimpleRanker1() {
        System.out.println("test 1");
        String original = "Hello, dear i want to check your hand.\n But "
                + "warning! The *** is not for you";
        String comparative = "Hello, degr want to check youm hand.\n But "
                + "warning| The *** is not for you";
        SimpleRanker instance = new SimpleRanker();        
        double expResult = 3.f / 15 *100;
        double result = instance.compare(original, comparative);
        assertEquals(expResult, result, 1e-3);
        System.out.println("Errors: "+instance.getNbError());
    }

    /**
     * Test of class SimpleRanker.
     */
    @Test
    public void testSimpleRanker2() {
        System.out.println("test 2");
        String original = "GG FF HH ZZ UU II";
        String comparative = "GG v vv vv v  vv FF HH ZZ UU II";
        SimpleRanker instance = new SimpleRanker();        
        double expResult = 5.f / 6 *100;
        double result = instance.compare(original, comparative);
        assertEquals(expResult, result, 1e-3);
        System.out.println("Errors: "+instance.getNbError());
    }
    
    /**
     * Test of class SimpleRanker.
     */
    @Test
    public void testSimpleRanker3() {
        System.out.println("test 3");
        String original = "GG FF HH ZZ UU II";
        String comparative = "GG v ZZ v v FF HH ZZ UU II";
        SimpleRanker instance = new SimpleRanker();        
        double expResult = 4.f / 6 *100;
        double result = instance.compare(original, comparative);
        assertEquals(expResult, result, 1e-5);
        System.out.println("Errors: "+instance.getNbError());
    }
    
    /**
     * Test of class SimpleRanker.
     */
    @Test
    public void testSimpleRanker4() {
        System.out.println("test 4");
        String original = "GG FF HH ZZ UU II";
        String comparative = "GG v ZZ v v v v v FF ZZ UU II";
        SimpleRanker instance = new SimpleRanker();        
        double expResult = 11.f / 6 *100;
        double result = instance.compare(original, comparative);
        assertEquals(expResult, result, 1e-3);
        System.out.println("Errors: "+instance.getNbError());
    }
    
    /**
     * Test of class SimpleRanker.
     */
    @Test
    public void testSimpleRanker5() {
        System.out.println("test 5");
        String original = "GG FF HH ZZ UU II";
        String comparative = "GG HH FF ZZ UU II";
        SimpleRanker instance = new SimpleRanker();        
        double expResult = 2.f / 6 *100;
        double result = instance.compare(original, comparative);
        assertEquals(expResult, result, 1e-3);
        System.out.println("Errors: "+instance.getNbError());
    }
}
