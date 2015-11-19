package sorterer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.BTConnection;

public class BTConnectEV3 implements Runnable {

    private volatile boolean ready = false;
    private volatile boolean done = false;
    private volatile boolean running;
    private String address = "00:16:53:0A:57:A7";// Adresse til NXTen
    private final int READY = 1;
    private final int DONE = 5;

    public void run() {
	// TODO Add timout on connection, if necesary
	running = true;
	boolean connected = false;
	BTConnector con = new BTConnector();
	BTConnection nxt = null;
	// DataInputStream dis = null;
	DataOutputStream dos = null;
	while (running) {
	    /* Try to connect until success, then open io streams */
	    while (running && nxt == null) {
		nxt = con.connect(address, BTConnection.PACKET);
	    }
	    connected = true;
	    // dis = nxt.openDataInputStream();
	    dos = nxt.openDataOutputStream();
	    try {
		Thread.sleep(300);
	    } catch (InterruptedException ie) {
		running = false;
	    }
	    while (running && connected) {
		try {
		    int value = 0;
		    if (ready) {
			value = READY;
			ready = false;
		    }
		    if (done) {
			value = DONE;
			running = false;
		    }
		    if (value != 0)
			dos.writeInt(value);
		    /* Handle disconnect/ioexception */
		} catch (IOException ioe) {
		    System.out.println("IOException writing:");
		    System.out.println(ioe.getMessage());
		    connected = false;
		    try {
			// dis.close();
			dos.close();
			Thread.sleep(100);
			nxt.close();
		    } catch (IOException e) {
			System.out.println("IOException reconnecting:");
			System.out.println(ioe.getMessage());
		    } catch (InterruptedException e) {
			running = false;
		    }

		}

	    }
	}
	try {
	    // dis.close();
	    dos.close();
	    Thread.sleep(100);
	    nxt.close();

	} catch (IOException ioe) {
	    System.out.println("IOException closing connection:");
	    System.out.println(ioe.getMessage());
	} catch (InterruptedException e) {

	}
    }

    public void setReady() {
	ready = true;
    }

    // Tell thread to stop
    public void setDone() {
	done = true;
    }
}
