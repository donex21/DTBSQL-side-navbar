package com.example.dtbssql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editText_username_email, editText_password;
    Button login_btn, sign_up_btn;
    Db_operations db_operations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_username_email = findViewById(R.id.editText_username_email);
        editText_password = findViewById(R.id.editText_password);
        login_btn = findViewById(R.id.login_btn);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        db_operations = new Db_operations(this,"",null,1);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!complete_details()){
                    Toast.makeText(MainActivity.this,"Please complete the details needed", Toast.LENGTH_SHORT).show();
                }else if(db_operations.SearchEmail(editText_username_email.getText().toString(), editText_password.getText().toString())){
                    List<String> username = new ArrayList<>();
                    Cursor cursor = db_operations.viewAUserThruEmail(editText_username_email.getText().toString());
                    if(cursor.getCount() == 0){
                        Toast.makeText(MainActivity.this, "No data",Toast.LENGTH_LONG).show();
                    }else{
                        while(cursor.moveToNext()) {
                            username.add(cursor.getString(0));
                        }
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("username", username.get(0));
                        startActivity(intent);
                        finish();
                    }
                }else if(db_operations.SearchUsername(editText_username_email.getText().toString(), editText_password.getText().toString())){
                    List<String> username = new ArrayList<>();
                    Cursor cursor = db_operations.viewAUserThruUsername(editText_username_email.getText().toString());
                    if(cursor.getCount() == 0){
                        Toast.makeText(MainActivity.this, "No data",Toast.LENGTH_LONG).show();
                    }else{
                        while(cursor.moveToNext()) {
                            username.add(cursor.getString(0));
                        }
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("username", username.get(0));
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Invalid Inputted Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass){
            if(editText_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                //Hide Password
                editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    public void go_sign_up_page(View view) {
        Intent intent =  new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean complete_details(){
        if(editText_username_email.getText().toString().equals("") || editText_password.getText().toString().equals("")){
            return false;
        }
        return true;
    }
}