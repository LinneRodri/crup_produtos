package br.com.faculdade.crud;

import br.com.faculdade.crud.controller.AlunoController;
import br.com.faculdade.crud.dao.AlunoDAO;
import br.com.faculdade.crud.view.AlunoView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            AlunoDAO dao = new AlunoDAO();
            AlunoView view = new AlunoView();
            new AlunoController(view, dao);
            view.setVisible(true);
        });
    }
}
