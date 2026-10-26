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
    private Button btnSubmit, btnClear;

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
        btnClear = findViewById(R.id.btnClear);
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

        // Czyszczenie formularza -> Potwierdzenie dialogiem
        btnClear.setOnClickListener(v -> showClearConfirmationDialog());
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

    private void showClearConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_confirm_title)
                .setMessage(R.string.clear_confirm_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> clearFormWithUndo())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void clearFormWithUndo() {
        // 1. Zapisujemy aktualny stan formularza do obiektu klasy pomocniczej
        FormState savedState = new FormState(
                sbQ1.getProgress(), sbQ2.getProgress(), sbQ6.getProgress(), sbQ7.getProgress(),
                spQ3.getSelectedItemPosition(), spQ4.getSelectedItemPosition(),
                rgQ5.getCheckedRadioButtonId(),
                etQ8.getText().toString(),
                swNewsletter.isChecked(),
                etNewsletterEmail.getText().toString()
        );

        // 2. Czyścimy formularz zgodnie z wytycznymi
        sbQ1.setProgress(1); // Zmienialiśmy min="1", więc wartością zerową/najniższą jest 1
        sbQ2.setProgress(1);
        sbQ6.setProgress(1);
        sbQ7.setProgress(1);

        spQ3.setSelection(0);
        spQ4.setSelection(0);

        rgQ5.clearCheck();

        etQ8.setText("");
        etQ8.setError(null);

        // Odznaczenie switcha (to automatycznie ukryje email z racji listenera, 
        // ale robimy to też ręcznie, by mieć pewność, że pozbywamy się błędu)
        swNewsletter.setChecked(false);
        etNewsletterEmail.setText("");
        etNewsletterEmail.setError(null);

        tvSummary.setText("");

        // 3. Pokazujemy Snackbar z opcją przywrócenia stanu
        Snackbar.make(findViewById(R.id.main), R.string.cleared, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> restoreFormState(savedState))
                .show();
    }

    private void restoreFormState(FormState state) {
        sbQ1.setProgress(state.q1);
        sbQ2.setProgress(state.q2);
        sbQ6.setProgress(state.q6);
        sbQ7.setProgress(state.q7);

        spQ3.setSelection(state.q3Pos);
        spQ4.setSelection(state.q4Pos);

        if (state.q5CheckedId != -1) {
            rgQ5.check(state.q5CheckedId);
        } else {
            rgQ5.clearCheck();
        }

        etQ8.setText(state.q8Text);

        swNewsletter.setChecked(state.newsletterChecked);
        if (state.newsletterChecked) {
            etNewsletterEmail.setVisibility(View.VISIBLE);
            etNewsletterEmail.setText(state.newsletterEmail);
        }
    }

    // Klasa pomocnicza typu Data Class (w Javie jako zwykły model obiektowy z polami)
    // służąca do przetrzymywania "pamięci" formularza na wypadek cofnięcia.
    private static class FormState {
        int q1, q2, q6, q7;
        int q3Pos, q4Pos;
        int q5CheckedId;
        String q8Text, newsletterEmail;
        boolean newsletterChecked;

        FormState(int q1, int q2, int q6, int q7, int q3Pos, int q4Pos, int q5CheckedId, 
                  String q8Text, boolean newsletterChecked, String newsletterEmail) {
            this.q1 = q1;
            this.q2 = q2;
            this.q6 = q6;
            this.q7 = q7;
            this.q3Pos = q3Pos;
            this.q4Pos = q4Pos;
            this.q5CheckedId = q5CheckedId;
            this.q8Text = q8Text;
            this.newsletterChecked = newsletterChecked;
            this.newsletterEmail = newsletterEmail;
        }
    }
}
