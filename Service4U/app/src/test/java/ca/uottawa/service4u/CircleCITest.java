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

    @Test
    public void UserTest(){

        User u1=new User("10000", "John", "Smith", "customer", "12345678910", "23 lane road");
        assertEquals("Checking phone number", "12345678910", u1.getphoneNumber());
    }

    @Test
    public void ServiceProviderTest(){

        ServiceProvider sp1= new ServiceProvider();
        assertEquals("Checking super-firstName of user", "", sp1.getfirstName());
    }

    /*
    public void JobTest(){

        Job j1= new Job("123", "Painting", "321", "567", 2018, 06, 19, 60000066321L, 600000023452L, 5, 13.50, "91011", "Bring pizza");
        assertEquals("Checking price", "13.50", Double.toString(j1.getTotalPrice()));
    }*/

    @Test
    public void PotentialJobMethodTest(){

        PotentialJob pj1=new PotentialJob(10000000L, 15000000L, "John", "Doe", "12345", 5.0);
        assertEquals("Checking describeContents", "0", Double.toString(pj1.describeContents()));


    }


}
