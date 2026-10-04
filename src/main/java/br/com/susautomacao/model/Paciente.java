package br.com.susautomacao.model;

public class Paciente {
    private String cns;
    private String nome;
    private String dataNascimento;
    private String sexo; // <-- ESTA É A NOVA ALTERAÇÃO

    // Construtor atualizado para receber o sexo
    public Paciente(String cns, String nome, String dataNascimento, String sexo) {
        this.cns = cns;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo; // <-- ESTA É A NOVA ALTERAÇÃO
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
    
    // Novo método para o robô descobrir se é homem ou mulher na hora de clicar
    public String getSexo() { 
        return sexo; 
    }
}