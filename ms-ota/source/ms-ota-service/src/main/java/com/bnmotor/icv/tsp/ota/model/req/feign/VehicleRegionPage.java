package com.bnmotor.icv.tsp.ota.model.req.feign;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor(staticName = "of")
@Accessors(chain = true)
@Data
public class VehicleRegionPage implements Serializable {

	private static final long serialVersionUID = 1L;
	Integer code;
	Integer current;
	Integer totalItem;
	String message;
	Integer pageSize;
	Boolean success;
	List<VehicleRegionState> data;

}