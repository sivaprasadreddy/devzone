package com.sivalabs.devzone.domain.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeUtils {

    public static final long ONE_SECOND = 1000;
    public static final long SECONDS = 60;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long MINUTES = 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long HOURS = 24;
    public static final long ONE_DAY = ONE_HOUR * 24;

    public static void main(String[] args) {
        log.info(String.valueOf((5 * ONE_SECOND + 123) % 1000));
        log.info(millisToLongDHMS(123));
        log.info(millisToLongDHMS(5 * ONE_SECOND + 123));
        log.info(millisToLongDHMS(ONE_DAY + ONE_HOUR));
        log.info(millisToLongDHMS(ONE_DAY + 2 * ONE_SECOND));
        log.info(millisToLongDHMS(ONE_DAY + ONE_HOUR + (2 * ONE_MINUTE)));
        log.info(millisToLongDHMS(4 * ONE_DAY + 3 * ONE_HOUR + 2 * ONE_MINUTE + ONE_SECOND));
        log.info(millisToLongDHMS(5 * ONE_DAY + 4 * ONE_HOUR + ONE_MINUTE + 23 * ONE_SECOND + 123));
        log.info(millisToLongDHMS(42 * ONE_DAY));
    }

    /**
     * Converts time (in milliseconds) to human-readable format "w days, x hours, y minutes and z seconds."
     */
    public static String millisToLongDHMS(long durationVal) {
        long duration = durationVal;
        StringBuilder res = new StringBuilder();
        long temp;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append(temp).append(" day").append(temp > 1 ? "s" : "").append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append(temp).append(" hour").append(temp > 1 ? "s" : "").append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
            }

            temp = duration / ONE_SECOND;
            long millis = duration % ONE_SECOND;

            int minMillis = 1;
            if (res.length() > 0 && duration >= ONE_SECOND) {
                if (millis < minMillis) {
                    res.append(" and ");
                } else {
                    res.append(", ");
                }
            }

            if (temp > 0) {
                res.append(temp).append(" second").append(temp > 1 ? "s" : "");
            }
            if (millis > 0) {
                res.append(" and " + millis + " millis");
            }
            return res.toString();
        } else {
            return duration + " millis";
        }
    }

}
