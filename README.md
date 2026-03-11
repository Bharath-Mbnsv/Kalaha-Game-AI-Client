# 🪨 Kalaha Game AI Client

An intelligent AI client for the ancient board game **Kalaha**, powered by **Minimax search with Iterative Deepening and Alpha-Beta Pruning** — engineered to select the optimal move within a strict 5-second time limit.

---

## 🎯 Overview

Kalaha (also known as Mancala) is a two-player strategy game involving seeds and pits. This project implements a competitive AI client that connects to a Kalaha game server and plays autonomously, using adversarial search techniques to maximize winning outcomes against any opponent.

The AI is built to:
- Evaluate millions of game states efficiently within the time constraint
- Prune irrelevant branches to search deeper in the same time
- Adapt search depth dynamically using iterative deepening
- Apply strong heuristics to evaluate non-terminal board positions

---

## 🏺 Kalaha Rules

The board consists of two rows of **6 pits** and one **store (Kalaha)** per player.

| Element      | Description                                                        |
|--------------|--------------------------------------------------------------------|
| 🟤 Pit       | Each player has 6 pits, starting with an equal number of seeds    |
| 🏆 Store     | Seeds captured go here; most seeds at game end wins               |
| ➡️ Sowing    | Seeds are distributed counter-clockwise, one per pit              |
| 🔄 Extra Turn | Landing in your own store earns a bonus turn                     |
| 🫴 Capture   | Landing in an empty own pit captures opposite pit's seeds         |

The player with the most seeds in their store when one side is empty wins.

---

## 🧩 Features

- **Minimax Algorithm** — full adversarial game tree search
- **Alpha-Beta Pruning** — eliminates branches that cannot affect the final decision, dramatically increasing search efficiency
- **Iterative Deepening** — progressively deepens search until the time limit is reached, always returning the best move found so far
- **5-Second Time Budget** — guaranteed move selection within the server's time constraint
- **Board Evaluation Heuristic** — scores non-terminal states based on seed differential, store counts, and extra-turn opportunities
- **Server Communication** — connects to a Kalaha game server via socket/network protocol

---

## 🗂️ Project Structure

```
├── src/
│   ├── KalahaClient.java       # Main client — connects to game server, drives game loop
│   ├── MiniMax.java            # Minimax + Alpha-Beta Pruning + Iterative Deepening
│   ├── Board.java              # Board state representation and move logic
│   ├── Heuristic.java          # Board evaluation function
│   └── ...
├── build/                      # Compiled class files
├── dist/                       # Distributable JAR
├── nbproject/                  # NetBeans project configuration
├── build.xml                   # Ant build script
├── manifest.mf                 # JAR manifest
└── README.md
```

---

## ⚙️ Installation & Setup

### Prerequisites

- Java JDK 8+
- A running Kalaha game server
- Apache Ant **or** NetBeans IDE

### Clone the Repository

```bash
git clone https://github.com/Bharath-Mbnsv/Kalaha-Game-AI-Client.git
cd Kalaha-Game-AI-Client
```

### Build with Ant

```bash
ant build
```

### Run the Client

```bash
java -jar dist/KalahaClient.jar <server_host> <port>
```

Replace `<server_host>` and `<port>` with your game server's address and port number.

### Open in NetBeans

Open the project folder in **NetBeans IDE** — it will auto-detect the `nbproject/` configuration for a one-click build and run.

---

## 🤖 AI — How It Works

### Minimax with Alpha-Beta Pruning

```
          MAX (AI)
         /    |    \
       MIN   MIN   MIN      ← Opponent's responses
      / \   / \   / \
    MAX MAX ...            ← AI's counter-moves
```

The AI assumes the opponent always plays optimally (minimizing AI's score) and searches for the move that maximizes the AI's outcome in the worst case.

**Alpha-Beta Pruning** cuts branches where:
- `α >= β` — the current path cannot possibly be chosen by a rational opponent

This allows the AI to search **significantly deeper** in the same amount of time compared to plain Minimax.

### Iterative Deepening

```
Round 1: Search depth 1  → best move found
Round 2: Search depth 2  → best move updated
Round 3: Search depth 3  → best move updated
  ...
Round N: Time limit hit  → return best move so far
```

Each round restarts from depth 1, ensuring the AI always has a valid best move ready — even if the time limit is hit mid-search. This makes it **both time-safe and depth-optimal**.

### Evaluation Heuristic

For non-terminal board states, the heuristic considers:
- **Store difference** — AI's store count minus opponent's store count
- **Seed advantage** — total seeds on AI's side vs. opponent's side
- **Extra turn opportunities** — positions that would grant a bonus move
- **Capture potential** — seeds at risk of being captured

---

## 📈 Performance

| Technique              | Benefit                                      |
|------------------------|----------------------------------------------|
| Alpha-Beta Pruning     | Reduces nodes evaluated by up to ~50–90%    |
| Iterative Deepening    | Guarantees best available move within 5s     |
| Heuristic Evaluation   | Enables informed pruning at shallow depths   |

---

## 🔮 Future Improvements

- **Transposition Table** — cache previously evaluated board states to avoid re-computation
- **Move Ordering** — evaluate likely-good moves first to improve pruning efficiency
- **Monte Carlo Tree Search (MCTS)** — explore as an alternative to deterministic Minimax
- **Opening Book** — hardcode optimal early-game moves for common openings
- **Self-play training** — use reinforcement learning to improve the evaluation heuristic

---

## 📚 References

- Russell, S. & Norvig, P. — *Artificial Intelligence: A Modern Approach* (Chapter 5 — Adversarial Search)
- Knuth & Moore (1975) — *An Analysis of Alpha-Beta Pruning*
- Korf, R. (1985) — *Depth-First Iterative-Deepening*

---

## 🤝 Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request
