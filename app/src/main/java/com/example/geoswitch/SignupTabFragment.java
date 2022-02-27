package com.example.geoswitch;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import maes.tech.intentanim.CustomIntent;

public class SignupTabFragment extends Fragment {

    Button signup;
    EditText uname, pswd, confirmPswd, email;
    DBHelper mydb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        signup = (Button) root.findViewById(R.id.signup);
        uname = (EditText) root.findViewById(R.id.uname);
        pswd = (EditText) root.findViewById(R.id.pswd);
        confirmPswd = (EditText) root.findViewById(R.id.confirm_pswd);
        email = (EditText) root.findViewById(R.id.email);

        mydb = new DBHelper(getContext());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = uname.getText().toString();
                String password = pswd.getText().toString();
                String confirmPassword = confirmPswd.getText().toString();
                String emailadd = email.getText().toString();

                if (username.equals("") || password.equals("") || confirmPassword.equals("") || emailadd.equals("")) {
                    Toast.makeText(getContext(), "No fields should be empty", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getContext(), "Password not matching", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean ifExists = mydb.checkUserExists(emailadd);

                    if (ifExists) {

                        Toast.makeText(getContext(), "This email is already registered. Try logging in.", Toast.LENGTH_SHORT).show();
                        goToLoginPage();
                    } else {

                        Boolean res = mydb.insertData(username, password, emailadd);
                        if (res == true) {
                            Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                            goToLoginPage();
                        } else {
                            Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });


        return root;

    }


    public void goToLoginPage() {
        Intent i = new Intent(getContext(), UserLoginSignup.class);
        i.putExtra("tab", "0");
        startActivity(i);

        CustomIntent.customType(getContext(), "right-to-left");
    }
}


