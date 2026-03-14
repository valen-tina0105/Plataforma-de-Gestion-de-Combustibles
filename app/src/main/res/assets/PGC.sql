PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Roles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    rol_id INTEGER NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS Reglas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo_vehiculo TEXT NOT NULL,
    precio INTEGER NOT NULL CHECK(precio > 0),
    fecha TEXT,
    admin_id INTEGER,
    FOREIGN KEY (admin_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Transacciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo_vehiculo TEXT NOT NULL,
    cantidad INTEGER NOT NULL CHECK(cantidad > 0),
    total INTEGER NOT NULL CHECK(total > 0),
    fecha TEXT,
    estacion_id INTEGER,
    FOREIGN KEY (estacion_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Registros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo_combustible TEXT NOT NULL,
    cantidad INTEGER NOT NULL CHECK(cantidad > 0),
    fecha TEXT,
    estacion_id INTEGER,
    FOREIGN KEY (estacion_id) REFERENCES Users(id) ON DELETE CASCADE
);

INSERT OR IGNORE INTO roles (id, nombre) VALUES
(1, 'distribuidor_mayorista'),
(2, 'estacion_de_servicio'),
(3, 'autoridad_reguladora'),
(4, 'admin_usuarios'),
(5, 'admin_reglas');