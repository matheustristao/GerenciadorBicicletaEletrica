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

                cadastroView=new CadastroView(context);
                cadastroView.setBackgroundColor(Color.RED);
                cadastroView.setLayoutParams(params);
                cadastroView.setY(110);

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
