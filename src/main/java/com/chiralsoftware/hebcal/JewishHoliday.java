package com.chiralsoftware.hebcal;

import static com.chiralsoftware.hebcal.HebrewMonth.Kislev;
import static com.chiralsoftware.hebcal.HebrewMonth.Nisan;
import static com.chiralsoftware.hebcal.HebrewMonth.Sivan;
import static com.chiralsoftware.hebcal.HebrewMonth.Tishrei;

/**
 * The major Jewish holidays
 */
public enum JewishHoliday {
    
    RoshHashana(Tishrei, (byte)1), 
    YomKippur(Tishrei, (byte)10), 
    Sukkot(Tishrei, (byte)15), 
    SimchatTorah(Tishrei, (byte)22), 
    Chanukah(Kislev, (byte)25), 
    Pesach(Nisan, (byte)15), 
    Shavuot(Sivan, (byte)6);
    
    private JewishHoliday(HebrewMonth month, byte day) {
        this.month = month;
        this.day = day;
    }
    
    private final HebrewMonth month;
    private final byte day; 

    public HebrewMonth getMonth() {
        return month;
    }

    public byte getDay() {
        return day;
    }
    
    /** @param year the Hebrew calendar year (example: 5772) 
     * @return a HebLocalDate of this holiday at the given year */
    public HebLocalDate with(int year) {
        return new HebLocalDate(day, month.getBibicalMonth(), year);
    }
    
}
