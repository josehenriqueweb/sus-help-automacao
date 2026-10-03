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
        String dataGeral = "03/09/2026"; // Data padrão caso não encontre
        Pattern patternData = Pattern.compile("(?i)Data/Hora:\\s*(\\d{2}/\\d{2}/\\d{4})");
        Matcher matcherData = patternData.matcher(textoPdf);
        if (matcherData.find()) {
            dataGeral = matcherData.group(1);
        } else {
            // Padrão alternativo (ex: "DD/MM/AAAA - DIA")
            Pattern patternDataAlt = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})\\s+-");
            Matcher matcherDataAlt = patternDataAlt.matcher(textoPdf);
            if (matcherDataAlt.find()) {
                dataGeral = matcherDataAlt.group(1);
            }
        }

        // 2. Regex robusta à prova de falhas:
        // Captura CNS (15 dígitos), Nome (mesmo com quebras de linha) e Código do procedimento (10 dígitos)
        String regex = "(?i)CNS\\s*:\\s*(\\d{15})\\s+Paciente\\s*:\\s*([\\s\\S]*?)\\s*Nascimento\\s*:[\\s\\S]*?Procedimento(?:\\(s\\)|s)?\\s*:[\\s\\S]*?\\(?(\\d{10})\\)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textoPdf);

        // 3. Itera por todos os procedimentos encontrados no relatório
        while (matcher.find()) {
            String cns = matcher.group(1).trim();
            // Trata quebras de linha e múltiplos espaços no nome do paciente
            String nome = matcher.group(2).replaceAll("\\s+", " ").trim();
            String codigoProcedimento = matcher.group(3).trim();

            Paciente paciente = new Paciente(cns, nome, "");
            Procedimento procedimento = new Procedimento(codigoProcedimento, "");

            Registro registroEncontrado = null;
            for (Registro r : registros) {
                if (r.getPaciente().getCns().equals(cns)) {
                    registroEncontrado = r;
                    break;
                }
            }

            if (registroEncontrado != null) {
                registroEncontrado.getProcedimentos().add(procedimento);
                if (registroEncontrado.getDataAtendimento() == null) {
                    registroEncontrado.setDataAtendimento(dataGeral);
                }
            } else {
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