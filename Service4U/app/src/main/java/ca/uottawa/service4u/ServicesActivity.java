package ca.uottawa.service4u;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServicesActivity extends AppCompatActivity {
    private static final String dbTAG = "Database";

    private EditText sNameField;
    private Spinner sTypeField;
    private EditText sRateField;

    ListView listViewServices;
    DatabaseReference databaseServices;
    List<Service> services;

    String currentServiceType;

    String currentServiceId;

    List<String> serviceTypesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        databaseServices = FirebaseDatabase.getInstance().getReference("Services");

        services = new ArrayList<>();
        serviceTypesArray = Arrays.asList(getResources().getStringArray(R.array.service_type_array));

        // Views
        sNameField = findViewById(R.id.nameField);
        sTypeField = findViewById(R.id.typeField);
        sRateField = findViewById(R.id.rateField);
        listViewServices = (ListView) findViewById(R.id.listViewServices);

        //Service Type spinner
        ArrayAdapter<CharSequence> serviceTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.service_type_array, android.R.layout.simple_spinner_item);
        // Create an ArrayAdapter using the string array and a default spinner layout
        serviceTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sTypeField.setAdapter(serviceTypeAdapter);
        currentServiceType = serviceTypeAdapter.getItem(0).toString();

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

        sTypeField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                currentServiceType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("item", "none");
            }
        });


        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Service service = services.get(position);
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

                Collections.sort(services, (Service s1, Service s2) ->{
                    return s1.toString().compareToIgnoreCase(s2.toString());
                });

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
        sTypeField.setSelection(serviceTypesArray.indexOf(service.getType()));
        sRateField.setText(String.valueOf(service.getRatePerHour()));
    }

    private boolean validateFields(int addType){

        //addType 0 for a regular add, 1 for update
        double min = 10.0;
        double max = 500.0;
        boolean validFlag = true;
        String name = sNameField.getText().toString();
        String rateasString = sRateField.getText().toString();

        if(TextUtils.isEmpty(name)){
            sNameField.setError("Required");
            validFlag = false;
        }

        if(TextUtils.isEmpty(rateasString)){
            sRateField.setError("Required");
            validFlag = false;
        } else {
            double rate = Double.parseDouble(sRateField.getText().toString());
            if(rate<min || rate>max ) {
            sRateField.setError("Out of range");
            validFlag = false;
            }
        }

        if(addType ==0){
            for(int i =0; i<services.size(); i++){
                if(services.get(i).getName().equals(name)){
                    sNameField.setError("A service by that name already exists");
                    validFlag = false;
                    return validFlag;
                }
            }
        }


        return validFlag;
    }

    private void updateService(String id) {

        if (validateFields(1)) {

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

        if (validateFields(0)){

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
        String type = currentServiceType;

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
        sTypeField.setSelection(-1);
        sRateField.setText("");
    }


    public class ServiceList extends ArrayAdapter<Service> {
        private Activity context;
        List<Service> services;

        public ServiceList(Activity context, List<Service> services) {
            super(context, R.layout.layout_service_list, services);
            this.context = context;
            this.services = services;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_service_list, null, true);

            TextView textViewName = (TextView) listViewItem.findViewById(R.id.serviceNameText);
            TextView textViewType = (TextView) listViewItem.findViewById(R.id.serviceTypeText);
            TextView textViewRate = (TextView) listViewItem.findViewById(R.id.serviceRateText);

            Service service = services.get(position);
            textViewName.setText(service.getName());
            textViewType.setText(service.getType());
            textViewRate.setText(String.format("%.2f",service.getRatePerHour()));
            return listViewItem;
        }
    }



}
