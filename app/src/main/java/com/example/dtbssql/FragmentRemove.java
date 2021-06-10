package com.example.dtbssql;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentRemove extends Fragment {

    Spinner spinner_delete_by, spinner_by_bookID, spinner_by_book_title;
    Button book_remove_btn;
    Db_operations db_operations;
    String selected_delte_by ="";
    String selected_title = "";
    String selected_id = "";
    List<String> ids, titles;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.remove_fragment, container, false);

        spinner_delete_by= v.findViewById(R.id.spinner_delete_by);
        spinner_by_bookID= v.findViewById(R.id.spinner_by_bookID);
        spinner_by_book_title= v.findViewById(R.id.spinner_by_book_title);
        book_remove_btn= v.findViewById(R.id.book_remove_btn);
        db_operations = new Db_operations(getActivity().getApplicationContext(),"",null,1);

        ids = new ArrayList<String>();
        titles = new ArrayList<String>();
        getIDandTITLE();

        ArrayAdapter<String> adapter_selected_id = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item, ids);
        adapter_selected_id.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_by_bookID.setAdapter(adapter_selected_id);
        spinner_by_bookID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_id = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter_selected_title = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item, titles);
        adapter_selected_title.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_by_book_title.setAdapter(adapter_selected_title);
        spinner_by_book_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_title = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] deleteBy = {"ID", "Title"};
        ArrayAdapter<String> adapter_search = new ArrayAdapter<String>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item, deleteBy);
        adapter_search.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_delete_by.setAdapter(adapter_search);
        spinner_delete_by.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_delte_by = parent.getItemAtPosition(position).toString();
                if(selected_delte_by.equals("ID")){
                    spinner_by_bookID.setVisibility(View.VISIBLE);
                    spinner_by_book_title.setVisibility(View.GONE);
                }else if(selected_delte_by.equals("Title")){
                    spinner_by_bookID.setVisibility(View.GONE);
                    spinner_by_book_title.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        book_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_delte_by.equals("ID")){
                    db_operations.delete_by_book_number(selected_id);
                    getIDandTITLE();
                    Toast.makeText(getActivity(),"Book Number Successfully Deleted", Toast.LENGTH_SHORT).show();
                }else if(selected_delte_by.equals("Title")){
                    db_operations.delete_by_book_title(selected_title);
                    getIDandTITLE();
                    Toast.makeText(getActivity(),"Book Title Successfully Deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void getIDandTITLE(){
        Cursor cursor = db_operations.getAllData();
        ids.clear();
        titles.clear();
        while (cursor.moveToNext()){
            ids.add(cursor.getString(1));
            titles.add(cursor.getString(2));
        }
    }
}
