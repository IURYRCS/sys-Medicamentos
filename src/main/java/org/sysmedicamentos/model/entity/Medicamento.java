package org.sysmedicamentos.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicamento {
    private String codigo;
    private String nome;
    private String descricao;
    private String pricipioAtivo;
    private LocalDate dataValidade;
    private int quantidadeEstoque;
    private BigDecimal preco;
    private boolean controlado;
    private Fornecedor fornecedor;

    //CONTRUTORES

    public Medicamento() {
    }

    public Medicamento(String codigo, String nome, String descricao, String pricipioAtivo, LocalDate dataValidade, int quantidadeEstoque, BigDecimal preco, boolean controlado, Fornecedor fornecedor) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.pricipioAtivo = pricipioAtivo;
        this.dataValidade = dataValidade;
        this.quantidadeEstoque = quantidadeEstoque;
        this.preco = preco;
        this.controlado = controlado;
        this.fornecedor = fornecedor;
    }

    //GETTERS E SETTERS


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPricipioAtivo() {
        return pricipioAtivo;
    }

    public void setPricipioAtivo(String pricipioAtivo) {
        this.pricipioAtivo = pricipioAtivo;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public boolean isControlado() {
        return controlado;
    }

    public void setControlado(boolean controlado) {
        this.controlado = controlado;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
    //METODOS

    @Override
    public String toString() {
        return "Medicamento{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", pricipioAtivo='" + pricipioAtivo + '\'' +
                ", dataValidade=" + dataValidade +
                ", quantidadeEstoque=" + quantidadeEstoque +
                ", preco=" + preco +
                ", controlado=" + controlado +
                ", fornecedor=" + fornecedor +
                '}';
    }
}
