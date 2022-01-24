package com.chiralsoftware.hebcal;

/**
 * The months of the Hebrew calendar
 */
public enum HebrewMonth {

    Nisan((byte) 1),
    Iyar((byte) 2), 
    Sivan((byte) 3),
    Tammuz((byte) 4),
    Av((byte) 5),
    Elul((byte) 6),
    Tishrei((byte) 7),
    Cheshvan((byte) 8),
    Kislev((byte) 9),
    Tevet((byte) 10),
    Shvat((byte) 11),
    Adar((byte) 12),
    AdarII((byte) 13);
    
    private final byte bibicalMonth;
    
    private HebrewMonth(byte bibicalMonth) {
        this.bibicalMonth = bibicalMonth;
    }

    public byte getBibicalMonth() {
        return bibicalMonth;
    }
    
}
