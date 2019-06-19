package com.jhz.wifi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class WIFIMainActivity extends AppCompatActivity {
    WifiManager wifiManager;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                List resut = wifiManager.getScanResults();
                if(resut!=null){
                    Log.d("",""+resut.size());
                    Toast.makeText(getApplicationContext(),"扫描到的WiFi："+resut.size(),Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifimain);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission_group.LOCATION)!= PackageManager.PERMISSION_GRANTED){
            // 获取wifi连接需要定位权限,没有获取权限
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
            },127);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 127:
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
            // 允许
                    initData();
                }else{
            // 不允许
                    Toast.makeText(this,"没有权限",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initData() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean canScan = wifiManager.startScan();
        Toast.makeText(getApplicationContext(),"能否调用WiFi扫描"+canScan,Toast.LENGTH_LONG).show();
        IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(mReceiver,intentFilter);
    }

}
