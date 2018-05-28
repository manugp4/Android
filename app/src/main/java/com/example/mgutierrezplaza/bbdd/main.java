package com.example.mgutierrezplaza.bbdd;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class main extends AppCompatActivity {

    EditText edtDescrip;
    Button btnGuardar, btnVer;
    ImageView imageView, imageView2, imageView3, imageView4;

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1001;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    public static SQLiteHelper sqLiteHelper;

    private static final int REQUEST_AUDIO = 200;
    private static String mFileName = null;

    private Button btnRecord;
    private MediaRecorder mRecorder = null;

    private Button btnPlay;
    private MediaPlayer mPlayer = null;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    private int seleccionIV = 0;

    private ProgressDialog pd;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_AUDIO) permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        else if(requestCode == MY_CAMERA_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            }
        }
        if(!permissionToRecordAccepted) finish();
    }

    private void onRecord(boolean start){
        if(start) startRecording();
        else stopRecording();
    }

    private void onPlay(boolean start){
        if (start) startPlaying();
        else stopPlaying();
    }

    private void startPlaying(){
        mPlayer = new MediaPlayer();
        if (mPlayer == null) {
            return;
        }
        else mPlayer.stop();
        try{
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
        } catch (IOException e){
            Log.e("play", "prepare() failed");
        }
        mPlayer.start();
    }

    private void stopPlaying(){
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording(){
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mRecorder.prepare();
        } catch(IOException e){
            Log.e("record", "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording(){
        mRecorder.stop();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_AUDIO);

        init();

        edtDescrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtDescrip.length() > 0){
                    TextKeyListener.clear(edtDescrip.getText());
                }
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                if(mStartRecording) btnRecord.setText("Stop recording");
                else btnRecord.setText("Start recording");
                mStartRecording = !mStartRecording;
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if(mStartPlaying) btnPlay.setText("Stop playing");
                else btnPlay.setText("Start playing");
                mStartPlaying = !mStartPlaying;
            }
        });

        sqLiteHelper = new SQLiteHelper(this, "PruebaDB.sqlite", null, 1);

        sqLiteHelper.queryData("DROP TABLE PRUEBA");

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS PRUEBA (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR, image BLOB, image2 BLOB, image3 BLOB, image4 BLOB, audio VARCHAR)");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                Drawable myDrawable = getResources().getDrawable(R.mipmap.ic_launcher);
                final Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
                if(imageView.getDrawable().equals()){

                }
                guardarEnBBDD();
            }
        });

        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main.this, Lista.class);
                startActivity(intent);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionIV = 1;
                showDialog(main.this, "Elija una de las opciones:");
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionIV = 2;
                showDialog(main.this, "Elija una de las opciones:");
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionIV = 3;
                showDialog(main.this, "Elija una de las opciones:");
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionIV = 4;
                showDialog(main.this, "Elija una de las opciones:");
            }
        });

    }

    private void guardarEnBBDD(){
        pd = ProgressDialog.show(this, "Cargando",
                "Espere mientras se guarda la información", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sqLiteHelper.insertData(
                        edtDescrip.getText().toString().trim(),
                        imageViewToByte(imageView),
                        imageViewToByte(imageView2),
                        imageViewToByte(imageView3),
                        imageViewToByte(imageView4),
                        mFileName
                );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
            }
        }).start();
        //Toast.makeText(getApplicationContext(), "Insertado bien", Toast.LENGTH_SHORT).show();
        edtDescrip.setText("");
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView2.setImageResource(R.mipmap.ic_launcher);
        imageView3.setImageResource(R.mipmap.ic_launcher);
        imageView4.setImageResource(R.mipmap.ic_launcher);
        mFileName = "";

    }

    private byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
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
        btnRecord = findViewById(R.id.btnRecord);
        btnPlay = findViewById(R.id.btnPlay);
    }

    public void showDialog(Activity activity, String title){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if(title != null) builder.setTitle(title);

        builder.setPositiveButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPermission();
            }
        });
        builder.setNegativeButton("Camara", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    //File file = new File(Environment.getExternalStorageDirectory().getPath() + "/ImagenesSQLite/" + cont + ".jpg");
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        builder.show();

    }

    public void pickImageFromGallery() {
        /*Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), 1);*/
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            switch(seleccionIV){
                case 1: requestCode1(data, imageView);
                        break;
                case 2: requestCode1(data, imageView2);
                        break;
                case 3: requestCode1(data, imageView3);
                        break;
                case 4: requestCode1(data, imageView4);
                        break;
                default: finish();
                        break;
            }
        } else if (requestCode == CAMERA_REQUEST) {
            switch(seleccionIV){
                case 1: requestCodeCamera(data, imageView);
                    break;
                case 2: requestCodeCamera(data, imageView2);
                    break;
                case 3: requestCodeCamera(data, imageView3);
                    break;
                case 4: requestCodeCamera(data, imageView4);
                    break;
                default: finish();
                    break;
            }
        }
    }

    public void requestCode1 (Intent data, ImageView iv){
        Uri img = data.getData();
        iv.setImageURI(img);
        final Bundle extras = data.getExtras();
        if (extras != null) {
            //Get image
            Bitmap bitmap = extras.getParcelable("data");
            iv.setImageBitmap(bitmap);
        }
    }

    public void requestCodeCamera(Intent data, ImageView iv){
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        iv.setImageBitmap(photo);
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.ivPrincipal);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }*/

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission(){
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
        else pickImageFromGallery();
    }


}
