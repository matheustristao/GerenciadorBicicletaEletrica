package com.unb.gerenciadorbicicletaeletrica;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DadosActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados);

        Intent intent = getIntent();
        Usuario usuario = (Usuario) intent.getSerializableExtra("Usuario");
        TextView textNome = (TextView) findViewById(R.id.tvNome);
        TextView textSobrenome = (TextView) findViewById(R.id.tvSobrenome);
        TextView textEmail = (TextView) findViewById(R.id.tvEmail);
        TextView textTelefone = (TextView) findViewById(R.id.tvTelefone);

        textNome.setText(usuario.getNome());
        textSobrenome.setText(usuario.getSobrenome());
        textEmail.setText(usuario.getEmail());
        textTelefone.setText(usuario.getTelefone());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dados, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
