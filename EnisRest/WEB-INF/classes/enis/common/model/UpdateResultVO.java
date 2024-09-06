package enis.common.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateResultVO implements Serializable {
	
	private static final long serialVersionUID = -1L;
	
	int resultCode;
	String resultMsg;
	
	public UpdateResultVO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


}
