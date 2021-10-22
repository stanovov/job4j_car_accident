package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {

    private final Map<Integer, Accident> accidents = new HashMap<>();

    private final AtomicInteger ids = new AtomicInteger(1);

    private AccidentMem() {
        init();
    }

    public void init() {
        this.save(
                Accident.of(
                        "Водитель не заметил на «зебре» пешехода и сбил",
                        "Автомобилист не заметил на регулируемом пешеходном переходе идущего 60-летнего мужчину.",
                        "Санкт-Петербургское шоссе, в районе дома №54/2"
                )
        );
        this.save(
                Accident.of(
                        "83-летний водитель спровоцировал ДТП с пострадавшим",
                        "Водитель автомобиля ВАЗ 2112 спровоцировал ДТП с пострадавшим.",
                        "улица Шмидта возле дома №84"
                )
        );
        this.save(
                Accident.of(
                        "Столкнулись «Лада» и Ford: двое пострадали",
                        "В ночь на субботу, 9 октября, произошло ДТП.",
                        "На Казанском проспекте у дома №44"
                )
        );
    }

    public void save(Accident accident) {
        if (accident.getId() == 0) {
            accident.setId(ids.incrementAndGet());
        }
        accidents.put(accident.getId(), accident);
    }

    public Accident findById(int id) {
        return accidents.get(id);
    }

    public Collection<Accident> findAll() {
        return accidents.values();
    }
}
