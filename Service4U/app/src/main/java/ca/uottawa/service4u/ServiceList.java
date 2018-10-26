package ca.uottawa.service4u;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

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
        textViewRate.setText(String.valueOf(service.getRatePerHour()));
        return listViewItem;
    }
}

