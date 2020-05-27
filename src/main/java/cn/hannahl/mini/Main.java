package cn.hannahl.mini;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import net.sf.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws Exception {
        JSONObject content = new JSONObject();
        content.put("PhoneNumbers", "*");
        content.put("TemplateCode", "*");
        JSONObject param = new JSONObject();
        param.put("code", "888888");
        content.put("TemplateParam", param.toString());
        Main.sendSms(content);
    }

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new InputStreamReader(Main.class.getResourceAsStream("/application.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendSms(JSONObject content) {
        DefaultProfile profile = DefaultProfile.getProfile(properties.get("aliyunRegionId") + "",
                properties.get("aliyunAccessKeyId") + "",
                properties.get("aliyunSecret") + "");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(properties.get("aliyunSysDomain") + "");
        request.setSysAction(properties.get("aliyunSysAction") + "");
        request.setSysVersion(properties.get("aliyunSysVersion") + "");
        request.putQueryParameter("RegionId", properties.get("aliyunRegionId") + "");
        request.putQueryParameter("SignName", properties.get("aliyunSignName") + "");

        request.putQueryParameter("PhoneNumbers", content.getString("PhoneNumbers"));
        request.putQueryParameter("TemplateCode", content.getString("TemplateCode"));
        request.putQueryParameter("TemplateParam", content.getString("TemplateParam"));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println("发送阿里云短信接口返回值为：" + response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
