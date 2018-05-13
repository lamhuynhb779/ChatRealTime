package Model.ObjectClass;

import java.util.ArrayList;

/**
 * Created by huynh on 12/23/2017.
 */

public class User
{
    private String id;
    private String username;
    private String password;
    private boolean isChecked;

    public User() {
    }

    public User(String username, String password, String id, boolean isChecked) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.isChecked = isChecked;
    }

    public boolean getisChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
