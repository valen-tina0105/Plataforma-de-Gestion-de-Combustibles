PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Roles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_completo TEXT,
    username TEXT UNIQUE NOT NULL,
    email TEXT,
    rol_id INTEGER NOT NULL,
    direccion TEXT,
    latitud REAL,
    longitud REAL,
    fecha_nacimiento TEXT,
    genero TEXT,
    password TEXT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS Combustibles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Precios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    precio REAL NOT NULL,
    id_estacion INTEGER NOT NULL,
    id_combustible INTEGER NOT NULL,
    FOREIGN KEY (id_combustible) REFERENCES Combustibles(id),
    FOREIGN KEY (id_estacion) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Inventarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_estacion INTEGER NOT NULL,
    id_combustible INTEGER NOT NULL,
    cantidad_combustible REAL NOT NULL,
    capacidad_maxima REAL NOT NULL,
    nivel_minimo REAL NOT NULL,
    FOREIGN KEY (id_combustible) REFERENCES Combustibles(id),
    FOREIGN KEY (id_estacion) REFERENCES Users(id)
);


CREATE TABLE IF NOT EXISTS Reglas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo_vehiculo TEXT UNIQUE NOT NULL,
    precio REAL NOT NULL CHECK(precio > 0),
    fecha TEXT,
    admin_id INTEGER,
    FOREIGN KEY (admin_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Subsidios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    subsidio INTEGER NOT NULL CHECK(subsidio IN (0,1)),
    porcentaje REAL DEFAULT 0,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Transacciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo_vehiculo TEXT NOT NULL,
    id_combustible INTEGER NOT NULL,
    cantidad REAL NOT NULL CHECK(cantidad > 0),
    total REAL NOT NULL CHECK(total > 0),
    fecha TEXT,
    estacion_id INTEGER,
    user_id INTEGER,
    FOREIGN KEY (id_combustible) REFERENCES Combustibles(id),
    FOREIGN KEY (estacion_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Registros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_combustible INTEGER NOT NULL,
    cantidad REAL NOT NULL CHECK(cantidad > 0),
    fecha TEXT,
    estacion_id INTEGER,
    FOREIGN KEY (id_combustible) REFERENCES Combustibles(id),
    FOREIGN KEY (estacion_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Entregas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    placa TEXT NOT NULL,
    id_combustible INTEGER NOT NULL,
    cantidad REAL NOT NULL CHECK(cantidad > 0),
    fecha TEXT,
    estacion_destino_id INTEGER NOT NULL,
    distribuidor_id INTEGER,
    FOREIGN KEY (id_combustible) REFERENCES Combustibles(id),
    FOREIGN KEY (estacion_destino_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (distribuidor_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE VIEW Movimientos AS SELECT t.id, t.tipo_vehiculo AS tipo, t.id_combustible, t.cantidad, t.total, t.fecha, t.estacion_id, t.user_id, 'SALIDA' AS tipo_movimiento FROM Transacciones t UNION ALL SELECT r.id, NULL AS tipo, r.id_combustible, r.cantidad, NULL AS total, r.fecha, r.estacion_id, NULL AS user_id, 'ENTRADA' AS tipo_movimiento FROM Registros r;
