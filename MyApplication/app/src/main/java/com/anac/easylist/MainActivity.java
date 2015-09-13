package com.anac.easylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void lista_necessarios(View v){
        Intent intent = new Intent(this, Necessarios.class);
        startActivity(intent);
    }

    public void nova_lista(View v){
        Intent intent = new Intent(this, NovaLista.class);
        intent.putExtra("main","main");
        startActivity(intent);
    }

    public void validade(View v){
        Intent intent = new Intent(this, GetValidade.class);
        startActivity(intent);
    }

    public void stock(View v){
        Intent intent = new Intent(this, Stock.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Button btn = (Button) findViewById(R.id.bt4);
        Button btn2 = (Button) findViewById(R.id.bt3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    validade(view);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stock(view);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
