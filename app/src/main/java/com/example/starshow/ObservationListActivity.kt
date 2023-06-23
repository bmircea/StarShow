package com.example.starshow

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.starshow.databinding.ActivityObslistBinding
import java.util.*
import kotlin.collections.ArrayList

class ObservationListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView : SearchView
    private var mList = ArrayList<Observation>()
    private lateinit var adapter : ObservationAdapter

    private lateinit var binding: ActivityObslistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityObslistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        addData()
        adapter = ObservationAdapter(mList)

        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true;
            }

        })


    }

    override fun onBackPressed() {
        startActivity(Intent(this@ObservationListActivity, MainActivity::class.java))
        finish()
    }

    private fun filterList(query: String?)
    {
        if (query != null)
        {
            val filteredList = ArrayList<Observation>()
            for (i in mList)
            {
                if (i.name.lowercase(Locale.ROOT).contains(query))
                {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty())
            {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            }
            else
            {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addData() {
        val db = AppDatabase.getInstance(this)
        val obsDao = db.observationDao()
        val obs = obsDao.getAllObservations()

        for (o in obs)
        {
            mList.add(o)
        }
    }
}