package com.example.appnho

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity() {

    private lateinit var imgEditAvatar: ImageView
    private lateinit var edtEditName: EditText
    private lateinit var edtEditClass: EditText
    private lateinit var edtEditPhone: EditText
    private lateinit var edtEditEmail: EditText
    private var selectedImageUri: Uri? = null

    // Activity Result API chọn ảnh từ bộ sưu tập
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            imgEditAvatar.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        imgEditAvatar = findViewById(R.id.imgEditAvatar)
        val btnChangeAvatar = findViewById<Button>(R.id.btnChangeAvatar)
        edtEditName = findViewById(R.id.edtEditName)
        edtEditClass = findViewById(R.id.edtEditClass)
        edtEditPhone = findViewById(R.id.edtEditPhone)
        edtEditEmail = findViewById(R.id.edtEditEmail)
        val btnSaveProfile = findViewById<Button>(R.id.btnSaveProfile)

        // Điền dữ liệu nhận được từ MainActivity
        edtEditName.setText(intent.getStringExtra("EXTRA_NAME"))
        edtEditClass.setText(intent.getStringExtra("EXTRA_CLASS"))
        edtEditPhone.setText(intent.getStringExtra("EXTRA_PHONE"))
        edtEditEmail.setText(intent.getStringExtra("EXTRA_EMAIL"))

        val prevAvatarUri = intent.getStringExtra("EXTRA_AVATAR_URI")
        if (!prevAvatarUri.isNullOrEmpty()) {
            selectedImageUri = Uri.parse(prevAvatarUri)
            imgEditAvatar.setImageURI(selectedImageUri)
        }

        // Chọn ảnh đại diện
        btnChangeAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Lưu thông tin và đóng Activity
        btnSaveProfile.setOnClickListener {
            val name = edtEditName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Họ tên không được để trống!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent().apply {
                putExtra("EXTRA_NAME", name)
                putExtra("EXTRA_CLASS", edtEditClass.text.toString().trim())
                putExtra("EXTRA_PHONE", edtEditPhone.text.toString().trim())
                putExtra("EXTRA_EMAIL", edtEditEmail.text.toString().trim())
                selectedImageUri?.let { uri ->
                    putExtra("EXTRA_AVATAR_URI", uri.toString())
                }
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}