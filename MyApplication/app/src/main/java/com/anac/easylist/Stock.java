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
 * Created by Artur on 12-09-2015.
 */
public class Stock extends ActionBarActivity {
    private static String url = "http://178.62.25.30:4000/getStock";

    ArrayList<String> items = new ArrayList<String>();
    private ArrayAdapter<String> listAdapter;
    ListView list;


    // server
    JSONArray produtos = null;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    ArrayList<HashMap<String, String>> listaprodutos;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listaprodutos = new ArrayList<HashMap<String, String>>();
        list = (ListView) findViewById(R.id.listView2);

        new StockFetcher().execute();
    }
    public class StockFetcher extends AsyncTask<Void, Void, Void> {

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
                    String quantidade = c.getString("quantidade");

                    HashMap<String, String> produto = new HashMap<String, String>();
                    produto.put("nome", nome);
                    produto.put("quantidade",String.format("%s %s",quantidade,"unidade"+(quantidade.equals("1")?"":"s")));
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

            ListAdapter adapter = new SimpleAdapter(Stock.this, listaprodutos, R.layout.list_item, new String[]{"nome", "quantidade", ""}, new int[]{R.id.name, R.id.quantidade});
            list.setAdapter(adapter);


        }
    }

}
