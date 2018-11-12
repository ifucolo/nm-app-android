package example.com.nm.util.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

 operator fun CompositeDisposable.plusAssign(subscribe: Disposable?) {}
