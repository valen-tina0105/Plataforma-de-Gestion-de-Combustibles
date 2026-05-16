# Plataforma de Gestión de Combustibles
[![Android SDK](https://img.shields.io/badge/Android-24%2B-brightgreen.svg)](https://developer.android.com)
[![Version](https://img.shields.io/badge/version-1.0-orange.svg)]()

## Descripción

Aplicación Android nativa desarrollada en Java/Kotlin para la gestión integral de combustibles. Permite a distribuidores, estaciones de servicio y usuarios finales gestionar inventarios, precios, entregas y transacciones de combustible de manera eficiente. La aplicación incluye autenticación de usuarios, validación de precios, gestión de subsidios y seguimiento del historial de operaciones.

## Tabla de Contenidos

- [Características](#características)
- [Tecnologías](#tecnologías)
- [Prerrequisitos](#prerrequisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Licencia](#licencia)

## Características

- **Autenticación de Usuarios**: Sistema de inicio de sesión y registro con diferentes roles (Administrador, Distribuidor, Usuario)
- **Gestión de Precios**: Consulta, validación y actualización de precios de combustibles con reglas de negocio
- **Gestión de Inventarios**: Control de existencias de combustible por estación
- **Gestión de Entregas**: Solicitud, confirmación y seguimiento de entregas de combustible
- **Historial de Transacciones**: Registro completo de todas las operaciones realizadas
- **Calculadora de Precios**: Herramienta para calcular precios finales con subsidios aplicados
- **Gestión de Usuarios**: Creación y administración de usuarios del sistema

## Tecnologías

| Componente | Tecnología | Versión |
|------------|------------|---------|
| Lenguaje | Java | 11 |
| Plataforma | Android | SDK 24-36 |
| Build Tool | Gradle | 9.0.1 |
| Networking | Retrofit | 2.9.0 |
| JSON Parser | Gson | 2.9.0 |
| UI Components | Material Design | 1.13.0 |
| Local Database | SQLite | - |
| Location Services | Google Play Services | 21.2.0 |

## Prerrequisitos

- **JDK**: Java Development Kit 11 o superior
- **Android Studio**: Versión reciente (2023+)
- **Android SDK**: Platform 36, Build Tools
- **Gradle**: Wrapper incluido en el proyecto

## Instalación

1. Clonar el repositorio:

```bash
git clone <repository-url>
cd Plataforma-de-Gestion-de-Combustibles
```

2. Abrir el proyecto en Android Studio:

```bash
# En Android Studio: File > Open > Seleccionar carpeta del proyecto
```

3. Sincronizar dependencias:

```bash
# Android Studio sincronizará automáticamente las dependencias de Gradle
# O desde terminal:
./gradlew dependencies
```

4. Compilar el proyecto:

```bash
./gradlew assembleDebug
```

5. Instalar en dispositivo/emulador:

```bash
./gradlew installDebug
```

## Uso

### Inicio de Sesión

Al iniciar la aplicación, el usuario debe autenticarse con sus credenciales. El sistema validará el rol del usuario y mostrará las opciones correspondientes.

### Roles de Usuario

| Rol | Funcionalidades |
|-----|-----------------|
| Administrador | Gestión de usuarios, precios, reglas y reportes |
| Distribuidor | Solicitud y confirmación de entregas |
| Usuario | Consulta de precios, compras, historial |

### Flujo de Trabajo

1. **Consulta de Precios**: El usuario consulta los precios actuales de combustibles
2. **Compra de Combustible**: El usuario selecciona estación, tipo de combustible y cantidad
3. **Validación de Precio**: El sistema valida el precio según reglas de negocio
4. **Confirmación**: El usuario confirma la transacción
5. **Historial**: La operación queda registrada en el historial

### Ejemplo de Uso - Compra de Combustible

```
1. El usuario inicia sesión
2. Accede a "Compras de Combustible"
3. Selecciona la estación de servicio
4. Elige el tipo de combustible (Regular, Premium, Diesel)
5. Ingresa la cantidad en galones
6. El sistema calcula el precio con subsidios aplicados
7. El usuario confirma la compra
8. Se registra la transacción en la base de datos
```

## Estructura del Proyecto

```
Plataforma-de-Gestion-de-Combustibles/
├── app/
│   ├── src/main/
│   │   ├── java/co/edu/unipiloto/pgc/
│   │   │   ├── dao/           # Objetos de Acceso a Datos
│   │   │   │   ├── DeliveryDAO.java
│   │   │   │   ├── FuelDAO.java
│   │   │   │   ├── InventoryDAO.java
│   │   │   │   ├── PriceDAO.java
│   │   │   │   ├── RuleDAO.java
│   │   │   │   ├── StationDAO.java
│   │   │   │   ├── SubsidyDAO.java
│   │   │   │   ├── TransactionDAO.java
│   │   │   │   ├── UserDAO.java
│   │   │   │   └── MovementDAO.java
│   │   │   ├── database/
│   │   │   │   └── DatabaseHelper.java
│   │   │   ├── model/         # Modelos de Datos
│   │   │   │   ├── Delivery.java
│   │   │   │   ├── Fuel.java
│   │   │   │   ├── Inventory.java
│   │   │   │   ├── Movement.java
│   │   │   │   ├── Price.java
│   │   │   │   ├── Rol.java
│   │   │   │   ├── Station.java
│   │   │   │   ├── Subsidy.java
│   │   │   │   ├── Transaction.java
│   │   │   │   └── User.java
│   │   │   ├── network/       # Servicios de Red
│   │   │   │   ├── ApiService.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── RetrofitClient.java
│   │   │   ├── ui/            # Actividades y UI
│   │   │   │   ├── BaseActivity.java
│   │   │   │   ├── LogInActivity.java
│   │   │   │   ├── SignUpActivity.java
│   │   │   │   ├── FuelDeliveryActivity.java
│   │   │   │   ├── InventoryManagementActivity.java
│   │   │   │   ├── PriceCalculatorActivity.java
│   │   │   │   ├── PriceRulesActivity.java
│   │   │   │   └── ...
│   │   │   └── ui/adapters/   # Adaptadores RecyclerView
│   │   │       ├── DeliveriesAdapter.java
│   │   │       ├── InventoryAdapter.java
│   │   │       ├── PriceManagmentAdapter.java
│   │   │       └── ...
│   │   ├── res/
│   │   │   ├── layout/        # Archivos XML de UI
│   │   │   ├── drawable/     # Recursos gráficos
│   │   │   ├── menu/         # Menús de navegación
│   │   │   └── values/       # Recursos de valores
│   │   └── assets/            # Archivos SQL y datos
│   │       ├── PGC.sql
│   │       ├── Insert*.sql
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── gradle/libs.versions.toml
```
