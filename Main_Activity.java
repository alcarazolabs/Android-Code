package com.example.readexcel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    Workbook workbook;
    AsyncHttpClient asyncHttpClient;
    List<String> titulos,descripciones,imagenes;

    private RecyclerView recyclerView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        titulos = new ArrayList<>();
        descripciones = new ArrayList<>();
        imagenes = new ArrayList<>();

        String URL = "https://bikashthapa01.github.io/excel-reader-android-app/story.xls";
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(URL, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(MainActivity.this, "Error al descargar el archivo Excel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                WorkbookSettings ws = new WorkbookSettings();
                ws.setGCDisabled(true);
                if(file!=null){
                    try {
                        workbook = Workbook.getWorkbook(file);
                        Sheet sheet = workbook.getSheet(0);
                        for(int i=0;i<sheet.getRows();i++){
                            Cell[] row = sheet.getRow(0);
                            titulos.add(row[0].getContents());
                            descripciones.add(row[1].getContents());
                            imagenes.add(row[2].getContents());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                    mostrarData();
                }
            }
        });
    }
    private void mostrarData(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,titulos,descripciones,imagenes);
        recyclerView.setAdapter(adapter);
    }
}
