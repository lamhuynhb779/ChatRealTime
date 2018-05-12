package Presenter.SharePreferences;

import android.content.Context;
import android.content.SharedPreferences;

import Model.ObjectClass.User;

/**
 * Created by huynh on 5/12/2018.
 */

public interface IPresenterMySharePreferences
{
    void xuLySharedPreferences(Context context, User user);
    void kiemTraDangNhap(String un, String pw);
    SharedPreferences getInstance(Context context);
}
