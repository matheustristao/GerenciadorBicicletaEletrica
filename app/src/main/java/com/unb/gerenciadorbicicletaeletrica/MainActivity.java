package com.unb.gerenciadorbicicletaeletrica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btNovoUsuario;

        btNovoUsuario = (Button) findViewById(R.id.btNovoUsuario);
//comment
        btNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CadastroActivity.class);
                startActivity(i);
            }
        });

    }


}