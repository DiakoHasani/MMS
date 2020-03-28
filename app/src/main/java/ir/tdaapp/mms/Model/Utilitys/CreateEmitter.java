package ir.tdaapp.mms.Model.Utilitys;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.plugins.RxJavaPlugins;

//این کلاس برای زمانی است که در آر ایکس جاوا خطای رخ دهد که زمان وقوع خطا این کلاس اجازه بسته شدن برنامه را نمی دهد و خطای آن را برگشت می دهد که خطایش در سنترال اکتیویتی گرفته می شود
public final class CreateEmitter<T> extends AtomicReference<Disposable> implements ObservableEmitter<T>, Disposable {
    @Override
    public void setDisposable(Disposable d) {

    }

    @Override
    public void setCancellable(Cancellable c) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isDisposed() {
        return false;
    }

    @Override
    public ObservableEmitter<T> serialize() {
        return null;
    }

    @Override
    public boolean tryOnError(Throwable t) {
        if (t == null) {
            t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        if (!isDisposed()) {
            try {
                onError(t);
            } finally {
                dispose();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable error) {
        if (!tryOnError(error)) {
            RxJavaPlugins.onError(error);
        }
    }

    @Override
    public void onComplete() {

    }
}
