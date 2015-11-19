package sorterer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class BTConnectNXT implements Runnable {
    private BTConnection btc;
    private DataInputStream dis;
    private DataOutputStream dos;
    private volatile boolean ready = false;
    private volatile boolean done = false;
    private volatile boolean running;
    private final int READY = 1;
    private final int DONE = 5;

    public void run() {
	running = true;
	boolean connected = false;
	while (running) {
		btc = Bluetooth.waitForConnection(10000,BTConnection.PACKET);
	    if (btc != null){
	    	dis = btc.openDataInputStream();
	    	dos = btc.openDataOutputStream();
	    	connected= true;
	    	}
	    while (running && connected) {
		try {
		    int value = dis.readInt();
		    if (value == READY)
			ready = true;
		    if (value == DONE) {
			done = true;
			running = false;
		    }	
		} catch (IOException ioe) {
		    try {
		    System.out.println("Disconnected");
			dis.close();
			dos.close();
			Thread.sleep(100);
			btc.close();
			connected = false;
		    } catch (IOException e) {
		    	System.out.println("Cant reconnect");
		    } catch (InterruptedException ie) {
			running = false;
		    }
		}
	    }
	}
	try {
	    dis.close();
	    dos.close();
	    Thread.sleep(100);
	    btc.close();
	} catch (IOException ioe) {
	    System.out.println("IOException closing connection:");
	    System.out.println(ioe.getMessage());
	} catch (InterruptedException e) {
	}
    }

    public boolean getReady() {
	if (ready) {
	    ready = false;
	    return true;
	} else
	    return false;
    }

    public boolean getDone() {
	if (done) {
	    done = false;
	    return true;
	} else
	    return false;
    }

    public void close() {
	running = false;
    }

}