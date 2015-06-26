package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Ramon on 6/26/15.
 */
public class DataView extends RelativeLayout {


    private RelativeLayout.LayoutParams alinhamento;
    private TextView tv_email;
    private TextView tv_nome;
    private TextView tv_telefone;
    public DataView(Context context) {
        super(context);

        alinhamento=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        alinhamento.addRule(ALIGN_END);

        this.setTv_nome(new TextView(context));
        this.getTv_nome().setText("Nome:");
        this.getTv_nome().setTextSize(25);
        this.getTv_nome().setLayoutParams(alinhamento);
        this.getTv_nome().setTextColor(Color.BLACK);
        this.getTv_nome().setY(150);

        this.setTv_email(new TextView(context));
        this.getTv_email().setText("E-mail:");
        this.getTv_email().setTextSize(25);
        this.getTv_email().setLayoutParams(alinhamento);
        this.getTv_email().setTextColor(Color.BLACK);
        this.getTv_email().setY(this.getTv_nome().getY() + 50);

        this.setTv_telefone(new TextView(context));
        this.getTv_telefone().setText("Telefone:");
        this.getTv_telefone().setTextSize(25);
        this.getTv_telefone().setLayoutParams(alinhamento);
        this.getTv_telefone().setTextColor(Color.BLACK);
        this.getTv_telefone().setY(this.getTv_email().getY() + 50);


        this.addView(this.getTv_nome());
        this.addView(this.getTv_email());
        this.addView(this.getTv_telefone());
    }

    public DataView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addInfo(String nome, String email,String telefone)
    {
        this.tv_nome.setText("Nome :"+nome);
        this.tv_email.setText("E-mail :"+email);
        this.tv_telefone.setText("Telefone :"+telefone);
    }
    public TextView getTv_email() {
        return tv_email;
    }

    public void setTv_email(TextView tv_email) {
        this.tv_email = tv_email;
    }

    public TextView getTv_nome() {
        return tv_nome;
    }

    public void setTv_nome(TextView tv_nome) {
        this.tv_nome = tv_nome;
    }

    public TextView getTv_telefone() {
        return tv_telefone;
    }

    public void setTv_telefone(TextView tv_telefone) {
        this.tv_telefone = tv_telefone;
    }
}
