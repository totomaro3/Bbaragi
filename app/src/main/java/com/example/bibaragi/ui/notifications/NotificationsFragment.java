package com.example.bibaragi.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.bibaragi.MainActivity;
import com.example.bibaragi.R;
import com.example.bibaragi.ui.home.HomeFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NotificationsFragment extends Fragment {

    View dialogView;
    ArrayList<String> workList;
    ArrayAdapter<String> adapter;
    ArrayList<String> allView;
    ArrayAdapter<String> searchSpinnerAdapter;
    String[] work_id = new String[100];
    String[] subject_name = new String[100];
    int work_number;
    int check;
    int subject_number;
    public static int lastWorkNumber;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        final ListView lv_workList = (ListView) root.findViewById(R.id.lv_workList);
        workList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice,workList);
        lv_workList.setChoiceMode(lv_workList.CHOICE_MODE_MULTIPLE);
        lv_workList.setAdapter(adapter);

        lv_workList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.sqlDB = MainActivity.myHelper.getReadableDatabase();
                Cursor cursor;
                cursor = MainActivity.sqlDB.rawQuery("SELECT * FROM groupReport WHERE gWorkNumber = '"+work_id[position]+"';", null);
                while (cursor.moveToNext()) check = cursor.getInt(4);
                check = (check + 1) % 2;
                MainActivity.sqlDB = MainActivity.myHelper.getWritableDatabase();
                MainActivity.sqlDB.execSQL("UPDATE groupReport SET gCheck = '"+Integer.toString(check)+"' WHERE gWorkNumber = '"+work_id[position]+"';");
                adapter.notifyDataSetChanged();
                cursor.close();
                MainActivity.sqlDB.close();
            }
        });

        lv_workList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("확인");
                dlg.setIcon(R.mipmap.ic_launcher);
                dialogView = View.inflate(getActivity(), R.layout.repeat_commend, null);
                dlg.setView(dialogView);

                dlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workList.remove(position);
                        adapter.notifyDataSetChanged();
                        MainActivity.sqlDB = MainActivity.myHelper.getWritableDatabase();
                        MainActivity.sqlDB.execSQL("DELETE FROM groupReport WHERE gWorkNumber = '"+work_id[position]+"';");
                        MainActivity.sqlDB.close();
                    }
                });
                dlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
                return false;
            }
        });

        Spinner searchSpinner = root.findViewById(R.id.searchSpinner);
        allView = new ArrayList<String>();
        allView.add("전체 보기");
        searchSpinnerAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, allView);
        searchSpinnerAdapter.addAll(HomeFragment.subjectList);
        searchSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position -= 1;
                if(position >= 0) {
                    int checkPosition = 0;
                    MainActivity.sqlDB = MainActivity.myHelper.getReadableDatabase();
                    Cursor cursor = MainActivity.sqlDB.rawQuery("SELECT * FROM groupReport WHERE gSubject = '"+HomeFragment.subject_name[position]+"';", null);
                    workList.clear();
                    while (cursor.moveToNext()) {
                        work_id[work_number] = cursor.getString(0);
                        workList.add("#"+ cursor.getString(1) + "\n" + cursor.getString(2) + " / " + cursor.getString(3));
                        if (cursor.getInt(4) == 1) lv_workList.setItemChecked(checkPosition, true);
                        else lv_workList.setItemChecked(checkPosition, false);
                        work_number++;
                        checkPosition++;
                    }
                    work_number = 0;
                    cursor.close();
                    adapter.notifyDataSetChanged();
                    MainActivity.sqlDB.close();
                }
                else {
                    int checkPosition = 0;
                    MainActivity.sqlDB = MainActivity.myHelper.getReadableDatabase();
                    Cursor cursor = MainActivity.sqlDB.rawQuery("SELECT * FROM groupReport;", null);
                    workList.clear();
                    while (cursor.moveToNext()) {
                        work_id[work_number] = cursor.getString(0);
                        lastWorkNumber = Integer.parseInt(work_id[work_number++]);
                        workList.add("#"+cursor.getString(1)+ "\n"+cursor.getString(2)+" / "+cursor.getString(3));
                        if(cursor.getInt(4) == 1) lv_workList.setItemChecked(checkPosition,true);
                        else lv_workList.setItemChecked(checkPosition, false);
                        checkPosition++;
                    }
                    work_number = 0;
                    adapter.notifyDataSetChanged();
                    cursor.close();
                    MainActivity.sqlDB.close();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        searchSpinner.setAdapter(searchSpinnerAdapter);

        TextView dateNow = (TextView) root.findViewById(R.id.tv_dateNow);
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String strDate = df.format(cal.getTime());
        dateNow.setText("현재 날짜 : "+strDate.substring(0,4)+"년 "+strDate.substring(4,6)+"월 "+strDate.substring(6,8)+"일");

        return root;
    }
}