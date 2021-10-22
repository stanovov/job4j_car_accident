package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class AccidentMem {

    private final Map<Integer, Accident> accidents = new HashMap<>();

    private final Map<Integer, AccidentType> accidentTypes = new HashMap<>();

    private final Map<Integer, Rule> rules = new HashMap<>();

    private final AtomicInteger accidentIds = new AtomicInteger(0);

    private final AtomicInteger accidentTypeIds = new AtomicInteger(0);

    private final AtomicInteger ruleIds = new AtomicInteger(0);

    private AccidentMem() {
        init();
    }

    public void init() {
        saveAccidentType(AccidentType.of(0, "Две машины"));
        saveAccidentType(AccidentType.of(0, "Машина и человек"));
        saveAccidentType(AccidentType.of(0, "Машина и велосипед"));
        saveRule(Rule.of(0, "Статья. 1"));
        saveRule(Rule.of(0, "Статья. 2"));
        saveRule(Rule.of(0, "Статья. 3"));
    }

    public void saveAccident(Accident accident) {
        if (accident.getId() == 0) {
            accident.setId(accidentIds.incrementAndGet());
        }
        accidents.put(accident.getId(), accident);
    }

    public void saveAccidentType(AccidentType accidentType) {
        if (accidentType.getId() == 0) {
            accidentType.setId(accidentTypeIds.incrementAndGet());
        }
        accidentTypes.put(accidentType.getId(), accidentType);
    }

    public void saveRule(Rule rule) {
        if (rule.getId() == 0) {
            rule.setId(ruleIds.incrementAndGet());
        }
        rules.put(rule.getId(), rule);
    }

    public boolean deleteAccident(Accident accident) {
        return accidents.remove(accident.getId(), accident);
    }

    public Accident findAccidentById(int id) {
        return accidents.get(id);
    }

    public Collection<Accident> findAllAccidents() {
        return accidents.values().stream()
                .sorted(Comparator.comparing(Accident::getId))
                .collect(Collectors.toList());
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        return accidentTypes.values().stream()
                .sorted(Comparator.comparing(AccidentType::getId))
                .collect(Collectors.toList());
    }

    public Collection<Rule> findAllRules() {
        return rules.values().stream()
                .sorted(Comparator.comparing(Rule::getId))
                .collect(Collectors.toList());
    }

    public Collection<Rule> findRulesByIds(int[] ids) {
        return Arrays.stream(ids)
                .mapToObj(rules::get)
                .sorted(Comparator.comparing(Rule::getId))
                .collect(Collectors.toList());
    }
}
