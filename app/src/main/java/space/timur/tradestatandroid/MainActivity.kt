package space.timur.tradestatandroid

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import space.timur.tradestatandroid.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

const val ADD_TASK_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_TASK_RESULT_OK = Activity.RESULT_FIRST_USER + 1