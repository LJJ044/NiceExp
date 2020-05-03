package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.excelergo.niceexp.R;

import entity.RegisterInfo;
import entity.UserInfo;


public class AlterActivity extends AppCompatActivity {
    private UserInfo userInfo;
    private EditText editText1;
    private EditText editText2;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alter);
        userInfo=new UserInfo(getApplicationContext());
        editText1=findViewById(R.id.et_account_alter);
        editText2=findViewById(R.id.et_psw_alter);
        textView=findViewById(R.id.btn_confirm);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alter_name=editText1.getText().toString();
                String alter_pwd=editText2.getText().toString();
                RegisterInfo registerInfo=new RegisterInfo();
                registerInfo.setName(alter_name);
                registerInfo.setPwd(alter_pwd);
                userInfo.updateUser(registerInfo);//修改用户密码
                finish();
            }
        });
    }

}
