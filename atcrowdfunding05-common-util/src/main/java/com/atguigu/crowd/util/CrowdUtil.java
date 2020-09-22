package com.atguigu.crowd.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.atguigu.crowd.constant.CrowdConstant;

/**
 * General tools and methods
 * @author
 *
 */
public class CrowdUtil {
	
/**
* Tools and methods responsible for uploading files to OSS server
* @param endpoint OSS parameters
* @param accessKeyId OSS parameter
* @param accessKeySecret OSS parameter
* @param inputStream The input stream of the file to be uploaded
* @param bucketName OSS parameter
* @param bucketDomain OSS parameters
* @param originalName The original file name of the file to be uploaded
* @return contains the upload result and the access path of the uploaded file on OSS
*/
	public static ResultEntity<String> uploadFileToOss(
			String endpoint, 
			String accessKeyId, 
			String accessKeySecret,
			InputStream inputStream,
			String bucketName,
			String bucketDomain,
			String originalName) {
		
				// Create an OSSClient instance.
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		
		// Generate a directory for uploading files
		String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		// Generate the file name of the uploaded file when it is saved on the OSS server
		// Original file name: beautifulgirl.jpg
		// Generated file name: wer234234efwer235346457dfswet346235.jpg
		// Use UUID to generate file body name
		String fileMainName = UUID.randomUUID().toString().replace("-", "");
		
		// Get the file extension from the original file name

		String extensionName = originalName.substring(originalName.lastIndexOf("."));
		
		// Use directory, file main name, file extension name to splice to get object name
		String objectName = folderName + "/" + fileMainName + extensionName;
		
		try {
			// Call the method of the OSS client object to upload the file and get the response result data
			PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);
			
			// Obtain the specific response message from the response result
			ResponseMessage responseMessage = putObjectResult.getResponse();
			
			// Determine whether the request is successful according to the response status code

			if(responseMessage == null) {
				
				// Stitch the path to access the file just uploaded
				String ossFileAccessPath = bucketDomain + "/" + objectName;
				
				// The current method returns success
				return ResultEntity.successWithData(ossFileAccessPath);
			} else {
				// Get response status code
				int statusCode = responseMessage.getStatusCode();
				
				// If the request is not successful, get the error message
				String errorMessage = responseMessage.getErrorResponseAsString();
				
				// The current method returns failed
				return ResultEntity.failed("当前响应状态码="+statusCode+" 错误消息="+errorMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			// The current method returns failed
			return ResultEntity.failed(e.getMessage());
		} finally {
			
			if(ossClient != null) {
				
				// Close OSSClient.
				ossClient.shutdown();
			}
		}
		
	}
	
/**
* Send a request to the remote third-party SMS interface to send the verification code to the user's mobile phone
* @param host URL address called by SMS interface
* @param path The specific address for sending SMS
* @param method request method
* @param phoneNum mobile phone number to receive SMS
* @param appCode is used to call the AppCode of the third-party SMS API
* @param sign signature number
* @param skin template number
* @return returns whether the call result is successful
* Success: return verification code
* Failure: return a failure message
* Status code: 200 normal; 400 URL invalid; 401 appCode error; 403 times used up; 500 API network management error
*/
	public static ResultEntity<String> sendCodeByShortMessage(
			
			String host,
			
			String path,
			
			String method,
			
			String phoneNum, 
			
			String appCode, 
			
			String sign, 
			
			String skin) {
	
		Map<String, String> headers = new HashMap<String, String>();

		// The format in the header (with English spaces in the middle) is Authorization: APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appCode);

		// Encapsulate other parameters
		Map<String, String> querys = new HashMap<String, String>();
		
		// Generate verification code
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < 4; i++) {
			int random = (int) (Math.random() * 10);
			builder.append(random);
		}
		
		String code = builder.toString();

		// The verification code to be sent, which is the part of the template that will change
		querys.put("param", code);

		// Mobile phone number for receiving SMS
		querys.put("phone", phoneNum);

		// signature number

		querys.put("sign", sign);

		// template number
		querys.put("skin", skin);

		try {
			HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
			
			StatusLine statusLine = response.getStatusLine();
			
			// Status code: 200 normal; 400 URL invalid; 401 appCode error; 403 times used up; 500 API network management error
			int statusCode = statusLine.getStatusCode();
			
			String reasonPhrase = statusLine.getReasonPhrase();
			
			if(statusCode == 200) {
				
				// The operation is successful, return the generated verification code
				return ResultEntity.successWithData(code);
			}
			
			return ResultEntity.failed(reasonPhrase);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResultEntity.failed(e.getMessage());
		}
		
	}
	
/**
* MD5 encryption of plaintext strings
* @param source the plaintext string passed in
* @return encrypted result
*/
	public static String md5(String source) {
		
		// 1. Determine whether the source is valid
		if(source == null || source.length() == 0) {
		
			// 2. Throw an exception if it is not a valid string
			throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
		}
		
		try {
			// 3. Get the MessageDigest object
			String algorithm = "md5";
			
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			
			// 4. Get the byte array corresponding to the plaintext string
			byte[] input = source.getBytes();
			
			// 5. Perform encryption
			byte[] output = messageDigest.digest(input);
			
			// 6. Create BigInteger object
			int signum = 1;
			BigInteger bigInteger = new BigInteger(signum, output);
			
			// 7. Convert the value of bigInteger to a string according to hexadecimal
			int radix = 16;
			String encoded = bigInteger.toString(radix).toUpperCase();
			
			return encoded;
		
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
/**
* Determine whether the current request is an Ajax request
* @param request request object
* @return
* true: the current request is an Ajax request
* false: The current request is not an Ajax request
*/
	public static boolean judgeRequestType(HttpServletRequest request) {
		
		// 1. Get the request message header
		String acceptHeader = request.getHeader("Accept");
		String xRequestHeader = request.getHeader("X-Requested-With");
		
		// 2. Judgment
		return (acceptHeader != null && acceptHeader.contains("application/json"))
				
				||
				
				(xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"));
	}

}
