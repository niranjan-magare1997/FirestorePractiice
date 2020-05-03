package com.example.comeonfirestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public class Database {
    private FirebaseFirestore db;
    /** Use this TAG for your logs and place appropriate logs in your code starting with function name they are in and a pipe as below functions */
    private static String TAG = "Dream_Team";

    public Database() {
        db = FirebaseFirestore.getInstance();
    }

    public void addEmployee(Map<String,Object> map){
        try{
            Log.d(TAG, "addEmployee | In function addEmployee() ");

            db.collection("Employee").document("someEmpId")
                    /**SetOptions.merge() will work same as update() method,
                     *but if collection or document is not exist this will create them and then insert the data in it*/
                    .set(map, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "addEmployee | Employee Added Successful");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "addEmployee | Employee Adding Failed......");
                }
            });
        }catch(Exception e){
            Log.e(TAG, "addEmployee | Exception in addEmployee: "+e.getMessage());
        }
    }

    public String EMPLOYEE() { return "EMPLOYEE"; }

    public String TABLE() { return "ORDERS"; }

    public String REPORT() { return "REPORT"; }
}
