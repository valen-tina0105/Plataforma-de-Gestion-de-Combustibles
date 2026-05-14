-- Transacciones para las 15 nuevas estaciones
-- Estación 1: Terpel Zarzamora
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Servicio particular', 1, 10, 156000, '2026-03-20', (SELECT id FROM Users WHERE username = 'terpel_zarzamora'), (SELECT id FROM Users WHERE username = 'juan_perez'));
-- Estación 2: EDS Hergos Petrobras
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Oficiales', 3, 20, 191000, '2026-03-21', (SELECT id FROM Users WHERE username = 'eds_hergos_petrobras'), (SELECT id FROM Users WHERE username = 'maria_garcia'));
-- Estación 3: Terpel Engativa
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Diplomáticos', 2, 15, 313500, '2026-03-22', (SELECT id FROM Users WHERE username = 'terpel_engativa'), (SELECT id FROM Users WHERE username = 'carlos_rod'));
-- Estación 4: Biomax Inverniza
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Servicio particular', 1, 12, 187800, '2026-03-20', (SELECT id FROM Users WHERE username = 'biomax_inverniza'), (SELECT id FROM Users WHERE username = 'ana_mtz'));
-- Estación 5: EDS Terpel El Dorado
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Camperos y Cuatrimotos', 3, 25, 239500, '2026-03-21', (SELECT id FROM Users WHERE username = 'eds_terpel_eldorado'), (SELECT id FROM Users WHERE username = 'luis_sanchez'));
-- Estación 6: Shell La Española
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Subsidiado', 4, 10, 25000, '2026-03-22', (SELECT id FROM Users WHERE username = 'shell_la_espanola'), (SELECT id FROM Users WHERE username = 'elena_gomez'));
-- Estación 7: Terpel Av Boyacá
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Servicio particular', 1, 8, 124960, '2026-03-20', (SELECT id FROM Users WHERE username = 'terpel_av_boyaca'), (SELECT id FROM Users WHERE username = 'pedro_lopez'));
-- Estación 8: EDS ESSO Quirigua
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Oficiales', 2, 12, 249480, '2026-03-21', (SELECT id FROM Users WHERE username = 'eds_esso_quirigua'), (SELECT id FROM Users WHERE username = 'lucia_diaz'));
-- Estación 9: Primax Calle 80 Norte
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Diplomáticos', 3, 15, 146250, '2026-03-22', (SELECT id FROM Users WHERE username = 'primax_calle80_norte'), (SELECT id FROM Users WHERE username = 'jorge_torres'));
-- Estación 10: Primax Calle 80 Sur
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Servicio particular', 1, 9, 140760, '2026-03-20', (SELECT id FROM Users WHERE username = 'primax_calle80_sur'), (SELECT id FROM Users WHERE username = 'sofia_castro'));
-- Estación 11: EDS Texaco Bloodberry
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Oficiales', 3, 18, 172260, '2026-03-21', (SELECT id FROM Users WHERE username = 'eds_texaco_bloodberry'), (SELECT id FROM Users WHERE username = 'juan_perez'));
-- Estación 12: EDS Texaco La Granja
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Camperos y Cuatrimotos', 2, 14, 289100, '2026-03-22', (SELECT id FROM Users WHERE username = 'eds_texaco_lagranja'), (SELECT id FROM Users WHERE username = 'maria_garcia'));
-- Estación 13: Terpel Calle 49
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Servicio particular', 1, 11, 172480, '2026-03-20', (SELECT id FROM Users WHERE username = 'terpel_calle49'), (SELECT id FROM Users WHERE username = 'carlos_rod'));
-- Estación 14: EDS Terpel Álamos
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Oficiales', 3, 22, 209880, '2026-03-21', (SELECT id FROM Users WHERE username = 'eds_terpel_alamos'), (SELECT id FROM Users WHERE username = 'ana_mtz'));
-- Estación 15: Primax Normandía
INSERT INTO Transacciones (tipo_vehiculo, id_combustible, cantidad, total, fecha, estacion_id, user_id) VALUES ('Subsidiado', 4, 8, 21280, '2026-03-22', (SELECT id FROM Users WHERE username = 'primax_normandia'), (SELECT id FROM Users WHERE username = 'luis_sanchez'));
