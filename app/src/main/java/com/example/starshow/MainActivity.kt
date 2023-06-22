package com.example.starshow

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.starshow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle // open/close menu

    //lateinit var animateButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.importantObs->{Toast.makeText(this@MainActivity, "Opening Important Observations", Toast.LENGTH_SHORT).show()}
                    R.id.futureObs->{Toast.makeText(this@MainActivity, "Opening Future Observations", Toast.LENGTH_SHORT).show()}
                    R.id.pastObs->{Toast.makeText(this@MainActivity, "Opening Past Observations", Toast.LENGTH_SHORT).show()}
                }
                true
            }
        }

        //object animator
        //animateButton = findViewById(R.id.animateButton)
        //animateButton.setOnClickListener {
            //val tY = ObjectAnimator.ofFloat(R.id.redView, View.TRANSLATION_Y, R.id.redView.translationY, R.id.redView.translationY + 100f)
        //}

        binding.imageStar.setOnClickListener {
            binding.imageStar.animate().apply {
                duration = 1000
                alpha(.5f)
                scaleXBy(.5f)
                scaleYBy(.5f)
                rotationYBy(360f) // continue adding 360 degrees to the animation
                translationYBy(200f)
            }.withEndAction {
                binding.imageStar.animate().apply {
                    duration = 1000
                    alpha(1f)
                    scaleXBy(-.5f)
                    scaleYBy(-.5f)
                    rotationYBy(360f) // continue adding 360 degrees to the animation
                    translationYBy(-200f)
                }.start()
            }
        }

   }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
}
