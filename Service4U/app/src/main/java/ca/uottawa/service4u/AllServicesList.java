package ca.uottawa.service4u;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class AllServicesList extends ArrayAdapter<Service> {
    private Activity context;
    private List<Service> allServices;
    private List<Service> myServices;

    public AllServicesList(Activity context, List<Service> allServices, List<Service> myServices) {
        super(context, R.layout.layout_all_services_list, allServices);
        this.context = context;
        this.allServices = allServices;
        this.myServices = myServices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.layout_all_services_list, null, true);
        Service service = allServices.get(position);


        CheckBox checkBox = (CheckBox) listViewItem.findViewById(R.id.serviceCB);
        checkBox.setText(service.getName());

        TextView serviceTypeText = (TextView) listViewItem.findViewById(R.id.serviceTypeText);
        serviceTypeText.setText(service.getType());

        TextView serviceRateText = (TextView) listViewItem.findViewById(R.id.serviceRateText);
        serviceRateText.setText(String.valueOf(service.getRatePerHour()));

        checkBox.setChecked(false);

        if (myServices != null) {
            if (myServices.contains(service)) {
                checkBox.setChecked(true);
            }
        }

        return listViewItem;
    }
}
