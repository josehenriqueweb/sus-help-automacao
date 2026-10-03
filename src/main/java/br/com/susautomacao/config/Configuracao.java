package br.com.susautomacao.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Configuracao {
    
    // Constantes de preenchimento padrão
    public static final String MEDICA_PADRAO = "Dra. Larissa";
    public static final String RACA_PADRAO = "Parda";
    
    // Usamos um Set (conjunto) porque é extremamente rápido para o Java verificar se um item existe lá dentro.
    // Aqui colocamos o código que já sabemos que não entra.
    public static final Set<String> PROCEDIMENTOS_BLOQUEADOS = new HashSet<>(Arrays.asList(
        "0205020186",
        "0205020062"
        // Quando confirmar mais procedimentos bloqueados, basta adicionar aqui:
        // "0205020168", "0205020100", "0205020182"
    ));
    
    // Construtor privado para evitar que alguém tente instanciar esta classe (ela só guarda constantes)
    private Configuracao() {}
}