package com.qy.ntf.util.wenchang.listener;

public interface SignEventListener {

    /**
     * Transaction signature event
     * The SDK will call the concrete implementation of this event to sign all transactions sent to the chain
     *
     * @param event signature event parameter
     * @return returns the signed transaction string
     */
    String signEvent(SignEvent event);
}
