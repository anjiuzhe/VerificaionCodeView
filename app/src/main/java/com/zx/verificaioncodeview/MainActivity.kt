package com.zx.verificaioncodeview

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zx.library.OnCodeChangeListener
import com.zx.library.VerificationCodeView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val codeView = findViewById<VerificationCodeView>(R.id.code_view)
        val textView = findViewById<TextView>(R.id.text_view)

        codeView.setOnCodeChangeListener(object : OnCodeChangeListener {
            override fun onCodeChange(text: String?) {
                textView.text = text
            }
        })
        codeView.setText("123456")
    }
}