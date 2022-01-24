package com.chiralsoftware.hebcal;

import java.time.LocalDate;
import static java.util.Collections.emptySortedSet;
import static java.util.Collections.unmodifiableSortedSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 */
public final class HebrewCalendar {

    private HebrewCalendar() {
        throw new RuntimeException("don't instantiate");
    }

    /**
     * Find the Jewish holidays between the start and end. If start is after end
     * or equals end, return an empty set. The results are inclusive of the
     * start and end days. If a holiday falls on the start or end LocalDate it
     * is included in the result, so to search for an entire year search from
     * Jan 1 to Dec 31.
     *
     * @return an unmodifiable SortedSet
     */
    public static final SortedSet<JewishHolidayLocalDate> holidays(LocalDate start, LocalDate end) {
        if (start.isAfter(end) || start.equals(end))
            return emptySortedSet();

        final HebLocalDate hebStart = toHebLocalDate(start);
        final HebLocalDate hebEnd = toHebLocalDate(end);

        // easy calculation: search one year before and after and then filter
        final int startHebYear = hebStart.year() - 1;
        final int endHebYear = hebEnd.year() + 1;
        final SortedSet<JewishHolidayLocalDate> holidaySet = new TreeSet<>();
        for (int year = startHebYear; year <= endHebYear; year++) {
            for (JewishHoliday jewishHoliday : JewishHoliday.values()) {
                final HebLocalDate hld = jewishHoliday.with(year);
                if (hld.compareTo(hebStart) >= 0 && hld.compareTo(hebEnd) <= 0)
                    holidaySet.add(new JewishHolidayLocalDate(jewishHoliday, toLocalDate(hld), hld));
            }
        }
        return unmodifiableSortedSet(holidaySet);
    }

    /**
     * convert a LocalDate object to a HebLocalDate
     */
    public static HebLocalDate toHebLocalDate(LocalDate localDate) {
        final int absoluteDay = absoluteFromGregorianDate(localDate);
        final HebLocalDate hld = jewishDateFromAbsolute(absoluteDay);
        return hld;
    }

    public static LocalDate toLocalDate(HebLocalDate hebLocalDate) {
        final int absoluteDay = absoluteFromJewishDate(hebLocalDate);
        final LocalDate localDate = gregorianDateFromAbsolute(absoluteDay);
        return localDate;
    }

    public static int absoluteFromGregorianDate(LocalDate localDate) {
        return (int) localDate.toEpochDay() + 719163; // unix epoch starts 1970
    }

    public static LocalDate gregorianDateFromAbsolute(int absDate) {
        return LocalDate.ofEpochDay(absDate - 719163);
    }

    private static boolean hebrewLeapYear(int year) {
        return (((year * 7) + 1) % 19) < 7;
    }

    public static int getLastMonthOfJewishYear(int year) {
        return hebrewLeapYear(year) ? 13 : 12;
    }

    public static int getLastDayOfJewishMonth(int month, int year) {
        if (month == 2
                || month == 4
                || month == 6
                || month == 10
                || month == 13)
            return 29;
        if ((month == 12) && (!hebrewLeapYear(year)))
            return 29;
        if ((month == 8) && (!longHeshvan(year)))
            return 29;
        if ((month == 9) && (shortKislev(year)))
            return 29;
        return 30;
    }

    private static int hebrewCalendarElapsedDays(int year) {
        /* Months in complete cycles so far */
        int monthsElapsed = 235 * ((year - 1) / 19);

        /* Regular months in this cycle */
        monthsElapsed += 12 * ((year - 1) % 19);

        /* Leap months this cycle */
        monthsElapsed += ((((year - 1) % 19) * 7) + 1) / 19;

        final int partsElapsed = (((monthsElapsed % 1080) * 793) + 204);
        final int hoursElapsed = 5
                + (monthsElapsed * 12)
                + ((monthsElapsed / 1080) * 793)
                + (partsElapsed / 1080);

        /* Conjunction day */
        final int day = 1 + 29 * monthsElapsed + hoursElapsed / 24;

        /* Conjunction parts */
        final int parts = ((hoursElapsed % 24) * 1080)
                + (partsElapsed % 1080);

        int alternativeDay;

        /* If new moon is at or after midday, */
        if ((parts >= 19440)
                || /* ...or is on a Tuesday... */ (((day % 7) == 2)
                && /* at 9 hours, 204 parts or later */ (parts >= 9924)
                && /* of a common year */ (!hebrewLeapYear(year)))
                || /* ...or is on a Monday at... */ (((day % 7) == 1)
                && /* 15 hours, 589 parts or later... */ (parts >= 16789)
                && /* at the end of a leap year */ (hebrewLeapYear(year - 1))))
            /* Then postpone Rosh HaShanah one day */
            alternativeDay = day + 1;
        else
            alternativeDay = day;

        //  If Rosh HaShanah would occur on Sunday, Wednesday, or Friday 
        if (((alternativeDay % 7) == 0)
                || ((alternativeDay % 7) == 3)
                || ((alternativeDay % 7) == 5))
            /* Then postpone it one (more) day and return */
            alternativeDay++;

        return alternativeDay;
    }

    private static int daysInHebrewYear(int year) {
        return (hebrewCalendarElapsedDays(year + 1)
                - hebrewCalendarElapsedDays(year));
    }

    private static boolean longHeshvan(int year) {
        return (daysInHebrewYear(year) % 10) == 5;
    }

    private static boolean shortKislev(int year) {
        return (daysInHebrewYear(year) % 10) == 3;
    }

    public static int absoluteFromJewishDate(HebLocalDate hebLocalDate) {
        /* Days so far this month */
        int returnValue = hebLocalDate.day();

        /* If before Tishri */
        if (hebLocalDate.monthValue() < 7) {
            /* Then add days in prior months this year before and */
             /* after Nisan. */
            for (int m = 7; m <= getLastMonthOfJewishYear(hebLocalDate.year()); m++) {
                returnValue += getLastDayOfJewishMonth(m, hebLocalDate.year());
            }
            for (int m = 1; m < hebLocalDate.monthValue(); m++) {
                returnValue += getLastDayOfJewishMonth(m, hebLocalDate.year());
            }
        } else {
            for (int m = 7; m < hebLocalDate.monthValue(); m++) {
                returnValue += getLastDayOfJewishMonth(m, hebLocalDate.year());
            }
        }

        /* Days in prior years */
        returnValue += hebrewCalendarElapsedDays(hebLocalDate.year());

        /* Days elapsed before absolute date 1 */
        returnValue -= 1373429;

        return returnValue;
    }

    public static HebLocalDate jewishDateFromAbsolute(int absDate) {
        int temp;

        /* Search forward from the approximation */
        int year = (absDate + 1373429) / 366;
        for (;;) {
            temp = absoluteFromJewishDate(new HebLocalDate(1, 7, year + 1));
            if (absDate < temp)
                break;
            year++;
        }

        /* Starting month for search for month */
        temp = absoluteFromJewishDate(new HebLocalDate(1, 1, year));

        /* Search forward from either Tishri or Nisan */
        int m = absDate < temp ? 7 : 1;
        for (;;) {
            temp = absoluteFromJewishDate(new HebLocalDate(getLastDayOfJewishMonth(m, year), m, year));
            if (absDate <= temp)
                break;
            m++;
        }
        final int month = m;

        /* Calculate the day by subtraction */
        temp = absoluteFromJewishDate(new HebLocalDate(1, month, year));
        final int day = absDate - temp + 1;

        return new HebLocalDate(day, month, year);
    }
}
