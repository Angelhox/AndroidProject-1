package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class pagePersona extends AppCompatActivity {
    private EditText etCedula,etNombres,etProvincia,etCorreo;
    private RadioButton rbtnHombre,rbtnMujer;
    private Button btnGuardar,btnBuscar,btnRegresar,btnActualizar,btnBorrar;
    private Spinner spnPais;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_persona);
        etCedula=(EditText) findViewById(R.id.etCedula);
        etNombres=(EditText) findViewById(R.id.etNombre);
        etProvincia=(EditText)findViewById(R.id.etProvincia);
        etCorreo=(EditText) findViewById(R.id.etCorreo);
        spnPais=(Spinner)findViewById(R.id.spnPais);
        rbtnHombre=(RadioButton)findViewById(R.id.rbtnHombre);
        rbtnMujer=(RadioButton)findViewById(R.id.rbtnMujer);
        btnGuardar=(Button) findViewById(R.id.btnGuardar);
        btnBuscar=(Button) findViewById(R.id.btnBuscar);
        btnActualizar=(Button)findViewById(R.id.btnActualizar);
        btnBorrar=(Button) findViewById(R.id.btnBorrar);
        btnRegresar=(Button) findViewById(R.id.btnRegresar);
        String []opciones={"Seleccione su Pais","Ecuador","Colombia","Bolivia","Chile","Argentina"};
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.layout_spinner,opciones);
        spnPais.setAdapter(adapter);
        Bundle bundle=getIntent().getExtras();
        String cedula=bundle.getString("cedula");
        mostrarDatos(cedula);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarCedula();
            }
        });
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizar();
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regresar();
            }
        });
    }
    public void guardar() {
        AdminSQLOpenHelper admin = new AdminSQLOpenHelper(this, "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String nombres = etNombres.getText().toString();
        String cedula = etCedula.getText().toString();
        String pais = spnPais.getSelectedItem().toString();
        String genero="null";
        if (rbtnHombre.isChecked()) {
            genero = "Hombre";
        } else if (rbtnMujer.isChecked()) {
             genero = "Mujer";
        }

        String provincia = etProvincia.getText().toString();
        String correo = etCorreo.getText().toString();
        if (cedula.equals("")) {
            Toast.makeText(this, "Debes ingresar tu cédula", Toast.LENGTH_LONG).show();
        } else if (nombres.equals("")) {
            Toast.makeText(this, "Debes ingresar tus nombres", Toast.LENGTH_LONG).show();
        }else if(genero.equals("null")){
            Toast.makeText(this,"Debes seleccionar tu genero",Toast.LENGTH_LONG).show();
        } else if (pais.equals("Seleccione su Pais")) {
            Toast.makeText(this, "Debes seleccionar tu pais", Toast.LENGTH_LONG).show();
        } else if (provincia.equals("")) {
            Toast.makeText(this, "Debes ingresar tu provincia", Toast.LENGTH_LONG).show();
        } else if (correo.equals("")) {
            Toast.makeText(this, "Debes ingresar tu correo", Toast.LENGTH_LONG).show();
        } else {
            ContentValues datosPersona = new ContentValues();
            datosPersona.put("cedula", cedula);
            datosPersona.put("nombres", nombres);
            datosPersona.put("genero", genero);
            datosPersona.put("pais", pais);
            datosPersona.put("provincia", provincia);
            datosPersona.put("correo", correo);
            try {
                bd.insert("personas", null, datosPersona);
                bd.close();
                etNombres.setText("");
                etCedula.setText("");
                etProvincia.setText("");
                etCorreo.setText("");
                rbtnHombre.setChecked(false);
                rbtnMujer.setChecked(false);
                spnPais.setSelection(0);
                Toast.makeText(this, "Datos ingresados con exito"+genero, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    public void buscarCedula(){
        AdminSQLOpenHelper admin= new AdminSQLOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bd=admin.getWritableDatabase();
        String cedula=etCedula.getText().toString();
        Cursor fila=bd.rawQuery("Select cedula,nombres,genero,pais,provincia,correo from personas where cedula='"+cedula+
                "'",null);
        if(fila.moveToFirst()){
            rbtnMujer.setChecked(true);
            etCedula.setText(fila.getString (0));
            etNombres.setText(fila.getString(1));
            if (fila.getString(2).equals("Mujer")) {
                rbtnMujer.setChecked(true);
            }else if(fila.getString(2).equals("Hombre")){
                rbtnHombre.setChecked(true);
            }
            spnPais.setSelection(((ArrayAdapter<String>)spnPais.getAdapter()).getPosition(fila.getString(3)));
            etProvincia.setText(fila.getString(4));
            etCorreo.setText(fila.getString(5));

        }else{
            Toast.makeText(this,"No existe una persona con este numero de Cedula",Toast.LENGTH_LONG).show();
            bd.close();
        }

    }
    public void actualizar(){
        AdminSQLOpenHelper admin=new AdminSQLOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bd=admin.getWritableDatabase();
        String cedula=etCedula.getText().toString();
        String nombres=etNombres.getText().toString();
        String genero="";
        if(rbtnHombre.isChecked()){
            genero="Hombre";

        }else if (rbtnMujer.isChecked()){
            genero="Mujer";
        }
        String pais=spnPais.getSelectedItem().toString();
        String provincia=etProvincia.getText().toString();
        String correo=etCorreo.getText().toString();
        ContentValues datosPersona=new ContentValues();
        datosPersona.put("cedula",cedula);
        datosPersona.put("nombres",nombres);
        datosPersona.put("genero",genero);
        datosPersona.put("pais",pais);
        datosPersona.put("provincia",provincia);
        datosPersona.put("correo",correo);
        int cant=bd.update("personas",datosPersona,"cedula='"+cedula+"'",null);
        bd.close();
        if(cant==1){
            Toast.makeText(this,"Se aplicaron los cambios",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"No existe una persona con este numero de cédula",Toast.LENGTH_LONG).show();
        }

    }
public void eliminar (){
        AdminSQLOpenHelper admin= new AdminSQLOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bd=admin.getWritableDatabase();
        String cedula=etCedula.getText().toString();
        int cant = bd.delete("personas","cedula='"+cedula+"'",null);
        bd.close();
        if(cant==1){
            Toast.makeText(this,"Se borro la persona con el numero de cedula",Toast.LENGTH_LONG).show();
        etCedula.setText("");
        etNombres.setText("");
        etProvincia.setText("");
        etCorreo.setText("");
        rbtnHombre.setChecked(false);
        rbtnMujer.setChecked(false);
        spnPais.setSelection(0);
        }else{
            Toast.makeText(this,"No existe una persona con este numero de cédula",Toast.LENGTH_LONG).show();
        }
}
public void mostrarDatos(String cedula){
        try {
            AdminSQLOpenHelper admin= new AdminSQLOpenHelper(this,"administracion",null,1);
            SQLiteDatabase bd=admin.getWritableDatabase();

            Cursor fila=bd.rawQuery("Select cedula,nombres,genero,pais,provincia,correo from personas where cedula='"+cedula+
                    "'",null);
            if(fila.moveToFirst()){
                rbtnMujer.setChecked(true);
                etCedula.setText(fila.getString (0));
                etNombres.setText(fila.getString(1));
                if (fila.getString(2).equals("Mujer")) {
                    rbtnMujer.setChecked(true);
                }else if(fila.getString(2).equals("Hombre")){
                    rbtnHombre.setChecked(true);
                }
                spnPais.setSelection(((ArrayAdapter<String>)spnPais.getAdapter()).getPosition(fila.getString(3)));
                etProvincia.setText(fila.getString(4));
                etCorreo.setText(fila.getString(5));

            }else{
                Toast.makeText(this,"No existen datos para este usuario",Toast.LENGTH_LONG).show();
                bd.close();
            }


        }catch (Exception e){

    }
}
public void regresar(){
    Intent i = new Intent(this,MainActivity.class);
    startActivity(i);
}
}