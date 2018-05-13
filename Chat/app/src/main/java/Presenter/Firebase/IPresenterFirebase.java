package Presenter.Firebase;

import java.util.ArrayList;

import Model.ObjectClass.InfoChat;
import Model.ObjectClass.User;

/**
 * Created by huynh on 5/11/2018.
 */

public interface IPresenterFirebase
{
    void themUserVaoFirebase(User user);
    void taoNodeChatGiua2Nguoi(String roomID, InfoChat infoChat);
    void xuLyDuLieuGroupChatTrenFirebase();
    void kiemTraDangNhap(User user);
}
