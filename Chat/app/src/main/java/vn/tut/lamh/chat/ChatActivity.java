package vn.tut.lamh.chat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import Model.ObjectClass.InfoChat;
import Model.ObjectClass.RowChat;
import Presenter.Firebase.LPresenterFirebase;
import Presenter.SharePreferences.LPresenterMySharePreferences;
import EditImage.XuLyAnh;
import vn.tut.lamh.chat.Firebase.ViewFirebase;
import vn.tut.lamh.chat.ServiceFloatingWidgetLayout.FloatingWidgetService;
import vn.tut.lamh.chat.SharePreferences.ViewSharePreferences;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, ViewFirebase, ViewSharePreferences
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
    private String userName = "", id_user = "";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(LoginActivity.linkConnectServer); // Đây là đường dẫn tên server
        } catch (URISyntaxException e) {}
    }

    /*  Permission request code to draw over other apps  */
    private static final int DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE = 1222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Thưc hiện kết nối lên server
        mSocket.connect();
        //Client on
//        mSocket.on("server-gui-tin-chat", nhanDuLieuChat);
//        mSocket.on("server-gui-anh-chat", nhanAnhChat);

        addControls();
        addEvents();
        //loadLaiDuLieuDaChat();
        lPresenterFirebase.xuLyDuLieuGroupChatTrenFirebase();
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
//        adapterNoiDungChat = new ChatAdapter(ChatActivity.this,R.layout.sample_chat, mangNoiDungChat);
        lvNoiDungChat = (ListView) findViewById(R.id.lvNoiDungChat);
//        lvNoiDungChat.setAdapter(adapterNoiDungChat);
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
        userName = lPresenterMySharePreferences.getInstance(this).getString("USERNAME","");
        id_user = lPresenterMySharePreferences.getInstance(this).getString("ID","");
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

        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK)
                //If permission granted start floating widget service
                startFloatingWidgetService();
            else
                //Permission is not available then display toast
                Toast.makeText(this,
                        getResources().getString(R.string.draw_other_app_permission_denied),
                        Toast.LENGTH_SHORT).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
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
        InfoChat infoChat = new InfoChat();
        infoChat.setUsername(userName);
        infoChat.setId_user(id_user);
        infoChat.setNoidungchat(noidungchat);
        lPresenterFirebase.taoNodeChatGiua2Nguoi("1", infoChat);
    }

    private void xuLyGuiNoiDungChatLenServer(String noidungchat) {
        mSocket.emit("client-gui-tin-chat", noidungchat);
    }

    private boolean laTui(String usernameArriveFromServer) {
        if(usernameArriveFromServer.equals(userName))
            return true;
        return false;
    }

    @Override
    public void dangKyThongCong() {

    }

    @Override
    public void guiNoiDungChat2NguoiThanhCong() {
        Toast.makeText(ChatActivity.this, "Gửi nội dung chat giữa 2 người thành công!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hienThiDanhSachInfoChat(ArrayList<InfoChat> infoChats) {
//        Log.d("infochat", infoChats.get(0).getNoidungchat()+"");
        ArrayList<RowChat> rowChats = new ArrayList<>();
        for (int i = 0; i < infoChats.size(); i++){
            String noidungchat = infoChats.get(i).getNoidungchat();
            String id = infoChats.get(i).getId_user();
            String username = infoChats.get(i).getUsername();
            RowChat rowChat = new RowChat();
            if(id_user.equals(id)) {
                rowChat.setUsernameChat(userName);
                rowChat.setLaTui(true);
            }
            else{
                rowChat.setUsernameChat(username);
                rowChat.setLaTui(false);
            }
            rowChat.setNoiDungChat(noidungchat);
            rowChats.add(rowChat);
        }
        adapterNoiDungChat = new ChatAdapter(ChatActivity.this,R.layout.sample_chat, rowChats);
        lvNoiDungChat.setAdapter(adapterNoiDungChat);
    }

    @Override
    public void hienThiDanhSachThatBai() {

    }

    @Override
    public void dangNhapThanhCong() {

    }

    @Override
    public void dangNhapThatBai() {

    }

    /*  start floating widget service  */
    public void createFloatingWidget(View v){
        //Check if the app has draw over other apps permission or not?
//        This permission is by default available of API < 23. But for API > 23
//        you have to ask for the permission in runtime
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION_REQUEST_CODE);
        }else
//            //If permission is granted start floating widget service
            startFloatingWidgetService();
    }

    /*  Start Floating widget service and finish current activity */
    private void startFloatingWidgetService() {
        startService(new Intent(this, FloatingWidgetService.class));
        finish();
    }
}