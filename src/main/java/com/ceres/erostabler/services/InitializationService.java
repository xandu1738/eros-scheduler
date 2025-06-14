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

        Developer gogo = new Developer("Ayesiga Gorret", "g.ayesiga@servicecops.com");
        devs.add(gogo);

        Developer jm = new Developer("John Mark", "johnmark@servicecops.com");
        devs.add(jm);

        Developer blessing = new Developer("Linda Kellen", "lindakellen9@gmail.com");
        devs.add(blessing);

        Developer iggy = new Developer("Ignatius Kisekka", "ignatiuskisekka@gmail.com");
        devs.add(iggy);

        Developer praise = new Developer("Praise Ainembabazi", "praisekaine04@gmail.com");
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

        Developer bogere = new Developer("Bogere Moses", "b.bogere@servicecops.com");
        devs.add(bogere);

        Developer samNtunarabo = new Developer("Samuel Ntunarabo", "s.nturanabo@servicecops.com");
        devs.add(samNtunarabo);

        Developer sham = new Developer("Nakayiza Shamim", "nakayiza2shamim@gmail.com");
        devs.add(sham);

        Developer probuse = new Developer("Nimukama Probuse", "etwin.himself@gmail.com");
        devs.add(probuse);

        Developer jet = new Developer("Tumusiime Ezra Jr", "ezrajet9@gmail.com");
        devs.add(jet);

        Developer favour = new Developer("Kiiza Favour", "f.kiiza@servicecops.com");
        devs.add(favour);

        Developer bidhinski = new Developer("Bidha Brian", "b.bidha@servicecops.com");
        devs.add(bidhinski);

        Developer akora = new Developer("Akora Solomon", "0701901774aka@gmail.com");
        devs.add(akora);

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
