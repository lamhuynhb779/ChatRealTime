package Presenter.Firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.ObjectClass.User;
import vn.tut.lamh.chat.Firebase.ViewFirebase;

/**
 * Created by huynh on 5/11/2018.
 */

public class LPresenterFirebase implements IPresenterFirebase {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mData ;
    ViewFirebase viewFirebase;

    public LPresenterFirebase(ViewFirebase viewFirebase) {
        this.viewFirebase = viewFirebase;
        firebaseDatabase = FirebaseDatabase.getInstance();
        mData = firebaseDatabase.getReference();
    }

    @Override
    public void themUserVaoFirebase(User user) {
        mData.child("users").push().setValue(user);
        viewFirebase.dangKyThongCong();
    }

    @Override
    public void taoNodeChatGiua2Nguoi(String roomID, String noidungchat) {
        mData.child(roomID).push().setValue(noidungchat);
    }
}
