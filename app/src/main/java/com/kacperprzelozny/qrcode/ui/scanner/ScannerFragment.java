package com.kacperprzelozny.qrcode.ui.scanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.kacperprzelozny.qrcode.R;

import java.util.Objects;

public class ScannerFragment extends Fragment {

    public Button scannerButton;
    public TextView resultView;
    public ImageView copy;
    public String resultText;
    public ScannerFragment() { }

    public static ScannerFragment newInstance(String param1, String param2) {
        return new ScannerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        scannerButton = (Button) view.findViewById(R.id.open_scanner_button);
        resultView = (TextView) view.findViewById(R.id.scan_result);
        copy = (ImageView) view.findViewById(R.id.copy_button);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { copyToClipboard(); }
        });

        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        return view;
    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    private void copyToClipboard(){
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", resultText);
        clipboard.setPrimaryClip(clip);

        Toast toast = Toast.makeText(getContext(),"Copied to clipboard",Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null)
        {
            resultText = result.getContents();
            resultView.setText(resultText);
        }

    });
}