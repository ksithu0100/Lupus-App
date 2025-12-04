package com.example.lupusapp;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LogFragment extends Fragment {

    private SeekBar[] seekBars = new SeekBar[7];
    private TextView[] valueTextViews = new TextView[7];
    private DatabaseHelper dbHelper;

    public LogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log, container, false);

        dbHelper = new DatabaseHelper(getContext());

        FrameLayout linLay1 = view.findViewById(R.id.frameLayout1);
        FrameLayout linLay2 = view.findViewById(R.id.frameLayout2);
        FrameLayout linLay3 = view.findViewById(R.id.frameLayout3);
        FrameLayout linLay4 = view.findViewById(R.id.frameLayout4);
        FrameLayout linLay5 = view.findViewById(R.id.frameLayout5);
        FrameLayout linLay6 = view.findViewById(R.id.frameLayout6);
        FrameLayout linLay7 = view.findViewById(R.id.frameLayout7);

        linLay1.setVisibility(
                PreferencesManager.getSymptom1(requireContext()) ? View.VISIBLE : View.GONE
        );

        linLay2.setVisibility(
                PreferencesManager.getSymptom2(requireContext()) ? View.VISIBLE : View.GONE
        );

        linLay3.setVisibility(
                PreferencesManager.getSymptom3(requireContext()) ? View.VISIBLE : View.GONE
        );
        linLay4.setVisibility(
                PreferencesManager.getSymptom4(requireContext()) ? View.VISIBLE : View.GONE
        );

        linLay5.setVisibility(
                PreferencesManager.getSymptom5(requireContext()) ? View.VISIBLE : View.GONE
        );

        linLay6.setVisibility(
                PreferencesManager.getSymptom6(requireContext()) ? View.VISIBLE : View.GONE
        );
        linLay7.setVisibility(
                PreferencesManager.getSymptom7(requireContext()) ? View.VISIBLE : View.GONE
        );

        int[] seekBarIds = {
                R.id.scale_seek_bar2,
                R.id.scale_seek_bar3,
                R.id.scale_seek_bar4,
                R.id.scale_seek_bar5,
                R.id.scale_seek_bar6,
                R.id.scale_seek_bar7,
                R.id.scale_seek_bar8
        };

        int[] textViewIds = {
                R.id.rating_value_text_view2,
                R.id.rating_value_text_view3,
                R.id.rating_value_text_view4,
                R.id.rating_value_text_view5,
                R.id.rating_value_text_view6,
                R.id.rating_value_text_view7,
                R.id.rating_value_text_view8
        };

        // Initialize arrays and attach listeners
        for (int i = 0; i < 7; i++) {
            seekBars[i] = view.findViewById(seekBarIds[i]);
            valueTextViews[i] = view.findViewById(textViewIds[i]);

            final int index = i;
            seekBars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int actualValue = (progress < 1) ? 1 : progress;
                    valueTextViews[index].setText("Symptom Severity: " + actualValue);
                }

                // pretty much here if we want to add anything for the bars
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        // Handle the submit button
        Button submitButton = view.findViewById(R.id.button);
        submitButton.setOnClickListener(v -> {
            int[] ratings = new int[7];
            for (int i = 0; i < 7; i++) {
                ratings[i] = Math.max(1, seekBars[i].getProgress()); // ensure minimum 1
            }

            // Insert ratings into the database
            boolean success = dbHelper.insertSymptomRatings(
                    ratings[0], ratings[1], ratings[2],
                    ratings[3], ratings[4], ratings[5],
                    ratings[6]
            );

            if (success) {
                Toast.makeText(getContext(), "Symptoms submitted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error submitting symptoms.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}