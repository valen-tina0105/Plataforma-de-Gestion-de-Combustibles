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

INSERT OR IGNORE INTO roles (id, nombre) VALUES
(1, 'Estacion de servicio'),
(2, 'Usuario vehiculo particular'),
(3, 'Autoridad reguladora'),
(4, 'Usuario vehiculo con subsidio'),
(5, 'Distribuidor mayorista'),
(6, 'Administrador de reglas'),
(7, 'Administrador de usuarios');

INSERT OR IGNORE INTO Combustibles (id, nombre) VALUES
(1, 'Gasolina Corriente'),
(2, 'Gasolina Extra'),
(3, 'ACPM(Diésel)'),
(4, 'Gas Natural Vehicular');

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, latitud, longitud, fecha_nacimiento, rol_id)
VALUES
('Admin Usuarios', 'admin_users', 'admin_users@mail.com', 'admin123', 'Masculino', 'Bogotá', 4.6097, -74.0817, '1990-01-01', 7);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, latitud, longitud, fecha_nacimiento, rol_id)
VALUES
('Admin Reglas', 'admin_reglas', 'admin_reglas@mail.com', 'admin123', 'Masculino', 'Bogotá', 4.6097, -74.0817, '1990-01-01', 6);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, latitud, longitud, fecha_nacimiento, rol_id)
VALUES
('Estación Norte', 'estacion_1', 'estacion1@mail.com', 'estacion123', 'N/A', 'Bogotá Norte', 4.7110, -74.0721, '2000-01-01', 1);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, latitud, longitud, fecha_nacimiento, rol_id)
VALUES
('Estación Sur', 'estacion_2', 'estacion2@mail.com', 'estacion123', 'N/A', 'Bogotá Sur', 4.5708, -74.1302, '2000-01-01', 1);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, latitud, longitud, fecha_nacimiento, rol_id)
VALUES
('Autoridad Reguladora', 'autoridad', 'autoridad@mail.com', 'autoridad123', 'N/A', 'Bogotá', 4.6097, -74.0817, '1985-01-01', 3);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, latitud, longitud, fecha_nacimiento, rol_id)
VALUES
('Distribuidor Uno', 'distribuidor_1', 'dist1@mail.com', 'distribuidor123', 'N/A', 'Bogotá', 4.6097, -74.0817, '1995-01-01', 5);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, latitud, longitud, fecha_nacimiento, rol_id)
VALUES
('Distribuidor Dos', 'distribuidor_2', 'dist2@mail.com', 'distribuidor123', 'N/A', 'Bogotá', 4.6097, -74.0817, '1995-01-01', 5);

INSERT OR IGNORE INTO Users
(nombre_completo, username, email, password, genero, direccion, latitud, longitud, fecha_nacimiento, rol_id)
VALUES
('Usuario Vehiculo', 'user_vehicle', 'user_vehicle@mail.com', 'user123', 'Masculino', 'Calle 69A #91-24, Engativá, Bogotá', 4.7086, -74.1182, '2000-05-15', 2);

INSERT OR IGNORE INTO Reglas (tipo_vehiculo, precio, fecha, admin_id) VALUES
('Servicio particular', 10000, '2026-03-01', 2);

INSERT OR IGNORE INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Oficiales', 8000, '2026-03-01', 2);

INSERT OR IGNORE INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Diplomáticos', 1000, '2026-03-01', 2);

INSERT OR IGNORE INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Camperos y Cuatrimotos', 12000, '2026-03-01', 2);

INSERT OR IGNORE INTO Reglas (tipo_vehiculo, precio, fecha, admin_id)
VALUES ('Subsidiado', 5000, '2026-03-01', 2);

INSERT OR IGNORE INTO Entregas (placa, id_combustible, cantidad, fecha, estacion_destino_id, distribuidor_id) VALUES
('ABC123', 1, 1200, '2026-03-16', 3, 6);

INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES
('Camperos y Cuatrimotos', 1, 30, 36000, '2026-03-14', 3, 8),
('Servicio particular', 2, 20, 20000, '2026-03-10', 3, 8),
('Oficiales', 1, 15, 12000, '2026-03-12', 4, 8);

INSERT OR IGNORE INTO Registros (id_combustible, cantidad, fecha, estacion_id) VALUES
(1, 150, '2026-03-13', 3),
(2, 200, '2026-03-09', 4),
(3, 300, '2026-03-11', 3);

INSERT OR IGNORE INTO Inventarios (id_estacion, id_combustible, cantidad_combustible, capacidad_maxima, nivel_minimo) VALUES
(3, 1, 9000, 10000, 1000),
(3, 2, 3000, 8000, 800),
(3, 3, 7000, 12000, 1500),
(3, 4, 2000, 5000, 500);

INSERT OR IGNORE INTO Inventarios (id_estacion, id_combustible, cantidad_combustible, capacidad_maxima, nivel_minimo) VALUES
(4, 1, 1000, 15000, 5000),
(4, 2, 6000, 7000, 100),
(4, 3, 12000, 13000, 500),
(4, 4, 7000, 9000, 200);

INSERT OR IGNORE INTO Precios (precio, id_estacion, id_combustible) VALUES
(15000, 3, 1),
(20000, 3, 2),
(10000, 3, 3),
(2000, 3, 4);

INSERT OR IGNORE INTO Precios (precio, id_estacion, id_combustible) VALUES
(16000, 4, 1),
(22000, 4, 2),
(11000, 4, 3),
(3000, 4, 4);
