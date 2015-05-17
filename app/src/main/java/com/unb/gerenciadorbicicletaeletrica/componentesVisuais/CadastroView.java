package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by Ramon on 5/16/15.
 */

public class CadastroView  extends RelativeLayout{



    private RelativeLayout.LayoutParams alinhamento;

    private EditText tf_email;
    private EditText tf_nome;
    private EditText tf_sobre_nome;
    private EditText tf_senha;
    private EditText tf_repeta_senha;
    private EditText tf_telefone;
    private Button btn_salvar ;

    public CadastroView(Context context) {
        super(context);

        alinhamento=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        alinhamento.addRule(ALIGN_END);

        setTf_nome(new EditText(context));
        getTf_nome().setHint("Nome");
        getTf_nome().setHintTextColor(Color.GRAY);
        getTf_nome().setTextColor(Color.BLACK);
        getTf_nome().setLayoutParams(alinhamento);
        getTf_nome().setY(100);


        setTf_sobre_nome(new EditText(context));
        getTf_sobre_nome().setHint("Sobrenome");
        getTf_sobre_nome().setHintTextColor(Color.GRAY);
        getTf_sobre_nome().setTextColor(Color.BLACK);
        getTf_sobre_nome().setLayoutParams(alinhamento);
        getTf_sobre_nome().setY(getTf_nome().getY() + 90);

        setTf_email(new EditText(context));
        getTf_email().setHint("E-mail");
        getTf_email().setHintTextColor(Color.GRAY);
        getTf_email().setTextColor(Color.BLACK);
        getTf_email().setLayoutParams(alinhamento);
        getTf_email().setY(getTf_sobre_nome().getY() + 90);

        setTf_telefone(new EditText(context));
        getTf_telefone().setHint("Telefone");
        getTf_telefone().setHintTextColor(Color.GRAY);
        getTf_telefone().setTextColor(Color.BLACK);
        getTf_telefone().setLayoutParams(alinhamento);
        getTf_telefone().setY(getTf_email().getY() + 90);

        setTf_senha(new EditText(context));
        getTf_senha().setHint("Senha");
        getTf_senha().setHintTextColor(Color.GRAY);
        getTf_senha().setTextColor(Color.BLACK);
        getTf_senha().setLayoutParams(alinhamento);
        getTf_senha().setY(getTf_telefone().getY() + 90);


        setTf_repeta_senha(new EditText(context));
        getTf_repeta_senha().setHint("Repita a senha");
        getTf_repeta_senha().setHintTextColor(Color.GRAY);
        getTf_repeta_senha().setTextColor(Color.BLACK);
        getTf_repeta_senha().setLayoutParams(alinhamento);
        getTf_repeta_senha().setY(getTf_senha().getY() + 90);

        this.addView(getTf_nome());
        this.addView(getTf_sobre_nome());
        this.addView(getTf_email());
        this.addView(getTf_telefone());
        this.addView(getTf_senha());
        this.addView(getTf_repeta_senha());






    }

    public void setBtn_salvar(Button btn_salvar)
    {
        RelativeLayout.LayoutParams alinhamento=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.btn_salvar=btn_salvar;
        this.btn_salvar.setY(getTf_repeta_senha().getY()+100);
        this.btn_salvar.setText("Enviar");
        this.btn_salvar.setTextColor(Color.BLACK);
        this.btn_salvar.setLayoutParams(alinhamento);
        this.addView(btn_salvar);
    }
    public Button getBtn_salvar()
    {
        return this.btn_salvar;
    }
    public CadastroView(Context context, AttributeSet attrs)
    {
        super(context, attrs);



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

    public EditText getTf_sobre_nome() {
        return tf_sobre_nome;
    }

    public void setTf_sobre_nome(EditText tf_sobre_nome) {
        this.tf_sobre_nome = tf_sobre_nome;
    }

    public EditText getTf_senha() {
        return tf_senha;
    }

    public void setTf_senha(EditText tf_senha) {
        this.tf_senha = tf_senha;
    }

    public EditText getTf_repeta_senha() {
        return tf_repeta_senha;
    }

    public void setTf_repeta_senha(EditText tf_repeta_senha) {
        this.tf_repeta_senha = tf_repeta_senha;
    }

    public EditText getTf_telefone() {
        return tf_telefone;
    }

    public void setTf_telefone(EditText tf_telefone) {
        this.tf_telefone = tf_telefone;
    }
}
