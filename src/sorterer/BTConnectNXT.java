package sorterer;

import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTConnectNXT implements Runnable {
    private BTConnection btc;
    private DataInputStream dis;
    //private DataOutputStream dos;
    private volatile boolean ready = false;
    private volatile boolean done = false;
    private volatile boolean running;
    private final int READY = 1;
    private final int DONE = 5;
/**
 * Opprettholder bluetooth tilkobling og leser int fra host
 */
    public void run() {
	running = true;
	boolean connected = false;
	while (running) {
	    //Venter på bluetooth tilkobling
		btc = Bluetooth.waitForConnection(10000,BTConnection.PACKET);
	    if (btc != null){
		// Åpner io stream når tilkobling  er opprettet 
	    	dis = btc.openDataInputStream();
	    	//dos = btc.openDataOutputStream();
	    	connected= true;
	    	}
	    while (running && connected) {
		try {
		    //leser int og lagrer boolean for henting 
		    int value = dis.readInt();
		    if (value == READY)
			ready = true;
		    if (value == DONE) {
			//avlutter
			done = true;
			running = false;
		    }	
		} catch (IOException ioe) {
		    try {
		    System.out.println("Disconnected");
			dis.close();
			//dos.close();
			Thread.sleep(100);
			btc.close();
			//Går ut av indre loop for vente på ny tilkobling
			connected = false;
		    } catch (IOException e) {
		    	System.out.println("Can't reconnect");
		    } catch (InterruptedException ie) {
			running = false;
		    }
		}
	    }
	}
	try {
	    dis.close();
	    //dos.close();
	    Thread.sleep(100);
	    btc.close();
	} catch (IOException ioe) {
	    System.out.println("IOException closing connection:");
	    System.out.println(ioe.getMessage());
	} catch (InterruptedException e) {
	}
    }
    
/**
 * Sjekker om klarsignal er motatt.
 * @return om klarsignal er motatt.
 */
    public boolean getReady() {
	if (ready) {
	    ready = false;
	    return true;
	} else
	    return false;
    }

    /**
     * Sjekker om avslutt signal er motatt.
     * @return om avslutt signal er motatt.
     */
    public boolean getDone() {
	if (done) {
	    done = false;
	    return true;
	} else
	    return false;
    }
    
/**
 * Stopper run metoden
 */
    public void close() {
	running = false;
    }

}