package plukker;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.remote.nxt.BTConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.remote.nxt.BTConnection;

/**
 * Klasse for bluetooth kommunikasjon på EV3.
 * 
 * @author sindr
 *
 */
public class BTConnectEV3 implements Runnable {
    private volatile boolean ready = false;
    private volatile boolean done = false;
    private volatile boolean running;
    // Adresse til NXTen
    private String address = "00:16:53:0A:57:A7";

    private final int READY = 1;
    private final int DONE = 5;

    public void run() {
	running = true;
	boolean connected = false;
	BTConnector con = new BTConnector();
	BTConnection nxt = null;
	// DataInputStream dis = null;
	DataOutputStream dos = null;
	System.err.close();
	while (running) {
	    // Try to connect until success, then open io streams
	    while (running && !connected) {
		try{
		    nxt = con.connect(address, NXTConnection.PACKET);
		
		if (nxt != null) {
		    // dis = nxt.openDataInputStream();
		    dos = nxt.openDataOutputStream();
		    connected = true;
		}
		Thread.sleep(500);
		}catch (InterruptedException ie){
		    running = false;
		}
	    }
	    while (running && connected) {
		try {
		    // Sender verdi om en boolean er true
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

		    // Behandle disconnect/ioexception
		} catch (IOException ioe) {
		    connected = false;
		    try {
			// dis.close();
			dos.close();
			Thread.sleep(100);
			nxt.close();
			nxt = null;
		    } catch (IOException e) {
		    } catch (InterruptedException e) {
			running = false;
		    }
		}
	    }
	}
	try {
	    if (nxt != null) {
		// dis.close();
		dos.close();
		Thread.sleep(100);
		nxt.close();
	    }
	} catch (IOException ioe) {
	    System.out.println("IOException closing connection:");
	    System.out.println(ioe.getMessage());
	} catch (InterruptedException e) {
	}
    }

    /**
     * Setter at klarsignal skal sendes
     */
    public void setReady() {
	ready = true;
    }

    /**
     * Setter at avsluttsignal skal sendes
     */
    public void setDone() {
	done = true;
    }
}