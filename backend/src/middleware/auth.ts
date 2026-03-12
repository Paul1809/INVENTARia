
import { Request, Response, NextFunction } from 'express'
import jwt from 'jsonwebtoken'

export interface AuthRequest extends Request { user?: { id: number; email: string } }

export function authMiddleware(req: AuthRequest, res: Response, next: NextFunction) {
  const header = req.headers['authorization']
  if (!header) return res.status(401).json({ error: 'Missing Authorization header' })

  const token = header.replace('Bearer ', '')
  try {
    const payload = jwt.verify(token, process.env.JWT_SECRET!) as { id: number; email: string }
    req.user = payload
    next()
  } catch (e) {
    return res.status(401).json({ error: 'Invalid token' })
  }
}
