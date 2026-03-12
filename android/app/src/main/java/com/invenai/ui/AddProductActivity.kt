
package com.invenai.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.invenai.model.Product
import com.invenai.network.RetrofitClient
import com.invenai.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = EditText(this).apply { hint = "Nombre" }
        val price = EditText(this).apply { hint = "Precio" }
        val stock = EditText(this).apply { hint = "Stock" }
        val btn = Button(this).apply { text = "Guardar" }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(name); addView(price); addView(stock); addView(btn)
        }
        setContentView(root)

        btn.setOnClickListener {
            val token = SessionManager(this).getToken() ?: return@setOnClickListener
            lifecycleScope.launch {
                try {
                    val p = Product(
                        name = name.text.toString(),
                        price = price.text.toString().toDoubleOrNull() ?: 0.0,
                        stock = stock.text.toString().toIntOrNull() ?: 0
                    )
                    val created = withContext(Dispatchers.IO) {
                        RetrofitClient.api.addProduct("Bearer $token", p)
                    }
                    Toast.makeText(this@AddProductActivity, "Creado #${created.id}", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@AddProductActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
