package com.example.bibaragi.ui.subjectadd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bibaragi.MainActivity;
import com.example.bibaragi.R;

public class SubjectaddFragment extends Fragment {

    EditText edittext_subjectname, editText_professorname, editText_classname;
    RadioGroup radioGroup;
    RadioButton radio_week1,radio_week2,radio_week3,radio_week4,radio_week5;
    Button btn_subjectAdd;
    String day_of_week;
    int startTime,endTime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subjectadd, container, false);

        edittext_subjectname = (EditText) root.findViewById(R.id.ed_subjectname);
        editText_professorname = (EditText) root.findViewById(R.id.ed_professorname);
        editText_classname = (EditText) root.findViewById(R.id.ed_classroom);
        radioGroup = (RadioGroup) root.findViewById(R.id.radio_group);
        radio_week1 = (RadioButton) root.findViewById(R.id.radio_week1);
        radio_week2 = (RadioButton) root.findViewById(R.id.radio_week2);
        radio_week3 = (RadioButton) root.findViewById(R.id.radio_week3);
        radio_week4 = (RadioButton) root.findViewById(R.id.radio_week4);
        radio_week5 = (RadioButton) root.findViewById(R.id.radio_week5);

        Spinner startTimeSpinner = (Spinner) root.findViewById(R.id.startspinner);
        ArrayAdapter<CharSequence> startSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.startClassTime, android.R.layout.simple_spinner_item);
        startSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTimeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startTime = position + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        startTimeSpinner.setAdapter(startSpinnerAdapter);

        Spinner endTimeSpinner = (Spinner) root.findViewById(R.id.endspinner);
        ArrayAdapter<CharSequence> endSpinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.endClassTime, android.R.layout.simple_spinner_item);
        endSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endTimeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endTime = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        endTimeSpinner.setAdapter(endSpinnerAdapter);

        btn_subjectAdd = (Button) root.findViewById(R.id.btn_subjectadd);

        btn_subjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radio_week1.isChecked()) day_of_week = "월";
                if(radio_week2.isChecked()) day_of_week = "화";
                if(radio_week3.isChecked()) day_of_week = "수";
                if(radio_week4.isChecked()) day_of_week = "목";
                if(radio_week5.isChecked()) day_of_week = "금";
                MainActivity.sqlDB = MainActivity.myHelper.getWritableDatabase();
                MainActivity.sqlDB.execSQL("INSERT INTO groupSubject VALUES ( '"
                        + edittext_subjectname.getText().toString() + "' , '"
                        + editText_professorname.getText().toString() + "' , '"
                        + editText_classname.getText().toString()+"' , '"
                        + day_of_week+"' , '" + startTime + "' , '" + endTime + "');");
                MainActivity.sqlDB.close();
                Navigation.findNavController(v).navigate(R.id.action_SubjectAddFragment_to_HomeFragment);
            }
        });

        return root;
    }
}
