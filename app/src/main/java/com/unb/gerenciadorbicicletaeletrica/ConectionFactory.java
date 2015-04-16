package com.unb.gerenciadorbicicletaeletrica;



import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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


    private String mensagem_cadastro;

    private String mensagem_login;

    private String numero_funcao;

    public String postHttp(String nome, String sobrenome, String email, String telefone, String senha){

        numero_funcao = "1";

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.0.18/xampp/server_bike/server.php");
        try{
            ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
            valores.add(new BasicNameValuePair("nome", nome));
            valores.add(new BasicNameValuePair("sobrenome", sobrenome));
            valores.add(new BasicNameValuePair("email", email));
            valores.add(new BasicNameValuePair("telefone", telefone));
            valores.add(new BasicNameValuePair("senha", senha));

            valores.add(new BasicNameValuePair("numerofuncao", numero_funcao));

            httpPost.setEntity(new UrlEncodedFormEntity(valores));
            final HttpResponse resposta = httpClient.execute(httpPost);

            mensagem_cadastro = EntityUtils.toString(resposta.getEntity());


        }

        catch(ClientProtocolException e){}
        catch(IOException e){}

        return mensagem_cadastro;
    }

    public String loginHttp(String email, String senha) {

        numero_funcao = "2";

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.168.0.18/xampp/server_bike/server.php");
        try {
            ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();

            valores.add(new BasicNameValuePair("email", email));
            valores.add(new BasicNameValuePair("senha", senha));

            valores.add(new BasicNameValuePair("numerofuncao", numero_funcao));

            httpPost.setEntity(new UrlEncodedFormEntity(valores));

            final HttpResponse resposta = httpClient.execute(httpPost);

            mensagem_login = EntityUtils.toString(resposta.getEntity());

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        return mensagem_login;
    }
}
