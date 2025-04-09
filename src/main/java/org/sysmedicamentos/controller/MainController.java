package org.sysmedicamentos.controller;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sysmedicamentos.model.entity.Fornecedor;
import org.sysmedicamentos.model.entity.Medicamento;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    private List<Medicamento> medicamentoList;
    private List<Fornecedor> fornecedorList;
    private Medicamento medicamento;
    private Fornecedor fornecedor;

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtDescricao;
    @FXML
    private TextField txtPricipioAtivo;
    @FXML
    private TextField txtDataValidade;
    @FXML
    private TextField txtQuantidade;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtTipo;
    @FXML
    private TextField txtCNPJ;
    @FXML
    private TextField txtRazaoSocial;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCidade;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtFornecedor;
    @FXML
    private Label labelResultadoAqui;

    @FXML
    private Button btnMedNovo;
    @FXML
    private Button btnMedSalvar;
    @FXML
    private Button btnMedExcluir;

    @FXML
    private Button btnFornNovo;
    @FXML
    private Button btnFornSalvar;
    @FXML
    private Button btnFornExcluir;

    @FXML
    private Button btnCarregar;
    @FXML
    private Button btnFiltrarControlados;
    @FXML
    private Button btnFiltrarNaoControlados;
    @FXML
    private Button btnProximoVencimento;
    @FXML
    private Button btnEstoqueFornecedor;
    @FXML
    private Button btnEstoqueBaixo;
    @FXML
    private Button btnSalvarArquivo;

    @FXML
    private TableView<Medicamento> tbMedicamento;
    @FXML
    private TableColumn<Medicamento, String> codigoField;
    @FXML
    private TableColumn<Medicamento, String> nomeField;
    @FXML
    private TableColumn<Medicamento, LocalDate> validadeField;
    @FXML
    private TableColumn<Medicamento, BigDecimal> precoField;
    @FXML
    private TableColumn<Medicamento, Integer> estoqueField;
    @FXML
    private TableColumn<Medicamento, Boolean> tipoField;
    @FXML
    private TableColumn<Medicamento, String> fornecedorField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.medicamentoList = new ArrayList<>();
        this.fornecedorList = new ArrayList<>();
        this.medicamento = new Medicamento();
        this.fornecedor = new Fornecedor();

        this.btnFornExcluir.setDisable(true);
        this.btnMedExcluir.setDisable(true);
        this.btnFornSalvar.setDisable(true);
        this.btnMedSalvar.setDisable(true);
        this.btnEstoqueBaixo.setDisable(true);
        this.btnFiltrarControlados.setDisable(true);
        this.btnFiltrarNaoControlados.setDisable(true);
        this.btnEstoqueFornecedor.setDisable(true);
        this.btnProximoVencimento.setDisable(true);

        codigoField.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        nomeField.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        validadeField.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataValidade()));
        precoField.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPreco()));
        estoqueField.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidadeEstoque()).asObject());
        tipoField.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isControlado()).asObject());
        fornecedorField.setCellValueFactory(cellData -> {
            Fornecedor fornecedor = cellData.getValue().getFornecedor();
            return new SimpleStringProperty(fornecedor != null ? fornecedor.getRazaoSocial() : "");
        });

        List<Fornecedor> fornecedoresCarregados = lerDadosDoArquivoFornecedores();
        fornecedorList.addAll(fornecedoresCarregados);

        List<Medicamento> medicamentosCarregados = lerDadosDoArquivoMedicamentos();
        medicamentoList.addAll(medicamentosCarregados);

    }

    @FXML
    public void onBtnMedNovo() {
        this.medicamento = new Medicamento();
        limparFormularioMed();
        this.btnMedSalvar.setDisable(false);
        this.btnMedExcluir.setDisable(false);
    }

    @FXML
    public void onBtnFornNovo() {
        this.fornecedor = new Fornecedor();
        limparFormularioForn();
        this.btnFornSalvar.setDisable(false);
        this.btnFornExcluir.setDisable(false);
    }

    @FXML
    public void onBtnCarregar() {
        this.btnEstoqueBaixo.setDisable(false);
        this.btnFiltrarControlados.setDisable(false);
        this.btnFiltrarNaoControlados.setDisable(false);
        this.btnEstoqueFornecedor.setDisable(false);
        this.btnProximoVencimento.setDisable(false);

        updateTableView1(medicamentoList);
    }

    @FXML
    public void onBtnExcluirMed() {
        String codigo = txtCodigo.getText();
        excluirMedicamento(codigo);
    }

    @FXML
    public void onBtnExcluirForn() {
        String cnpj = txtCNPJ.getText();
        excluirFornecedor(cnpj);
    }

    @FXML
    public void onBtnSalvarMed() {
        if (this.medicamento != null) {
            if (lerFormularioMed() == null) return;

            Medicamento medicamentoExistente = medicamentoList.stream()
                    .filter(m -> m.getCodigo().equals(this.medicamento.getCodigo()))
                    .findFirst()
                    .orElse(null);

            if (medicamentoExistente != null) {
                medicamentoExistente.setNome(this.medicamento.getNome());
                medicamentoExistente.setDescricao(this.medicamento.getDescricao());
                medicamentoExistente.setPricipioAtivo(this.medicamento.getPricipioAtivo());
                medicamentoExistente.setDataValidade(this.medicamento.getDataValidade());
                medicamentoExistente.setQuantidadeEstoque(this.medicamento.getQuantidadeEstoque());
                medicamentoExistente.setPreco(this.medicamento.getPreco());
                medicamentoExistente.setControlado(this.medicamento.isControlado());
                medicamentoExistente.setFornecedor(this.medicamento.getFornecedor());

                System.out.println("Medicamento atualizado com sucesso: " + medicamentoExistente.getCodigo());
            } else {
                this.medicamentoList.add(this.medicamento);
                System.out.println("Medicamento adicionado com sucesso: " + this.medicamento.getCodigo());
            }


            gravarMedicamentosEmArquivo(medicamentoList);
            updateTableView1(medicamentoList);
        }
        tbMedicamento.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                preencherFormularioComMedicamento(newValue);
            }
        });
    }

    @FXML
    public void onBtnCalcularValorTotalEstoque() {
        calcularValorTotalEstoquePorFornecedor();
    }

    @FXML
    public void onBtnSalvarForn() {
        if (this.fornecedor != null) {
            if (lerFormularioForn() == null) return;

            if (cnpjFornecedorJaExiste(this.fornecedor.getCnpj())) {
                System.out.println("Erro: Já existe um fornecedor com o CNPJ " + this.fornecedor.getCnpj());
                return;
            }

            this.fornecedorList.add(this.fornecedor);
            gravarFornecedoresEmArquivo(fornecedorList);
        }
    }

    @FXML
    public void onBtnSalvarArquivo() {
        gravarMedicamentosEmArquivo(medicamentoList);
        gravarFornecedoresEmArquivo(fornecedorList);
    }

    @FXML
    public void onBtnFiltrarControlados() {
        List<Medicamento> medicamentosFiltrados = medicamentoList.stream()
                .filter(Medicamento::isControlado)
                .collect(Collectors.toList());

        updateTableView1(medicamentosFiltrados);
    }

    @FXML
    public void onBtnProximoVencimento() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(30);

        List<Medicamento> medicamentosProximosVencimento = medicamentoList.stream()
                .filter(medicamento -> medicamento.getDataValidade().isBefore(dataLimite) &&
                        medicamento.getDataValidade().isAfter(hoje))
                .collect(Collectors.toList());
        updateTableView1(medicamentosProximosVencimento);
    }

    @FXML
    public void onBtnFiltrarEstoqueBaixo() {
        List<Medicamento> medicamentosEstoqueBaixo = medicamentoList.stream()
                .filter(medicamento -> medicamento.getQuantidadeEstoque() < 5)
                .collect(Collectors.toList());
        updateTableView1(medicamentosEstoqueBaixo);
    }

    @FXML
    public void onBtnListarNaoControlados() {
        List<Medicamento> medicamentosNaoControlados = medicamentoList.stream()
                .filter(medicamento -> !medicamento.isControlado())
                .collect(Collectors.toList());

        updateTableView1(medicamentosNaoControlados);
    }

    //===============================================================
    // MÉTODOS

    private boolean codigoMedicamentoJaExiste(String codigo) {
        return medicamentoList.stream().anyMatch(medicamento -> medicamento.getCodigo().equals(codigo));
    }

    private boolean cnpjFornecedorJaExiste(String cnpj) {
        return fornecedorList.stream().anyMatch(fornecedor -> fornecedor.getCnpj().equals(cnpj));
    }

    public Medicamento lerFormularioMed() {
        String codigo = this.txtCodigo.getText();
        if (codigo.length() != 7 || !codigo.matches("[a-zA-Z0-9]+")) {
            showAlert("Código inválido", "O código deve ter exatamente 7 caracteres alfanuméricos.");
            return null;
        }
        this.medicamento.setCodigo(codigo);

        String nome = this.txtNome.getText();
        if (nome.isEmpty() || nome.length() < 3) {
            showAlert("Nome inválido", "O nome não pode ser vazio e deve ter pelo menos 3 caracteres.");
            return null;
        }
        this.medicamento.setNome(nome);

        this.medicamento.setDescricao(this.txtDescricao.getText());
        this.medicamento.setPricipioAtivo(this.txtPricipioAtivo.getText());

        LocalDate dataValidade;
        try {
            dataValidade = LocalDate.parse(this.txtDataValidade.getText());
            if (dataValidade.isBefore(LocalDate.now())) {
                showAlert("Data de validade inválida", "A data de validade não pode ser uma data passada.");
                return null;
            }
        } catch (Exception e) {
            showAlert("Data de validade inválida", "Formato de data inválido. Use o formato YYYY-MM-DD.");
            return null;
        }
        this.medicamento.setDataValidade(dataValidade);

        int quantidadeEstoque;
        try {
            quantidadeEstoque = Integer.parseInt(this.txtQuantidade.getText());
            if (quantidadeEstoque < 0) {
                showAlert("Quantidade inválida", "A quantidade em estoque não pode ser negativa.");
                return null;
            }
        } catch (NumberFormatException e) {
            showAlert("Quantidade inválida", "Por favor, insira um número válido para a quantidade em estoque.");
            return null;
        }
        this.medicamento.setQuantidadeEstoque(quantidadeEstoque);

        BigDecimal preco;
        try {
            preco = new BigDecimal(this.txtPreco.getText());
            if (preco.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert("Preço inválido", "O preço deve ser um valor positivo.");
                return null;
            }
        } catch (NumberFormatException e) {
            showAlert("Preço inválido", "Por favor, insira um valor válido para o preço.");
            return null;
        }
        this.medicamento.setPreco(preco);

        this.medicamento.setControlado(Boolean.parseBoolean(this.txtTipo.getText()));

        String nomeFornecedor = this.txtFornecedor.getText();
        Fornecedor fornecedor = buscarFornecedorPorNome(nomeFornecedor);

        if (fornecedor == null) {
            return null;
        } else {
            this.medicamento.setFornecedor(fornecedor);
        }

        return this.medicamento;
    }

    public Fornecedor buscarFornecedorPorNome(String nomeFornecedor) {
        for (Fornecedor fornecedor : fornecedorList) {
            if (fornecedor.getRazaoSocial().equalsIgnoreCase(nomeFornecedor)) {
                return fornecedor;
            }
        }
        return null;
    }

    public Fornecedor lerFormularioForn() {
        this.fornecedor.setCnpj(this.txtCNPJ.getText());
        if (!isCnpjValido(this.fornecedor.getCnpj())) {
            showAlert("CNPJ inválido", "Por favor, insira um CNPJ válido.");
            return null;
        }
        this.fornecedor.setRazaoSocial(this.txtRazaoSocial.getText());
        if (this.fornecedor.getRazaoSocial().isEmpty()) {
            showAlert("Nome inválido", "A razão social não pode ser vazia.");
            return null;
        }
        this.fornecedor.setTelefone(this.txtTelefone.getText());
        this.fornecedor.setEmail(this.txtEmail.getText());
        this.fornecedor.setCidade(this.txtCidade.getText());
        this.fornecedor.setEstado(this.txtEstado.getText());
        return this.fornecedor;
    }

    private boolean isCnpjValido(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        if (cnpj.length() != 14) {
            return false;
        }

        if (cnpj.chars().distinct().count() == 1) {
            return false;}
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void limparFormularioMed() {
        this.txtCodigo.setText("");
        this.txtNome.setText("");
        this.txtDescricao.setText("");
        this.txtPricipioAtivo.setText("");
        this.txtDataValidade.setText("");
        this.txtQuantidade.setText("");
        this.txtPreco.setText("");
        this.txtTipo.setText("");
        this.txtFornecedor.setText("");
    }

    public void limparFormularioForn() {
        this.txtCNPJ.setText("");
        this.txtRazaoSocial.setText("");
        this.txtTelefone.setText("");
        this.txtEmail.setText("");
        this.txtCidade.setText("");
        this.txtEstado.setText("");
    }

    private void preencherFormularioComMedicamento(Medicamento medicamento) {
        txtCodigo.setText(medicamento.getCodigo());
        txtNome.setText(medicamento.getNome());
        txtDescricao.setText(medicamento.getDescricao());
        txtPricipioAtivo.setText(medicamento.getPricipioAtivo());
        txtDataValidade.setText(medicamento.getDataValidade().toString());
        txtQuantidade.setText(String.valueOf(medicamento.getQuantidadeEstoque()));
        txtPreco.setText(medicamento.getPreco().toString());
        txtTipo.setText(String.valueOf(medicamento.isControlado()));

        Fornecedor fornecedor = medicamento.getFornecedor();
        txtFornecedor.setText(fornecedor != null ? fornecedor.getRazaoSocial() : "");
    }

    public List<Fornecedor> lerDadosDoArquivoFornecedores() {
        File file = new File("fornecedores.csv");

        if (!file.exists() || file.length() == 0) {
            System.out.println("O arquivo 'fornecedores.csv' não existe ou está vazio.");
            return Collections.emptyList();
        }

        List<Fornecedor> fornecedores = new ArrayList<>();

        try {
            fornecedores = Files.lines(Paths.get("fornecedores.csv"))
                    .map(linha -> linha.split(";"))
                    .filter(dados -> dados.length == 6)
                    .map(dados -> {
                        Fornecedor fornecedor = new Fornecedor();
                        fornecedor.setCnpj(dados[0]);
                        fornecedor.setRazaoSocial(dados[1]);
                        fornecedor.setTelefone(dados[2]);
                        fornecedor.setEmail(dados[3]);
                        fornecedor.setCidade(dados[4]);
                        fornecedor.setEstado(dados[5]);
                        return fornecedor;
                    })
                    .collect(Collectors.toList());

            fornecedorList.addAll(fornecedores);

            fornecedores.forEach(fornecedor ->
                    System.out.printf("CNPJ: %s, Razão Social: %s, Telefone: %s, Email: %s, Cidade: %s, Estado: %s%n",
                            fornecedor.getCnpj(), fornecedor.getRazaoSocial(), fornecedor.getTelefone(),
                            fornecedor.getEmail(), fornecedor.getCidade(), fornecedor.getEstado())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fornecedores;
    }

    public List<Medicamento> lerDadosDoArquivoMedicamentos() {
        File file = new File("medicamentos.csv");

        if (!file.exists() || file.length() == 0) {
            System.out.println("O arquivo 'medicamentos.csv' não existe ou está vazio.");
            return Collections.emptyList();
        }

        List<Medicamento> medicamentos = new ArrayList<>();

        try {
            medicamentos = Files.lines(Paths.get("medicamentos.csv"))
                    .map(linha -> linha.split(";"))
                    .filter(dados -> dados.length == 9)
                    .map(dados -> {
                        Medicamento medicamento = new Medicamento();
                        medicamento.setCodigo(dados[0]);
                        medicamento.setNome(dados[1]);
                        medicamento.setDescricao(dados[2]);
                        medicamento.setPricipioAtivo(dados[3]);
                        medicamento.setDataValidade(LocalDate.parse(dados[4]));
                        medicamento.setQuantidadeEstoque(Integer.parseInt(dados[5]));
                        medicamento.setPreco(new BigDecimal(dados[6]));
                        medicamento.setControlado(Boolean.parseBoolean(dados[7]));
                        medicamento.setFornecedor(buscarFornecedorPorNome(dados[8]));
                        return medicamento;
                    })
                    .collect(Collectors.toList());

            medicamentoList.addAll(medicamentos);

            medicamentos.forEach(medicamento ->
                    System.out.printf("Código: %s, Nome: %s, Descrição: %s, Princípio Ativo: %s, Data de Validade: %s, " +
                                    "Quantidade em Estoque: %d, Preço: %.2f, Controlado: %b, Fornecedor: %s%n",
                            medicamento.getCodigo(), medicamento.getNome(), medicamento.getDescricao(),
                            medicamento.getPricipioAtivo(), medicamento.getDataValidade(), medicamento.getQuantidadeEstoque(),
                            medicamento.getPreco(), medicamento.isControlado(), medicamento.getFornecedor() != null ? medicamento.getFornecedor().getRazaoSocial() : "N/A")
            );
        } catch (Exception e) {
            System.err.println("Erro ao ler dados do arquivo: " + e.getMessage());
        }

        return medicamentos;
    }

    public void gravarFornecedoresEmArquivo(List<Fornecedor> fornecedores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fornecedores.csv"))) {
            for (Fornecedor fornecedor : fornecedores) {
                String linha = String.join(";",
                        fornecedor.getCnpj(),
                        fornecedor.getRazaoSocial(),
                        fornecedor.getTelefone(),
                        fornecedor.getEmail(),
                        fornecedor.getCidade(),
                        fornecedor.getEstado());
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Fornecedores gravados com sucesso em 'fornecedores.csv'.");
        } catch (IOException e) {
            System.err.println("Erro ao gravar fornecedores em arquivo: " + e.getMessage());
        }
    }

    public void gravarMedicamentosEmArquivo(List<Medicamento> medicamentos) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("medicamentos.csv"))) {
            for (Medicamento medicamento : medicamentos) {
                String linha = String.join(";",
                        medicamento.getCodigo(),
                        medicamento.getNome(),
                        medicamento.getDescricao(),
                        medicamento.getPricipioAtivo(),
                        medicamento.getDataValidade().toString(),
                        String.valueOf(medicamento.getQuantidadeEstoque()),
                        decimalFormat.format(medicamento.getPreco()),
                        String.valueOf(medicamento.isControlado()),
                        medicamento.getFornecedor() != null
                                ? medicamento.getFornecedor().getRazaoSocial() : "");

                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Medicamentos gravados com sucesso em 'medicamentos.csv'.");
        } catch (IOException e) {
            System.err.println("Erro ao gravar medicamentos em arquivo: " + e.getMessage());
        }
    }

    public void excluirMedicamento(String codigo) {
        Medicamento medicamentoParaRemover = null;
        for (Medicamento medicamento : medicamentoList) {
            if (medicamento.getCodigo().equals(codigo)) {
                medicamentoParaRemover = medicamento;
                break;
            }
        }
        if (medicamentoParaRemover != null) {
            medicamentoList.remove(medicamentoParaRemover);
            System.out.println("Medicamento excluído com sucesso.");
            gravarMedicamentosEmArquivo(medicamentoList);
            updateTableView1(medicamentoList);
        } else {
            System.out.println("Medicamento não encontrado.");
        }
    }

    public void excluirFornecedor(String cnpj) {
        Fornecedor fornecedorParaRemover = null;
        for (Fornecedor fornecedor : fornecedorList) {
            if (fornecedor.getCnpj().equals(cnpj)) {
                fornecedorParaRemover = fornecedor;
                break;
            }
        }
        if (fornecedorParaRemover != null) {
            fornecedorList.remove(fornecedorParaRemover);
            System.out.println("Fornecedor excluído com sucesso.");
            gravarFornecedoresEmArquivo(fornecedorList);
        } else {
            System.out.println("Fornecedor não encontrado.");
        }
    }

    public void calcularValorTotalEstoquePorFornecedor() {
        Map<String, BigDecimal> totalPorFornecedor = new HashMap<>();

        for (Medicamento medicamento : medicamentoList) {
            String fornecedorNome = medicamento.getFornecedor() != null ? medicamento.getFornecedor().getRazaoSocial() : "N/A";
            BigDecimal precoUnitario = medicamento.getPreco();
            int quantidadeEstoque = medicamento.getQuantidadeEstoque();

            BigDecimal valorTotalMedicamento = precoUnitario.multiply(BigDecimal.valueOf(quantidadeEstoque));

            totalPorFornecedor.merge(fornecedorNome, valorTotalMedicamento, BigDecimal::add);
        }

        StringBuilder resultado = new StringBuilder();
        totalPorFornecedor.forEach((fornecedor, total) ->
                resultado.append(String.format("Fornecedor: %s, Valor Total do Estoque: %.2f%n", fornecedor, total))
        );
        labelResultadoAqui.setText(resultado.toString());
    }

    public void updateTableView1(List<Medicamento> medicamentos) {
        ObservableList<Medicamento> observableList = FXCollections.observableArrayList(medicamentos);
        tbMedicamento.setItems(observableList);
    }
}