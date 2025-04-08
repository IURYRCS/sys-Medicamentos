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
        this.fornecedorList = lerDadosDoArquivoFornecedores();
        this.medicamentoList = lerDadosDoArquivoMedicamentos();
    }
    @FXML
    public void onBtnMedNovo() {
        this.medicamento = new Medicamento();
        limparFormularioMed();
        this.btnMedSalvar.setDisable(false);
    }

    @FXML
    public void onBtnFornNovo() {
        this.fornecedor = new Fornecedor();
        limparFormularioForn();
        this.btnFornSalvar.setDisable(false);
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
            lerFormularioMed();

            if (codigoMedicamentoJaExiste(this.medicamento.getCodigo())) {
                System.out.println("Erro: Já existe um medicamento com o código " + this.medicamento.getCodigo());
                return;
            }

            this.medicamentoList.add(this.medicamento);
            updateTableView1(medicamentoList);
        }
    }
    @FXML
    public void onBtnCalcularValorTotalEstoque() {
        calcularValorTotalEstoquePorFornecedor();
    }

    @FXML
    public void onBtnSalvarForn() {
        if (this.fornecedor != null) {
            lerFormularioForn();


            if (cnpjFornecedorJaExiste(this.fornecedor.getCnpj())) {
                System.out.println("Erro: Já existe um fornecedor com o CNPJ " + this.fornecedor.getCnpj());
                return;
            }

            this.fornecedorList.add(this.fornecedor);
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
        this.medicamento.setCodigo(this.txtCodigo.getText());
        this.medicamento.setNome(this.txtNome.getText());
        this.medicamento.setDescricao(this.txtDescricao.getText());
        this.medicamento.setPricipioAtivo(this.txtPricipioAtivo.getText());

        this.medicamento.setDataValidade(LocalDate.parse(this.txtDataValidade.getText()));
        this.medicamento.setQuantidadeEstoque(Integer.parseInt(this.txtQuantidade.getText()));

        String precoString = this.txtPreco.getText();
        BigDecimal preco = new BigDecimal(precoString);
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
        this.fornecedor.setRazaoSocial(this.txtRazaoSocial.getText());
        this.fornecedor.setTelefone(this.txtTelefone.getText());
        this.fornecedor.setEmail(this.txtEmail.getText());
        this.fornecedor.setCidade(this.txtCidade.getText());
        this.fornecedor.setEstado(this.txtEstado.getText());
        return this.fornecedor;
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

    public List<Fornecedor> lerDadosDoArquivoFornecedores() {
        File file = new File("fornecedores.txt");

        if (!file.exists() || file.length() == 0) {
            System.out.println("O arquivo 'fornecedores.txt' não existe ou está vazio.");
            return Collections.emptyList();
        }

        List<Fornecedor> fornecedores = new ArrayList<>();

        try {
            fornecedores = Files.lines(Paths.get("fornecedores.txt"))
                    .map(linha -> linha.split(","))
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
        File file = new File("medicamentos.txt");

        if (!file.exists() || file.length() == 0) {
            System.out.println("O arquivo 'medicamentos.txt' não existe ou está vazio.");
            return Collections.emptyList();
        }

        List<Medicamento> medicamentos = new ArrayList<>();

        try {
            medicamentos = Files.lines(Paths.get("medicamentos.txt"))
                    .map(linha -> linha.split(","))
                    .filter(dados -> dados.length == 9)
                    .map(dados -> {
                        try {
                            String codigo = dados[0];
                            String nome = dados[1];
                            String descricao = dados[2];
                            String pricipioAtivo = dados[3];
                            LocalDate dataValidade = LocalDate.parse(dados[4]);
                            int quantidadeEstoque = Integer.parseInt(dados[5]);
                            BigDecimal preco = new BigDecimal(dados[6]);
                            boolean controlado = Boolean.parseBoolean(dados[7]);
                            String fornecedorNome = dados[8];

                            Medicamento medicamento = new Medicamento();
                            medicamento.setCodigo(codigo);
                            medicamento.setNome(nome);
                            medicamento.setDescricao(descricao);
                            medicamento.setPricipioAtivo(pricipioAtivo);
                            medicamento.setDataValidade(dataValidade);
                            medicamento.setQuantidadeEstoque(quantidadeEstoque);
                            medicamento.setPreco(preco);
                            medicamento.setControlado(controlado);
                            medicamento.setFornecedor(buscarFornecedorPorNome(fornecedorNome));
                            return medicamento;
                        } catch (Exception e) {
                            System.err.println("Erro ao converter dados da linha: " + String.join(",", dados));
                            return null;
                        }
                    })
                    .filter(medicamento -> medicamento != null)
                    .collect(Collectors.toList());


            medicamentoList.addAll(medicamentos);


            medicamentos.forEach(medicamento ->
                    System.out.printf("Código: %s, Nome: %s, Descrição: %s, Princípio Ativo: %s, Data de Validade: %s, " +
                                    "Quantidade em Estoque: %d, Preço: %.2f, Controlado: %b, Fornecedor: %s%n",
                            medicamento.getCodigo(), medicamento.getNome(), medicamento.getDescricao(),
                            medicamento.getPricipioAtivo(), medicamento.getDataValidade(), medicamento.getQuantidadeEstoque(),
                            medicamento.getPreco(), medicamento.isControlado(), medicamento.getFornecedor() != null ? medicamento.getFornecedor().getRazaoSocial() : "N/A")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return medicamentos;
    }

    public void gravarFornecedoresEmArquivo(List<Fornecedor> fornecedores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("fornecedores.txt"))) {
            for (Fornecedor fornecedor : fornecedores) {
                String linha = String.join(",",
                        fornecedor.getCnpj(),
                        fornecedor.getRazaoSocial(),
                        fornecedor.getTelefone(),
                        fornecedor.getEmail(),
                        fornecedor.getCidade(),
                        fornecedor.getEstado());
                writer.write(linha);
                writer.newLine();
            }
            System.out.println("Fornecedores gravados com sucesso em 'fornecedores.txt'.");
        } catch (IOException e) {
            System.err.println("Erro ao gravar fornecedores em arquivo: " + e.getMessage());
        }
    }

    public void gravarMedicamentosEmArquivo(List<Medicamento> medicamentos) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("medicamentos.txt"))) {
            for (Medicamento medicamento : medicamentos) {
                String linha = String.join(",",
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
            System.out.println("Medicamentos gravados com sucesso em 'medicamentos.txt'.");
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