package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryImplTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealRepository repository;

    @Test
    public void saveNew() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, Month.MARCH, 23, 12, 50),
                "Завтрак с мясом", 1250);
        Meal created = repository.save(newMeal, USER_ID);
        assertThat(repository.getAll(USER_ID)).contains(newMeal);
    }

    @Test
    public void saveExist() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, Month.MARCH, 23, 12, 50),
                "Завтрак с мясом", 1250);
        int id = repository.getAll(USER_ID).get(0).getId();
        newMeal.setId(id);
        Meal created = repository.save(newMeal, USER_ID);
        assertThat(repository.getAll(USER_ID)).contains(newMeal);
    }

    @Test
    public void updateAlien() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, Month.MARCH, 23, 12, 50),
                "Завтрак с мясом", 1250);
        int id = repository.getAll(ADMIN_ID).get(0).getId();
        newMeal.setId(id);
        Meal updated = repository.save(newMeal, USER_ID);
        assertNull(updated);
    }

    @Test
    public void delete() {
        List<Meal> list = repository.getAll(USER_ID);
        List<Integer> ids = new ArrayList<>();
        list.forEach(m -> ids.add(m.getId()));
        ids.forEach(i -> repository.delete(i, USER_ID));
        Meal[] arr = new Meal[ADMIN_MEALS.size()];
        ADMIN_MEALS.toArray(arr);
        assertMatch(repository.getAll(ADMIN_ID), arr);
    }

    @Test
    public void get() {
        int id = repository.getAll(USER_ID).get(0).getId();
        Meal meal = repository.get(id, USER_ID);
        assertThat(repository.getAll(USER_ID)).contains(meal);
    }

    @Test
    public void getAll() {
        Meal[] arr = new Meal[MEALS.size()];
        MEALS.toArray(arr);
        assertMatch(repository.getAll(USER_ID), arr);
    }

    @Test
    public void getBetween() {
        LocalDateTime start = LocalDateTime.of(2015, Month.MAY, 30, 20, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, Month.MAY, 31, 15, 0, 0);
        List<Meal> actual = repository.getBetween(start, end, USER_ID);
        assertMatch(actual, MEALS.get(2), MEALS.get(3), MEALS.get(4));
    }
}