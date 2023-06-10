package com.example.bibaragi.ui.dashboard;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bibaragi.MainActivity;
import com.example.bibaragi.R;

public class DashboardFragment extends Fragment {
    TextView[] time = new TextView[100];
    int startTimeNumber , endTimeNumber , colorNumber = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container,false);

        time[11] = (TextView) root.findViewById(R.id.time1_1);
        time[12] = (TextView) root.findViewById(R.id.time1_2);
        time[13] = (TextView) root.findViewById(R.id.time1_3);
        time[14] = (TextView) root.findViewById(R.id.time1_4);
        time[15] = (TextView) root.findViewById(R.id.time1_5);
        time[21] = (TextView) root.findViewById(R.id.time2_1);
        time[22] = (TextView) root.findViewById(R.id.time2_2);
        time[23] = (TextView) root.findViewById(R.id.time2_3);
        time[24] = (TextView) root.findViewById(R.id.time2_4);
        time[25] = (TextView) root.findViewById(R.id.time2_5);
        time[31] = (TextView) root.findViewById(R.id.time3_1);
        time[32] = (TextView) root.findViewById(R.id.time3_2);
        time[33] = (TextView) root.findViewById(R.id.time3_3);
        time[34] = (TextView) root.findViewById(R.id.time3_4);
        time[35] = (TextView) root.findViewById(R.id.time3_5);
        time[41] = (TextView) root.findViewById(R.id.time4_1);
        time[42] = (TextView) root.findViewById(R.id.time4_2);
        time[43] = (TextView) root.findViewById(R.id.time4_3);
        time[44] = (TextView) root.findViewById(R.id.time4_4);
        time[45] = (TextView) root.findViewById(R.id.time4_5);
        time[51] = (TextView) root.findViewById(R.id.time5_1);
        time[52] = (TextView) root.findViewById(R.id.time5_2);
        time[53] = (TextView) root.findViewById(R.id.time5_3);
        time[54] = (TextView) root.findViewById(R.id.time5_4);
        time[55] = (TextView) root.findViewById(R.id.time5_5);
        time[61] = (TextView) root.findViewById(R.id.time6_1);
        time[62] = (TextView) root.findViewById(R.id.time6_2);
        time[63] = (TextView) root.findViewById(R.id.time6_3);
        time[64] = (TextView) root.findViewById(R.id.time6_4);
        time[65] = (TextView) root.findViewById(R.id.time6_5);
        time[71] = (TextView) root.findViewById(R.id.time7_1);
        time[72] = (TextView) root.findViewById(R.id.time7_2);
        time[73] = (TextView) root.findViewById(R.id.time7_3);
        time[74] = (TextView) root.findViewById(R.id.time7_4);
        time[75] = (TextView) root.findViewById(R.id.time7_5);
        time[81] = (TextView) root.findViewById(R.id.time8_1);
        time[82] = (TextView) root.findViewById(R.id.time8_2);
        time[83] = (TextView) root.findViewById(R.id.time8_3);
        time[84] = (TextView) root.findViewById(R.id.time8_4);
        time[85] = (TextView) root.findViewById(R.id.time8_5);
        time[91] = (TextView) root.findViewById(R.id.time9_1);
        time[92] = (TextView) root.findViewById(R.id.time9_2);
        time[93] = (TextView) root.findViewById(R.id.time9_3);
        time[94] = (TextView) root.findViewById(R.id.time9_4);
        time[95] = (TextView) root.findViewById(R.id.time9_5);

        MainActivity.sqlDB = MainActivity.myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = MainActivity.sqlDB.rawQuery("SELECT * FROM groupSubject;",null);
        while (cursor.moveToNext()) {
            if(cursor.getString(3).compareTo("월") == 0) {startTimeNumber += 1; endTimeNumber += 1;}
            if(cursor.getString(3).compareTo("화") == 0) {startTimeNumber += 2; endTimeNumber += 2;}
            if(cursor.getString(3).compareTo("수") == 0) {startTimeNumber += 3; endTimeNumber += 3;}
            if(cursor.getString(3).compareTo("목") == 0) {startTimeNumber += 4; endTimeNumber += 4;}
            if(cursor.getString(3).compareTo("금") == 0) {startTimeNumber += 5; endTimeNumber += 5;}
            startTimeNumber += (cursor.getInt(4) * 10);
            time[startTimeNumber].setText(cursor.getString(0));

            endTimeNumber += (cursor.getInt(5) * 10);
            time[endTimeNumber].setText(cursor.getString(2));

            for(int i = startTimeNumber; i <= endTimeNumber; i+=10){
                switch (colorNumber){
                    case 0:
                        time[i].setBackgroundColor(Color.RED);
                        break;
                    case 1:
                        time[i].setBackgroundColor(Color.CYAN);
                        break;
                    case 2:
                        time[i].setBackgroundColor(Color.MAGENTA);
                        break;
                    case 3:
                        time[i].setBackgroundColor(Color.YELLOW);
                        break;
                    case 4:
                        time[i].setBackgroundColor(Color.GREEN);
                        break;
                    case 5:
                        time[i].setBackgroundColor(Color.GRAY);
                        break;
                }
            }
            colorNumber = (colorNumber + 1) % 6;
            startTimeNumber = 0;
            endTimeNumber = 0;
        }
        colorNumber = 0;
        cursor.close();
        MainActivity.sqlDB.close();

        return root;
    }
}
