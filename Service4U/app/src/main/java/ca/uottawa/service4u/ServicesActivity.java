package ca.uottawa.service4u;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity {
    private static final String dbTAG = "Database";

    private EditText sNameField;
    private EditText sTypeField;
    private EditText sRateField;


    ListView listViewServices;
    DatabaseReference databaseServices;
    List<Service> services;

    String currentServiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseServices = FirebaseDatabase.getInstance().getReference("Services");

        services = new ArrayList<>();

        // Views
        sNameField = findViewById(R.id.nameField);
        sTypeField = findViewById(R.id.typeField);
        sRateField = findViewById(R.id.rateField);

        // Buttons
        findViewById(R.id.addServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addService();
            }
        });

        findViewById(R.id.updateServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateService(currentServiceId);
            }
        });

        findViewById(R.id.deleteServiceBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService(currentServiceId);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newServiceBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.listServicesLayout).setVisibility(View.GONE);
                findViewById(R.id.newServiceBtn).setVisibility(View.GONE);
                findViewById(R.id.editServiceLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.updateDeleteButtons).setVisibility(View.GONE);
                findViewById(R.id.addServiceBtn).setVisibility(View.VISIBLE);
            }
        });

        listViewServices = (ListView) findViewById(R.id.listViewServices);
        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Service service = services.get(i);
                currentServiceId = service.getId();
                updateDeleteService(service);
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                services.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service service = postSnapshot.getValue(Service.class);
                    services.add(service);
                }

                ServiceList servicesAdapter = new ServiceList(ServicesActivity.this, services);
                listViewServices.setAdapter(servicesAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateDeleteService(Service service) {
        findViewById(R.id.listServicesLayout).setVisibility(View.GONE);
        findViewById(R.id.addServiceBtn).setVisibility(View.GONE);
        findViewById(R.id.editServiceLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.updateDeleteButtons).setVisibility(View.VISIBLE);
        findViewById(R.id.addServiceBtn).setVisibility(View.GONE);

        sNameField.setText(service.getName());
        sTypeField.setText(service.getType());
        sRateField.setText(String.valueOf(service.getRatePerHour()));
    }

    private boolean validateFields(){
        //TODO

        return true;
    }

    private void updateService(String id) {

        if (validateFields()) {

            Service service = readFields();

            DatabaseReference dR = databaseServices.child(id);
            dR.setValue(service);

            Toast.makeText(getApplicationContext(), "Service updated", Toast.LENGTH_LONG).show();
            doneEdit();
        }
    }

    private void deleteService(String id) {
        DatabaseReference dR = databaseServices.child(id);
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Service deleted", Toast.LENGTH_LONG).show();
        doneEdit();
    }

    private void addService() {

        if (validateFields()){

            Service service = readFields();
            String id = databaseServices.push().getKey();
            service.setId(id);
            databaseServices.child(id).setValue(service);

            Toast.makeText(this, "Service added", Toast.LENGTH_LONG).show();
            doneEdit();
        }

    }

    public Service readFields(){

        Service service = new Service();

        String name = sNameField.getText().toString();
        String type = sTypeField.getText().toString();
        double rate = Double.parseDouble(sRateField.getText().toString());

        service.setId(currentServiceId);
        service.setName(name);
        service.setRatePerHour(rate);
        service.setType(type);

        return service;
    }

    public void doneEdit(){
        findViewById(R.id.listServicesLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.newServiceBtn).setVisibility(View.VISIBLE);
        findViewById(R.id.editServiceLayout).setVisibility(View.GONE);

        sNameField.setText("");
        sTypeField.setText("");
        sRateField.setText("");
    }

}
