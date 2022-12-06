package ru.alfacampus.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<RiskPointsView>(R.id.custom_view).setOnClickListener {
            (it as RiskPointsView).setRiskCount(3)
        }
//        findViewById<CounterView>(R.id.counter).setCounter(10)
    }
}