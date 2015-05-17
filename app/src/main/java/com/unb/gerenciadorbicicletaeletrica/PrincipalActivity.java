package com.unb.gerenciadorbicicletaeletrica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.CadastroView;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.LoginView;
import  android.widget.RelativeLayout.LayoutParams;
import  android.R.*;
import android.widget.Toast;


/**
 * Created by Ramon on 5/16/15.
 */
public class PrincipalActivity  extends Activity
{
    private ConectionFactory conectionFactory = new ConectionFactory();
    private String recebimento_servidor;
    private String SHAHash;

    private LoginView loginView;
    private CadastroView cadastroView;

    private RelativeLayout rlayout;
    private LayoutParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        configuraEventosToolbarBottom(this);
    }


    private void configuraEventosToolbarBottom( final Context context)
    {
        Toolbar mToolBarBottom=(Toolbar) findViewById(R.id.inc_tb_bottom);
        rlayout=(RelativeLayout) findViewById(R.id.layoutPrincipal);
        params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,920);//Essa linha vai dar merda

        mToolBarBottom.findViewById(R.id.toolBtn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginView= new LoginView(context);
                loginView.setBackgroundColor(Color.BLUE);
                loginView.setLayoutParams(params);
                loginView.setY(110);

                popViews();
                rlayout.addView(loginView);
            }
        });

        mToolBarBottom.findViewById(R.id.toolBtn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cadastroView = new CadastroView(context);
                cadastroView.setLayoutParams(params);
                cadastroView.setY(110);

                Button btn_salvar = new Button(context);
                btn_salvar.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View view) {

                        new Thread(){
                            public void run(){

                                EditText nomeEt = cadastroView.getTf_nome();
                                EditText sobrenomeEt = cadastroView.getTf_sobre_nome();
                                EditText emailEt = cadastroView.getTf_email();
                                EditText telefoneEt = cadastroView.getTf_telefone();
                                EditText senhaEt = cadastroView.getTf_senha();
                                EditText repeatsenhatEt = cadastroView.getTf_repeta_senha();



                                if(senhaEt.getText().toString().equals(repeatsenhatEt.getText().toString())) {

                                    String hashSenha = Util.computeSHAHash(emailEt.getText().toString(), senhaEt.getText().toString());

                                    recebimento_servidor = conectionFactory.postHttp(nomeEt.getText().toString(), sobrenomeEt.getText().toString(), emailEt.getText().toString(), telefoneEt.getText().toString(), hashSenha);


                                    runOnUiThread(new Runnable(){
                                        public void run(){
                                            Toast.makeText(getBaseContext(), recebimento_servidor, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    PrincipalActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(PrincipalActivity.this, "Senhas não conferem. Digite novamente", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                        }.start();
                    }
                });
                cadastroView.setBtn_salvar(btn_salvar);

                popViews();
                rlayout.addView(cadastroView);

            }
        });
        mToolBarBottom.findViewById(R.id.toolBtn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int total=rlayout.getChildCount();
                Toast.makeText(context,"Total "+total,Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void popViews()
    {
        View v=null;
        int total=rlayout.getChildCount();
        for (int i=0;i<total;i++)
        {
            v=rlayout.getChildAt(i);
            if(v instanceof CadastroView || v instanceof LoginView)
                rlayout.removeView(v);
        }
    }



}
