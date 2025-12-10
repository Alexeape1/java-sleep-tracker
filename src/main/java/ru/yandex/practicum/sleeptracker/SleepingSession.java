package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

class SleepingSession {
    private final LocalDateTime sleepTime;
    private final LocalDateTime wakeTime;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime sleepTime, LocalDateTime wakeTime, SleepQuality quality) {
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.quality = quality;
    }

    public LocalDateTime getSleepTime() {
        return sleepTime;
    }

    public LocalDateTime getWakeTime() {
        return wakeTime;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationInMinutes() {
        return Duration.between(sleepTime, wakeTime).toMinutes();
    }

    public boolean isNightSleep() {
        LocalTime sleepLocalTime = sleepTime.toLocalTime();
        LocalTime wakeLocalTime = wakeTime.toLocalTime();

        if (!sleepTime.toLocalDate().equals(wakeTime.toLocalDate())) {
            return true;
        }

        boolean crossesNight = (sleepLocalTime.isBefore(LocalTime.of(6, 0)) ||
                wakeLocalTime.isAfter(LocalTime.of(0, 0)));

        return crossesNight;
    }

    @Override
    public String toString() {
        return String.format("Sleep: %s, Wake: %s, Quality: %s, Duration: %d min",
                sleepTime, wakeTime, quality, getDurationInMinutes());
    }
}