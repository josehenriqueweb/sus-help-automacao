package br.com.susautomacao.regras;

import java.util.ArrayList;
import java.util.List;

import br.com.susautomacao.config.Configuracao;
import br.com.susautomacao.model.Procedimento;
import br.com.susautomacao.model.Registro;

public class RegraProcedimento {

    public List<Registro> aplicarFiltro(List<Registro> todosRegistros) {
        List<Registro> registrosValidos = new ArrayList<>();
        int excluidos = 0;

        for (Registro registro : todosRegistros) {
            // Cria um novo registro vazio para o mesmo paciente
            Registro registroFiltrado = new Registro(registro.getPaciente());
            registroFiltrado.setDataAtendimento(registro.getDataAtendimento());
            
            for (Procedimento proc : registro.getProcedimentos()) {
                boolean bloqueado = false;
                
                // Verifica se o código bate com a nossa lista negra na classe Configuracao
                for (String codigoBloqueado : Configuracao.PROCEDIMENTOS_BLOQUEADOS) {
                    if (proc.getCodigo().endsWith(codigoBloqueado)) {
                        bloqueado = true;
                        excluidos++;
                        break;
                    }
                }

                // Se não estiver bloqueado, adiciona na lista válida do paciente
                if (!bloqueado) {
                    registroFiltrado.adicionarProcedimento(proc);
                }
            }

            // Só mandamos o paciente para a digitação se sobrar algum procedimento válido para ele
            if (!registroFiltrado.getProcedimentos().isEmpty()) {
                registrosValidos.add(registroFiltrado);
            }
        }

        System.out.println("-> Procedimentos bloqueados e ignorados pela regra: " + excluidos);
        return registrosValidos;
    }
}