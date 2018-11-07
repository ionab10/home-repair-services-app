package ca.uottawa.service4u;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    MainActivity mA1=new MainActivity();
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void MainActivityMethodsTest(){
        mA1.myJobs();
        mA1.bookJob();
        mA1.allServices();

    }
}

