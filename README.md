# Gerenciamento de Medicamentos e Fornecedores

## Descrição
Este projeto é uma aplicação JavaFX que permite gerenciar medicamentos e fornecedores. Os usuários podem adicionar, excluir e visualizar informações sobre medicamentos e fornecedores através de uma interface gráfica intuitiva.

## Instalação
1. **Pré-requisitos**:
    - Java JDK 11 ou superior.
    - JavaFX SDK.

2. **Configuração do Ambiente**:
    - Baixe o JavaFX SDK do [site oficial](https://openjfx.io/).
    - Extraia o SDK em um diretório de sua escolha.

3. **Compilação e Execução**:
    - Navegue até o diretório do projeto.
    - Compile o projeto e execute.

## Uso
- Para adicionar um medicamento, clique em Novo,preencha os campos no formulário e clique em "Salvar", lembre-se de cadastrar o Fornecedor, caso ele nao exista em nosso arquivos.
- Para excluir um medicamento, insira o código do medicamento no campo apropriado ou clique nele na tabela e em seguida clique em "Excluir".
- Para adicionar um fornecedor, clique em Novo, preencha os campos correspondentes e clique em "Salvar".
- Para excluir um fornecedor, insira o CNPJ no campo correspondente e clique em "Excluir".

## Funcionalidades
- Adição, edição e exclusão de medicamentos.
- Adição, edição e exclusão de fornecedores.
- Visualização de medicamentos e fornecedores em uma tabela.
- Filtragem de medicamentos por controle e estoque.
- Filtragem de medicamentos por tipo (Controlado ou nao).
- Filtragem de medicamento com estoque inferior a 5 unidades.
- Filtragem de medicamentos por Data de validade inferior a 30 dias.
- Cálculo do valor total do estoque por fornecedor.
- Persistência de dados em arquivos CSV.

