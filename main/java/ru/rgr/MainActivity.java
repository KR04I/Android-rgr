package ru.rgr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import ru.rgr.list.MyListOfArrays;
import ru.rgr.types.factory.FactoryUserType;
import ru.rgr.types.userTypes.UserType;

public class MainActivity extends AppCompatActivity {

    public FactoryUserType factoryUserType;
    public UserType userType;
    public MyListOfArrays myListOfArrays;

    private final String FILE_NAME_INTEGER = "integer.txt";
    private final String FILE_NAME_POINT = "point.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factoryUserType = new FactoryUserType();
        ArrayList<String> typeNameList = factoryUserType.getTypeNameList();
        String[] types = new String[typeNameList.size()];
        for (int i = 0; i < typeNameList.size(); i++) {
            types[i] = typeNameList.get(i);
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userType = FactoryUserType.getBuilderByName(parent.getSelectedItem().toString());
                assert userType != null;
                myListOfArrays = new MyListOfArrays(100);
                outText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createButtons();
    }

    private void createButtons() {
        Button deleteByIdButton = (Button) findViewById(R.id.deleteByIdBtn);
        Button insertByIdButton = (Button) findViewById(R.id.insertByIdBtn);
        Button findByIdButton = (Button) findViewById(R.id.findByIdBtn);
        Button insertButton = (Button) findViewById(R.id.insertBtn);
        Button sortButton = (Button) findViewById(R.id.sortBtn);
        Button clearButton = (Button) findViewById(R.id.clearBtn);
        Button saveButton = findViewById(R.id.saveBtn);
        Button loadButton = findViewById(R.id.loadBtn);

        deleteByIdButton.setOnClickListener(view -> {
            EditText deleteByIdField = (EditText) findViewById(R.id.deleteEditText);
            if (deleteByIdField.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), "Введите индекс для удаления!", Toast.LENGTH_LONG).show();
            } else {
                if (myListOfArrays.get(Integer.parseInt(String.valueOf(deleteByIdField.getText()))) == null) {
                    Toast.makeText(getBaseContext(), "Введите правильный индекс для удаления!", Toast.LENGTH_LONG).show();
                } else {
                    myListOfArrays.remove(Integer.parseInt(String.valueOf(deleteByIdField.getText())));
                    outText();
                }
            }
        });

        insertByIdButton.setOnClickListener(view -> {
            EditText insertByIdField = (EditText) findViewById(R.id.addEditText);
            if (insertByIdField.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), "Введите индекс для вставки!", Toast.LENGTH_LONG).show();
            } else {
                if (myListOfArrays.get(Integer.parseInt(String.valueOf(insertByIdField.getText()))) == null) {
                    Toast.makeText(getBaseContext(), "Введите правильный индекс для вставки!", Toast.LENGTH_LONG).show();
                } else {
                    myListOfArrays.insert(userType.create(), Integer.parseInt(String.valueOf(insertByIdField.getText())));
                    outText();
                }
            }
        });

        findByIdButton.setOnClickListener(view -> {
            EditText findByIdField = (EditText) findViewById(R.id.findEditText);
            if (findByIdField.getText().toString().equals("")) {
                Toast.makeText(getBaseContext(), "Введите индекс для поиска!", Toast.LENGTH_LONG).show();
            } else {
                if (myListOfArrays.get(Integer.parseInt(String.valueOf(findByIdField.getText()))) == null) {
                    Toast.makeText(getBaseContext(), "Введите правильный индекс для поиска!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "Ваш элемент:\n" +
                                    myListOfArrays.get(Integer.parseInt(String.valueOf(findByIdField.getText()))).toString()
                            , Toast.LENGTH_LONG).show();
                }
            }
        });

        clearButton.setOnClickListener(view ->{
            myListOfArrays = new MyListOfArrays(100);
            outText();
        });

        insertButton.setOnClickListener(view -> {
            myListOfArrays.add(userType.create());
            outText();
        });

        sortButton.setOnClickListener(view -> {
            myListOfArrays = myListOfArrays.sort(userType.getTypeComparator());
            outText();
        });

        saveButton.setOnClickListener(view -> {
            BufferedWriter bufferedWriter = null;
            try {
                if (userType.typeName().equals("Integer")) {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter((openFileOutput(FILE_NAME_INTEGER, MODE_PRIVATE))));
                } else {
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter((openFileOutput(FILE_NAME_POINT, MODE_PRIVATE))));
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(getBaseContext(), "Ошибка при записи файла!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            try {
                bufferedWriter.write(userType.typeName() + "\n");
                bufferedWriter.write(myListOfArrays.getSizeOfArrays() + "\n");
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Ошибка при записи файла!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            BufferedWriter finalBufferedWriter = bufferedWriter;
            myListOfArrays.forEach(el -> {
                try {
                    finalBufferedWriter.write(Arrays.toString((Object[]) el) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Toast.makeText(getBaseContext(), "Список успешно сохранен в файл!", Toast.LENGTH_LONG).show();
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loadButton.setOnClickListener(view -> {
            BufferedReader bufferedReader;
            try {
                if (userType.typeName().equals("Integer")) {
                    bufferedReader = new BufferedReader(new InputStreamReader((openFileInput(FILE_NAME_INTEGER))));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader((openFileInput(FILE_NAME_POINT))));
                }
            } catch (Exception ex) {
                Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                return;
            }
            String line;
            try {
                line = bufferedReader.readLine();
                if (line == null) {
                    Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!userType.typeName().equals(line)) {
                    Toast.makeText(getBaseContext(), "Неправильный формат файла!", Toast.LENGTH_LONG).show();
                    return;
                }

                line = bufferedReader.readLine();
                int sizeOfArrays = (int) Math.pow(Integer.parseInt(line), 2);
                System.out.println(sizeOfArrays);
                myListOfArrays = new MyListOfArrays(sizeOfArrays);

                while ((line = bufferedReader.readLine()) != null) {
                    try {
                        parseString(myListOfArrays, line, userType);
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "Ошибка при чтении файла!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outText();

        });
    }

    private void outText() {
        TextView outTextView = (TextView) findViewById(R.id.listView);
        outTextView.setMovementMethod(new ScrollingMovementMethod());
        outTextView.setText(myListOfArrays.toString());
    }

    private static void parseString(MyListOfArrays list, String s, UserType userType) {
        s = s.replace("[", "");
        s = s.replace("]", "");
        String[] arrayFromString = s.split(", ");

        for (int i = 0; i < arrayFromString.length; i++) {
            if (!Objects.equals(arrayFromString[i], "null")) {
                list.add(userType.parseValue(arrayFromString[i]));
            }
        }
    }
}