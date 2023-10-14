package com.example.logintest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private EditText etUsuario,etPassword;
private Button btnIngresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etUsuario=(EditText)findViewById(R.id.etUsuario);
        etPassword=(EditText) findViewById(R.id.etPasword);
        btnIngresar=(Button) findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresar();
            }
        });

    }
    public void ingresar(){
        try {
        AdminSQLOpenHelper admin=new AdminSQLOpenHelper(this,"administracion",null,1);
        SQLiteDatabase bd=admin.getWritableDatabase();
        String usuario=etUsuario.getText().toString();
        String password=etPassword.getText().toString();
        Cursor fila=bd.rawQuery("Select usuario,password from admin3 where usuario='"+usuario+"' and password='"
                +password+"'",null);


        if (fila.moveToFirst()) {
          Intent i = new Intent(this,pagePersona.class);
          i.putExtra("cedula",usuario);
          startActivity(i);
            }else {
            Cursor fila2 = bd.rawQuery("Select cedula,nombres from personas where cedula='" + usuario + "' and nombres='"
                    + password + "'", null);
            if (fila2.moveToFirst()) {
                Intent i = new Intent(this, pagePersona.class);
                i.putExtra("cedula",usuario);
                startActivity(i);
            } else {
                etPassword.setText("");
                Toast.makeText(this, "Usuario o contraseña incorrectos Intente de nuevo", Toast.LENGTH_LONG).
                        show();
                bd.close();
            }
        }}catch(Exception e){
            Toast.makeText(this,"Error en database"+e.toString(),Toast.LENGTH_LONG).show();
        }


    }

}


