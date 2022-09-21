package com.kacperprzelozny.qrcode.ui.qrgenerator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kacperprzelozny.qrcode.R;

import java.util.Objects;

public class QrgeneratorFragment extends Fragment {

    public ImageView qr;
    public EditText input;
    public Button generateButton;

    public QrgeneratorFragment() { }

    public static QrgeneratorFragment newInstance()
    {
        return new QrgeneratorFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_qr_generator, container, false);

        generateButton = (Button) view.findViewById(R.id.generate_qr);
        input = (EditText) view.findViewById(R.id.to_generate_qr);
        qr = (ImageView) view.findViewById(R.id.generated_qr);


        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToGenerate = input.getText().toString();
                QRCodeWriter writer = new QRCodeWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(textToGenerate, BarcodeFormat.QR_CODE, 1024, 1024);
                    int width = bitMatrix.getWidth();
                    int height = bitMatrix.getHeight();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    qr.setImageBitmap(bmp);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}