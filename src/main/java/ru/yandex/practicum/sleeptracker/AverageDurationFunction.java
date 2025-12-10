package ru.yandex.practicum.sleeptracker;

import java.util.List;

class AverageDurationFunction implements SleepAnalysisFunction {

    @Override
    public String getFunctionName() {
        return "Средняя продолжительность сессии (минут)";
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double avgDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .sorted()
                .average()
                .orElse(0L);
        return new SleepAnalysisResult(getFunctionName(), String.format("%.1f", avgDuration));
    }
}