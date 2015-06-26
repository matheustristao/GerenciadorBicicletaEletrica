package com.unb.gerenciadorbicicletaeletrica.componentesVisuais;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * Created by Ramon on 5/16/15.
 */
public class MapaView  extends RelativeLayout {

    private ImageButton btn_trava;
    private RelativeLayout.LayoutParams alinhamento;


    public MapaView(Context context) {
        super(context);
        alinhamento=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        alinhamento.addRule(ALIGN_END);


    }
    public MapaView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


}
