package com.example.hello;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hello.databinding.ActivityMainBinding;
import com.friendlyarm.FriendlyThings.HardwareControler;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public static int devfd = -1;

    @Override
    public void onDestroy() {
        if (devfd != -1) {
            HardwareControler.close(devfd);
            devfd = -1;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.hello.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String devName = "/dev/ttyAMA3";
        int speed = 115200;
        int dataBits = 8;
        int stopBits = 1;
        devfd = HardwareControler.openSerialPort(devName, speed, dataBits, stopBits);
    }
}