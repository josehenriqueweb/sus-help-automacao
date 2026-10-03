package br.com.susautomacao.model;

import java.util.ArrayList;
import java.util.List;

public class Registro {
    private Paciente paciente;
    private List<Procedimento> procedimentos;
    private String dataAtendimento;

    // Construtor 1 (Usado pelo RegraProcedimento)
    public Registro(Paciente paciente) {
        this.paciente = paciente;
        this.procedimentos = new ArrayList<>();
    }

    // Construtor 2 (Usado pelo PacienteParser)
    public Registro(Paciente paciente, List<Procedimento> procedimentos) {
        this.paciente = paciente;
        this.procedimentos = procedimentos;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public List<Procedimento> getProcedimentos() {
        return procedimentos;
    }

    // Método que faltava!
    public void adicionarProcedimento(Procedimento procedimento) {
        this.procedimentos.add(procedimento);
    }

    public String getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(String dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }
}