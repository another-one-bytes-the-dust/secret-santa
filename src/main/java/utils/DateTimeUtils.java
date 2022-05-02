package utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateTimeUtils {
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm:ss");

    public static final ZoneId MOSCOW_ZONE = ZoneId.of("UTC+3");

    public static final ZonedDateTime GIFT_DATE = ZonedDateTime.parse(
            System.getenv("GIFT_DATE").trim(),
            dateTimeFormatter.withZone(MOSCOW_ZONE).withResolverStyle(ResolverStyle.STRICT));

    public static final ZonedDateTime REVEAL_DATE = ZonedDateTime.parse(
            System.getenv("REVEAL_DATE").trim(),
            dateTimeFormatter.withZone(MOSCOW_ZONE).withResolverStyle(ResolverStyle.STRICT));

    public static boolean isGiftGivingDate() {
        return Instant.now().atZone(MOSCOW_ZONE).compareTo(GIFT_DATE) > 0;
    }

    public static boolean isRevealDate() {
        return Instant.now().atZone(MOSCOW_ZONE).compareTo(REVEAL_DATE) > 0;
    }

    public static boolean isSameDate(Instant i1, Instant i2) {
        ZonedDateTime zdt1 = i1.atZone(MOSCOW_ZONE);
        ZonedDateTime zdt2 = i2.atZone(MOSCOW_ZONE);

        return zdt1.format(dateFormatter).equals(zdt2.format(dateFormatter));
    }

    public static String stringifyInstant(Instant instant) {
        return instant.atZone(MOSCOW_ZONE).format(timeFormatter);
    }

    public static String stringifyDuration(Instant i1, Instant i2) {
        if (i1 == null) return null;

        String from = stringifyInstant(i1);
        String to = i2 == null ? "..." : stringifyInstant(i2);
        return String.format("%s-%s", from, to);
    }

    public static Duration timeBetween(Instant start, Instant finish) {
        return Duration.between(start.atZone(MOSCOW_ZONE).toLocalDateTime(),
                finish.atZone(MOSCOW_ZONE).toLocalDateTime());
    }

    public static String normalizeDuration(Duration duration) {
        long minutes = Math.abs(duration.toMinutes());

        return String.format("%d:%02d", minutes / 60, minutes % 60);
    }
}