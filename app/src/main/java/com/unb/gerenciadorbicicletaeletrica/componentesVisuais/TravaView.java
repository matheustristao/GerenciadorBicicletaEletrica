package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unb.gerenciadorbicicletaeletrica.ConectionFactory;
import com.unb.gerenciadorbicicletaeletrica.MainActivity;
import com.unb.gerenciadorbicicletaeletrica.R;
import com.unb.gerenciadorbicicletaeletrica.Util;

/**
 * Created by Ramon on 5/16/15.
 */
public class TravaView  extends RelativeLayout {

    ConectionFactory conectionFactory = new ConectionFactory();
    String recebimento_servidor;

    private ImageButton btn_trava;
    private RelativeLayout.LayoutParams alinhamento;
    private boolean lock;
    private TextView tv_email;
    private TextView tv_nome;
    private TextView tv_telefone;
    private Context context;


    public TravaView(Context context)
    {
        super(context);
        this.context=context;
        alinhamento=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        alinhamento.addRule(ALIGN_END);

        setBtn_trava(new ImageButton(context));
        getBtn_trava().setY(300);
        getBtn_trava().setLayoutParams(alinhamento);



        ((ImageButton) btn_trava).setImageResource(R.drawable.cadeado_aberto);
        btn_trava.setBackgroundColor(Color.TRANSPARENT);
        this.lock=false;
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
        if (lock)((ImageButton) btn_trava).setImageResource(R.drawable.cadeado_fechado);
        else ((ImageButton) btn_trava).setImageResource(R.drawable.cadeado_aberto);
        this.lock = lock;

    }
    public void changeLock()
    {

        this.lock = !lock;
        if (this.lock)
        {
            ((ImageButton) btn_trava).setImageResource(R.drawable.cadeado_fechado);
            btn_trava.setBackgroundColor(Color.TRANSPARENT);
        }else
        {
            ((ImageButton) btn_trava).setImageResource(R.drawable.cadeado_aberto);
            btn_trava.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    public void habilitarTrava(final String email_logado, final String senha, final String estacao) {

        new Thread() {
            public void run() {
                EditText estacaoEt = (EditText) findViewById(R.id.estacaoTextView);
                final EditText senhaEt = (EditText) findViewById(R.id.senhaTextView);

                final String hashSenha;
                hashSenha = Util.computeSHAHash(email_logado, senha);

                Log.e("email", hashSenha);
                recebimento_servidor = conectionFactory.postHttpHabilitar(hashSenha, estacao);

//                if(!recebimento_servidor.equals("Solicitacao nao atendida pelo arduino"))
//                {
//                    travaView.changeLock();
//                }
                Log.e("email",recebimento_servidor);
            }
        }.start();
    }

    public void retirarBike(final String email_logado, final String senha, final String estacao) {

        new Thread() {
            public void run() {
//                EditText estacaoEt = (EditText) findViewById(R.id.estacaoTextView);
//                final EditText senhaEt = (EditText) findViewById(R.id.senhaTextView);

                final String hashSenha = Util.computeSHAHash(email_logado, senha);
                Log.e("email",MainActivity.email_logado);
                recebimento_servidor = conectionFactory.postHttpRetirar(hashSenha, estacao);

//                if(!recebimento_servidor.equals("Solicitacao nao atendida pelo arduino"))
//                {
//                   travaView.changeLock();
//                }
                Log.e("email",recebimento_servidor);

            }
        }.start();
    }
}
