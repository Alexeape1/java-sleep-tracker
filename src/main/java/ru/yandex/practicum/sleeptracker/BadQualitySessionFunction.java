package ru.yandex.practicum.sleeptracker;

import java.util.List;

class BadQualitySessionsFunction implements SleepAnalysisFunction {

    @Override
    public String getFunctionName() {
        return "Количество сессий с плохим качеством сна";
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badQualityCount = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult(getFunctionName(), badQualityCount);
    }
}