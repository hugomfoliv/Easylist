package com.anac.easylist;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Necessarios extends ActionBarActivity {

    private static String url = "http://178.62.25.30:4000/getNecessario";
    private static String urlAdd = "http://178.62.25.30:4000/AddNecessario?nome=";
    private static String urlSuper = "http://178.62.25.30:4000/getSupermercado";

    ListView list;
    String item;
    ArrayList<String> items = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter ;

    JSONArray produtos = null;
    ArrayList<HashMap<String,String>> listaprodutos;
    String newitem;
    boolean voice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_necessarios);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.list_necessarios);
        listaprodutos = new ArrayList<HashMap<String, String>>();


        Intent i = getIntent();
        if (i.hasExtra("list")) {
            items = getIntent().getExtras().getStringArrayList("list");
            newitem = getIntent().getExtras().getString("item");
            voice = true;
        }

        new GetProdutos().execute();








    }

    private class GetProdutos extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... params) {
            if (voice){
                ServiceHandler nsh = new ServiceHandler();
                String quant = getIntent().getExtras().getString("quant");
                urlAdd = "http://178.62.25.30:4000/AddNecessario?nome="+newitem+"&quantidade="+quant;

                String njsonStr = nsh.makeServiceCall(urlAdd, ServiceHandler.GET);







            }

                ServiceHandler sh = new ServiceHandler();

                String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

                Log.d("Response: ", "> " + jsonStr);


                try {

                    JSONArray produtos = new JSONArray(jsonStr);
                    for (int i = 0; i < produtos.length(); i++) {
                        JSONObject c = produtos.getJSONObject(i);

                        String nome = c.getString("nome");
                        Log.d("nome", nome);
                        String quant = c.getString("quantidade");
                        Log.d("quant", quant);

                        HashMap<String, String> produto = new HashMap<String, String>();


                        produto.put("nome", nome);
                        // produto.put("preco","0");
                        produto.put("quantidade", quant+" unidades");
                        listaprodutos.add(produto);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ListAdapter adapter = new SimpleAdapter(Necessarios.this, listaprodutos, R.layout.list_item, new String[] { "nome", "quantidade" }, new int[] { R.id.name , R.id.quantidade });





            list.setAdapter(adapter);


        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_necessarios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.item1) {
            Context c = getApplicationContext();
            Intent intent = new Intent(c, createList.class);
            intent.putStringArrayListExtra("list", items);
            startActivity(intent);
        }
        else {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }




}
