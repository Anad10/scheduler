package edu.iliauni.tabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class ChooseProgramActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_program)

        val loginButton = findViewById(R.id.btn_login) as Button
        loginButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val createProgramButton = findViewById(R.id.btn_create_program) as Button
        createProgramButton.setOnClickListener{
            val intent = Intent(this, CreateProgramActivity::class.java)
            startActivity(intent)
        }
    }
}