package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void getBetweenDates() {
        LocalDate start = LocalDate.of(2015, 5, 30);
        LocalDate end = LocalDate.of(2015, 5, 30);
        List<Meal> actual = service.getBetweenDates(start, end, USER_ID);
        assertMatch(actual, MEALS.get(0), MEALS.get(1), MEALS.get(2));
    }

    @Test
    public void get() throws Exception {
        int id = service.getAll(USER_ID).get(0).getId();
        Meal meal = service.get(id, USER_ID);
        assertThat(service.getAll(USER_ID)).contains(meal);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getAlien() throws Exception {
        int id = service.getAll(ADMIN_ID).get(0).getId();
        service.get(id, USER_ID);
    }

    @Test
    public void delete() throws Exception {
        List<Meal> list = service.getAll(USER_ID);
        List<Integer> ids = new ArrayList<>();
        list.forEach(m -> ids.add(m.getId()));
        ids.forEach(i -> service.delete(i, USER_ID));
        Meal[] arr = new Meal[ADMIN_MEALS.size()];
        ADMIN_MEALS.toArray(arr);
        assertMatch(service.getAll(ADMIN_ID), arr);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAlien() throws Exception {
        int id = service.getAll(ADMIN_ID).get(0).getId();
        service.delete(id, USER_ID);
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime start = LocalDateTime.of(2015, Month.MAY, 30, 20, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, Month.MAY, 31, 15, 0, 0);
        List<Meal> actual = service.getBetweenDateTimes(start, end, USER_ID);
        assertMatch(actual, MEALS.get(2), MEALS.get(3), MEALS.get(4));
    }

    @Test
    public void getAll() {
        Meal[] arr = new Meal[MEALS.size()];
        MEALS.toArray(arr);
        assertMatch(service.getAll(USER_ID), arr);
    }

    @Test
    public void update() throws Exception {
        int id = service.getAll(ADMIN_ID).get(0).getId();
        Meal updated = service.get(id, ADMIN_ID);
        updated.setDateTime(DINNER.getDateTime());
        updated.setDescription(DINNER.getDescription());
        updated.setCalories(DINNER.getCalories());
        service.update(updated, ADMIN_ID);
        assertThat(service.getAll(ADMIN_ID)).contains(updated);
    }

    @Test(expected = NotFoundException.class)
    public void updatedNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateAlien() throws Exception {
        int id = service.getAll(ADMIN_ID).get(0).getId();
        Meal updated = service.get(id, ADMIN_ID);
        service.update(updated, USER_ID);
    }

//    @Test(expected = DataAccessException.class)
//    public void duplicateMailCreate() throws Exception {
//        service.create(new Meal(null, LocalDateTime.of(2015, Month.MAY, 30, 13, 00, 00),
//                "Обед в столовке", 800), USER_ID);
//    }

    @Test
    public void create() throws SQLException {
        Meal newMeal = new Meal(null, LocalDateTime.of(2019, Month.MARCH, 23, 12, 50),
                "Завтрак с мясом", 1250);
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertThat(service.getAll(USER_ID)).contains(newMeal);
    }
}