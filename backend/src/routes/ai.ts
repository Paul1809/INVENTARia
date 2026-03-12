
import { Router } from 'express'
import { authMiddleware } from '../middleware/auth.js'
import { z } from 'zod'
import { GoogleGenAI } from '@google/genai'

const ai = new GoogleGenAI({ apiKey: process.env.GOOGLE_API_KEY })
export const router = Router()
router.use(authMiddleware)

const DescribeSchema = z.object({
  name: z.string(),
  category: z.string().optional(),
  attributes: z.record(z.any()).optional(),
})

router.post('/describe', async (req, res) => {
  const parsed = DescribeSchema.safeParse(req.body)
  if (!parsed.success) return res.status(400).json(parsed.error)

  const { name, category, attributes } = parsed.data
  const prompt = `Eres un asistente de inventario. Genera una descripción breve y clara para el producto: 
` +
    `- Nombre: ${name}
` + (category ? `- Categoría: ${category}
` : '') +
    (attributes ? `- Atributos: ${JSON.stringify(attributes)}
` : '') +
    `Devuelve 1 párrafo (60-90 palabras) y un bullet list de 3 beneficios.`

  try {
    const response = await ai.models.generateContent({
      model: 'gemini-2.5-flash',
      contents: prompt,
    })
    res.json({ text: response.text })
  } catch (e: any) {
    res.status(500).json({ error: e?.message || 'Gemini error' })
  }
})

const ForecastSchema = z.object({
  name: z.string(),
  stock: z.number().int().nonnegative(),
  avgWeeklySales: z.number().nonnegative().optional().default(0),
  leadTimeDays: z.number().int().positive().optional().default(7)
})

router.post('/forecast', async (req, res) => {
  const parsed = ForecastSchema.safeParse(req.body)
  if (!parsed.success) return res.status(400).json(parsed.error)
  const { name, stock, avgWeeklySales, leadTimeDays } = parsed.data

  const prompt = `Actúa como planificador de inventario. Producto: ${name}. Stock actual: ${stock}. ` +
    `Ventas semanales promedio: ${avgWeeklySales}. Lead time (días): ${leadTimeDays}. ` +
    `Calcula si debo reabastecer y sugiere una cantidad (número). ` +
    `Responde en JSON con: {"reabastecer": boolean, "cantidad_sugerida": number, "comentarios": string}.`

  try {
    const response = await ai.models.generateContent({ model: 'gemini-2.5-flash', contents: prompt })
    // Intenta parsear JSON desde el texto de Gemini
    const text = response.text
    let json: any
    try { json = JSON.parse(text) } catch { json = { raw: text } }
    res.json(json)
  } catch (e: any) {
    res.status(500).json({ error: e?.message || 'Gemini error' })
  }
})
