package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExcess(service.getAll(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAll(LocalDate startDate, LocalDate endDate) {
        log.info("getAll filtered by date");
        if (startDate == null || endDate == null) {
            return new ArrayList<>();
        }
        return MealsUtil.getFilteredWithExcess(service.getAll(), SecurityUtil.authUserCaloriesPerDay(),
                meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate));
    }

    public List<MealTo> getAll(LocalTime startTime, LocalTime endTime) {
        log.info("getAll filtered by time");
        if (startTime == null || endTime == null) {
            return new ArrayList<>();
        }
        return MealsUtil.getFilteredWithExcess(service.getAll(), SecurityUtil.authUserCaloriesPerDay(),
                meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime));
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal);
    }
}