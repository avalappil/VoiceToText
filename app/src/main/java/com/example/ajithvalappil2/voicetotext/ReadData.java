package com.example.ajithvalappil2.voicetotext;

import java.io.InputStream;

/**
 * Created by ajithvalappil2 on 1/6/15.
 */
public class ReadData extends Thread  {

    boolean executeLoop = false;
    public InputStream inStream = null;
    String inpMsg = "";

    public void run(){
        System.out.println("********************************************************************");
        try {
            int i;
            char c;
            System.out.println("Ajith1>>" + inStream);
            while (inStream!=null && (i=inStream.read())!=-1){ //Check if there is an available byte to read
                c = (char)i; //Conduct a serial read
                if (c=='#'){
                    break;
                }
                inpMsg = inpMsg.concat(String.valueOf(c));
                System.out.println("Ajith2>>" + c + ">>" + inpMsg);
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
}
