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
    TextToSpeech textToSpeech;

    public void run(){
        textToSpeech.speak("Welcome", TextToSpeech.QUEUE_FLUSH, null);
        while (true){
            try{
                inpMsg = "";

            }catch(Exception e1){
                e1.printStackTrace();
            }
            try {
                int i;
                char c;
                inpMsg = "";
                while (inStream!=null && inStream.available() >= 0 && (i=inStream.read())!=-1){ //Check if there is an available byte to read
                    c = (char)i; //Conduct a serial read
                    if (c=='#'){
                        break;
                    }
                    inpMsg = inpMsg.concat(String.valueOf(c));
                }
                System.out.println("Ajith>>" + inpMsg);
                if (inpMsg!=null && !"".equals(inpMsg)){
                    System.out.println("Ajith>>" + inpMsg);
                    System.out.println(">>> " + inpMsg);
                    textToSpeech.speak(inpMsg, TextToSpeech.QUEUE_ADD, null);
                    while(textToSpeech.isSpeaking()){

                    }
                    inpMsg = "";
                }

            }catch(Exception e){
                e.printStackTrace();
                break;
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
}
