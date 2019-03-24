package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final Meal LUNCH = new Meal(null, LocalDateTime.of(2019, Month.MARCH, 23, 15, 20),
            "Обед в McDonalds", 2600);
    public static final Meal DINNER = new Meal(null, LocalDateTime.of(2019, Month.MARCH, 23, 21, 50),
            "Ужин в чебуречной", 1700);
    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0, 0), "Ужин", 510)
    );
    public static final List<Meal> ADMIN_MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 14, 0, 0), "Админ ланч", 510),
            new Meal(LocalDateTime.of(2015, Month.JUNE, 1, 21, 0, 0), "Админ ужин", 1500)
    );

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrder(expected);
    }

}
