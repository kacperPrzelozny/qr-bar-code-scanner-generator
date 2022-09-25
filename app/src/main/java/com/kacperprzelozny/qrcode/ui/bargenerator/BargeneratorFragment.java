package com.kacperprzelozny.qrcode.ui.bargenerator;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kacperprzelozny.qrcode.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BargeneratorFragment extends Fragment {

    public ImageView qr;
    public EditText input;
    public Button generateButton;
    public TextView save;
    public boolean isGenerated;
    public Bitmap code;

    public BargeneratorFragment() { }

    public static BargeneratorFragment newInstance(String param1, String param2) {
        return new BargeneratorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bar_generator, container, false);

        generateButton = (Button) view.findViewById(R.id.generate_bar);
        input = (EditText) view.findViewById(R.id.to_generate_bar);
        qr = (ImageView) view.findViewById(R.id.generated_bar);
        save = (TextView) view.findViewById(R.id.button_save);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String textToGenerate = input.getText().toString();
                Code128Writer writer = new Code128Writer();
                BitMatrix bitMatrix = writer.encode(textToGenerate, BarcodeFormat.CODE_128, 1024, 768);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                code = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        code.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                qr.setImageBitmap(code);

                save.setVisibility(View.VISIBLE);
                isGenerated = true;

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message;
                File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                code.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                FileOutputStream fs = null;

                try
                {
                    String fullPath = pic.getPath();
                    Log.d("PATH", fullPath);
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String d = df.format(new Date());
                    fs = new FileOutputStream(fullPath + "/" + d + ".jpg");
                    fs.write(byteArray);
                    fs.close();
                    message = "Saved to phone memory";
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    message = "Couldn't save in phone memory";
                }

                Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                toast.setMargin(50, 50);
                toast.show();

            }
        });

        return view;
    }
}