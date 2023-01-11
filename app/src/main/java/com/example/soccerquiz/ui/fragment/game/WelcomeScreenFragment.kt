package com.example.soccerquiz.ui.fragment.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.soccerquiz.R
import com.example.soccerquiz.databinding.FragmentWelcomeScreenBinding

class WelcomeScreenFragment : Fragment() {


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentWelcomeScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_welcome_screen, container, false
        )

        binding.letsPlayButton.setOnClickListener{ view:View ->
            Navigation.findNavController(view).navigate(R.id.action_welcomeScreenFragment_to_quizFragment)
        }

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title)
        return binding.root
    }

}