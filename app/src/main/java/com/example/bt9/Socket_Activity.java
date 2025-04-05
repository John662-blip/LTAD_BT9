package com.example.bt9;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Set;

public class Socket_Activity extends AppCompatActivity {
    Button btnPaired;
    ListView listDS;
    public static int REQUEST_BLUETOOTH = 1;
    private BluetoothAdapter myBluetooth = null;
    Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        btnPaired = findViewById(R.id.button);
        listDS = findViewById(R.id.listTb);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            Toast.makeText(this, "Thiet Bi Bluetooth chua bat", Toast.LENGTH_SHORT).show();
            finish();
        } else if (!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thiet Bi bluetoot chua bat", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Thiet bi bluetoot da bat", Toast.LENGTH_SHORT).show();
            startActivityForResult(turnBTon, REQUEST_BLUETOOTH);
        }
        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
    }

    private void pairedDevicesList() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 100);
            pairedDevices = myBluetooth.getBondedDevices();
            ArrayList list = new ArrayList<>();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) !=
                            PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Danh sach thiet bi da bat", Toast.LENGTH_SHORT).show();
                        list.add(bt.getName() + "\n" + bt.getAddress());
                    }
                }
            } else {
                Toast.makeText(this, "Khong tim thay thiet bi", Toast.LENGTH_SHORT).show();
            }
            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listDS.setAdapter(adapter);
            listDS.setOnItemClickListener(myListClickListener);
            return;
        }
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String infor = ((TextView) view).getText().toString();
            String address = infor.substring(infor.length() - 17);
            Intent i = new Intent(Socket_Activity.this, BlueControl.class);
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };
}
