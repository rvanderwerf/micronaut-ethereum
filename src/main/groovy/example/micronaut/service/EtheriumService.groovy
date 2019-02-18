package example.micronaut.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Value
import org.micronaut.web3j.generated.greeter.Greeter
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Contract
import org.web3j.tx.ManagedTransaction
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import org.web3j.utils.Numeric

import javax.annotation.PostConstruct
import javax.inject.Singleton

@Singleton
@CompileStatic
@Slf4j
class EtheriumService {

    Credentials credentials
    Web3j web3j

    @Value("etherium.keyPassword:changeme")
    String keyPassword

    @Value("etherium.keyFile:~/.etherium/chaindata/keystore/changeme")
    String keyStoreFile

    @PostConstruct
    void init() {
        credentials =
                WalletUtils.loadCredentials(
                        keyPassword,keyPassword);
        web3j = Web3j.build(new HttpService());

    }

    /**
     * send funds in Wei to the given address
     * @param toAddress
     * @param amount
     * @param unit
     * @return
     */
    TransactionReceipt sendFunds(String toAddress, BigDecimal amount, Convert.Unit unit) {

        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                toAddress,  // you can put any address here
                amount, unit)  // 1 wei = 10^-18 Ether
                .send();
        transferReceipt
    }

    /**
     * deploy the greeter contract to the blockchain
     * @param greeting
     * @return
     */
    Greeter deployGreeterContract(String greeting) {
        log.info("Deploying smart contract");
        Greeter contract = Greeter.deploy(
                web3j, credentials,
                ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT,
                greeting).send()
        contract
    }

    /**
     * update the contract in the blockchain with a new greeting
     * @param contract
     * @param greeting
     * @return
     */
    TransactionReceipt updateGreeting(Greeter contract, String greeting) {
        TransactionReceipt transactionReceipt = contract.newGreeting(greeting).send()
        transactionReceipt
    }

    List<Greeter.ModifiedEventResponse> getUpdatedEvents(Greeter greeter, TransactionReceipt receipt) {

        List<Greeter.ModifiedEventResponse> events = greeter.getModifiedEvents(receipt)
        events.each {   Greeter.ModifiedEventResponse event ->
            log.info("Modify event fired, previous value: " + event.oldGreeting
                    + ", new value: " + event.newGreeting)
            log.info("Indexed event previous value: " + Numeric.toHexString(event.oldGreetingIdx)
                    + ", new value: " + Numeric.toHexString(event.newGreetingIdx))
        }
        events
    }




}
