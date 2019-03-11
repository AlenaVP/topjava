package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            /*AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            adminUserController.create(new User(null, "admin", "email@mail.net", "password", Role.ROLE_ADMIN));
            adminUserController.create(new User(null, "userA", "email@mail.com", "password", Role.ROLE_USER));
            adminUserController.create(new User(null, "userB", "emailb@mail.com", "password", Role.ROLE_USER));
            adminUserController.create(new User(null, "userA", "emaila@mail.com", "password", Role.ROLE_USER));
            System.out.println("\nBy e-mail (.com): " + adminUserController.getByMail("email@mail.com") + "\n");
            System.out.println("\nBy e-mail (.net): " + adminUserController.getByMail("email@mail.net") + "\n");
            adminUserController.getAll().forEach(System.out::println);*/
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.now(), "firstMeal", 170));
            mealRestController.create(new Meal(LocalDateTime.now(), "serviceMeal", 350));
            mealRestController.create(new Meal(LocalDateTime.now(), "controllerMeal", 260));
            System.out.println("\ngetAllTo: ");
            mealRestController.getAllTo().forEach(System.out::println);
            System.out.println("\ndate: 2015 may");
            LocalDate statDate = LocalDate.parse("2015-05-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate endDate = LocalDate.parse("2015-05-30", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            mealRestController.getAllTo(statDate, endDate).forEach(System.out::println);
            System.out.println("\ntime: 5:30..15:20");
            LocalTime statTime = LocalTime.parse("05:30", DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime endTime = LocalTime.parse("15:20", DateTimeFormatter.ofPattern("HH:mm"));
            mealRestController.getAllTo(statTime, endTime).forEach(System.out::println);
            System.out.println("\n");
            Meal meal = mealRestController.get(8);
            System.out.println(meal);
            System.out.println("\n");
            Meal updatedMeal = new Meal(LocalDateTime.parse("2019-03-10 19:52",
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "updatedMeal", 800);
            mealRestController.update(updatedMeal, meal.getId());
            System.out.println(mealRestController.get(8));
            System.out.println("\n");
            mealRestController.delete(8);
            try {
                System.out.println(mealRestController.get(8));
            } catch (NotFoundException e) {
                System.err.println(e.getMessage());
            }
            mealRestController.getAllTo().forEach(System.out::println);

        }
    }
}
