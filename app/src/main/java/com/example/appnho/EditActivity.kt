package com.example.appnho

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val edtName = findViewById<EditText>(R.id.edtName)
        val btnSaveAndReturn = findViewById<Button>(R.id.btnSaveAndReturn)

        // 1. Nhận tên hiện tại từ MainActivity gửi sang
        val receivedName = intent.getStringExtra("EXTRA_CURRENT_NAME") ?: ""
        if (receivedName.isNotEmpty() && receivedName != "Chưa có thông tin") {
            edtName.setText(receivedName.replace("Họ tên: ", ""))
        }

        // 2. Bấm nút Lưu & Quay lại
        btnSaveAndReturn.setOnClickListener {
            val newName = edtName.text.toString().trim()

            if (newName.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Đóng gói tên mới qua Intent, gán RESULT_OK và đóng màn hình bằng finish()
            val resultIntent = Intent().apply {
                putExtra("EXTRA_UPDATED_NAME", newName)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}