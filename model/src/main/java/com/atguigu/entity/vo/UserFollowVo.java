package com.atguigu.entity.vo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class UserFollowVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long houseId;
	private Date createTime;
	private String communityName;
	private String name;
	private String buildArea;
	private BigDecimal totalPrice;
	private String defaultImageUrl;
	private Long houseTypeId;
	private Long floorId;
	private Long directionId;

	private String houseTypeName;
	private String floorName;
	private String directionName;
}

