PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Roles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_completo TEXT,
    username TEXT UNIQUE NOT NULL,
    email TEXT, rol_id INTEGER NOT NULL,
    direccion TEXT, fecha_nacimiento TEXT,
    genero TEXT, password TEXT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS Reglas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo_vehiculo TEXT UNIQUE NOT NULL,
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
    user_id INTEGER,

    FOREIGN KEY (estacion_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
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

CREATE VIEW Movimientos AS SELECT id, tipo_vehiculo AS tipo, cantidad, total, fecha, estacion_id, user_id, 'SALIDA' AS tipo_movimiento FROM Transacciones UNION ALL SELECT id, tipo_combustible AS tipo, cantidad, NULL AS total, fecha, estacion_id, NULL AS user_id, 'ENTRADA' AS tipo_movimiento FROM Registros;

CREATE TABLE IF NOT EXISTS Subsidios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER UNIQUE NOT NULL,
    cupo_total INTEGER NOT NULL CHECK(cupo_total > 0),
    saldo_disponible INTEGER NOT NULL CHECK(saldo_disponible >= 0),

    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

INSERT OR IGNORE INTO roles (id, nombre) VALUES
(1, 'Estacion de servicio'),
(2, 'Usuario vehiculo particular'),
(3, 'Autoridad reguladora'),
(4, 'Usuario vehiculo con subsidio'),
(5, 'Distribuidor mayorista'),
(6, 'Administrador de reglas'),
(7, 'Administrador de usuarios');

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Admin Usuarios', 'admin_users', 'admin_users@mail.com', 'admin123', 'Masculino', 'Bogotá', '1990-01-01', 7);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Admin Reglas', 'admin_reglas', 'admin_reglas@mail.com', 'admin123', 'Masculino', 'Bogotá', '1990-01-01', 6);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Estación Norte', 'estacion_1', 'estacion1@mail.com', 'estacion123', 'N/A', 'Bogotá Norte', '2000-01-01', 1);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Estación Sur', 'estacion_2', 'estacion2@mail.com', 'estacion123', 'N/A', 'Bogotá Sur', '2000-01-01', 1);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Autoridad Central', 'autoridad', 'autoridad@mail.com', 'autoridad123', 'N/A', 'Bogotá', '1985-01-01', 3);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Distribuidor Uno', 'distribuidor_1', 'dist1@mail.com', 'distribuidor123', 'N/A', 'Bogotá', '1995-01-01', 5);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Distribuidor Dos', 'distribuidor_2', 'dist2@mail.com', 'distribuidor123', 'N/A', 'Bogotá', '1995-01-01', 5);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Usuario Vehiculo', 'user_vehicle', 'user_vehicle@mail.com', 'user123', 'Masculino', 'Bogotá', '2000-05-15', 2);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, fecha_nacimiento, rol_id)
VALUES
('Juan Subsidio', 'juan_sub', 'juan_sub@mail.com', '123456', 'Masculino', 'Bogotá', '1999-08-20', 4);

INSERT INTO Subsidios (user_id, cupo_total, saldo_disponible)
SELECT id, 100000, 100000 FROM Users WHERE username = 'juan_sub';

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id) VALUES
('Servicio particular', 10000, '2026-03-01', 2);

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Oficiales', 8000, '2026-03-01', 2);

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Diplomáticos', 1000, '2026-03-01', 2);

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Camperos y Cuatrimotos', 12000, '2026-03-01', 2);

INSERT INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Subsidiado', 5000, '2026-03-01', 2);

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

