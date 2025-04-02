package org.sysmedicamentos.controller;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.sysmedicamentos.model.entity.Fornecedor;
import org.sysmedicamentos.model.entity.Medicamento;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private List<Medicamento> medicamentoList;
    private List<Fornecedor> fornecedorList;
    private Medicamento medicamento;
    private Fornecedor fornecedor;

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
    private Label lbNome;
    @FXML
    private Label lbDescricao;
    @FXML
    private Label lbPricipioAtivo;
    @FXML
    private Label lbDataValidade;
    @FXML
    private Label lbQuantidade;
    @FXML
    private Label lbPreco;
    @FXML
    private Label lbTipo;
    @FXML
    private Label lbCNPJ;
    @FXML
    private Label lbRazaoSocial;
    @FXML
    private Label lbTelefone;
    @FXML
    private Label lbEmail;
    @FXML
    private Label lbCidade;
    @FXML
    private Label lbEstado;

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
    private Button btnFiltrarTipo;
    @FXML
    private Button btnProximoVencimento;
    @FXML
    private Button btnEstoqueFornecedor;
    @FXML
    private Button btnEstoqueBaixo;

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
    private TableColumn<Medicamento, Fornecedor> fornecedorField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        this.medicamentoList =new ArrayList<>();
        this.fornecedorList = new ArrayList<>();
        this.medicamento = new Medicamento();
        this.fornecedor = new Fornecedor();

        this.btnFornExcluir.setDisable(true);
        this.btnMedExcluir.setDisable(true);
        this.btnFornSalvar.setDisable(true);
        this.btnMedSalvar.setDisable(true);


        codigoField.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        nomeField.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        estoqueField.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidadeEstoque()).asObject());
        //validadeField; precoField;
        //    @FXML
        //    private TableColumn<Medicamento, Integer> estoqueField;
        //    @FXML
        //    private TableColumn<Medicamento, Boolean> tipoField;
        //    @FXML
        //    private TableColumn<Medicamento, Fornecedor> fornecedorField;

    }
}
