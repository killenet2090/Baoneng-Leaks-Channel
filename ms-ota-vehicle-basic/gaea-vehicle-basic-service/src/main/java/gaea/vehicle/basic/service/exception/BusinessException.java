package gaea.vehicle.basic.service.exception;

public class BusinessException extends RuntimeException{
	
	private static final long serialVersionUID = 114232567869L;

	private String code;
	
    private String msg;

    public BusinessException(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
