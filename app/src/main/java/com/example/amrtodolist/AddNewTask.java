package com.example.amrtodolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.amrtodolist.Model.ToDoModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText NewTaskEditText;
    private Button newTaskBtn;
    private DataWorker db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.new_task,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewTaskEditText= requireView().findViewById(R.id.editTextNewTask);
        newTaskBtn=getView().findViewById(R.id.btnNewTask);

        boolean isUpdate= false;

        final Bundle bundle= getArguments();
        if(bundle != null){
            isUpdate=true;
            String task= bundle.getString("task");
            NewTaskEditText.setText(task);
            assert task!=null;
            if(task.length()>0)
                newTaskBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        db = new DataWorker(getActivity());
        db.openDatabase();

        NewTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskBtn.setEnabled(false);
                    newTaskBtn.setTextColor(Color.GRAY);
                }
                else{
                    newTaskBtn.setEnabled(true);
                    newTaskBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;
        newTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = NewTaskEditText.getText().toString();
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                }
                else {
                    ToDoModel task= new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });


    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity= getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
