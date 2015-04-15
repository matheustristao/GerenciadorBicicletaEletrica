package com.unb.gerenciadorbicicletaeletrica;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class CadastroActivity extends Activity {


    ConectionFactory conectionFactory = new ConectionFactory();
    boolean recebimento_servidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }

    public void enviarDados(View view){

        new Thread(){
            public void run(){
                EditText nomeEt = (EditText) findViewById(R.id.nome);
                EditText sobrenomeEt = (EditText) findViewById(R.id.sobrenome);
                EditText emailEt = (EditText) findViewById(R.id.email);
                EditText telefoneEt = (EditText) findViewById(R.id.telefone);
                EditText senhaEt = (EditText) findViewById(R.id.senha);
                EditText repeatsenhatEt = (EditText) findViewById(R.id.repeat_senha);

                if(senhaEt.getText().toString().equals(repeatsenhatEt.getText().toString())) {
                    recebimento_servidor = conectionFactory.postHttp(nomeEt.getText().toString(), sobrenomeEt.getText().toString(), emailEt.getText().toString(), telefoneEt.getText().toString(), senhaEt.getText().toString());

                    if (recebimento_servidor == true) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(CadastroActivity.this, "Dados cadastrados com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        });
                        }
                        else{
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(CadastroActivity.this, "Dados não foram recebidos pelo servidor", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                }
                else {
                    CadastroActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CadastroActivity.this, "Senhas não batem. Digite novamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        }.start();

    }


}