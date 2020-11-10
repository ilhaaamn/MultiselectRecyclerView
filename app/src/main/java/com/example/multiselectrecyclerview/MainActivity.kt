package com.example.multiselectrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multiselectrecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recylerAdapter: MultiSelectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recylerAdapter = MultiSelectAdapter(activity = this)
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(application)
            adapter = recylerAdapter
        }

        val itemList = mutableListOf<Item>()
        repeat(100){
            itemList.add(Item("Title percobaan bisa $it", "Desc percobaan lorem ipsum panjang lah ini pokonya di coba dulu aja $it"))
        }
        recylerAdapter.submitList(itemList)
    }
}