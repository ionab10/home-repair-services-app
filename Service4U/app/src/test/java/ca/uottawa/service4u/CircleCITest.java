package ca.uottawa.service4u;

import org.junit.Test;

import static org.junit.Assert.*;

public class CircleCITest {
    MainActivity mA1=new MainActivity();


    @Test
    public void ServiceTest(){

        Service aS=new Service("1", "Virus Removal","IT", 15.5);
        assertEquals("Checking id", "1", aS.getId());
    }
}
