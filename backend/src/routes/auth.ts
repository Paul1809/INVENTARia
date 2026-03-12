
import { Router } from 'express'
import { prisma } from '../db.js'
import jwt from 'jsonwebtoken'
import { z } from 'zod'

export const router = Router()

const LoginSchema = z.object({ email: z.string().email(), password: z.string().min(4) })

router.post('/login', async (req, res) => {
  const parsed = LoginSchema.safeParse(req.body)
  if (!parsed.success) return res.status(400).json(parsed.error)

  const { email, password } = parsed.data

  let user = await prisma.user.findUnique({ where: { email } })
  if (!user) {
    // Auto-registro simple para demo
    user = await prisma.user.create({ data: { email, password, name: email.split('@')[0] } })
  }

  if (user.password !== password) return res.status(401).json({ error: 'Credenciales inválidas' })

  const token = jwt.sign({ id: user.id, email: user.email }, process.env.JWT_SECRET!, { expiresIn: '2d' })
  return res.json({ token, user: { id: user.id, email: user.email, name: user.name } })
})
