package ru.yandex.practicum.sleeptracker;

import java.util.List;

class MinDurationFunction implements SleepAnalysisFunction {

    @Override
    public String getFunctionName() {
        return "Минимальная продолжительность сессии (минут)";
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        long minDuration = sessions.stream()
                .mapToLong(session -> session.getDurationInMinutes())
                .sorted()
                .findFirst()
                .orElse(0L);
        return new SleepAnalysisResult(getFunctionName(), minDuration);
    }
}