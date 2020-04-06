package com.example.iochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.iochat.Constants.UrlConstant;
import com.example.iochat.ForegroundServices.DedicatedThreadService;
import com.example.iochat.ForegroundServices.ServiceConstants;
import com.example.iochat.MessageAdapter.MessageAdapter;
import com.example.iochat.MessageAdapter.message;
import com.example.iochat.Notifications.NotificationConfigObject;
import com.example.iochat.Notifications.SimpleNotification;
import com.example.iochat.Registry.ActivitymainRegistry;
import com.example.iochat.Registry.Observer;
import com.example.iochat.Registry.RegistryMainConstant;
import com.example.iochat.RestClientService.BaseResponse.BaseResponse;
import com.example.iochat.RestClientService.MainService;
import com.example.iochat.dialogs.ConnectionDialog;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ConnectionDialog.NoticeDialogListener, Observer {


    private ProgressBar progressBar;
    private Handler mHandler;
    private Toolbar toolbar;
    private Socket mSocket;
    private static final String TAG = "MainActivity";
    private NotificationConfigObject configObject, configObject2;
    private Context mContext;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;
    private MessageAdapter adapter;
    private ArrayList<message> datas;
    private EditText inputMessage;
    private Button send;
    private ActivitymainRegistry registry;
    public static final String CHANNEL_ID = "channelId";
    public static final String CHANNEL_IDA = "channelIdA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        refreshLayout = findViewById(R.id.main_refresh_layout_id);
        refreshLayout.setOnRefreshListener(refreshListener);
        toolbar = findViewById(R.id.main_ac_toolbar_id);
        progressBar = findViewById(R.id.main_ac_smooth_progressbar_id);
        progressBar.setVisibility(View.GONE);
        mHandler = handler;
        registry = ActivitymainRegistry.getInstance();
        registry.setRegistryFor(RegistryMainConstant.MAIN_HANDLER_KEY, mHandler);



        toolbar.setSubtitle(R.string.not_connected);
        setSupportActionBar(toolbar);

        configObject = new NotificationConfigObject(R.drawable.ic_launcher_foreground, "John Wick",
                "Do You Want to go the nearest Market.");
        configObject2 = new NotificationConfigObject(R.drawable.ic_launcher_foreground,
                "Picture Download", "Download in Progress");


        mRecyclerView = findViewById(R.id.main_ac_recycler_view_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        datas = new ArrayList<>();

        inputMessage = findViewById(R.id.main_ac_message_id);
        send = findViewById(R.id.main_ac_send_id);

        send.setOnClickListener(v -> {
            if(!inputMessage.getText().toString().isEmpty() && (mSocket != null)){
              JSONObject object = new JSONObject();
                try {
                    object.put("id", Build.MODEL.toString());
                    object.put("ms", inputMessage.getText().toString());
                    mSocket.emit("ms", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.arg1 == 0){
                NotificationConfigObject config = new NotificationConfigObject(R.drawable.ic_launcher_foreground, "Service", "The Service Has Been Killed.");
                SimpleNotification.showNotification(mContext, CHANNEL_ID, config);
                Log.i(TAG, "args is 0");
            }else if(msg.arg1 == 1){
                NotificationConfigObject config = new NotificationConfigObject(R.drawable.ic_launcher_foreground, "Service", "The Service Has Been Started.");
                SimpleNotification.showNotification(mContext, CHANNEL_ID, config);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        showConnectionDialog();

    }
    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(TAG, "msg");
            mHandler.postDelayed(() -> {refreshLayout.setRefreshing(false);}, 1000);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.simple_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_connect_id:
                showConnectionDialog();
                return true;
            case R.id.menu_item_show_notification_id:
                initializeNotification();
                return true;
            case R.id.menu_item_download_progress_id:
                initializeProgressNotification();
                return true;
            case R.id.menu_item_service_start_id:
                startExampleService();
                return true;
            case R.id.menu_item_service_stop_id:
                stopExampleService();
                return true;
            default:
                return true;
        }
    }

    private void startExampleService(){
        Intent serviceIntent = new Intent(MainActivity.this, DedicatedThreadService.class);
        serviceIntent.putExtra(ServiceConstants.textKey, "start_service");
        startService(serviceIntent);
    }

    private void stopExampleService(){
        Intent serviceIntent = new Intent(MainActivity.this, DedicatedThreadService.class);
        serviceIntent.putExtra(ServiceConstants.textKey, "stop_service");
        stopService(serviceIntent);
    }

    private void initializeNotification(){
        try{
            SimpleNotification.showNotification(mContext, CHANNEL_ID, configObject);
        }catch (Exception e){
            Log.i(TAG, "Notification Error", e);
        }
    }
    private void initializeProgressNotification(){
        try{
            SimpleNotification.showProgressNotification(mContext, CHANNEL_IDA, configObject2 );
        }catch (Exception e){
            Log.i(TAG, "Progress.", e);
        }
    }

    @Override
    public void onDialogNegativeButtonClick(int which) {

    }

    @Override
    public void onDialogPositiveButtonCLick(int which) {
        connectNoIo();
    }

    public void showConnectionDialog() {
        ConnectionDialog dialog = new ConnectionDialog();
        dialog.show(getSupportFragmentManager(), "NoticeConnectionFragment");
    }

    public void connectNoIo(){
        progressBar.setVisibility(View.VISIBLE);
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = false;
            mSocket = IO.socket(UrlConstant.getIOUrl(), opts);
            mSocket.on(Socket.EVENT_CONNECT, args -> {
                Log.i(TAG, "onNewMessage");
                mHandler.postDelayed(() -> {toolbar.setSubtitle("Connected");}, 100);
                mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
            }).on(Socket.EVENT_DISCONNECT, args -> {
                Log.i(TAG, "onDisconnect");
                mHandler.postDelayed(() -> {toolbar.setSubtitle("Not Connected.");}, 100);
                mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
            }).on(Socket.EVENT_RECONNECT, args -> {
                Log.i(TAG, "onReconnect");
                mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
            }).on(Socket.EVENT_RECONNECT_ATTEMPT, args -> {
                Log.i(TAG, "Reconnect_Attempt");
                mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
            }).on(Socket.EVENT_RECONNECTING, args -> {
                Log.i(TAG, "Event_Reconnecting");
                mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
            }).on(Socket.EVENT_RECONNECT_ERROR, args -> {
                Log.i(TAG, "Event_Reconnect_Error");
                mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
            }).on("ms", args -> {
                JSONObject obj = (JSONObject)args[0];
                try {
                    message msg = new message(obj.get("id").toString(), obj.get("ms").toString());
                    datas.add(msg);
                    mHandler.postDelayed(() -> {adapter.setMessags(datas);}, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            Log.i(TAG, "IOConnection");
            mSocket.connect();
        } catch (URISyntaxException e) {
            Log.i(TAG, "IOSocket Error", e);
            mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
        }
    }


    public void connectNow(){

        progressBar.setVisibility(View.VISIBLE);

        Call<BaseResponse> responseCall = MainService.getApiService().getBaseResponse();
        responseCall.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful()){
                    Log.i(TAG, "successful");
                    mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
                }

                if(!response.isSuccessful()){
                    Log.i(TAG, "unsuccessful");
                    mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.i(TAG, "failure", t);
            }
        });       mHandler.postDelayed(() -> {progressBar.setVisibility(View.GONE);}, 1000);

    }

    @Override
    public void onUpdate(String event, Object object) {
        Log.i(TAG, "eventType: " + event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registry.onDestroy();
    }
}
