package example.com.nm.util.extensions

import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Created by nodo on 8/8/17.
 */
inline fun <T : Any> Observable<T>.toCompletable(): Completable = Completable.fromObservable(this)