package com.example.soccerquiz.ui.fragment.game

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.soccerquiz.QuizItem
import com.example.soccerquiz.R
import com.example.soccerquiz.databinding.FragmentQuizBinding

import com.example.soccerquiz.util.Constants.Companion.QUESTION_1
import com.example.soccerquiz.util.Constants.Companion.QUESTION_10
import com.example.soccerquiz.util.Constants.Companion.QUESTION_11
import com.example.soccerquiz.util.Constants.Companion.QUESTION_12
import com.example.soccerquiz.util.Constants.Companion.QUESTION_2
import com.example.soccerquiz.util.Constants.Companion.QUESTION_3
import com.example.soccerquiz.util.Constants.Companion.QUESTION_4
import com.example.soccerquiz.util.Constants.Companion.QUESTION_5
import com.example.soccerquiz.util.Constants.Companion.QUESTION_6
import com.example.soccerquiz.util.Constants.Companion.QUESTION_7
import com.example.soccerquiz.util.Constants.Companion.QUESTION_8
import com.example.soccerquiz.util.Constants.Companion.QUESTION_9


class QuizFragment : Fragment() {


    private val quizItems: MutableList<QuizItem> = mutableListOf(
        QuizItem(QUESTION_1,
            listOf("11", "8", "12")),

        QuizItem(
            QUESTION_2,
            listOf("27\" to 28\"", "24\" to 25\"", "23\" to 24\"")),

        QuizItem(
            QUESTION_3,
            listOf("Red Card", "Green Card", "Yellow Card")),

        QuizItem(
            QUESTION_4,
            listOf("Football", "Footgame", "Legball")),

        QuizItem(QUESTION_5,
            listOf("Awards an indirect free kick to the opposing team",
                "Awards a penalty to the opposing team",
                "Awards a yellow card to the player")),

        QuizItem(QUESTION_6,
            listOf("17", "11", "23")),

        QuizItem(QUESTION_7,
            listOf("Arm", "Head", "Shoulder")),

        QuizItem(QUESTION_8,
            listOf("Offside", "Outside", "Field-side")),

        QuizItem(QUESTION_9,
            listOf("70", "80", "90")),

        QuizItem(QUESTION_10,
            listOf("90", "95", "100")),

        QuizItem(QUESTION_11,
            listOf("Both hands must be on the ball behind the head, both feet must be on the ground",
                "Both hands must be on the ball behind the head",
                "Both feet must be on the ground")),

        QuizItem(QUESTION_12,
            listOf("2.44m high, 7.32m wide", "2.55m high, 7.62m wide", "2.33m high, 8.15m wide"))

    )

    lateinit var currentQuizItem: QuizItem
    lateinit var answers:MutableList<String>
    private var quizItemIndex = 0
    private val numberOfQuestion = 3


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val binding = DataBindingUtil.inflate<FragmentQuizBinding>(
           inflater, R.layout.fragment_quiz,container,false)

        getRandomQuizItem()

        binding.quizFragment = this

        binding.passButton.setOnClickListener {view:View->
            val selectedCheckboxId = binding.quizRadioGroup.checkedRadioButtonId
            if (selectedCheckboxId !=-1){
                var answerIndex = 0
                when(selectedCheckboxId){
                    R.id.firstRadioButton -> answerIndex = 0
                    R.id.secondRadioButton -> answerIndex = 1
                    R.id.thirdRadioButton -> answerIndex = 2
                }
                if (answers[answerIndex] == currentQuizItem.answerList[0]){
                    quizItemIndex++
                    if (quizItemIndex < numberOfQuestion){

                        setQuizItem()
                        binding.invalidateAll()

                    }else{

                        binding.ballImageView.animate().translationXBy(700f)
                            .rotation(3600f).duration=3000

                        Handler().postDelayed({
                            //go to goalFragment
                            view.findNavController().navigate(
                                R.id.action_quizFragment_to_goalFragment
                            )
                        },3000)


                    }
                }else{
                    binding.ballImageView.animate().translationXBy(-700f)
                        .rotation(3600f).duration=3000

                    Handler().postDelayed({
                        //go to missFragment
                        view.findNavController().navigate(
                            R.id.action_quizFragment_to_missFragment
                        )
                    },3000)


                }
            }
        }
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title)

        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu,menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }


    private fun getRandomQuizItem(){
        quizItems.shuffle()
        quizItemIndex = 0
        setQuizItem()

    }

    private fun setQuizItem(){
        currentQuizItem = quizItems[quizItemIndex]
        answers = currentQuizItem.answerList.toMutableList()
        answers.shuffle()
    }

}