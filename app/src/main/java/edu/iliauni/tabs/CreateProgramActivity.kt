package edu.iliauni.tabs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CreateProgramActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_program)

        val registerProgramButton = findViewById(R.id.btn_register_program) as Button
        registerProgramButton.setOnClickListener{
            val intent = Intent(this, ProgramCreationSuccessActivity::class.java)
            startActivity(intent)
        }
    }
}