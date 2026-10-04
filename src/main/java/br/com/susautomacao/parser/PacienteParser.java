package br.com.susautomacao.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.susautomacao.model.Paciente;
import br.com.susautomacao.model.Procedimento;
import br.com.susautomacao.model.Registro;

public class PacienteParser {

    public List<Registro> extrairRegistros(String textoPdf) {
        List<Registro> registros = new ArrayList<>();

        if (textoPdf == null || textoPdf.trim().isEmpty()) {
            return registros;
        }

        // 1. Extração da data de atendimento do relatório (topo do PDF)
        String regexData = "(\\d{2}/\\d{2}/\\d{4})\\s+-";
        Pattern patternData = Pattern.compile(regexData);
        Matcher matcherData = patternData.matcher(textoPdf);

        String dataGeral = "03/09/2026"; 
        if (matcherData.find()) {
            dataGeral = matcherData.group(1); 
        }

        // 2. Regex robusta e atualizada (A prova de falhas): 
        // Captura CNS, Nome, Sexo (M/F), Data Nasc. e Código do procedimento
        String regex = "(?s)(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})?\\s*(\\d{15})\\s+(.+?)\\s+([MF])\\s+(\\d{2}/\\d{2}/\\d{4})?\\s+(\\d{10})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textoPdf);

        // 3. Itera por todos os procedimentos encontrados no relatório
        while (matcher.find()) {
            String cns = matcher.group(2);
            
            // Limpa as quebras de linha e múltiplos espaços no nome do paciente
            String nome = matcher.group(3).replaceAll("\\r\\n|\\r|\\n", " ").trim();
            
            // Captura o Sexo e a Data de Nascimento (Novos campos!)
            String sexo = matcher.group(4); 
            String dataNascimento = matcher.group(5) != null ? matcher.group(5) : ""; 
            
            String codigoProcedimento = matcher.group(6);

            // Passa os novos campos para o objeto Paciente
            Paciente paciente = new Paciente(cns, nome, dataNascimento, sexo);
            Procedimento procedimento = new Procedimento(codigoProcedimento, "");
            
            Registro registroEncontrado = null;
            for (Registro r : registros) {
                if (r.getPaciente().getCns().equals(cns)) {
                    registroEncontrado = r;
                    break;
                }
            }

            if (registroEncontrado != null) {
                // Se já existe, apenas adiciona o novo procedimento
                registroEncontrado.getProcedimentos().add(procedimento);
            } else {
                // Se não existe, cria um novo registro
                List<Procedimento> novosProcedimentos = new ArrayList<>();
                novosProcedimentos.add(procedimento);
                Registro novoRegistro = new Registro(paciente, novosProcedimentos);
                
                novoRegistro.setDataAtendimento(dataGeral); 
                registros.add(novoRegistro);
            }
        }

        return registros;
    }
}