import java.time.Duration;
import java.time.Instant;

public class Timer {
    private Instant startTime;
    private Duration workDuration;
    private Duration breakDuration;
    private boolean isWorkSession = true;
    private boolean isRunning = false;

    public Timer(int workMinutes, int breakMinutes) {
        this.workDuration = Duration.ofMinutes(workMinutes);
        this.breakDuration = Duration.ofMinutes(breakMinutes);
    }

    public void start() {
        startTime = Instant.now();
        isRunning = true;
    }

    public void pause() {
        isRunning = false;
    }

    public void reset() {
        isRunning = false;
        isWorkSession = true;
        startTime = null;
    }

    public Duration getRemainingTime() {
        if (!isRunning) return isWorkSession ? workDuration : breakDuration;
        
        Duration elapsed = Duration.between(startTime, Instant.now());
        Duration totalDuration = isWorkSession ? workDuration : breakDuration;
        Duration remaining = totalDuration.minus(elapsed);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    public boolean isSessionOver() {
        if (!isRunning) return false;
        return getRemainingTime().isZero();
    }

    public void switchSession() {
        isWorkSession = !isWorkSession;
        startTime = Instant.now();
    }

    public boolean isWorkSession() {
        return isWorkSession;
    }

    public boolean isRunning() {
        return isRunning;
    }
    
}