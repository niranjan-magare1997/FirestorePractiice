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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**                                *** NOTE ***
 *
 * 1) Use camelCase for variable and function names
 * 2) Keep your all variable and components private if not needed outside your class
 * 3) Keep your all functions private if not needed outside your class
 * 4) Add {try catch} block for your function in order to avoid app crash
 * 5) Common Log Format => Log.d(TAG, " Method_name | Your_Message ");
 * 6) Use separate function to initialise all the components and to set onclick listener too
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /** Use this TAG for your logs and place appropriate logs in your code starting with function name they are in and a pipe as below functions */
    private static String TAG = "Dream_Team";
    private Button addDataBtn,getDataBtn,signUpBtn,signInBtn,sendOtpBtn;
    private EditText getName,getSurname,getEmail,getPassword,getPhoneNo;
    private String name,surname;
    private String NAME_KEY = "Name";
    private String SURNAME_KEY = "Surname";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider phoneAuthProvider;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setComponentsValues();
    }

    public void setComponentsValues(){
        /** Compulsory use try catch block for your function */
        try {
            Log.d(TAG, "setComponentsValues | In function setComponentsValues ");

            database = new Database();
            phoneAuthProvider = PhoneAuthProvider.getInstance();
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();

            addDataBtn = findViewById(R.id.Add_Data_Button);
            getDataBtn = findViewById(R.id.Get_Data_Button);
            signInBtn = findViewById(R.id.signIn_Button);
            signUpBtn = findViewById(R.id.signUp_Button);
            sendOtpBtn = findViewById(R.id.sendOtp_Button);

            getName = findViewById(R.id.NameEditText);
            getSurname = findViewById(R.id.SurnameEditText);
            getEmail = findViewById(R.id.emailEditText);
            getPassword = findViewById(R.id.passwordEditText);
            getPhoneNo = findViewById(R.id.phoneEditText);

            addDataBtn.setOnClickListener(MainActivity.this);
            getDataBtn.setOnClickListener(MainActivity.this);
            signInBtn.setOnClickListener(MainActivity.this);
            signUpBtn.setOnClickListener(MainActivity.this);
            sendOtpBtn.setOnClickListener(MainActivity.this);

            Log.d(TAG, "setComponentsValues | Done with setting Components Values ");
        }catch (Exception e){
            Log.e(TAG, "setComponentsValues | Exception in setComponentsValues function: " + e.getMessage());
        }
    }

    public void makeToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public String[] listToArray(List<String> list){
        Log.d(TAG, "List size: "+list.size());
        String[] array = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            array[i] = list.get(i);
            Log.d(TAG, i+"th Element "+list.get(i));
        }
        return array;
    }

    @Override
    public void onClick(View v) {
        try{
            Log.d(TAG, "onClick | In function onClick");

            name = getName.getText().toString();
            surname = getSurname.getText().toString();

            switch(v.getId()){
                case R.id.Add_Data_Button:
                    Toast.makeText(this, "Add_Data Button Clicked", Toast.LENGTH_SHORT).show();
                    addDataFun();
                    //addSomething();
                    //addData();
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
            Log.e(TAG, "onClick | Exception in onClick function: " + e.getMessage());
        }
    }

    private void sendOtp() {
        try{
            Log.d(TAG, "sendOtp | In function sendOtp() ");
            String phNo = getPhoneNo.getText().toString();
            makeToast("Number  : " + phNo);

            phoneAuthProvider.verifyPhoneNumber(phNo.trim(), 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    makeToast("OTP Sent to number"+s);
                    Log.d(TAG, "Verification code sent. ");
                }

                @Override
                public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                    super.onCodeAutoRetrievalTimeOut(s);
                    makeToast("Auto Retrieval Timeout");
                    Log.d(TAG, "Verification code autoRetrieval Timeout. ");
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    makeToast("Verification Completed");
                    Log.d(TAG, "Verification code verified successfully. ");
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    makeToast("Verification Failed");
                    Log.d(TAG, "Code verification failed " + e.toString());
                }
            });
        }catch(Exception e){
            Log.e(TAG, "sendOtp | Exception in sendOtp: " + e.getMessage());
        }
    }

    private void addDataFun(){
        try {
            Log.d(TAG, "addDataFun: In function addDataFun: ");
            String name = getName.getText().toString();
            String sName = getSurname.getText().toString();

            Map<String,Object> emp = new HashMap<>();
                emp.put(NAME_KEY,name);
                emp.put(SURNAME_KEY,sName);

            database.addEmployee(emp);
        }catch (Exception e){
            Log.e(TAG, "addDataFun: Exception while adding data: " + e.getMessage());
        }
    }

    private void addSomething(){
        try{
            Log.d(TAG, "addSomething | In function onClick");
            /*
            List<String> integerList = new ArrayList<>(2);
            integerList.add("TEN");
            integerList.add("TWENTY");

            String[] stringArray = listToArray(integerList);
            for (int i = 0; i < stringArray.length; i++) {
                Log.d(TAG, "Integer Array: " + stringArray[i]);
            }
            */
            String nm = getName.getText().toString();
            String sName = getSurname.getText().toString();

            Map<String, Object> map2 = new HashMap<>();
            map2.put("extra1","My Value 2");
            map2.put("extra2","My value");

            Map<String,Object> map = new HashMap<>();
            map.put("Name",nm);
            map.put("Surname",sName);
            map.put("The Extra Field",map2);

            db.collection("Database").document("DB=1")
                    /**SetOptions.merge() will work same as update() method,
                     but if collection or document is not exist this will create them and then insert the data in it*/
                    .set(map, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "addSomething | Add Successful");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "addSomething | Add Failed");
                }
            });
        }catch (Exception e){
            Log.e(TAG, "addSomething | Exception in addSomething: " + e.getMessage());
        }
    }

    private void addData() {
        final String TAG = "MainActivity | addData ";
        try{
            Log.d(TAG, "In function addData");
            String[] arrayValue = {
                    "First Array Value",
                    "Second Array Value"
            };
            List<String> array = Arrays.asList(arrayValue);

            Map<String, Object> user = new HashMap<>() ;
            user.put(NAME_KEY,name);
            user.put(SURNAME_KEY,surname);
            /** Adding array as a value for key "Array" */
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
            Log.e(TAG, "Exception in Function addData => \n" + e.getMessage());
        }
    }

    private void getData() {
        try{
            Log.d(TAG, "getData | In function getData");

            db.collection("Employee")
                /**QUERY*/
                //.whereEqualTo("Name","Niranjan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "getData | ID: " + document.getId() + " => " + "Data: " + document.getData());
                        }
                    }else{
                        Log.e(TAG, "getData | Failed to get data");
                        makeToast("getData | Data Fetched Failed");
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
            Log.e(TAG, "getData | Exception in getData function: " + e.getMessage());
        }
    }

    private void signUpUser(){
        try{
            Log.d(TAG, "signUpUser | In function signUpUser ");
            String email = getEmail.getText().toString();
            String password = getPassword.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signUpUser | createUserWithEmail success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signUpUser | createUserWithEmail failure", task.getException());
                    }
                    }
                });
        }catch (Exception e){
            Log.e(TAG, "signUpUser | Exception in signUpUser " + e.getMessage());
        }
    }

    private void signInUser(){
        try{
            Log.d(TAG, "signInUser | In function signInUser");
            String email = getEmail.getText().toString();
            String password = getPassword.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInUser | signInWithEmail success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInUser | signInWithEmail failure", task.getException());
                    }
                    }
                });
        }catch (Exception e){
            Log.e(TAG, "signInUser | Exception in signInUser: " + e.getMessage());
        }
    }
}
