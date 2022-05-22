package com.example.remaketodolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.remaketodolist.Model.ToDoModel;
import com.example.remaketodolist.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db; //from databasehandler class

    public static AddNewTask newInstance(){ //use to return object to mainactivity
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){ //to check the instance of this fragment exist in memory
        super.onCreate(savedInstanceState); //inheriting the parameter on the method
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); //helping the dialogfragment toreadjust and move upwards when the keyboard is shown
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){ //when the the text is added as task
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        db = new DatabaseHandler(getActivity()); //passing the activity to the handler
        db.openDatabase();

        boolean isUpdate = false; //checking if updating a task or trying to create a new task
        final Bundle bundle = getArguments(); //update
        if(bundle != null){
            isUpdate = true; //if updating a task
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark)); //enable to save
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { //useless method but deleting it makes an error

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){ // s for character sequence
                    newTaskSaveButton.setEnabled(false); //if no changes is applied button will be disabled
                    newTaskSaveButton.setTextColor(Color.DKGRAY);
                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { //useless method but deleting it makes an error

            }
        });
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() { //onclicklistener for save buytton
            @Override
            public void onClick(View v) { //for checking if adding or updating a task
                String text = newTaskText.getText().toString();
                if (finalIsUpdate) { //88
                    db.updateTask(bundle.getInt("id"), text);
                }
                else{
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }


    });
    }
    @Override // recyclerview should be automatically be refreshed not manually by the user or after restarting the app
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){ //check if activity instance dialog close listener is an interface which going to define which contain the function which is going to do all the database task such as refreshing
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }

    }
}
