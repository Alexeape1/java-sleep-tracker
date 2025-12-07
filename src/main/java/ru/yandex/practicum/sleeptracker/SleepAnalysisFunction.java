package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

interface SleepAnalysisFunction extends Function<List<SleepingSession>, SleepAnalysisResult> {
    String getFunctionName();
}