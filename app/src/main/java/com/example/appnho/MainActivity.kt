package com.example.appnho

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var imgProfile: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvMssv: TextView
    private lateinit var tvClass: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private var currentAvatarUri: String? = null

    // Đón dữ liệu trả về từ EditActivity bằng Activity Result API
    private val editProfileLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val data = result.data!!
            val newName = data.getStringExtra("EXTRA_NAME")
            val newClass = data.getStringExtra("EXTRA_CLASS")
            val newPhone = data.getStringExtra("EXTRA_PHONE")
            val newEmail = data.getStringExtra("EXTRA_EMAIL")
            val newImageUri = data.getStringExtra("EXTRA_AVATAR_URI")

            if (!newName.isNullOrEmpty()) tvName.text = newName
            if (!newClass.isNullOrEmpty()) tvClass.text = "Lớp: $newClass"
            if (!newPhone.isNullOrEmpty()) tvPhone.text = "SĐT: $newPhone"
            if (!newEmail.isNullOrEmpty()) tvEmail.text = "Email: $newEmail"
            if (!newImageUri.isNullOrEmpty()) {
                currentAvatarUri = newImageUri
                imgProfile.setImageURI(Uri.parse(newImageUri))
            }

            Toast.makeText(this, "Đã cập nhật hồ sơ sinh viên thành công!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgProfile = findViewById(R.id.imgProfile)
        tvName = findViewById(R.id.tvName)
        tvMssv = findViewById(R.id.tvMssv)
        tvClass = findViewById(R.id.tvClass)
        tvPhone = findViewById(R.id.tvPhone)
        tvEmail = findViewById(R.id.tvEmail)

        val btnEditProfile = findViewById<Button>(R.id.btnEditProfile)
        val btnCall = findViewById<Button>(R.id.btnCall)
        val btnEmail = findViewById<Button>(R.id.btnEmail)

        // Chuyển sang EditActivity truyền dữ liệu hiện tại
        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java).apply {
                putExtra("EXTRA_NAME", tvName.text.toString())
                putExtra("EXTRA_CLASS", tvClass.text.toString().replace("Lớp: ", ""))
                putExtra("EXTRA_PHONE", tvPhone.text.toString().replace("SĐT: ", ""))
                putExtra("EXTRA_EMAIL", tvEmail.text.toString().replace("Email: ", ""))
                putExtra("EXTRA_AVATAR_URI", currentAvatarUri)
            }
            editProfileLauncher.launch(intent)
        }

        // Implicit Intent quay số gọi điện
        btnCall.setOnClickListener {
            val phone = tvPhone.text.toString().replace("SĐT: ", "").trim()
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            try {
                startActivity(dialIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Không có ứng dụng gọi điện!", Toast.LENGTH_SHORT).show()
            }
        }

        // Implicit Intent gửi Email
        btnEmail.setOnClickListener {
            val email = tvEmail.text.toString().replace("Email: ", "").trim()
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
                putExtra(Intent.EXTRA_SUBJECT, "Báo cáo thực hành Android UTE")
            }
            try {
                startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng gửi mail"))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Không có ứng dụng gửi email!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}