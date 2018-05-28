package com.example.mgutierrezplaza.bbdd;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class Lista extends AppCompatActivity {

    ArrayList<Pantalla> list;
    EditText edtDescrip;
    ImageView imageView, imageView2, imageView3, imageView4;
    Button btnPlay;
    boolean playing = true;
    private MediaPlayer mPlayer;
    private String audio;

    private void onPlay(boolean start){
        if (start) startPlaying();
        else stopPlaying();
    }

    private void startPlaying(){
        mPlayer = new MediaPlayer();
        try{
            mPlayer.setDataSource(audio);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e){
            Log.e("play", "prepare() failed");
        }
    }

    private void stopPlaying(){
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);

        edtDescrip = findViewById(R.id.edtDescrip);
        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(playing);
                if(playing) btnPlay.setText("Stop playing");
                else btnPlay.setText("Start playing");
                playing = !playing;
            }
        });

        list = new ArrayList<>();

        int i = 1;

        Cursor cursor = main.sqLiteHelper.getData("SELECT id,name FROM PRUEBA");
        Cursor cursor1 = main.sqLiteHelper.getData("SELECT image FROM PRUEBA");
        Cursor cursor2 = main.sqLiteHelper.getData("SELECT image2 FROM PRUEBA");
        Cursor cursor3 = main.sqLiteHelper.getData("SELECT image3 FROM PRUEBA");
        Cursor cursor4 = main.sqLiteHelper.getData("SELECT image4 FROM PRUEBA");
        Cursor cursor5 = main.sqLiteHelper.getData("SELECT audio FROM PRUEBA");
        list.clear();
        while (cursor.moveToNext() && cursor1.moveToNext() && cursor2.moveToNext() && cursor3.moveToNext() && cursor4.moveToNext() && cursor5.moveToNext() && i == 1){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            edtDescrip.setText(name);
            byte[] image = cursor1.getBlob(0);
            byteToImage(imageView, image);
            byte[] image2 = cursor2.getBlob(0);
            byteToImage(imageView2, image2);
            byte[] image3 = cursor3.getBlob(0);
            byteToImage(imageView3, image3);
            byte[] image4 = cursor4.getBlob(0);
            byteToImage(imageView4, image4);
            audio = cursor5.getString(0);
            //list.add(new Pantalla(id, name, image, image2, image3, image4, audio));
            i++;
        }
        cursor.close();
        cursor1.close();
        cursor2.close();
        cursor3.close();
        cursor4.close();
        cursor5.close();
        main.sqLiteHelper.close();
    }

    public void byteToImage(ImageView iv, byte[] img){
        Bitmap bmp = BitmapFactory.decodeByteArray(img,0,img.length);
        iv.setImageBitmap(bmp);
    }
}
