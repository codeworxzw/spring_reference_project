package spring.reference.service.messaging;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.transaction.annotation.Transactional;

import spring.reference.meta.JBOSS_AS7;
import spring.reference.meta.TODO;
import spring.reference.meta.TODOTag;
import spring.reference.util.Logged;

@Logged
@Named
@Transactional
public class MessagingService {
    @JBOSS_AS7
    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @JBOSS_AS7
    @Resource(mappedName = "java:/queue/admin")
    private Queue queue;

    @TODO(tags = { TODOTag.MAY_CHANGE_IN_THE_FUTURE, TODOTag.JMS_2 }, value = "Probably should use features from JMS 2 (like @JMSContext)")
    public void sendTextMessage(String text) {
        try (Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer messageProducer = session.createProducer(queue);) {

            TextMessage message = session.createTextMessage(text);
            messageProducer.send(message);
        } catch (JMSException e) {
            throw new RuntimeException("Couldn't send text message!", e);
        }

    }

    public void sendChangeEvent(String attributeName, Object oldValue, Object newValue) {
        sendTextMessage(attributeName + " has been changed from " + oldValue + " to " + newValue);
    }
}
