package com.ceres.erostabler.services;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuotesService {
    private static final String RANDOM_SINGLE_QUOTE_ENDPOINT = "https://zenquotes.io/api/random";
    private final RestTemplate restTemplate;

    public List<?> getRandomQuote() {
        RestClient restClient = RestClient.builder()
                .baseUrl(RANDOM_SINGLE_QUOTE_ENDPOINT)
                .build();

        Object body = restClient.get()
                .uri(RANDOM_SINGLE_QUOTE_ENDPOINT)
                .retrieve().body(Object.class);

        List<?> list = new ArrayList<>();

        if (body != null && body.getClass().isArray()) {
             var ls  = List.of(body);

             list = ls.stream()
                     .flatMap(o -> JSON.parseArray(o.toString()).stream())
                     .toList();

        } else if (body instanceof Collection) {
            list = new ArrayList<>((Collection<?>) body);
        }

        System.out.println(body);
        return list;
    }

    public JSONObject randomAnimeQuote() {
        List<JSONObject> quotes = animeQuotes();
        Random random = new Random();
        int randomIndex = random.nextInt(quotes.size());
        return quotes.get(randomIndex);
    }
    public JSONObject randomAncientQuote() {
        List<JSONObject> quotes = getAncientQuotes();
        Random random = new Random();
        int randomIndex = random.nextInt(quotes.size());
        return quotes.get(randomIndex);
    }

    public List<JSONObject> animeQuotes() {
        List<JSONObject> quotes = new ArrayList<>();

        quotes.add(new JSONObject().fluentPut("quote", "A lesson without pain is meaningless. That's because you can't gain something without sacrificing something else.").fluentPut("author", "Edward Elric (Fullmetal Alchemist: Brotherhood)"));
        quotes.add(new JSONObject().fluentPut("quote", "Fear is not evil. It tells you what your weakness is. And once you know your weakness, you can become stronger.").fluentPut("author", "Gildarts Clive (Fairy Tail)"));
        quotes.add(new JSONObject().fluentPut("quote", "A person grows up when he's able to overcome hardships.").fluentPut("author", "Jiraiya (Naruto)"));
        quotes.add(new JSONObject().fluentPut("quote", "Hard work is worthless for those that don’t believe in themselves.").fluentPut("author", "Naruto Uzumaki (Naruto)"));
        quotes.add(new JSONObject().fluentPut("quote", "A person becomes strong when they have someone they want to protect.").fluentPut("author", "Haku (Naruto)"));
        quotes.add(new JSONObject().fluentPut("quote", "The world’s not perfect. But it’s there for us, doing the best it can... that’s what makes it so damn beautiful.").fluentPut("author", "Roy Mustang (Fullmetal Alchemist: Brotherhood)"));
        quotes.add(new JSONObject().fluentPut("quote", "It's not about going faster. It's about never stopping.").fluentPut("author", "Saitama (One Punch Man)"));
        quotes.add(new JSONObject().fluentPut("quote", "Power comes in response to a need, not a desire. You have to create that need.").fluentPut("author", "Goku (Dragon Ball Z)"));
        quotes.add(new JSONObject().fluentPut("quote", "The only thing we're allowed to do is to believe that we won't regret the choice we made.").fluentPut("author", "Levi Ackerman (Attack on Titan)"));
        quotes.add(new JSONObject().fluentPut("quote", "A flower does not think of competing with the flower next to it. It just blooms.").fluentPut("author", "Zenitsu Agatsuma (Demon Slayer)"));
        quotes.add(new JSONObject().fluentPut("quote", "No matter how deep the night, it always turns to day, eventually.").fluentPut("author", "Brook (One Piece)"));
        quotes.add(new JSONObject().fluentPut("quote", "You should enjoy the little detours. Because that's where you'll find the things more important than what you want.").fluentPut("author", "Ging Freecss (Hunter x Hunter)"));
        quotes.add(new JSONObject().fluentPut("quote", "Forgetting is like a wound. The wound may heal, but it has already left a scar.").fluentPut("author", "Monkey D. Luffy (One Piece)"));
        quotes.add(new JSONObject().fluentPut("quote", "It’s not the face that makes someone a monster; it’s the choices they make with their lives.").fluentPut("author", "Naruto Uzumaki (Naruto)"));
        quotes.add(new JSONObject().fluentPut("quote", "A sword is a weapon. The art of swordsmanship is learning how to protect.").fluentPut("author", "Kenshin Himura (Rurouni Kenshin)"));
        quotes.add(new JSONObject().fluentPut("quote", "Sometimes, we have to look beyond what we want and do what's best.").fluentPut("author", "Piccolo (Dragon Ball Z)"));
        quotes.add(new JSONObject().fluentPut("quote", "No one knows what the future holds. That's why its potential is infinite.").fluentPut("author", "Rintarou Okabe (Steins;Gate)"));
        quotes.add(new JSONObject().fluentPut("quote", "It's not about winning or losing. It's about making the effort.").fluentPut("author", "Seijuro Akashi (Kuroko's Basketball)"));
        quotes.add(new JSONObject().fluentPut("quote", "Even if things are painful and tough, people should appreciate what it means to be alive.").fluentPut("author", "Yato (Noragami)"));
        quotes.add(new JSONObject().fluentPut("quote", "There’s no shame in falling down! True shame is to not stand up again!").fluentPut("author", "Shintarō Midorima (Kuroko's Basketball)"));
        quotes.add(new JSONObject().fluentPut("quote", "You don’t die for your friends, you live for them!").fluentPut("author", "Erza Scarlet (Fairy Tail)"));
        quotes.add(new JSONObject().fluentPut("quote", "The world is not beautiful, therefore it is.").fluentPut("author", "Kino (Kino’s Journey)"));
        quotes.add(new JSONObject().fluentPut("quote", "Life is not a game of luck. If you wanna win, work hard.").fluentPut("author", "Sora (No Game No Life)"));
        quotes.add(new JSONObject().fluentPut("quote", "A strong heart can overcome any obstacle.").fluentPut("author", "Tsubaki Nakatsukasa (Soul Eater)"));
        quotes.add(new JSONObject().fluentPut("quote", "Even if I can't do it now, I'll get stronger and stronger until I can.").fluentPut("author", "Asta (Black Clover)"));
        quotes.add(new JSONObject().fluentPut("quote", "Whatever you lose, you'll find it again. But what you throw away you'll never get back.").fluentPut("author", "Kenshin Himura (Rurouni Kenshin)"));
        quotes.add(new JSONObject().fluentPut("quote", "No matter how unlikely it seems, a path to the future will always open up.").fluentPut("author", "Lelouch Lamperouge (Code Geass)"));
        quotes.add(new JSONObject().fluentPut("quote", "Our lives aren't just measured in years. They're measured by what we do with the time we have.").fluentPut("author", "Makarov Dreyar (Fairy Tail)"));
        quotes.add(new JSONObject().fluentPut("quote", "If you don’t take risks, you can’t create a future.").fluentPut("author", "Monkey D. Luffy (One Piece)"));
        quotes.add(new JSONObject().fluentPut("quote", "A person grows when he dares to face challenges.").fluentPut("author", "Kenshin Himura (Rurouni Kenshin)"));

        return quotes;
    }

    public static List<JSONObject> getAncientQuotes() {
        List<JSONObject> quotes = new ArrayList<>();

        quotes.add(new JSONObject().fluentPut("quote", "The only true wisdom is in knowing you know nothing.").fluentPut("author", "Socrates"));
        quotes.add(new JSONObject().fluentPut("quote", "Knowing yourself is the beginning of all wisdom.").fluentPut("author", "Aristotle"));
        quotes.add(new JSONObject().fluentPut("quote", "It does not matter how slowly you go as long as you do not stop.").fluentPut("author", "Confucius"));
        quotes.add(new JSONObject().fluentPut("quote", "Success is dependent on effort.").fluentPut("author", "Sophocles"));
        quotes.add(new JSONObject().fluentPut("quote", "In the midst of chaos, there is also opportunity.").fluentPut("author", "Sun Tzu"));
        quotes.add(new JSONObject().fluentPut("quote", "The happiness of your life depends upon the quality of your thoughts.").fluentPut("author", "Marcus Aurelius"));
        quotes.add(new JSONObject().fluentPut("quote", "Patience is bitter, but its fruit is sweet.").fluentPut("author", "Jean-Jacques Rousseau"));
        quotes.add(new JSONObject().fluentPut("quote", "It is not what we do, but also what we do not do, for which we are accountable.").fluentPut("author", "Molière"));
        quotes.add(new JSONObject().fluentPut("quote", "We are what we repeatedly do. Excellence, then, is not an act, but a habit.").fluentPut("author", "Aristotle"));
        quotes.add(new JSONObject().fluentPut("quote", "Wisdom begins in wonder.").fluentPut("author", "Socrates"));
        quotes.add(new JSONObject().fluentPut("quote", "He who is not a good servant will not be a good master.").fluentPut("author", "Plato"));
        quotes.add(new JSONObject().fluentPut("quote", "The superior man is modest in his speech but exceeds in his actions.").fluentPut("author", "Confucius"));
        quotes.add(new JSONObject().fluentPut("quote", "If you are irritated by every rub, how will your mirror be polished?").fluentPut("author", "Rumi"));
        quotes.add(new JSONObject().fluentPut("quote", "Luck is what happens when preparation meets opportunity.").fluentPut("author", "Seneca"));
        quotes.add(new JSONObject().fluentPut("quote", "The more you know, the more you realize you don't know.").fluentPut("author", "Aristotle"));
        quotes.add(new JSONObject().fluentPut("quote", "A leader is best when people barely know he exists.").fluentPut("author", "Lao Tzu"));
        quotes.add(new JSONObject().fluentPut("quote", "He who opens a school door, closes a prison.").fluentPut("author", "Victor Hugo"));
        quotes.add(new JSONObject().fluentPut("quote", "Time is a created thing. To say 'I don't have time' is like saying 'I don't want to.'").fluentPut("author", "Lao Tzu"));
        quotes.add(new JSONObject().fluentPut("quote", "Judge a man by his questions rather than his answers.").fluentPut("author", "Voltaire"));
        quotes.add(new JSONObject().fluentPut("quote", "A journey of a thousand miles begins with a single step.").fluentPut("author", "Lao Tzu"));
        quotes.add(new JSONObject().fluentPut("quote", "Quality is not an act, it is a habit.").fluentPut("author", "Aristotle"));
        quotes.add(new JSONObject().fluentPut("quote", "The greater the obstacle, the more glory in overcoming it.").fluentPut("author", "Molière"));
        quotes.add(new JSONObject().fluentPut("quote", "When anger rises, think of the consequences.").fluentPut("author", "Confucius"));
        quotes.add(new JSONObject().fluentPut("quote", "Waste no more time arguing what a good man should be. Be one.").fluentPut("author", "Marcus Aurelius"));
        quotes.add(new JSONObject().fluentPut("quote", "Education is the kindling of a flame, not the filling of a vessel.").fluentPut("author", "Socrates"));
        quotes.add(new JSONObject().fluentPut("quote", "Courage is knowing what not to fear.").fluentPut("author", "Plato"));
        quotes.add(new JSONObject().fluentPut("quote", "The best revenge is to be unlike him who performed the injury.").fluentPut("author", "Marcus Aurelius"));
        quotes.add(new JSONObject().fluentPut("quote", "Knowing others is intelligence; knowing yourself is true wisdom.").fluentPut("author", "Lao Tzu"));
        quotes.add(new JSONObject().fluentPut("quote", "An investment in knowledge pays the best interest.").fluentPut("author", "Benjamin Franklin"));
        quotes.add(new JSONObject().fluentPut("quote", "He who conquers himself is the mightiest warrior.").fluentPut("author", "Confucius"));

        return quotes;
    }
}
