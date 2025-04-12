# Sistema de Gestión de Garantías

## Descripción del Proyecto
Este proyecto tiene como objetivo desarrollar un sistema en Java para administrar las garantías de computadoras en un centro de servicios. El sistema se encarga de gestionar cada fase del proceso (Recepción, Inspección, Reparación, Control de Calidad y Entrega) mediante un mecanismo de colas (FIFO) y almacena la información en archivos de texto con formato JSON. La aplicación se ejecuta desde la terminal, ofreciendo una interfaz de línea de comandos para interactuar con el sistema.

## Objetivos
- Desarrollar una aplicación en Java para el manejo de garantías de computadoras.
- Procesar las garantías a través de colas (FIFO) que aseguren la correcta secuencia de fases.
- Persistir la información del sistema (clientes, computadoras, técnicos, fases e historial) en archivos JSON.
- Proveer una interfaz CLI (Command Line Interface) intuitiva que permita registrar, mover y consultar computadoras a lo largo del proceso.

## Funcionalidades Principales
- **Registro de Computadoras:**  
  Permite ingresar computadoras al sistema, registrando datos como:
  - Service Tag (identificador único)
  - Descripción del problema
  - Fecha de recepción (formato YYYY-MM-DD)
  - Datos del cliente (nombre, correo y teléfono)

- **Gestión de Fases (Colas FIFO):**  
  El flujo de procesos incluye las siguientes fases:
  1. **Recepción:** La computadora se ingresa y se coloca en la cola de inspección.
  2. **Inspección:** Se diagnostica el equipo para determinar si procede reparar o entregar.
  3. **Reparación:** Se realizan las reparaciones necesarias, registrando el técnico y las acciones efectuadas.
  4. **Control de Calidad:** Se verifica la reparación; en caso de problemas, se regresa a reparación.
  5. **Entrega:** Una vez aprobada la reparación, la computadora se prepara para entrega al cliente.

- **Registro de Historial:**  
  Cada movimiento a través del proceso se registra en un historial que se guarda en un archivo JSON, lo que permite consultar el recorrido completo de la computadora.

- **Interfaz de Línea de Comandos (CLI):**  
  El usuario interactúa con el sistema a través de un menú en la terminal, que ofrece opciones para:
  - Registrar nuevas computadoras.
  - Mover computadoras entre las distintas fases.
  - Consultar el historial de computadoras.
  - Visualizar el estado actual de cada equipo.
  - Salir del sistema.

## Estructura de Datos (Archivos JSON)
La información se almacena en archivos JSON. A continuación se muestra un ejemplo de la estructura de cada archivo:

### clientes.json
```json
[
  {
    "cliente_id": 1,
    "nombre": "Juan Pérez",
    "correo": "juan.perez@example.com",
    "telefono": "1234567890"
  }
]
```

### computadoras.json
```json
[
  {
    "service_tag": "ABC123",
    "cliente_id": 1,
    "descripcion_problema": "Error en disco duro",
    "fecha_recepcion": "2025-04-12"
  }
]
```

### tecnicos.json
```json
[
  {
    "tecnico_id": 1,
    "nombre_tecnico": "Carlos López"
  }
]
```

### fases.json
```json
[
  { "fase_id": 1, "nombre_fase": "Recepción" },
  { "fase_id": 2, "nombre_fase": "Inspección" },
  { "fase_id": 3, "nombre_fase": "Reparación" },
  { "fase_id": 4, "nombre_fase": "Control de Calidad" },
  { "fase_id": 5, "nombre_fase": "Entrega" }
]
```

### historial.json
```json
[
  {
    "historial_id": 1,
    "service_tag": "ABC123",
    "fase_id": 1,
    "tecnico_id": null,
    "fecha_entrada": "2025-04-12T08:00:00",
    "fecha_salida": "2025-04-12T09:00:00",
    "detalles": "Ingresado en cola de inspección"
  }
]
```

## Tecnologías Utilizadas
- **Java:** Lenguaje principal para desarrollar la aplicación.
- **JSON:** Formato para la persistencia de datos.
- **Terminal:** Interfaz de usuario basada en línea de comandos.

## Instrucciones de Configuración y Ejecución

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/sistema-gestion-garantias.git
   cd sistema-gestion-garantias
   ```

2. **Compilar el Proyecto:**
   Compila los archivos Java utilizando el compilador de tu preferencia. Por ejemplo, usando `javac`:
   ```bash
   javac -d bin src/*.java
   ```

3. **Ejecutar la Aplicación:**
   Inicia la aplicación desde la terminal:
   ```bash
   java -cp bin Main
   ```
   El sistema presentará un menú interactivo en la terminal para gestionar las operaciones del sistema.

## Contribuciones
¡Las contribuciones son bienvenidas! Si deseas colaborar:
- Crea una nueva rama para implementar tus cambios.
- Realiza un pull request para revisar y fusionar tus aportes.

## Licencia
Este proyecto se distribuye bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.

---

Este README.md provee una descripción completa del proyecto, su estructura, funcionalidades y cómo configurarlo y ejecutarlo. Puedes ajustarlo según los requerimientos específicos de tu desarrollo. ¡Éxito con tu proyecto!