package com.unb.gerenciadorbicicletaeletrica;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

                if(senhaEt.getText().toString().equals(repeatsenhatEt.getText().toString())){
                    postHttp(nomeEt.getText().toString(), sobrenomeEt.getText().toString(), emailEt.getText().toString(), telefoneEt.getText().toString(),senhaEt.getText().toString());
                }
                else {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, "Senhas não batem. Digite novamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                }

        }.start();

    }

    public void postHttp(String nome, String sobrenome, String email, String telefone, String senha){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.0.18/xampp/server_bike/server.php");

        try{
            ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
            valores.add(new BasicNameValuePair("nome", nome));
            valores.add(new BasicNameValuePair("sobrenome", sobrenome));
            valores.add(new BasicNameValuePair("email", email));
            valores.add(new BasicNameValuePair("telefone", telefone));
            valores.add(new BasicNameValuePair("senha", senha));

            httpPost.setEntity(new UrlEncodedFormEntity(valores));
            final HttpResponse resposta = httpClient.execute(httpPost);

            runOnUiThread(new Runnable(){
                public void run(){
                    try {
                        Toast.makeText(getBaseContext(), EntityUtils.toString(resposta.getEntity()), Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
        catch(ClientProtocolException e){}
        catch(IOException e){}
    }
}