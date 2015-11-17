package sorterer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.remote.nxt.BTConnector;
import lejos.hardware.Bluetooth;
import lejos.hardware.RemoteBTDevice;
import lejos.remote.nxt.BTConnection;

public class BTConnectEV3 extends Thread {

    private boolean ready = false;
    private boolean done = false;
    private boolean running;

    public void run() {
	BTConnector con = new BTConnector();
	// BTConnection nxt = con.connect("NXT", BTConnection.PACKET);
	String address = "baka";
	for (RemoteBTDevice device : Bluetooth.getLocalDevice().getPairedDevices()) {
	    System.out.println(device.getName());
	    System.out.println(device.getAddress());
	    if (device.getName().toUpperCase().equals("NXT"))
		address = device.getAddress();
	}
	BTConnection nxt = con.connect(address, BTConnection.PACKET);
	if (nxt == null) {
	    System.err.println("Failed to connect to any NXT");
	    System.exit(1);
	}
	DataInputStream dis;
	DataOutputStream dos;
	dis = nxt.openDataInputStream();
	dos = nxt.openDataOutputStream();
	running = true;
	while (running) {
	    try {
		int value = 0;
		if (ready)
		    value = 1;
		ready = false;
		if (done)
		    value = 5;
		done = false;
		dos.writeInt(value);

	    } catch (IOException ioe) {
		System.out.println("IOException reading connection:");
		System.out.println(ioe.getMessage());
	    }

	}
	try {
	    dis.close();
	    dos.close();
	    Thread.sleep(100);
	    nxt.close();

	} catch (IOException ioe) {
	    System.out.println("IOException closing connection:");
	    System.out.println(ioe.getMessage());
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void setReady() {
	ready = true;
    }

    public void setDone() {
	done = false;
    }

    public void close() {
	running = false;
    }

}
