package br.com.faculdade.crud.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuração segura por variáveis de ambiente.
 * Nunca coloque a senha do banco diretamente no código ou no GitHub.
 */
public final class Database {
    private Database() {}

    public static Connection getConnection() throws SQLException {
        String url = env("DATABASE_URL", "");
        String user = env("DATABASE_USER", "");
        String password = env("DATABASE_PASSWORD", "");

        if (!url.isBlank()) {
            // DATABASE_URL deve ser JDBC, por exemplo:
            // jdbc:postgresql://host/neondb?sslmode=require
            if (!url.startsWith("jdbc:postgresql://")) {
                throw new SQLException("DATABASE_URL precisa começar com jdbc:postgresql://");
            }
            if (user.isBlank() || password.isBlank()) {
                throw new SQLException("Defina também DATABASE_USER e DATABASE_PASSWORD.");
            }
            return DriverManager.getConnection(url, user, password);
        }

        String host = env("DB_HOST", "");
        String port = env("DB_PORT", "5432");
        String database = env("DB_NAME", "neondb");
        user = env("DB_USER", "");
        password = env("DB_PASSWORD", "");

        if (host.isBlank() || user.isBlank() || password.isBlank()) {
            throw new SQLException(
                "Configuração do banco ausente. Consulte README.md e configure as variáveis de ambiente."
            );
        }
        String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database
                + "?sslmode=require";
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null ? fallback : value.trim();
    }
}
