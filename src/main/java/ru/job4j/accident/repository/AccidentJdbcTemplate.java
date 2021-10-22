package ru.job4j.accident.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Repository
public class AccidentJdbcTemplate {
    private final JdbcTemplate jdbc;

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void saveAccident(Accident accident) {
        if (accident.getId() == 0) {
            createAccident(accident);
        } else {
            updateAccident(accident);
        }
    }

    public boolean deleteAccident(Accident accident) {
        deleteAccidentRules(accident);
        final String sql = "DELETE FROM accidents WHERE id = ?";
        final Object[] args = {accident.getId()};
        return jdbc.update(sql, args) > 0;
    }

    public Accident findAccidentById(int id) {
        final String sql = "SELECT "
                         + " a.id AS id,"
                         + " a.name AS name,"
                         + " a.text AS text,"
                         + " a.address AS address,"
                         + " a.accident_type_id AS type_id,"
                         + " t.name AS type_name "
                         + "FROM accidents AS a"
                         + " LEFT JOIN accident_types AS t"
                         + "  ON a.accident_type_id = t.id "
                         + "WHERE a.id = ?";
        final Object[] args = {id};
        Accident accident = jdbc.queryForObject(sql, args,
                (rs, row) -> {
                    Accident rsl = Accident.of(
                            rs.getString("name"),
                            rs.getString("text"),
                            rs.getString("address"),
                            AccidentType.of(
                                    rs.getInt("type_id"),
                                    rs.getString("type_name")
                            )
                    );
                    rsl.setId(rs.getInt("id"));
                    return rsl;
                });
        findAccidentRulesByAccidentId(id)
                .forEach(accident::addRule);
        return accident;
    }

    public Collection<Accident> findAllAccidents() {
        final String sql = "SELECT "
                         + " a.id AS id,"
                         + " a.name AS name,"
                         + " a.text AS text,"
                         + " a.address AS address,"
                         + " a.accident_type_id AS type_id,"
                         + " t.name AS type_name "
                         + "FROM accidents AS a"
                         + " LEFT JOIN accident_types AS t"
                         + "  ON a.accident_type_id = t.id "
                         + "ORDER BY a.id";
        List<Accident> accidents = jdbc.query(sql,
                (rs, row) -> {
                    Accident accident = Accident.of(
                            rs.getString("name"),
                            rs.getString("text"),
                            rs.getString("address"),
                            AccidentType.of(
                                    rs.getInt("type_id"),
                                    rs.getString("type_name")
                            )
                    );
                    accident.setId(rs.getInt("id"));
                    return accident;
                });
        accidents.forEach(accident -> findAccidentRulesByAccidentId(accident.getId())
                        .forEach(accident::addRule));
        return accidents;
    }

    public Collection<AccidentType> findAllAccidentTypes() {
        final String sql = "SELECT * FROM accident_types ORDER BY id";
        return jdbc.query(sql,
                (rs, row) ->
                        AccidentType.of(
                                rs.getInt("id"),
                                rs.getString("name")
                        )
        );
    }

    public Collection<Rule> findAllRules() {
        final String sql = "SELECT * FROM rules ORDER BY id";
        return jdbc.query(sql,
                (rs, row) ->
                        Rule.of(
                                rs.getInt("id"),
                                rs.getString("name")
                        )
        );
    }

    public Collection<Rule> findRulesByIds(int[] ids) {
        if (ids.length == 0) {
            return List.of();
        }
        String sql = "SELECT * FROM rules WHERE id IN (";
        sql += ",?".repeat(ids.length).replaceFirst(",", "") + ")";
        final Object[] args = Arrays.stream(ids).boxed().toArray();
        return jdbc.query(sql, args,
                (rs, row) ->
                    Rule.of(
                            rs.getInt("id"),
                            rs.getString("name")
                    )
        );
    }

    private void createAccident(Accident accident) {
        final String sql = "INSERT INTO accidents(name, text, address, accident_type_id) "
                         + "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement statement
                    = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, accident.getName());
            statement.setString(2, accident.getText());
            statement.setString(3, accident.getAddress());
            statement.setInt(4, accident.getType().getId());
            return statement;
        }, keyHolder);
        accident.setId((int) keyHolder.getKeys().get("id"));
        createAccidentRules(accident);
    }

    private void updateAccident(Accident accident) {
        final String sql = "UPDATE accidents "
                         + "SET name = ?, text = ?, address = ?, accident_type_id = ? "
                         + "WHERE id = ?";
        jdbc.update(
                sql,
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                accident.getId()
        );
        updateAccidentRules(accident);
    }

    private void updateAccidentRules(Accident accident) {
        deleteAccidentRules(accident);
        createAccidentRules(accident);
    }

    private void createAccidentRules(Accident accident) {
        final String sql = "INSERT INTO accidents_rules(accident_id, rule_id) "
                         + "VALUES (?, ?)";
        accident.getRules()
                .forEach(rule -> jdbc.update(sql, accident.getId(), rule.getId()));
    }

    private void deleteAccidentRules(Accident accident) {
        final String sql = "DELETE FROM accidents_rules WHERE accident_id = ?";
        final Object[] args = {accident.getId()};
        jdbc.update(sql, args);
    }

    private Collection<Rule> findAccidentRulesByAccidentId(int id) {
        final String sql = "SELECT "
                         + " r.id AS id, "
                         + " r.name AS name "
                         + "FROM rules AS r"
                         + " JOIN accidents_rules AS ar"
                         + "  ON r.id = ar.rule_id "
                         + "WHERE ar.accident_id = ?";
        final Object[] args = {id};
        return jdbc.query(sql, args,
                (rs, row) -> Rule.of(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
    }
}
