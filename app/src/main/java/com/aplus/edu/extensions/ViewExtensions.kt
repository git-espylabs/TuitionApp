package com.aplus.edu.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.aplus.edu.R
import com.google.android.material.snackbar.Snackbar

/**
 * View extension methods
 */

/**
 * snack - extension to display a snack message
 */
fun View.snack(message: String?, length: Int = Snackbar.LENGTH_LONG) : Snackbar {
    var snackBar : Snackbar? = null
    this.takeIf { null != message }?.apply {
        val snack = Snackbar.make(this, message!!, length)
        val layout = snack.view as Snackbar.SnackbarLayout
        val textView = layout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

        snack.duration = 5000
        textView.maxLines = 10

        snack.show()

        snackBar = snack;
    }

    return snackBar!!;
}

/**
 * inflate - Extension method to inflate a view from resource identifier
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

infix fun TextView.setSelectedMode(isSelected: Boolean){
    val typefaceNormal = ResourcesCompat.getFont(context, R.font.poppins_regular)
    val typefaceSelected = ResourcesCompat.getFont(context, R.font.poppins_medium)
    when{
        isSelected ->{
            setBackgroundResource(R.drawable.rect_bg_white_tabs_vw)
            typeface = typefaceSelected
            setTextColor(ContextCompat.getColor(context, R.color.btn_clr1))
        }
        else -> {
            setBackgroundResource(android.R.color.transparent)
            typeface = typefaceNormal
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }
    }
}