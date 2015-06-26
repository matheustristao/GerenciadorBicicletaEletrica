package com.unb.gerenciadorbicicletaeletrica;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.CadastroView;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.DataView;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.LoginView;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.Tela_cadastro;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.TravaView;
import  android.text.*;
import  android.widget.RelativeLayout.LayoutParams;
import  android.R.*;
import android.widget.Toast;
import android.view.inputmethod.*;


/**
 * Created by Ramon on 5/16/15.
 */
public class PrincipalActivity  extends Activity
{
    private String sessaoFake=null;
    private ConectionFactory conectionFactory = new ConectionFactory();
    private String recebimento_servidor;
    private String SHAHash;

    private LoginView loginView;
    private CadastroView cadastroView;
    private TravaView travaView;
    private DataView infoView;

    public static String email_logado = "";
    private String logado;
    private Usuario usuario;

    private RelativeLayout rlayout;
    private LayoutParams params;
    private Toolbar upperToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        setContentView(R.layout.activity_principal);
        upperToolBar = (Toolbar) findViewById(R.id.tb_main);
        upperToolBar.inflateMenu(R.menu.menu_main);

        upperToolBar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {



                        int id = item.getItemId();

                        //noinspection SimplifiableIfStatement
                        if (id == R.id.action_settings) {
                            return true;
                        }

                        if (id == R.id.sair) {
                            popViews();
                            logado=null;
                            solicitaLogin(getBaseContext());

                        }
                        return true;
                    }
                });

        Toast.makeText(this,"Loging solicita "+solicitaLogin(this),Toast.LENGTH_SHORT).show();

//        configuraEventosToolbarBottom(this);

    }

    private int solicitaLogin(final Context context)
    {
        //0: Nao logado
        //1: Logado
        //2:Novo cadastro
        final int[] retorno = {0};

        rlayout=(RelativeLayout) findViewById(R.id.layoutPrincipal);
        params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//Essa linha vai dar merda


        loginView = new LoginView(context);
        loginView.setLayoutParams(params);
        loginView.setY(110);

        Button btn_enviar = new Button(context);
        Button btn_novoCadastro = new Button(context);

        loginView.setBtn_enviar(btn_enviar);
        loginView.setBtn_cadastrar(btn_novoCadastro);


        btn_enviar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                retorno[0] =loginView.logar();
//                Toast.makeText(context," Op 0 "+retorno[0],Toast.LENGTH_SHORT).show();
//
//                configuraEventosToolbarBottom(context);

                new Thread() {
                    public void run() {

                        EditText etemail = loginView.getTf_email();
                        EditText etsenha = loginView.getTf_senha();

                        email_logado = etemail.getText().toString();

                        final String hashSenha = Util.computeSHAHash(etemail.getText().toString(),etsenha.getText().toString());

                        logado = conectionFactory.loginHttp(etemail.getText().toString(), hashSenha);

                        if (logado.equals("loguei")) {

                             trava(context);
                             configuraEventosToolbarBottom(context);
                             usuario=conectionFactory.getDadosUsuarioHttp(email_logado);

                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    email_logado=null;
                                    usuario=null;
                                    Toast.makeText(getBaseContext(), "Login não efetuado: Usuario e/ou senha incorretos", Toast.LENGTH_SHORT).show();

                                    loginView.getTf_senha().setInputType(InputType.TYPE_NULL);//Tentativa de esconder o teclado
                                }
                            });

                        }
                    }

                }.start();
            }
        });


        btn_novoCadastro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                    retorno[0]=2;
                Toast.makeText(context," Op 2",Toast.LENGTH_SHORT).show();
                novoCadastro(context);

            }
        });
//        trava(context);//apagar essa linha
//        configuraEventosToolbarBottom(context);//Apagar essa linha
        popViews();
        rlayout.addView(loginView);


        return retorno[0];
    }

    private void novoCadastro(final Context context)
    {
        rlayout=(RelativeLayout) findViewById(R.id.layoutPrincipal);
        params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);//Essa linha vai dar merda

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
                                    solicitaLogin(context);
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

    private void informacoes(final Context context)
    {
        if(logado==null)
        {
            bloqueiaEventosToolbarBottom(context);
            return;
        }

        rlayout=(RelativeLayout) findViewById(R.id.layoutPrincipal);
        params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);//Essa linha vai dar merda


//        Toast.makeText(context,"Tranca",Toast.LENGTH_SHORT).show();
        infoView=new DataView(context);
        infoView.setLayoutParams(params);
        infoView.addInfo(usuario.getNome(),usuario.getEmail(),usuario.getTelefone());





              //  infoView.addInfo(response.getNome(),response.getEmail(),response.getTelefone());



        rlayout.addView(infoView);

        Toast.makeText(context,"informacoes ",Toast.LENGTH_SHORT).show();

    }
    private void trava(final Context context)
    {
        if(logado==null)
        {
            bloqueiaEventosToolbarBottom(context);
            return;
        }

        rlayout=(RelativeLayout) findViewById(R.id.layoutPrincipal);
        params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);//Essa linha vai dar merda

        travaView=new TravaView(context);
        travaView.setLayoutParams(params);
        travaView.getBtn_trava().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(travaView.isLock())
                {

                    travaView.retirarBike(usuario.getEmail(),"r","piloto");
                    Toast.makeText(context,"1-->"+travaView.isLock(),Toast.LENGTH_SHORT).show();
                    travaView.changeLock();
                }else
                {
                    travaView.habilitarTrava(usuario.getEmail(),"r","piloto");
                    Toast.makeText(context,"2-->"+travaView.isLock(),Toast.LENGTH_SHORT).show();
                    travaView.changeLock();
                }



            }
        });

        popViews();

        this.runOnUiThread(new Runnable() {

            public void run()
            {
                rlayout.addView(travaView);
            }
        });

    }
    private void bloqueiaEventosToolbarBottom(final  Context context)
    {
        Toolbar mToolBarBottom=(Toolbar) findViewById(R.id.inc_tb_bottom);
        rlayout=(RelativeLayout) findViewById(R.id.layoutPrincipal);
        params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);//Essa linha vai dar merda

        //Botao Tranca
        mToolBarBottom.findViewById(R.id.toolBtn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popViews();
                solicitaLogin(context);

            }
        });

        //Botao bateria
        mToolBarBottom.findViewById(R.id.toolBtn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popViews();
                solicitaLogin(context);



            }
        });

        //Botao Mapa
        mToolBarBottom.findViewById(R.id.toolBtn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popViews();
                solicitaLogin(context);
            }

        });

    }
    private void configuraEventosToolbarBottom( final Context context)
    {

        Toolbar mToolBarBottom=(Toolbar) findViewById(R.id.inc_tb_bottom);
        rlayout=(RelativeLayout) findViewById(R.id.layoutPrincipal);
        params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);//Essa linha vai dar merda

        //Botao Tranca
        mToolBarBottom.findViewById(R.id.toolBtn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              popViews();
              trava(context);

            }
        });

        //Botao bateria

        mToolBarBottom.findViewById(R.id.toolBtn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                new Thread(){
//                        public void run()
//                    {
//                        final Usuario response = conectionFactory.getDadosUsuarioHttp(email_logado);
//                        Toast.makeText(getBaseContext(),response.getNome(),Toast.LENGTH_SHORT);
//                 }
//                 }.start();


              popViews();
              informacoes(context);



            }
        });

        //Botao Mapa
        mToolBarBottom.findViewById(R.id.toolBtn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }

        });
    }


    private void popViews()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                View v=null;
                int total=rlayout.getChildCount();
                for (int i=0;i<total;i++)
                {
                    v=rlayout.getChildAt(i);
                    if(v instanceof CadastroView || v instanceof LoginView || v instanceof TravaView || v instanceof DataView )
                        rlayout.removeView(v);
                }
            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//

//Inner Class

    private class CadastroAsync extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {


            return null;
        }
    }

}
