package com.unb.gerenciadorbicicletaeletrica;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.model.Marker;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.CadastroView;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.DataView;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.LoginView;
import com.unb.gerenciadorbicicletaeletrica.componentesVisuais.TravaView;

import  android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.app.AlertDialog;
import  android.content.*;

//MAPA
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.FragmentManager;
//Animacoes
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ramon on 5/16/15.
 */
public class PrincipalActivity  extends FragmentActivity implements GoogleMap.OnMarkerClickListener
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
    private String senhaUsuario;

    private RelativeLayout rlayout;
    private LayoutParams params;
    private Toolbar upperToolBar;

    private AlertDialog alert;

    //MAPa
    private LatLng location = new LatLng(-15.988267,  -48.044216);

    private GoogleMap map;
    private SupportMapFragment mMapFragment;
    private Marker markerMaps;//Marcador no mapa

    private boolean podeUsarCadeado; //Variavel utilizada para amarrar a visualizacao
    private boolean requisicaoAssincronaAtivada;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        setContentView(R.layout.activity_principal);
        upperToolBar = (Toolbar) findViewById(R.id.tb_main);
        upperToolBar.inflateMenu(R.menu.menu_main);
        podeUsarCadeado =false;
        requisicaoAssincronaAtivada=false;

        upperToolBar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {



                        int id = item.getItemId();

                        //noinspection SimplifiableIfStatement

                        if (id == R.id.sair) {
                            popViews();
                            logado=null;
                            solicitaLogin(getBaseContext());
                            if(timer!=null)paraAtualizacaoValorCorrente();

                        }
                        if(id==R.id.satelite)
                        {
                            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        }
                        if(id==R.id.mapa)
                        {
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        if(id==R.id.hibrido)
                        {
                            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            verificaVaga(email_logado);

                        }

                        return true;
                    }
                });

        solicitaLogin(this);


     configuraEventosToolbarBottom(this);
        logado="ramon";
        podeUsarCadeado=true;

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

                new Thread()
                {
                    public void run() {

                        EditText etemail = loginView.getTf_email();
                        final EditText etsenha = loginView.getTf_senha();

                        email_logado = etemail.getText().toString();

                        final String hashSenha = Util.computeSHAHash(etemail.getText().toString(),etsenha.getText().toString());

                        logado = conectionFactory.loginHttp(etemail.getText().toString(), hashSenha);

                        if (logado.equals("loguei")) {

                            runOnUiThread(new Runnable() {
                                public void run() {

                                    try
                                        {

                                             popViews();
                                             addMap();
                                             configuraEventosToolbarBottom(context);

                                              //Inicializa atualizacao assincrona do valor da corrente
                                             if(!requisicaoAssincronaAtivada) atualizaValorCorrente();
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                }
                            });
                            usuario=conectionFactory.getDadosUsuarioHttp(email_logado);

                              Log.e("teste bateria", usuario.getCorrente());

                        } else {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    YoYo.with(Techniques.Tada)
                                            .duration(700)
                                            .playOn(loginView);
                                    email_logado=null;
                                    usuario=null;
                                    loginView.setY(110);
                                   Toast.makeText(getBaseContext(), "Login não efetuado: Usuario e/ou senha incorretos", Toast.LENGTH_SHORT).show();


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
        trava(context);//apagar essa linha
        configuraEventosToolbarBottom(context);//Apagar essa linha
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
        params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);


//        Toast.makeText(context,"Tranca",Toast.LENGTH_SHORT).show();
        infoView=(infoView==null)? new DataView(context):infoView;
        infoView.setLayoutParams(params);
        infoView.addInfo(usuario.getNome(), usuario.getEmail(), usuario.getTelefone(), usuario.getCorrente());

        CorrenteAsync corrente = new CorrenteAsync();
        corrente.execute();

        this.runOnUiThread(new Runnable() {

            public void run() {
                rlayout.addView(infoView);
            }
        });




    }
    private void trava(final Context context)
    {
        if(logado==null)
        {
            bloqueiaEventosToolbarBottom(context);
            return;
        }

        if(podeUsarCadeado)
        {
            rlayout=(RelativeLayout) findViewById(R.id.layoutPrincipal);
            params=new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);//Essa linha vai dar merda

            if(travaView==null) travaView=new TravaView(context);


            String teste = conectionFactory.verificaVagaHttp(email_logado);
            Toast.makeText(getBaseContext(),"QTD Vagas:"+teste,Toast.LENGTH_SHORT).show();

            travaView.setLayoutParams(params);
            travaView.getBtn_trava().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                  validarSenha();

                }
            });


            popViews();

            this.runOnUiThread(new Runnable() {

                public void run()
                {
                    rlayout.addView(travaView);
                }
            });

        }else
        {
            AlertDialog alertDialog= new AlertDialog.Builder(PrincipalActivity.this).create();
            alertDialog.setTitle("Aviso");
            alertDialog.setMessage("Você Precisa escolher primeiro uma estação");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                            addMap();

                        }
                    });
            alertDialog.show();
        }

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
                loadMap();
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

                try
                {
                    popViews();

                    trava(context);

                }catch (Exception e)
                {

                }


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


                try
                {

                    popViews();

                    informacoes(context);


                }catch (Exception e)
                {
                  e.printStackTrace();
                }




            }
        });

        //Botao Mapa
        mToolBarBottom.findViewById(R.id.toolBtn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try
                {
                   popViews();
                   addMap();

                }catch (Exception e)
                {

                }


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

                removeMap();
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
private void addMap()
{
    FragmentManager fm = getFragmentManager();
   // map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
    map = mMapFragment.getMap();

   // map.addMarker(new MarkerOptions().position(location).title("Posto de recarga Piloto"));

    markerMaps = map.addMarker(new MarkerOptions()
            .position(location)
            .title("Posto de recarga Piloto")
            .snippet("1 Vaga dispoível")
           );

    map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    mMapFragment.getView().setVisibility(View.VISIBLE);

    map.setOnMarkerClickListener(PrincipalActivity.this);

//Mostra opcoes de mapa no menu
    upperToolBar.getMenu().findItem(R.id.mapa).setVisible(true);
    upperToolBar.getMenu().findItem(R.id.satelite).setVisible(true);
    upperToolBar.getMenu().findItem(R.id.hibrido).setVisible(true);

   // MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.iconMarker));
}
    private void loadMap()
    {
        FragmentManager fm = getFragmentManager();
        // map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
        map = mMapFragment.getMap();

    //  map.addMarker(new MarkerOptions().position(location).title("Posto de recarga Piloto"));
//      map.addCircle(new CircleOptions().center(location).radius(1.0));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        mMapFragment.getView().setVisibility(View.VISIBLE);


    }

private void removeMap()
{
    mMapFragment = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map));
    map = mMapFragment.getMap();
    mMapFragment.getView().setVisibility(View.INVISIBLE);
    upperToolBar.getMenu().findItem(R.id.mapa).setVisible(false);
    upperToolBar.getMenu().findItem(R.id.satelite).setVisible(false);
    upperToolBar.getMenu().findItem(R.id.hibrido).setVisible(false);
}



private String validarSenha() {


        final EditText[] edit = {null};
        //Cria o gerador do AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Senha");
        //define a mensagem
        builder.setMessage("Informe a sua senha");

        final LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_signin, null);

        AlertDialog.Builder builder1 = builder;
        builder1.setView(dialogView);
        builder1.setPositiveButton("Confirma", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

               edit[0] = (EditText) dialogView.findViewById(R.id.password);
               senhaUsuario= edit[0].getText().toString();

                try{


                    new Thread()
                    {
                        public void run() {

                            final String hashSenha = Util.computeSHAHash(email_logado,senhaUsuario);

                            logado = conectionFactory.loginHttp(email_logado, hashSenha);

                             if (logado.equals("loguei")) {


                                runOnUiThread(new Runnable() {
                                    public void run() {


                                        podeUsarCadeado =true;

                                        if(travaView.isLock())
                                        {
                                            travaView.retirarBike(email_logado,senhaUsuario,"piloto");
                                           Toast.makeText(getBaseContext(),"Aberto"+travaView.isLock(),Toast.LENGTH_SHORT).show();
                                            YoYo.with(Techniques.Pulse)
                                                    .duration(800)
                                                    .playOn(travaView);
                                            travaView.changeLock();
                                        }else
                                        {
                                            travaView.habilitarTrava(email_logado,senhaUsuario,"piloto");
                                            Toast.makeText(getBaseContext(),"Fechado"+travaView.isLock(),Toast.LENGTH_SHORT).show();
                                            YoYo.with(Techniques.Pulse)
                                                    .duration(800)
                                                    .playOn(travaView);
                                            travaView.changeLock();
                                        }

                                    }
                                });


                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        podeUsarCadeado=false;
                                        Toast.makeText(getBaseContext(),"Senha incorreta",Toast.LENGTH_SHORT).show();
                                        YoYo.with(Techniques.Tada)
                                                .duration(700)
                                                .playOn(travaView);

                                    }
                                });

                            }
                        }

                    }.start();

                }catch (Exception e)
                {

                }


            }
        });
        builder1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                //LoginDialogFragment.this.getDialog().cancel();
            }
        });

        //cria o AlertDialog
        alert = builder.create();
        //Exibe
        alert.show();

        return senhaUsuario;
    }




    @Override
    public boolean onMarkerClick(final Marker marker) {


            AlertDialog alertDialog = new AlertDialog.Builder(PrincipalActivity.this).create();
            alertDialog.setTitle("Estação Piloto");
            alertDialog.setMessage("1 vaga disponível\nDeseja utiliza-la?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SIM",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            podeUsarCadeado=true;
                            trava(getBaseContext());
                        }
                    });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"NÃO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        podeUsarCadeado=false;
                        dialog.dismiss();
                    }
                });


//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
            alertDialog.show();

        return true;
    }

public void paraAtualizacaoValorCorrente()
{
   timer.cancel();
}
public void atualizaValorCorrente() {
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask  doAsynchronousTask = new TimerTask() {
            @Override
            public void run()
            {
                boolean post = handler.post(new Runnable() {
                    public void run() {
                              try
                                  {
                                       CorrenteAsync corrente=new CorrenteAsync();
                                        corrente.execute();
                                  } catch (Exception e)
                                  {

                                  }
                    }
                });
            }

        };

        timer.schedule(doAsynchronousTask, 0, 5000);

    }

    @Override
    public void onPause()
    {
        super.onStop();
        if(timer!=null)
        paraAtualizacaoValorCorrente();
    }
    @Override
   public void onStop()
   {
       super.onStop();
       if(timer!=null)
           paraAtualizacaoValorCorrente();
   }


    private void verificaVaga(String email)
    {
         final String respost=conectionFactory.verificaVagaHttp(email_logado);

        new Thread()
        {
            public void run() {

                final String respost=conectionFactory.verificaVagaHttp(email_logado);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            try
                            {
                                Toast.makeText(getBaseContext(),"Resposta "+respost,Toast.LENGTH_SHORT).show();

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });



            }

        }.start();
    }
    //Inner Class

private class QtdVagaTask extends  AsyncTask<String,Void,String> {


    @Override
    protected String doInBackground(String... strings) {


        try
        {
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {

    }
}

    private class DelayCadeado extends AsyncTask<Void,Void,Void>
    {

        private AlertDialog dialog;
        @Override
        protected Void doInBackground(Void... voids) {




        try {
            Toast.makeText(getBaseContext(),"Delay Inicio",Toast.LENGTH_SHORT).show();

//               dialog= new AlertDialog.Builder(getBaseContext()).create();
//
//            dialog.setTitle("Trava");
//            dialog.setMessage("Aguarde enquanto processamos a informação");
//            dialog.show();
//               this.wait(7000);
//                Thread.sleep(5000);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result)
        {

            try
            {
                Toast.makeText(getBaseContext(),"Delay Fim",Toast.LENGTH_SHORT).show();
                  dialog.dismiss();
            }catch (Exception e)
            {
                e.printStackTrace();
            }


        }


    }
    private class CorrenteAsync extends AsyncTask<Void,Void,String>
    {


        @Override
        protected String doInBackground(Void... voids)
        {
            String correnteValor = null;
            try {

                correnteValor=conectionFactory.getDadosUsuarioHttp(email_logado).getCorrente();
//                Toast.makeText(getBaseContext(),"AsyncTask background "+correnteValor,Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return correnteValor;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try
            {
                if(email_logado!=null)
                {
//                    Toast.makeText(getBaseContext(),"AsyncTask PosExecute->"+result,Toast.LENGTH_SHORT).show();
                }    infoView.addInfo(usuario.getNome(),usuario.getEmail(),usuario.getTelefone(),result);
            }catch (Exception e)
            {

            }


        }
    }

}

















