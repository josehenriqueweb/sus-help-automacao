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

        // Data geral
        String regexData = "(\\d{2}/\\d{2}/\\d{4})\\s+-";
        Pattern patternData = Pattern.compile(regexData);
        Matcher matcherData = patternData.matcher(textoPdf);
        String dataGeral = matcherData.find() ? matcherData.group(1) : "03/09/2026"; 

        // Voltamos à SUA regex original (a mais fiável para este PDF)
        String regex = "(?i)CNS\\s*:\\s*(\\d{15})\\s+Paciente\\s*:\\s*([\\s\\S]*?)\\s*Nascimento\\s*:[\\s\\S]*?Procedimento(?:\\(s\\)|s)?\\s*:[\\s\\S]*?\\(?(\\d{10})\\)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(textoPdf);

        while (matcher.find()) {
            String cns = matcher.group(1).trim();
            
            // 1. LIMPEZA TOTAL DO NOME
            String nomeBruto = matcher.group(2);
            String nomeLimpo = nomeBruto.replaceAll("\\r\\n|\\r|\\n", " ") // Remove quebras de linha
                                        .replaceAll("(?i)Paciente\\s*:\\s*", "") // Garante que a tag "Paciente:" é removida
                                        .replaceAll("\\s+", " ") // Remove espaços duplos
                                        .trim();

            // 2. EXTRAÇÃO SEGURA DA DATA DE NASCIMENTO (Dentro do bloco atual)
            String blocoDados = matcher.group();
            Matcher mNasc = Pattern.compile("(?i)Nascimento\\s*:\\s*(\\d{2}/\\d{2}/\\d{4})").matcher(blocoDados);
            String dataNascimento = mNasc.find() ? mNasc.group(1) : "01/01/1990";
            
            String codigoProcedimento = matcher.group(3).trim();

            // 3. DEDUÇÃO DO SEXO
            String sexo = deduzirSexo(nomeLimpo);

            Paciente paciente = new Paciente(cns, nomeLimpo, dataNascimento, sexo);
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

    private String deduzirSexo(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.isEmpty()) return "Masculino";
        String primeiroNome = nomeCompleto.trim().split(" ")[0].toUpperCase();
        if (primeiroNome.endsWith("A") && !primeiroNome.equals("LUCAS") && !primeiroNome.equals("JOSUE") && !primeiroNome.equals("ELIAS") && !primeiroNome.equals("MESSIAS")) {
            return "Feminino";
        }
        return "Masculino";
    }
}