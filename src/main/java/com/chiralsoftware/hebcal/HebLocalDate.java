package com.chiralsoftware.hebcal;

import java.util.Comparator;
import static java.util.Comparator.comparing;

/** The month is the bibical month, values ranging from 1 (Nisan)
 * to 13 (Adar II). The day ranges from 1 to 31. */
public record HebLocalDate(int day, int monthValue, int year) implements Comparable<HebLocalDate> {
    
    public HebrewMonth month() {
        return HebrewMonth.values()[monthValue + 1];
    }
    
    public static final Comparator<HebLocalDate> comparator =
            comparing(HebLocalDate::year).thenComparing(HebLocalDate::civilMonth).thenComparing(HebLocalDate::day);

    @Override
    public int compareTo(HebLocalDate o) {
        return comparator.compare(this, o);
    }
    
    /** Civil month 1 is Tishrei. Civil months increase over the year, so civil month n + 1 is always after
     civil month n in a given year. */
    public int civilMonth() {
        if(monthValue >= 7) return monthValue - 6;
        return monthValue + 6;
    }
    
    /** @return true if this date is after other , false if this date is before or equal
     @throws NullPointerException if other is null */
    public boolean isAfter(HebLocalDate other) {
        if(other == null) throw new NullPointerException("can't compare to null");
        return comparator.compare(this, other) > 0;
    }
    
    public boolean isBefore(HebLocalDate other) {
        if(other == null) throw new NullPointerException("can't compare to null");
        return comparator.compare(this, other) < 0;
    }
    
}
