package utils;

import org.osbot.rs07.utility.ConditionalSleep;

import java.util.function.BooleanSupplier;

public final class SleepUtils extends ConditionalSleep {

    private final BooleanSupplier condition;

    public SleepUtils(final BooleanSupplier condition, final int timeout) {
        super(timeout);
        this.condition = condition;
    }

    public SleepUtils(final BooleanSupplier condition, final int timeout, final int interval) {
        super(timeout, interval);
        this.condition = condition;
    }

    @Override
    public final boolean condition() throws InterruptedException {
        return condition.getAsBoolean();
    }

    public static boolean sleepUntil(final BooleanSupplier condition, final int timeout) {
        return new SleepUtils(condition, timeout).sleep();
    }

    public static boolean sleepUntil(final BooleanSupplier condition, final int timeout, final int interval) {
        return new SleepUtils(condition, timeout, interval).sleep();
    }
}