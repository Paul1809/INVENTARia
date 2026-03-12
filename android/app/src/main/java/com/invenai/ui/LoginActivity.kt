
package com.invenai.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.invenai.model.LoginRequest
import com.invenai.network.RetrofitClient
import com.invenai.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sm = SessionManager(this)
        sm.getToken()?.let {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish(); return
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val email = EditText(this@LoginActivity).apply { hint = "email" }
            val pass = EditText(this@LoginActivity).apply { hint = "password" }
            val btn = Button(this@LoginActivity).apply { text = "Login" }
            addView(email); addView(pass); addView(btn)

            btn.setOnClickListener {
                lifecycleScope.launch {
                    try {
                        val res = withContext(Dispatchers.IO) {
                            RetrofitClient.api.login(LoginRequest(email.text.toString(), pass.text.toString()))
                        }
                        sm.saveToken(res.token)
                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        setContentView(root)
    }
}
