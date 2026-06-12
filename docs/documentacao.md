# Documentação Técnica — Projeto Frogi

Este documento descreve a estrutura de pacotes, a organização da arquitetura e as responsabilidades de cada classe que compõe o jogo **Frogi**, desenvolvido em Java com suporte gráfico JavaFX.

A aplicação segue o padrão arquitetural **MVC (Model-View-Controller)** para garantir a separação de conceitos, modularidade e facilidade de testes.

---

## 1. Estrutura de Pacotes (Arquitetura)

O código-fonte está organizado sob o pacote principal `org.frogi`, ramificando-se da seguinte forma:

* `org.frogi.model`: Classes que contêm o estado do jogo e as regras de negócio (independente da interface gráfica).
* `org.frogi.model.exceptions`: Exceções personalizadas para o controlo de erros do domínio.
* `org.frogi.controller`: Controladores que gerem o fluxo, o ciclo de tempo (Game Loop) e os eventos de entrada.
* `org.frogi.view`: Telas, menus e componentes visuais construídos em JavaFX.

---

## 2. Descrição das Classes

### Camada do Modelo (`org.frogi.model`)

* **`EntidadeJogo` (Abstrata):** Classe base para todos os objetos que existem no mapa do jogo. Define as coordenadas `(x, y)` lógicas e o método abstrato `interagir()`.
* **`Sapo`:** Representa o protagonista. Controla o estado de crescimento (`FaseSapo`), contagem de vidas, grilos consumidos e a lógica de transição entre estar vivo ou morto.
* **`FaseSapo` (Enum):** Define os três estados de evolução do sapo: `PEQUENO`, `MEDIO` e `GRANDE`.
* **`Predador` (Raposa):** Entidade inimiga que se move automaticamente e retira uma vida ao sapo em caso de colisão.
* **`Grilo`:** Item colecionável que aumenta a pontuação do jogador e faz o sapo evoluir.
* **`Princesa`:** Objetivo final do jogo, posicionada no fim do Nível 3 para desencadear a vitória.
* **`PowerUp` (Abstrata), `VidaExtra` e `Salto`:** Itens especiais que concedem vantagens temporárias ou permanentes ao sapo ao serem recolhidos.
* **`Mapa`:** Gere a grelha lógica do nível atual, armazenando a lista de entidades ativas e processando as colisões.
* **`Partida`:** Coordena o estado global da sessão atual (pontuação acumulada, nível ativo, controlo de pausa e transições de fase).
* **`Jogador`:** Guarda o nome do jogador.
* **`ResultadoPartida`:** Representa uma linha da tabela de classificação, associando um jogador à pontuação obtida.
* **`Leaderboard`:** Gestor de persistência. Carrega e grava as pontuações no ficheiro local `leaderboard.txt`.

### Camada de Exceções (`org.frogi.model.exceptions`)

* **`NomeInvalidoException`:** Lançada se o jogador tentar registar-se com um nome vazio, nulo ou com caracteres inválidos.
* **`PosicaoInvalidaException`:** Lançada pelo motor de jogo se uma entidade tentar mover-se para fora dos limites ou para uma coordenada inacessível.
* **`EstadoSapoInvalidoException`:** Disparada se houver uma tentativa de interagir ou mover o sapo quando o seu estado lógico já é "morto".

### Camada de Controlo (`org.frogi.controller`)

* **`JogoController`:** O motor principal do jogo. Captura os eventos do teclado (setas direcionais), executa a `Timeline` (Game Loop) que atualiza o modelo a cada ciclo e ordena o redesenho do ecrã.
* **`SomController`:** Centraliza a reprodução de efeitos sonoros (saltos, colisões) e da música de fundo, garantindo que apenas uma instância controla o áudio do sistema.

### Camada da Interface Gráfica (`org.frogi.view`)

* **`Main` / `Launcher`:** Classes de inicialização que isolam o ciclo de vida do JavaFX e arrancam a aplicação.
* **`MenuScreen`, `ComoJogarScreen`, `OpcoesScreen`:** Ecrãs de interface de navegação do utilizador.
* **`LeaderboardScreen`:** Renderiza graficamente a tabela com as 10 melhores pontuações extraídas do ficheiro de texto.
* **`JogoScreen`:** Painel principal do jogo. Transforma as coordenadas lógicas do `Mapa` em elementos gráficos (`ImageView`), desenha o cenário e atualiza o HUD (barra de vidas e pontos) em tempo real.
* **`GameOverScreen` / `VitoriaScreen`:** Telas de desfecho da partida com opções para reiniciar ou voltar ao menu.

---

## 3. Testes Unitários (`src/test/java`)

O projeto inclui diversos testes automatizados com **JUnit** para validar o comportamento isolado do modelo. As classes de teste (`SapoTest`, `MapaTest`, `PartidaTest`, `LeaderboardTest`, etc.) verificam cenários de movimentação, colisões, regras de eliminação e a correta escrita em ficheiros temporários de teste sem corromper os dados reais da aplicação.