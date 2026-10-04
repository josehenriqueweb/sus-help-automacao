# 🏥 Automação SUS Help (Web Scraping & RPA)

Robô desenvolvido em **Java** para automatizar a leitura de relatórios médicos em PDF e realizar o cadastro em massa de pacientes e procedimentos no sistema web "SUS Help".

## 🚀 O Problema
O cadastro manual de centenas de procedimentos médicos mensais gerava horas de trabalho repetitivo, além de estar sujeito a erros humanos na digitação de códigos CNS e datas.

## 💡 A Solução
Desenvolvi uma automação (RPA) que realiza duas etapas:
1. **Extração de Dados:** Utiliza `Apache PDFBox` e `Regex` avançado para ler um PDF estruturado, identificando Pacientes (Nome, CNS) e seus respectivos Procedimentos e Datas, lidando com quebras de linha e caracteres especiais.
2. **Navegação Automatizada:** Utiliza `Playwright para Java` para assumir o controle do navegador, preenchendo formulários dinâmicos, lidando com pop-ups e salvando os registros em loop de forma inteligente.

## 🛠️ Tecnologias Utilizadas
* **Java 17** (Linguagem principal)
* **Playwright** (Automação web/Navegador)
* **Apache PDFBox** (Leitura e manipulação de PDFs)
* **Expressões Regulares (Regex)** (Tratamento de strings complexas)
* **Maven** (Gerenciamento de dependências)

*Nota: Os dados e PDFs utilizados nos testes locais contêm informações fictícias ou foram ignorados via `.gitignore` para proteger dados sensíveis de pacientes (LGPD).*