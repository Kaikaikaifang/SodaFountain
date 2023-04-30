package com.example.hello;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.hello.databinding.FragmentSecondBinding;
import com.friendlyarm.FriendlyThings.HardwareControler;
import com.friendlyarm.FriendlyThings.BoardType;

import java.util.Timer;
import java.util.TimerTask;


public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private Timer timer;
    private TimerTask task;
    int BUFSIZE = 512;
    byte[] buffer = new byte[BUFSIZE];
    @Override
    public void onResume() {
        super.onResume();
        // 创建新的 Timer 和 TimerTask 对象
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                int res;
                res = HardwareControler.select(MainActivity.devfd, 0, 0);
                if (res > 0) {
                    res = HardwareControler.read(MainActivity.devfd, buffer, BUFSIZE);
                    if (res > 0) {
                        Message message = mHandler.obtainMessage();
                        message.sendToTarget();
                    }
                }
            }
        };
        // 启动任务
        timer.schedule(task, 0, 500);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 取消任务
        task.cancel();
        timer.cancel();
    }
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message inputMessage) {
//            // Runs on the UI thread
//            NavHostFragment.findNavController(SecondFragment.this)
//                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
            byte[] d = "In Cola Fragment.".getBytes();
            HardwareControler.write(MainActivity.devfd, d);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消任务和定时器，并释放绑定的视图
        task.cancel();
        timer.cancel();
        timer = null;
        binding = null;
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "cola";
                byte[] data = str.getBytes();
                HardwareControler.write(MainActivity.devfd, data);
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_EndFragment);

            }
        });
        binding.buttonSoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "pepsi";
                byte[] data = str.getBytes();
                HardwareControler.write(MainActivity.devfd, data);
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_EndFragment);

            }
        });
    }
}