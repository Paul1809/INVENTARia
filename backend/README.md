
# InvenAI Backend (Express + Prisma + PostgreSQL + Gemini)

Backend para **control de inventario con IA**. Usa:
- **Express** para API REST
- **PostgreSQL** en **Railway** (variable `DATABASE_URL`)
- **Prisma ORM** para acceso a datos
- **Google Gemini** vía **Google Gen AI SDK** para descripciones/forecast

## Variables de entorno
Crea `.env` (Railway → Variables):

```
DATABASE_URL=postgresql://USER:PASSWORD@HOST:PORT/DB?sslmode=require
JWT_SECRET=super_secret_change_me
GOOGLE_API_KEY=tu_api_key_de_gemini
PORT=3000
```

> Railway **inyecta** automáticamente `DATABASE_URL` cuando añades un servicio de PostgreSQL al proyecto. Puedes usarla directamente sin copiar valores manualmente. citeturn7search14

## Scripts
- `npm run dev` – desarrollo (hot reload con tsx)
- `npm run build` – compila TypeScript + genera Prisma Client
- `npm start` – ejecuta `dist/index.js`
- `npm run migrate:dev` – aplica migraciones en dev
- `npm run migrate:deploy` – aplica migraciones en prod (Railway)

## Despliegue en Railway (resumen)
1. Sube este repo a GitHub.
2. En Railway: **New → Deploy from GitHub**. Railway detecta Node.js automáticamente. citeturn7search26turn7search27
3. Agrega un servicio **PostgreSQL**; Railway añade `DATABASE_URL` al servicio. citeturn7search14
4. Agrega variables: `JWT_SECRET`, `GOOGLE_API_KEY`, `PORT`.
5. Configura comandos (opcional):
   - Build: `npm run build`
   - Start: `npm run migrate:deploy && npm start`

## Gemini (Google Gen AI SDK)
Este backend usa el SDK **@google/genai** y la API **Gemini**.
- Docs Gemini API: https://ai.google.dev/gemini-api/docs citeturn7search24
- Ejemplo de uso del SDK **@google/genai** (Node 20+): https://github.com/googleapis/js-genai citeturn7search25

## Endpoints principales
- `POST /auth/login` → obtiene JWT con email/clave demo
- `GET /products` (Auth) → lista productos
- `POST /products` (Auth) → crea producto
- `POST /ai/describe` (Auth) → genera descripción con IA
- `POST /ai/forecast` (Auth) → recomienda reabasto básico con IA

