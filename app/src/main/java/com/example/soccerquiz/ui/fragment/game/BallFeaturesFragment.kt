package com.example.soccerquiz.ui.fragment.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.soccerquiz.R


class BallFeaturesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Ball features"
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ball_features, container, false)
    }

}