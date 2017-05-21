package de.hbrs.willi_pustowalow.scanbluetoothdevices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ScanActivity extends AppCompatActivity {

    private Button scanBtn = null;
    private TextView devList = null;

    BluetoothAdapter bAdapt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scanBtn = (Button) findViewById(R.id.scanBtn);
        devList = (TextView) findViewById(R.id.devList);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(bReciever, filter);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                devList.setText("");

                bAdapt = BluetoothAdapter.getDefaultAdapter();
                if (bAdapt == null) {
                    devList.setText("Bluetooth Adaptor not found!\n");
                } else {
                    enableBluetooth();
                    devList.append(bAdapt.getName() + "\n");
                    devList.append(bAdapt.getAddress() + "\n");
                    devList.append("\n");
                    bAdapt.startDiscovery();
                }
            }
        });
    }

    private void enableBluetooth()
    {
        if(!bAdapt.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1000);
        }
    }

    private BroadcastReceiver bReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String act = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(act)){

                BluetoothDevice bDev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devList.append(bDev.getName() + "\n");
                devList.append(bDev.getAddress() + "\n");
                devList.append("\n");
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(act)){
                devList.append("Discavery finisched\n");
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(act)){
                devList.append("Discavery started\n");
            }
        }

    };

}
