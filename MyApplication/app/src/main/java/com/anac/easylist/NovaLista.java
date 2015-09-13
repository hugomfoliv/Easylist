package com.anac.easylist;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class NovaLista extends ActionBarActivity {

    private static String url = "http://178.62.25.30:4000/getProdutosSupermercado";
    private static String url1 = "http://178.62.25.30:4000/AddNecessario";
    private static String url2 = "http://178.62.25.30:4000/getNecessario";
    private static String urlAdd = "http://178.62.25.30:4000/AddLista";


    ArrayList<String> items = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter ;
    ListView list;
    String quant;
    float total;
    String newitem;


    // server
    JSONArray produtos = null;

    ArrayList<HashMap<String, String>> listaprodutos;
    TextView tt;
    String atividade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_lista);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listaprodutos = new ArrayList<HashMap<String, String>>();

        list = (ListView) findViewById(R.id.listView2);

        tt = (TextView) findViewById(R.id.total);

        Intent i = getIntent();
        if (i.hasExtra("listNew")) {

            items = getIntent().getExtras().getStringArrayList("listNew");
            new GetProdutos().execute();
        }
        if (i.hasExtra("main")){
            Log.d("entrou","entrou main");
            new GetProdutos().execute();
        }







    }

    private class GetProdutos extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... params) {


            ServiceHandler sh1 = new ServiceHandler();

            String json = sh1.makeServiceCall(url2, ServiceHandler.GET);
            String preco1 = null;
            if (true){
                Log.d("json","true");
                try {
                    JSONArray produtos = new JSONArray(json);
                    for (int i = 0; i < produtos.length(); i++) {
                        JSONObject c = produtos.getJSONObject(i);

                        String nome = c.getString("nome");
                        Log.d("nomeN", nome);
                        String quant = c.getString("quantidade");
                        Log.d("precoN", quant);


                        HashMap<String, String> produto = new HashMap<String, String>();



                            produto.put("nome", nome);
                            produto.put("quant", quant+" unidades");
Log.d("nomefor",nome);
                            ServiceHandler sh2 = new ServiceHandler();
                            String json1 = sh2.makeServiceCall(url, ServiceHandler.GET);
                            JSONArray produtos1 = new JSONArray(json1);
                            for (int k=0; k<produtos1.length();k++){
                                JSONObject g = produtos1.getJSONObject(k);
                                String nome1 = g.getString("nome");
                                if (nome1.equals(nome)) {
                                    preco1 = g.getString("preco");
                                    produto.put("preco", preco1+"€");
                                }


                            }
                            float p = Float.parseFloat(preco1);
                            int quantidade = Integer.parseInt(quant);
                            total += quantidade * p;
                            listaprodutos.add(produto);





                        // int quantidade = Integer.parseInt(quant);
                        //total += quantidade * p;



                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }



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
                    String preco = c.getString("preco");
                    Log.d("preco", preco);

                    HashMap<String, String> produto = new HashMap<String, String>();


                    int quantidades[] = new int[100];

                    for (int j = 0; j < items.size(); j++) {
                        if (items.get(j).equals(nome)) {
                            quant = getIntent().getExtras().getString("quant");
                            produto.put("nome", nome);
                            produto.put("preco", preco+"€");
                            produto.put("quant", quant+" unidades");
                            float p = Float.parseFloat(preco);
                            int quantidade = Integer.parseInt(quant);
                            total += quantidade * p;

                            listaprodutos.add(produto);

                        }
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }







            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            ListAdapter adapter = new SimpleAdapter(NovaLista.this, listaprodutos, R.layout.list_item, new String[] { "nome", "preco", "quant" }, new int[] { R.id.name, R.id.preco, R.id.quantidade });

            list.setAdapter(adapter);
            tt.setText(String.valueOf(total)+"€");

        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nova_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        if (id == R.id.item2) {
            Intent i = new Intent(getApplicationContext(), createList.class);
            i.putStringArrayListExtra("listNew", items);
            startActivity(i);
        }
        else {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }



    public void Add(View v){
        new AddList().execute();

    }


    private class AddList extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... params) {





            ServiceHandler nsh = new ServiceHandler();

            for (int i=0;i<listaprodutos.size();i++) {
                String[] quantidade = listaprodutos.get(i).get("quant").split(" ");
                urlAdd = "http://178.62.25.30:4000/AddLista?nome=" + listaprodutos.get(i).get("nome") + "&quantidade=" + quantidade[0];

                String njsonStr = nsh.makeServiceCall(urlAdd, ServiceHandler.GET);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


           Intent i = new Intent(getApplicationContext(), MainActivity.class);
           startActivity(i);

        }



    }


}
