package ca.uottawa.service4u;

import org.junit.Test;

import static org.junit.Assert.*;

public class CircleCITest {
    MainActivity mA1=new MainActivity();
    @Test
    public void MainActivityMethodsTest(){
        mA1.myJobs();
        mA1.bookJob();
        mA1.allServices();

    }
}
