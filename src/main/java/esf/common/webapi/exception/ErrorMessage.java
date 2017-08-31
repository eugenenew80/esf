package esf.common.webapi.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="errorMessage")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ErrorMessage {	

	public ErrorMessage() { }
	
	public ErrorMessage(String errMsg) {
		super();
		this.errMsg = errMsg;
	}

	public ErrorMessage(String errCode, String errMsg) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	
	
	private String errMsg;
	private String errCode;
}
