package example.micronaut.controller.org.micronaut.web3j.sample

import example.micronaut.service.EtheriumService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import org.micronaut.web3j.generated.greeter.Greeter
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.utils.Convert

@Controller
@CompileStatic
@Slf4j
class FundsController {

    final EtheriumService etheriumService
    FundsController(EtheriumService etheriumService) {
        this.etheriumService = etheriumService
    }

    HttpResponse<TransactionReceipt> transferFunds(String toAddress, BigDecimal weiAmount) {
        log.info("Sending ${weiAmount} Wei (${Convert.fromWei(weiAmount, Convert.Unit.ETHER).toPlainString()}  Ether)")
        TransactionReceipt receipt = etheriumService.sendFunds(toAddress,weiAmount, Convert.Unit.WEI)
        log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                + receipt.getTransactionHash());
        HttpResponse.ok(receipt)
    }




}
