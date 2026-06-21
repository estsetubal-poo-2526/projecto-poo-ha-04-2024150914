# Frogi 🐸 - Jogo Desktop em JavaFX

Este projeto consiste num jogo desktop de estilo arcade desenvolvido em Java com a biblioteca gráfica JavaFX. O trabalho foi realizado no âmbito da unidade curricular de **Programação Orientada a Objetos (POO)** do ano letivo 2025/2026.

O objetivo principal do jogo é guiar o Sapo até à Princesa no nível final, sobrevivendo aos perigos, rios e predadores inteligentes, enquanto se colecionam grilos e *power-ups* pelo caminho.

---

## 🛠️ Pré-requisitos e Tecnologias

Para compilar e executar o projeto, é necessário ter instalado:
* **Java Development Kit (JDK):** Versão 21.
* **JavaFX SDK:** Compatível com a versão do JDK 21.
* **Ambiente de Desenvolvimento (IDE):** IntelliJ IDEA

---

## 🚀 Como Executar o Jogo

### Através do IntelliJ IDEA
1. Abra o IntelliJ IDEA e selecione **Open**.
2. Navegue até à pasta raiz do projeto e selecione-a.
3. Garanta que o SDK do projeto está definido para o Java 21 nas configurações (`File > Project Structure`).
4. Na árvore de ficheiros à esquerda, navegue pelo caminho: `src/main/java/org.frogi/`.
5. Localize e abra a classe **`Launcher.java`**.
6. Clique com o botão direito em qualquer zona do código da classe e selecione **Run 'Launcher.main()'**.


> 💡 **Nota Técnica:** O uso da classe `Launcher` é obrigatório neste projeto estruturado em Maven para contornar as restrições de inicialização de módulos nativos do JavaFX 11+ quando executado diretamente de uma classe que estende `Application`.