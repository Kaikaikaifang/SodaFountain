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

import com.example.hello.databinding.FragmentFifthBinding;
import com.friendlyarm.FriendlyThings.HardwareControler;

import java.util.Timer;
import java.util.TimerTask;

public class FifthFragment extends Fragment {

    private FragmentFifthBinding binding;
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
                        String data = new String(buffer, 0, res).trim();
                        Message message = mHandler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", data);
                        message.setData(bundle);
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
//            byte[] d = "In Login Fragment.".getBytes();
//            HardwareControler.write(MainActivity.devfd, d);
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

        binding = FragmentFifthBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.usernameEdittext.getText().toString().trim();
                String password = binding.passwordEdittext.getText().toString().trim();

                // 检查用户名和密码是否正确
                if (username.equals("admin") && password.equals("admin")) {
                    NavHostFragment.findNavController(FifthFragment.this)
                            .navigate(R.id.action_FifthFragment_to_AdminFragment);
                } else {
                    HardwareControler.write(MainActivity.devfd, "5".getBytes());
                    NavHostFragment.findNavController(FifthFragment.this)
                            .navigate(R.id.action_FifthFragment_to_FirstFragment);
                }
            }
        });
    }
}