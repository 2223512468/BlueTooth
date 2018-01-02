package com.jajahome;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 1.打开蓝牙(设备支持蓝牙功能)
 * 2.搜索周边设备
 * 3.连接设备
 * 4.数据的传输
 */
public class MainAty extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView listview;
    private ListAdapter listAdapter;
    private BluetoothAdapter adapter;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    listAdapter.addDatas(device);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Toast.makeText(MainAty.this, "开始搜索蓝牙", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(MainAty.this, "搜索蓝牙结束", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_aty);
        listview = (ListView) findViewById(R.id.lv);
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter != null && !adapter.isDiscovering()) {
                    listAdapter.clear();
                    adapter.startDiscovery();
                }
            }
        });
        listAdapter = new ListAdapter(this);
        listview.setAdapter(listAdapter);
        adapter = BluetoothAdapter.getDefaultAdapter();
        listview.setOnItemClickListener(this);
        if (adapter != null) {
            //设备支持蓝牙
            if (!adapter.isEnabled()) {
                //说明蓝牙没有打开，提示用户打开蓝牙
                // 1.强制打开蓝牙 adapter.enable();

                //询问用户打开蓝牙
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 0x001);
            }
        } else {
            Toast.makeText(this, "此设备不支持蓝牙功能", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到蓝牙设备的action
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始搜索蓝牙设备
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//结束蓝牙搜索
        registerReceiver(receiver, filter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //蓝牙已经打开
            Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED) {
            //未打开
            System.exit(0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        BluetoothDevice deceive = (BluetoothDevice) listAdapter.getItem(position);
        Intent intent = new Intent(this, ConcatAct.class);
        intent.putExtra("deceive", deceive);
        startActivity(intent);
    }
}
