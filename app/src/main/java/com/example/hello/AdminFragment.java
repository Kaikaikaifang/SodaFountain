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

import com.example.hello.databinding.FragmentAdminBinding;
import com.friendlyarm.FriendlyThings.HardwareControler;

import java.util.Timer;
import java.util.TimerTask;

public class AdminFragment extends Fragment {

    private FragmentAdminBinding binding;
    private Timer timer;
    private TimerTask task;
    int BUFSIZE = 512;
    byte[] buffer = new byte[BUFSIZE];
    String type = "1";
    boolean done = true;
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

            switch (type) {
                case "1":
                    binding.messageTest1.setText(msg);
                    break;
                case "2":
                    binding.messageTest2.setText(msg);
                    break;
                case "3":
                    binding.messageColor.setText(msg);
                    break;
                case "4":
                    binding.messageLength.setText(msg);
                    break;
                case "6":
                    binding.messageMargin.setText(msg);
                    break;
                default:
                    break;
            }

            done = true;
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
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.admin);

        binding.buttonTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (done) {
                    binding.messageTest1.setText("测试中，请等待片刻！");
                    done = false;
                    type = "1";
                    HardwareControler.write(MainActivity.devfd, type.getBytes());
                }
            }
        });
        binding.buttonTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (done) {
                    binding.messageTest2.setText("测试中，请等待片刻！");
                    done = false;
                    type = "2";
                    HardwareControler.write(MainActivity.devfd, type.getBytes());
                }
            }
        });
        binding.buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (done) {
                    binding.messageColor.setText("测试中，请等待片刻！");
                    done = false;
                    type = "3";
                    HardwareControler.write(MainActivity.devfd, type.getBytes());
                }
            }
        });
        binding.buttonLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (done) {
                    binding.messageLength.setText("测试中，请等待片刻！");
                    done = false;
                    type = "4";
                    HardwareControler.write(MainActivity.devfd, type.getBytes());
                }
            }
        });
        binding.buttonMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (done) {
                    binding.messageMargin.setText("测试中，请等待片刻！");
                    done = false;
                    type = "6";
                    HardwareControler.write(MainActivity.devfd, type.getBytes());
                }
            }
        });
        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (done) {
                    type = "5";
                    HardwareControler.write(MainActivity.devfd, type.getBytes());
                    NavHostFragment.findNavController(AdminFragment.this)
                            .navigate(R.id.action_AdminFragment_to_FirstFragment);
                }
            }
        });
    }
}