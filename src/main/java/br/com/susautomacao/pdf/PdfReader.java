package br.com.susautomacao.pdf;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfReader {

    /**
     * Recebe o caminho do arquivo PDF e retorna todo o texto contido nele.
     */
    public String extrairTexto(String caminhoArquivo) {
        String textoExtraido = "";
        File arquivoPdf = new File(caminhoArquivo);

        // Usamos o try-with-resources para garantir que o documento será fechado da memória
        // mesmo se acontecer algum erro durante a leitura.
        try (PDDocument documento = Loader.loadPDF(arquivoPdf)) {
            
            PDFTextStripper extrator = new PDFTextStripper();
            textoExtraido = extrator.getText(documento);
            
        } catch (IOException e) {
            System.err.println("Erro ao tentar ler o arquivo PDF: " + e.getMessage());
        }

        return textoExtraido;
    }
}