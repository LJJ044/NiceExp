package com.example.excelergo.niceexp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etaccount,etpsw;
    private TextView btnregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        init();
        }
        public void onClick(View view) {

                String account = etaccount.getText().toString();
                String psw = etpsw.getText().toString();
                if(TextUtils.isEmpty(account)) {
                    Toast.makeText(this, "请输入账号",
                            Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(psw)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
// 保存用户信息
            UserInfo userInfo=new UserInfo(getApplicationContext());
            RegisterInfo registerInfo=new RegisterInfo();
                registerInfo.setName(account);
                registerInfo.setPwd(psw);
                Long success=userInfo.insertUser(registerInfo);
               if(success>0) {
                   Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
               }
            finish();
        }

    private void init(){
        etaccount=findViewById(R.id.et_account);
        etpsw=findViewById(R.id.et_psw);
        btnregister=findViewById(R.id.btn_register);
        btnregister.setOnClickListener(this);
    }
}

