package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ChronotypeFunction implements SleepAnalysisFunction {

    @Override
    public String getFunctionName() {
        return "Хронотип пользователя";
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(SleepingSession::isNightSleep)
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult(getFunctionName(), "недостаточно данных");
        }

        Map<Chronotype, Long> chronotypeCounts = nightSessions.stream()
                .collect(Collectors.groupingBy(
                        this::determineSessionChronotype,
                        Collectors.counting()
                ));

        Chronotype dominantChronotype = determineDominantChronotype(chronotypeCounts);

        return new SleepAnalysisResult(getFunctionName(), dominantChronotype.toString());
    }

    private Chronotype determineSessionChronotype(SleepingSession session) {
        LocalTime sleepTime = session.getSleepTime().toLocalTime();
        LocalTime wakeTime = session.getWakeTime().toLocalTime();

        if (sleepTime.isAfter(LocalTime.of(23, 0)) &&
                wakeTime.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        }

        if (sleepTime.isBefore(LocalTime.of(22, 0)) &&
                wakeTime.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }
        return Chronotype.DOVE;
    }

    private Chronotype determineDominantChronotype(Map<Chronotype, Long> counts) {
        if (counts.isEmpty()) {
            return Chronotype.DOVE;
        }

        long maxCount = counts.values().stream()
                .max(Long::compare)
                .orElse(0L);

        long countWithMax = counts.values().stream()
                .filter(count -> count == maxCount)
                .count();

        if (countWithMax > 1) {
            return Chronotype.DOVE;
        }

        return counts.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(Chronotype.DOVE);
    }
}
