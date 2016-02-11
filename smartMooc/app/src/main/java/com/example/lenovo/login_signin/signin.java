package com.example.lenovo.login_signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.lenovo.smartMooc.R;

public class signin extends AppCompatActivity {
    private EditText userName;
    private EditText userPassword;
    private EditText userPassword_confirm;
    private EditText nicheng;
    private Button signin;
    private Intent intent;

    private ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent();
        setContentView(R.layout.content_signin);
        init();
    }

    public void init() {
        nicheng  = (EditText) findViewById(R.id.nicheng);
        userName = (EditText) findViewById(R.id.userName);
        userPassword = (EditText) findViewById(R.id.userPassword);
        userPassword_confirm = (EditText) findViewById(R.id.userPassword_confirm);
        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = ProgressDialog.show(signin.this,null,"注册中...",true);
                if(!userPassword.getText().toString().equals(userPassword_confirm.getText().toString())){
                    pd .dismiss();
                    Toast.makeText(getApplicationContext(), "两次输入密码不一致!", Toast.LENGTH_SHORT).show();
                }else {
                    AVUser user = new AVUser();
                    user.setUsername(userName.getText().toString());
                    user.setPassword(userPassword.getText().toString());
                    user.put("nicheng", nicheng.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(AVException e) {
                            if (e == null) {
                                // successfully
                                intent = new Intent();
                                setResult(RESULT_OK, intent);
                                pd.dismiss();
                                finish();
                            } else {
                                // failed
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "注册失败!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
