package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.unb.gerenciadorbicicletaeletrica.ConectionFactory;
import com.unb.gerenciadorbicicletaeletrica.HomeActivity;
import com.unb.gerenciadorbicicletaeletrica.R;
import com.unb.gerenciadorbicicletaeletrica.Util;

/**
 * Created by Ramon on 5/16/15.
 */
public class LoginView  extends RelativeLayout{

    private RelativeLayout.LayoutParams alinhamento;

    private EditText tf_email;
    private EditText tf_nome;

    private EditText tf_senha;
    private Button btn_enviar ;
    private Button btn_cadastrar ;
    private ConectionFactory conectionFactory ;
    public LoginView(Context context) {
        super(context);
        alinhamento=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        alinhamento.addRule(ALIGN_END);

        conectionFactory = new ConectionFactory();

        setTf_email(new EditText(context));
        getTf_email().setHint("Email");
        getTf_email().setTextColor(Color.BLACK);
        getTf_email().setHintTextColor(Color.GRAY);
        getTf_email().setLayoutParams(alinhamento);
        getTf_email().setY(100);


        setTf_senha(new EditText(context));
        getTf_senha().setHint("Senha");
        getTf_senha().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        getTf_senha().setTextColor(Color.BLACK);
        getTf_senha().setHintTextColor(Color.GRAY);
        getTf_senha().setLayoutParams(alinhamento);
        getTf_senha().setY(getTf_email().getY() + 90);

        this.addView(getTf_email());
        this.addView(getTf_senha());


    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public int logar()
    {
        if(!getTf_senha().getText().equals("Email") && !getTf_senha().getText().equals("Senha"))
            return 1;
        else return 0;

//        new Thread() {
//            public void run() {
//                String logado;
//                String email_logado ="";
//
//                email_logado = getTf_email().toString();
//
//
//                final String hashSenha = Util.computeSHAHash(getTf_email().toString(), getTf_senha().toString());
//
//                logado = conectionFactory.loginHttp(getTf_email().toString(), hashSenha);
//
//                if (logado.equals("loguei"))
//                {
//                    Intent i = new Intent(view.getContext(), HomeActivity.class);
//                    startActivity(i);
//
//                } else {
//
//                    runOnUiThread(new Runnable()
//                    {
//                        public void run() {
//                            Toast.makeText(getBaseContext(), "Login não efetuado: Usuario e/ou senha incorretos", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
//            }
//
//        }.start();


    }

    public EditText getTf_email() {
        return tf_email;
    }

    public void setTf_email(EditText tf_email) {
        this.tf_email = tf_email;
    }

    public EditText getTf_nome() {
        return tf_nome;
    }

    public void setTf_nome(EditText tf_nome) {
        this.tf_nome = tf_nome;
    }

    public EditText getTf_senha() {
        return tf_senha;
    }

    public void setTf_senha(EditText tf_senha) {
        this.tf_senha = tf_senha;
    }

    public Button getBtn_enviar() {
        return btn_enviar;
    }

    public void setBtn_enviar(Button btn_enviar) {
        this.btn_enviar = btn_enviar;
        RelativeLayout.LayoutParams alinhamento=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        this.btn_enviar.setY(getTf_senha().getY()+100);
        this.btn_enviar.setText("Enviar");
        this.btn_enviar.setTextColor(Color.BLACK);
        this.btn_enviar.setLayoutParams(alinhamento);
        addView(this.btn_enviar);
    }

    public Button getBtn_cadastrar() {
        return btn_cadastrar;
    }

    public void setBtn_cadastrar(Button btn_cadastrar) {


        this.btn_cadastrar = btn_cadastrar;
        RelativeLayout.LayoutParams alinhamento=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        this.btn_cadastrar.setY(getBtn_enviar().getY()+140);
        this.btn_cadastrar.setText("Cadastrar");
        this.btn_cadastrar.setTextColor(Color.BLACK);
        this.btn_cadastrar.setLayoutParams(alinhamento);

        this.addView(this.btn_cadastrar);
    }
}
