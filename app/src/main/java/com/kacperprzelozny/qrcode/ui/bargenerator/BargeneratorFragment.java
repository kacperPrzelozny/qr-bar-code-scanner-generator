package com.kacperprzelozny.qrcode.ui.bargenerator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kacperprzelozny.qrcode.R;

public class BargeneratorFragment extends Fragment {

    public ImageView qr;
    public EditText input;
    public Button generateButton;

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


        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToGenerate = input.getText().toString();
                Code128Writer writer = new Code128Writer();
                try {
                    BitMatrix bitMatrix = writer.encode(textToGenerate, BarcodeFormat.CODE_128, 1024, 768);
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