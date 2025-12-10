package ru.yandex.practicum.sleeptracker;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;

public class SleepTrackerApp {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final List<SleepAnalysisFunction> ANALYSIS_FUNCTIONS = List.of(
            new TotalSessionFunction(),
            new MinDurationFunction(),
            new MaxDurationFunction(),
            new AverageDurationFunction(),
            new BadQualitySessionFunction(),
            new SleeplessNightsFunction(),
            new ChronotypeFunction()
    );

    private static List<SleepingSession> readSleepLog(String filePath) throws IOException {
        try {
          return   Files.lines(Paths.get(filePath))
                    .filter(line -> !line.trim().isEmpty())
                    .map(SleepTrackerApp::parseSleepSession)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static SleepingSession parseSleepSession(String line) {
        String[] parts = line.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Неверный формат строки: " + line);
        }

        LocalDateTime sleepTime = LocalDateTime.parse(parts[0], DATE_FORMATTER);
        LocalDateTime wakeTime = LocalDateTime.parse(parts[1], DATE_FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2]);

        if (wakeTime.isBefore(sleepTime) || wakeTime.isEqual(sleepTime)) {
            throw new IllegalArgumentException("Время пробуждения должно быть позже времени засыпания: " + line);
        }
        return new SleepingSession(sleepTime, wakeTime, quality);
    }

    public static void main(String[] args) {

        String filePath;
        String currentDir = System.getProperty("user.dir");
        filePath = currentDir + "/src/main/resources/sleep_log.txt";

        try {

            List<SleepingSession> sessions = readSleepLog(filePath);

            if (sessions.isEmpty()) {
                System.out.println("Нет данных для анализа.");
                return;
            }

            System.out.println("\nАнализ сна");
            ANALYSIS_FUNCTIONS.stream()
                    .map(function -> function.apply(sessions))
                    .forEach(result -> System.out.println(result));

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка в формате данных: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}