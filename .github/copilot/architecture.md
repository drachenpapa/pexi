# PEXI – Architecture

## 1. Project Overview

PEXI (Personal Expense Insights) is a **Java 21 desktop application** for personal finance tracking.
It uses **Swing** for the UI and **H2** as an embedded file-based database. There is no server, no network layer, and no external service dependency. The application runs as a single process on the user's machine.

Current status: **work in progress**. Core data model and database layer exist; several UI actions are not yet implemented (see §12).

Build tool: Maven. Packaging: executable JAR, with optional native installers via `jpackage` (see `.github/workflows/executable.yml`).

---

## 2. Main Responsibilities

- Store and display financial transactions (income / expense).
- Manage accounts (name, IBAN, budget) and categories (name, type).
- Persist user-facing settings (window position, locale, currency) across sessions.
- Provide a German and English UI via resource bundles.

---

## 3. High-Level Architecture

```
┌─────────────────────────────────────────────────┐
│                   Swing UI                       │
│  PexiUI  MainWindow  MenuBar  Dialogs            │
└─────────────────────┬───────────────────────────┘
                      │ direct static calls
┌─────────────────────▼───────────────────────────┐
│              Database Layer                      │
│  AccountsDB  CategoriesDB  TransactionsDB        │
│              DatabaseManager                     │
└─────────────────────┬───────────────────────────┘
                      │ JDBC
┌─────────────────────▼───────────────────────────┐
│          H2 (embedded, file-based)               │
│          ./data/pexi  (created at runtime)       │
└─────────────────────────────────────────────────┘

Cross-cutting utilities:
  Settings   – reads/writes pexi.properties (window pos, locale, currency)
  Messages   – ResourceBundle wrapper for i18n (en / de)
  Converters – ResultSet → domain model (Account, Category, Transaction)
```

There is **no service or business-logic layer** between UI and database. The UI calls database classes directly.

---

## 4. Packages and Responsibilities

| Package | Responsibility |
|---|---|
| `de.drachenpapa.pexi` | Entry point only (`PexiApplication.main`) |
| `de.drachenpapa.pexi.model` | Immutable domain records: `Account`, `Category`, `Transaction`, `CategoryType` |
| `de.drachenpapa.pexi.database` | CRUD access to H2; one class per table plus `DatabaseManager` |
| `de.drachenpapa.pexi.ui` | Swing frames, dialogs, menu bar |
| `de.drachenpapa.pexi.utils` | `Settings`, `Messages`, ResultSet converters, `WindowSettingsSaver` |

### `model`

Pure data. Three Java records (`Account`, `Category`, `Transaction`) and one enum (`CategoryType`). No behaviour, no annotations, no Lombok.

### `database`

Four classes, all using **static methods only**:

- `DatabaseManager` – provides `getConnection()` and `executeUpdate(sql, params...)`. Also owns `startUp()` (schema creation + example data seeding).
- `AccountsDB`, `CategoriesDB`, `TransactionsDB` – each provides `createTable()`, `get()`, `insert(...)`, `update(...)`, `remove(id)`.

Schema creation uses `CREATE TABLE IF NOT EXISTS`. Example data is seeded once when the `transactions` table is empty.

Connection handling: each operation opens and closes its own `Connection` (no pooling; acceptable for embedded H2).

### `ui`

- `PexiUI` – creates and shows the main `JFrame`. Wired on the Swing EDT via `SwingUtilities.invokeLater`.
- `MainWindow` – table + toolbar buttons (Add / Remove / Statistics). Component references are expected to be injected by IntelliJ's GUI form designer (`.form` files not present in the repository; components must be initialised externally before use).
- `MenuBar` – File / Edit / Help menus. Edit menu items for accounts and categories exist in the UI but have **no action handlers yet**.
- `SettingsDialog` – modal dialog to change locale and currency. Changes are persisted immediately on save; the app must be restarted for the locale change to take effect.
- `AboutDialog` – modal informational dialog. Displays a static HTML string from the message bundle.

### `utils`

- `Settings` – singleton. Reads/writes `pexi.properties` via `java.util.Properties`. Stores window position, size, locale tag, currency code. Default values are set if the file does not exist.
- `Messages` – static wrapper around `ResourceBundle`. Must be initialised via `Messages.initialize(locale)` before any call to `Messages.get(key)`. Calling `get()` before `initialize()` throws `NullPointerException`.
- `AccountConverter`, `CategoryConverter`, `TransactionConverter` – stateless utility classes; each exposes one `public static List<T> convert(ResultSet)` method. The converter closes the `ResultSet` via `try-with-resources`.
- `WindowSettingsSaver` – `WindowAdapter` that persists window bounds to `Settings` on window close.

---

## 5. Entry Points

| Entry point | Description |
|---|---|
| `PexiApplication.main(String[])` | Application start. Initialises settings, locale, then creates the Swing UI on the EDT. |
| `DatabaseManager.startUp()` | Called implicitly as part of UI initialisation (must be called before any DB operation). **Note:** as of the current code, `startUp()` is defined but the call site in the UI startup sequence is not visible in the codebase – verify before adding new DB operations. |

---

## 6. Data Flow

### Application startup

```
main()
  → Settings.getInstance()          // load pexi.properties (or create defaults)
  → Messages.initialize(locale)     // load messages_<locale>.properties
  → PexiUI.createAndDisplayUI()
      → new PexiUI()
          → new MainWindow()        // creates table + buttons (via form designer)
          → new MenuBar(frame)
          → frame.setSize/Location  // from Settings
          → frame.addWindowListener(WindowSettingsSaver)
      → frame.setVisible(true)
```

### Read transactions (intended flow, not fully wired yet)

```
UI event (button / menu)
  → TransactionsDB.get()
      → DatabaseManager.getConnection()
      → Statement.executeQuery("SELECT * FROM transactions")
      → TransactionConverter.convert(resultSet)
  → List<Transaction> returned to UI for display
```

### Write operation (e.g. insert account)

```
UI event
  → AccountsDB.insert(name, iban, startBudget, budget)
      → DatabaseManager.executeUpdate(sql, params...)
          → DatabaseManager.getConnection()
          → PreparedStatement.executeUpdate()
```

### Settings persistence

```
Window close / Exit menu
  → Settings.saveWindowSettings(x, y, width, height)
      → Properties.store() → pexi.properties

Settings dialog save
  → Settings.saveLocaleAndCurrency(locale, currency)
      → Properties.store() → pexi.properties
```

---

## 7. External Dependencies

| Dependency | Scope | Purpose |
|---|---|---|
| `com.h2database:h2` | runtime | Embedded relational database (file-based) |
| `org.projectlombok:lombok` | provided | **Declared but not used** – models are Java records |
| `org.junit.jupiter:junit-jupiter-api` | test | JUnit 5 test framework |
| `org.mockito:mockito-core` | test | Mocking in unit tests |
| `org.mockito:mockito-junit-jupiter` | test | Mockito JUnit 5 extension |
| `org.hamcrest:hamcrest` | test | Matcher-based assertions |

No Spring, no Jakarta EE, no ORM (Hibernate/JPA). All SQL is plain JDBC.

### GitHub integrations

- **Renovate** – automated dependency updates (inherits config from `drachenpapa/skeletor`).
- **SBOM** – generated on `main` push and releases via `anchore/sbom-action` (CycloneDX JSON).
- **CodeQL** – static analysis (via `dependency-review.yml`).
- **dorny/test-reporter** – publishes JUnit XML results as GitHub check.

---

## 8. Configuration

### Runtime configuration: `pexi.properties`

Stored next to the working directory of the running process (not inside the JAR). Created automatically with defaults on first run. Excluded from version control via `.gitignore`.

| Key | Default | Description |
|---|---|---|
| `x` | 100 | Window x position |
| `y` | 100 | Window y position |
| `width` | 400 | Window width |
| `height` | 300 | Window height |
| `locale` | `en` | BCP 47 language tag |
| `currency` | `EUR` | ISO 4217 currency code |

### Localisation: `messages_<locale>.properties`

Located in `src/main/resources/`. Currently: `messages_en.properties`, `messages_de.properties`. Loaded once at startup via `Messages.initialize(locale)`. A locale change requires an application restart.

**Note:** `messages_de.properties` has encoding issues with non-ASCII characters (umlauts). Java `.properties` files require ISO-8859-1 or `\uXXXX` escape sequences.

### Database

H2 JDBC URL: `jdbc:h2:./data/pexi`  
The `data/` directory is created relative to the process working directory. Credentials are hardcoded in `DatabaseManager` (`admin`/`nimda`). H2 in embedded file mode does not expose a network port by default.

---

## 9. Error Handling

There is no structured error handling strategy at present.

- All `SQLException`s in `DatabaseManager`, `AccountsDB`, `CategoriesDB`, `TransactionsDB` are caught and printed via `ex.printStackTrace()`. The calling code receives an empty list or silent no-op.
- `IOException` in `Settings` is caught the same way.
- There is no user-facing error reporting for database failures.
- `Messages.get()` will throw `NullPointerException` if called before `Messages.initialize()`.

---

## 10. Testing Approach

Tests live in `src/test/java/de/drachenpapa/pexi/utils/`.

Only the three `Converter` classes are tested:

| Test class | What it tests |
|---|---|
| `AccountConverterTest` | `AccountConverter.convert()` – happy path (2 rows), empty ResultSet |
| `CategoryConverterTest` | `CategoryConverter.convert()` – happy path (1 row), empty ResultSet |
| `TransactionConverterTest` | `TransactionConverter.convert()` – happy path (1 row), empty ResultSet |

All tests use Mockito to mock `ResultSet` and Hamcrest matchers for assertions.

**Not tested:** database layer, settings, messages, UI components, startup sequence.

CI runs tests via `mvn -B test -U` on Ubuntu (JDK 21, Temurin). Results are published as a GitHub check via `dorny/test-reporter`.

---

## 11. Key Architectural Decisions

- **Swing over a web UI**: Single-process desktop app; no HTTP server, no browser required.
- **H2 embedded**: No external database installation needed; database lives as a file next to the application.
- **Java Records for domain model**: Immutable, concise, no boilerplate. Preferred over Lombok-annotated classes.
- **Static utility classes for database access**: Simple to call from anywhere; no dependency injection framework. Trade-off: untestable without a real database.
- **`ResourceBundle` for i18n**: Standard library; no third-party i18n framework. Supports EN and DE.
- **`pexi.properties` for window state**: Settings persist via `java.util.Properties` to a plain text file. No registry, no OS-specific API.
- **Converter classes**: ResultSet-to-model mapping is isolated in dedicated converter classes, keeping both the DB classes and model clean.

---

## 12. Known Limitations and Technical Debt

| Issue | Impact |
|---|---|
| `TransactionsDB.update()` is missing the `id` parameter | Bug: every update call throws `SQLException` at runtime |
| `CategoriesDB.insert()` and `update()` reference non-existent column `categoryType` (actual column: `income`) | Bug: insert/update of categories always fails |
| No service/business layer | Business logic has no natural home; UI cannot be tested independently of the database |
| All DB classes use static methods; no interfaces | Cannot mock the database layer in unit tests |
| UI buttons (Add, Remove, Statistics) have no action listeners | Core features are not reachable from the UI |
| Edit menu items for accounts and categories have commented-out action handlers | Accounts and category management are not accessible |
| `messages_de.properties` has encoding issues | German UI displays corrupted characters |
| `about.content` uses a hardcoded file path for the logo image | Logo does not display in packaged JAR |
| Hardcoded DB credentials in `DatabaseManager` | Bad practice; not exploitable in embedded mode but sets a poor pattern |
| `isTableEmpty()` builds SQL via string concatenation | Inconsistent with the rest of the codebase; minor SQL injection risk if ever extended |
| Lombok declared as dependency but not used | Unnecessary dependency |
| `maven-surefire-plugin` not explicitly configured for JUnit 5 | Tests may silently not run with older Maven versions |

---

## 13. What Should Stay Simple

- **Domain model**: Keep using Java Records. Do not introduce Lombok, JPA entities, or inheritance hierarchies for `Account`, `Category`, `Transaction`.
- **Database**: H2 embedded is correct for this application. Do not introduce an ORM or a separate database server.
- **Configuration**: `pexi.properties` via `java.util.Properties` is sufficient. Do not add a configuration framework.
- **i18n**: `ResourceBundle` is enough for two languages. No third-party i18n library is needed.
- **Build**: Plain Maven without plugins beyond what is strictly necessary. The project does not need a plugin-heavy setup.
- **Architecture**: For a personal desktop app of this size, a simple layered structure (UI → optional service → database) is ideal. Do not introduce dependency injection frameworks, event buses, or plugin systems unless there is a clear, concrete need.
