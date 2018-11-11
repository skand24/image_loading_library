package imagelibrary.sk.com.imagedownload.base

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment<out V : ViewModel> : Fragment() {

    var mActivity: BaseActivity<*>? = null
    private var mViewModel: V? = null
    lateinit var activityContext: AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
        setHasOptionsMenu(false)
    }




    override fun onAttach(context: Context?) {
        performDependencyInjection()
        super.onAttach(context)
        attachFun(context)
    }

    private fun attachFun(context: Context?) {
        if (context is BaseActivity<*>) {
            val activity = context as BaseActivity<*>?
            this.mActivity = activity
            activity!!.onFragmentAttached()
        }
        if (context is AppCompatActivity) {
            activityContext = context
        }
    }


    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    fun getBaseActivity(): BaseActivity<*>? {
        return mActivity
    }


    fun hideKeyboard() {
        if (mActivity != null) {
            mActivity!!.hideKeyboard()
        }
    }


    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    fun getVisibleFragment(): Fragment? {
        if (mActivity != null) {
            return mActivity!!.getVisibleFragment()
        }
        return null
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V


}