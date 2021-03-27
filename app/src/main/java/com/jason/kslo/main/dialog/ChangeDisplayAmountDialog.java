package com.jason.kslo.main.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.jason.kslo.R;

public class ChangeDisplayAmountDialog extends AppCompatDialogFragment {
    EditText editTextChangeAmount;
    private ChangeDisplayAmountListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final LayoutInflater inflater = getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_change_display_amount, null);

            builder.setView(view)
                    .setTitle(getString(R.string.ChangeAmountDisplayed))
                    .setNegativeButton(getString(R.string.Cancel), (dialogInterface, i) -> {

                    })
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        String number = editTextChangeAmount.getText().toString();
                        listener.applyTexts(number);
                    })
                    .setMessage(
                            "\n" +
                            getString(R.string.Min) + " 0\n" +
                            getString(R.string.Recommended) + " 20\n" +
                            getString(R.string.Max) + " 1500");
            editTextChangeAmount = view.findViewById(R.id.InputAmountDisplayed);
    return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ChangeDisplayAmountListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "Must implement ChangeDisplayAmountDialog");
        }
    }

    public interface ChangeDisplayAmountListener{
        void applyTexts(String number);
    }
}
