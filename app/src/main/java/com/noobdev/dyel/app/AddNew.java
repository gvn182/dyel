package com.noobdev.dyel.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Giovanni on 07/08/2014.
 */
public class AddNew extends Activity implements View.OnClickListener {
    Button btnSave;
    Button btnCancelar;
    SQLiteDatabase mydb;
    private static String DBNAME = "Academia.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "EXERCICIOS";       // THIS IS THE TABLE NAME
    TextView txtSerie;
    TextView txtEquipamento;
    TextView txtQtd;
    TextView txtPeso;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnew);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnCancelar = (Button)findViewById(R.id.btnCancel);
        btnCancelar.setOnClickListener(this);
        txtSerie = (TextView)findViewById(R.id.txtSerie);
        txtEquipamento = (TextView)findViewById(R.id.txtEquipamento);
        txtQtd = (TextView)findViewById(R.id.txtRepeticoes);
        txtPeso = (TextView)findViewById(R.id.txtPeso);
        txtEquipamento.setOnClickListener(this);

      ShowDialog();

    }
    void ShowDialog()
    {
        final EditText input = new EditText(this);
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        input.setAllCaps(true);
        input.setGravity(1);
        input.requestFocus();
        new AlertDialog.Builder(AddNew.this)
                .setTitle("New exercise")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        if (!value.toString().equals(""))
                            txtEquipamento.setText(value.toString().toUpperCase());

                        txtSerie.requestFocus();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                txtSerie.requestFocus();
            }
        }).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtEquipamento:
            {
                ShowDialog();



            }break;
            case R.id.btnCancel:
            {
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);

            }break;
            case R.id.btnSave:
            {
                if(txtEquipamento.getText().toString().equals("")) {
                    SendToast("Fill the exercise name");
                    return;
                }

                if(txtSerie.getText().toString().equals("")) {
                    SendToast("Fill the series");
                    return;
                }
                if(txtQtd.getText().toString().equals("")) {
                    SendToast("Fill repeats");
                    return;
                }

                if(txtPeso.getText().toString().equals("")) {
                    SendToast("Fill the height");
                    return;
                }

                InsertNewMaquina("",txtEquipamento.getText().toString(),Integer.parseInt(txtSerie.getText().toString()),Integer.parseInt(txtQtd.getText().toString()),Integer.parseInt(txtPeso.getText().toString()));
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
            }break;

        }
    }
    public void InsertNewMaquina(String TAG, String Maquina, int Serie, int Qtd, float peso) {
        mydb = openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
        mydb.execSQL("INSERT INTO " + TABLE + "(TAG,MAQUINA,SERIE,QTD,PESO) VALUES('" + TAG + "','" + Maquina + "','" + Serie + "','" + Qtd + "','" + peso + "')");
        mydb.close();
    }
    private void SendToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
