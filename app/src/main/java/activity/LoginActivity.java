package activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.excelergo.niceexp.R;

import java.util.Map;

import entity.RegisterInfo;
import entity.UserInfo;
import fragment.Fragment4;

public class LoginActivity extends RegisterActivity {
    private EditText etaccount,etpsw;
    private TextView btnlog,btnregister,textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        init();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, AlterActivity.class);
                startActivity(intent);
            }
        });
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etaccount.getText().toString().trim();
                String psw = etpsw.getText().toString().trim();
                RegisterInfo registerInfo=new RegisterInfo();
                registerInfo.setName(account);
                registerInfo.setPwd(psw);
                UserInfo userInfo=new UserInfo(getApplicationContext());
                Map<String,String> map=userInfo.queryUser(registerInfo);
                String name=map.get("name");
                String pwd=map.get("pwd");
                Map<String,String>map1=userInfo.queryByUserName(account);//通过用户名找密码通过
                Map<String,String>map2=userInfo.queryByPwd(psw);//通过密码找用户


// 校验号码和密码是否正确
                if (TextUtils.isEmpty(account)) {
                    Toast.makeText(getApplicationContext(), "请输入账号", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();

                }else if(!account.equals(map2.get("name2"))){
                    Toast.makeText(getApplicationContext(), "请输入正确的账号", Toast.LENGTH_SHORT).show();

                }else if(!psw.equals(map1.get("pwd1"))){
                    Toast.makeText(getApplicationContext(), "请输入正确的密码", Toast.LENGTH_SHORT).show();
                    etpsw.setText("");
                }
                    if (account.equals(name) && psw.equals(pwd)) {
                        SharedPreferences sp=getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("userName",name);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                        Fragment4.tv_user.setText("用户名："+name);
                        finish();
                    }
                }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        }

    private void init(){
        etaccount=findViewById(R.id.et_account);
        etpsw=findViewById(R.id.et_psw);
        btnlog=findViewById(R.id.btn_log);
        btnregister=findViewById(R.id.btn_register);
        textView=findViewById(R.id.alter_pwd);

    }

}
