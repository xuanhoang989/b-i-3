package com.example.appnho
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvCurrentName: TextView
    private lateinit var btnEditInfo: Button

    // 1. Đăng ký ActivityResultLauncher theo chuẩn Activity Result API
    private val editLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedName = result.data?.getStringExtra("EXTRA_UPDATED_NAME")
            if (!updatedName.isNullOrEmpty()) {
                tvCurrentName.text = "Họ tên: $updatedName"
                tvCurrentName.setTextColor(Color.parseColor("#2E7D32"))
                tvCurrentName.setTypeface(null, Typeface.BOLD)
                Toast.makeText(this, "Đã cập nhật tên thành công!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCurrentName = findViewById(R.id.tvCurrentName)
        btnEditInfo = findViewById(R.id.btnEditInfo)

        btnEditInfo.setOnClickListener {
            val currentName = tvCurrentName.text.toString()
            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra("EXTRA_CURRENT_NAME", currentName)
            }
            // Kích hoạt chuyển Activity con
            editLauncher.launch(intent)
        }
    }
}