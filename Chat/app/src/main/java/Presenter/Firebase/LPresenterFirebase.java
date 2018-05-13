package Presenter.Firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import Model.ObjectClass.InfoChat;
import Model.ObjectClass.User;
import vn.tut.lamh.chat.Firebase.ViewFirebase;

/**
 * Created by huynh on 5/11/2018.
 */

public class LPresenterFirebase implements IPresenterFirebase {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mData ;
    ViewFirebase viewFirebase;
    ArrayList<InfoChat> duLieuDaChat;

    public LPresenterFirebase(ViewFirebase viewFirebase) {
        this.viewFirebase = viewFirebase;
        firebaseDatabase = FirebaseDatabase.getInstance();
        mData = firebaseDatabase.getReference();
        duLieuDaChat = new ArrayList<>();
    }

    @Override
    public void themUserVaoFirebase(User user) {
        mData.child("users").push().setValue(user);
        viewFirebase.dangKyThongCong();
    }

    @Override
    public void taoNodeChatGiua2Nguoi(String roomID, InfoChat infoChat) {
        mData.child(roomID).push().setValue(infoChat);
    }

    @Override
    public void xuLyDuLieuGroupChatTrenFirebase() {
        mData.child("1").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                duLieuDaChat.add(dataSnapshot.getValue().toString());
//                Log.d("firebase", dataSnapshot.toString());
//                DataSnapshot x = dataSnapshot.child(dataSnapshot.getKey().toString());
                Log.d("id_user", dataSnapshot.getValue(InfoChat.class)+"");
                duLieuDaChat.add(dataSnapshot.getValue(InfoChat.class));
                if(duLieuDaChat.size()>0) viewFirebase.hienThiDanhSachInfoChat(duLieuDaChat);
                else viewFirebase.hienThiDanhSachThatBai();
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
    }

    @Override
    public void kiemTraDangNhap(final User user) {
        Query query = mData.child("users").orderByChild("id").equalTo("85b9fccc-1be7-437e-b905-d6ed93bcb753");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("query", dataSnapshot.getValue(User.class)+"");
                User ressultUser = dataSnapshot.getValue(User.class);
                String id = ressultUser.getId();
                String username = ressultUser.getUsername();
                String password = ressultUser.getPassword();
                if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                    viewFirebase.dangNhapThanhCong();
                }else viewFirebase.dangNhapThatBai();
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
    }
}
