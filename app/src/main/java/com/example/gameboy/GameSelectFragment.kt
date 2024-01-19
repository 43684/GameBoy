package com.example.gameboy

import HighScoreAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameboy.databinding.FragmentSelectGameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GameSelectFragment : Fragment() {

    private lateinit var adapter: HighScoreAdapter
    private val userList = mutableListOf<UserData>()
    lateinit var binding: FragmentSelectGameBinding
    var listener: GameListener? = null
    private lateinit var firebaseAuth: FirebaseAuth
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

        firebaseAuth = FirebaseAuth.getInstance()
        binding.highscore.visibility = View.INVISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
        if(firebaseAuth.currentUser != null){
            fetchData()
            binding.highscore.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE
        }


        binding.btn2.setOnClickListener(View.OnClickListener {
            listener?.startHockey()
            println("hockey")
        })

        binding.btn1.setOnClickListener(View.OnClickListener {
            listener?.startPong()
            println("clicking")

        })
    }

    override fun onDetach() {
        super.onDetach()
        listener = null

    }

    interface GameListener {
        fun startPongMenu()
        fun startPong()
        fun startHockey()
    }

    private fun fetchData() {
        val database = FirebaseDatabase.getInstance()
        val dataRef: DatabaseReference = database.getReference("users")
        val key = firebaseAuth.currentUser?.uid
        var currentLoggedUser: UserData = UserData("",0,0)


        dataRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear() // Clear the existing data before adding new data

                for (childSnapshot in snapshot.children) {
                    val userDataMap = childSnapshot.value as? Map<String, Any>
                    if (childSnapshot.key != key) {
                        if (userDataMap != null) {

                            val name = userDataMap["name"] as? String ?: ""
                            val highscore = (userDataMap["highscore"] as? Long)?.toInt() ?: 0
                            val hockeyScore = (userDataMap["hockeyScore"] as? Long)?.toInt() ?: 0

                            val userData = UserData(name, highscore,hockeyScore)
                            userList.add(userData)


                        }
                    } else {
                        if (userDataMap != null) {

                            val name = userDataMap["name"] as? String ?: ""
                            val highscore = (userDataMap["highscore"] as? Long)?.toInt() ?: 0
                            val hockeyScore = (userDataMap["hockeyScore"] as? Long)?.toInt() ?: 0

                            currentLoggedUser = UserData(name, highscore,hockeyScore)

                        }
                    }


                }
                userList.sortWith(compareByDescending { it.highscore })
                userList.add(0,currentLoggedUser)

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                println("Error: $error")
            }
        })
    }

    }

