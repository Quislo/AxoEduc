# AxoEduc

Aplicativo Android focado na área de educação (projetos FIAP), desenvolvido utilizando tecnologias modernas do ecossistema Android.

## 🚀 Principais Tecnologias e Bibliotecas

- **Linguagem**: Kotlin
- **Interface de Usuário (UI)**: Jetpack Compose (Material Design 3)
- **Navegação**: Navigation Compose
- **Persistência Local**: Room Database
- **Autenticação**: Firebase Auth e Google Sign-In via Credential Manager
- **Imagens e Mídia**: Coil
- **Arquitetura**: MVVM (Model-View-ViewModel)

## ⚙️ Pré-requisitos do Sistema

Para rodar este projeto na sua máquina de desenvolvimento, você precisará de:
- **Android Studio** (versão Ladybug ou superior recomendada, devido ao suporte ao Jetpack Compose e KSP).
- **JDK 11** no mínimo.
- **Android SDK**: Min SDK 28 (Android 9.0) e Target SDK 35.

## 🛠️ Passos para Configuração

### 1. Clonando e Abrindo o Projeto
No Android Studio, acesse a opção **File > Open** e selecione o diretório raiz do repositório `AxoEduc`.

### 2. Configuração das Chaves Locais (Google Sign-In)
O projeto utiliza uma variável de ambiente no arquivo de propriedades local para proteger a chave do Google Sign-In.
- Na raiz do projeto, existe um arquivo de exemplo chamado `local.properties.example`.
- Renomeie esse arquivo (ou crie um novo) com o nome exato de `local.properties`.
- Dentro dele, defina o diretório do seu SDK e a sua chave no formato abaixo:
  ```properties
  sdk.dir=C\:\\Users\\SeuUsuario\\AppData\\Local\\Android\\Sdk
  GOOGLE_WEB_CLIENT_ID=COLOQUE_SEU_WEB_CLIENT_ID_AQUI
  ```

### 3. Configuração do Firebase (`google-services.json`)
O aplicativo utiliza o Firebase Authentication para gerenciar contas de usuários e logins.
- Baixe o arquivo `google-services.json` referente ao projeto direto do console do Firebase. 
- Coloque o arquivo na pasta `app/` (o caminho ficará `app/google-services.json`).

### 4. Sincronização e Instalação
- Aguarde ou force o **Sync do Gradle** clicando no botão de sincronização ("Sync Project with Gradle Files").
- Conecte um celular físico compatível ou inicie um emulador.
- Clique em **Run** (atalho `Shift + F10`) e aguarde o processo de build do projeto.
