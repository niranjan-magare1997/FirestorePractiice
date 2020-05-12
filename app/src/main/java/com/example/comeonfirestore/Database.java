package com.example.comeonfirestore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;
import java.util.Random;

public class Database {
    private FirebaseFirestore db;
    /**
     * Use this TAG for your logs and place appropriate logs in your code starting with function name they are in and a pipe as below functions
     */
    private static String TAG = "Dream_Team";

    public Database() {
        db = FirebaseFirestore.getInstance();
    }

    private Integer generateNewId() {
        final int min = 0;
        final int max = 9;
        final int random = new Random().nextInt((max - min) + 1) + min;
        Log.d(TAG, "generateNewId | Random Number Generated => " + random);
        return random;
    }

    public void addEmployee(final Map<String, Object> map) {
        try {
            Log.d(TAG, "addEmployee | In function addEmployee() ");
            final String newEmpId = "EMP_" + generateNewId().toString();

            db.collection("Employee").document(newEmpId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists() == true) {
                                Log.d(TAG, "addEmployee | Employee already exist with the same Id");
                                String name = documentSnapshot.getString("Name");
                                String surname = documentSnapshot.getString("Surname");
                                Log.d(TAG, "addEmployee | Name: " + name + " Surname: " + surname);
                            } else {
                                Log.d(TAG, "addEmployee | Employee Not exist already");

                                /**
                                 * SetOptions.merge() will work same as update() method,
                                 *but if collection or document is not exist this will create them and then insert the data in it
                                 * */

                                db.collection("Employee").document(newEmpId)
                                        .set(map, SetOptions.merge())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "addEmployee | Added new Employee");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "addEmployee | Failed to add new Employee");
                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "addEmployee | Failed to check Employee exist or not");
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "addEmployee | Exception in addEmployee: " + e.getMessage());
        }
    }

    public void deleteEmployee(String empName, String empSurname) {
        try {
            db.collection("Employee")
                    .whereEqualTo("Name", empName)
                    .whereEqualTo("Surname", empSurname)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, "getData | ID: " + document.getId() + " => " + "Data: " + document.getData());
                                    db.collection("Employee").document(document.getId())
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "deleteEmployee | Employee deleted");
                                                }
                                            });
                                }
                            } else {
                                Log.e(TAG, "deleteEmployee | Exception in getting Employee ");
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "deleteEmployee | Exception in deleteEmployee " + e.getMessage());
        }
    }
}
