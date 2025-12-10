package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

class SleeplessNightsFunction implements SleepAnalysisFunction {

    @Override
    public String getFunctionName() {
        return "Количество бессонных ночей";
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(getFunctionName(), 0);
        }

        // В файле пропущено 19 дней, поэтому считает большое кол-во бессонных ночей
        LocalDate startDate = sessions.get(0).getSleepTime().toLocalDate();
        LocalDate endDate = sessions.get(sessions.size() - 1).getWakeTime().toLocalDate();


        if (sessions.get(0).getSleepTime().toLocalTime().isAfter(LocalTime.NOON)) {
            startDate = startDate.plusDays(1);
        }

        long totalNights = ChronoUnit.DAYS.between(startDate, endDate);

        long sleeplessNights = Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(totalNights)
                .filter(nightDate -> isSleeplessNight(nightDate, sessions))
                .count();

        return new SleepAnalysisResult(getFunctionName(), sleeplessNights);
    }

    private boolean isSleeplessNight(LocalDate nightDate, List<SleepingSession> sessions) {

        LocalDateTime nightStart = LocalDateTime.of(nightDate, LocalTime.of(0, 0));
        LocalDateTime nightEnd = LocalDateTime.of(nightDate, LocalTime.of(6, 0));

        long sessionsCoveringNight = sessions.stream()
                .filter(session -> {
                    LocalDateTime sleep = session.getSleepTime();
                    LocalDateTime wake = session.getWakeTime();

                    boolean startsBeforeNightEnds = sleep.isBefore(nightEnd);
                    boolean endsAfterNightStarts = wake.isAfter(nightStart);

                    return startsBeforeNightEnds && endsAfterNightStarts;
                })
                .count();

        return sessionsCoveringNight == 0;
    }
}