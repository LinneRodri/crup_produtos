package br.com.faculdade.crud.model;

public class Aluno {
    private Long id;
    private String nome;
    private String email;
    private String curso;
    private int semestre;

    public Aluno() {}

    public Aluno(Long id, String nome, String email, String curso, int semestre) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.curso = curso;
        this.semestre = semestre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
}
