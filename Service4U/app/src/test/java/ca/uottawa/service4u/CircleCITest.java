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

    public void UserTest(){

        User u1=new User("10000", "John", "Smith", "customer", "12345678910", "23 lane road");
        assertEquals("Checking phone number", "12345678910", u1.getphoneNumber());
    }


}
