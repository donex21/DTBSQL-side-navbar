package com.example.dtbssql;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentAddBook extends Fragment {

    EditText editText_Author, editText_title;
    TextView textView_book_number;
    Spinner spinner_category, spinner_year_published;
    Button add_book_btn;
    int getyear = 0;
    String get_category = "";
    Db_operations db_operations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_book_fragment, container, false);

        editText_Author = v.findViewById(R.id.editText_Author);
        editText_title = v.findViewById(R.id.editText_title);
        textView_book_number = v.findViewById(R.id.textView_book_number);
        spinner_category = v.findViewById(R.id.spinner_category);
        spinner_year_published = v.findViewById(R.id.spinner_year_published);
        add_book_btn = v.findViewById(R.id.add_book_btn);

        db_operations = new Db_operations(getActivity().getApplicationContext(),"",null,1);

        String bookNumber = get_book_number() + "";
        textView_book_number.setText(bookNumber);


        List<Integer> yearlist = new ArrayList<>();
        for(int i=1950; i<=2021;i++){
            yearlist.add(i);
        }

        //spinner for published year
        ArrayAdapter<Integer> adapteryear = new ArrayAdapter<Integer>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item, yearlist);
        adapteryear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year_published.setAdapter(adapteryear);
        spinner_year_published.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getyear = (int)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.category));
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter_category);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                get_category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!completeFields()){
                    Toast.makeText(getActivity(),"Please input the fields completely", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        String getyearString = getyear +"";
                        db_operations.insert_book(textView_book_number.getText().toString(), editText_title.getText().toString(), editText_Author.getText().toString(), get_category, getyearString);
                        Toast.makeText(getActivity(),"The Book Successfully Added", Toast.LENGTH_SHORT).show();
                        editText_Author.setText("");
                        editText_title.setText("");
                        getyear = 0;
                        get_category = "";
                    }catch (SQLiteException e){
                        Toast.makeText(getActivity(),"The Books is Already Exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return v;
    }

    private int get_book_number(){
        Random random = new Random();
        int book_num = 0;
        String bookNum = "";
        do{
            book_num = random.nextInt(10000);
            bookNum = book_num + "";
        }while(db_operations.SearchBooks(bookNum));
        return book_num;
    }
    private boolean completeFields(){
        if(editText_Author.getText().toString().equals("") || editText_title.getText().toString().equals("") ||
        get_category.equals("") || getyear == 0){
            return false;
        }
        return true;
    }
}
