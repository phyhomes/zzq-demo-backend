package com.zzq.common.utils.ip;

import com.zzq.common.config.ProjectConfig;
import com.zzq.common.constant.Constants;
import com.zzq.common.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONObject;

/**
 * @Project : zzq-demo-backend
 * @Time : 2025-12-18 09:09
 * @Author : ZZQ
 * @Desc : 根据ip获取地址的工具类
 */
public class AddressUtils {

    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    // 太平洋网络IP地址查询Web接口
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "内网IP";
        }
        if (ProjectConfig.isIpAddressEnabled())
        {
            try
            {
                String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
                if (StringUtils.isEmpty(rspStr))
                {
                    log.error("获取地理位置失败 {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = (JSONObject) new JSONTokener(rspStr).nextValue();
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            }
            catch (Exception e)
            {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return UNKNOWN;
    }

}
