package site.fxqn.zcl.utils.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class AliyunSMSSender {
    private static final String product = "Dysmsapi";
    private static final String domain = "dysmsapi.aliyuncs.com";
    private String accessKeyId;
    private String accessKeySecret;
    private IClientProfile profile;
    private IAcsClient acsClient;
    private String templateCode;
    private String signName;

    public AliyunSMSSender(String accessKeyId, String accessKeySecret, String templateCode, String signName) throws ClientException {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.templateCode = templateCode;
        this.signName = signName;
        this.profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        this.acsClient = new DefaultAcsClient(profile);
    }

    public void sendVerifyCode(String phone, String code) throws ClientException, AliyunSMSException {
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(this.signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(this.templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            // 发送成功
        } else {
            throw new AliyunSMSException(sendSmsResponse.getMessage());
        }
    }


    /**
     * 示例代码
     *
     * @param args
     */
    public static void main(String[] args) throws ClientException, AliyunSMSException {
        // 全局创建一个即可
        AliyunSMSSender sender = new AliyunSMSSender("id", "secret", "", "");
        sender.sendVerifyCode("13800000000", "");
    }
}
