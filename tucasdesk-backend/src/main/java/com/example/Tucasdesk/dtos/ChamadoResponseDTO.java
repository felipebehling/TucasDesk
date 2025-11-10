package com.example.Tucasdesk.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO exposing a {@code Chamado} enriched with related data such as categoria, prioridade and interações.
 */
public class ChamadoResponseDTO {

    private Integer id;
    private String titulo;
    private String descricao;
    private LookupResponseDTO categoria;
    private LookupResponseDTO status;
    private LookupResponseDTO prioridade;
    private UsuarioResumoDTO solicitante;
    private UsuarioResumoDTO tecnico;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private List<InteracaoResponseDTO> interacoes = new ArrayList<>();
    private List<HistoricoStatusResponseDTO> historicoStatus = new ArrayList<>();

    public ChamadoResponseDTO() {
    }

    public ChamadoResponseDTO(Integer id,
                              String titulo,
                              String descricao,
                              LookupResponseDTO categoria,
                              LookupResponseDTO status,
                              LookupResponseDTO prioridade,
                              UsuarioResumoDTO solicitante,
                              UsuarioResumoDTO tecnico,
                              LocalDateTime dataAbertura,
                              LocalDateTime dataFechamento,
                              List<InteracaoResponseDTO> interacoes,
                              List<HistoricoStatusResponseDTO> historicoStatus) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.status = status;
        this.prioridade = prioridade;
        this.solicitante = solicitante;
        this.tecnico = tecnico;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        if (interacoes != null) {
            this.interacoes = interacoes;
        }
        if (historicoStatus != null) {
            this.historicoStatus = historicoStatus;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LookupResponseDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(LookupResponseDTO categoria) {
        this.categoria = categoria;
    }

    public LookupResponseDTO getStatus() {
        return status;
    }

    public void setStatus(LookupResponseDTO status) {
        this.status = status;
    }

    public LookupResponseDTO getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(LookupResponseDTO prioridade) {
        this.prioridade = prioridade;
    }

    public UsuarioResumoDTO getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(UsuarioResumoDTO solicitante) {
        this.solicitante = solicitante;
    }

    public UsuarioResumoDTO getTecnico() {
        return tecnico;
    }

    public void setTecnico(UsuarioResumoDTO tecnico) {
        this.tecnico = tecnico;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public List<InteracaoResponseDTO> getInteracoes() {
        return interacoes;
    }

    public void setInteracoes(List<InteracaoResponseDTO> interacoes) {
        this.interacoes = interacoes;
    }

    public List<HistoricoStatusResponseDTO> getHistoricoStatus() {
        return historicoStatus;
    }

    public void setHistoricoStatus(List<HistoricoStatusResponseDTO> historicoStatus) {
        this.historicoStatus = historicoStatus;
    }
}
