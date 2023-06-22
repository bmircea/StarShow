package com.example.starshow

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.starshow.databinding.ActivityMainBinding
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle // open/close menu

    //profile pic
    var pickedPhoto : Uri? = null
    var pickedBitMap : Bitmap? = null

    //video
    private  var videoView : VideoView? = null
    private  var mediaController : MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

    //profile pic
    fun pickedPhoto(view: View){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1)
        } else {
            val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntext,2)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntext = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver,pickedPhoto!!)
                    pickedBitMap = ImageDecoder.decodeBitmap(source)
                    ImageView(findViewById(R.id.imageViewUser)).setImageBitmap(pickedBitMap)
                }
                else {
                    pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver,pickedPhoto)
                    ImageView(findViewById(R.id.imageViewUser)).setImageBitmap(pickedBitMap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //video
    private fun setUpVideoPlayer(){
        if (mediaController == null){
            mediaController = MediaController(this)
            mediaController!!.setAnchorView(this.videoView)
        }
        videoView!!.setMediaController(mediaController)
        videoView!!.setVideoURI(
            Uri.parse("android.resource://"
            + packageName + "/" + R.raw.videostars)
        )
        videoView!!.requestFocus()
        videoView!!.pause()
        videoView!!.setOnCompletionListener {
            Toast.makeText(applicationContext, "Video Completed", Toast.LENGTH_SHORT).show()
        }
        videoView!!.setOnErrorListener{ mp, what, extra ->
            Toast.makeText(applicationContext, "Error Occured", Toast.LENGTH_SHORT).show()
            false
        }
    }

}
