package ca.uottawa.service4u;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);


        //TODO Read database for list of services and make checkboxes
    }



    public void associateServices(){
        //databaseUsers.child(user.getUID()).child(“services”).setValue(listOfServices)
    }
}
