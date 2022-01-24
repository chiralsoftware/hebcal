package com.chiralsoftware.hebcal;

import java.time.LocalDate;
import java.util.Comparator;
import static java.util.Comparator.comparing;

/**
 * Represent a Jewish holiday at a specific local date
 */
public record JewishHolidayLocalDate(JewishHoliday jewishHoliday, LocalDate localDate, HebLocalDate hebLocalDate) 
        implements Comparable<JewishHolidayLocalDate> {
    
    public static final Comparator<JewishHolidayLocalDate> comparator =
            comparing(JewishHolidayLocalDate::localDate).thenComparing(JewishHolidayLocalDate::jewishHoliday);

    @Override
    public int compareTo(JewishHolidayLocalDate o) {
        return comparator.compare(this, o);
    }
    
}
