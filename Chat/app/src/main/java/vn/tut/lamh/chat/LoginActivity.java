package vn.tut.lamh.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import Model.ObjectClass.User;
import Presenter.Firebase.LPresenterFirebase;
import Presenter.SharePreferences.LPresenterMySharePreferences;
import vn.tut.lamh.chat.Firebase.ViewFirebase;
import vn.tut.lamh.chat.SharePreferences.ViewSharePreferences;

public class LoginActivity extends AppCompatActivity implements ViewFirebase, ViewSharePreferences {

    EditText edtUN, edtPW;
    CheckBox chkRMB;
    Button btnRegister, btnLogin, txtvRegister, txtvLogin;
    ProgressDialog progressDialog;
    public static String linkConnectServer = "http://192.168.1.3:3000";//"https://kimhungpham779.github.io/";
    LPresenterMySharePreferences lPresenterMySharePreferences;
    LPresenterFirebase lPresenterFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();
    }

    private void addControls() {
        edtPW = (EditText) findViewById(R.id.edtPW);
        edtUN = (EditText) findViewById(R.id.edtUN);
        chkRMB = (CheckBox) findViewById(R.id.chkRMB);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtvRegister = (Button) findViewById(R.id.txtvRegister);
        txtvLogin = (Button) findViewById(R.id.txtvLogin);
        progressDialog = new ProgressDialog(LoginActivity.this);
        lPresenterMySharePreferences = new LPresenterMySharePreferences(this, this);
        lPresenterFirebase = new LPresenterFirebase(this);
    }

    private void addEvents() {
        txtvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setVisibility(View.GONE);
                btnRegister.setVisibility(View.VISIBLE);
                txtvRegister.setVisibility(View.GONE);
                txtvLogin.setVisibility(View.VISIBLE);
            }
        });

        txtvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setVisibility(View.VISIBLE);
                btnRegister.setVisibility(View.GONE);
                txtvLogin.setVisibility(View.GONE);
                txtvRegister.setVisibility(View.VISIBLE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uniqueID = UUID.randomUUID().toString();
                String UN = edtUN.getText().toString();
                String PW = edtPW.getText().toString();
                boolean isChecked = chkRMB.isChecked();
                User user = new User(UN, PW, uniqueID, isChecked);
                lPresenterMySharePreferences.xuLySharedPreferences(LoginActivity.this, user);
                lPresenterFirebase.themUserVaoFirebase(user);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UN = edtUN.getText().toString();
                String PW = edtPW.getText().toString();
                lPresenterMySharePreferences.kiemTraDangNhap(UN, PW);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            SharedPreferences sharedPreferences = lPresenterMySharePreferences.getInstance(LoginActivity.this);
            boolean checked = sharedPreferences.getBoolean("SAVE",false);
            if(checked) {
                edtUN.setText(sharedPreferences.getString("USERNAME",""));
                edtPW.setText(sharedPreferences.getString("PASSWORD",""));
            }
            chkRMB.setChecked(checked);
        }catch (Exception e){}
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void dangKyThongCong() {
        Toast.makeText(LoginActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void guiNoiDungChat2NguoiThanhCong() {

    }

    @Override
    public void dangNhapThanhCong() {
        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

        CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long l) {
                progressDialog.setTitle("Đang đồng bộ...");
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
                Intent iChat = new Intent(LoginActivity.this, FriendListActivity.class);
                startActivity(iChat);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void dangNhapThatBai() {
        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
    }
}