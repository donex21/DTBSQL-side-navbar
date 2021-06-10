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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentEdit extends Fragment {

    EditText editText_SearchID, editText_title, editText_Author;
    Button search_edit_btn, edit_book_btn;
    LinearLayout linear_layout_edit;
    TextView textView_book_number;
    Spinner spinner_category, spinner_year_published;
    int getyear = 0;
    String get_category = "";
    Db_operations db_operations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_fragment, container, false);

        editText_SearchID = v.findViewById(R.id.editText_SearchID);
        editText_title = v.findViewById(R.id.editText_title);
        editText_Author = v.findViewById(R.id.editText_Author);
        search_edit_btn = v.findViewById(R.id.search_edit_btn);
        edit_book_btn = v.findViewById(R.id.edit_book_btn);
        linear_layout_edit = v.findViewById(R.id.linear_layout_edit);
        textView_book_number = v.findViewById(R.id.textView_book_number);
        spinner_category = v.findViewById(R.id.spinner_category);
        spinner_year_published = v.findViewById(R.id.spinner_year_published);

        db_operations = new Db_operations(getActivity().getApplicationContext(),"",null,1);

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

        search_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_SearchID.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please Input Book Number", Toast.LENGTH_SHORT).show();
                }else{
                    Cursor cursor = db_operations.searchByID(editText_SearchID.getText().toString());
                    if(cursor.getCount() == 0){
                        Toast.makeText(getActivity(),"The Book number does not Exist", Toast.LENGTH_SHORT).show();
                    }else {
                        dataView(cursor);
                        linear_layout_edit.setVisibility(View.VISIBLE);
                        search_edit_btn.setVisibility(View.GONE);
                        editText_SearchID.setVisibility(View.GONE);
                    }
                }
            }
        });

        edit_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_title.getText().toString().equals("") || editText_Author.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"Please Complete Fields", Toast.LENGTH_SHORT).show();
                }else{
                    String getyearString = getyear +"";
                    db_operations.update_book(textView_book_number.getText().toString(), editText_title.getText().toString(), editText_Author.getText().toString(), get_category, getyearString);
                    Toast.makeText(getActivity()," Book Successfully Updated", Toast.LENGTH_SHORT).show();
                    linear_layout_edit.setVisibility(View.GONE);
                    search_edit_btn.setVisibility(View.VISIBLE);
                    editText_SearchID.setVisibility(View.VISIBLE);
                    editText_SearchID.setText("");
                }
            }
        });
        return v;
    }

    private void dataView(Cursor cursor) {
        cursor.moveToFirst();
        textView_book_number.setText(cursor.getString(cursor.getColumnIndex("BOOKID")));
        editText_title.setText(cursor.getString(cursor.getColumnIndex("BOOKTITLE")));
        editText_Author.setText(cursor.getString(cursor.getColumnIndex("BOOKAUTHOR")));
        get_category = cursor.getString(cursor.getColumnIndex("BOOKCATEGORY"));
        getyear = Integer.parseInt(cursor.getString(cursor.getColumnIndex("BOOKYEARPUB")));
    }
}
