package com.login.pregoneros;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListPregonAdapter extends ArrayAdapter<Pregon> {

    //the list values in the List of type hero
    List<Pregon> pregonList;

    //activity context
    Context context;

    //the layout resource file for the list items
    int resource;

    //constructor initializing the values
    public ListPregonAdapter(Context context, int resource, List<Pregon> pregonList) {
        super(context, resource, pregonList);
        this.context = context;
        this.resource = resource;
        this.pregonList = pregonList;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        final TextView tvCompanyName = view.findViewById(R.id.companyName);
        final TextView tvPregonCode = view.findViewById(R.id.companyCode);
        TextView tvObject = view.findViewById(R.id.object);
        TextView tvEndDate = view.findViewById(R.id.endDate);
        final TextView tv_identPregon = view.findViewById(R.id.ident_pregon);
        final TextView tv_identCampaign = view.findViewById(R.id.ident_campaign);
        final TextView tv_identClient = view.findViewById(R.id.ident_cliente);

        LinearLayout lClick = view.findViewById(R.id.pregonClick);

        //getting the hero of the specified position
        Pregon pregon = pregonList.get(position);

        //adding values to the list item
        //imageView.setImageDrawable(context.getResources().getDrawable(hero.getImage()));
        tvCompanyName.setText(pregon.getCompanyName());
        //tvPregonCode.setText(pregon.getPregonCode());
        tvObject.setText(pregon.getObject());
        tvEndDate.setText(pregon.getEndDate());
        tv_identPregon.setText(pregon.getId_pregon());
        tv_identCampaign.setText(pregon.getId_campaign());
        tv_identClient.setText(pregon.getId_client());

        //adding a click listener to the button to remove item from the list
        lClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
                //removePregon(position);
                //PregonesActivosActivity p = new PregonesActivosActivity();
                goToPregon(
                        Integer.parseInt(tv_identPregon.getText().toString()),
                        Integer.parseInt(tv_identCampaign.getText().toString()),
                        Integer.parseInt(tv_identClient.getText().toString()),
                        tvCompanyName.getText().toString(),
                        tvPregonCode.getText().toString()
                );

            }
        });

        //finally returning the view
        return view;
    }

    //this method will remove the item from the list
    private void removePregon(final int position) {
        //Creating an alert dialog to confirm the deletion
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete this?");

        //if the response is positive in the alert
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //removing the item
                pregonList.remove(position);

                //reloading the list
                notifyDataSetChanged();
            }
        });

        //if response is negative nothing is being done
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //creating and displaying the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void goToPregon(int id_pregon, int id_campaign, int id_client, String nombreEmpresa, String codigoPregon)
    {
        // Redirigiendo a Bienvenida a usuario por su registro
        Intent intentPregon = new Intent(getContext(), PregonActivity.class);
        intentPregon.putExtra("id_pregon", id_pregon);
        intentPregon.putExtra("id_campaign", id_campaign);
        intentPregon.putExtra("id_cliente", id_client);
        intentPregon.putExtra("nombre_empresa", nombreEmpresa);
        intentPregon.putExtra("codigo_pregon", codigoPregon);
        getContext().startActivity(intentPregon);
    }

}
