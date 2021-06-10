package com.example.dtbssql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText editText_email_add, editText_username, editText_password, editText_repeat_password;
    Button sign_up_btn;
    Db_operations db_operations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editText_username = findViewById(R.id.editText_username);
        editText_email_add = findViewById(R.id.editText_email_add);
        editText_password = findViewById(R.id.editText_password);
        editText_repeat_password = findViewById(R.id.editText_repeat_password);
        sign_up_btn = findViewById(R.id.sign_up_btn);

        db_operations = new Db_operations(this,"",null,1);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!complete_fields()){
                    Toast.makeText(SignUpActivity.this,"Please input the fields correctly", Toast.LENGTH_SHORT).show();
                }else if(confirm_password()){
                    try {
                        db_operations.insert_user(editText_email_add.getText().toString(), editText_username.getText().toString(), editText_password.getText().toString());
                        Toast.makeText(SignUpActivity.this,"Successfully Added", Toast.LENGTH_SHORT).show();
                        editText_email_add.setText("");
                        editText_username.setText("");
                        editText_password.setText("");
                        editText_repeat_password.setText("");
                    }catch (SQLiteException e){
                        Toast.makeText(SignUpActivity.this,"The Email/Username is Already Exist", Toast.LENGTH_SHORT).show();
                    }

                }else if(!confirm_password()){
                    Toast.makeText(SignUpActivity.this,"Password and Confirm Password did not match", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignUpActivity.this,"Invalid / Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void go_login_page(View view) {
        Intent intent =  new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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

    public void ShowHideRePass(View view) {
        if(view.getId()==R.id.show_repass){
            if(editText_repeat_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                editText_repeat_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                //Hide Password
                editText_repeat_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private boolean validateEmail(String data){
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(data);
        if(!emailMatcher.matches())
            editText_email_add.setError("Invalid Email");
        return emailMatcher.matches();
    }

    private boolean complete_fields(){
        if(editText_email_add.getText().toString().equals("") || editText_username.getText().toString().equals("") ||
                editText_password.getText().toString().equals("") || editText_repeat_password.getText().toString().equals("") ||
                !validateEmail(editText_email_add.getText().toString())){
            return false;
        }
        return true;
    }

    private boolean confirm_password(){
        if(editText_password.getText().toString().equals(editText_repeat_password.getText().toString()))
            return true;
        return false;
    }
}