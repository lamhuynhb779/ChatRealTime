package vn.tut.lamh.chat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.ArrayList;

import Adapters.ChatAdapter;
import Model.ObjectClass.RowChat;
import Presenter.Firebase.LPresenterFirebase;
import Presenter.SharePreferences.LPresenterMySharePreferences;
import QuanLyChat.ChatFireBase;
import EditImage.XuLyAnh;
import vn.tut.lamh.chat.Firebase.ViewFirebase;
import vn.tut.lamh.chat.SharePreferences.ViewSharePreferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewFirebase, ViewSharePreferences
{
    TextView txtvChatWith;
    private EditText edtNoiDung;
    private Button btnGui, btnHinh;
    private ArrayList<RowChat> mangNoiDungChat;
    private ChatAdapter adapterNoiDungChat;
    private ListView lvNoiDungChat;
    private static final int SELECTED_PICTURE = 1;
    private XuLyAnh xuLyAnh = new XuLyAnh();
    private String chatWith;
    private LPresenterFirebase lPresenterFirebase;
    private LPresenterMySharePreferences lPresenterMySharePreferences;


    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(LoginActivity.linkConnectServer); // Đây là đường dẫn tên server
        } catch (URISyntaxException e) {}
    }

    private ChatFireBase chatFireBase;

    private ArrayList<String> oldData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Thưc hiện kết nối lên server
        mSocket.connect();
        //Client on
        mSocket.on("server-gui-tin-chat", nhanDuLieuChat);
        mSocket.on("server-gui-anh-chat", nhanAnhChat);

        addControls();
        addEvents();
        //loadLaiDuLieuDaChat();
    }

//    private void loadLaiDuLieuDaChat() {
//        chatFireBase = new ChatFireBase(LoginActivity.UN, chatWith);
//        oldData.addAll(chatFireBase.loadLaiDuLieuDaChat());
//        for(int i =0;i<oldData.size();i++)
//        {
//            RowChat chat = new RowChat();
//            chat.setNoiDungChat(oldData.get(i).toString());
//            mangNoiDungChat.add(chat);
//        }
//        adapterNoiDungChat.notifyDataSetChanged();
//    }

    private Emitter.Listener nhanDuLieuChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray infocontentchat;
                    try {
                        infocontentchat = data.getJSONArray("datachat");
                        mangNoiDungChat.add(
                                new RowChat(infocontentchat.get(0).toString(), null, infocontentchat.get(1).toString(),
                                        laTui(infocontentchat.get(0).toString())));
                    } catch (JSONException e) {return;}
                    adapterNoiDungChat.notifyDataSetChanged();
                }
            });
        }
    };

    private Emitter.Listener nhanAnhChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray inFoHinh;
                    try {
                        inFoHinh = data.getJSONArray("datahinh");
                        byte[] imageByteArray = (byte[]) inFoHinh.get(1);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray,0,imageByteArray.length);
                        Drawable drawable = new BitmapDrawable(bitmap);
                        mangNoiDungChat.add(
                                new RowChat(inFoHinh.get(0).toString(), drawable, "", laTui(inFoHinh.get(0).toString())));
                    } catch (JSONException e) {return;}
                    adapterNoiDungChat.notifyDataSetChanged();
                }
            });
        }
    };

    private void addControls() {
        txtvChatWith = (TextView) findViewById(R.id.txtvChatWith);
        mangNoiDungChat = new ArrayList<>();
        adapterNoiDungChat = new ChatAdapter(MainActivity.this,R.layout.sample_chat, mangNoiDungChat);
        lvNoiDungChat = (ListView) findViewById(R.id.lvNoiDungChat);
        lvNoiDungChat.setAdapter(adapterNoiDungChat);
        btnGui = (Button) findViewById(R.id.btnGui);
        edtNoiDung = (EditText) findViewById(R.id.edtNoiDung);
        btnHinh = (Button) findViewById(R.id.btnHinh);
        lPresenterFirebase = new LPresenterFirebase(this);
        lPresenterMySharePreferences = new LPresenterMySharePreferences(this, this);
    }

    private void addEvents() {
        Intent intent = getIntent();
        chatWith =  intent.getStringExtra("FRIEND");
        if(!chatWith.equals(""))
            txtvChatWith.setText(chatWith);
        btnGui.setOnClickListener(this);
        btnHinh.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SELECTED_PICTURE && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData(); // uri = content://media/external/images/media/53162
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]); //0
            String filePath = cursor.getString(columnIndex); // path = /storage/emulated/0/DCIM/Screenshots/ten-hinh.png
            cursor.close();

            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

            //Xử lý chất lượng và kích thước ảnh
            yourSelectedImage = xuLyAnh.resize(yourSelectedImage,600,600);

            byte[] byteArray = xuLyAnh.layMangByteTuBitmap(yourSelectedImage);

            if(byteArray!=null)
                mSocket.emit("client-gui-anh-chat",byteArray);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGui:
                String noidungchat = edtNoiDung.getText().toString();
                xuLyGuiNoiDungChatLenServer(noidungchat);
                xuLyGuiNoiDungChatLenFirebase(noidungchat);
                //Reset lại box noi dung chat
                edtNoiDung.setText("");
                edtNoiDung.requestFocus();
                break;
            case R.id.btnHinh:
                Intent iAlbum = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iAlbum,SELECTED_PICTURE);
                break;
        }
    }

    private void xuLyGuiNoiDungChatLenFirebase(String noidungchat) {
        lPresenterFirebase.taoNodeChatGiua2Nguoi("1", noidungchat);
    }

    private void xuLyGuiNoiDungChatLenServer(String noidungchat) {
        mSocket.emit("client-gui-tin-chat", noidungchat);
    }

    private boolean laTui(String usernameArriveFromServer) {
        if(usernameArriveFromServer.equals(lPresenterMySharePreferences.getInstance(this).getString("USERNAME","")))
            return true;
        return false;
    }

    @Override
    public void dangKyThongCong() {

    }

    @Override
    public void guiNoiDungChat2NguoiThanhCong() {
        Toast.makeText(MainActivity.this, "Gửi nội dung chat giữa 2 người thành công!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dangNhapThanhCong() {

    }

    @Override
    public void dangNhapThatBai() {

    }
}