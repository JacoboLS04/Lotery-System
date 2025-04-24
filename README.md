# 🎟️ Sistema de Lotería

Este proyecto implementa un sistema completo para la gestión de lotería, permitiendo el registro de clientes, validación de boletos, registro de ganadores, procesamiento de pagos y notificaciones automatizadas.

## ✨ Funcionalidades principales

- 👤 Registro y gestión de clientes
- 🔍 Verificación de clientes
- 🎫 Administración de boletos
- 🏆 Registro de ganadores de sorteos
- 💰 Simulación de procesos de pago para premios
- 📧 Notificación automática a ganadores por correo electrónico
- 🔒 Panel de administración protegido con credenciales
- 🔎 Búsqueda de clientes por nombre o cédula
- 🖱️ Interfaz gráfica intuitiva

## 🌐 Servicios externos utilizados

- **MongoDB**: 📊 Base de datos NoSQL para almacenamiento de información de clientes, boletos y ganadores
- **Servicio de correo electrónico**: ✉️ Para enviar notificaciones automáticas a los ganadores
- **Sistema de procesamiento de pagos**: 💳 Simulación del procesamiento de pagos para premios
- **Look and Feel del sistema**: 🎨 Mejora de la interfaz gráfica con componentes personalizados

## 📁 Estructura del proyecto

El proyecto sigue un patrón de arquitectura MVC (Modelo-Vista-Controlador):
- **Modelo**: 📋 Clases que representan entidades como Cliente, Boleto y Ganador
- **Vista**: 🖼️ Interfaces gráficas desarrolladas con Swing
- **Controlador**: ⚙️ Clases que gestionan la lógica de negocio
- **DAO**: 💾 Clases para la persistencia y acceso a datos en MongoDB

## 🛠️ Requisitos técnicos

- ☕ Java 11 o superior
- 🍃 MongoDB
- 🏗️ Maven
- 🌐 Conexión a Internet (para envío de correos)

## ⚙️ Configuración

1. 🔄 Asegúrate de tener MongoDB instalado y en ejecución
2. 📨 Configura las credenciales de correo electrónico en el servicio EmailService
3. 🚀 Ejecuta la aplicación desde la clase AplicacionLoteria

## 🔐 Seguridad

El sistema implementa validación de credenciales para funciones administrativas. La clave predeterminada para operaciones administrativas es: `lot01AXM`
