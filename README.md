# hebcal
Modern public domain Java code for Hebrew calendar and Jewish holiday calculation

All classes in this package are immutable and thread-safe. There are no external
dependencies and it is public domain.

This package allows converting Gregorian dates to and from Hebrew calendar dates.

This package also calculates the dates of the major holidays and provides an easy
way to get a list of the dates of the major holidays within any desired date range.

This package is not intended for use with historical dates.

Note that Hebrew dates are from sunset to sunset, whereas Gregorian dates are from
midnignt to midnight. Therefore, Rosh Hashana 2022 wil be marked on calendars as
being September 26, 2022, but it actually begins at local sunset, September 25, 2022.

Jewish holidays may be more than one day. Only the first day is generated. Certain
holidays are celebrated on different days in Israel vs elsewhere, and Purim
is celebrated one day later in Jerusalem than elsewhere. This calendar does not
reflect those issues, but users should take those into consideration if necessary.

Hebrew months can be numbered in a Bibical pattern, Tishrei is month #1,
or they can also have a civil numbering,
in which Nisan is month #1. With Bibical numbering, months 1-6 come
after months 7-13. This package provides both values in a clear way,
and correctly handles isBefore() and isAfter().

The Hebrew calendar has either 12 or 13 months. The 13th month is a leap month used to
keep the lunar calendar synchronized with the solar year.
