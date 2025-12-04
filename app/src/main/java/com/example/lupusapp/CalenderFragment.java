package com.example.lupusapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lupusapp.databinding.FragmentCalenderBinding;
import com.kizitonwose.calendar.core.CalendarDay;
import com.kizitonwose.calendar.core.DayPosition;
import com.kizitonwose.calendar.view.MonthDayBinder;
import com.kizitonwose.calendar.view.ViewContainer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class CalenderFragment extends Fragment {

    private LocalDate selectedDate = null;

    private FragmentCalenderBinding binding;
    private final Set<LocalDate> highlightedDates = new HashSet<>();
    private YearMonth currentMonth;

    private final DateTimeFormatter monthFormatter =
            DateTimeFormatter.ofPattern("MMMM yyyy");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCalenderBinding.inflate(inflater, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            highlightedDates.add(LocalDate.of(2025, 12, 26));
            highlightedDates.add(LocalDate.of(2025, 12, 2));
            highlightedDates.add(LocalDate.of(2025, 10, 15));
        }

        setupCalendar();
        setupMonthNavigation();

        return binding.getRoot();
    }

    private void setupCalendar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentMonth = YearMonth.now();
        }
        YearMonth startMonth = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMonth = currentMonth.minusMonths(12);
        }
        YearMonth endMonth = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            endMonth = currentMonth.plusMonths(12);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.calendarView.setup(startMonth, endMonth, DayOfWeek.SUNDAY);
        }
        binding.calendarView.scrollToMonth(currentMonth);

        // Update month text initially
        binding.textMonthTitle.setText(monthFormatter.format(currentMonth));

        // Detect month scroll changes
        binding.calendarView.setMonthScrollListener(month -> {
            currentMonth = month.getYearMonth();
            binding.textMonthTitle.setText(monthFormatter.format(currentMonth));
            return null;
        });

        binding.calendarView.setDayBinder(new MonthDayBinder<DayViewContainer>() {
            @NonNull
            @Override
            public DayViewContainer create(@NonNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NonNull DayViewContainer container, CalendarDay day) {
                container.day = day;
                TextView textView = container.textView;

                LocalDate date = day.getDate();

                // Set text
                textView.setText(String.valueOf(date.getDayOfMonth()));

                if (day.getPosition() == DayPosition.MonthDate) {
                    textView.setVisibility(View.VISIBLE);

                    // Reset each bind
                    textView.setBackground(null);
                    textView.setTextColor(Color.BLACK);

                    boolean isLoggedDay = highlightedDates.contains(date);
                    boolean isSelected = date.equals(selectedDate);

                    // CASE 1: SELECTED + LOGGED (blue circle over orange square)
                    if (isLoggedDay && isSelected) {
                        textView.setBackground(
                                requireContext().getDrawable(R.drawable.circle_blue)
                        );
                        textView.setTextColor(Color.WHITE);
                    }

                    // CASE 2: LOGGED (orange square)
                    else if (isLoggedDay) {
                        textView.setBackground(
                                requireContext().getDrawable(R.drawable.square_orange)
                        );
                        textView.setTextColor(Color.WHITE);
                    }

                    // CASE 3: SELECTED only (blue circle)
                    else if (isSelected) {
                        textView.setBackground(
                                requireContext().getDrawable(R.drawable.circle_blue)
                        );
                        textView.setTextColor(Color.WHITE);
                    }

                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }

        });
    }

    private void setupMonthNavigation() {

        binding.btnPrevMonth.setOnClickListener(v -> {
            currentMonth = currentMonth.minusMonths(1);
            binding.calendarView.scrollToMonth(currentMonth);
            binding.textMonthTitle.setText(monthFormatter.format(currentMonth));
        });

        binding.btnNextMonth.setOnClickListener(v -> {
            currentMonth = currentMonth.plusMonths(1);
            binding.calendarView.scrollToMonth(currentMonth);
            binding.textMonthTitle.setText(monthFormatter.format(currentMonth));
        });
    }

    class DayViewContainer extends ViewContainer {
        TextView textView;
        CalendarDay day;

        DayViewContainer(View view) {
            super(view);
            textView = view.findViewById(R.id.exOneDayText);

            view.setOnClickListener(v -> {
                if (day != null && day.getPosition() == DayPosition.MonthDate) {
                    selectedDate = day.getDate();
                    binding.editTextDate.setText(selectedDate.toString());
                    binding.calendarView.notifyCalendarChanged();
                }
            });


        }
    }
}
