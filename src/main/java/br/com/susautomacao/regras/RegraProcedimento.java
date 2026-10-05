package br.com.susautomacao.regras;

import java.util.ArrayList;
import java.util.List;

import br.com.susautomacao.model.Procedimento;
import br.com.susautomacao.model.Registro;

public class RegraProcedimento {

    // Coloque as numerações bloqueadas aqui (como Strings exatas)
    private List<String> procedimentosBloqueados = List.of(
        "0205020186",
        "0205020062" 
    );

    public List<Registro> aplicarFiltro(List<Registro> registrosOriginais) {
        List<Registro> registrosFiltrados = new ArrayList<>();
        int ignorados = 0;

        for (Registro registro : registrosOriginais) {
            List<Procedimento> procedimentosValidos = new ArrayList<>();

            for (Procedimento proc : registro.getProcedimentos()) {
                // O .trim() é vital aqui para ignorar espaços invisíveis do PDF
                String codigoLimpo = proc.getCodigo().trim(); 
                
                if (procedimentosBloqueados.contains(codigoLimpo)) {
                    ignorados++;
                } else {
                    procedimentosValidos.add(proc);
                }
            }

            
            // Se o paciente sobrou com pelo menos 1 válido, atualizamos a lista e adicionamos.
            // Se ficou com 0 (caso da Evellyn), ele é ignorado e não vai para o registrosFiltrados!
            if (!procedimentosValidos.isEmpty()) {
                registro.getProcedimentos().clear();
                registro.getProcedimentos().addAll(procedimentosValidos);
                registrosFiltrados.add(registro);
            }
        }

        System.out.println("-> Procedimentos bloqueados e ignorados pela regra: " + ignorados);
        return registrosFiltrados;
    }
}