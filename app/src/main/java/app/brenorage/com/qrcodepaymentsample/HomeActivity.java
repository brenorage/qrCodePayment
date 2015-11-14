package app.brenorage.com.qrcodepaymentsample;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button scanCode;
    public String contents;
    public String format;
    public AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadComponents();
        loadListeners();
    }

    public void loadComponents() {
        scanCode = (Button) findViewById(R.id.buttonScan);
        dialog = new AlertDialog.Builder(this);
    }

    public void loadListeners() {
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                }
                catch (Exception e) {
                    String exception = e.toString();
                    if (exception.contains("ActivityNotFound")) {
                        buildDialogAppNotFound();
                    }
                }
            }
        });
    }

    public void buildDialogAppNotFound() {
        dialog.setTitle("Atenção");
        dialog.setMessage("Por Favor, instale o aplicativo Barcode Scanner!");
        dialog.setPositiveButton("Instalar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openStore();
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void openStore() {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.zxing.client.android&hl=pt_BR");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                contents = intent.getStringExtra("SCAN_RESULT");
                format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                showAnotherActivity(contents);

            } else if (resultCode == RESULT_CANCELED) {
                dialog.setTitle("Atenção");
                dialog.setMessage("Não foi possivel ler o QR Code");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Log.i("App", "Scan unsuccessful");
            }
        }
    }

    public void showAnotherActivity(String qrCodeText) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(Constants.tagQrCodeIntent, qrCodeText);
        startActivity(intent);
    }
}