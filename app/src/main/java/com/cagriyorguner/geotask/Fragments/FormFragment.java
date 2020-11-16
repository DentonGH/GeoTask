package com.cagriyorguner.geotask.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cagriyorguner.geotask.Activities.MainActivity;
import com.cagriyorguner.geotask.Database.RoomDB;
import com.cagriyorguner.geotask.Models.CustomMenuItem;
import com.cagriyorguner.geotask.R;
import com.google.android.gms.maps.model.LatLng;

public class FormFragment extends Fragment {

    LatLng markerPosition;
    String locationName;
    EditText taskNameEditText;
    EditText taskDescriptionEditText;
    Button buttonCancel;
    Button buttonSave;
    String taskName;
    String taskDescription;

    public FormFragment(LatLng markerPosition, String locationName, String taskName, String taskDescription) {
        this.markerPosition = markerPosition;
        this.locationName = locationName;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        //initializations
        taskNameEditText = view.findViewById(R.id.task_name_edit_text);
        taskDescriptionEditText = view.findViewById(R.id.task_description_edit_text);
        buttonCancel = view.findViewById(R.id.cancel_button);
        buttonSave = view.findViewById(R.id.save_button);

        //when accessed here from pressing 'detayı göster' button, i define taskName and taskDescription as parameters, otherwise (accessed by pressing floatingActionButton) i leave those empty.
        if(!taskName.isEmpty()){
            buttonSave.setVisibility(View.GONE);

            taskNameEditText.setText(taskName);
            taskDescriptionEditText.setText(taskDescription);

            taskNameEditText.setEnabled(false);
            taskDescriptionEditText.setEnabled(false);

        }
        else{
            //manual focus on editText initially
            taskDescriptionEditText.requestFocus();
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null){
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        }

        //onClickListeners
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                ((MainActivity)getActivity()).resetState();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTaskName = taskNameEditText.getText().toString().trim();
                String sTaskDescription = taskDescriptionEditText.getText().toString().trim();
                if(!sTaskName.isEmpty()){
                    RoomDB database = RoomDB.getInstance(getActivity());
                    CustomMenuItem customMenuItem = new CustomMenuItem();
                    customMenuItem.setTaskName(sTaskName);
                    customMenuItem.setDescription(sTaskDescription);
                    customMenuItem.setLatitude(markerPosition.latitude);
                    customMenuItem.setLongitude(markerPosition.longitude);
                    customMenuItem.setLocationName(locationName);
                    customMenuItem.setUserId(database.mainDao().getAuthenticatedUser(true).getID());

                    //saving to database
                    database.mainDao().insertCustomMenuItem(customMenuItem);
                    taskNameEditText.getText().clear();
                    taskDescriptionEditText.getText().clear();
                    ((MainActivity) getActivity()).createNewCustomMenuItemsList();
                    getActivity().getSupportFragmentManager().popBackStack();
                    ((MainActivity)getActivity()).resetState();
                }
                else{
                    Toast.makeText(getActivity(), "İş ismi boş bırakılamaz.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}