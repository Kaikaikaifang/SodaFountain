package com.example.hello;

import android.media.MediaPlayer;
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
import com.example.hello.databinding.FragmentForthBinding;
import com.friendlyarm.FriendlyThings.HardwareControler;
import java.util.Timer;
import java.util.TimerTask;


public class ForthFragment extends Fragment {

    private FragmentForthBinding binding;
    private Timer timer;
    private TimerTask task;
    int BUFSIZE = 512;
    byte[] buffer = new byte[BUFSIZE];
    private MediaPlayer mediaPlayer;

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
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
                        bundle.putString("message", data);
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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // 取消任务
        task.cancel();
        timer.cancel();
    }
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message inputMessage) {
            Bundle bundle = inputMessage.getData();
            String msg = bundle.getString("message");
            switch (msg) {
                case "S":
                    NavHostFragment.findNavController(ForthFragment.this)
                            .navigate(R.id.action_ForthFragment_to_FirstFragment);
                    break;
                default:
                    NavHostFragment.findNavController(ForthFragment.this)
                            .navigate(R.id.action_ForthFragment_to_EndFragment);
                    break;
            }
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

        binding = FragmentForthBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.coffee);

        binding.buttonLatte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "5";
                byte[] data = str.getBytes();
                HardwareControler.write(MainActivity.devfd, data);
            }
        });
        binding.buttonMocha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "6";
                byte[] data = str.getBytes();
                HardwareControler.write(MainActivity.devfd, data);
            }
        });
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HardwareControler.write(MainActivity.devfd, "S".getBytes());
            }
        });
    }
}