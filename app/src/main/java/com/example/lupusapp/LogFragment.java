package com.example.lupusapp;

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

    public LogFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_log, container, false);

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

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        return view;
    }
}