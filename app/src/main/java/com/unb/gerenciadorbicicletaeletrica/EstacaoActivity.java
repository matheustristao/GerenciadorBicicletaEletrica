package com.unb.gerenciadorbicicletaeletrica;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EstacaoActivity extends ActionBarActivity {

    ConectionFactory conectionFactory = new ConectionFactory();
    String recebimento_servidor;
    private String SHAHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacao);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estacao, menu);
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

    public void enviarDadosEstacao(View view) {

        new Thread() {
            public void run() {
                EditText estacaoEt = (EditText) findViewById(R.id.estacaoTextView);
                EditText senhaEt = (EditText) findViewById(R.id.senhaTextView);

                // Pedro, e agora? Cadê o email?
                String hashSenha = Util.computeSHAHash(emailEt.getText().toString(), senhaEt.getText().toString());

                recebimento_servidor = conectionFactory.postHttpCatraca(hashSenha, estacaoEt.getText().toString());
            }
        }.start();
    }

}
