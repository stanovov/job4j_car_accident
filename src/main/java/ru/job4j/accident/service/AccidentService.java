package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentJdbcTemplate;

import java.util.Arrays;
import java.util.Collection;

@Service
public class AccidentService {

    private final AccidentJdbcTemplate accidentStore;

    public AccidentService(AccidentJdbcTemplate accidentStore) {
        this.accidentStore = accidentStore;
    }

    public void saveAccident(Accident accident) {
        accidentStore.saveAccident(accident);
    }

    public Accident findAccidentById(int id) {
        return accidentStore.findAccidentById(id);
    }

    public void initAccidentRules(Accident accident, String[] ruleIds) {
        int[] ids = Arrays.stream(ruleIds)
                .mapToInt(Integer::parseInt)
                .toArray();
        accidentStore.findRulesByIds(ids)
                        .forEach(accident::addRule);
    }

    public Collection<Accident> findAllAccidents() {
        return accidentStore.findAllAccidents();
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        return accidentStore.findAllAccidentTypes();
    }

    public Collection<Rule> findAllRules() {
        return accidentStore.findAllRules();
    }
}
