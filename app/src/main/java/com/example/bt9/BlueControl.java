package com.example.bt9;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BlueControl extends AppCompatActivity {
    ImageButton btnTb1,btnTb2,btnDis;
    TextView txt1,txtMac;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    boolean isBtConnected = false;
    Set<BluetoothDevice> pairedDevices1;
    String address = null;
    ProgressDialog progess;
    int flaglamp1;
    int flaglamp2;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(Socket_Activity.EXTRA_ADDRESS);
        setContentView(R.layout.activity_control);

        btnTb1 = findViewById(R.id.btnTb1);
        btnTb2 = findViewById(R.id.btnTb2);
        txt1 = findViewById(R.id.textV1);
        txtMac = findViewById(R.id.textV3);
        btnDis = findViewById(R.id.btnDisc);
        new ConnectBT().execute();
        btnTb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thietBi1();
            }
        });
        btnTb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thietBi7();
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });
    }

    private class ConnectBT extends AsyncTask<Void,Void,Void>{
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progess = ProgressDialog.show(BlueControl.this,"Dang ket noi","Xin vui long doi");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try{
                if (btSocket == null || ! isBtConnected){
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    if (ActivityCompat.checkSelfPermission(BlueControl.this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED){
                        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect();
                    }
                }
            }
            catch (IOException e){
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (!ConnectSuccess){
                msg("Ket noi That bai");
                finish();
            }
            else{
                msg("Ket noi thanh cong");
                isBtConnected = true;
                pairedDevicesList1();
            }
            progess.dismiss();
        }
    }
    private void msg(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    private void thietBi1(){
        if (btSocket!= null){
            try{
                if (this.flaglamp1 == 0){
                    this.flaglamp1 = 1;
                    btSocket.getOutputStream().write("1".toString().getBytes());
                    txt1.setText("1 dang bat");
                }
                else{
                    if (this.flaglamp1 != 1) return;
                    this.flaglamp1 =0;
                    btSocket.getOutputStream().write("A".toString().getBytes());
                    txt1.setText("1 dang tat");
                    return;
                }

            } catch (Exception e) {
                msg("loi");
            }
        }
    }

    private void thietBi7(){
        if (btSocket!= null){
            try{
                if (this.flaglamp2 == 0){
                    this.flaglamp2 = 1;
                    btSocket.getOutputStream().write("7".toString().getBytes());
                    txt1.setText("7 dang bat");
                }
                else{
                    if (this.flaglamp2 != 1) return;
                    this.flaglamp2 =0;
                    btSocket.getOutputStream().write("G".toString().getBytes());
                    txt1.setText("7 dang tat");
                    return;
                }

            } catch (Exception e) {
                msg("loi");
            }
        }
    }
    private void Disconnect(){
        if (btSocket!= null){
            try {
                btSocket.close();
            } catch (IOException e) {
                msg("Loi");
            }
        }
        finish();
    }
    private void pairedDevicesList1(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_CONNECT)!=PackageManager.PERMISSION_GRANTED){
            pairedDevices1 = myBluetooth.getBondedDevices();

            if (pairedDevices1.size()>0){
                for (BluetoothDevice bt : pairedDevices1){
                    txtMac.setText(bt.getName()+" - "+bt.getAddress());
                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Ko tim thay",Toast.LENGTH_LONG).show();
            }
        }
    }
}
