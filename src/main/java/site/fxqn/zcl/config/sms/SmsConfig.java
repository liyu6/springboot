package site.fxqn.zcl.config.sms;
/*
    作者: kat-qian
    日期: 2018/5/31 12:22
*/

import com.aliyuncs.exceptions.ClientException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.fxqn.zcl.utils.sms.AliyunSMSSender;

@Configuration
public class SmsConfig {

    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;
    @Value("${aliyun.sms.access-key-secret}")
    private String accesskeySecret;
    @Value("${aliyun.sms.template-code}")
    private String templateCode;
    @Value("${aliyun.sms.sign-name}")
    private String signName;

    @Bean
    @SneakyThrows
    public AliyunSMSSender aliyunSMSSender() throws ClientException {
        return new AliyunSMSSender(accessKeyId, accesskeySecret, templateCode, signName);
    }
}
