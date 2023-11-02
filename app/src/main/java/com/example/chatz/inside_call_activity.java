//package com.example.chatz;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.view.GestureDetector;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Toast;
//
//import com.example.chatz.databinding.ActivityInsideCallBinding;
//
//import io.agora.rtc2.ChannelMediaOptions;
//import io.agora.rtc2.Constants;
//import io.agora.rtc2.IRtcEngineEventHandler;
//import io.agora.rtc2.RtcEngine;
//import io.agora.rtc2.RtcEngineConfig;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import io.agora.rtc2.Constants;
//import io.agora.rtc2.IRtcEngineEventHandler;
//import io.agora.rtc2.RtcEngine;
//import io.agora.rtc2.RtcEngineConfig;
//import io.agora.rtc2.ChannelMediaOptions;
//
//public class inside_call_activity extends AppCompatActivity {
//    ActivityInsideCallBinding binding;
//    //for calling
//    private static final int PERMISSION_REQ_ID = 22;
//    private static final String[] REQUESTED_PERMISSIONS =
//            {
//                    Manifest.permission.RECORD_AUDIO
//            };
//
//    private boolean checkSelfPermission()
//    {
//        if (ContextCompat.checkSelfPermission(this, REQUESTED_PERMISSIONS[0]) !=  PackageManager.PERMISSION_GRANTED)
//        {
//            return false;
//        }
//        return true;
//    }
//    void showMessage(String message) {
//        runOnUiThread(() ->
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show());
//    }
//    // Fill the App ID of your project generated on Agora Console.
//    private final String appId = "411b52a20fd44e34abf97c3d2b816aa1";
//    // Fill the channel name.
//    private String channelName = "chatz";
//    // Fill the temp token generated on Agora Console.
//    private String token = "007eJxTYHjwcc+hlwcktnicftS69LtK4OT0C3vzbrnfrSiZZnxJvCxNgcHMJDUtycDcNNHYLNnE1Ng8ycjCwMw8xdQwKc0kNdUyceruhJSGQEaGMJsJrIwMEAjiszIkZySWVDEwAACIfCLF";
//    // An integer that identifies the local user.
//    private int uid = 0;
//    // Track the status of your connection
//    private boolean isJoined = false;
//
//    // Agora engine instance
//    private RtcEngine agoraEngine;
//    private void setupVoiceSDKEngine() {
//        try {
//            RtcEngineConfig config = new RtcEngineConfig();
//            config.mContext = getBaseContext();
//            config.mAppId = appId;
//            config.mEventHandler = mRtcEventHandler;
//            agoraEngine = RtcEngine.create(config);
//        } catch (Exception e) {
//            throw new RuntimeException("Check the error.");
//        }
//    }
//    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
//        @Override
//        // Listen for the remote user joining the channel.
//        public void onUserJoined(int uid, int elapsed) {
//            runOnUiThread(()->binding.call.setText("Remote user joined: " + uid));
//        }
//        @Override
//        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
//            // Successfully joined a channel
//            isJoined = true;
//            showMessage("Joined Channel " + channel);
//            runOnUiThread(()->binding.call.setText("Waiting for a remote user to join"));
//        }
//        @Override
//        public void onUserOffline(int uid, int reason) {
//            // Listen for remote users leaving the channel
//            showMessage("Remote user offline " + uid + " " + reason);
//            if (isJoined) runOnUiThread(()->binding.call.setText("Waiting for a remote user to join"));
//        }
//        @Override
//        public void onLeaveChannel(RtcStats 	stats) {
//            // Listen for the local user leaving the channel
//            runOnUiThread(()->binding.call.setText("Press the button to join a channel"));
//            isJoined = false;
//        }
//    };
//    private void joinChannel() {
//        ChannelMediaOptions options = new ChannelMediaOptions();
//        options.autoSubscribeAudio = true;
//        // Set both clients as the BROADCASTER.
//        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER;
//        // Set the channel profile as BROADCASTING.
//        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING;
//
//        // Join the channel with a temp token.
//        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
//        agoraEngine.joinChannel(token, channelName, uid, options);
//    }
//    public void joinLeaveChannel(View view) {
//        if (isJoined) {
//            agoraEngine.leaveChannel();
//        } else {
//            joinChannel();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        binding = ActivityInsideCallBinding.inflate(getLayoutInflater());
//        super.onCreate(savedInstanceState);
//        setContentView(binding.getRoot());
//
//        if (!checkSelfPermission()) {
//            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
//        }
//        setupVoiceSDKEngine();
//    }
//    protected void onDestroy() {
//        super.onDestroy();
//        agoraEngine.leaveChannel();
//
//        // Destroy the engine in a sub-thread to avoid congestion
//        new Thread(() -> {
//            RtcEngine.destroy();
//            agoraEngine = null;
//        }).start();
//    }
//}
