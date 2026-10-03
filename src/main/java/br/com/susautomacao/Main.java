package br.com.susautomacao;

import java.util.List;

import br.com.susautomacao.browser.SusHelpBot;
import br.com.susautomacao.model.Registro;
import br.com.susautomacao.parser.PacienteParser;
import br.com.susautomacao.pdf.PdfReader;
import br.com.susautomacao.regras.RegraProcedimento; // <-- Importação nova

public class Main {
    public static void main(String[] args) {
        System.out.println("1. Lendo o PDF...");
        PdfReader leitor = new PdfReader();
        String textoExtraido = leitor.extrairTexto("CEDIM - SETEMBRO.pdf");

        if (textoExtraido != null && !textoExtraido.trim().isEmpty()) {
            System.out.println("2. Extraindo pacientes...");
            PacienteParser parser = new PacienteParser();
            List<Registro> todosOsRegistros = parser.extrairRegistros(textoExtraido);

            System.out.println("3. Aplicando regras de bloqueio...");
            RegraProcedimento regras = new RegraProcedimento();
            List<Registro> registrosParaDigitar = regras.aplicarFiltro(todosOsRegistros);

            System.out.println("\n====================================================");
            System.out.println("TOTAL ENCONTRADO NO PDF: " + todosOsRegistros.size());
            System.out.println("TOTAL LIBERADO PARA O SITE: " + registrosParaDigitar.size());
            System.out.println("====================================================\n");

            // ==========================================
            // NOVIDADE: ENVIANDO OS DADOS PARA O ROBÔ
            // ==========================================
            System.out.println("4. Iniciando a automação no navegador...");
            SusHelpBot robo = new SusHelpBot();
            
            // Passamos a lista 'registrosParaDigitar' para dentro do robô
            robo.iniciarDigitacao(registrosParaDigitar);

        } else {
            System.out.println("Erro na leitura do arquivo.");
        }
    }
}