### 📨 Arquitectura Orientada a Eventos (Kafka / Asíncrono)
- [ ] **No hay `Thread.sleep()` en el código.** Manejé las esperas de Kafka utilizando `Awaitility` o mecanismos de polling asíncronos.
- [ ] Mis consumidores de Kafka buscan mensajes usando un **Correlation ID** o clave única (guardada en la memoria del actor) para evitar leer mensajes de otras pruebas concurrentes.
- [ ] La configuración del Producer/Consumer se maneja a través de las Habilidades (`Abilities`) del Actor y no está quemada en las `Tasks`.