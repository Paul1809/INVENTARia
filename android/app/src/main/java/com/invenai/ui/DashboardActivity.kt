
package com.invenai.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.invenai.model.Product
import com.invenai.network.RetrofitClient
import com.invenai.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardActivity : AppCompatActivity() {
    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sm = SessionManager(this)
        val token = sm.getToken()
        if (token == null) { startActivity(Intent(this, LoginActivity::class.java)); finish(); return }

        tv = TextView(this).apply { text = "Productos cargando..." }
        val btnAdd = Button(this).apply { text = "Añadir producto" }
        val btnDescribe = Button(this).apply { text = "IA: describir producto demo" }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(tv)
            addView(btnAdd)
            addView(btnDescribe)
        }
        setContentView(ScrollView(this).apply { addView(container) })

        loadProducts(token)

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        btnDescribe.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val res = withContext(Dispatchers.IO) {
                        RetrofitClient.api.aiDescribe("Bearer $token", mapOf(
                            "name" to "Zapato deportivo X",
                            "category" to "Calzado",
                            "attributes" to mapOf("color" to "negro", "talla" to 27)
                        ))
                    }
                    tv.text = res["text"]?.toString() ?: res.toString()
                } catch (e: Exception) {
                    tv.text = "Error IA: ${e.message}"
                }
            }
        }
    }

    private fun loadProducts(token: String) {
        lifecycleScope.launch {
            try {
                val products: List<Product> = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getProducts("Bearer $token")
                }
                tv.text = products.joinToString("
") { "#${it.id} ${it.name} (${it.stock})" }
            } catch (e: Exception) {
                tv.text = "Error cargando: ${e.message}"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { menu?.add(0,1,0,"Cerrar sesión"); return true }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) { SessionManager(this).clear(); startActivity(Intent(this, LoginActivity::class.java)); finish(); return true }
        return super.onOptionsItemSelected(item)
    }
}
