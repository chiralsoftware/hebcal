package com.chiralsoftware.hebcal;

import static com.chiralsoftware.hebcal.HebrewCalendar.holidays;
import static com.chiralsoftware.hebcal.HebrewCalendar.toHebLocalDate;
import static com.chiralsoftware.hebcal.HebrewCalendar.toLocalDate;
import static java.lang.System.out;
import java.time.LocalDate;
import java.time.Month;
import static java.time.Month.JANUARY;
import static java.time.Month.OCTOBER;
import static java.time.Month.SEPTEMBER;
import java.util.SortedSet;

/**
 * Some simple tests and examples showing the calendar in use
 */
public class Test {
    public static void main(String[] args) {
        out.println("Testing!!");
        LocalDate ld = LocalDate.now();
        out.println("local date is: " + ld);
        HebLocalDate hld = toHebLocalDate(ld);
        out.println("and as hebrew date: " + hld);
        ld = LocalDate.of(2022, Month.MARCH, 23);
        out.println("testing Adar II");
        out.println(ld + " is: " + toHebLocalDate(ld));
        out.println();

        out.println("Let's test some holidays!!");
        for (JewishHoliday jh : JewishHoliday.values()) {
            for (int i = 5782; i < 5785; i++) {
                final HebLocalDate hebLocalDate = jh.with(i);
                out.println("Jewish holiday: " + jh + " in: " + i + " is at Jewish date: "
                        + hebLocalDate + ", which is: " + toLocalDate(hebLocalDate));
            }
            out.println();
            out.println("testing holiday dates");
            if(toHebLocalDate(RoshHashana_Local_2022).equals(RoshHashana_Heb_5783)) 
                out.println("passed: " + RoshHashana_Local_2022);
            else
                out.println("failed: " + RoshHashana_Local_2022);

            if(toHebLocalDate(RoshHashana_Local_2024).equals(RoshHashana_Heb_5785)) 
                out.println("passed: " + RoshHashana_Local_2024);
            else
                out.println("failed: " + RoshHashana_Local_2024);
            
            if(toHebLocalDate(RoshHashana_Local_2031).equals(RoshHashana_Heb_5792))
                out.println("passed: " + RoshHashana_Local_2031);
            else
                out.println("failed: " + RoshHashana_Local_2031);

            if(toLocalDate(RoshHashana_Heb_5783).equals(RoshHashana_Local_2022))
                out.println("passed: " + RoshHashana_Heb_5783);
            else
                out.println("failed: " + RoshHashana_Heb_5783);
            
            if(toLocalDate(RoshHashana_Heb_5785).equals(RoshHashana_Local_2024))
                out.println("passed: " + RoshHashana_Heb_5785);
            else
                out.println("failed: " + RoshHashana_Heb_5785);
            
            if(toLocalDate(RoshHashana_Heb_5792).equals(RoshHashana_Local_2031)) 
                out.println("passed: " + RoshHashana_Heb_5792);
            else
                out.println("failed: " + RoshHashana_Heb_5792);
            
            if(toLocalDate(NewYearEve_2022).equals(NewYearEve_Local_2022))
                out.println("Passed: " + NewYearEve_2022);
            else
                out.println("Failed! " + NewYearEve_2022);
            
            if(toHebLocalDate(NewYearEve_Local_2022).equals(NewYearEve_2022)) 
                out.println("Passed: " + NewYearEve_Local_2022);
            else
                out.println("failed: " + NewYearEve_Local_2022);
            
            out.println();
            final LocalDate now = LocalDate.now();
            final LocalDate startOfLocalYear = now.withDayOfYear(1);
            final LocalDate endOfLocalYear = now.withDayOfYear(now.lengthOfYear());

            out.println("Finding a list of Jewish holidays this Gregorian year, " + startOfLocalYear + 
                    " to " + endOfLocalYear);
            
            final SortedSet<JewishHolidayLocalDate> holidaySet = 
                    holidays(startOfLocalYear, endOfLocalYear);

            for(JewishHolidayLocalDate jewishHolidayLocalDate : holidaySet) {
                out.println(jewishHolidayLocalDate);
            }
            
            // now test a range of dates
            LocalDate testDate = LocalDate.of(2022, JANUARY, 1);
            boolean error = false;
            
            for(int i = 0; i < 5000; i = i + 23) {
                testDate = testDate.plusDays(1);
                final HebLocalDate testHebDate = toHebLocalDate(testDate);
                final LocalDate testBack = toLocalDate(testHebDate);
                if(testBack.equals(testDate)) out.println("converted: " + testDate + " to: " + testHebDate + " and back to: " + testBack);
                else {
                    out.println("error! at day: " + i + ", testDate: " + testDate + " did not convert back");
                    error = true;
                }
            }
            if(error) out.println("test failed!");
            
        }
    }
    
    public static final LocalDate RoshHashana_Local_2022 = LocalDate.of(2022, SEPTEMBER, 26);
    public static final LocalDate RoshHashana_Local_2024 = LocalDate.of(2024, OCTOBER, 3);
    public static final LocalDate RoshHashana_Local_2031 = LocalDate.of(2031, SEPTEMBER, 18);
    
    public static final LocalDate NewYearEve_Local_2022 = LocalDate.of(2022, Month.DECEMBER, 31);
    
    public static final HebLocalDate RoshHashana_Heb_5783 = JewishHoliday.RoshHashana.with(5783); // 2022
    public static final HebLocalDate RoshHashana_Heb_5785 = JewishHoliday.RoshHashana.with(5785); // 2024
    public static final HebLocalDate RoshHashana_Heb_5792 = JewishHoliday.RoshHashana.with(5792); // 2031
    
    public static final HebLocalDate NewYearEve_2022 = new HebLocalDate(7,10,5783);
    
}
