package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ramon on 5/16/15.
 */

public class CadastroView  extends LinearLayout{

    private LayoutParams alinhamento;
    private TextView lb_email;

    public CadastroView(Context context) {
        super(context);

        lb_email=new TextView(context);
        lb_email.setText("E-mail:");

        this.addView(lb_email);

    }

    public CadastroView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
