package ru.mirea.kovalyov.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText editTextFile;
    private EditText editTextText;

    private SharedPreferences preferences;
    final String SAVED_TEXT = "SAVED_TEXT";

    private String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFile = findViewById(R.id.editTextFile);
        editTextText = findViewById(R.id.editTextText);

        preferences = getPreferences(MODE_PRIVATE);

        fileName = preferences.getString(SAVED_TEXT, "");
        if (!fileName.equals("")) {
            new Thread(new Runnable() {
                public void run() {
                    editTextText.post(new Runnable() {
                        public void run() {
                            editTextText.setText(getTextFromFile());
                        }
                    });
                }
            }).start();

            editTextFile.setText(fileName);
        }
    }

    public void onClick(View view) {
        SharedPreferences.Editor editor = preferences.edit();
        fileName = editTextFile.getText().toString() + ".txt";
        editor.putString(SAVED_TEXT, fileName);
        editor.apply();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        String text = editTextText.getText().toString();

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTextFromFile() {
        try (FileInputStream fis = openFileInput(fileName)) {
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            String text = new String(bytes);
            return text;
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return "";
    }
}