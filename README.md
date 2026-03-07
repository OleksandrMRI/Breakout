# Breakout Game (Java Implementation)

A classic arcade game project developed as part of the Java Programming course. This implementation features smooth ball physics, paddle movement, and brick-breaking mechanics.

## Key Features
- Collision detection and physics-based ball movement.
- Dynamic game environment with multiple brick layers.
- User-friendly graphical interface powered by a dedicated library.

## Dependencies
This project uses an external graphical library provided by the **SHPP Programming School**:
* **shpp-cs-java-lib.jar** — A specialized toolkit for rendering and basic UI elements.

> **Note:** The library is included in the `lib/` directory to ensure the project is self-contained and can be run immediately after cloning.

## Technical Requirements & Setup
To run this project correctly, please ensure your environment matches the following configuration:

### 1. Java SDK
- **Version:** Java 18 (OpenJDK 18 or Oracle JDK 18).
- *Note:* Using a different version may cause compatibility issues with the pre-compiled library.

### 2. IDE Configuration (IntelliJ IDEA recommended)
- **Project SDK:** Set to Java 18 in `File` -> `Project Structure` -> `Project`.
- **Language Level:** Set to `18 - No new language features`.
- **Compiler Output:** Ensure the output path is set to the `out` directory in your project root (`File` -> `Project Structure` -> `Project` -> `Project compiler output`).

### 3. Dependencies
- Add the library manually: `File` -> `Project Structure` -> `Libraries` -> `+` -> Select `lib/shpp-cs-java-lib.jar`.

## How to Run
1. Clone the repository.
2. Apply the settings mentioned above.
3. Locate `src/Breakout.java`.
4. Right-click and select **Run 'Breakout.main()'**.

---
*Developed during the curriculum of SHPP Programming School.*