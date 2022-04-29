package com.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.lifecycle.LifecycleCoroutineScope
//import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay

fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.shortToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

//fun ImageView.loadImage(image:String){
//    Glide.with(this).load(image).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(this)
//}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

@SuppressLint("ResourceType")
inline fun View.snack(
    @IntegerRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.showSnack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}

fun View.appBackSnack(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    val snack = Snackbar.make(this, message, length)
    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(Color.parseColor("#0B3089"))
    snack.show()
}

fun View.errorSnack(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    val snack = Snackbar.make(this, message, length)
    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(Color.parseColor("#DF1E26"))
    snack.show()
}

@SuppressLint("ResourceType")
fun Snackbar.action(@IntegerRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

inline fun View.errorSnackAction(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.setActionTextColor(Color.parseColor("#FFFFFF"))
    snack.view.setBackgroundColor(Color.parseColor("#978FEA"))
    snack.show()
}


suspend fun ViewPager2.scrollIndefinitely(interval: Long) {

    delay(interval)
    val numberOfItems = adapter?.itemCount ?: 0
    val lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
    val nextItem = if (currentItem == lastIndex) 0 else currentItem + 1

    setCurrentItem(nextItem, true)

    scrollIndefinitely(interval)
}

fun ViewPager2.autoScroll(lifecycleScope: LifecycleCoroutineScope, interval: Long) {
    lifecycleScope.launchWhenResumed {
        scrollIndefinitely(interval)
    }
}

