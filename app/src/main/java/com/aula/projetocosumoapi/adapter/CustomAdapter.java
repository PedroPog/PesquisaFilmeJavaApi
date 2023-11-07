package com.aula.projetocosumoapi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aula.projetocosumoapi.R;
import com.aula.projetocosumoapi.models.MovieModel;
import com.aula.projetocosumoapi.models.MoviesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sanjeev k Saroj on 28/2/17.
 */

public class CustomAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<MoviesModel> customListDataModelArrayList = new ArrayList<>();
    LayoutInflater layoutInflater = null;

    public CustomAdapter(Activity activity, ArrayList<MoviesModel> customListDataModelArray) {
        this.activity = activity;
        this.customListDataModelArrayList = customListDataModelArray;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return customListDataModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return customListDataModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static class ViewHolder {
        //declaramos aqui os componentes do nosso item_lista.xml
        ImageView imageViewFilme;
        TextView textViewNomeFilme;

    }

    ViewHolder viewHolder = null;


    // this method  is called each time for arraylist data size.
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View vi = view;
        final int pos = position;
        if (vi == null) {

            // create  viewholder object for list_rowcell View.
            viewHolder = new ViewHolder();
            // passamos nosso item_lista.xml
            // inflate list_rowcell for each row
            vi = layoutInflater.inflate(R.layout.item_lista, null);
            viewHolder.imageViewFilme = vi.findViewById(R.id.imageViewFilme);
            viewHolder.textViewNomeFilme = vi.findViewById(R.id.textViewNomeFilme);
            /*We can use setTag() and getTag() to set and get custom objects as per our requirement.
            The setTag() method takes an argument of type Object, and getTag() returns an Object.*/
            vi.setTag(viewHolder);
        } else {

            /* We recycle a View that already exists */
            viewHolder = (ViewHolder) vi.getTag();
        }

        Picasso.get().load(customListDataModelArrayList.get(pos).Poster).into(viewHolder.imageViewFilme);
        viewHolder.textViewNomeFilme.setText(customListDataModelArrayList.get(pos).Title);

        return view;
    }
}