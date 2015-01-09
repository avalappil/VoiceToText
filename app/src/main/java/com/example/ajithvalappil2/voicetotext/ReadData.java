package com.example.ajithvalappil2.voicetotext;

import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by ajithvalappil2 on 1/6/15.
 */
public class ReadData extends Thread{

    public InputStream inStream = null;
    String inpMsg = null;
    TextView voiceToText = null;
    TextToSpeech textToSpeech;

    public void run(){
        textToSpeech.speak("Welcome", TextToSpeech.QUEUE_FLUSH, null);
        while (true){
            try {
                Thread.sleep(100);
            }catch(Exception e){

            }
            try{
                inpMsg = null;

            }catch(Exception e1){
                e1.printStackTrace();
            }
            try {
                int i;
                char c;
                while (inStream!=null && (i=inStream.read())!=-1){ //Check if there is an available byte to read
                    c = (char)i; //Conduct a serial read
                    if (c=='#'){
                        break;
                    }
                    inpMsg = inpMsg.concat(String.valueOf(c));
                }

                if (inStream!=null && inpMsg!=null && !"".equals(inpMsg)){
                    System.out.println("Ajith>>" + inpMsg);
                    System.out.println(">>> " + inpMsg);
                    textToSpeech.speak(inpMsg, TextToSpeech.QUEUE_ADD, null);
                    try{
                        Thread.sleep(2000);
                    }catch(Exception dd){
                        dd.printStackTrace();
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
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

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    public void setTextToSpeech(TextToSpeech textToSpeech) {
        this.textToSpeech = textToSpeech;
    }

    public TextView getVoiceToText() {
        return voiceToText;
    }

    public void setVoiceToText(TextView voiceToText) {
        this.voiceToText = voiceToText;
    }
}
