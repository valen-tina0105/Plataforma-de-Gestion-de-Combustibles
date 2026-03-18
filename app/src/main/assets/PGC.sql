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

CREATE TABLE IF NOT EXISTS Entregas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    placa TEXT NOT NULL,
    tipo_combustible TEXT NOT NULL,
    cantidad INTEGER NOT NULL CHECK(cantidad > 0),
    fecha TEXT,
    estacion_destino_id INTEGER NOT NULL,
    distribuidor_id INTEGER,

    FOREIGN KEY (estacion_destino_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (distribuidor_id) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE VIEW Movimientos AS SELECT id, tipo_vehiculo AS tipo, cantidad, total, fecha, estacion_id, 'SALIDA' AS tipo_movimiento FROM Transacciones UNION ALL SELECT id, tipo_combustible AS tipo, cantidad, NULL AS total, fecha, estacion_id, 'ENTRADA' AS tipo_movimiento FROM Registros;

INSERT OR IGNORE INTO roles (id, nombre) VALUES
(1, 'Estacion de servicio'),
(2, 'Distribuidor mayorista'),
(3, 'Autoridad reguladora'),
(4, 'Administrador de usuarios'),
(5, 'Administrador de reglas'),
(6, 'Usuario vehiculo particular');

INSERT OR IGNORE INTO Users (username, password, rol_id) VALUES
('admin_users', 'admin123', 4);

INSERT OR IGNORE INTO Users (username, password, rol_id) VALUES
('admin_reglas', 'admin123', 5);

INSERT OR IGNORE INTO Users (username, password, rol_id) VALUES
('estacion_1', 'estacion123', 1);

INSERT OR IGNORE INTO Users (username, password, rol_id) VALUES
('estacion_2', 'estacion123', 1);

INSERT OR IGNORE INTO Users (username, password, rol_id) VALUES
('autoridad', 'autoridad123', 3);

INSERT OR IGNORE INTO Users (username, password, rol_id) VALUES
('distribuidor_1', 'distribuidor123', 2);

INSERT OR IGNORE INTO Users (username, password, rol_id) VALUES
('distribuidor_2', 'distribuidor123', 2);

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id) VALUES
('Servicio particular', 10000, '2026-03-01', 2);

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Oficiales', 8000, '2026-03-01', 2);

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Diplomáticos', 1000, '2026-03-01', 2);

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Camperos y Cuatrimotos', 12000, '2026-03-01', 2);

INSERT INTO Entregas (placa, tipo_combustible, cantidad, fecha, estacion_destino_id, distribuidor_id) VALUES
('ABC123', 'Gasolina Corriente', 1200, '16-03-2026', 3, 6);

INSERT INTO Transacciones (tipo_vehiculo, cantidad, total, fecha, estacion_id) VALUES
('Camperos y Cuatrimotos', 30, 36000, '2026-03-14', 3);

INSERT INTO Transacciones (tipo_vehiculo, cantidad, total, fecha, estacion_id)
VALUES ('Servicio particular', 20, 20000, '2026-03-10', 3);

INSERT INTO Transacciones (tipo_vehiculo, cantidad, total, fecha, estacion_id)
VALUES ('Oficiales', 15, 12000, '2026-03-12', 4);

INSERT INTO Registros (tipo_combustible, cantidad, fecha, estacion_id) VALUES
('Gasolina Corriente', 150, '2026-03-13', 3);

INSERT INTO Registros (tipo_combustible, cantidad, fecha, estacion_id)
VALUES ('Gasolina Extra', 200, '2026-03-09', 4);

INSERT INTO Registros (tipo_combustible, cantidad, fecha, estacion_id)
VALUES ('ACPM(Diésel)', 300, '2026-03-11', 3);