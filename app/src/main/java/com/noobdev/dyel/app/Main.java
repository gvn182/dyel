package com.noobdev.dyel.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Giovanni on 07/08/2014.
 */
public class Main extends Activity {

    SQLiteDatabase mydb;
    private static String DBNAME = "Academia.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "EXERCICIOS";       // THIS IS THE TABLE NAME
    private static String TABLE2 = "ALTERACOES";       // THIS IS THE TABLE NAME
    private ListView listView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        createTable();
        FillList("");

    }

    private void FillList(String Filtro) {
        //GetData
        listView = (ListView) findViewById(R.id.list);

        ArrayList<Weather> Data = getValues(Filtro);

        final WeatherAdapter adapter = new WeatherAdapter(this,
                R.layout.listview_item_row, Data);


        ListView listView1 = (ListView) findViewById(R.id.list);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Weather We = adapter.getItem(i);


            }
        });


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,          int position, long id) {
                final Weather We = adapter.getItem(position);
                final EditText input = new EditText(getApplicationContext());
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                input.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                input.setMaxEms(5);
                input.setEms(5);
                input.setGravity(1);
                input.requestFocus();
                input.setTextColor(Color.BLACK);
                new AlertDialog.Builder(Main.this)
                        .setTitle("New height")
                        .setMessage(We.title)
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Editable value = input.getText();
                                if(value.toString().equals(""))
                                    return;

                                Integer Serie = Integer.parseInt(We.historia.split(" X ")[0]);
                                Integer Qtd = Integer.parseInt(We.historia.split(" X ")[1]);
                                InserNewMaquina("",We.title,Serie,Qtd,Float.parseFloat(value.toString()));
                                FillList("");
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FillList("");
                    }
                }).show();

            }});
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id)
            {
                final Weather We = adapter.getItem(pos);
                new AlertDialog.Builder(Main.this)
                        .setTitle("Delete")
                        .setMessage("Remove " + We.title + "?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DeleteMaquina(We.title);
                                FillList("");
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FillList("");
                    }
                }).show();


                return true;
            }
        });
        listView1.setAdapter(adapter);
    }
    public void InsertNewMaquina(String TAG, String Maquina, int Serie, int Qtd, float peso) {
        mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("INSERT INTO " + TABLE + "(TAG,MAQUINA,SERIE,QTD,PESO) VALUES('" + TAG + "','" + Maquina + "','" + Serie + "','" + Qtd + "','" + peso + "')");
        mydb.close();
    }
    public void DeleteMaquina(String Titulo) {
        mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("DELETE FROM " + TABLE + " WHERE MAQUINA = '" + Titulo + "'");
        mydb.close();
    }
    public void dropTable(){
        try{
            mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("DROP TABLE " + TABLE);
            mydb.close();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error encountered while dropping.", Toast.LENGTH_LONG);
        }
    }
    public void createTable(){
        try{
            mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY autoincrement, TAG TEXT, MAQUINA TEXT,SERIE INTEGER, QTD INTEGER, PESO FLOAT);");
            //mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE2 +" (ID_MAQUINA INTEGER NOT NULL, SERIE INTEGER, QTD INTEGER, PESO FLOAT);");
            mydb.close();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    public void InserNewMaquina(String TAG, String Maquina, int Serie, int Qtd, float peso) {
        mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("INSERT INTO " + TABLE + "(TAG,MAQUINA,SERIE,QTD,PESO) VALUES('" + TAG + "','" + Maquina + "','" + Serie + "','" + Qtd + "','" + peso + "')");
        mydb.close();
    }
    public ArrayList<Weather> getValues(String Filtro) {
        try {
            mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM " + TABLE + " WHERE ID IN (" +
                    " SELECT MAX(ID) FROM " + TABLE +
                    " GROUP BY MAQUINA) AND MAQUINA LIKE '%" + Filtro + "%'", null);
            //Log.e("aff", String.valueOf(allrows.getCount()));
            ArrayList<Weather> List = new ArrayList<Weather>();
            if (allrows.moveToFirst()) {
                do {

                    Weather NewI = new Weather();
                    NewI.id = allrows.getString(0);
                    NewI.title = allrows.getString(2);
                    NewI.historia = allrows.getString(3) + " X " + allrows.getString(4);
                    NewI.qtd = allrows.getString(5) + " KG";
                    List.add(NewI);
                }
                while (allrows.moveToNext());
            }
            mydb.close();
            return List;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_procurar)
                .getActionView();
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                FillList(newText);
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                FillList(query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_adicionar: {

                Intent intent = new Intent(this, AddNew.class);
                startActivity(intent);
            };break;

        }
        return true;
    }
}
