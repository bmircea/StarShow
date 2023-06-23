package com.example.starshow

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.starshow.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle // open/close menu
    lateinit var navName : TextView
    lateinit var navEmail: TextView
    lateinit var googleSignInClient: GoogleSignInClient
    //video
    private  var videoView : VideoView? = null
    private  var mediaController : MediaController? = null
    private  var pass : String? = null


    private var imageView : ImageView? = null
    //profile pic
    var pickedPhoto : Uri? = null
    var pickedBitMap : Bitmap? = null

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this,gso)

        val username = SavedPreference.getUsername(this)
        val email = SavedPreference.getEmail(this)

        val navigationView : NavigationView = findViewById(R.id.navView)
        val headerView : View = navigationView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.navName)
        val navUserEmail : TextView = headerView.findViewById(R.id.navEmail)

        navUsername.text = username
        navUserEmail.text = email

        videoView = findViewById(R.id.videoView)
        imageView = findViewById(R.id.imageView)
        setUpVideoPlayer()

        pass = SavedPreference.getPass(this).toString()

        binding.apply {
            toggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open, R.string.close)

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)



            navView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.importantObs->{
                        Toast.makeText(this@MainActivity, "Opening Important Observations", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, ObservationListActivity::class.java))
                        finish()
                    }
                    R.id.futureObs->{
                        Toast.makeText(this@MainActivity, "Opening Future Observations", Toast.LENGTH_SHORT).show()
                    }
                    R.id.pastObs->{
                        Toast.makeText(this@MainActivity, "Opening Past Observations", Toast.LENGTH_SHORT).show()
                    }
                    R.id.logout->{

                        auth.signOut()


                        if (pass == "")
                        {
                            googleSignInClient.signOut().addOnCompleteListener {
                                Toast.makeText(this@MainActivity,"Logging Out",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                finish()
                            }
                        }


                    }
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
        videoView!!.start()
        videoView!!.setOnCompletionListener {
            Toast.makeText(applicationContext, "Video Completed", Toast.LENGTH_SHORT).show()
        }
        videoView!!.setOnErrorListener{ mp, what, extra ->
            Toast.makeText(applicationContext, "Error Occured", Toast.LENGTH_SHORT).show()
            false
        }
    }

    //profile pic
    fun pickedPhoto(view: View){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            startActivityForResult(takePictureIntent, 100)
        }catch (e: ActivityNotFoundException)
        {
            Toast.makeText(this, "Error:"+e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

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
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            imageView!!.setImageBitmap(imageBitmap)
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)

        }
    }
}
