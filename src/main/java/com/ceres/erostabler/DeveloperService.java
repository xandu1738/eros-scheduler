package com.ceres.erostabler;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DeveloperService {
    List<Developer> devs = new ArrayList<>();

    public List<Developer> getDevs() {
        Developer blessing = new Developer("Linda Kellen","b.ayebale@servicecops.com");
        devs.add(blessing);

        Developer iggy = new Developer("Ignatius Kisekka","i.kisekka@servicecops.com");
        devs.add(iggy);

        Developer praise = new Developer("Praise Aine","p.ainembabazi@servicecops.com");
        devs.add(praise);

        Developer bidha = new Developer("Bidha Brian","b.bidha@servicecops.com");
        devs.add(bidha);

        Developer steven = new Developer("Steven Mpawulo","s.mpawulo@servicecops.com");
        devs.add(steven);

        Developer owen = new Developer("Owen Azandu","o.azandu@servicecops.com");
        devs.add(owen);

        Developer sam = new Developer("Sam Kisitu","s.kisitu@servicecops.com");
        devs.add(sam);

        Developer ivan = new Developer("Ivan Isiiko","i.isiiko@servicecops.com");
        devs.add(ivan);

        Developer zeresi = new Developer("Biira Zeresi","b.zeresi@servicecops.com");
        devs.add(zeresi);

        return devs;
    }

    public void setDevs(List<Developer> devs) {
        this.devs = devs;
    }
}
