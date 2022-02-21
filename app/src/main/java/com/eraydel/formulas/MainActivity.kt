package com.eraydel.formulas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.eraydel.formulas.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var analytics : FirebaseAnalytics
    private var itemSelected : Int = 0
    private var result : Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analytics = Firebase.analytics

        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT){
            param(FirebaseAnalytics.Param.ITEM_ID,"1")
            param(FirebaseAnalytics.Param.ITEM_NAME,"Pantalla principal")
            param(FirebaseAnalytics.Param.CONTENT_TYPE,"Pantalla")
        }

        setUpVariables(
            R.drawable.smart_kid ,
            resources.getString(R.string.bienvenido)
        )

        val formulas = resources.getStringArray(R.array.listado_formulas)

        val adapter = ArrayAdapter(
            this,
            R.layout.list_item,
            formulas
        )

        with(binding.actvFormulas){
            setAdapter(adapter)
            onItemClickListener = this@MainActivity
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        itemSelected = position
        formulaSeleccionada(itemSelected)
    }

    private fun formulaSeleccionada(opcion: Int) {
        when(opcion){
            0 -> {
                setUpVariables(
                    R.drawable.ley_ohm,
                    resources.getString(R.string.str_descripcion1),
                    resources.getString(R.string.str_mensaje1),
                    resources.getString(R.string.str_varA11),
                    resources.getString(R.string.str_varA12),
                    true
                )
            }
            1 -> {
                setUpVariables(
                    R.drawable.volumen_cilindro,
                    resources.getString(R.string.str_descripcion2),
                    resources.getString(R.string.str_mensaje2),
                    resources.getString(R.string.str_varA21),
                    resources.getString(R.string.str_varA22),
                    true
                )
            }
            2 -> {
                setUpVariables(
                    R.drawable.area_triangulo,
                    resources.getString(R.string.str_descripcion3),
                    resources.getString(R.string.str_mensaje3),
                    resources.getString(R.string.str_varA31),
                    resources.getString(R.string.str_varA32),
                    true
                )
            }
        }
    }

    private fun setUpVariables(resource : Int , description: String , textMensaje: String = "" , varA : String = "" , varB: String = "" , showBotton: Boolean = false){
        binding.ivFormulaSeleccionada.setImageResource(resource)
        binding.tvDescripcion.text = description
        binding.tvMensaje.text=textMensaje
        binding.etVariableA.hint = varA
        binding.etVariableB.hint = varB
        binding.labelA.text = varA
        binding.labelB.text = varB
        if(!showBotton) binding.etVariableA.visibility = View.INVISIBLE else binding.etVariableA.visibility = View.VISIBLE
        if(!showBotton) binding.etVariableB.visibility = View.INVISIBLE else binding.etVariableB.visibility = View.VISIBLE
        if(showBotton) binding.btnCalcular.visibility = View.VISIBLE else binding.btnCalcular.visibility = View.INVISIBLE
    }

    fun click(view: View) {
        if(validaCampos()){
            ejecutaOperacion()
            analytics.logEvent("clic_botón"){
                param("accion" , "click")
                param("operacion", binding.tvMensaje.text.toString() )
                param("texto_botón","Realizar cálculo")
            }
        }
        else {
            Toast.makeText(this,getString(R.string.ingresa_valor),Toast.LENGTH_SHORT).show()
            if(binding.etVariableA.text.toString() == "" ) {
                binding.etVariableA.error = getString(R.string.valor_requerido)
                binding.etVariableA.requestFocus()
            }
            if(binding.etVariableB.text.toString() == "" ) {
                binding.etVariableB.error = getString(R.string.valor_requerido)
                binding.etVariableB.requestFocus()
            }
        }

    }

    private fun validaCampos(): Boolean {
        if( binding.etVariableA.text.toString() == "" || binding.etVariableB.text.toString() == "" ) return false
        if( binding.etVariableA.text.toString() == "" && binding.etVariableB.text.toString() != "" ) return false
        if( binding.etVariableA.text.toString() != "" && binding.etVariableB.text.toString() == "" ) return false
        else return true
    }

    private fun ejecutaOperacion(){

        val variableA = Integer.parseInt(binding.etVariableA.text.toString())
        val variableB = Integer.parseInt(binding.etVariableB.text.toString())

        when (itemSelected){
            0 -> {
                result = (variableA.toFloat() * variableB.toFloat())
            }
            1 -> {
                val pi : Float = 3.141539F
                result = ((pi * variableA.toFloat()) * (variableB.toFloat()*variableB.toFloat()))
            }
            2 -> {
                result = ((variableA.toFloat() * variableB.toFloat())/2)
            }
        }

        val intent = Intent(this, Resultado::class.java)
        val parametros = Bundle()

        parametros.putString("itemSelected", itemSelected.toString())
        parametros.putString("operacion", binding.tvMensaje.text.toString())
        parametros.putString("variableA", binding.etVariableA.hint.toString())
        parametros.putInt("valorA", variableA)
        parametros.putString("variableB", binding.etVariableB.hint.toString())
        parametros.putInt("valorB", variableB)
        parametros.putFloat("resultado", result)
        intent.putExtras(parametros)
        startActivity(intent)
    }

}