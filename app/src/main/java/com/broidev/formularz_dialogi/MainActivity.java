package com.broidev.formularz_dialogi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etWaga;
    EditText etWzrost;
    RadioGroup rgPlec;
    Button btnLiczBmi;
    TextView tvWynik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // szukanie widoków
        etWaga = findViewById(R.id.etWaga);
        etWzrost = findViewById(R.id.etWzrost);
        rgPlec = findViewById(R.id.rgPlec);
        btnLiczBmi = findViewById(R.id.btnLiczBmi);
        tvWynik = findViewById(R.id.tvWynik);

        btnLiczBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wagaStr = etWaga.getText().toString();
                String wzrostStr = etWzrost.getText().toString();

                // sprawdzam czy waga nie jest pusta
                if (wagaStr.isEmpty()) {
                    etWaga.setError("Podaj wagę!");
                    return;
                }

                double waga = Double.parseDouble(wagaStr);
                if (waga == 0) {
                    etWaga.setError("Waga nie może być zero!");
                    return;
                }

                // sprawdzam wzrost
                if (wzrostStr.isEmpty()) {
                    etWzrost.setError("Podaj wzrost!");
                    return;
                }

                int wzrost = Integer.parseInt(wzrostStr);
                if (wzrost == 0) {
                    etWzrost.setError("Wzrost nie może wynosić zero!");
                    return;
                }

                if (wzrost > 300) {
                    etWzrost.setError("Wzrost nie może być powyżej 300 cm!");
                    return;
                }

                // sprawdzam czy płeć zaznaczona
                if (rgPlec.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, "Musisz wybrać płeć!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // liczenie bmi
                double wzrostWMietrach = wzrost / 100.0;
                double bmi = waga / (wzrostWMietrach * wzrostWMietrach);

                // ustalam kategorie
                String kategoria = "";
                if (bmi < 18.5) {
                    kategoria = "Niedowaga";
                } else if (bmi >= 18.5 && bmi < 25) {
                    kategoria = "W normie";
                } else if (bmi >= 25 && bmi < 30) {
                    kategoria = "Nadwaga";
                } else {
                    kategoria = "Otyłość";
                }

                // pokazywanie wyniku na ekranie, zaokraglam zeby nie bylo duzo miejsc po przecinku
                double zaokragloneBmi = Math.round(bmi * 100.0) / 100.0;
                tvWynik.setText("Twoje BMI: " + zaokragloneBmi + "\nKategoria: " + kategoria);
            }
        });
    }
}
