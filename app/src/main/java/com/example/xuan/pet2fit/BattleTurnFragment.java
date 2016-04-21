package com.example.xuan.pet2fit;


import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * A simple {@link Fragment} subclass.
 */
public class BattleTurnFragment extends Fragment implements WifiP2pManager.ConnectionInfoListener {
    WifiP2pInfo mInfo;
    View root_view;
    Handler handler;

    public BattleTurnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        handler = new LogHandler((BattleLAN)getActivity());

        root_view = inflater.inflate(R.layout.fragment_battle_turn, container, false);
        // Inflate the layout for this fragment
        return root_view;
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        this.mInfo = info;
        System.out.println("onConnectionInfoAvailable");

        // After the group negotiation, we assign the group owner as the file
        // server. The file server is single threaded, single connection server
        // socket.
        if (info.groupFormed && info.isGroupOwner) {
            System.out.println("I am server. Start the server!");
            (new EchoServer()).execute();
        } else if (info.groupFormed) {


            System.out.println("I am not server. I am client.");
            Intent intent = new Intent(getActivity(), DataTransferService.class);
            intent.putExtra("message", "i am client and this is my message!");
            intent.putExtra("host", info.groupOwnerAddress.getHostAddress());
            intent.putExtra("port", 4444);
            intent.putExtra("messenger", new Messenger(handler));

            getActivity().startService(intent);
        }
    }

    private static class LogHandler extends Handler {
        private final WeakReference<BattleLAN> mActivity;

        public LogHandler(BattleLAN activity) {
            mActivity = new WeakReference<BattleLAN>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle reply = msg.getData();
            String result = reply.getString("log");

            TextView log_text;
            log_text = (TextView) mActivity.get().findViewById(R.id.battle_log);

            log_text.setText(result);
        }
    };

    public class EchoServer extends AsyncTask<Void, Void, String> {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        PrintWriter out = null;
        BufferedReader in = null;

        int port;

        @Override
        protected String doInBackground(Void... params) {
            System.out.println("this is in async task");
            // create socket
            port = 4444;

            int health = ThePet.getCurrentHealth();
            int attack = 5;
            boolean done = false;
            String log = "";

            // repeatedly wait for connections, and process
            while (!done) {
                try {
                    serverSocket = new ServerSocket(port);
                    // a "blocking" call which waits until a connection is requested
                    clientSocket = serverSocket.accept();
                    System.out.println("Connection successful");
                    System.out.println("Waiting for input.....");

                    // open up IO streams
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    // waits for data and reads it in until connection dies
                    // readLine() blocks until the server receives a new line from client
                    String s;

                    while ((s = in.readLine()) != null) {
                        System.out.println("i am server and my health is " + health);
                        System.out.println("client attack " + s);

                        log = log + "My health is " + health + ". ";
                        log = log + "Enemy attacks " + s + ".\n";

                        if (Integer.parseInt(s) == 0) {
                            done = true;
                            System.out.println("I am server, client died and I won");
                            log = log + "Enemy died. Hooray! I won!\n";
                            break;
                        }

                        health = health - Integer.parseInt(s);
                        if (health <= 0) {
                            attack = 0;
                            done = true;
                            out.println(attack);
                            System.out.println("I am server and I died.");
                            log = log + "I died... Sadly enemy won.\n";
                            break;
                        }
                        out.println(attack);
                    }

                } catch (IOException e) {}
                finally {
                    if (out != null)
                        out.close();
                    try {
                        if (in != null)
                            in.close();
                        if (clientSocket != null)
                            clientSocket.close();
                    } catch (IOException e) {}
                }
            }
            return log;
        }

        /*
         * Print the log of the battle to the fragment's view
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            TextView log_text;
            log_text = (TextView) getView().findViewById(R.id.battle_log);

            log_text.setText(result);
        }
    }
}
