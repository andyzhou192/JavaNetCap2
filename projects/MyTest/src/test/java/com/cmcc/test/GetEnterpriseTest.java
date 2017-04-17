package com.cmcc.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.asserts.AssertResult;
import com.asserts.Asserts;
import com.data.provider.testng.ExcelDataProvider;
import com.protocol.httpclient.HttpClientImpl;

/**
 * This code is generated by FreeMarker
 * @author zhouyelin
 * @date 2017-4-17 9:24:08
 * Description: test class description
 */
public class GetEnterpriseTest{

	private static Log LOGGER = LogFactory.getLog(GetEnterpriseTest.class);

    /********** test method ***********/
    @Test(groups={"SMOKE"}, dataProvider="defaultMethod", dataProviderClass=ExcelDataProvider.class)
    public void testGetEnterprise(String CaseID, String CaseDesc, String Method, String URL, String ReqHeader, String ReqParams, String StatusCode, String ReasonPhrase, String RspHeader, String RspBody) {
    	LOGGER.info("########## " + CaseID + ": " + CaseDesc + " ##########"+" BEGIN ##########");
		try {
			HttpClientImpl client = new HttpClientImpl(URL, ReqHeader, ReqParams);
			HttpResponse rsp = client.doRequest(Method);
			
			Assert.assertEquals(String.valueOf(rsp.getStatusLine().getStatusCode()), StatusCode);
			Assert.assertEquals(rsp.getStatusLine().getReasonPhrase(), ReasonPhrase);
			if(null != RspHeader && RspHeader.trim().length() > 0){
				AssertResult result = Asserts.assertJson(client.convertHeards2Json(rsp.getAllHeaders()), RspHeader);
				Assert.assertTrue(result.isSucc(), result.getMessage());
			}
			if(null != RspBody && RspBody.trim().length() > 0){
				AssertResult result = Asserts.assertJson(EntityUtils.toString(rsp.getEntity()), RspBody);
				Assert.assertTrue(result.isSucc(), result.getMessage());
			}
		} catch(Exception e) {
			Assert.fail(e.toString());
		}finally{
			LOGGER.info("########## " + CaseID + ": " + CaseDesc + " ##########"+" END ##########");
		}		
    }

}
