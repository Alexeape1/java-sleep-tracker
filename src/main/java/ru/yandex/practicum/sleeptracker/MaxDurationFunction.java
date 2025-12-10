package ru.yandex.practicum.sleeptracker;

import java.util.List;

class MaxDurationFunction implements SleepAnalysisFunction {

    @Override
    public String getFunctionName() {
        return "Максимальная продолжительность сессии (минут)";
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long maxDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .max()
                .orElse(0L);
        return new SleepAnalysisResult(getFunctionName(), maxDuration);
    }
}