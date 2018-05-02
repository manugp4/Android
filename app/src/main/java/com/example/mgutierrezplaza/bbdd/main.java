package com.example.mgutierrezplaza.bbdd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class main extends AppCompatActivity {

    EditText edtDescrip;
    Button btnGuardar, btnVer;
    ImageView imageView, imageView2, imageView3, imageView4;

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();

        sqLiteHelper = new SQLiteHelper(this, "PruebaDB.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS PRUEBA (Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, image BLOB, image2 BLOB, image3 BLOB, image4 BLOB)");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sqLiteHelper.insertData(
                            edtDescrip.getText().toString().trim(),
                            imageViewToByte(imageView),
                            imageViewToByte(imageView2),
                            imageViewToByte(imageView3),
                            imageViewToByte(imageView4)
                    );
                    Toast.makeText(getApplicationContext(), "Insertado bien", Toast.LENGTH_SHORT).show();
                    edtDescrip.setText("");
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    imageView2.setImageResource(R.mipmap.ic_launcher);
                    imageView3.setImageResource(R.mipmap.ic_launcher);
                    imageView4.setImageResource(R.mipmap.ic_launcher);
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, Lista.class);
                startActivity(intent);
            }
        });

    }

    private byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void init(){
        edtDescrip = findViewById(R.id.edtDescrip);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnVer = findViewById(R.id.btnVer);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
    }
}
