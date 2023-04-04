package com.qy.ntf.util.wenchang.util;

import com.qy.ntf.util.wenchang.listener.SignEventListener;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

public class UserSignEvent implements SignEventListener {
  private String userPrivateKey;

  public UserSignEvent(String userPrivateKey) {
    this.userPrivateKey = userPrivateKey;
  }

  @Override
  public String signEvent(com.qy.ntf.util.wenchang.listener.SignEvent signEvent) {
    Credentials credentials = Credentials.create(userPrivateKey);
    byte[] signMessage = TransactionEncoder.signMessage(signEvent.getRawTransaction(), credentials);
    return Numeric.toHexString(signMessage);
  }
}
