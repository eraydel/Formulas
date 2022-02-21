package com.eraydel.formulas

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class Resultado : AppCompatActivity() {
    private lateinit var analytics : FirebaseAnalytics
    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        analytics = Firebase.analytics

        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT){
            param(FirebaseAnalytics.Param.ITEM_ID,"2")
            param(FirebaseAnalytics.Param.ITEM_NAME,"Pantalla resultado")
            param(FirebaseAnalytics.Param.CONTENT_TYPE,"Pantalla 2")
        }

        mp = MediaPlayer.create(this,R.raw.mariachi)
        mp.start()

        val bundle = intent.extras
        val itemSelected = bundle?.getInt("itemSelected", 0)
        val operacion = bundle?.getString("operacion", "")
        val variableA = bundle?.getString("variableA", "")
        val valorA = bundle?.getInt("valorA", 0)
        val variableB = bundle?.getString("variableB", "")
        val valorB = bundle?.getInt("valorB", 0)
        val resultado = bundle?.getFloat("resultado", 0.0F)

        val formula = findViewById<TextView>(R.id.tvFormula)
        supportActionBar!!.title = operacion
        formula.text = operacion

        val tvResVarA = findViewById<TextView>(R.id.tvResVarA)
        tvResVarA.text = variableA

        val tvResValA = findViewById<TextView>(R.id.tvResValA)
        tvResValA.text = valorA.toString()

        val tvResVarB = findViewById<TextView>(R.id.tvResVarB)
        tvResVarB.text = variableB

        val tvResValB = findViewById<TextView>(R.id.tvResValB)
        tvResValB.text = valorB.toString()

        val tvResFinal = findViewById<TextView>(R.id.tvResFinal)
        tvResFinal.text = resultado.toString()



        //Toast.makeText(this, "Operación: $operacion VariableA: $variableA, VariableB: $variableB , resultado: $resultado", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        mp.pause()
    }

    override fun onRestart() {
        super.onRestart()
        mp.start()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}