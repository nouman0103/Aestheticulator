<div align="center">

# 📱 Scientific Calculator

### A feature-rich Android scientific calculator with a Casio-inspired UI, built entirely in Java

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![API](https://img.shields.io/badge/Min%20SDK-31%20(Android%2012)-blue?style=for-the-badge&logo=android)](https://developer.android.com/tools/releases/platforms)
[![Gradle](https://img.shields.io/badge/Build-Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org)

</div>

---

## 🎬 Preview

<div align="center">

https://github.com/user-attachments/assets/calculator-preview.mp4

> *Full walkthrough of the calculator's features — arithmetic, scientific functions, cursor navigation, and haptic feedback.*

</div>

---

## ✨ Features

### Core Functionality
- **Full arithmetic support** — addition, subtraction, multiplication, division
- **Scientific functions** — `sin`, `cos`, `tan`, `log`, `ln`, `√`, `x²`, `xⁿ`, `x⁻¹`, fraction entry
- **Parentheses** — nested grouping with `(` and `)`
- **Previous answer recall** — `Ans` key reuses the last computed result
- **Integer truncation** — MODE button converts a decimal result to its integer part instantly

### UX & Polish
- **Blinking cursor** — animated cursor updates every 500ms via a `ScheduledExecutorService`, just like a real calculator
- **Cursor navigation** — dedicated `◀` / `▶` arrow keys let you insert or delete anywhere in the expression
- **Haptic feedback** — every button press triggers a physical vibration (`VIRTUAL_KEY` haptic constant)
- **Dual display** — a top line shows the live expression being built; the bottom line shows the evaluated result
- **Edge-to-Edge UI** — leverages `WindowInsetsCompat` for a modern, immersive full-screen layout

### Expression Engine
- Powered by **[exp4j](https://www.objecthunter.net/exp4j/)** — a battle-tested Java math expression parser
- Expressions are stored as a `List<String>` token list, enabling precise mid-expression insertion and deletion
- Separate display map and evaluation map decouple what the user sees (e.g. `ln(`) from what gets computed (e.g. `log2(`)

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 8 |
| UI Framework | Android Views + ConstraintLayout + GridLayout |
| Expression Parsing | exp4j |
| Build System | Gradle (Kotlin DSL) |
| Min / Target SDK | 31 / 34 (Android 12 – 14) |
| Theme | Custom dark theme (`#16171D` background) with selector-based button states |

---

## 🏗️ Architecture & Design Decisions

```
MainActivity.java
│
├── Expression state         → List<String> tokens + cursor position integer
├── Evaluation pipeline      → token list → evaluatable string → exp4j → double
├── Display pipeline         → token list → display string (with unicode symbols)
├── Cursor rendering         → ScheduledExecutorService (500ms blink loop)
└── Button wiring            → individual click listeners per button group
```

**Why a token list instead of a raw string?**  
Storing each button press as a discrete token makes mid-expression cursor manipulation O(1) — inserting or deleting at any position is a simple `List.add(index, token)` / `List.remove(index)` call, with no string-splitting edge cases.

**Why two maps (`buttonLabelToExpression` + `buttonLabelToDisplay`)?**  
User-facing symbols (e.g. `²`, `√(`, `ln(`) differ from what exp4j expects (`^2`, `sqrt(`, `log2(`). Keeping them in separate `HashMap`s makes the mapping explicit, testable, and easy to extend.

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 8+
- Android device or emulator running **Android 12 (API 31)** or higher

### Build & Run

```bash
# Clone the repository
git clone https://github.com/<your-username>/scientific-calculator-android.git
cd scientific-calculator-android

# Build a debug APK
./gradlew assembleDebug

# Install directly to a connected device
./gradlew installDebug
```

Or simply open the project in **Android Studio** and press **Run ▶**.

---

## 📁 Project Structure

```
app/src/main/
├── java/com/nouman/calculator/
│   └── MainActivity.java          # All app logic — expression engine, UI, cursor
└── res/
    ├── layout/
    │   └── activity_main.xml      # Full calculator layout (Toolbar + display + button grid)
    ├── drawable/
    │   ├── button_background.xml  # Normal button state
    │   ├── button_background_pressed.xml
    │   ├── button_selector.xml    # StateListDrawable wiring normal ↔ pressed
    │   └── screen_background.xml  # Display panel background
    └── values/
        ├── colors.xml             # Dark theme palette
        ├── styles.xml             # Button & label styles
        └── themes.xml
```

---

## 🎨 UI Design

The layout draws inspiration from classic **Casio scientific calculators**, featuring:
- A **SHIFT** (gold) and **ALPHA** (purple) modifier row — a nod to real calculator conventions
- Secondary function labels rendered above each button using overlapping `TextView` + `RelativeLayout`
- A monospace font on the display for precise character alignment
- Rounded button corners and pressed-state feedback via XML drawables

---

## 👤 Author

**M. Nouman Iqbal**  
Student ID: `22L-6671`

---

<div align="center">

*Built as a portfolio project demonstrating Android UI design, custom expression parsing, and attention to UX detail.*

</div>
