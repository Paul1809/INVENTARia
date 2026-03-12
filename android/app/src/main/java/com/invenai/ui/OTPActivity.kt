
package com.invenai.ui

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            val t = TextView(this@OTPActivity).apply { text = "OTP no requerido (flujo demo)" }
            addView(t)
        }
        setContentView(root)
    }
}
