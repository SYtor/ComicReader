package ua.syt0r.comicreader.ui.base

import androidx.lifecycle.*

abstract class BasePresenter<View> : LifecycleObserver {

    private var lifecycleOwner: LifecycleOwner? = null
    protected var view: View? = null

    fun attachView(view: View, lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        lifecycleOwner.lifecycle.addObserver(this)
        this.view = view
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onViewDestroyed() {
        view = null
        lifecycleOwner?.lifecycle?.removeObserver(this)
        lifecycleOwner = null
    }

}