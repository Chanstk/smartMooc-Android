package com.example.lenovo.login_signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.lenovo.smartMooc.R;

public class login extends AppCompatActivity {
    private EditText userName;
    private EditText userPassword;
    private Button login;
    private Intent intent, toSignin;
    private ProgressDialog pd ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        userName = (EditText) findViewById(R.id.userName);
        userPassword = (EditText) findViewById(R.id.userPassword);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = ProgressDialog.show(login.this, null, "登陆中...",true);
                if (!userName.getText().toString().equals("") || !userPassword.getText().toString().equals("")) {
                    AVUser.logInInBackground(userName.getText().toString(), userPassword.getText().toString(), new LogInCallback() {
                        public void done(AVUser user, AVException e) {
                            if (user != null) {
                                // 登录成功
                                intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                                pd.dismiss();
                            } else {
                                // 登录失败
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "登录失败，请检查用户名密码!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "请输入用户名或密码", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else if (resultCode == 0){
            intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                setResult(RESULT_CANCELED, intent);
                this.finish();
                break;
            case R.id.signin:
                toSignin = new Intent(this, signin.class);
                startActivityForResult(toSignin, 1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

}
