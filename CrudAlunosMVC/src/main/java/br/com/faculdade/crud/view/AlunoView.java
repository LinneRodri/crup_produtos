package br.com.faculdade.crud.view;

import br.com.faculdade.crud.model.Aluno;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.IntConsumer;

public class AlunoView extends JFrame {
    private final JTextField idField = new JTextField();
    private final JTextField nomeField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JTextField cursoField = new JTextField();
    private final JSpinner semestreSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
    private final JTextField pesquisaField = new JTextField();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nome", "E-mail", "Curso", "Semestre"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabela = new JTable(tableModel);
    private final JLabel statusLabel = new JLabel("Preparando conexão...");
    private List<Aluno> alunos = List.of();

    private ActionListener salvarAction, excluirAction, novoAction, atualizarAction, pesquisarAction;
    private IntConsumer selecionarAction = row -> {};

    public AlunoView() {
        super("Academia • Sistema de Cadastro de Alunos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(820, 560));
        setLocationRelativeTo(null);
        construirTela();
    }

    private void construirTela() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBorder(new EmptyBorder(18, 22, 12, 22));
        root.setBackground(new Color(245, 247, 250));
        setContentPane(root);

        JLabel titulo = new JLabel("Cadastro de alunos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 25));
        JLabel subtitulo = new JLabel("CRUD completo • Java Swing • MVC • PostgreSQL / Neon");
        subtitulo.setForeground(new Color(95, 105, 120));
        JPanel cabecalho = new JPanel(new GridLayout(2, 1, 0, 4));
        cabecalho.setOpaque(false);
        cabecalho.add(titulo);
        cabecalho.add(subtitulo);
        root.add(cabecalho, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.setOpaque(false);
        root.add(centro, BorderLayout.CENTER);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 235)),
                new EmptyBorder(14, 16, 14, 16)));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 6, 5, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        adicionarCampo(formulario, c, "ID (automático)", idField, 0, 0);
        adicionarCampo(formulario, c, "Nome completo *", nomeField, 1, 0);
        adicionarCampo(formulario, c, "E-mail *", emailField, 2, 0);
        adicionarCampo(formulario, c, "Curso *", cursoField, 0, 2);
        adicionarCampo(formulario, c, "Semestre (1–20) *", semestreSpinner, 1, 2);
        idField.setEditable(false);
        idField.setBackground(new Color(245, 246, 248));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        botoes.setOpaque(false);
        JButton salvar = botao("Salvar cadastro", new Color(20, 135, 100), Color.WHITE);
        JButton novo = botao("Limpar / Novo", new Color(235, 239, 244), new Color(40, 50, 65));
        JButton excluir = botao("Excluir selecionado", new Color(190, 55, 65), Color.WHITE);
        JButton atualizar = botao("Atualizar lista", new Color(235, 239, 244), new Color(40, 50, 65));
        salvar.addActionListener(e -> { if (salvarAction != null) salvarAction.actionPerformed(e); });
        novo.addActionListener(e -> { if (novoAction != null) novoAction.actionPerformed(e); });
        excluir.addActionListener(e -> { if (excluirAction != null) excluirAction.actionPerformed(e); });
        atualizar.addActionListener(e -> { if (atualizarAction != null) atualizarAction.actionPerformed(e); });
        botoes.add(salvar); botoes.add(novo); botoes.add(excluir); botoes.add(atualizar);
        c.gridx = 0; c.gridy = 4; c.gridwidth = 3; c.weightx = 1;
        formulario.add(botoes, c);
        centro.add(formulario, BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new BorderLayout(0, 9));
        listPanel.setOpaque(false);
        JPanel search = new JPanel(new BorderLayout(8, 0));
        search.setOpaque(false);
        JLabel label = new JLabel("Pesquisar:");
        pesquisaField.setToolTipText("Digite nome, e-mail ou curso");
        JButton pesquisar = botao("Buscar", new Color(45, 90, 160), Color.WHITE);
        pesquisar.addActionListener(e -> { if (pesquisarAction != null) pesquisarAction.actionPerformed(e); });
        pesquisaField.addActionListener(e -> { if (pesquisarAction != null) pesquisarAction.actionPerformed(e); });
        search.add(label, BorderLayout.WEST);
        search.add(pesquisaField, BorderLayout.CENTER);
        search.add(pesquisar, BorderLayout.EAST);
        listPanel.add(search, BorderLayout.NORTH);

        tabela.setRowHeight(27);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoCreateRowSorter(true);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabela.setFillsViewportHeight(true);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = tabela.getSelectedRow();
                if (viewRow >= 0) selecionarAction.accept(tabela.convertRowIndexToModel(viewRow));
            }
        });
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 229, 235)));
        listPanel.add(scroll, BorderLayout.CENTER);
        centro.add(listPanel, BorderLayout.CENTER);

        statusLabel.setBorder(new EmptyBorder(5, 2, 0, 2));
        statusLabel.setForeground(new Color(90, 100, 115));
        root.add(statusLabel, BorderLayout.SOUTH);
    }

    private void adicionarCampo(JPanel panel, GridBagConstraints c, String label, JComponent campo,
                                int x, int y) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 5));
        wrapper.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        wrapper.add(l, BorderLayout.NORTH);
        campo.setPreferredSize(new Dimension(180, 30));
        wrapper.add(campo, BorderLayout.CENTER);
        c.gridx = x; c.gridy = y; c.gridwidth = 1; c.weightx = 1;
        panel.add(wrapper, c);
    }

    private JButton botao(String texto, Color fundo, Color textoCor) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setBackground(fundo);
        b.setForeground(textoCor);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMargin(new Insets(9, 13, 9, 13));
        return b;
    }

    public Aluno lerFormulario() {
        String nome = nomeField.getText().trim();
        String email = emailField.getText().trim();
        String curso = cursoField.getText().trim();
        int semestre = (Integer) semestreSpinner.getValue();
        if (nome.isBlank() || email.isBlank() || curso.isBlank())
            throw new IllegalArgumentException("Preencha nome, e-mail e curso.");
        if (nome.length() > 120 || email.length() > 160 || curso.length() > 120)
            throw new IllegalArgumentException("Confira o tamanho dos campos informados.");
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new IllegalArgumentException("Informe um endereço de e-mail válido.");
        Long id = idField.getText().isBlank() ? null : Long.valueOf(idField.getText());
        return new Aluno(id, nome, email, curso, semestre);
    }

    public void exibirAlunos(List<Aluno> lista) {
        alunos = List.copyOf(lista);
        tableModel.setRowCount(0);
        for (Aluno a : alunos)
            tableModel.addRow(new Object[]{a.getId(), a.getNome(), a.getEmail(), a.getCurso(), a.getSemestre()});
    }

    public Aluno getAlunoDaLinha(int row) { return alunos.get(row); }

    public void preencherFormulario(Aluno a) {
        idField.setText(String.valueOf(a.getId()));
        nomeField.setText(a.getNome());
        emailField.setText(a.getEmail());
        cursoField.setText(a.getCurso());
        semestreSpinner.setValue(a.getSemestre());
    }

    public void limparFormulario() {
        tabela.clearSelection();
        idField.setText("");
        nomeField.setText("");
        emailField.setText("");
        cursoField.setText("");
        semestreSpinner.setValue(1);
        nomeField.requestFocusInWindow();
    }

    public Long getIdSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) return null;
        return alunos.get(tabela.convertRowIndexToModel(row)).getId();
    }
    public String getFiltro() { return pesquisaField.getText().trim(); }
    public int getQuantidade() { return tableModel.getRowCount(); }
    public void setStatus(String texto) { statusLabel.setText(texto); }
    public void mostrarInfo(String texto) { JOptionPane.showMessageDialog(this, texto, "Sucesso", JOptionPane.INFORMATION_MESSAGE); }
    public void mostrarAviso(String texto) { JOptionPane.showMessageDialog(this, texto, "Verifique os dados", JOptionPane.WARNING_MESSAGE); }
    public void mostrarErro(String texto) { JOptionPane.showMessageDialog(this, texto, "Erro", JOptionPane.ERROR_MESSAGE); }
    public void setSalvarAction(ActionListener a) { salvarAction = a; }
    public void setExcluirAction(ActionListener a) { excluirAction = a; }
    public void setNovoAction(ActionListener a) { novoAction = a; }
    public void setAtualizarAction(ActionListener a) { atualizarAction = a; }
    public void setPesquisarAction(ActionListener a) { pesquisarAction = a; }
    public void setSelecionarAction(IntConsumer a) { selecionarAction = a; }
}
