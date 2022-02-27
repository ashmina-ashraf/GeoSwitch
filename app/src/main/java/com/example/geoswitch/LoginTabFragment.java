package com.example.geoswitch;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    Button loginBtn;
    EditText emailadd, pswd;
    TextView isUser, isBO;

    int who = Global.ADMIN;

    DBHelper mydb;
    DBHelperBusinessRegister db;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = Global.getGlobalContext();
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        loginBtn = (Button) root.findViewById(R.id.login);
        emailadd = (EditText) root.findViewById(R.id.uname);
        pswd = (EditText) root.findViewById(R.id.pswd);
        isUser = (TextView) root.findViewById(R.id.login_user);
        isBO = (TextView) root.findViewById(R.id.login_bo);

        mydb = new DBHelper(getContext());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailadd.getText().toString();
                String password = pswd.getText().toString();

                login(email, password);
            }
        });

        isUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    isUser.setTextColor(context.getColor(R.color.teal_200));
                    isBO.setTextColor(context.getColor(R.color.dark_grey));
                    who = Global.USER;
                }
            }
        });

        isBO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    isBO.setTextColor(context.getColor(R.color.teal_200));
                    isUser.setTextColor(context.getColor(R.color.dark_grey));
                    who = Global.BUSINESS_OWNER;
                }
            }
        });

        return root;
    }

    private void login(String email, String password) {
        String message;
        int isSuccess = -1;

        if (who == Global.USER) {
            isSuccess = mydb.checkEmailPassword(email, password);
        } else if (who == Global.BUSINESS_OWNER) {
            DBHelperBusinessRegister db = new DBHelperBusinessRegister(Global.getGlobalContext());
            isSuccess = db.checkEmailPassword(email, password);
        }

        switch (isSuccess) {
            case 0:
                //Login successful
                SessionManagement sessionManagement = new SessionManagement(getContext());
                sessionManagement.saveUserType(who, email);

                switch (Global.getUserType()) {
                    case Global.USER:
                        startActivity(new Intent(getContext(), HomeActivity.class));
                        break;
                    case Global.BUSINESS_OWNER:
                        startActivity(new Intent(getContext(), BOHomeActivity.class));
                        break;
                }
                break;

            case 1:
                //Wrong password
                message = "Wrong Password";
                customToast(message);
                break;

            case 2:
                //No user exists
                message = "No user is registered with this email.";
                customToast(message);
                break;

            default:
                customToast("Failed");
        }
    }

    private void customToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
