/*
 * Created by Michael Carrara <michael.carrara@breadwallet.com> on 5/31/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package com.breadwallet.corenative;

import com.breadwallet.corenative.bitcoin.BRChainParams;
import com.breadwallet.corenative.crypto.BRCryptoAccount;
import com.breadwallet.corenative.bitcoin.BRWallet;
import com.breadwallet.corenative.bitcoin.BRWalletManager;
import com.breadwallet.corenative.bitcoin.BRWalletManagerClient;
import com.breadwallet.corenative.crypto.BRCryptoAddress;
import com.breadwallet.corenative.crypto.BRCryptoAmount;
import com.breadwallet.corenative.crypto.BRCryptoCWMClient;
import com.breadwallet.corenative.crypto.BRCryptoCWMListener;
import com.breadwallet.corenative.crypto.BRCryptoCurrency;
import com.breadwallet.corenative.crypto.BRCryptoFeeBasis;
import com.breadwallet.corenative.crypto.BRCryptoHash;
import com.breadwallet.corenative.crypto.BRCryptoNetwork;
import com.breadwallet.corenative.crypto.BRCryptoTransfer;
import com.breadwallet.corenative.crypto.BRCryptoUnit;
import com.breadwallet.corenative.crypto.BRCryptoWallet;
import com.breadwallet.corenative.crypto.BRCryptoWalletManager;
import com.breadwallet.corenative.support.BRAddress;
import com.breadwallet.corenative.bitcoin.BRTransaction;
import com.breadwallet.corenative.ethereum.BREthereumAddress;
import com.breadwallet.corenative.support.BRMasterPubKey;
import com.breadwallet.corenative.support.UInt256;
import com.breadwallet.corenative.support.UInt512;
import com.breadwallet.corenative.utility.SizeT;
import com.breadwallet.corenative.utility.SizeTByReference;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public interface CryptoLibrary extends Library {

    String JNA_LIBRARY_NAME = "crypto";
    NativeLibrary LIBRARY = NativeLibrary.getInstance(CryptoLibrary.JNA_LIBRARY_NAME);
    CryptoLibrary INSTANCE = Native.load(CryptoLibrary.JNA_LIBRARY_NAME, CryptoLibrary.class);

    // bitcoin/BRTransaction.h
    BRTransaction BRTransactionParse(byte[] buf, SizeT bufLen);
    BRTransaction BRTransactionCopy(BRTransaction tx);
    SizeT BRTransactionSerialize(BRTransaction tx, byte[] buf, SizeT bufLen);
    void BRTransactionFree(BRTransaction tx);

    // bitcoin/BRWallet.h
    BRAddress.ByValue BRWalletLegacyAddress(BRWallet wallet);
    int BRWalletContainsAddress(BRWallet wallet, String addr);
    long BRWalletBalance(BRWallet wallet);
    long BRWalletFeePerKb(BRWallet wallet);
    void BRWalletSetFeePerKb(BRWallet wallet, long feePerKb);
    BRTransaction BRWalletCreateTransaction(BRWallet wallet, long amount, String addr);
    long BRWalletAmountReceivedFromTx(BRWallet wallet, BRTransaction tx);
    long BRWalletAmountSentByTx(BRWallet wallet, BRTransaction tx);
    long BRWalletFeeForTx(BRWallet wallet, BRTransaction tx);
    long BRWalletFeeForTxAmount(BRWallet wallet, long amount);

    // bitcoin/BRWalletManager.h
    int bwmAnnounceBlockNumber(BRWalletManager manager, int rid, long blockNumber);
    int bwmAnnounceTransaction(BRWalletManager manager, int id, BRTransaction transaction);
    void bwmAnnounceTransactionComplete(BRWalletManager manager, int id, int success);
    void bwmAnnounceSubmit(BRWalletManager manager, int rid, BRTransaction transaction, int error);
    BRWalletManager BRWalletManagerNew(BRWalletManagerClient.ByValue client, BRMasterPubKey.ByValue mpk, BRChainParams params, int earliestKeyTime, int mode, String storagePath);
    void BRWalletManagerFree(BRWalletManager manager);
    void BRWalletManagerConnect(BRWalletManager manager);
    void BRWalletManagerDisconnect(BRWalletManager manager);
    void BRWalletManagerScan(BRWalletManager manager);
    BRWallet BRWalletManagerGetWallet(BRWalletManager manager);
    void BRWalletManagerGenerateUnusedAddrs(BRWalletManager manager, int limit);
    BRAddress BRWalletManagerGetAllAddrs(BRWalletManager manager, SizeTByReference addressesCount);
    BRAddress BRWalletManagerGetAllAddrsLegacy(BRWalletManager manager, SizeTByReference addressesCount);
    void BRWalletManagerSubmitTransaction(BRWalletManager manager, BRTransaction transaction, byte[] seed, SizeT seedLen);

    // crypto/BRCryptoAccount.h
    UInt512.ByValue cryptoAccountDeriveSeed(String phrase);
    BRCryptoAccount cryptoAccountCreate(String paperKey);
    BRCryptoAccount cryptoAccountCreateFromSeedBytes(byte[] bytes);
    long cryptoAccountGetTimestamp(BRCryptoAccount account);
    void cryptoAccountSetTimestamp(BRCryptoAccount account, long timestamp);
    void cryptoAccountGive(BRCryptoAccount obj);

    // crypto/BRCryptoAddress.h
    Pointer cryptoAddressAsString(BRCryptoAddress address);
    int cryptoAddressIsIdentical(BRCryptoAddress a1, BRCryptoAddress a2);
    BRCryptoAddress cryptoAddressTake(BRCryptoAddress obj);
    void cryptoAddressGive(BRCryptoAddress obj);

    // crypto/BRCryptoAmount.h
    BRCryptoAmount cryptoAmountCreateDouble(double value, BRCryptoUnit unit);
    BRCryptoAmount cryptoAmountCreateInteger(long value, BRCryptoUnit unit);
    BRCryptoAmount cryptoAmountCreateString(String value, int isNegative, BRCryptoUnit unit);
    BRCryptoCurrency cryptoAmountGetCurrency(BRCryptoAmount amount);
    int cryptoAmountIsNegative(BRCryptoAmount amount);
    int cryptoAmountIsCompatible(BRCryptoAmount a1, BRCryptoAmount a2);
    int cryptoAmountCompare(BRCryptoAmount a1, BRCryptoAmount a2);
    BRCryptoAmount cryptoAmountAdd(BRCryptoAmount a1, BRCryptoAmount a2);
    BRCryptoAmount cryptoAmountSub(BRCryptoAmount a1, BRCryptoAmount a2);
    BRCryptoAmount cryptoAmountNegate(BRCryptoAmount amount);
    double cryptoAmountGetDouble(BRCryptoAmount amount, BRCryptoUnit unit, IntByReference overflow);
    long cryptoAmountGetIntegerRaw(BRCryptoAmount amount, IntByReference overflow);
    UInt256.ByValue cryptoAmountGetValue(BRCryptoAmount amount);
    void cryptoAmountGive(BRCryptoAmount obj);

    // crypto/BRCryptoCurrency.h
    Pointer cryptoCurrencyGetUids(BRCryptoCurrency currency);
    Pointer cryptoCurrencyGetName(BRCryptoCurrency currency);
    Pointer cryptoCurrencyGetCode(BRCryptoCurrency currency);
    Pointer cryptoCurrencyGetType(BRCryptoCurrency currency);
    int cryptoCurrencyIsIdentical(BRCryptoCurrency c1, BRCryptoCurrency c2);
    void cryptoCurrencyGive(BRCryptoCurrency obj);

    // crypto/BRCryptoFeeBasis.h
    int cryptoFeeBasisGetType(BRCryptoFeeBasis feeBasis);
    BRCryptoFeeBasis cryptoFeeBasisTake(BRCryptoFeeBasis obj);
    void cryptoFeeBasisGive(BRCryptoFeeBasis obj);

    // crypto/BRCryptoHash.h
    int cryptoHashEqual(BRCryptoHash h1, BRCryptoHash h2);
    Pointer cryptoHashString(BRCryptoHash hash);
    int cryptoHashGetHashValue(BRCryptoHash hash);
    BRCryptoHash cryptoHashTake(BRCryptoHash obj);
    void cryptoHashGive(BRCryptoHash obj);

    // crypto/BRCryptoNetwork.h
    int cryptoNetworkGetType(BRCryptoNetwork network);
    Pointer cryptoNetworkGetUids(BRCryptoNetwork network);
    Pointer cryptoNetworkGetName(BRCryptoNetwork network);
    int cryptoNetworkIsMainnet(BRCryptoNetwork network);
    BRCryptoCurrency cryptoNetworkGetCurrency(BRCryptoNetwork network);
    BRCryptoUnit cryptoNetworkGetUnitAsDefault(BRCryptoNetwork network, BRCryptoCurrency currency);
    BRCryptoUnit cryptoNetworkGetUnitAsBase(BRCryptoNetwork network, BRCryptoCurrency currency);
    long cryptoNetworkGetHeight(BRCryptoNetwork network);
    SizeT cryptoNetworkGetCurrencyCount(BRCryptoNetwork network);
    BRCryptoCurrency cryptoNetworkGetCurrencyAt(BRCryptoNetwork network, SizeT index);
    int cryptoNetworkHasCurrency(BRCryptoNetwork network, BRCryptoCurrency currency);
    BRCryptoCurrency cryptoNetworkGetCurrencyForSymbol(BRCryptoNetwork network, String symbol);
    SizeT cryptoNetworkGetUnitCount(BRCryptoNetwork network, BRCryptoCurrency currency);
    BRCryptoUnit cryptoNetworkGetUnitAt(BRCryptoNetwork network, BRCryptoCurrency currency, SizeT index);
    BRCryptoNetwork cryptoNetworkTake(BRCryptoNetwork obj);
    void cryptoNetworkGive(BRCryptoNetwork obj);

    // crypto/BRCryptoPrivate.h
    BRMasterPubKey.ByValue cryptoAccountAsBTC(BRCryptoAccount account);
    BRCryptoAddress cryptoAddressCreateAsBTC(BRAddress.ByValue btc);
    BRCryptoAddress cryptoAddressCreateAsETH(BREthereumAddress.ByValue address);
    BRCryptoAmount cryptoAmountCreate (BRCryptoCurrency currency, int isNegative, UInt256.ByValue value);
    BRCryptoCurrency cryptoCurrencyCreate(String uids, String name, String code, String type);
    void cryptoNetworkSetHeight(BRCryptoNetwork network, long height);
    void cryptoNetworkSetCurrency(BRCryptoNetwork network, BRCryptoCurrency currency);
    BRCryptoNetwork cryptoNetworkCreateAsBTC(String uids, String name, byte forkId, Pointer params);
    BRCryptoNetwork cryptoNetworkCreateAsETH(String uids, String name, int chainId, Pointer net);
    void cryptoNetworkAddCurrency(BRCryptoNetwork network, BRCryptoCurrency currency, BRCryptoUnit baseUnit, BRCryptoUnit defaultUnit);
    void cryptoNetworkAddCurrencyUnit(BRCryptoNetwork network, BRCryptoCurrency currency, BRCryptoUnit unit);
    BRCryptoUnit cryptoUnitCreateAsBase(BRCryptoCurrency currency, String uids, String name, String symbol);
    BRCryptoUnit cryptoUnitCreate(BRCryptoCurrency currency, String uids, String name, String symbol, BRCryptoUnit base, byte decimals);

    // crypto/BRCryptoTransfer.h
    BRCryptoAddress cryptoTransferGetSourceAddress(BRCryptoTransfer transfer);
    BRCryptoAddress cryptoTransferGetTargetAddress(BRCryptoTransfer transfer);
    BRCryptoAmount cryptoTransferGetAmount(BRCryptoTransfer transfer);
    BRCryptoAmount cryptoTransferGetFee(BRCryptoTransfer transfer);
    int cryptoTransferGetDirection(BRCryptoTransfer transfer);
    BRCryptoHash cryptoTransferGetHash(BRCryptoTransfer transfer);
    BRCryptoFeeBasis cryptoTransferGetFeeBasis(BRCryptoTransfer transfer);
    int cryptoTransferEqual(BRCryptoTransfer transfer1, BRCryptoTransfer transfer2);
    BRCryptoTransfer cryptoTransferTake(BRCryptoTransfer obj);
    void cryptoTransferGive(BRCryptoTransfer obj);

    // crypto/BRCryptoUnit.h
    Pointer cryptoUnitGetUids(BRCryptoUnit unit);
    Pointer cryptoUnitGetName(BRCryptoUnit unit);
    Pointer cryptoUnitGetSymbol(BRCryptoUnit unit);
    BRCryptoCurrency cryptoUnitGetCurrency(BRCryptoUnit unit);
    BRCryptoUnit cryptoUnitGetBaseUnit(BRCryptoUnit unit);
    byte cryptoUnitGetBaseDecimalOffset(BRCryptoUnit unit);
    int cryptoUnitIsCompatible(BRCryptoUnit u1, BRCryptoUnit u2);
    int cryptoUnitIsIdentical(BRCryptoUnit u1, BRCryptoUnit u2);
    BRCryptoUnit cryptoUnitTake(BRCryptoUnit obj);
    void cryptoUnitGive(BRCryptoUnit obj);

    // crypto/BRCryptoWallet.h
    int cryptoWalletGetState(BRCryptoWallet wallet);
    void cryptoWalletSetState(BRCryptoWallet wallet, int state);
    BRCryptoCurrency cryptoWalletGetCurrency(BRCryptoWallet wallet);
    BRCryptoUnit cryptoWalletGetUnitForFee(BRCryptoWallet wallet);
    BRCryptoAmount cryptoWalletGetBalance(BRCryptoWallet wallet);
    SizeT cryptoWalletGetTransferCount(BRCryptoWallet wallet);
    void cryptoWalletAddTransfer(BRCryptoWallet wallet, BRCryptoTransfer transfer);
    void cryptoWalletRemTransfer(BRCryptoWallet wallet, BRCryptoTransfer transfer);
    BRCryptoTransfer cryptoWalletGetTransfer(BRCryptoWallet wallet, SizeT index);
    BRCryptoAddress cryptoWalletGetAddress(BRCryptoWallet wallet);
    BRCryptoFeeBasis cryptoWalletGetDefaultFeeBasis(BRCryptoWallet wallet);
    void cryptoWalletSetDefaultFeeBasis(BRCryptoWallet wallet, BRCryptoFeeBasis feeBasis);
    BRCryptoWallet cryptoWalletTake(BRCryptoWallet obj);
    void cryptoWalletGive(BRCryptoWallet obj);
    BRCryptoTransfer cryptoWalletCreateTransfer(BRCryptoWallet wallet, BRCryptoAddress target, BRCryptoAmount amount, BRCryptoFeeBasis feeBasis);
    BRCryptoAmount cryptoWalletEstimateFee(BRCryptoWallet wallet, BRCryptoAmount amount, BRCryptoFeeBasis feeBasis, BRCryptoUnit feeUnit);
    int cryptoWalletEqual(BRCryptoWallet w1, BRCryptoWallet w2);

    // crypto/BRCryptoWalletManager.h
     BRCryptoWalletManager cryptoWalletManagerCreate(BRCryptoCWMListener.ByValue listener, BRCryptoCWMClient.ByValue client, BRCryptoAccount account, BRCryptoNetwork network, int mode, String path);
     BRCryptoNetwork cryptoWalletManagerGetNetwork(BRCryptoWalletManager cwm);
     BRCryptoAccount cryptoWalletManagerGetAccount(BRCryptoWalletManager cwm);
     int cryptoWalletManagerGetMode(BRCryptoWalletManager cwm);
     int cryptoWalletManagerGetState(BRCryptoWalletManager cwm);
     Pointer cryptoWalletManagerGetPath(BRCryptoWalletManager cwm);
     BRCryptoWallet cryptoWalletManagerGetWallet(BRCryptoWalletManager cwm);
     SizeT cryptoWalletManagerGetWalletsCount(BRCryptoWalletManager cwm);
     BRCryptoWallet cryptoWalletManagerGetWalletAtIndex(BRCryptoWalletManager cwm, SizeT index);
     BRCryptoWallet cryptoWalletManagerGetWalletForCurrency(BRCryptoWalletManager cwm, BRCryptoCurrency currency);
     void cryptoWalletManagerAddWallet(BRCryptoWalletManager cwm, BRCryptoWallet wallet);
     void cryptoWalletManagerRemWallet(BRCryptoWalletManager cwm, BRCryptoWallet wallet);
     void cryptoWalletManagerConnect(BRCryptoWalletManager cwm);
     void cryptoWalletManagerDisconnect(BRCryptoWalletManager cwm);
     void cryptoWalletManagerSync(BRCryptoWalletManager cwm);
     void cryptoWalletManagerSubmit(BRCryptoWalletManager cwm, BRCryptoWallet wid, BRCryptoTransfer tid, String paperKey);

    // ethereum/base/BREthereumAddress.h
    BREthereumAddress.ByValue addressCreate(String address);
    int addressValidateString(String addr);

    // ethereum/util/BRUtilMath.h
    UInt256.ByValue createUInt256(long value);
    Pointer coerceStringPrefaced(UInt256.ByValue value, int base, String preface);

    // support/BRAddress.h
    int BRAddressIsValid(String addr);
}
