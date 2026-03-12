
import { Router } from 'express'
import { prisma } from '../db.js'
import { z } from 'zod'
import { authMiddleware } from '../middleware/auth.js'

export const router = Router()

router.use(authMiddleware)

router.get('/', async (_req, res) => {
  const items = await prisma.product.findMany({ orderBy: { id: 'desc' } })
  res.json(items)
})

const ProductSchema = z.object({
  name: z.string().min(1),
  description: z.string().optional(),
  price: z.number().nonnegative(),
  stock: z.number().int().nonnegative().default(0),
  sku: z.string().optional(),
})

router.post('/', async (req, res) => {
  const parsed = ProductSchema.safeParse(req.body)
  if (!parsed.success) return res.status(400).json(parsed.error)
  const created = await prisma.product.create({ data: parsed.data })
  res.status(201).json(created)
})
