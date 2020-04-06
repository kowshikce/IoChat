package com.example.iochat.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.iochat.R;

public class ConnectionDialog extends DialogFragment {

    private NoticeDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.connect_title)
                .setMessage(R.string.connect_message)
                .setPositiveButton(R.string.connect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogPositiveButtonCLick(which);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeButtonClick(which);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener = (NoticeDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "must implement NoticeDialogListener");
        }
    }

    public interface NoticeDialogListener{
        public void onDialogNegativeButtonClick(int which);
        public void onDialogPositiveButtonCLick(int which);
    }
}
