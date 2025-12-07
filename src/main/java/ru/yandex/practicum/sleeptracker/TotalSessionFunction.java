package ru.yandex.practicum.sleeptracker;

import java.util.List;

class TotalSessionsFunction implements SleepAnalysisFunction {

    @Override
    public String getFunctionName() {
        return "Общее количество сессий сна";
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long total = sessions.size();
        return new SleepAnalysisResult(getFunctionName(), total);
    }
}