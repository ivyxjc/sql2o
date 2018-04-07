package org.sql2o;

import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.zapodot.junit.db.EmbeddedDatabaseRule;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.zapodot.junit.db.EmbeddedDatabaseRule.CompatibilityMode.Oracle;

/**
 * @author zapodot
 */
public class QueryArrayTest {

    @Rule
    public EmbeddedDatabaseRule databaseRule = EmbeddedDatabaseRule.builder()
        .withMode(Oracle)
        .withInitialSql(
            "CREATE TABLE FOO(BAR int PRIMARY KEY); INSERT INTO FOO VALUES(1); INSERT INTO FOO VALUES(2)")
        .build();

    @Test
    public void arrayTest() {
        final Sql2o database = new Sql2o(databaseRule.getDataSource());
        try (final Connection connection = database.open();
             final Query query = connection.createQuery("SELECT * FROM FOO WHERE BAR IN (:bars)")) {
            final List<Foo> foos = query.addParameter("bars", 1, 2).executeAndFetch(Foo.class);
            assertThat(foos.size(), equalTo(2));
        }
    }

    @Test
    public void emptyArrayTest() {
        final Sql2o database = new Sql2o(databaseRule.getDataSource());

        try (final Connection connection = database.open();
             final Query query = connection.createQuery("SELECT * FROM FOO WHERE BAR IN (:bars)")) {

            final List<Foo> noFoos =
                query.addParameter("bars", new Integer[] {}).executeAndFetch(Foo.class);
            assertThat(noFoos.size(), equalTo(0));
        }
    }

    private static class Foo {
        public int bar;
    }
}