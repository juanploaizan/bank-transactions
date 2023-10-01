# Sistema de Transacciones Bancarias

Este repositorio contiene una aplicación de simulación de un sistema de transacciones bancarias básico. La aplicación permite a los usuarios abrir cuentas bancarias, realizar depósitos en sus cuentas, transferir dinero entre cuentas y administrar bolsillos (subcuentas asociadas a una cuenta principal). Además, cuenta con autorización mediante JWT a través de Spring Security. A continuación, se proporciona información detallada sobre cómo se abordó este problema, las tecnologías utilizadas y cómo utilizar el servicio.

## Problema

Los sistemas de transacciones bancarias son esenciales para la gestión de movimientos de dinero entre cuentas y entidades financieras. Este proyecto tiene como objetivo simular un sistema de transacciones bancarias que permita a los usuarios realizar las siguientes acciones:

- Abrir una cuenta bancaria.
- Realizar depósitos en cuentas.
- Transferir dinero entre cuentas.
- Crear bolsillos asociados a cuentas principales.
- Realizar transferencias de dinero a los bolsillos.

## Solución

La solución se desarrolló utilizando las siguientes tecnologías:

- Spring Boot 3.1.4 como framework de desarrollo.
- Java 17 como lenguaje de programación.
- PostgreSQL como base de datos para almacenar la información de las cuentas y los bolsillos.
- Swagger para documentar la API.
- Spring Security para la autorización basada en JWT.
- AWS como plataforma de contenedor virtual para implementar la aplicación.

## Uso del Servicio

### Cuentas

#### Apertura de Cuentas

Para abrir una cuenta bancaria, haga una solicitud POST a `/api/accounts` con el siguiente cuerpo JSON:

```json
{
  "owner_name": "Nombre del Propietario",
  "initial_balance": 1000
}
```

#### Depósitos en Cuentas

Para realizar un depósito en una cuenta existente, haga una solicitud POST a `/api/accounts/{account_number}/deposit` con el siguiente cuerpo JSON:

```json
{
  "amount": 5000
}
```

#### Transferencias entre Cuentas

Para realizar una transferencia de dinero entre cuentas, haga una solicitud POST a `/api/accounts/transfer` con el siguiente cuerpo JSON:

```json
{
  "source_account_number": "CuentaOrigen",
  "destination_account_number": "CuentaDestino",
  "amount": 2000
}
```

#### Consultar Cuenta

Para consultar los detalles y el saldo actual de una cuenta específica, haga una solicitud GET a `/api/accounts/{account_number}`.

### Bolsillos

#### Creación de Bolsillos

Para crear un bolsillo (subcuenta) asociado a una cuenta principal, haga una solicitud POST a `/api/pockets` con el siguiente cuerpo JSON:

```json
{
  "account_number": "CuentaPrincipal",
  "name": "Nombre del Bolsillo",
  "initial_balance": 2000
}
```

#### Transferencias a Bolsillos

Para transferir dinero desde la cuenta principal a un bolsillo existente, haga una solicitud POST a `/api/pockets/transfer` con el siguiente cuerpo JSON:

```json
{
  "account_number": "CuentaPrincipal",
  "pocket_number": "Bolsillo",
  "amount": 1000
}
```

#### Consulta de Bolsillos

Para obtener una lista de los bolsillos asociados a una cuenta específica, haga una solicitud GET a `/api/accounts/{account_number}/pockets`.

Ejemplo de respuesta:

```json
[
  {
    "name": "Vacaciones",
    "pocket_number": "P001",
    "amount": 50000
  },
  {
    "name": "Impuestos",
    "pocket_number": "P002",
    "amount": 25000
  }
]
```

## Autorización por JWT

La aplicación implementa la autorización mediante JWT a través de Spring Security. A continuación, se describen los endpoints relacionados con la autorización:

#### Registro de Usuario

Para registrar un nuevo usuario, haga una solicitud POST a `/api/auth/signup` con el siguiente cuerpo JSON:

```json
{
  "username": "Nombre de Usuario",
  "email": "correo@example.com",
  "password": "Contraseña",
  "roles": ["user"]
}
```

#### Cierre de Sesión

Para cerrar la sesión de un usuario autenticado, haga una solicitud POST a `/api/auth/signout`.

#### Autenticación de Usuario

Para autenticar a un usuario y obtener un token JWT, haga una solicitud POST a `/api/auth/signin` con el siguiente cuerpo JSON:

```json
{
  "username": "Nombre de Usuario",
  "password": "Contraseña"
}
```

El token JWT se incluirá en la respuesta y se utilizará para autorizar las solicitudes a los otros endpoints protegidos de la aplicación. Asegúrese de incluir el token en el encabezado `Authorization` de sus solicitudes a dichos endpoints.

## Configuración del Proyecto

El proyecto utiliza Gradle como sistema de construcción y gestión de dependencias. Asegúrese de tener instalado Gradle antes de continuar. Puede construir el proyecto y ejecutarlo localmente siguiendo estos pasos:

1. Clone este repositorio a su máquina local utilizando el siguiente comando:

   ```shell
   git clone https://github.com/tu-usuario/nombre-del-repositorio.git
   ```

2. Asegúrese de tener Gradle instalado. Si no lo tiene, puede descargarlo e instalarlo desde [Gradle's website](https://gradle.org/install/).

3. Configure una base de datos PostgreSQL y actualice la configuración de conexión en `src/main/resources/application.properties` con la información de su base de datos.

4. Ejecute la aplicación utilizando el siguiente comando desde la raíz del proyecto:

   ```shell
   ./gradlew bootRun
   ```

5. La aplicación estará disponible en `http://localhost:8080`. Puede acceder a la documentación de la API a través de Swagger en `http://localhost:8080/swagger-ui.html`.

Con estas instrucciones, debería poder instalar y ejecutar la aplicación en su entorno local.
