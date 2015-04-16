package com.unb.gerenciadorbicicletaeletrica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private String logado;

    ConectionFactory conectionFactory = new ConectionFactory();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btNovoUsuario;
        btNovoUsuario = (Button) findViewById(R.id.btNovoUsuario);
        btNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CadastroActivity.class);
                startActivity(i);
            }
        });
    }

    public void logar(final View view){

        new Thread(){
            public void run(){
                EditText etemail = (EditText) findViewById(R.id.etEmail);
                EditText etsenha = (EditText) findViewById(R.id.etSenha);

                logado = conectionFactory.loginHttp(etemail.getText().toString(),etsenha.getText().toString());


                if (logado.equals("loguei")) {
                    Intent i = new Intent(view.getContext(), HomeActivity.class);
                    startActivity(i);
                }
                else {
                    runOnUiThread(new Runnable(){
                        public void run(){
                            Toast.makeText(getBaseContext(), "Login não efetuado: Usuario e/ou senha incorretos" , Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

        }.start();

    }
}