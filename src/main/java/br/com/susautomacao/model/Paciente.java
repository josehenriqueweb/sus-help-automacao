package br.com.susautomacao.model;

public class Paciente {
    private String cns;
    private String nome;
    private String dataNascimento;

    public Paciente(String cns, String nome, String dataNascimento) {
        this.cns = cns;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public String getCns() {
        return cns;
    }

    public String getNome() {
        return nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    @Override
    public String toString() {
        return "CNS: " + cns + " | Nome: " + nome + " | Nasc: " + dataNascimento;
    }
}