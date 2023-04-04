package com.qy.ntf.util.wenchang.util;

import com.qy.ntf.util.ResourcesUtil;
import com.qy.ntf.util.wenchang.listener.SignEventListener;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

public class SignEvent implements SignEventListener {

  @Override
  public String signEvent(com.qy.ntf.util.wenchang.listener.SignEvent signEvent) {
    Credentials credentials =
        //
        // Credentials.create("db3d9d8df32ee14e9791860ca16e1a30b75efc8234998aef2b29b1fcbaa8a3f3");
        Credentials.create(ResourcesUtil.getProperty("bsn_plate_pri_key"));
    byte[] signMessage = TransactionEncoder.signMessage(signEvent.getRawTransaction(), credentials);
    return Numeric.toHexString(signMessage);
  }
}
