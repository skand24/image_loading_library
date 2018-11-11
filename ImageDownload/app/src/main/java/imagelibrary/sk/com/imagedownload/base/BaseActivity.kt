package imagelibrary.sk.com.imagedownload.base

import android.app.Dialog
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import dagger.android.AndroidInjection

abstract class BaseActivity<out V : ViewModel> : AppCompatActivity(), BaseFragment.Callback {

    private lateinit var mViewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        performDependencyInjection()
        super.onCreate(savedInstanceState)

    }

    fun getVisibleFragment(): Fragment? {
        return null
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    private fun performDependencyInjection() {
        AndroidInjection.inject(this)
        mViewModel = getViewModel()
    }


    fun hideKeyboard() {
        val view: View? = this.currentFocus
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    abstract fun getViewModel(): V


    override fun onDestroy() {
        System.gc()
        super.onDestroy()
    }


}