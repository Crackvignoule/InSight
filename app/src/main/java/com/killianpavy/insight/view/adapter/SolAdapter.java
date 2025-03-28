package com.killianpavy.insight.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.killianpavy.insight.R;
import com.killianpavy.insight.model.Sol;

public class SolAdapter extends ArrayAdapter<Sol> {

    private Context context;
    private List<Sol> sols;
    private LayoutInflater inflater;


    public SolAdapter(Context context, List<Sol> sols) {
        super(context, -1, sols);
        this.context = context;
        this.sols = sols;
        this.inflater = LayoutInflater.from(context);
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_item_sol, parent, false);
        Sol sol = sols.get(position);

        TextView tvName = view.findViewById(R.id.tvListName);
        TextView tvDist = view.findViewById(R.id.tvListDist);
        TextView tvPressure = view.findViewById(R.id.tvListMagnitude);

        tvName.setText(sol.getName());
        String temperature = context.getString(R.string.temperature) + ": "
                + sol.getTemperature().setScale(2, RoundingMode.HALF_DOWN).toString();
        tvDist.setText(temperature);
        String pressure = context.getString(R.string.pressure) + ": "
                + sol.getPressure().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
        tvPressure.setText(pressure);

        return view;

    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_sol, parent, false);

            TextView tvName = convertView.findViewById(R.id.tvListName);
            TextView tvDist = convertView.findViewById(R.id.tvListDist);
            TextView tvPressure = convertView.findViewById(R.id.tvListMagnitude);

            holder = new Holder();
            holder.tvName = tvName;
            holder.tvDist = tvDist;
            holder.tvPressure = tvPressure;

            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        Sol sol = sols.get(position);



        holder.tvName.setText(sol.getName());
        String distance = context.getString(R.string.dist) + ":"
                + sol.getDistance().setScale(2, RoundingMode.HALF_DOWN).toString();
        holder.tvDist.setText(distance);
        String magnitude = context.getString(R.string.magnitude) + ":"
                + sol.getMagnitude().setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
        holder.tvPressure.setText(magnitude);

        return convertView;

    }

    private class Holder {
        TextView tvName;
        TextView tvDist;
        TextView tvPressure;
    }
*/

}
