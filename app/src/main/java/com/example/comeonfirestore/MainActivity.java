package com.example.comeonfirestore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public String TAG = "MainActivity";
    public Button addData,getData,signUp,signIn,sendOtp;
    public EditText getName,getSurname,getEmail,getPassword,getPhoneNo;
    public String name,surname;
    public String NAME_KEY = "Name";
    public String SURNAME_KEY = "Surname";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private PhoneAuthProvider phoneAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setComponentsValues();
    }

    public void setComponentsValues(){
        /**Compulsory use try catch block for your function*/
        try {
            //Make TAG variable for your logs and place appropriate logs in your code
            final String TAG = "MainActivity | oncLick ";
            Log.d(TAG, "In function onClick");

            phoneAuthProvider = PhoneAuthProvider.getInstance();
            mAuth = FirebaseAuth.getInstance();

            addData = findViewById(R.id.Add_Data_Button);
            getData = findViewById(R.id.Get_Data_Button);
            signIn = findViewById(R.id.signIn_Button);
            signUp = findViewById(R.id.signUp_Button);
            sendOtp = findViewById(R.id.sendOtp_Button);

            getName = findViewById(R.id.NameEditText);
            getSurname = findViewById(R.id.SurnameEditText);
            getEmail = findViewById(R.id.emailEditText);
            getPassword = findViewById(R.id.passwordEditText);
            getPhoneNo = findViewById(R.id.phoneEditText);

            addData.setOnClickListener(MainActivity.this);
            getData.setOnClickListener(MainActivity.this);
            signIn.setOnClickListener(MainActivity.this);
            signUp.setOnClickListener(MainActivity.this);
            sendOtp.setOnClickListener(MainActivity.this);
        }catch (Exception e){
            Log.e(TAG, "Exception in setComponentsValues function => \n" + e.toString() );
        }
    }

    public void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public String[] listToArray(List<String> list){
        Log.d(TAG, "List size: "+list.size());
        String[] array = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            array[i] = (String) list.get(i);
            Log.d(TAG, i+"th Element "+list.get(i));
        }
        return array;
    }

    @Override
    public void onClick(View v) {
        try{
            final String TAG = "MainActivity | oncLick ";
            Log.d(TAG, "In function onClick");

            name = getName.getText().toString();
            surname = getSurname.getText().toString();

            switch(v.getId()){
                case R.id.Add_Data_Button:
                    Toast.makeText(this, "Add_Data Button Clicked", Toast.LENGTH_SHORT).show();
                    addData();
                    break;
                case R.id.Get_Data_Button:
                    Toast.makeText(this, "Get Data Button Clicked", Toast.LENGTH_SHORT).show();
                    getData();
                    break;
                case R.id.signIn_Button:
                    Toast.makeText(this, "Sign In Button Clicked", Toast.LENGTH_SHORT).show();
                    signInUser();
                    break;
                case R.id.signUp_Button:
                    Toast.makeText(this, "Sign Up Button Clicked", Toast.LENGTH_SHORT).show();
                    signUpUser();
                    break;
                case R.id.sendOtp_Button:
                    Toast.makeText(this, "Send OTP Button Clicked", Toast.LENGTH_SHORT).show();
                    sendOtp();
                    break;
                default:
                    Toast.makeText(this, "This is default case", Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (Exception e){
            Log.e(TAG, "Exception in onClick function => \n" + e.toString() );
        }
    }

    private void sendOtp() {
        String phNo = getPhoneNo.getText().toString();
        makeToast("Number  : " + phNo);
        phoneAuthProvider.verifyPhoneNumber(phNo.trim().toString(), 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                makeToast("OTP Sent to number"+s);
                Log.d(TAG, "onCodeSent: "+s);
                super.onCodeSent(s, forceResendingToken);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                makeToast("Auto Retrieval Timeout");
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                makeToast("Verification Completed");
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                makeToast("Verification Failed");
                Log.d(TAG, "onVerificationFailed: "+e.toString());
            }
        });
    }

    private void addData() {
        try{
            final String TAG = "MainActivity | addData ";
            Log.d(TAG, "In function addData");
            String[] arrayValue ={"First Array Value","Second Array Value"};
            List<String> array = Arrays.asList(arrayValue);

            //arrayValue[0] = "First Array Value";
            //arrayValue[1] = "Second Array Value";

            Map<String, Object> user = new HashMap<>() ;
            user.put(NAME_KEY,name);
            user.put(SURNAME_KEY,surname);
            user.put("Array",array);

            /**To add values in document use .set() method*/
            db.collection("Whole_Hotel").document(name+"_Id")
                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        makeToast("Value added");
                    }else{
                        makeToast("Failed to add value");
                    }
                }
            });

            /**To add values in document use .add() method*/
            /*db.collection("Hotel_Data").document("Employee").collection("AllEmployees")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        makeToast("Add Data Successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeToast("Add Data Failed");
                    }
                });*/
        }catch (Exception e){
            Log.e(TAG, "Exception in Function addData => \n" + e.toString());
        }
    }

    private void getData() {
        try{
            final String TAG = "MainActivity | getData ";
            Log.d(TAG, "In function getData");

            List<String> integerList = new ArrayList<>(2);
            integerList.add("TEN");
            integerList.add("TWENTY");

            String[] stringArray = listToArray(integerList);
            for (int i = 0; i < stringArray.length; i++) {
                Log.d(TAG, "Integer Array: " + stringArray[i]);
            }
            //Log.d(TAG, "Integer Array: "+listToArray(integerList));

            db.collection("Whole_Hotel")
                    //.whereEqualTo("Name","fddf")                              //QUERY//
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "ID: " + document.getId() + " => " + "Data: " + document.getData());
                                }
                            }else{
                                makeToast("Data Fetched Failed");
                            }
                        }
                    });
            /**This is the code to get data from collection*/
            /*db.collection("Whole_Hotel")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "ID: " + document.getId() + " => " + "Data: " + document.getData());
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });*/

            /**This is the code to get data from document*/
                /*
                db.collection("Whole_Hotel").document("documentName")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString(NAME_KEY);
                    String surname = documentSnapshot.getString(SURNAME_KEY);
                    makeToast("Name: "+name+" Surname: "+surname);
                }else{
                    makeToast("Document Does Not Exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("Failed to fetch data"+e.toString());
            }
        });*/
        }catch (Exception e){
            Log.e(TAG, "Exception in getData function => \n" + e.toString() );
        }
    }

    public void signUpUser(){
        String email = getEmail.getText().toString();
        String password = getPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signInUser(){
        String email = getName.getText().toString();
        String password = getSurname.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
