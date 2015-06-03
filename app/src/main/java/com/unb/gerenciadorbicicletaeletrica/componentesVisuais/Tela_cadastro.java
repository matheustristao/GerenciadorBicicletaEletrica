package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;


/**
 * Created by Ramon on 5/27/15.
 */

public class Tela_cadastro extends RelativeLayout {

    private RelativeLayout.LayoutParams alinhamento;
    private EditText tf_email;

    public Tela_cadastro(Context context) {

        super(context);


    }

    public Tela_cadastro(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
