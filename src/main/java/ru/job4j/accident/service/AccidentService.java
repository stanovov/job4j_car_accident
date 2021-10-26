package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentRepository;
import ru.job4j.accident.repository.AccidentTypeRepository;
import ru.job4j.accident.repository.RuleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class AccidentService {

    private final AccidentRepository accidentStore;

    private final AccidentTypeRepository accidentTypeStore;

    private final RuleRepository ruleRepository;

    public AccidentService(AccidentRepository accidentStore,
                           AccidentTypeRepository accidentTypeStore,
                           RuleRepository ruleStore) {
        this.accidentStore = accidentStore;
        this.accidentTypeStore = accidentTypeStore;
        this.ruleRepository = ruleStore;
    }

    public void saveAccident(Accident accident) {
        accidentStore.save(accident);
    }

    public Accident findAccidentById(int id) {
        return accidentStore.findById(id).get();
    }

    public void initAccidentRules(Accident accident, String[] ruleIds) {
        Arrays.stream(ruleIds)
                .mapToInt(Integer::parseInt)
                .mapToObj(id -> ruleRepository.findById(id).get())
                .forEach(accident::addRule);
    }

    public Collection<Accident> findAllAccidents() {
        List<Accident> accidents = new ArrayList<>();
        accidentStore.findAll().forEach(accidents::add);
        return accidents;
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        List<AccidentType> accidentTypes = new ArrayList<>();
        accidentTypeStore.findAll().forEach(accidentTypes::add);
        return accidentTypes;
    }

    public Collection<Rule> findAllRules() {
        List<Rule> rules = new ArrayList<>();
        ruleRepository.findAll().forEach(rules::add);
        return rules;
    }
}
