package ir.ideacenter.speedmatch;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GetNameDialog extends Dialog {

    Context context;
    EditText getName;
    Button confirmName;
    OnNameSelectedListener listener;

    public GetNameDialog(@NonNull Context context, OnNameSelectedListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_get_name);
        getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        getName = (EditText) findViewById(R.id.get_name_text);
        confirmName = (Button) findViewById(R.id.get_name_confirm);

        confirmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNameSelected(getName.getText().toString());
                dismiss();
            }
        });
    }
}
