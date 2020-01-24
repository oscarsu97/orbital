package com.example.myorbital;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PromptDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Intent homeIntent = new Intent(getContext(), MainActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("End Trip")
                .setMessage("Do you want to share your trip with us?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openCommentDialog();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(homeIntent);
                    }
                })
        ;
        return builder.create();
    }

    private void openCommentDialog() {
        CommentDialog commentDialog = new CommentDialog();
        commentDialog.show(getFragmentManager(), "comment Dialog");
    }
}
