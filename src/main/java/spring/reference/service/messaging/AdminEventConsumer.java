package spring.reference.service.messaging;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;

import spring.reference.meta.POI;
import spring.reference.meta.POITag;
import spring.reference.util.Logged;

@Logged
@MessageDriven(mappedName = "AdminEventConsumer", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/admin") })
@POI(tags = { POITag.UNEXPECTED_BEHAVIOUR, POITag.STRANGE_BEHAVIOUR },
    value = "According to specification MDBs should be intercepted by CDI, but they aren't. EJB interceptor works.")
public class AdminEventConsumer implements MessageListener {

    @Inject
    private Logger logger;

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                logger.info("Message arrived: " + ((TextMessage) message).getText());
            } catch (JMSException e) {
                logger.warn("Failed to get message text!", e);
            }
        } else {
            logger.info("Non text message arrived: " + message);
        }
    }
}
