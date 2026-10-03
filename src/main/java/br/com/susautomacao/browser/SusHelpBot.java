package br.com.susautomacao.browser;

import java.util.List;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import br.com.susautomacao.model.Procedimento;
import br.com.susautomacao.model.Registro;

public class SusHelpBot {

    public void iniciarDigitacao(List<Registro> registos) {
        System.out.println("-> Iniciando o navegador...");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            
            page.navigate("https://sus-help.egovinovacoes.com.br/modulos/beneficios");

            System.out.println("=================================================");
            System.out.println("Você tem 20 SEGUNDOS para logar e abrir a tela...");
            System.out.println("=================================================");
            
            page.waitForTimeout(20000); 

            System.out.println("\n-> Iniciando a digitação...");
            
            for (int i = 0; i < registos.size(); i++) {
                Registro pacienteAtual = registos.get(i);
                String cns = pacienteAtual.getPaciente().getCns();
                String data = pacienteAtual.getDataAtendimento(); // Pega a data que extraímos
                List<Procedimento> procedimentos = pacienteAtual.getProcedimentos();
                
                System.out.println("Digitando paciente " + (i + 1) + " de " + registos.size() + ": " + pacienteAtual.getPaciente().getNome() + " (Data: " + data + ")");
                
                // ==========================================
                // 0. DATA
                // ==========================================
                // Clica no campo de data (placeholder dd/mm/aaaa)
                page.getByPlaceholder("dd/mm/aaaa").click();
                page.waitForTimeout(500);
                
                // Apaga o que já estiver lá (seleciona tudo e deleta)
                page.keyboard().press("Control+A");
                page.keyboard().press("Backspace");
                page.waitForTimeout(500);
                
                // Digita a data nova
                page.keyboard().type(data);
                page.waitForTimeout(1000);
                
                // Muito importante: Aperta TAB (não Enter!) para sair do campo
                page.keyboard().press("Tab");
                page.waitForTimeout(1000);

                // ==========================================
                // 1. CNS
                // ==========================================
                page.locator("nz-select-top-control:has-text('Digite o nome, CPF ou CNS do cidadão') input").click();
                page.waitForTimeout(500);
                page.keyboard().type(cns); 
                page.waitForTimeout(2000); 
                page.keyboard().press("ArrowDown"); 
                page.waitForTimeout(500);
                page.keyboard().press("Enter");
                page.waitForTimeout(1500); 
                
                // ==========================================
                // 2. PROFISSIONAL
                // ==========================================
                page.keyboard().press("Tab");
                page.waitForTimeout(500);
                page.keyboard().type("LARICE LEITE");
                page.waitForTimeout(1500); 
                page.keyboard().press("ArrowDown");
                page.waitForTimeout(500);
                page.keyboard().press("Enter");
                page.waitForTimeout(1000);

                // ==========================================
                // 3. PROCEDIMENTOS
                // ==========================================
                for (int p = 0; p < procedimentos.size(); p++) {
                    if (p > 0) {
                        page.locator("button i.fa-plus").click();
                        page.waitForTimeout(1000); 
                    }
                    String codigoProc = procedimentos.get(p).getCodigo();
                    page.locator("nz-select-top-control:has-text('Pesquise por Código ou Descrição do Procedimento') input").last().click();
                    page.waitForTimeout(500);
                    page.keyboard().type(codigoProc);
                    page.waitForTimeout(2000); 
                    page.keyboard().press("ArrowDown");
                    page.waitForTimeout(500);
                    page.keyboard().press("Enter");
                }
                
                // ==========================================
                // 4. SALVAR E CONFIRMAR
                // ==========================================
                page.waitForTimeout(1000);
                page.locator("button:has-text('Adicionar')").click();
                
                page.waitForTimeout(1000); 
                page.locator("button:has-text('Confirmar')").click(); 
                
                System.out.println("-> Paciente salvo com sucesso!");
                
                page.waitForTimeout(3000); 
                
                // 5. Clica em "Registrar" para reabrir a janela para o próximo paciente
                if (i < registos.size() - 1) {
                    page.locator("button:has-text('Registrar')").first().click();
                    page.waitForTimeout(2500); 
                }
            }
            
            System.out.println("\n-> Fim da automação!");
        }
    }
}