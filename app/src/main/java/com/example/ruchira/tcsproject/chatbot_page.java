package com.example.ruchira.tcsproject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.Graphmaster;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;
import org.alicebot.ab.Timer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class chatbot_page extends AppCompatActivity {
    private ListView mListView;
    private Button mButtonSend;
    private EditText mEditTextMessage;
    private Button bDeleteMessage;
    private Button bSaveMessage;
    private Button bChatHistory;
    public String mimic;
    public TextView humantext;
    public String message;
    public String response;
    private SQLiteDatabase db;
    String username;
    String bal;
    // String chatbalance;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private Cursor c;


    // public static final String PreferenceSaveChat = "Save chat save";
    private Spinner spinner;
    // private ImageView mImageView;
    public Bot bot;
    public static Chat chat;
    private ChatMessageAdapter mAdapter;
    private ImageButton speak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_page);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        username = getIntent().getExtras().getString("username");
       bal = getIntent().getExtras().getString("Balance");
        speak=(ImageButton)findViewById(R.id.speak);

        final DatabaseHelper helper = new  DatabaseHelper(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        mButtonSend = (Button) findViewById(R.id.btn_send);
        mEditTextMessage = (EditText) findViewById(R.id.et_message);
        // mImageView = (ImageView) findViewById(R.id.iv_image);
        //bSaveMessage=(Button)findViewById(R.id.button7);
        //bDeleteMessage = (Button)findViewById(R.id.button6);
        // bChatHistory = (Button) findViewById(R.id.button8);
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        mListView.setAdapter(mAdapter);
        String[] items = new String[] {"Clear Chat","Save last message","See last saved message"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);
        mimicOtherMessage("Hi,We are TEAM1 ready to help you");

        //code to clear/save/show chat

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    ClearChat();
                }
                else if (position == 1){
                    SaveMessage();
                }
                else if (position== 2)
                {
                    ShowMessage();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//code for voice to text

   speak.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           promptSpeechInput();

       }
   });


        //code for sending the message
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = mEditTextMessage.getText().toString();
                // String aimlmessage = getAIML(message);
                response =chat.multisentenceRespond(message);
                if(TextUtils.isEmpty(message)){
                    return;
                }
                sendMessage(message);

                //code for getting values fro database



                if (response.contains("PRICE+")) {
                   // String s = helper.getDataForChat(username);
                   // helper.close();

                    response = bal;
                    mimicOtherMessage(response);


                } else if (response.contains("ACCOUNT+")) {
                    String s = helper.getAcc(username);
                    helper.close();
                    response = s;
                    mimicOtherMessage(response);

                } else {


                    mimicOtherMessage(response);
                }


                mEditTextMessage.setText("");
                mListView.setSelection(mAdapter.getCount() - 1);


            }
        });



        boolean a =  isSDCARDAvailable();
        //receiving the assets from the app directory
        AssetManager assets = getResources().getAssets();
        File jayDir = new File(Environment.getExternalStorageDirectory().toString() + "/Team1/bots/Ruchira");
        boolean b = jayDir.mkdirs();
        if (jayDir.exists()) {
            //Reading the file
            try {
                for (String dir : assets.list("Ruchira")) {
                    File subdir = new File(jayDir.getPath() + "/" + dir);
                    boolean subdir_check = subdir.mkdirs();
                    for (String file : assets.list("Ruchira/" + dir)) {
                        File f = new File(jayDir.getPath() + "/" + dir + "/" + file);
                        if (f.exists()) {
                            continue;
                        }
                        InputStream in = null;
                        OutputStream out = null;
                        in = assets.open("Ruchira/" + dir + "/" + file);
                        out = new FileOutputStream(jayDir.getPath() + "/" + dir + "/" + file);
                        //copy file from assets to the mobile's SD card or any secondary memory
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //get the working directory

        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/Team1";
        System.out.println("Working Directory = " + MagicStrings.root_path);
        AIMLProcessor.extension =  new PCAIMLProcessorExtension();
        //Assign the AIML files to bot for processing
        bot = new Bot("Ruchira", MagicStrings.root_path, "chat");
        chat = new Chat(bot);
        //for info from java to aiml
        String[] args = null;
        mainFunction(args);






    }


    protected void SavePreferences(String key,String value){
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor =data.edit();

        editor.putString(key,value);
        editor.commit();
    }
    protected void LoadPreferences(){
        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this);
        ChatMessage HumanMessage = new ChatMessage(data.getString("Human","None Available"),true,false);
        ChatMessage BotMessage = new ChatMessage(data.getString("Bot","None Available"),false,false);


        mAdapter.add(HumanMessage);
        mAdapter.add(BotMessage);
        mAdapter.notifyDataSetChanged();
    }




    //code to send message
    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);

    }

    //code for chatbot message from the computer side
    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }

    //code for chatbot message from the human side
    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }
    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }

    //code to clear chat
    private void ClearChat(){
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();

    }

    //code to save message

    private void SaveMessage(){

        SavePreferences("Human",message);
        SavePreferences("Bot",response);

        Toast.makeText(getApplicationContext(),"Chat Saved",Toast.LENGTH_SHORT).show();


    }

    //code to show message
    private void ShowMessage(){
        ClearChat();
        LoadPreferences();

    }
    //to check if sd card is present
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true :false;
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {


        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        while (read != -1) {
            out.write(buffer, 0, read);
            read = in.read(buffer);
        }


    }
    //to see in the terminal if the code is running correct or not

    public static void mainFunction (String[] args) {
        MagicBooleans.trace_mode = false;
        System.out.println("trace mode = " + MagicBooleans.trace_mode);
        Graphmaster.enableShortCuts = true;
        Timer timer = new Timer();
        String request = "Hello.";
        String response = chat.multisentenceRespond(request);

        System.out.println("Human: "+request);
        System.out.println("Robot: " + response);

    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mEditTextMessage.setText(result.get(0));
                }
                break;
            }

        }
    }

}


