package com.unb.gerenciadorbicicletaeletrica;


import android.app.Activity;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by matheus on 15/04/15.
 */
public class ConectionFactory {


    private String mensagem;

    public String postHttp(String nome, String sobrenome, String email, String telefone, String senha){
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

            mensagem = EntityUtils.toString(resposta.getEntity());


        }

        catch(ClientProtocolException e){}
        catch(IOException e){}

        return mensagem;
    }
}
