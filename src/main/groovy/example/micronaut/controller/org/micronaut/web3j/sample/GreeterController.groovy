package example.micronaut.controller.org.micronaut.web3j.sample

import example.micronaut.service.EtheriumService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import org.micronaut.web3j.generated.greeter.Greeter
import org.web3j.protocol.core.methods.response.TransactionReceipt

@Controller
@CompileStatic
@Slf4j
class GreeterController {

    final EtheriumService etheriumService

    GreeterController(EtheriumService etheriumService) {
        this.etheriumService = etheriumService
    }

    HttpResponse<Greeter> deployGreeterContract(String greeting) {

        Greeter greeter = etheriumService.deployGreeterContract(greeting)
        HttpResponse.ok(greeter)

    }

    HttpResponse<TransactionReceipt> updateGreeting(Greeter greeter, String greeting) {
        TransactionReceipt receipt = etheriumService.updateGreeting(greeter,greeting)
        HttpResponse.ok(receipt)
    }

    HttpResponse<List<Greeter.ModifiedEventResponse>> getUpdatedEvents(Greeter greeter, TransactionReceipt receipt) {
        HttpResponse.ok(etheriumService.getUpdatedEvents(greeter, receipt))
    }
}
