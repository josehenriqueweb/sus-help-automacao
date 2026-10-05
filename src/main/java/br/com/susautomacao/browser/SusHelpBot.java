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
                
                // ==========================================
                // INÍCIO DA BLINDAGEM (TRY)
                // ==========================================
                try {
                    Registro pacienteAtual = registos.get(i);
                    String cns = pacienteAtual.getPaciente().getCns();
                    String data = pacienteAtual.getDataAtendimento(); 
                    List<Procedimento> procedimentos = pacienteAtual.getProcedimentos();
                    
                    System.out.println("Digitando paciente " + (i + 1) + " de " + registos.size() + ": " + pacienteAtual.getPaciente().getNome() + " (Data: " + data + ")");
                    
                    // ==========================================
                    // 0. DATA
                    // ==========================================
                    page.getByPlaceholder("dd/mm/aaaa").first().click();
                    page.waitForTimeout(500);
                    
                    page.keyboard().press("Control+A");
                    page.keyboard().press("Backspace");
                    page.waitForTimeout(500);
                    
                    page.keyboard().type(data);
                    page.waitForTimeout(1000);
                    
                    page.keyboard().press("Tab");
                    page.waitForTimeout(1000);

                    // ==========================================
                    // 1. CNS (Com Espera Dinâmica e Clique Forçado)
                    // ==========================================
                    page.locator("nz-select-top-control:has-text('Digite o nome, CPF ou CNS do cidadão') input").click();
                    page.waitForTimeout(500);
                    page.keyboard().type(cns); 
                    
                    page.waitForTimeout(3500); 

                    boolean naoEncontrado = page.locator("text=Nenhum resultado encontrado").isVisible();

                    if (naoEncontrado) {
                        System.out.println("-> Cidadão não encontrado na base. Realizando cadastro automático...");
                        
                        page.locator("button:has-text('Cadastrar cidadão')").click();
                        page.waitForTimeout(2000); 

                        page.locator("input[placeholder*='000 0000 0000 0000']").fill(cns);
                        page.waitForTimeout(500);

                        page.locator("input[placeholder='Nome completo']").fill(pacienteAtual.getPaciente().getNome());
                        page.waitForTimeout(500);

                        page.locator("input[placeholder='Informe a data']").fill(pacienteAtual.getPaciente().getDataNascimento());
                        page.waitForTimeout(500);
                        page.keyboard().press("Tab");

                        page.locator("nz-select-top-control:has-text('Informe o sexo')").click();
                        page.waitForTimeout(500);
                        String textoSexo = pacienteAtual.getPaciente().getSexo().equalsIgnoreCase("M") ? "Masculino" : "Feminino";
                        page.locator("nz-option-item:has-text('" + textoSexo + "')").click();
                        page.waitForTimeout(500);

                        page.locator("nz-select-top-control:has-text('Informe a cor/raça')").click();
                        page.waitForTimeout(500);
                        page.locator("nz-option-item:has-text('Parda')").click();
                        page.waitForTimeout(500);

                        page.waitForTimeout(1000);
                        page.locator("button:has-text('Cadastrar')").last().evaluate("node => node.click()");
                        
                        page.waitForTimeout(3500); 
                        
                        page.locator("nz-select-top-control:has-text('Digite o nome, CPF ou CNS do cidadão') input").click();
                        page.waitForTimeout(500);
                        page.keyboard().type(cns);
                        page.waitForTimeout(3500); 
                    }

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
                            page.locator("button i.fa-plus").last().click();
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
                    
                    // ==========================================
                    // 5. REABRIR JANELA (COM CLIQUE FORÇADO JS)
                    // ==========================================
                    if (i < registos.size() - 1) {
                        page.locator("button:has-text('Registrar')").first().evaluate("node => node.click()");
                        page.waitForTimeout(2500); 
                    }

                } catch (Exception e) {
                    // ==========================================
                    // FIM DA BLINDAGEM (CATCH) - SE DER ERRO CAI AQUI
                    // ==========================================
                    System.out.println("-> ⚠️ ERRO NO SISTEMA (Paciente " + (i + 1) + "). Limpando tela e pulando para o próximo...");
                    
                    // Aperta 'ESC' duas vezes para forçar o fecho de qualquer janela/mensagem travada
                    page.keyboard().press("Escape");
                    page.waitForTimeout(1000);
                    page.keyboard().press("Escape");
                    page.waitForTimeout(1000);
                    
                    // Tenta reabrir a janela para o próximo paciente (também com clique forçado JS)
                    if (i < registos.size() - 1) {
                        page.locator("button:has-text('Registrar')").first().evaluate("node => node.click()");
                        page.waitForTimeout(2500);
                    }
                }
            }
            
            System.out.println("\n-> Fim da automação!");
        }
    }
}