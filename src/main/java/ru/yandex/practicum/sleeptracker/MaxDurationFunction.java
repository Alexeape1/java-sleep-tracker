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
                .sorted()
                .reduce((first, second) -> second)
                .orElse(0L);
        return new SleepAnalysisResult(getFunctionName(), maxDuration);
    }
}