package com.example.hello;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.hello.databinding.FragmentFirstBinding;
import com.friendlyarm.FriendlyThings.HardwareControler;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Timer timer;
    private TimerTask task;
    int BUFSIZE = 512;
    byte[] buffer = new byte[BUFSIZE];
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        // 取消任务
        task.cancel();
        timer.cancel();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        binding.buttonHave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String str = "H";
//                byte[] data = str.getBytes();
//                HardwareControler.write(MainActivity.devfd, data);
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.welcome);
        binding.languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                if (selectedLanguage.equals("English")) {
                    binding.textviewSecond.setText("Soda Fountain");
                    binding.buttonHave.setText("Please choose your favourite color, and put the cup in the right place\n\uD83D\uDC95: cola\uD83C\uDF7A\n\uD83D\uDC99: coffee☕\n\uD83D\uDC9A: juice\uD83C\uDF79");
                } else {
                    binding.textviewSecond.setText("智能饮料机");
                    binding.buttonHave.setText("请您选取喜欢的颜色，并放置水杯\n\uD83D\uDC95：碳酸饮料\uD83C\uDF7A\n\uD83D\uDC99：咖啡☕\n\uD83D\uDC9A：果汁\uD83C\uDF79");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message inputMessage) {
            // Runs on the UI thread
            Bundle bundle = inputMessage.getData();
            String id = bundle.getString("id");

            switch (id) {
                case "A":
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    break;
                case "B":
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_ThirdFragment);
                    break;
                case "C":
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_ForthFragment);
                    break;
                case "D":
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_FifthFragment);
                    break;
                case "L":
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_EndFragment);
                    break;
                default:
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

}







