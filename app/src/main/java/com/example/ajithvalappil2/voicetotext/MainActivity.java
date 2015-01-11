package com.example.ajithvalappil2.voicetotext;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.*;
import android.app.AlertDialog;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import java.io.InputStream;
import java.util.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.OutputStream;
import android.speech.tts.TextToSpeech;
import android.os.Handler;
import android.os.Message;


public class MainActivity extends ActionBarActivity{

    Button connectBlu;
    TextView voiceToText;
    ListView mBluAdapter;
    LinearLayout aMainLayout;
    LinearLayout aBluListLayout;
    ArrayAdapter<String> mArrayAdapter;
    static List<String> items = new ArrayList<String>();
    private SpeechRecognizer speech = null;
    public OutputStream outStream = null;
    public InputStream inStream = null;
    static boolean processingComplete = false;
    static final int REQUEST_ENABLE_BT = 0;
    boolean showConnectedMsg = false;
    int selectedBlueToothDevices = 0;
    String inMsg = "";
    boolean executeLoop = true;
    String message = "";
    boolean blConnTaskCom = false;
    //speech data
    private final int REQ_CODE_SPEECH_INPUT = 100;
    BluetoothController aBluetoothController = new BluetoothController();
    public BluetoothAdapter btAdapter = null;
    public BluetoothSocket btSocket = null;
    public static String address = "88:C9:D0:94:DE:3F";
    static boolean isDevicesConnected = false;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private boolean mIsListening;
    ReadData aReadData = new ReadData();


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();

            if (bundle.containsKey("connected")){
                String msgData  = bundle.getString("connected");
                System.out.println("Complete.....>> " + msgData);
                Button connectBlu=(Button)findViewById(R.id.button);
                if (msgData!=null && msgData.equalsIgnoreCase("Connected")){
                    connectBlu.setText("Disconnect");
                    btSocket = aBluetoothController.getBtSocket();
                    outStream = aBluetoothController.getOutStream();
                    inStream = aBluetoothController.getInStream();
                    aReadData.setHandler(handler);
                    aReadData.setBtSocket(btSocket);
                    aReadData.setInStream(inStream);
                    aReadData.start();
                }else if (msgData!=null && msgData.equalsIgnoreCase("Disconnected")){
                    connectBlu.setText("Connect");
                }
                System.out.println("Complete.....");
            }
            if (bundle.containsKey("message")){
                String msgData  = bundle.getString("message");
                System.out.println("Complete.....>> " + msgData);
                TextView voiceToText = (TextView)findViewById(R.id.textView);
                voiceToText.append("Controller:" + msgData);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        SpeechRecognitionListener listener = new SpeechRecognitionListener();
        mSpeechRecognizer.setRecognitionListener(listener);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        connectBlu=(Button)findViewById(R.id.button);
        voiceToText=(TextView)findViewById(R.id.textView);
        mBluAdapter = (ListView)findViewById(R.id.listView);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, items);
        mBluAdapter.setAdapter(mArrayAdapter);
        mBluAdapter.setChoiceMode(mBluAdapter.CHOICE_MODE_SINGLE);
        aMainLayout = (LinearLayout)findViewById(R.id.mainlayout);
        aBluListLayout = (LinearLayout)findViewById(R.id.bluToothList);
        try {
            System.out.println("Starting.....");
            aBluetoothController.setProcessType("init");
            System.out.println("init.....");
            System.out.println("Thread started.....");
            aBluetoothController.start();
            System.out.println("wait for complete started.....");
            aBluetoothController.join();
            System.out.println("Complete.....");
            System.out.println("aBluetoothController.isDeviceHasBluetooth() >>" + aBluetoothController.isDeviceHasBluetooth());
            System.out.println("aBluetoothController.isDeviceBluetoothIsOn() >>" + aBluetoothController.isDeviceBluetoothIsOn());
            if (aBluetoothController.isDeviceHasBluetooth()){
                if (!aBluetoothController.isDeviceBluetoothIsOn()){
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    btAdapter = aBluetoothController.getBtAdapter();
                }else{
                    btAdapter = aBluetoothController.getBtAdapter();
                }
                aBluetoothController = new BluetoothController();
                aBluetoothController.setBtAdapter(btAdapter);
                aBluetoothController.setProcessType("getlist");
                System.out.println("init.....");
                System.out.println("Thread started.....");
                aBluetoothController.start();
                System.out.println("wait for complete started.....");
            }else{
                finish();
            }
        }catch (Exception ee){
            ee.printStackTrace();
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        System.out.println(item.getTitle());
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openBluetooth(View view) {
        System.out.println("clicked the connect button");
        if (btAdapter!=null){
            try {
                if (isDevicesConnected){
                    try {
                        if (btSocket!=null)
                            btSocket.close();
                        isDevicesConnected = false;
                    } catch (IOException e2) {
                        System.out.println("Fatal Error In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                        isDevicesConnected = false;
                    }
                    connectBlu.setText("Connect");
                    aMainLayout.setVisibility(view.VISIBLE);
                    aBluListLayout.setVisibility(view.INVISIBLE);
                }else{
                    System.out.println("Showing List.....");
                    mBluAdapter.setAdapter(mArrayAdapter);
                    aMainLayout.setVisibility(view.INVISIBLE);
                    aBluListLayout.setVisibility(view.VISIBLE);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void blueSelected(View view) {
        if (btAdapter!=null){
            try {
                int selectedIndex = mBluAdapter.getCheckedItemPosition();
                String selectedDevice  = mArrayAdapter.getItem(selectedIndex);
                if (selectedDevice!=null && selectedDevice.contains("\n")) {
                    String data[] = selectedDevice.split("\n");
                    String deviceName = "";
                    String deviceAddress = "";
                    if (data.length == 2) {
                        deviceName = data[0];
                        deviceAddress = data[1];
                    }
                    address = deviceAddress;
                }
                if (!isDevicesConnected){
                    aBluetoothController = new BluetoothController();
                    aBluetoothController.setBtAdapter(btAdapter);
                    aBluetoothController.setProcessType("setup");
                    aBluetoothController.setHandler(handler);
                    aBluetoothController.address =  MainActivity.address;
                    System.out.println("init.....");
                    System.out.println("Thread started.....");
                    aBluetoothController.start();
                    //readVoiceFromText();
                    aMainLayout.setVisibility(view.VISIBLE);
                    aBluListLayout.setVisibility(view.INVISIBLE);
                    aMainLayout.setVisibility(view.VISIBLE);
                    aBluListLayout.setVisibility(view.INVISIBLE);
                    voiceToText.setText("");
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                    Toast.makeText(this, "Connecting...", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        if (btSocket!=null)
                            btSocket.close();
                        isDevicesConnected = false;
                    } catch (IOException e2) {
                        System.out.println("Fatal Error In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                        isDevicesConnected = false;
                    }
                    connectBlu.setText("Connect");
                    voiceToText.setText("");
                    aMainLayout.setVisibility(view.VISIBLE);
                    aBluListLayout.setVisibility(view.INVISIBLE);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void cancel(View view) {
        aMainLayout.setVisibility(view.VISIBLE);
        aBluListLayout.setVisibility(view.INVISIBLE);
    }


    public void sendMessage(String message){
        byte[] msgBuffer = message.getBytes();
        try {
            if (outStream!=null) {
                outStream.write(msgBuffer);
            }else{
                Toast.makeText(this, "Please connect to a device...", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            System.out.println("In onResume() and an exception occurred during write: " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                if (mSpeechRecognizer != null){
                    mSpeechRecognizer.stopListening();
                    mSpeechRecognizer.destroy();
                }
                try {
                    if (btSocket!=null)
                        btSocket.close();
                    isDevicesConnected = false;
                } catch (IOException e2) {
                    System.out.println("Fatal Error In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
                    isDevicesConnected = false;
                }
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            System.out.println("onBeginingOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
            System.out.println("onEndOfSpeech");
        }

        @Override
        public void onError(int error)
        {
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

            //System.out.println("error = " + error);
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            //System.out.println("onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
            //Log.d(TAG, "onResults"); //$NON-NLS-1$
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches.size()>=0) {
                message = matches.get(0);
                voiceToText.setText("You: " + message + "\n");
                sendMessage("*" + message + "#");
            }
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }

    }

}
