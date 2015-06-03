package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by Ramon on 5/16/15.
 */
public class TravaView  extends RelativeLayout {


    private Button btn_trava;
    private RelativeLayout.LayoutParams alinhamento;

    public TravaView(Context context) {
        super(context);

        alinhamento=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        alinhamento.addRule(ALIGN_END);
        setBtn_trava(new Button(context));
        getBtn_trava().setY(300);
        getBtn_trava().setLayoutParams(alinhamento);
        getBtn_trava().setTextColor(Color.BLACK);
        getBtn_trava().setText("Botão");

        this.addView(getBtn_trava());
    }

    public TravaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Button getBtn_trava() {
        return btn_trava;
    }

    public void setBtn_trava(Button btn_trava) {
        this.btn_trava = btn_trava;
    }

    public LayoutParams getAlinhamento() {
        return alinhamento;
    }

    public void setAlinhamento(LayoutParams alinhamento) {
        this.alinhamento = alinhamento;
    }
   
}
