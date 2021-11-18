package com.example.template.part2.context;

import com.example.template.part1.strategy.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcContext {
    private final DataSource dataSource;

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
}
