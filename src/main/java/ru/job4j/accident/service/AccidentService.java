package ru.job4j.accident.service;

import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentMem;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class AccidentService {

    private final AccidentMem accidentMem;

    public AccidentService(AccidentMem accidentMem) {
        this.accidentMem = accidentMem;
    }

    public void saveAccident(Accident accident) {
        accidentMem.saveAccident(accident);
    }

    public Accident findAccidentById(int id) {
        return accidentMem.findAccidentById(id);
    }

    public Rule findRuleById(int id) {
        return accidentMem.findRuleById(id);
    }

    public Collection<Accident> findAllAccidents() {
        return accidentMem.findAllAccidents().stream()
                .sorted(Comparator.comparing(Accident::getId))
                .collect(Collectors.toList());
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        return accidentMem.findAllAccidentTypes().stream()
                .sorted(Comparator.comparing(AccidentType::getId))
                .collect(Collectors.toList());
    }

    public Collection<Rule> findAllRules() {
        return accidentMem.findAllRules().stream()
                .sorted(Comparator.comparing(Rule::getId))
                .collect(Collectors.toList());
    }
}
