package com.example.mgutierrezplaza.bbdd;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class Lista extends AppCompatActivity {

    ArrayList<Pantalla> list;
    EditText edtDescrip;
    ImageView imageView, imageView2, imageView3, imageView4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        edtDescrip = findViewById(R.id.edtDescrip);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);

        list = new ArrayList<>();

        Cursor cursor = main.sqLiteHelper.getData("SELECT * FROM PRUEBA");
        list.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            edtDescrip.setText(name);
            byte[] image = cursor.getBlob(2);
            byteToImage(imageView, image);
            byte[] image2 = cursor.getBlob(3);
            //byteToImage(imageView2, image2);
            byte[] image3 = cursor.getBlob(4);
            //byteToImage(imageView3, image3);
            byte[] image4 = cursor.getBlob(5);
            //byteToImage(imageView4, image4);

            list.add(new Pantalla(id, name, image, image2, image3, image4));
        }
    }

    public void byteToImage(ImageView iv, byte[] img){
        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);
        iv.setImageBitmap(bmp);
    }
}
