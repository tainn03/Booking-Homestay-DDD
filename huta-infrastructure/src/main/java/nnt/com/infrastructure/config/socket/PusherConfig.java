package nnt.com.infrastructure.config.socket;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfig {

    @Bean
    public Pusher pusher() {
        Pusher pusher = new Pusher(
                "${PUSHER_APP_ID}",
                "${PUSHER_KEY}",
                "${PUSHER_SECRET}"
        );
        pusher.setCluster("${PUSHER_CLUSTER}");
        pusher.setEncrypted(true);
        return pusher;
    }
}