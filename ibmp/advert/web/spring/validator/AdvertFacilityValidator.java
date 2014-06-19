package com.kingdee.ais.ibmp.view.advert.web.spring.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.kingdee.ais.core.web.spring3.validator.AbstractSpringValidator;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertFacilityInfo;

public class AdvertFacilityValidator extends AbstractSpringValidator{

	@Override
	public boolean supports(Class<?> clazz) {
		return AdvertFacilityInfo.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "facilityCode", "facilityCode.isEmpty","广告设施编码不能为空");
	}

}
