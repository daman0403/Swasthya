package com.example.android.swasthya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.*;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class createAccountActivity extends AppCompatActivity {

    Button createAccountBtn,generateOtp;
    EditText phone, otp, userName;
    FirebaseAuth mAuth;
    String VerificationID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createAccountBtn = findViewById(R.id.create_account_btn);
        generateOtp = findViewById(R.id.verify_otp_btn);
        phone = findViewById(R.id.phoneNumber_editText);
        otp = findViewById(R.id.otp_editText);
        userName = findViewById(R.id.user_name_editText);
        mAuth = FirebaseAuth.getInstance();
        
        generateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(createAccountActivity.this, "Enter Valid Phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    String phoneNumber = phone.getText().toString();
                    sendVerificationCode(phoneNumber);
                }

            }
        });
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(createAccountActivity.this, "Wrong OTP Entered", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifyCode(otp.getText().toString());
                }

            }
        });
    }

    private void verifyCode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationID,Code);
        signInByCredentials(credential);

    }

    private void signInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(createAccountActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(createAccountActivity.this, MainActivity.class));
                }
            }
        });
    }

    private void sendVerificationCode( String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted( PhoneAuthCredential credential) {

            final String code = credential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed( FirebaseException e) {
            Toast.makeText(createAccountActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent( String s,
               PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            VerificationID = s;
        }
    };
}