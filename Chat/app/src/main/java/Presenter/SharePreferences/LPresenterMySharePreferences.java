package Presenter.SharePreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import Model.ObjectClass.User;
import vn.tut.lamh.chat.SharePreferences.ViewSharePreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by huynh on 5/12/2018.
 */

public class LPresenterMySharePreferences implements IPresenterMySharePreferences
{
    SharedPreferences sharedPreferences;
    String fileName = "SharePreferences";
    ViewSharePreferences viewSharePreferences;

    public LPresenterMySharePreferences(Context context, ViewSharePreferences viewSharePreferences) {
        this.viewSharePreferences = viewSharePreferences;
        sharedPreferences = context.getSharedPreferences(fileName,MODE_PRIVATE);
    }

    @Override
    public void xuLySharedPreferences(Context context, User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d("unique", user.getId());
        editor.putString("ID", user.getId());
        editor.putString("USERNAME", user.getUsername());
        editor.putString("PASSWORD", user.getPassword());
        editor.putBoolean("SAVE",user.getisChecked());
        editor.commit();
    }

    @Override
    public void kiemTraDangNhap(String un, String pw) {
        if(un.equals(sharedPreferences.getString("USERNAME","")) && !un.equals(""))
        {
            if(pw.equals(sharedPreferences.getString("PASSWORD","")) && !pw.equals(""))
            {
                viewSharePreferences.dangNhapThanhCong();
            }
        }
        viewSharePreferences.dangNhapThatBai();
    }

    @Override
    public SharedPreferences getInstance(Context context) {
        return sharedPreferences;
    }
}