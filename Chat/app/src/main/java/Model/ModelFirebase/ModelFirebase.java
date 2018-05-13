package Model.ModelFirebase;

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

/**
 * Created by huynh on 5/12/2018.
 */

public class ModelFirebase
{
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mData;
    ArrayList<User> userArrayList;

    public ModelFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        mData = firebaseDatabase.getReference();
        userArrayList = new ArrayList<>();
    }

}
