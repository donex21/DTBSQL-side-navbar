package com.example.dtbssql;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentBookSearch extends Fragment {

    Spinner spinner_search_by, spinner_search_category;
    EditText editText_search_by;
    Button book_search_btn;
    String selected_search ="";
    String get_category ="";
    Db_operations db_operations;

    ListView listView;
    ArrayAdapter arrayAdapter;
    List<String> liststud;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.book_search_fragment, container, false);
        spinner_search_by = v.findViewById(R.id.spinner_search_by);
        spinner_search_category = v.findViewById(R.id.spinner_search_category);
        editText_search_by = v.findViewById(R.id.editText_search_by);
        book_search_btn = v.findViewById(R.id.book_search_btn);
        listView = v.findViewById(R.id.listview);
        liststud =  new ArrayList<String>();

        db_operations = new Db_operations(getActivity().getApplicationContext(),"",null,1);

        String[] searchBy = {"ID", "Title", "Category", "yrPublished"};

        ArrayAdapter<String> adapter_search = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item, searchBy);
        adapter_search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_search_by.setAdapter(adapter_search);
        spinner_search_by.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_search = parent.getItemAtPosition(position).toString();
                if(selected_search.equals("ID")){
                    editText_search_by.setVisibility(View.VISIBLE);
                    editText_search_by.setHint("Book Number");
                    spinner_search_category.setVisibility(View.GONE);
                }else if(selected_search.equals("Title")){
                    editText_search_by.setVisibility(View.VISIBLE);
                    editText_search_by.setHint("Title");
                    spinner_search_category.setVisibility(View.GONE);
                }else if(selected_search.equals("Category")){
                    editText_search_by.setVisibility(View.GONE);
                    spinner_search_category.setVisibility(View.VISIBLE);
                }else if(selected_search.equals("yrPublished")){
                    editText_search_by.setVisibility(View.VISIBLE);
                    editText_search_by.setHint("Year Published");
                    spinner_search_category.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter_category = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.category));
        adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_search_category.setAdapter(adapter_category);
        spinner_search_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                get_category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        book_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liststud.clear();
                if(selected_search.equals("ID") && !editText_search_by.getText().toString().equals("")){
                    Cursor cursor = db_operations.searchByID(editText_search_by.getText().toString());
                    dataView(cursor);
                    listView.setAdapter(arrayAdapter);
                }else if(selected_search.equals("Title")  && !editText_search_by.getText().toString().equals("")){
                    Cursor cursor = db_operations.searchByTitle(editText_search_by.getText().toString());
                    dataView(cursor);
                    listView.setAdapter(arrayAdapter);
                }else if(selected_search.equals("Category")){
                    Cursor cursor = db_operations.searchByCategory(get_category);
                    dataView(cursor);
                    listView.setAdapter(arrayAdapter);
                }else if(selected_search.equals("yrPublished")  && !editText_search_by.getText().toString().equals("")){
                    Cursor cursor = db_operations.searchByYearPub(editText_search_by.getText().toString());
                    dataView(cursor);
                    listView.setAdapter(arrayAdapter);
                }else{
                    Toast.makeText(getActivity(),"Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return v;
    }

    private void dataView(Cursor cursor) {
        if(cursor.getCount() == 0){
            Toast.makeText(getActivity(), "No data",Toast.LENGTH_LONG).show();
        }else{
            liststud.add("ID \t Title \t Author\t Category \t Year");
            while(cursor.moveToNext()) {
                liststud.add(cursor.getString(1) + "\t" + cursor.getString(2) + "\t" +
                        cursor.getString(3) +  "\t" + cursor.getString(4) + "\t" +
                        cursor.getString(5));
            }
            arrayAdapter = new ArrayAdapter(getActivity(), R.layout.student_list, liststud);
        }
    }
}
