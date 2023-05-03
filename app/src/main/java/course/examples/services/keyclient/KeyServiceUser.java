package course.examples.services.keyclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;


import course.examples.Services.KeyCommon.KeyGenerator;

public class KeyServiceUser extends Activity {

    protected static final String TAG = "FunServiceUser";
    protected static final int PERMISSION_REQUEST = 0;
    private KeyGenerator mFunService;
    private boolean mIsBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Button getPicture = findViewById(R.id.button_request_picture);
        final Button playclip = findViewById(R.id.button_play_clip);
        final Button pauseclip = findViewById(R.id.button_pause_clip);
        final Button resumeclip = findViewById(R.id.button_resume_clip);
        final Button stopclip = findViewById(R.id.button_stop_clip);
        final EditText picutureID = findViewById(R.id.edit_text_picture_number);
        final EditText clipID = findViewById(R.id.edit_text_clip_number);
        final ImageView IMGview = findViewById(R.id.image_view);

        // button to get the picture
        getPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(mIsBound){
                        String text = picutureID.getText().toString();
                        int picID = Integer.parseInt(text);
                        Bitmap bitmap = mFunService.getBitmaps(picID);

                        if (bitmap != null){
                            Log.i(TAG, "got data");
                        }
                        IMGview.setImageBitmap(bitmap);
                    }
                    else{
                        Log.i(TAG, "Ayo says that the service was not bound!");
                    }}catch (RemoteException e) {

                    Log.e(TAG, e.toString());

                }
            }
        });


        playclip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(mIsBound){
                        String text = clipID.getText().toString();
                        int clipID = Integer.parseInt(text);
                        mFunService.playclip(clipID);
                    }
                    else{
                        Log.i(TAG, "Ayo says that the service was not bound!");
                    }}catch (RemoteException e) {

                    Log.e(TAG, e.toString());

                }
            }
        });

        pauseclip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(mIsBound){
                        mFunService.pauseclip();
                    }
                    else{
                        Log.i(TAG, "Ayo says that the service was not bound!");
                    }}catch (RemoteException e) {

                    Log.e(TAG, e.toString());

                }
            }
        });

        stopclip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(mIsBound){
                        mFunService.stopclip();
                    }
                    else{
                        Log.i(TAG, "Ayo says that the service was not bound!");
                    }}catch (RemoteException e) {

                    Log.e(TAG, e.toString());

                }
            }
        });

        resumeclip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(mIsBound){
                        mFunService.resumeclip();
                    }
                    else{
                        Log.i(TAG, "Ayo says that the service was not bound!");
                    }}catch (RemoteException e) {

                    Log.e(TAG, e.toString());

                }
            }
        });

    }

    // Bind to KeyGenerator Service
    @Override
    protected void onStart() {
        super.onStart();

        if (checkSelfPermission("course.examples.Services.FunCenter.GEN_ID")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{"course.examples.Services.FunCenter.GEN_ID"},
                    PERMISSION_REQUEST);
        }
        else {
            checkBindingAndBind();
        }
    }

    protected void checkBindingAndBind() {
        if (!mIsBound) {

            boolean b ;
            Intent i = new Intent(KeyGenerator.class.getName());
            Log.e("Key client", i.toString()) ;
            ResolveInfo info = getPackageManager().resolveService(i, PackageManager.MATCH_ALL);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
            if (b) {
                Log.i(TAG, "Ayo says bindService() succeeded!");
            } else {
                Log.i(TAG, "Ayo says bindService() failed!");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission granted, go ahead and bind to service
                    checkBindingAndBind();
                }
                else {
                    Toast.makeText(this, "BUMMER: No Permission :-(", Toast.LENGTH_LONG).show() ;
                }
            }
            default: {
                // do nothing
            }
        }
    }
    // Unbind from KeyGenerator Service
    @Override
    protected void onStop() {

        super.onStop();

        if (mIsBound) {
            unbindService(this.mConnection);
        }
    }

    private final ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mFunService = KeyGenerator.Stub.asInterface(iservice);
            mIsBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {

            mFunService = null;
            mIsBound = false;

        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
