package com.lunatialiens.incidentreportingsystem.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;


/**
 * The type Network state receiver.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    /**
     * The Connected.
     */
    public Boolean connected;
    /**
     * The Listeners.
     */
    protected Set<NetworkStateReceiverListener> listeners;

    /**
     * Instantiates a new Network state receiver.
     */
    public NetworkStateReceiver() {
        listeners = new HashSet<>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            connected = false;
        }

        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for (NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateReceiverListener listener) {
        if (connected == null || listener == null)
            return;

        if (connected)
            listener.networkAvailable();
        else {
            listener.networkUnavailable();

        }
    }

    /**
     * Add listener.
     *
     * @param l the l
     */
    public void addListener(NetworkStateReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    /**
     * Remove listener.
     *
     * @param l the l
     */
    public void removeListener(NetworkStateReceiverListener l) {
        listeners.remove(l);
    }

    /**
     * The interface Network state receiver listener.
     */
    public interface NetworkStateReceiverListener {
        /**
         * Network available.
         */
        void networkAvailable();

        /**
         * Network unavailable.
         */
        void networkUnavailable();
    }
}