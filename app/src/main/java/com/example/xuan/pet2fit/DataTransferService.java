package com.example.xuan.pet2fit;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Xuan on 4/5/2016.
 */
public class DataTransferService extends IntentService {
    public DataTransferService() {
        super("DataTransferService");
    }

    public DataTransferService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int health = ThePet.getCurrentHealth();
        int attack = 5;

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        String host_name = intent.getExtras().getString("host");
        int port_number = intent.getExtras().getInt("port");
        String message = intent.getExtras().getString("message");
        boolean done = false;

        String log = "";

        while (!done) {
            try {
                socket = new Socket(host_name, port_number);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));

                out.println(attack);

                String s;
                while ((s = in.readLine()) != null) {
                    System.out.println("I am client and my health is " + health);
                    System.out.println("Server attack " + s);

                    log = log + "My health is " + health + ". ";
                    log = log + "Enemy attacks " + s + ".\n";

                    if (Integer.parseInt(s) == 0) {
                        done = true;
                        System.out.println("I am client, server died and I won!");
                        log = log + "Enemy died. Hooray! I won!\n";
                        break;
                    }

                    health = health - Integer.parseInt(s);
                    if (health <= 0) {
                        attack = 0;
                        done = true;
                        out.println(attack);
                        System.out.println("I am client and I died");
                        log = log + "I died... Sadly enemy won.\n";
                        break;
                    }
                    out.println(attack);
                }
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: " + host_name);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for "
                        + "the connection to: " + host_name);
                System.exit(1);
            } finally {
                if (out != null)
                    out.close();
                try {
                    if (in != null)
                        in.close();
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {}
            }
        }

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            Messenger messenger = (Messenger) bundle.get("messenger");
            Message msg = Message.obtain();
            bundle.putString("log", log);
            msg.setData(bundle);
            try {
                messenger.send(msg);
            } catch (RemoteException e) {}
        }
    }
}
