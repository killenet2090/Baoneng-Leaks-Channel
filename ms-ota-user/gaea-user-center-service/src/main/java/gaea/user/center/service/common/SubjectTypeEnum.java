package gaea.user.center.service.common;

public enum SubjectTypeEnum {

	ROLE(1,"角色");
	
	private Integer key;
	
	private String value;
	
	SubjectTypeEnum(Integer key, String value){
		this.key = key;
		this.value = value;
	}
	
    public static String getValueByKey(Integer key) {  
        for (SubjectTypeEnum c : SubjectTypeEnum.values()) {  
            if (c.getKey().equals(key)) {  
                return c.value;  
            }  
        }  
        return null;  
    }
    
    public static Integer getKeyByValue(String value) {  
        for (SubjectTypeEnum c : SubjectTypeEnum.values()) {  
            if (c.getValue().equals(value)) {  
                return c.getKey();  
            }  
        }  
        return null;  
    }
    
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	} 
}
