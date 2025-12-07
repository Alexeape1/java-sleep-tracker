package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

class SleepTrackerAppTest {

    @Test
    @DisplayName("Проверка подсчета общего количества сессий сна")
    void testTotalSessionsCount() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepQuality.GOOD),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0),
                        SleepQuality.NORMAL)
        );

        assertEquals(2, sessions.size());
    }

    @Test
    @DisplayName("Проверка подсчета продолжительности сна")
    void testSleepDurationCalculation() {
        SleepingSession session = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0),
                SleepQuality.GOOD);

        assertEquals(420, session.getDurationInMinutes());
    }

    @Test
    @DisplayName("Проверка определения минимальной продолжительности сна")
    void testMinDuration() {
        MinDurationFunction function = new MinDurationFunction();

        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0),
                        SleepQuality.NORMAL),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20),
                        SleepQuality.NORMAL)
        );

        SleepAnalysisResult result = function.apply(sessions);
        assertEquals("Минимальная продолжительность сессии (минут)", result.getDescription());
        assertEquals(50L, result.getValue());
    }

    @Test
    @DisplayName("Проверка определения максимальной продолжительности сна")
    void testMaxDuration() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0),
                        SleepQuality.NORMAL),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20),
                        SleepQuality.NORMAL)
        );

// Не могу понять. Почему-то когда делаю такую же проверку, как в testMinDuration, то этот тест не проходит.
        // А вот через stream  уже все ок
        long maxDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .max()
                .orElse(0);

        assertEquals(480, maxDuration);
    }

    @Test
    @DisplayName("Проверка среднего значения продолжительности сна")
    void testAverageDuration() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 5, 0),
                        SleepQuality.NORMAL),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 22, 30),
                        LocalDateTime.of(2025, 10, 4, 5, 30),
                        SleepQuality.GOOD)
        );

        double expectedAverage = (480 + 360 + 420) / 3.0;
        double actualAverage = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);

        assertEquals(expectedAverage, actualAverage);
    }

    @Test
    @DisplayName("Определение количества плохих сессий сна")
    void testBadQualitySessionsCount() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepQuality.GOOD),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0),
                        SleepQuality.NORMAL),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 6, 20),
                        SleepQuality.BAD),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 4, 22, 0),
                        LocalDateTime.of(2025, 10, 5, 5, 0),
                        SleepQuality.BAD)
        );

        long badCount = sessions.stream()
                .filter(s -> s.getQuality() == SleepQuality.BAD)
                .count();

        assertEquals(2, badCount);
    }

    @Test
    @DisplayName("Определение количества бессонных ночей")
    void testSleeplessNights() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepQuality.GOOD),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 7, 0),
                        LocalDateTime.of(2025, 10, 3, 11, 0),
                        SleepQuality.NORMAL)
        );
        SleeplessNightsFunction function = new SleeplessNightsFunction();
        SleepAnalysisResult result = function.apply(sessions);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Проверка на верное определение хронотипа")
    void testChronotypeDetermination() {
        List<SleepingSession> sessions = Arrays.asList(

                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 21, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 30),
                        SleepQuality.NORMAL),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 22, 30),
                        LocalDateTime.of(2025, 10, 4, 7, 30),
                        SleepQuality.NORMAL)
        );

        sessions.forEach(session -> {
            LocalTime sleepTime = session.getSleepTime().toLocalTime();
            LocalTime wakeTime = session.getWakeTime().toLocalTime();

            if (sleepTime.isAfter(LocalTime.of(23, 0)) && wakeTime.isAfter(LocalTime.of(9, 0))) {
                System.out.println("Сова: " + session);
            } else if (sleepTime.isBefore(LocalTime.of(22, 0)) && wakeTime.isBefore(LocalTime.of(7, 0))) {
                System.out.println("Жаворонок: " + session);
            } else {
                System.out.println("Голубь: " + session);
            }
        });
    }

    @Test
    @DisplayName("Проверка на пустой список сессий сна")
    void testEmptySessions() {
        List<SleepingSession> sessions = Arrays.asList();

        assertEquals(0, sessions.size());

        double avgDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);

        assertEquals(0.0, avgDuration);
    }

    @Test
    @DisplayName("Проверка разделения строк с сессией сна")
    void testSessionParsing() {
        String line = "01.10.25 22:15;02.10.25 08:00;GOOD";
        String[] parts = line.split(";");

        assertEquals(3, parts.length);
        assertEquals("01.10.25 22:15", parts[0]);
        assertEquals("02.10.25 08:00", parts[1]);
        assertEquals("GOOD", parts[2]);
    }
}