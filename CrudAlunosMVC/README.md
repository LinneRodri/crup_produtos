# CRUD de Alunos — Java Swing + MVC + PostgreSQL (Neon)

Projeto acadêmico de cadastro de alunos com as quatro operações CRUD:
- **Create / Cadastro:** inserir aluno.
- **Read / Visualização:** listar e pesquisar por nome, e-mail ou curso.
- **Update / Edição:** selecionar um registro, alterar os campos e salvar.
- **Delete / Deleção:** excluir registro selecionado com confirmação.

A interface foi feita em Java Swing, organizada em MVC, e a persistência usa PostgreSQL por JDBC. O projeto pode conectar ao banco Neon mostrado na captura enviada, mas **a conexão real depende de configurar as credenciais válidas do seu projeto**. A senha aparece mascarada na imagem e o host está parcialmente oculto; por segurança, não é possível recuperar esses dados a partir da captura.

## 1. Requisitos
- JDK 17 ou superior
- Maven 3.9+
- Projeto PostgreSQL ativo no Neon
- Internet para conectar ao banco

Verifique:
```bash
java -version
mvn -version
```

## 2. Criar a tabela no Neon
1. Entre no console do Neon e abra **SQL Editor**.
2. Selecione o banco `neondb` e execute todo o arquivo `database/schema.sql`.
3. Na área **Connect**, copie os dados da conexão do seu próprio projeto. Use a conexão PostgreSQL com SSL habilitado.

## 3. Configurar as variáveis de ambiente

O programa não grava senha no código nem no repositório. Configure estas variáveis no terminal **antes** de executar.

### Windows PowerShell
Substitua os valores pelos dados reais da janela Connect do Neon:
```powershell
$env:DB_HOST="SEU_HOST_COMPLETO_DO_NEON"
$env:DB_PORT="5432"
$env:DB_NAME="neondb"
$env:DB_USER="neondb_owner"
$env:DB_PASSWORD="SUA_SENHA_REAL"
```

### Windows CMD
```bat
set DB_HOST=SEU_HOST_COMPLETO_DO_NEON
set DB_PORT=5432
set DB_NAME=neondb
set DB_USER=neondb_owner
set DB_PASSWORD=SUA_SENHA_REAL
```

Também é possível configurar `DATABASE_URL`, `DATABASE_USER` e `DATABASE_PASSWORD`. Nesse caso, `DATABASE_URL` deve estar no formato JDBC:
```text
jdbc:postgresql://SEU_HOST_COMPLETO:5432/neondb?sslmode=require
```
Use o host completo exibido pelo Neon. Se a string fornecida pelo Neon contiver `postgresql://`, troque o prefixo por `jdbc:postgresql://` para usar em `DATABASE_URL`; mantenha usuário e senha nas variáveis separadas.

## 4. Executar
Na pasta do projeto:
```bash
mvn clean compile
mvn exec:java
```
A janela abrirá e tentará carregar os alunos do banco. Se as variáveis não estiverem configuradas ou a tabela não existir, aparecerá uma mensagem de erro explicando o que conferir.

## 5. Como testar o CRUD
1. Preencha nome, e-mail, curso e semestre; clique **Salvar cadastro**.
2. Clique em **Atualizar lista** para consultar todos os registros.
3. Use **Pesquisar** para filtrar por nome, e-mail ou curso.
4. Clique em uma linha para carregar os dados no formulário; edite os campos e clique **Salvar cadastro** para atualizar.
5. Selecione uma linha e clique **Excluir selecionado**; confirme a exclusão.
6. **Limpar / Novo** limpa o formulário para um cadastro novo.

## 6. Arquitetura MVC
```text
src/main/java/br/com/faculdade/crud/
├── Main.java
├── config/
│   └── Database.java       # configuração e conexão JDBC
├── model/
│   └── Aluno.java          # entidade / dados
├── dao/
│   └── AlunoDAO.java       # SQL e persistência
├── view/
│   └── AlunoView.java      # interface Java Swing
└── controller/
    └── AlunoController.java # regras e ações do usuário
```
- **Model:** `Aluno` representa os dados do aluno.
- **View:** `AlunoView` exibe o formulário, a tabela e os botões.
- **Controller:** `AlunoController` recebe as ações, valida o fluxo e chama a camada de dados.
- **DAO:** `AlunoDAO` concentra os comandos SQL e usa `PreparedStatement`, reduzindo riscos de SQL injection.
- **Config:** `Database` abre a conexão usando variáveis de ambiente.

## 7. Enviar ao GitHub
Não consigo publicar no seu GitHub sem acesso/autorização à sua conta. Para enviar o projeto:
1. Crie um repositório vazio no GitHub, por exemplo `crud-alunos-java-mvc`.
2. Abra o terminal nesta pasta e execute:
```bash
git init
git add .
git commit -m "Projeto CRUD de alunos com Java Swing e MVC"
git branch -M main
git remote add origin https://github.com/SEU-USUARIO/crud-alunos-java-mvc.git
git push -u origin main
```
3. Troque `SEU-USUARIO` pelo seu usuário do GitHub e entregue o link do repositório na atividade.

**Importante:** nunca envie senha, URL com senha ou arquivo `.env` ao GitHub. O `.gitignore` já exclui `.env` e a pasta `target`.

## 8. Observações
- A coluna e-mail é única; se repetir, o sistema informa o problema.
- Os dados são persistidos no PostgreSQL, não apenas na memória da aplicação.
- O esquema inclui validações para campos obrigatórios e semestre entre 1 e 20.
- A conexão real não pode ser testada sem host completo e senha válidos.
