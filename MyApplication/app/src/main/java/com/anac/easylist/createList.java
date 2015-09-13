package com.anac.easylist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;


public class createList extends ActionBarActivity implements View.OnClickListener {



    public ListView mList;
    public Button speakButton;
    public String tv;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    ArrayList<String> items;

    String atividade = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        Intent i = getIntent();

        if (i.hasExtra("list")) {
            items = getIntent().getExtras().getStringArrayList("list");
            atividade = "list";
        }
        else if (i.hasExtra("listNew")){
            items = getIntent().getExtras().getStringArrayList("listNew");
            atividade = "listNew";
        }


        //items=getIntent().getExtras().getStringArrayList("list");

        mList = (ListView) findViewById(R.id.list_falar);
        mList.setClickable(true);



        speakButton = (Button) findViewById(R.id.bt_falar);
        speakButton.setOnClickListener(this);
        voiceinputbuttons();



    }

    private void voiceinputbuttons() {
        speakButton = (Button) findViewById(R.id.bt_falar);
        mList = (ListView) findViewById(R.id.list_falar);
    }

    public void startVoiceRecognitionActivity(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Falar");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));

            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String) mList.getItemAtPosition(position);
                    items.add(item);

                    show(items,item);

                }
            });


            if (matches.contains("information")){
                informationmenu();
            }
        }

    }

    private void informationmenu() {
        startActivity(new Intent("cenas"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_list, menu);
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

    @Override
    public void onClick(View v) {
        startVoiceRecognitionActivity();

    }

    public void show(final ArrayList<String> its, final String it)
    {
        final Context c = this;
        final Dialog d = new Dialog(createList.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.custom_dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
     //   Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
//        np.setOnValueChangedListener((NumberPicker.OnValueChangeListener) this);
        //final EditText np = (EditText) d.findViewById(R.id.editText);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tv = String.valueOf(np.getValue());
                //tv.setText(String.valueOf(np.getValue()));
                if (atividade=="list") {
                    Intent i = new Intent(c, Necessarios.class);
                    i.putStringArrayListExtra(atividade, its);
                    i.putExtra("item",it);
                    i.putExtra("quant",tv);
                    startActivity(i);
                }
                else if (atividade == "listNew"){
                    Intent i = new Intent(c, NovaLista.class);
                    i.putStringArrayListExtra(atividade, its);
                    i.putExtra("item",it);
                    i.putExtra("quant",tv);
                    startActivity(i);
                }
                d.dismiss();
            }
        });

        d.show();


    }
}
