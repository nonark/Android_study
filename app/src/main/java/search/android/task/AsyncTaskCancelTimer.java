package search.android.task;

import android.os.AsyncTask;
import android.os.CountDownTimer;

/**
 * Created by nhnent on 2017. 4. 12..
 */

public class AsyncTaskCancelTimer extends CountDownTimer {

    private AsyncTask asyncTask;
    private boolean interrupt;

    public AsyncTaskCancelTimer(AsyncTask asyncTask, long millisInFuture, long countDownInterval, boolean interrupt) {
        super(millisInFuture, countDownInterval);
        this.asyncTask = asyncTask;
        this.interrupt = interrupt;
    }

    @Override
    public void onTick(long millisUntilFinished) {

        if(asyncTask == null) {
            this.cancel();
            return;
        }

        if(asyncTask.isCancelled()) {
            this.cancel();
        }

        if(asyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.cancel();
        }
    }

    @Override
    public void onFinish() {

        if(asyncTask == null || asyncTask.isCancelled()) {
            return;
        }

        if(asyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            return;
        }

        if(asyncTask.getStatus() == AsyncTask.Status.PENDING ||
                asyncTask.getStatus() == AsyncTask.Status.RUNNING ) {
            asyncTask.cancel(interrupt);
        }
    }
}
