# FECAP - Fundação de Comércio Álvares Penteado

<p align="center">
<a href= "https://www.fecap.br/"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhZPrRa89Kma0ZZogxm0pi-tCn_TLKeHGVxywp-LXAFGR3B1DPouAJYHgKZGV0XTEf4AE&usqp=CAU" alt="FECAP - Fundação de Comércio Álvares Penteado" border="0"></a>
</p>

# NutriGenda

## NutriGenda - Grupo 5

## Integrantes: <a href="https://www.linkedin.com/in/mateusmonteiroaugusto/">Mateus Monteiro</a>, <a href="https://www.linkedin.com/in/matheus-andrade-mauro-372697253/">Matheus Mauro</a>, <a href="https://www.linkedin.com/in/nathan-camargo-2aaa84212/">Nathan Camargo</a>, <a href="https://www.linkedin.com/in/thiago-martinho-079120278/">Thiago Martinho</a>

## Professores Orientadores: <a href="https://www.linkedin.com/in/victorbarq/">Dr. Victor Von Doom</a>, <a href="https://www.linkedin.com/in/victorbarq/">Me. Saitama</a>, <a href="https://www.linkedin.com/in/victorbarq/">Dr. Strange</a>, <a href="https://www.linkedin.com/in/victorbarq/">Me. Yoda</a>, <a href="https://www.linkedin.com/in/victorbarq/">Dr. Gero</a>

## Descrição

NutriGenda é uma aplicação inovadora destinada a facilitar o gerenciamento de dietas personalizadas por nutricionistas e a adesão dos pacientes a essas dietas. Através de uma interface intuitiva e funcionalidades robustas, o NutriGenda oferece uma plataforma completa para a criação, atualização e monitoramento de planos alimentares.

## Funcionalidades

- **Gerenciamento de Pacientes**: Nutricionistas podem visualizar e gerenciar seus pacientes, acompanhando de perto suas necessidades e progresso.
- **Criação**: Permite a criação de dietas personalizadas com opções para adicionar ou remover refeições e itens alimentares conforme necessário.
- **Acompanhamento de Progresso**: Nutricionistas podem monitorar o progresso dos pacientes e ajustar as dietas de acordo com os resultados observados.
- **Notificações e Lembretes**: Pacientes recebem lembretes de refeições e notificações sobre atualizações na dieta para garantir que seguem o plano alimentar corretamente.
- **Comunicação Fácil**: Facilita a comunicação entre nutricionistas e pacientes, permitindo esclarecimentos e orientações rápidas e eficientes.

NutriGenda utiliza tecnologias modernas para garantir uma experiência de usuário fluida e um gerenciamento de dados seguro e eficiente, tornando-se uma ferramenta indispensável para qualquer nutricionista e seus pacientes.


## 🛠 Estrutura de pastas

-Raiz<br>
|-->documentos<br>
|-->imagens<br>
|-->src<br>
  &emsp;|-->Backend<br>
  &emsp;|-->Frontend<br>
|readme.md<br>

<b>README.MD</b>: Arquivo que serve como guia e explicação geral sobre seu projeto. O mesmo que você está lendo agora.

Há também 4 pastas que seguem da seguinte forma:

<b>documentos</b>: Toda a documentação estará nesta pasta.

<b>executáveis</b>: Binários e executáveis do projeto devem estar nesta pasta.

<b>imagens</b>: Imagens do sistema

<b>src</b>: Pasta que contém o código fonte.

## 🛠 Instalação

<b>Android:</b>

Faça o Download do Nutrigenda na sua playstore.

## 💻 Configuração para Desenvolvimento

Claro, aqui está a seção "Configuração para Desenvolvimento" reformulada:

---

## 💻 Configuração para Desenvolvimento

Descreva como instalar todas as dependências para desenvolvimento e como rodar um test-suite automatizado de algum tipo. Se necessário, faça isso para múltiplas plataformas.

### Ferramentas Necessárias

Para abrir este projeto, você necessita das seguintes ferramentas:

- [Android Studio](https://developer.android.com/studio)
- [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) 11 ou superior
- [MySQL](https://www.mysql.com/downloads/)
- [.NET 5.0](https://dotnet.microsoft.com/download/dotnet/5.0)

### Passos para Configuração

#### Configuração do Backend (API)

1. **Clone o repositório**
   ```bash
   git clone https://github.com/usuario/NutriGenda.git
   cd NutriGenda/backend
   ```

2. **Configure o MySQL**
   - Crie um banco de dados no MySQL.
   - Atualize a string de conexão no arquivo `appsettings.json` com as credenciais do seu banco de dados MySQL.

3. **Instale as dependências**
   ```bash
   dotnet restore
   ```

4. **Execute as migrações do Entity Framework Core**
   ```bash
   dotnet ef migrations add InitialCreate
   dotnet ef database update
   ```

5. **Execute a aplicação**
   ```bash
   dotnet run
   ```

#### Configuração do Frontend (App Android)

1. **Clone o repositório**
   ```bash
   git clone https://github.com/usuario/NutriGenda.git
   cd NutriGenda/app
   ```

2. **Abra o projeto no Android Studio**
   - Abra o Android Studio.
   - Selecione "Open an existing Android Studio project" e navegue até o diretório `NutriGenda/app`.

3. **Instale as dependências**
   - O Android Studio deve automaticamente instalar todas as dependências necessárias ao abrir o projeto. Certifique-se de que o Gradle esteja sincronizado.

4. **Configure o emulador ou dispositivo físico**
   - Configure um emulador Android ou conecte um dispositivo físico via USB para testar a aplicação.

5. **Execute a aplicação**
   - Clique no botão "Run" ou use o atalho `Shift + F10`.

## 🗃 Histórico de lançamentos

A cada atualização os detalhes devem ser lançados aqui.

* 0.9 - 22/05/2024
    * Versão beta do aplicativo

## 📋 Licença/License

Este projeto está licenciado sob a Licença MIT. 

This project is licensed under the MIT License.

## 🎓 Referências

1. **Retrofit**: Biblioteca de cliente HTTP para Android e Java. [Retrofit Documentation](https://square.github.io/retrofit/)
2. **Entity Framework Core**: ORM para .NET. [Entity Framework Core Documentation](https://docs.microsoft.com/ef/core/)
3. **Android Developer**: Documentação oficial do Android. [Android Developer Documentation](https://developer.android.com/docs)
4. **MySQL**: Sistema de gerenciamento de banco de dados relacional. [MySQL Documentation](https://dev.mysql.com/doc/)
5. **ASP.NET Core**: Framework para construção de aplicações web no .NET. [ASP.NET Core Documentation](https://docs.microsoft.com/aspnet/core/)

