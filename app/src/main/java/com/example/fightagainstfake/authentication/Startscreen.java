package com.example.fightagainstfake.authentication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fightagainstfake.MainActivity;
import com.example.fightagainstfake.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Startscreen extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(Startscreen.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);
        getSupportActionBar().hide();
        changeColor(R.color.startscreen);

        findViewById(R.id.asUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Startscreen.this,loginScreen.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.asAdmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

    }

    private void show() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Startscreen.this);
        alertDialog.setTitle("PASSCODE");
        alertDialog.setMessage("Enter Admin Passcode");

        final EditText input = new EditText(Startscreen.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.leftMargin=5;
        lp.rightMargin=5;
        lp.gravity=0;
        input.setLayoutParams(lp);
        input.setHint("Enter Your Passcode Here....");
        input.setHintTextColor(getResources().getColor(R.color.purple_500));
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.ic_baseline_password_24);

        alertDialog.setPositiveButton("ACCESS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        if (password.equals("1413914"))
                        {
                            Toast.makeText(Startscreen.this, "Welcome back!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Startscreen.this,loginScreen.class));
                        }
                        else
                        {
                            Toast.makeText(Startscreen.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialog.setNegativeButton("BACK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }




    public void changeColor(int resource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), resource));
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(resource)));

    }
}