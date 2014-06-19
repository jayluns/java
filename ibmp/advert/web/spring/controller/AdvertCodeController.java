package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

import java.util.HashMap;
import java.util.Map;

import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.advert.service.IAdvAgentNumberService;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertCodeInfo;

 
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdvertCodeController extends MultiActionControllerImpl {
	
	static IAdvAgentNumberService advAgentNumberService = null;

	/**
	 * @Title:       getAdvertCode 
	 * @Description: TODO(广告管理模块--操作广告管理编号表)
	 * @param        @param advertPrefix	是广告编号前缀  
	 * @param        @param advertNumberType是广告编号类型(后面最为查询统计使用)	
	 * @param        @return 
	 * @return       long
	 * @author       yue_gao  
	 * @date         2012-4-1 上午11:18:38
	 * @throws
	 */
	public static long getAdvertCode(String advertPrefix, long advertNumberType){
		/**
		 * 得到代理商编号,如果第一次要初始化一个值
		 */
		Map advAgentMap =  new HashMap();
		AdvertCodeInfo advertCodeInfo = new AdvertCodeInfo();
		advertCodeInfo.setCode(advertPrefix);
		advAgentMap.put("keyAdvAgentNumber", advertCodeInfo);
		long advagentNumber = advAgentNumberService.findAdvAgentByAbbreviation(advAgentMap);
		long advcodeNumber = 0;
	    if(advagentNumber == 0 ){
			//初始化一个值 再查询出来编号返回
	    	Map numberMap  = new HashMap();
	    	AdvertCodeInfo advnumberinfo = new AdvertCodeInfo();
	    		advnumberinfo.setCode(advertPrefix);	  //广告模块前缀
	    		advnumberinfo.setCodeNumber(new Long(1)); //广告模块数字编号
	    		advnumberinfo.setType(advertNumberType);  //广告模块编号类型
	    	numberMap.put("keyAdvAgentNumber", advnumberinfo);
	    	advAgentNumberService.addAdvAgentNumber(numberMap);
	    	advcodeNumber = advAgentNumberService.findAdvAgentByAbbreviation(advAgentMap);//返回编号
		}else if(advagentNumber != 0){
	    	advcodeNumber = advagentNumber + 1 ;
	    }
	    addAdvertCode(advertPrefix,advertNumberType);
	    return advcodeNumber;
	}
	
	
	/**
	 * @Title:       addAdvertCode 
	 * @Description: TODO(	1.先查询出代理商编号表的数字编号最大值 再增加记录
	 * 						2.向代理商编号表再次增加一条记录 
	 * 					 ) 
	 * @param        @param advertPrefix		前缀
	 * @param        @param advertNumberType 	编号类型
	 * @return       void
	 * @author       yue_gao  
	 * @date         2012-4-1 下午01:30:40
	 * @throws
	 */
	public static void addAdvertCode(String advertPrefix, long advertNumberType){
		Map advertMapsecond =  new HashMap();
		AdvertCodeInfo advertCodeInfosecond = new AdvertCodeInfo();
		advertCodeInfosecond.setCode(advertPrefix);
		advertMapsecond.put("keyAdvAgentNumber", advertCodeInfosecond);
		long advagentNumberSecond = advAgentNumberService.findAdvAgentByAbbreviation(advertMapsecond);
		//add advcode begin 
		Map numberMapSecond  = new HashMap();
    	AdvertCodeInfo advnumberinfoSecond = new AdvertCodeInfo();
    	advnumberinfoSecond.setCode(advertPrefix);					//前缀
    	advnumberinfoSecond.setCodeNumber(advagentNumberSecond + 1);//数字编号
    	advnumberinfoSecond.setType(advertNumberType);				//编号类型
		numberMapSecond.put("keyAdvAgentNumber", advnumberinfoSecond);
    	advAgentNumberService.addAdvAgentNumber(numberMapSecond);
    	//add advcode end 
	}
		
	public IAdvAgentNumberService getAdvAgentNumberService() {
		return advAgentNumberService;
	}
	public void setAdvAgentNumberService(
			IAdvAgentNumberService advAgentNumberService) {
		this.advAgentNumberService = advAgentNumberService;
	}
	
	
}