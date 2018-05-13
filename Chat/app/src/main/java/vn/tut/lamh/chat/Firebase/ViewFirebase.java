package vn.tut.lamh.chat.Firebase;

import java.util.ArrayList;

import Model.ObjectClass.InfoChat;

/**
 * Created by huynh on 5/11/2018.
 */

public interface ViewFirebase
{
    void dangKyThongCong();
    void guiNoiDungChat2NguoiThanhCong();
    void hienThiDanhSachInfoChat(ArrayList<InfoChat> infoChats);
    void hienThiDanhSachThatBai();
}
