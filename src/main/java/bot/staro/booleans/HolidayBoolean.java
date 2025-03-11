package bot.staro.booleans;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.function.Predicate;

/**
 * A boolean object that depends on holidays.
 *
 * @author cattyn.
 */
public final class HolidayBoolean {
    @SuppressWarnings("unused")
    public void setValue(boolean value) {
        throw new IllegalCallerException("You don't have the authority to change holidays.");
    }

    public boolean getValue() {
        Calendar calendar = Calendar.getInstance();
        LocalDateTime date = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
        boolean isHoliday = false;
        for (Holidays holiday : Holidays.values()) {
            if (holiday.test(date)) {
                isHoliday = true;
                break;
            }
        }
        return isHoliday;
    }

    @Override
    public String toString() {
        return getValue() ? "Yay holiday" : "Aww no holiday";
    }

    enum Holidays {
        NEW_YEAR(doubled(12, 31, 1, 1)),
        ORTHODOX_CHRISTMAS(simple(1, 7)),
        DEFENDER_OF_THE_FATHERLAND_DAY(simple(2, 23)),
        INTERNATIONAL_WOMEN_DAY(simple(3, 8)),
        LABOR_DAY(simple(5, 1)),
        RUSSIA_DAY(simple(6, 12)),
        NATIONAL_UNITY_DAY(simple(11, 4)),
        CATHOLIC_CHRISTMAS(doubled(12, 24, 12, 25));

        private final Predicate<LocalDateTime> date;

        Holidays(Predicate<LocalDateTime> date) {
            this.date = date;
        }

        public boolean test(LocalDateTime date) {
            return this.date.test(date);
        }

        private static Predicate<LocalDateTime> simple(int month, int day) {
            return date -> date.getMonthValue() == month && date.getDayOfMonth() == day;
        }

        @SuppressWarnings("all")
        private static Predicate<LocalDateTime> doubled(int month1, int day1, int month2, int day2) {
            return date -> simple(month1, day1).test(date)
                    && simple(month2, day2).test(date);
        }

    }

}
