package edu.ucsd.cse110.team1_personalbest.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;
import java.io.*;

import org.json.*;

public class Database extends AppCompatActivity implements Subject {

    private FirebaseFirestore db;
    private ArrayList<Observer> observers;
    private Map<String, Object> data;

    public Database() {
        //FirebaseApp.initializeApp(this);
        //db = FirebaseFirestore.getInstance();
        observers = new ArrayList<>();
    }

    public Database(FirebaseFirestore store) {
        db = store;
        observers = new ArrayList<>();
    }

    @Override
    public void register(Observer o) {
        if ( !observers.contains(o) ) {
            observers.add(o);
        }
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for( Observer obs : observers ) {
            obs.update(data);
        }
    }


    /**
     * Push to a document reference and insert a Map
     */
    public void push(String document, Map map) {
        db.collection("calendar")
                .document(document)
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added info");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding info", e);
                    }
                });
    }

    /**
     * Notifies observers with the Map data
     * @param document the document location to get the object
     */
    public void get(String document) {
        db.collection("calendar")
                .document(document)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if ( task.isSuccessful() ) {
                            data = task.getResult().getData();
                            notifyObservers();
                            Log.d(TAG, "Got document info");
                        }
                    }
                });
    }

    public void write(String fileName, JSONObject obj, Context c) {
        try {
            File temp = new File(c.getFilesDir(), fileName);
            PrintWriter pw = new PrintWriter(temp);
            pw.write(obj.toString());
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject read(String fileName, Context c) {
        try {
            File temp = new File(c.getFilesDir(), fileName);
            if ( !temp.exists() ) {
                return null;
            }
            FileReader r = new FileReader(temp);
            int i = 0;
            String line = "";
            while ( (i = r.read()) != -1 ) {
                line += (char) i;
            }
            return stringToJSON(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject stringToJSON(String t) throws JSONException {

        HashMap<String, Object> map = new HashMap<>();
        t = t.replaceAll("=", ":");
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            value = value.replaceAll("\\{", "");
            value = value.replaceAll("\\}", "");
            if ( value.contains(" ") ) {
                value = value.replaceAll(" ", "");
            }
            String[] split = value.split(",");
            JSONObject child = new JSONObject();
            for ( String s : split ) {
                child.put(s.split(":")[0], s.split(":")[1]);
            }
            jObject.put(key, child);

        }

        return jObject;
    }
}
