package sorterer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTConnect extends Thread {
    private BTConnection btc;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean ready = false;
    private boolean done = false;
    private boolean running;

    public void run() {
	btc = Bluetooth.waitForConnection();
	dis = btc.openDataInputStream();
	dos = btc.openDataOutputStream();
	running = true;
	while (running) {
	    try {
		int value = dis.readInt();
		if (value == 1)
		    ready = true;
		if (value == 5)
		    done = true;

	    } catch (IOException ioe) {
		System.out.println("IOException closing connection:");
		System.out.println(ioe.getMessage());
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
	    // TODO Auto-generated catch block
	    e.printStackTrace();
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