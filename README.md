# CyberAuditPro - Módulo móvil Android (Login)

Aplicación Android desarrollada en **Java** con diseños **XML** y **Firebase Authentication**.
Evidencia GA8-220501096-AA2-EV02 (SENA - ADSO, ficha 3186670).

## Pantallas
- **Login** (`MainActivity`): inicio de sesión con correo y contraseña.
- **Registro** (`Register`): creación de nuevas cuentas.
- **Home** (`Home`): pantalla de bienvenida con sesión iniciada y opción de cerrar sesión.

## Requisitos
- Android Studio (versión reciente) con Android SDK 35.
- Un celular Android (8.0 o superior) o un emulador.
- Un proyecto de Firebase con *Authentication > Correo y contraseña* habilitado.

## Cómo ejecutarlo
1. Abre la carpeta del proyecto en Android Studio y espera a que termine la sincronización de Gradle.
2. En la consola de Firebase registra una app Android con el paquete `com.cyberauditpro.login`.
3. Descarga `google-services.json` y colócalo en la carpeta `app/`.
4. Ejecuta la app con el botón verde (Run).

## Tecnologías
Java · XML · Material Components · Firebase Authentication · Gradle (Kotlin DSL)
