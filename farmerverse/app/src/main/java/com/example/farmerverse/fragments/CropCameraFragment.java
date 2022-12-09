package com.example.farmerverse.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farmerverse.R;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;

import android.content.ContentValues;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Button;

import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CropCameraFragment extends Fragment {

    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    public static final int REQUEST_CODE = 1001;

    PreviewView previewView;
    Button captureButton;
    Context context;
    private Executor executor = Executors.newSingleThreadExecutor();

    public CropCameraFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crop_camera, container, false);

        context = view.getContext();
        previewView = view.findViewById(R.id.previewView);
        captureButton = view.findViewById(R.id.captureButton);



        if (allPermissionGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE);
        }

        return view;
    }


    private boolean allPermissionGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }


    private void startCamera(){
        final ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture
                = ProcessCameraProvider.getInstance(context);

        cameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try{
                    ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                    bindPreview(cameraProvider);

                }
                catch(ExecutionException | InterruptedException e){
                    System.out.println(e.toString());
                }
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider){
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        final ImageCapture imageCapture = builder
                .setTargetRotation(getActivity().getBaseContext().getDisplay().getRotation())
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.unbindAll();

        Camera camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalysis,
                imageCapture
        );

        captureButton.setOnClickListener( v->{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            String displayName = dateFormat.format(new Date())+".jpg";

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");

            if(Build.VERSION.SDK_INT > 30){
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CropTracker");
            }

            ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(
                    context.getContentResolver(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues

            ).build();

            imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context
            ), new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    Toast.makeText(getActivity().getBaseContext(), "Image saved.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    exception.printStackTrace();
                    Toast.makeText(getActivity().getBaseContext(), "No.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}