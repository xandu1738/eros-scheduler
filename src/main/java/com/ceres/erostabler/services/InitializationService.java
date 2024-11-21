package com.ceres.erostabler.services;

import com.ceres.erostabler.dto.Developer;
import com.ceres.erostabler.dto.WeekDay;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InitializationService {
    List<Developer> devs = new ArrayList<>();
    List<WeekDay> weekDays = new ArrayList<>();

    public List<Developer> getDevs() {
        Developer mart = new Developer("Muhimbura Brian Mart", "b.muhimbura@servicecops.com");
        devs.add(mart);

        Developer blessing = new Developer("Linda Kellen", "lindakellen9@gmail.com");
        devs.add(blessing);

        Developer iggy = new Developer("Ignatius Kisekka", "ignatiuskisekka@gmail.com");
        devs.add(iggy);

        Developer praise = new Developer("Praise Aine", "praisekaine04@gmail.com");
        devs.add(praise);

        Developer steven = new Developer("Steven Mpawulo", "stevenmpawulo@gmail.com");
        devs.add(steven);

        Developer owen = new Developer("Owen Azandu", "xandu1738@gmail.com");
        devs.add(owen);

        Developer sam = new Developer("Samuel Kisitu", "kisitusam@gmail.com");
        devs.add(sam);

        Developer ivan = new Developer("Ivan Isiiko", "isiikoivan@gmail.com");
        devs.add(ivan);

        Developer zeresi = new Developer("Biira Zeresi", "biirazeresi555@gmail.com");
        devs.add(zeresi);

        return devs;
    }

    public List<WeekDay> getWeekDays() {
        WeekDay dayOne = new WeekDay(1, "Monday");
        weekDays.add(dayOne);

        WeekDay dayTwo = new WeekDay(2, "Tuesday");
        weekDays.add(dayTwo);

        WeekDay dayThree = new WeekDay(3, "Wednesday");
        weekDays.add(dayThree);

        WeekDay dayFour = new WeekDay(4, "Thursday");
        weekDays.add(dayFour);

        WeekDay dayFive = new WeekDay(5, "Friday");
        weekDays.add(dayFive);

        return weekDays;
    }
}
