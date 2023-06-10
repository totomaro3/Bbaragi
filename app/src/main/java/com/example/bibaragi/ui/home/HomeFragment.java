package com.example.bibaragi.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.bibaragi.MainActivity;
import com.example.bibaragi.R;
import com.example.bibaragi.ui.notifications.NotificationsFragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public static ArrayList<String> subjectList;
    ArrayAdapter<String> adapter;

    View dialogView;
    public static String[] subject_name = new String[100];
    int subject_number = 0;
    int workNumber = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ListView lv_subject = (ListView) root.findViewById(R.id.lv_subject);
        subjectList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, subjectList);
        lv_subject.setAdapter(adapter);

        lv_subject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());

                dlg.setTitle("과제 추가");
                dlg.setIcon(R.mipmap.ic_launcher);

                dialogView = View.inflate(getActivity(), R.layout.dialogview, null);
                dlg.setView(dialogView);

                TextView tv_subjectTitle = (TextView) dialogView.findViewById(R.id.tv_subjectTitle);
                final EditText ed_contentReport = (EditText) dialogView.findViewById(R.id.ed_contentReport);
                final EditText ed_endDayReport = (EditText) dialogView.findViewById(R.id.ed_endDayReport);

                tv_subjectTitle.setText(subject_name[position]);

                dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workNumber = NotificationsFragment.lastWorkNumber++;
                        workNumber += 1;
                        MainActivity.sqlDB = MainActivity.myHelper.getWritableDatabase();
                        MainActivity.sqlDB.execSQL("INSERT INTO groupReport Values('"+Integer.toString(workNumber)+"','"+subject_name[position]+"' , '"
                                + ed_contentReport.getText().toString() + "' , '" + ed_endDayReport.getText().toString() +"','0');");
                        MainActivity.sqlDB.close();
                        Toast.makeText(getActivity(), "과제가 기록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
                    }
                });

                dlg.show();
            }
        });

        lv_subject.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                        subjectList.remove(position);
                        adapter.notifyDataSetChanged();
                        MainActivity.sqlDB = MainActivity.myHelper.getWritableDatabase();
                        MainActivity.sqlDB.execSQL("DELETE FROM groupSubject WHERE gSubject = '"+subject_name[position]+"';");
                        MainActivity.sqlDB.execSQL("DELETE FROM groupReport WHERE gSubject = '"+subject_name[position]+"';");
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
                return true;
            }
        });

        Button btn_subjectAdd = (Button) root.findViewById(R.id.btn_subjectadd);

        btn_subjectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_SubjectAddFragment);
            }
        });

        Button btn_init = (Button) root.findViewById(R.id.btn_init);

        btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("확인");
                dlg.setIcon(R.mipmap.ic_launcher);
                dialogView = View.inflate(getActivity(), R.layout.repeat_commend, null);
                dlg.setView(dialogView);

                dlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.sqlDB = MainActivity.myHelper.getWritableDatabase();
                        MainActivity.myHelper.onUpgrade(MainActivity.sqlDB,1,2);
                        MainActivity.sqlDB.close();
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
            }
        });

        MainActivity.sqlDB = MainActivity.myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = MainActivity.sqlDB.rawQuery("SELECT * FROM groupSubject;", null);
        while (cursor.moveToNext()) {
            subject_name[subject_number++] = cursor.getString(0);
            subjectList.add(cursor.getString(0)+"/"+cursor.getString(1)+" 교수님\n"+cursor.getString(2)
                    +"/"+cursor.getString(3)+cursor.getString(4)+"~"+cursor.getString(5));
        }
        subject_number = 0;
        adapter.notifyDataSetChanged();
        cursor.close();
        MainActivity.sqlDB.close();

        return root;
    }

}
