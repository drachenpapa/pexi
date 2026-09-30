# PEXI – Coding Conventions

Conventions marked **[observed]** are already consistently applied in the codebase.  
Conventions marked **[recommended]** are proposed to fill gaps or resolve inconsistencies; they do not conflict with existing code.

---

## 1. General Coding Principles

**[observed]**

- Prefer simple, readable code over cleverness.
- Prefer explicit code over implicit magic.
- Keep classes small and focused on a single responsibility.
- Do not introduce abstractions or frameworks before there is a concrete need.
- Standard library first; add a dependency only when the standard library is genuinely insufficient.

---

## 2. Project Structure

**[observed]**

```
src/
  main/
    java/de/drachenpapa/pexi/
      PexiApplication.java        ← entry point only
      model/                      ← domain records and enums
      database/                   ← JDBC access, one class per table
      ui/                         ← Swing frames, dialogs, menu
      utils/                      ← cross-cutting utilities (settings, i18n, converters)
    resources/
      messages_en.properties
      messages_de.properties
      logo.png
  test/
    java/de/drachenpapa/pexi/
      utils/                      ← unit tests for converter classes
```

**[recommended]**

- If a service/business-logic layer is added, place it in a new `service/` package under `de.drachenpapa.pexi`.
- Keep the entry-point class (`PexiApplication`) free of logic; it initialises and delegates only.
- Test packages mirror the main source package structure.

---

## 3. Naming Conventions

**[observed]**

| Element | Convention | Example |
|---|---|---|
| Classes | PascalCase | `TransactionConverter`, `DatabaseManager` |
| Methods | camelCase | `createTableModel()`, `saveWindowSettings()` |
| Variables | camelCase | `startBudget`, `currencySelection` |
| Constants | UPPER\_SNAKE\_CASE | `JDBC_URL`, `ACCOUNT_ID` |
| Packages | lowercase | `de.drachenpapa.pexi.database` |
| Test classes | `<ClassUnderTest>Test` | `TransactionConverterTest` |
| Test methods | `should<Behaviour>()` | `shouldConvertCorrectly()`, `shouldHandleEmptyResultSet()` |

**[observed] Class naming patterns**

| Pattern | Convention | Example |
|---|---|---|
| Database access | `<Entity>sDB` (plural + `DB`) | `AccountsDB`, `CategoriesDB` |
| ResultSet converters | `<Entity>Converter` | `AccountConverter` |
| Swing dialogs | `<Name>Dialog` | `SettingsDialog`, `AboutDialog` |

**[observed] Method naming in DB classes**

Use the following verbs consistently across all DB classes:

| Operation | Method name |
|---|---|
| Read all | `get()` |
| Create | `insert(...)` |
| Modify | `update(...)` |
| Delete | `remove(id)` |
| Schema creation | `createTable()` (package-private) |

**[recommended]**

- Parameters that are database IDs should be typed as `int id`, not `String id`, to match the domain model and avoid silent type-conversion errors.
- Parameter names in DB methods should match the column names used in the model fields (e.g. `accountId`, not `account_id` – snake\_case is for SQL only).

---

## 4. Formatting and Style

**[observed]**

- 4-space indentation; no tabs.
- Opening brace `{` on the same line as the declaration.
- Single blank line between methods.
- No trailing whitespace (enforced by `.editorconfig`).
- UTF-8 source encoding.

**[recommended]**

- Prefer **lambdas** over anonymous inner classes for `ActionListener` and similar single-method interfaces. `AboutDialog` already uses this pattern; `SettingsDialog` should be made consistent when touched.
- Avoid wildcard imports (`import java.sql.*`). Use explicit imports.
- One blank line between logically distinct blocks inside a method; no more.

---

## 5. Error Handling

**[observed]**

The current approach uses `ex.printStackTrace()` everywhere. Database errors are silently absorbed; callers receive an empty list or a no-op.

**[recommended]**

Until a structured logging or error-reporting strategy is established, apply the following minimal rules:

- Do **not** swallow exceptions silently. At minimum, print the stack trace (current practice).
- Do **not** catch `Exception` broadly; catch the specific exception type (`SQLException`, `IOException`).
- For operations visible to the user (insert, update, remove), show a `JOptionPane.showMessageDialog` with an error message instead of silently failing.
- When adding new code, do not return empty collections to mask errors unless the calling code explicitly handles the empty case.

---

## 6. Logging

**[observed]**

There is no logging framework in the project. Errors are reported via `ex.printStackTrace()`.

**[recommended]**

- Do not add a logging framework without a clear need. `System.err` / `printStackTrace` is acceptable for a small desktop app.
- If a logging framework is added in the future, use `java.util.logging` (JDK built-in) to avoid a new dependency. Do not add SLF4J or Logback unless the project grows substantially.

---

## 7. Testing

**[observed]**

- Framework: JUnit 5 (`junit-jupiter-api`) + Mockito + Hamcrest.
- Tests live in `src/test/java/` mirroring the main package.
- One test class per production class.
- Test method names use the `should<Behaviour>()` convention.
- Assertions use Hamcrest (`assertThat`, `hasSize`, `is`) alongside `assertAll` for grouping related assertions.
- `ResultSet` is mocked with `Mockito.mock()` called directly on a field (not via `@Mock`).

**[recommended]**

- Use `@ExtendWith(MockitoExtension.class)` on the test class and `@Mock` on mock fields – this is the idiomatic JUnit 5 + Mockito pattern and makes mock lifecycle explicit.
- Always test the empty / null input case alongside the happy path (already done in existing tests – keep this pattern).
- Use `// Given / When / Then` comments to structure test bodies (already used in `AccountConverterTest` – apply consistently).
- Do not test private methods directly; test through the public API.
- New database features should be covered by integration tests using an **H2 in-memory database** (`jdbc:h2:mem:<name>`), which requires no external setup.

---

## 8. Dependency Conventions

**[observed]**

- Dependencies are declared directly in `pom.xml` with explicit versions.
- Test dependencies use `<scope>test</scope>`.
- Compile-time-only dependencies use `<scope>provided</scope>`.
- Renovate is configured to keep dependencies up to date automatically.

**[recommended]**

- Do not add new runtime dependencies without a clear justification. The standard library and existing dependencies cover most needs.
- Do not add Lombok annotations. The project uses Java Records for domain models, which makes Lombok unnecessary. The Lombok dependency should be removed when convenient.
- If a new dependency is required, document the reason in the PR description.

---

## 9. Configuration Conventions

**[observed]**

- **Application settings** (`pexi.properties`): key-value pairs read/written by `Settings`. The file is created automatically with defaults; it is excluded from version control.
- **i18n messages** (`messages_<locale>.properties`): one file per locale in `src/main/resources/`. Keys use dot-separated namespaced names.

**[observed] Message key naming convention**

```
<area>.<element>           → file.menu, file.exit
<area>.<subelement>.<part> → settings.buttons.save, table.column.id
```

**[recommended]**

- All user-visible strings must have a corresponding key in **both** `messages_en.properties` and `messages_de.properties`. Do not hardcode strings directly in UI code.
- Property files for i18n must be saved as **ISO-8859-1** or use `\uXXXX` Unicode escapes for non-ASCII characters. Do not save them as UTF-8 without a `native2ascii` step; this causes corrupted output for German characters.
- Do not store secrets, passwords, or API keys in `pexi.properties` or any committed file.
- Database connection settings are currently hardcoded in `DatabaseManager`. If changed, extract them to a **non-committed** configuration file, not into `pexi.properties`.

---

## 10. Documentation Conventions

**[observed]**

- Javadoc is present on all public and package-private classes and methods.
- Javadoc uses `{@link ClassName}` for cross-references.
- `@param` and `@return` tags are used consistently.

**[recommended]**

- Write Javadoc only where it adds value beyond the method signature. Simple methods like `onCancel()` or `show()` (one line of code) do not need Javadoc.
- Do not write comments that restate the code. Write comments to explain *why*, not *what*.
- Keep Javadoc in English. The codebase is in English; only the UI strings are localised.

---

## 11. Java-Specific Conventions

**[observed]**

- **Java 21** is the target version.
- **Java Records** are used for all domain model types. Do not replace them with classes.
- **`try-with-resources`** is used for all `Connection`, `Statement`, and `ResultSet` objects.
- **`PreparedStatement`** is used for all parameterised SQL queries. Do not use string concatenation to build SQL with user-supplied data.
- **`Locale.forLanguageTag()`** is used for locale parsing (not `new Locale("de")`).

**[recommended]**

- Use **lambdas** instead of anonymous inner classes for functional interfaces (`Runnable`, `ActionListener`, etc.).
- Prefer **`var`** for local variables with obvious types (e.g. `var accounts = new ArrayList<Account>()`), but avoid it when the type is not immediately clear from context.
- Use **`List.of()`** or **`List.copyOf()`** for unmodifiable lists where appropriate.
- Do not use `double` for monetary values in new code. The existing schema uses `DECIMAL(10,2)` in H2; consider using `BigDecimal` in the domain model when the transaction amount field is actively used in calculations.

---

## 12. Swing-Specific Conventions

**[observed]**

- All UI creation and manipulation happens on the **Swing Event Dispatch Thread (EDT)** via `SwingUtilities.invokeLater`.
- Modal dialogs use a static factory method `showDialog(JFrame parent)` that creates, packs, centres, and shows the dialog.
- Dialog configuration is split into focused private methods: `configureXxxButton()`, `configureXxxSelection()`.
- `setDefaultCloseOperation(DO_NOTHING_ON_CLOSE)` + `WindowAdapter` is used in dialogs to control close behaviour.

**[recommended]**

- Do not perform database access on the EDT. Long-running operations should use a `SwingWorker`.
- Keep UI classes free of business logic. If logic is needed (e.g. computing a total), extract it to a utility or service method.
- Dialogs should always have a parent frame passed to `setLocationRelativeTo()` so they are centred correctly.

---

## 13. Things to Avoid

- **No string concatenation in SQL.** Always use `PreparedStatement` with `?` placeholders.
- **No hardcoded user-visible strings in Java code.** Use `Messages.get("key")` instead.
- **No Lombok annotations.** Java Records are used for models; Lombok adds complexity without benefit here.
- **No static mutable global state** beyond the existing `Settings` and `Messages` singletons.
- **No new framework dependencies** (no Spring, no Jakarta EE, no Guice) unless the project scope changes fundamentally.
- **No `SELECT *`** in new queries. Specify columns explicitly.
- **No catching `Exception` broadly.** Catch specific exception types.
- **No `System.exit()` calls outside `MenuBar.exitApplication()`.**
- **Do not call `Messages.get()` before `Messages.initialize()` has been called.** Ensure initialisation order is respected.
- **Do not add fields or methods to model records.** Records are data holders only; add behaviour to converter or service classes.
