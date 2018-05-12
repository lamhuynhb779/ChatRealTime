package QuanLyChat;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.ArrayList;

import vn.tut.lamh.chat.LoginActivity;

/**
 * Created by huynh on 12/23/2017.
 */

public class ChatFireBase
{
    private String me;
    private String you;
    private String dulieuchat;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference data;
    ArrayList<String> duLieuDaChat = new ArrayList<>();

    public ChatFireBase(String me, String you) {
        this.me = me;
        this.you = you;
    }

    public ChatFireBase(String me, String you, String dulieuchat) {
        this.me = me;
        this.you = you;
        this.dulieuchat = dulieuchat;
        taoNodeChatGiua2Nguoi();
    }

    private void taoNodeChatGiua2Nguoi() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        data = firebaseDatabase.getReference();
//        data.child(me+you).push().setValue(dulieuchat);
        data.child("nodecon").push().setValue("hello world");
    }

    public ArrayList<String> loadLaiDuLieuDaChat() {

        data.child(me+you).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                duLieuDaChat.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return duLieuDaChat;
    }

}
