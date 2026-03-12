
import 'dotenv/config'
import express from 'express'
import cors from 'cors'
import { router as authRouter } from './routes/auth.js'
import { router as productsRouter } from './routes/products.js'
import { router as aiRouter } from './routes/ai.js'

const app = express()
app.use(cors())
app.use(express.json({ limit: '2mb' }))

app.get('/health', (_req, res) => res.json({ ok: true }))
app.use('/auth', authRouter)
app.use('/products', productsRouter)
app.use('/ai', aiRouter)

const port = process.env.PORT || 3000
app.listen(port, '0.0.0.0', () => {
  console.log(`API listening on :${port}`)
})
