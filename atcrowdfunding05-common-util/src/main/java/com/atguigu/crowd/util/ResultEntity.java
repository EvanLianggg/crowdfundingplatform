package com.atguigu.crowd.util;

/**
 * Unify the results returned by Ajax requests in the entire project (in the future, it can also be used to return a unified type when calling between various modules of the distributed architecture)
 * @author Lenovo
 *
 * @param <T>
 */
public class ResultEntity<T> {
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	
	// Used to encapsulate the success or failure of the current request processing result
	private String result;
	
	// The error message returned when the request processing fails
	private String message;
	
	// data to be returned

	private T data;
	
/**
* The tool method used when the request is processed successfully and no data is returned
* @return
*/
	public static <Type> ResultEntity<Type> successWithoutData() {
		return new ResultEntity<Type>(SUCCESS, null, null);
	}
	
/**
* The tool method used when the request is processed successfully and the data needs to be returned
* @param data The data to be returned
* @return
*/
	public static <Type> ResultEntity<Type> successWithData(Type data) {
		return new ResultEntity<Type>(SUCCESS, null, data);
	}
	
/**
* Tool method used after request processing fails
* @param message failed error message
* @return
*/
	public static <Type> ResultEntity<Type> failed(String message) {
		return new ResultEntity<Type>(FAILED, message, null);
	}
	
	public ResultEntity() {
		
	}

	public ResultEntity(String result, String message, T data) {
		super();
		this.result = result;
		this.message = message;
		this.data = data;
	}

	@Override
	public String toString() {
		return "ResultEntity [result=" + result + ", message=" + message + ", data=" + data + "]";
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
