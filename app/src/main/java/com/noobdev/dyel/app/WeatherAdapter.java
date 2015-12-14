package com.noobdev.dyel.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Giovanni on 01/07/2014.
 */
public class WeatherAdapter extends ArrayAdapter<Weather> {


    Context context;
    int layoutResourceId;
    ArrayList<Weather> data = null;

    public WeatherAdapter(Context context, int layoutResourceId, ArrayList<Weather> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.txtHistoria = (TextView)row.findViewById(R.id.txtHistoriaLinha);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTituloLinha);
            holder.txtQtd = (TextView)row.findViewById(R.id.txtLikesLinha);
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        Weather weather = data.get(position);
        if(weather != null) {
            holder.txtTitle.setText(weather.title);
            holder.txtHistoria.setText(weather.historia);
            holder.txtQtd.setText(weather.qtd);
        }

        return row;
    }

    static class WeatherHolder
    {
        TextView txtHistoria;
        TextView txtQtd;
        TextView txtTitle;
    }
}