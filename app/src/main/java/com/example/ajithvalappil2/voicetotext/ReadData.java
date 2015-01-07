package com.example.ajithvalappil2.voicetotext;

import android.speech.tts.TextToSpeech;

import java.io.InputStream;

/**
 * Created by ajithvalappil2 on 1/6/15.
 */
public class ReadData extends Thread{

    boolean executeLoop = false;
    public InputStream inStream = null;
    String inpMsg = "";
    TextToSpeech textToSpeech;

    public void run(){
        inpMsg = "";
        try {
            int i;
            char c;
            while (inStream!=null && (i=inStream.read())!=-1){ //Check if there is an available byte to read
                c = (char)i; //Conduct a serial read
                if (c=='#'){
                    break;
                }
                inpMsg = inpMsg.concat(String.valueOf(c));
                try {
                    Thread.sleep(10);
                }catch(Exception e){

                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        if (inpMsg!=null){
            System.out.println("Ajith>>" + inpMsg);
            /**
             * Speaks the string using the specified queuing strategy and speech parameters.
             */
            if (null == inpMsg || "".equals(inpMsg)) {
                inpMsg = "Please give me some input in below text field.";
            }
            //textToSpeech.speak(inpMsg, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public boolean isExecuteLoop() {
        return executeLoop;
    }

    public void setExecuteLoop(boolean executeLoop) {
        this.executeLoop = executeLoop;
    }

    public InputStream getInStream() {
        return inStream;
    }

    public void setInStream(InputStream inStream) {
        this.inStream = inStream;
    }

    public String getInpMsg() {
        return inpMsg;
    }

    public void setInpMsg(String inpMsg) {
        this.inpMsg = inpMsg;
    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    public void setTextToSpeech(TextToSpeech textToSpeech) {
        this.textToSpeech = textToSpeech;
    }
}
