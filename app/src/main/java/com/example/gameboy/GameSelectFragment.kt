package com.example.gameboy

import HighScoreAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameboy.databinding.FragmentSelectGameBinding
import com.google.firebase.database.*

class GameSelectFragment : Fragment() {

    private lateinit var adapter: HighScoreAdapter
    private val userList = mutableListOf<UserData>()

    lateinit var binding: FragmentSelectGameBinding
    var listener: GameListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as GameListener
            println("Successful implementation")
        } catch (e: Exception) {
            println("Failed implementation")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectGameBinding.inflate(layoutInflater)
        adapter = HighScoreAdapter(userList)

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()

        binding.btn1.setOnClickListener(View.OnClickListener {
            listener?.startPongMenu()
            println("clicking")
        })
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface GameListener {
        fun startPongMenu()
    }

    private fun fetchData() {
        val database = FirebaseDatabase.getInstance()
        val dataRef: DatabaseReference = database.getReference("users")

        dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear() // Clear the existing data before adding new data

                for (childSnapshot in snapshot.children) {
                    val userDataMap = childSnapshot.value as? Map<String, Any>

                    if (userDataMap != null) {
                        val name = userDataMap["name"] as? String ?: ""
                        val highscore = (userDataMap["highscore"] as? Long)?.toInt() ?: 0

                        val userData = UserData(name, highscore)
                        userList.add(userData)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                println("Error: $error")
            }
        })
    }
}
