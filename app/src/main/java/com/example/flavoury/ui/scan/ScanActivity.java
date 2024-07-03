package com.example.flavoury.ui.scan;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavoury.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanActivity extends AppCompatActivity {
    ImageButton capture, backBtn, galleryBtn, cancelDialogBtn;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CameraManager cameraManager;
    private final static String apiKey = "dkvkAngJ.4CRlETLTm0kAH2kVshhufooTkUandXDI";
    TextureView textureView;
    String mCameraId;
    Dialog ingredientDialog, progressDialog;
    ProgressBar progressBar;
    OnBackPressedCallback onBackPressedCallback;
    RecyclerView recyclerView;
    IngredientAdapter ingredientAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        getSupportActionBar().hide();

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        backBtn = findViewById(R.id.scan_backBtn);
        textureView = findViewById(R.id.scan_textureView);
        capture = findViewById(R.id.scan_capture);
        galleryBtn = findViewById(R.id.scan_gallery);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.dialog_box_loading);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
//        progressBar = new ProgressBar(progressDialog.getContext());


        backBtn.setOnClickListener(v->getOnBackPressedDispatcher().onBackPressed());

        ingredientDialog = new Dialog(this);
        ingredientDialog.setContentView(R.layout.dialog_box_ingredient);
        ingredientDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ingredientDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_rec_search_dialog));
        ingredientDialog.setCancelable(true);
        ingredientDialog.setCanceledOnTouchOutside(true);
        recyclerView = ingredientDialog.findViewById(R.id.scan_dialog_recyclerview);
        cancelDialogBtn = ingredientDialog.findViewById(R.id.scan_dialog_cancelBtn);

        cancelDialogBtn.setOnClickListener(v->{
            ingredientDialog.cancel();
        });

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri ->{
            if(uri != null){
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];

                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    sendImage(byteArrayOutputStream.toByteArray());

                } catch (Exception e) {
                    Log.d("PhotoPicker",e.toString());
                }
            }else {
                Log.d("PhotoPicker","no media selected");
            }
        });

        capture.setOnClickListener(v-> capturePhoto());
        galleryBtn.setOnClickListener(v->{
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        try {
            String cameraId = cameraManager.getCameraIdList()[0]; // Use first camera
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    cameraDevice.close();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    cameraDevice.close();
                    cameraDevice = null;
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(1080, 1920); // Set your desired preview size
            Surface surface = new Surface(texture);

            final CaptureRequest.Builder previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice == null) return;
                    cameraCaptureSession = session;
                    try {
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        cameraCaptureSession.setRepeatingRequest(previewRequestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(getApplicationContext(), "Failed to configure camera", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void capturePhoto(){
        if (cameraDevice == null) return;

        try {
            // Set up an ImageReader to capture the photo
            ImageReader reader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            // Create a CaptureRequest for taking a picture
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            final File file = new File(getExternalFilesDir(null), "pic.jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        sendImage(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener, null);

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                            @Override
                            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                                super.onCaptureCompleted(session, request, result);
                                Toast.makeText(getApplicationContext(), "Photo saved: " + file, Toast.LENGTH_SHORT).show();
                                createCameraPreviewSession();
                            }
                        }, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int getOrientation(int rotation) {
        return (ORIENTATIONS.get(rotation) + getSensorOrientation()) % 360;
    }

    private int getSensorOrientation() {
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            return characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private void sendImage(byte[] bytes){
        progressDialog.show();
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"), bytes))
                .build();

        Request request = new Request.Builder()
                .url("https://vision.foodvisor.io/api/1.0/en/analysis/")
                .addHeader("Authorization", "Api-Key "+ apiKey)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle response
                String jsonResponseString = response.body().string();
                Log.d("FoodVisor",jsonResponseString);
                ArrayList<String> foodName = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponseString);
                    JSONArray items = jsonObject.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONArray foods = item.getJSONArray("food");

                        for (int j = 0; j < foods.length(); j++) {
                            JSONObject food = foods.getJSONObject(j);
                            JSONObject foodInfo = food.getJSONObject("food_info");
                            String displayName = foodInfo.getString("display_name");
                            foodName.add(displayName);
                        }
                    }
                } catch (Exception e) {
                    Log.d("FoodVisor",e.toString());
                } finally {
                    runOnUiThread(()->{
                        if (foodName!=null) {
                            progressDialog.cancel();
                            ingredientDialog.show();
                            ingredientAdapter = new IngredientAdapter(foodName);
                            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
                            recyclerView.setAdapter(ingredientAdapter);
                            layoutManager.setFlexDirection(FlexDirection.ROW);
                            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
                            recyclerView.setLayoutManager(layoutManager);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        openCamera();
    }
}
