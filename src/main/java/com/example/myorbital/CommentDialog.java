package com.example.myorbital;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class CommentDialog extends AppCompatDialogFragment {
    private EditText commentEditText;
    private CommentDialogListener listener;
    private Intent homeIntent;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        homeIntent = new Intent(getContext(), MainActivity.class);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.comment_dialog, null);
        commentEditText = view.findViewById(R.id.commentEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("Do you want to share your trip?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = commentEditText.getText().toString();
                        listener.saveTexts(comment);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(homeIntent);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CommentDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement CommentDialogListener");
        }
    }

    public interface CommentDialogListener {
        void saveTexts(String comments);
    }
}
