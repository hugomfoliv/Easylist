package com.anac.easylist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Artur on 11-09-2015.
 */
public class GetValidade extends ActionBarActivity {
    private static String url = "http://178.62.25.30:4000/getForaValidade";
    private static String url2 = "http://178.62.25.30:4000/getAlmostForaValidade";

    ArrayList<String> items = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    ListView list, list2;


    // server
    JSONArray produtos = null;

    ArrayList<HashMap<String, String>> listaprodutos;
    ArrayList<HashMap<String, String>> listaprodutos2;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_validade, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prazo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listaprodutos = new ArrayList<HashMap<String, String>>();
        listaprodutos2= new ArrayList<HashMap<String, String>>();
        list = (ListView) findViewById(R.id.list_validadeBlock);
        list2 = (ListView) findViewById(R.id.list_validadeWarning);

        new Vality().execute();
        new Vality2().execute();

    }

public class Vality extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        ServiceHandler sh = new ServiceHandler();

        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

        Log.d("Response: ", "> " + jsonStr);
        try {

            JSONArray produtos = new JSONArray(jsonStr);
            for (int i = 0; i < produtos.length(); i++) {
                JSONObject c = produtos.getJSONObject(i);

                String nome = c.getString("nome");
                String validade = c.getString("data_validade");

                HashMap<String, String> produto = new HashMap<String, String>();
                produto.put("nome", nome);
                produto.put("validade", validade.equals("")? "" :validade.split("T")[0]);
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

        ListAdapter adapter = new SimpleAdapter(GetValidade.this, listaprodutos, R.layout.activity_validade_block, new String[]{"nome", "validade", "quant"}, new int[]{R.id.name, R.id.validade});
        list.setAdapter(adapter);


    }
}
    public class Vality2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(url2, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);
            try {

                JSONArray produtos = new JSONArray(jsonStr);
                for (int i = 0; i < produtos.length(); i++) {
                    JSONObject c = produtos.getJSONObject(i);

                    String nome = c.getString("nome");
                    String validade = c.getString("data_validade");
                    String quant="1";
                    HashMap<String, String> produto = new HashMap<String, String>();
                    produto.put("nome", nome);
                    produto.put("validade", validade.equals("")? "" :validade.split("T")[0]);

                    listaprodutos2.add(produto);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ListAdapter adapter = new SimpleAdapter(GetValidade.this, listaprodutos2, R.layout.activity_validade_warning, new String[]{"nome", "validade"}, new int[]{R.id.name, R.id.validade});

            list2.setAdapter(adapter);

        }


    }

}
