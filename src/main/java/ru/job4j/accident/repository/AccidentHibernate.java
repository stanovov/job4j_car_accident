package ru.job4j.accident.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class AccidentHibernate {

    private static final Logger LOG = LoggerFactory.getLogger(AccidentHibernate.class.getName());

    private final SessionFactory sf;

    public AccidentHibernate(SessionFactory sf) {
        this.sf = sf;
    }

    public void saveAccident(Accident accident) {
        try {
            executeTransaction(session -> {
                if (accident.getId() == 0) {
                    session.save(accident);
                } else {
                    session.update(accident);
                }
                return true;
            });
        } catch (Exception e) {
            LOG.error("Couldn't save accident");
        }
    }

    public boolean deleteAccident(Accident accident) {
        boolean rsl = false;
        try {
            rsl = executeTransaction(session -> {
                session.delete(session.get(Accident.class, accident.getId()));
                return true;
            });
        } catch (Exception e) {
            LOG.error("Couldn't delete accident");
        }
        return rsl;
    }

    public Accident findAccidentById(int id) {
        Accident rsl = null;
        String hql = "SELECT DISTINCT a FROM Accident a "
                    + "LEFT JOIN FETCH a.rules r "
                    + "WHERE a.id = :pId";
        try {
            rsl = executeTransaction(session -> session.createQuery(hql, Accident.class)
                    .setParameter("pId", id)
                    .uniqueResult());
        } catch (Exception e) {
            LOG.error("Couldn't find accident by id");
        }
        return rsl;
    }

    public Collection<Accident> findAllAccidents() {
        List<Accident> rsl = new ArrayList<>();
        String hql = "SELECT DISTINCT a FROM Accident a "
                   + "LEFT JOIN FETCH a.rules r "
                   + "ORDER BY a.id";
        try {
            rsl = executeTransaction(session -> session.createQuery(hql, Accident.class).list());
        } catch (Exception e) {
            LOG.error("Couldn't find accidents");
        }
        return rsl;
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        List<AccidentType> rsl = new ArrayList<>();
        String hql = "SELECT a FROM AccidentType a ORDER BY a.id";
        try {
            rsl = executeTransaction(session -> session.createQuery(hql, AccidentType.class).list());
        } catch (Exception e) {
            LOG.error("Couldn't find accident types");
        }
        return rsl;
    }

    public Collection<Rule> findAllRules() {
        List<Rule> rsl = new ArrayList<>();
        String hql = "SELECT r FROM Rule r ORDER BY r.id";
        try {
            rsl = executeTransaction(session -> session.createQuery(hql, Rule.class).list());
        } catch (Exception e) {
            LOG.error("Couldn't find rules");
        }
        return rsl;
    }

    public Collection<Rule> findRulesByIds(int[] ids) {
        List<Rule> rsl = new ArrayList<>();
        List<Integer> idsList = Arrays.stream(ids).boxed().collect(Collectors.toList());
        String hql = "SELECT r FROM Rule r WHERE r.id IN :ids";
        try {
            rsl = executeTransaction(session -> session.createQuery(hql, Rule.class)
                    .setParameter("ids", idsList)
                    .list());
        } catch (Exception e) {
            LOG.error("Couldn't find rules");
        }
        return rsl;
    }

    private <T> T executeTransaction(Function<Session, T> f) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T result = f.apply(session);
            tx.commit();
            return result;
        } catch (final Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}