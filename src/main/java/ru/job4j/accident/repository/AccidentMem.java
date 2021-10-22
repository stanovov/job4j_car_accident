package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
        Accident accident1 = Accident.of(
                "Водитель не заметил на «зебре» пешехода и сбил",
                "Автомобилист не заметил на регулируемом пешеходном переходе идущего 60-летнего мужчину.",
                "Санкт-Петербургское шоссе, в районе дома №54/2",
                findAccidentTypeById(2)
        );
        accident1.addRule(findRuleById(1));
        accident1.addRule(findRuleById(2));
        saveAccident(accident1);
        Accident accident2 = Accident.of(
                "83-летний водитель спровоцировал ДТП с пострадавшим",
                "Водитель автомобиля ВАЗ 2112 спровоцировал ДТП с пострадавшим.",
                "улица Шмидта возле дома №84",
                findAccidentTypeById(1)
        );
        accident2.addRule(findRuleById(3));
        saveAccident(accident2);
        Accident accident3 = Accident.of(
                "Столкнулись «Лада» и Ford: двое пострадали",
                "В ночь на субботу, 9 октября, произошло ДТП.",
                "На Казанском проспекте у дома №44",
                findAccidentTypeById(1)
        );
        accident3.addRule(findRuleById(1));
        saveAccident(accident3);
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

    public Accident findAccidentById(int id) {
        return accidents.get(id);
    }

    public AccidentType findAccidentTypeById(int id) {
        return accidentTypes.get(id);
    }

    public Rule findRuleById(int id) {
        return rules.get(id);
    }

    public Collection<Accident> findAllAccidents() {
        return accidents.values();
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        return accidentTypes.values();
    }

    public Collection<Rule> findAllRules() {
        return rules.values();
    }
}
