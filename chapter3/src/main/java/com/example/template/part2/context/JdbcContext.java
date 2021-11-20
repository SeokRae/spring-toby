package com.example.template.part2.context;

import com.example.template.part1.strategy.StatementStrategy;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Setter
@NoArgsConstructor
public class JdbcContext {

    private DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement s = strategy.makePreparedStatement(c);
        ) {
            s.executeUpdate();
        }
    }

    public void executeSql(final String query) throws SQLException {
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }
}
