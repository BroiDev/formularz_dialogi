package com.broidev.formularz_dialogi;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private SeekBar sbQ1, sbQ2, sbQ6, sbQ7;
    private TextView tvValueQ1, tvValueQ2, tvValueQ6, tvValueQ7, tvSummary;
    private Spinner spQ3, spQ4;
    private RadioGroup rgQ5;
    private EditText etQ8, etNewsletterEmail;
    private MaterialSwitch swNewsletter;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        setUpSpinners();
        setUpListeners();
        initValues();
    }

    private void bindViews() {
        sbQ1 = findViewById(R.id.sbQ1);
        sbQ2 = findViewById(R.id.sbQ2);
        sbQ6 = findViewById(R.id.sbQ6);
        sbQ7 = findViewById(R.id.sbQ7);

        tvValueQ1 = findViewById(R.id.tvValueQ1);
        tvValueQ2 = findViewById(R.id.tvValueQ2);
        tvValueQ6 = findViewById(R.id.tvValueQ6);
        tvValueQ7 = findViewById(R.id.tvValueQ7);
        tvSummary = findViewById(R.id.tvSummary);

        spQ3 = findViewById(R.id.spQ3);
        spQ4 = findViewById(R.id.spQ4);

        rgQ5 = findViewById(R.id.rgQ5);
        etQ8 = findViewById(R.id.etQ8);
        swNewsletter = findViewById(R.id.swNewsletter);
        etNewsletterEmail = findViewById(R.id.etNewsletterEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void setUpSpinners() {
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.question_3_answers, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQ3.setAdapter(adapter3);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(
                this, R.array.question_4_answers, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQ4.setAdapter(adapter4);
    }

    private void setUpListeners() {
        // Nasłuchiwania do aktualizacji tekstu obok suwaków
        setupSeekBar(sbQ1, tvValueQ1);
        setupSeekBar(sbQ2, tvValueQ2);
        setupSeekBar(sbQ6, tvValueQ6);
        setupSeekBar(sbQ7, tvValueQ7);

        // Odkrywanie pola e-mail w zależności od switcha
        swNewsletter.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etNewsletterEmail.setVisibility(View.VISIBLE);
            } else {
                etNewsletterEmail.setVisibility(View.GONE);
                etNewsletterEmail.setText("");
                etNewsletterEmail.setError(null);
            }
        });

        // Walidacja -> Potwierdzenie dialogiem
        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                showConfirmationDialog();
            }
        });
    }

    // Metoda pomocnicza, żeby nie kopiować anonimowych klas dla każdego suwaka
    private void setupSeekBar(SeekBar seekBar, TextView textView) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Wymuszenie pojawienia się początkowych cyfr (0) na interfejsie po włączeniu apki
    private void initValues() {
        tvValueQ1.setText(String.valueOf(sbQ1.getProgress()));
        tvValueQ2.setText(String.valueOf(sbQ2.getProgress()));
        tvValueQ6.setText(String.valueOf(sbQ6.getProgress()));
        tvValueQ7.setText(String.valueOf(sbQ7.getProgress()));
    }

    private boolean validateForm() {
        // 1. Walidacja Spinnera (czy wybrano cokolwiek poza "Wybierz odpowiedź")
        if (spQ3.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.error_spinner, Toast.LENGTH_SHORT).show();
            return false; // Przerwanie i powrót
        }

        // 2. Walidacja RadioGroup (czy zaznaczono Tak/Nie)
        if (rgQ5.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.error_radio, Toast.LENGTH_SHORT).show();
            return false;
        }

        // 3. Walidacja adresu e-mail (tylko jeżeli switch jest aktywny!)
        if (swNewsletter.isChecked()) {
            String email = etNewsletterEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etNewsletterEmail.setError(getString(R.string.error_required));
                etNewsletterEmail.requestFocus();
                return false;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etNewsletterEmail.setError(getString(R.string.error_email));
                etNewsletterEmail.requestFocus();
                return false;
            }
        }

        return true;
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_title)
                .setMessage(R.string.confirm_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> submitForm())
                .setNegativeButton(R.string.no, null) // 'null' oznacza po prostu zamknięcie okna bez akcji
                .show();
    }

    private void submitForm() {
        tvSummary.setText("Dziękujemy za wypełnienie ankiety! Zgłoszenie zostało zarejestrowane.");

        Snackbar.make(findViewById(R.id.main), R.string.sent, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> tvSummary.setText("")) // "Cofnięcie" w ramach demonstracji czyści tekst
                .show();
    }
}
