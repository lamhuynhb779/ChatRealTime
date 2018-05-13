package vn.tut.lamh.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import Presenter.SharePreferences.LPresenterMySharePreferences;
import vn.tut.lamh.chat.SharePreferences.ViewSharePreferences;

public class FriendListActivity extends AppCompatActivity implements ViewSharePreferences{

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    ListView friendList;
    LPresenterMySharePreferences lPresenterMySharePreferences;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(LoginActivity.linkConnectServer); // Đây là đường dẫn tên server
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        //Thưc hiện kết nối lên server
        mSocket.connect();
        lPresenterMySharePreferences = new LPresenterMySharePreferences(this, this);

        if(!lPresenterMySharePreferences.getInstance(this).getString("USERNAME","").equals(""))
            mSocket.emit("client-gui-username",lPresenterMySharePreferences.getInstance(this).getString("USERNAME",""));

        //Client on
        mSocket.on("server-gui-username", nhanDanhSachUsername);

        addControls();
        addEvents();


    }

    private void addControls() {
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, arrayList);
        friendList = (ListView) findViewById(R.id.friendList);
        friendList.setAdapter(adapter);
    }

    private void addEvents() {
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(FriendListActivity.this, ChatActivity.class);
                intent1.putExtra("FRIEND",arrayList.get(i).toString());
                startActivity(intent1);
            }
        });
    }

    private Emitter.Listener nhanDanhSachUsername = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray dsun;
                    try {
                        dsun = data.getJSONArray("dsun");

                        arrayList.clear();
                        for(int i=0;i< dsun.length();i++)
                        {
                            arrayList.add(dsun.get(i).toString());
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        return;
                    }

                }
            });
        }
    };

    @Override
    public void dangNhapThanhCong() {

    }

    @Override
    public void dangNhapThatBai() {

    }
}
