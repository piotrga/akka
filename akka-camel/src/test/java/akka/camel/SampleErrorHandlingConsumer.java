package akka.camel;

import akka.camel.javaapi.UntypedConsumerActor;
import org.apache.camel.builder.Builder;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import scala.Option;

/**
 * @author Martin Krasser
 */
public class SampleErrorHandlingConsumer extends UntypedConsumerActor {

    public String getEndpointUri() {
        return "direct:error-handler-test-java";
    }

    @Override
    //TODO write test confirming this gets called in java
    public ProcessorDefinition onRouteDefinition(RouteDefinition rd) {
        return rd.onException(Exception.class).handled(true).transform(Builder.exceptionMessage()).end();
    }

    @Override
    public CommunicationStyle communicationStyle(){
        return Blocking.seconds(1);
    }

    public void onReceive(Object message) throws Exception {
        Message msg = (Message) message;
        String body = msg.getBodyAs(String.class);
        throw new Exception(String.format("error: %s", body));
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message){
        getSender().tell(new Failure(reason));
    }

}
