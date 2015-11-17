import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTConnect extends Thread{
    private BTConnection btc;
    DataInputStream dis;
    DataOutputStream dos;
    public BTConnect(){
        btc = Bluetooth.waitForConnection();
        dis = btc.openDataInputStream();
	    dos = btc.openDataOutputStream();
    }
    
    public close(){
        try {
	    dis.close();
	    dos.close();
	    Thread.sleep(100);
	    btc.close();
	} catch (IOException ioe) {
	    System.out.println("IOException closing connection:");
	    System.out.println(ioe.getMessage());
	}
    }
	
	
	
	
	    
} 