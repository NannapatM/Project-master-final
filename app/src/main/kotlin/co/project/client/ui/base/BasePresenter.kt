package co.project.client.ui.base

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the [_view] that
 * can be accessed from the children classes by calling [view].
 */
open class BasePresenter<V : BaseMvp.View> : BaseMvp.Presenter<V> {

    private var _view: V? = null
    val view: V
        get() { return _view ?: throw MvpViewNotAttachedException() }

    override fun attachView(mvpView: V) {
        _view = mvpView
    }

    override fun detachView() {
        _view = null
    }

    class MvpViewNotAttachedException : RuntimeException(
            "Please call Presenter.attachView(BaseMvp) before requesting data to the Presenter")
}
