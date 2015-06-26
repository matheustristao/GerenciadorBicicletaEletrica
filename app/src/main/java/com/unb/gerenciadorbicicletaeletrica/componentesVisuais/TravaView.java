package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unb.gerenciadorbicicletaeletrica.R;

/**
 * Created by Ramon on 5/16/15.
 */
public class TravaView  extends RelativeLayout {


    private ImageButton btn_trava;
    private RelativeLayout.LayoutParams alinhamento;
    private boolean lock;
    private TextView tv_email;
    private TextView tv_nome;
    private TextView tv_telefone;

    public TravaView(Context context)
    {
        super(context);

        alinhamento=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        alinhamento.addRule(ALIGN_END);




        setBtn_trava(new ImageButton(context));
        getBtn_trava().setY(300);
        getBtn_trava().setLayoutParams(alinhamento);



        ((ImageButton) btn_trava).setImageResource(R.drawable.cadeado_aberto);
        btn_trava.setBackgroundColor(Color.TRANSPARENT);
        this.lock=true;
        this.addView(getBtn_trava());
    }

    public TravaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageButton getBtn_trava() {
        return btn_trava;
    }

    public void setBtn_trava(ImageButton btn_trava) {
        this.btn_trava = btn_trava;
    }

    public LayoutParams getAlinhamento() {
        return alinhamento;
    }

    public void setAlinhamento(LayoutParams alinhamento) {
        this.alinhamento = alinhamento;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;

    }
    public void changeLock()
    {
        this.lock = lock;
        if (lock)
        {
            ((ImageButton) btn_trava).setImageResource(R.drawable.cadeado_fechado);
            btn_trava.setBackgroundColor(Color.TRANSPARENT);
        }else
        {
            ((ImageButton) btn_trava).setImageResource(R.drawable.cadeado_aberto);
            btn_trava.setBackgroundColor(Color.TRANSPARENT);
        }
        this.lock = !lock;
    }
}
