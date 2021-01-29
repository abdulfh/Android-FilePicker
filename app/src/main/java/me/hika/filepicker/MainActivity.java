package me.hika.filepicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnImg,btnFile,btnAudio,btnVideo;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        btnImg = findViewById( R.id.btnImg );
        btnImg.setOnClickListener( this );

        btnFile = findViewById( R.id.btnFile );
        btnFile.setOnClickListener( this );

        btnAudio = findViewById( R.id.btnAudio );
        btnAudio.setOnClickListener( this );

        btnVideo = findViewById( R.id.btnVideo );
        btnVideo.setOnClickListener( this );
    }

    @Override
    public void onClick( View view ) {
        switch (view.getId()) {
            case R.id.btnImg:
                if(ContextCompat.checkSelfPermission( MainActivity.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions( MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1 );
                } else {
                    btnImgSection();
                }
                break;
            case R.id.btnFile:
                btnFileSection();
                break;
            case R.id.btnAudio:
                btnAudioSection();
                break;
            case R.id.btnVideo:
                if(ContextCompat.checkSelfPermission( MainActivity.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions( MainActivity.this, new String[]{Manifest.permission.CAMERA}, 2 );
                } else {
                    btnVideoSection();
                }
                break;
        }
    }

    private void btnVideoSection() {
            Intent intent = new Intent( MainActivity.this, FilePickerActivity.class );
            intent.putExtra( FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission( true )
                    .setShowVideos( true )
                    .setShowImages( false )
                    .enableVideoCapture( true )
                    .setMaxSelection( 1 )
                    .setSkipZeroSizeFiles( true )
                    .build() );
            startActivityForResult( intent, 104 );
    }

    private void btnAudioSection() {
        Intent intent = new Intent(MainActivity.this, FilePickerActivity.class );
        intent.putExtra( FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission( true )
                .setShowAudios( true )
                .setShowImages( false )
                .setShowVideos( false )
                .setMaxSelection( 1 )
                .setSkipZeroSizeFiles( true )
                .build());
        startActivityForResult( intent,103 );
    }

    private void btnFileSection() {
        Intent intent = new Intent(MainActivity.this, FilePickerActivity.class );
        intent.putExtra( FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission( true )
                .setShowImages( true )
                .setShowVideos( false )
                .enableImageCapture( true )
                .setMaxSelection( 1 )
                .setSuffixes( "txt","pdf","docx" )
                .build());
        startActivityForResult( intent,102 );
    }

    private void btnImgSection() {
            Intent intent = new Intent(MainActivity.this, FilePickerActivity.class );
            intent.putExtra( FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission( true )
                    .setShowImages( true )
                    .setShowVideos( false )
                    .enableImageCapture( true )
                    .setMaxSelection( 1 )
                    .setSkipZeroSizeFiles( true )
                    .build());
            startActivityForResult( intent,101 );
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if ((grantResults.length > 0 ) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            if (requestCode == 1) {
                btnImgSection();
            } else {
                btnVideoSection();
            }
        } else {
            Toast.makeText( getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<MediaFile> mediaFiles = data.getParcelableArrayListExtra( FilePickerActivity.MEDIA_FILES );
            String path = mediaFiles.get( 0 ).getPath();
            String message;
            switch (requestCode) {
                case 101:
                    message = "Image Path : " +path;
                    break;
                case 102:
                    message = "File Path : " +path;
                    break;
                case 103:
                    message = "Audio Path : " +path;
                    break;
                case 104:
                    message = "Video Path : " +path;
                    break;
                default:
                    throw new IllegalStateException( "Unexpected value: " + requestCode );
            }
            Toast.makeText( MainActivity.this, message, Toast.LENGTH_SHORT ).show();
        }
    }
}