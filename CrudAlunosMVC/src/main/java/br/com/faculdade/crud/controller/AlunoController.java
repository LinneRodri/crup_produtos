package br.com.faculdade.crud.controller;

import br.com.faculdade.crud.dao.AlunoDAO;
import br.com.faculdade.crud.model.Aluno;
import br.com.faculdade.crud.view.AlunoView;

import javax.swing.*;
import java.sql.SQLException;

/** Controlador: valida ações da interface e coordena Model, View e DAO. */
public class AlunoController {
    private final AlunoView view;
    private final AlunoDAO dao;

    public AlunoController(AlunoView view, AlunoDAO dao) {
        this.view = view;
        this.dao = dao;
        view.setSalvarAction(e -> salvar());
        view.setExcluirAction(e -> excluir());
        view.setNovoAction(e -> view.limparFormulario());
        view.setAtualizarAction(e -> carregar());
        view.setPesquisarAction(e -> carregar());
        view.setSelecionarAction(this::selecionarLinha);
        carregar();
    }

    private void salvar() {
        try {
            Aluno aluno = view.lerFormulario();
            if (aluno.getId() == null) {
                dao.inserir(aluno);
                view.mostrarInfo("Aluno cadastrado com sucesso!");
            } else {
                dao.atualizar(aluno);
                view.mostrarInfo("Cadastro atualizado com sucesso!");
            }
            view.limparFormulario();
            carregar();
        } catch (IllegalArgumentException ex) {
            view.mostrarAviso(ex.getMessage());
        } catch (SQLException ex) {
            tratarErroBanco(ex);
        }
    }

    private void excluir() {
        Long id = view.getIdSelecionado();
        if (id == null) {
            view.mostrarAviso("Selecione um aluno na tabela para excluir.");
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(view,
                "Tem certeza de que deseja excluir o cadastro selecionado?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (resposta != JOptionPane.YES_OPTION) return;
        try {
            dao.excluir(id);
            view.mostrarInfo("Aluno excluído com sucesso.");
            view.limparFormulario();
            carregar();
        } catch (SQLException ex) {
            tratarErroBanco(ex);
        }
    }

    private void carregar() {
        try {
            view.exibirAlunos(dao.listar(view.getFiltro()));
            view.setStatus("Conectado ao banco • " + view.getQuantidade() + " registro(s)");
        } catch (SQLException ex) {
            view.setStatus("Não foi possível conectar ao banco de dados.");
            tratarErroBanco(ex);
        }
    }

    private void selecionarLinha(int row) {
        if (row >= 0) view.preencherFormulario(view.getAlunoDaLinha(row));
    }

    private void tratarErroBanco(SQLException ex) {
        String mensagem = ex.getMessage() == null ? "Erro desconhecido." : ex.getMessage();
        if ("23505".equals(ex.getSQLState())) {
            mensagem = "Este e-mail já está cadastrado. Informe um e-mail diferente.";
        } else if (ex.getSQLState() != null && ex.getSQLState().startsWith("08")) {
            mensagem = "Falha de conexão. Confira host, usuário, senha e internet no README.md.";
        }
        view.mostrarErro(mensagem);
    }
}
