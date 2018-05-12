package Model.ObjectClass;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by huynh on 12/11/2017.
 */

public class RowChat implements Serializable
{
    private String usernameChat = "";
    private Drawable hinhChat = null;
    private String noiDungChat= "";
    private boolean laTui = false;

    public RowChat() {
    }

    public RowChat(String usernameChat, Drawable hinhChat, String noiDungChat, boolean laTui) {
        this.usernameChat = usernameChat;
        this.hinhChat = hinhChat;
        this.noiDungChat = noiDungChat;
        this.laTui = laTui;
    }

    public String getUsernameChat() {
        return usernameChat;
    }

    public void setUsernameChat(String usernameChat) {
        this.usernameChat = usernameChat;
    }

    public Drawable getHinhChat() {
        return hinhChat;
    }

    public void setHinhChat(Drawable hinhChat) {
        this.hinhChat = hinhChat;
    }

    public String getNoiDungChat() {
        return noiDungChat;
    }

    public void setNoiDungChat(String noiDungChat) {
        this.noiDungChat = noiDungChat;
    }

    public boolean isLaTui() {
        return laTui;
    }

    public void setLaTui(boolean laTui) {
        this.laTui = laTui;
    }
}
