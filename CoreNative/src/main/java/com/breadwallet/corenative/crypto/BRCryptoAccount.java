/*
 * Created by Michael Carrara <michael.carrara@breadwallet.com> on 5/31/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package com.breadwallet.corenative.crypto;

import com.breadwallet.corenative.CryptoLibrary;
import com.breadwallet.corenative.support.BRMasterPubKey;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BRCryptoAccount extends PointerType implements CoreBRCryptoAccount {

    public BRCryptoAccount(Pointer address) {
        super(address);
    }

    public BRCryptoAccount() {
        super();
    }

    @Override
    public Date getEarliestKeyTime() {
        return new Date(TimeUnit.SECONDS.toMillis(CryptoLibrary.INSTANCE.cryptoAccountGetTimestamp(this)));
    }

    @Override
    public void setEarliestKeyTime(Date earliestKeyTime) {
        CryptoLibrary.INSTANCE.cryptoAccountSetTimestamp(this, TimeUnit.MILLISECONDS.toSeconds(earliestKeyTime.getTime()));
    }

    @Override
    public BRMasterPubKey.ByValue asBtc() {
        return CryptoLibrary.INSTANCE.cryptoAccountAsBTC(this);
    }

    @Override
    public BRCryptoAccount asBRCryptoAccount() {
        return this;
    }
}
