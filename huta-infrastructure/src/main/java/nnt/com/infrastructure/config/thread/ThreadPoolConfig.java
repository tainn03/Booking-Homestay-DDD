package nnt.com.infrastructure.config.thread;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //Số lượng luồng cốt lõi, mặc định là 5
        threadPool.setCorePoolSize(10);
        //Số lượng luồng tối đa, mặc định là 10
        threadPool.setMaxPoolSize(50);
        //Độ dài tối đa của hàng đợi, thường cần thiết lập giá trị đủ lớn
        threadPool.setQueueCapacity(Integer.MAX_VALUE);
        //Thời gian chờ tối đa của luồng trong bộ nhớ cache, mặc định là 60s
        threadPool.setKeepAliveSeconds(60);
        //Cho phép đóng luồng hết thời gian chờ
        threadPool.setAllowCoreThreadTimeOut(true);
        threadPool.initialize();
        return threadPool;
    }
}
