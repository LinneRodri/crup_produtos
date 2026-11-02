package br.com.faculdade.crud.dao;

import br.com.faculdade.crud.config.Database;
import br.com.faculdade.crud.model.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Camada de acesso a dados: SQL e comunicação com PostgreSQL. */
public class AlunoDAO {
    public List<Aluno> listar(String filtro) throws SQLException {
        String sql = """
            SELECT id, nome, email, curso, semestre
            FROM alunos
            WHERE (? = '' OR nome ILIKE ? OR email ILIKE ? OR curso ILIKE ?)
            ORDER BY id DESC
            """;
        List<Aluno> alunos = new ArrayList<>();
        String termo = filtro == null ? "" : filtro.trim();
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, termo);
            String like = "%" + termo + "%";
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alunos.add(new Aluno(rs.getLong("id"), rs.getString("nome"),
                            rs.getString("email"), rs.getString("curso"),
                            rs.getInt("semestre")));
                }
            }
        }
        return alunos;
    }

    public void inserir(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO alunos (nome, email, curso, semestre) VALUES (?, ?, ?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, aluno);
            ps.executeUpdate();
        }
    }

    public void atualizar(Aluno aluno) throws SQLException {
        String sql = "UPDATE alunos SET nome=?, email=?, curso=?, semestre=? WHERE id=?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            preencher(ps, aluno);
            ps.setLong(5, aluno.getId());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("O aluno selecionado não existe mais no banco.");
            }
        }
    }

    public void excluir(long id) throws SQLException {
        String sql = "DELETE FROM alunos WHERE id=?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("O aluno selecionado não existe mais no banco.");
            }
        }
    }

    private void preencher(PreparedStatement ps, Aluno aluno) throws SQLException {
        ps.setString(1, aluno.getNome());
        ps.setString(2, aluno.getEmail());
        ps.setString(3, aluno.getCurso());
        ps.setInt(4, aluno.getSemestre());
    }
}
