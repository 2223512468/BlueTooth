package com.jajahome;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by ${Terry} on 2018/1/2.
 */
public class ConcatAct extends Activity implements View.OnClickListener {

    private TextView tv_receive;
    private EditText sendEt;
    private Button closeBtn, sendBtn;
    private BluetoothSocket bluetoothSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_item);

        tv_receive = (TextView) findViewById(R.id.tv_receive);
        sendEt = (EditText) findViewById(R.id.send_et);
        closeBtn = (Button) findViewById(R.id.close);
        sendBtn = (Button) findViewById(R.id.send);

        closeBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);


        BluetoothDevice device = getIntent().getParcelableExtra("deceive");
        UUID uuid = UUID.fromString("");
        try {
            bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            in = bluetoothSocket.getInputStream();
            out = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private InputStream in;
    private OutputStream out;


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.send:
                String msg = sendEt.getText().toString();
                try {
                    out.write(msg.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.close:
                closeBluetooth();
                break;
        }
    }

    private void closeBluetooth() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeBluetooth();
    }
}
