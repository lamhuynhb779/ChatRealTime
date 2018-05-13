package Model.ObjectClass;

/**
 * Created by huynh on 5/12/2018.
 */

public class InfoChat
{
    private String id_user;
    private String username;
    private String noidungchat;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNoidungchat() {
        return noidungchat;
    }

    public void setNoidungchat(String noidungchat) {
        this.noidungchat = noidungchat;
    }

    @Override
    public String toString() {
        return id_user +" - "+noidungchat;
    }
}
