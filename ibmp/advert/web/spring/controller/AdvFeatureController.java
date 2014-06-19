package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ibm.icu.text.SimpleDateFormat;
import com.kd.ais.common.CommonConstant.BUSINESS_TYPE;
import com.kd.ais.common.CommonConstant.LOG_TYPE;
import com.kd.ais.common.CommonWriteLog4j;
import com.kd.ais.log.service.OperLogService;
import com.kingdee.ais.core.constant.GlobalConstant;
import com.kingdee.ais.core.entity.OrgModel;
import com.kingdee.ais.core.exception.GenericRuntimeException;
import com.kingdee.ais.core.pagination.Page;
import com.kingdee.ais.core.util.CommUtil;
import com.kingdee.ais.core.util.Utils;
import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.advert.logic.impl.AdvertContractFeatureLogicImpl;
import com.kingdee.ais.ibmp.business.advert.service.IAdvAgentService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvCustomerService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvertContractFeatureService;
import com.kingdee.ais.ibmp.business.advert.service.IPerformenceListFeatureService;
import com.kingdee.ais.ibmp.business.dictionary.Dictionary;
import com.kingdee.ais.ibmp.business.dictionary.service.IDictionaryService;
import com.kingdee.ais.ibmp.business.org.service.IOrgCinemaService;
import com.kingdee.ais.ibmp.business.org.service.IOrgCityService;
import com.kingdee.ais.ibmp.business.org.service.IOrgRegionService;
import com.kingdee.ais.ibmp.model.pojo.advert.PerformenceListFeature;
import com.kingdee.ais.ibmp.model.pojo.advert.VAdvFeature;
import com.kingdee.ais.ibmp.model.pojo.org.OrgRegion;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertContractFeatureInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertCustomerInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AgentInfo;
import com.kingdee.ais.ibmp.model.vo.advert.JinyiInfo;

import com.kingdee.ais.ibmp.model.vo.advert.PerformenceListFeatureInfo;
import com.kingdee.ais.ibmp.model.vo.advert.VAdvContractInfo;

import com.kingdee.ais.ibmp.model.vo.advert.PerformenceListFeatureInfo;

import com.kingdee.ais.ibmp.model.vo.advert.VAdvFeatureInfo;
import com.kingdee.ais.ibmp.model.vo.dictionary.DataDictionaryInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCinemaInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCityInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgRegionInfo;
import com.kingdee.ais.ibmp.view.common.JudgementAuthority;
import com.kingdee.ais.ibmp.view.constant.Constant;
import com.kingdee.ais.ibmp.view.constant.ContractConstant;
import com.kingdee.ais.ibmp.view.util.FileUpLoadUtil;
import com.kingdee.ais.ibmp.view.util.IOUtil;

 
public class AdvFeatureController extends MultiActionControllerImpl {
	
	IAdvertContractFeatureService advertContractFeatureService;
	
	IPerformenceListFeatureService performenceListFeatureService;
	
	IOrgCinemaService orgCinemaService;
	
	public IPerformenceListFeatureService getPerformenceListFeatureService() {
		return performenceListFeatureService;
	}

	public void setPerformenceListFeatureService(
			IPerformenceListFeatureService performenceListFeatureService) {
		this.performenceListFeatureService = performenceListFeatureService;
	}

	public IAdvertContractFeatureService getAdvertContractFeatureService() {
		return advertContractFeatureService;
	}

	public void setAdvertContractFeatureService(IAdvertContractFeatureService advertContractFeatureService) {
		this.advertContractFeatureService = advertContractFeatureService;
	}
	
	IAdvAgentService advAgentService;
	
	public IAdvAgentService getAdvAgentService() {
		return advAgentService;
	}
	public void setAdvAgentService(IAdvAgentService advAgentService) {
		this.advAgentService = advAgentService;
	}
	IAdvCustomerService advCustomerService;
	
	public IAdvCustomerService getAdvCustomerService() {
		return advCustomerService;
	}
	public void setAdvCustomerService(IAdvCustomerService advCustomerService) {
		this.advCustomerService = advCustomerService;
	}
	private IDictionaryService dictionaryService=null;
	
	public IDictionaryService getDictionaryService() {
		return dictionaryService;
	}
	public void setDictionaryService(IDictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	IOrgRegionService orgRegionService;
	IOrgCityService orgCityService;
	
	public IOrgRegionService getOrgRegionService() {
		return orgRegionService;
	}

	public void setOrgRegionService(IOrgRegionService orgRegionService) {
		this.orgRegionService = orgRegionService;
	}

	public IOrgCityService getOrgCityService() {
		return orgCityService;
	}

	public void setOrgCityService(IOrgCityService orgCityService) {
		this.orgCityService = orgCityService;
	}

	public IOrgCinemaService getOrgCinemaService() {
		return orgCinemaService;
	}

	public void setOrgCinemaService(IOrgCinemaService orgCinemaService) {
		this.orgCinemaService = orgCinemaService;
	}

	public static final String VIEW_REQUEST_EDIT_ADVFEATURE="view_request_edit_advFeature";
	
	public static final String VIEW_REQUEST_EXCUTE_ADVFEATURE="view_request_excute_advFeature";
	
	public static final String VIEW_REQUEST_MODIFY_ADVCONTRACT="view_request_excute_advContract";
	
	public static final String VIEW_REQUEST_MODIFY_ADVCONTRACT_SHOW="view_request_excute_advContract_show";
	
	public static final String VIEW_REQUEST_ADD_ADVCONTRACT="view_request_add_advContract";
	
	public static final String VIEW_REQUEST_SHOW_EDIT_ADVFEATURE="view_request_show_edit_advFeature";
	
	private List<AgentInfo> queryAllAdvAgentInfoNames(HttpServletRequest request)throws Exception {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		List<AgentInfo>  agentInfosList = advAgentService.queryList(params);
		List<AgentInfo> agList = new ArrayList<AgentInfo>();
		for (int i = 0; i < agentInfosList.size(); i++) {
			AgentInfo ag = new AgentInfo();
			ag.setAgentAccount(agentInfosList.get(i).getAgentAccount());
			ag.setAgentAddress(agentInfosList.get(i).getAgentAddress());
			ag.setAgentBank(agentInfosList.get(i).getAgentBank());
			ag.setAgentCode(agentInfosList.get(i).getAgentCode());
			ag.setAgentFax(agentInfosList.get(i).getAgentFax());
			ag.setAgentId(agentInfosList.get(i).getAgentId());
			ag.setAgentLevel(agentInfosList.get(i).getAgentLevel());
			ag.setAgentName(agentInfosList.get(i).getAgentName());
			ag.setAgentTel(agentInfosList.get(i).getAgentTel());
			agList.add(ag);
		}
		return agList;
	}
	
	private void initRegionAndCityAndCinema(String regionId,String cityId,List<OrgModel> orgModels,Map modelMap){
		//初始化区域
		Map orgRegionParamsMap = new HashMap();
		orgRegionParamsMap.put(GlobalConstant.USER_CODE_KEY,orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(orgRegionParamsMap);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("orgRegions", orgRegions);
		//初始化城市
		if(!StringUtils.isBlank(regionId)){
			Map cityParamsMap = new HashMap();
			cityParamsMap.put(GlobalConstant.USER_CODE_KEY,orgModels);
			OrgRegionInfo orgRegionInfo=new OrgRegionInfo();
			orgRegionInfo.setRegionId(regionId);
			cityParamsMap.put("orgRegionInfoKey",orgRegionInfo);
			List<OrgCityInfo> orgCitys=orgCityService.findOrgCitysByRegionId(cityParamsMap);
			modelMap.put("orgCitys", orgCitys);
		}
		//初始化影院
		if(!StringUtils.isBlank(cityId)){
			Map cinemaParamsMap = new HashMap();
			cinemaParamsMap.put(GlobalConstant.USER_CODE_KEY,orgModels);
			OrgCityInfo orgCityInfo=new OrgCityInfo();
			orgCityInfo.setCityId(cityId);
			cinemaParamsMap.put("orgCityInfoKey",orgCityInfo);
			List<OrgCinemaInfo> orgCinemas=orgCinemaService.findOrgCinemas(cinemaParamsMap);
			modelMap.put("orgCinemas", orgCinemas);
		}
	}
	
	public ModelAndView editAdvFeature(HttpServletRequest request,HttpServletResponse response,AdvertContractFeatureInfo advertContractFeatureInfo)throws GenericRuntimeException{
		Map modelMap = new HashMap();
		//通过合同ID查询合同信息
		if(StringUtils.isNotBlank(advertContractFeatureInfo.getId())&&request.getAttribute("msg")==null){  //如果没有错误信息
			advertContractFeatureInfo=advertContractFeatureService.getAdvertContractFeature(advertContractFeatureInfo.getId());
		}
		//查询金逸信息,因为唯一,所以无参数
		JinyiInfo jinyiInfo = advCustomerService.queryJinyi();
		advertContractFeatureInfo.setJinyi(jinyiInfo);
		modelMap.put("advertContractFeatureInfo", advertContractFeatureInfo);
		/**
		 * 1.如果是代理商的合同需要查询代理商的表信息 
		 * 2.如果是非代理商的合同需要查询客户表信息 
		 */
		List<AdvertCustomerInfo> acfList = new ArrayList<AdvertCustomerInfo>();
		List<OrgModel> orgModels = initOrgModels(request);
		Map params = new HashMap();
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		if(StringUtils.isNotBlank(advertContractFeatureInfo.getCustomerType())){
			if (Long.valueOf(advertContractFeatureInfo.getCustomerType()) == Dictionary.CHOOSECUSTOMERTYPE_AGENT) {//查询代理商的所有名称
				List<AgentInfo> agentInfosList=null;
				try {
					agentInfosList = queryAllAdvAgentInfoNames(request);
				} catch (Exception e) {
						CommonWriteLog4j.writeLog4j(OperLogService.getInstance(), orgModels, BUSINESS_TYPE.WRITE, AdvFeatureController.class, LOG_TYPE.OPER_ERROR, 
								e.getMessage(), null);
				}
				//将代理商信息 转化为类似客户资料信息的对象
				for (int i = 0; null!=agentInfosList&&i <agentInfosList.size(); i++) {
					AdvertCustomerInfo acf = new AdvertCustomerInfo();
					acf.setCustomerId(agentInfosList.get(i).getAgentId());
					acf.setCustomerName(agentInfosList.get(i).getAgentName());
					acf.setCustomerAddress(agentInfosList.get(i).getAgentAddress());
					acf.setCustomerTel(agentInfosList.get(i).getAgentTel());
					acf.setCustomerFax(agentInfosList.get(i).getAgentFax());
					acf.setCustomerCode(agentInfosList.get(i).getAgentCode());
					acf.setContactName(agentInfosList.get(i).getContactName());
					acfList.add(acf);
				}
			}else if (Long.valueOf(advertContractFeatureInfo.getCustomerType()) == Dictionary.CHOOSECUSTOMERTYPE_NOTAGENT) {
				acfList =advCustomerService.findAdvCustomer(params);//得到客户列表,用于下拉 
			}
		}
		modelMap.put("advCustomerInfo",acfList );
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (advertContractFeatureInfo.getContractSignTime()!=null) {
			modelMap.put("date1", sdf.format(advertContractFeatureInfo.getContractSignTime()));
		}
		if (advertContractFeatureInfo.getContractStartTime()!=null) {
			modelMap.put("date2", sdf.format(advertContractFeatureInfo.getContractStartTime()));
		}
	    if (advertContractFeatureInfo.getContractEndTime()!=null) {
			modelMap.put("date3", sdf.format(advertContractFeatureInfo.getContractEndTime()));
		}
		//取出广告类型的数据
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		//付款方式
		dataDictionaryInfo.setFDataId(Dictionary.CUSTOMER_PAYMENT); 
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> paymentList = dictionaryService.findDataDictionarys(dictionaryMap);
		List<DataDictionaryInfo> paymentListTemp=new ArrayList<DataDictionaryInfo>();
		for(DataDictionaryInfo dataDictionary:paymentList){
			if(!"合作公司".equals(dataDictionary.getSDataName())&&!"月结".equals(dataDictionary.getSDataName())){ //不包含“合作公司”和“月结”
				paymentListTemp.add(dataDictionary);
			}
		}
		modelMap.put("paymentList", paymentListTemp);
		//初始化选择客户类型
		
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		modelMap.put("orgmodel",orgModel);
		
		Map choosecustomerTypeMap = new HashMap();
		DataDictionaryInfo dictionaryInfo = new DataDictionaryInfo();
		dictionaryInfo.setFDataId(Dictionary.CHOOSECUSTOMERTYPE); //选择客户类型
		choosecustomerTypeMap.put("dataDictionaryInfoKey", dictionaryInfo);
		List<DataDictionaryInfo> chooseCustomerTypeList = dictionaryService.findDataDictionarys(choosecustomerTypeMap);
		modelMap.put("chooseCustomerTypeList", chooseCustomerTypeList);
		initRegionAndCityAndCinema(advertContractFeatureInfo.getRegionId(),advertContractFeatureInfo.getCityId(),orgModels,modelMap);
		modelMap.put("isModify", request.getParameter("isModify"));
		  if(request.getParameterMap().containsKey(CommUtil.MSG_KEY)){ //如果包含提示信息
				String msg=TIP_MAP.get(request.getParameter(CommUtil.MSG_KEY));
				modelMap.put("msg",msg);
		  }
		return new ModelAndView(VIEW_REQUEST_EDIT_ADVFEATURE, modelMap);
}
	
	
	public ModelAndView showEditAdvFeature(HttpServletRequest request,HttpServletResponse response,AdvertContractFeatureInfo advertContractFeatureInfo)throws GenericRuntimeException{
		Map modelMap = new HashMap();
		//通过合同ID查询合同信息
		if(StringUtils.isNotBlank(advertContractFeatureInfo.getId())&&request.getAttribute("msg")==null){  //如果没有错误信息
			advertContractFeatureInfo=advertContractFeatureService.getAdvertContractFeature(advertContractFeatureInfo.getId());
		}
		//查询金逸信息,因为唯一,所以无参数
		JinyiInfo jinyiInfo = advCustomerService.queryJinyi();
		advertContractFeatureInfo.setJinyi(jinyiInfo);
		modelMap.put("advertContractFeatureInfo", advertContractFeatureInfo);
		/**
		 * 1.如果是代理商的合同需要查询代理商的表信息 
		 * 2.如果是非代理商的合同需要查询客户表信息 
		 */
		List<AdvertCustomerInfo> acfList = new ArrayList<AdvertCustomerInfo>();
		List<OrgModel> orgModels = initOrgModels(request);
		Map params = new HashMap();
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		if(StringUtils.isNotBlank(advertContractFeatureInfo.getCustomerType())){
			if (Long.valueOf(advertContractFeatureInfo.getCustomerType()) == Dictionary.CHOOSECUSTOMERTYPE_AGENT) {//查询代理商的所有名称
				List<AgentInfo> agentInfosList=null;
				try {
					agentInfosList = queryAllAdvAgentInfoNames(request);
				} catch (Exception e) {
						CommonWriteLog4j.writeLog4j(OperLogService.getInstance(), orgModels, BUSINESS_TYPE.WRITE, AdvFeatureController.class, LOG_TYPE.OPER_ERROR, 
								e.getMessage(), null);
				}
				//将代理商信息 转化为类似客户资料信息的对象
				for (int i = 0; null!=agentInfosList&&i <agentInfosList.size(); i++) {
					AdvertCustomerInfo acf = new AdvertCustomerInfo();
					acf.setCustomerId(agentInfosList.get(i).getAgentId());
					acf.setCustomerName(agentInfosList.get(i).getAgentName());
					acf.setCustomerAddress(agentInfosList.get(i).getAgentAddress());
					acf.setCustomerTel(agentInfosList.get(i).getAgentTel());
					acf.setCustomerFax(agentInfosList.get(i).getAgentFax());
					acf.setCustomerCode(agentInfosList.get(i).getAgentCode());
					acf.setContactName(agentInfosList.get(i).getContactName());
					acfList.add(acf);
				}
			}else if (Long.valueOf(advertContractFeatureInfo.getCustomerType()) == Dictionary.CHOOSECUSTOMERTYPE_NOTAGENT) {
				acfList =advCustomerService.findAdvCustomer(params);//得到客户列表,用于下拉 
			}
		}
		modelMap.put("advCustomerInfo",acfList );
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (advertContractFeatureInfo.getContractSignTime()!=null) {
			modelMap.put("date1", sdf.format(advertContractFeatureInfo.getContractSignTime()));
		}
		if (advertContractFeatureInfo.getContractStartTime()!=null) {
			modelMap.put("date2", sdf.format(advertContractFeatureInfo.getContractStartTime()));
		}
	    if (advertContractFeatureInfo.getContractEndTime()!=null) {
			modelMap.put("date3", sdf.format(advertContractFeatureInfo.getContractEndTime()));
		}
		//取出广告类型的数据
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		//付款方式
		dataDictionaryInfo.setFDataId(Dictionary.CUSTOMER_PAYMENT); 
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> paymentList = dictionaryService.findDataDictionarys(dictionaryMap);
		List<DataDictionaryInfo> paymentListTemp=new ArrayList<DataDictionaryInfo>();
		for(DataDictionaryInfo dataDictionary:paymentList){
			if(!"合作公司".equals(dataDictionary.getSDataName())&&!"月结".equals(dataDictionary.getSDataName())){ //不包含“合作公司”和“月结”
				paymentListTemp.add(dataDictionary);
			}
		}
		modelMap.put("paymentList", paymentListTemp);
		//初始化选择客户类型
		
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		modelMap.put("orgmodel",orgModel);
		
		Map choosecustomerTypeMap = new HashMap();
		DataDictionaryInfo dictionaryInfo = new DataDictionaryInfo();
		dictionaryInfo.setFDataId(Dictionary.CHOOSECUSTOMERTYPE); //选择客户类型
		choosecustomerTypeMap.put("dataDictionaryInfoKey", dictionaryInfo);
		List<DataDictionaryInfo> chooseCustomerTypeList = dictionaryService.findDataDictionarys(choosecustomerTypeMap);
		modelMap.put("chooseCustomerTypeList", chooseCustomerTypeList);
		initRegionAndCityAndCinema(advertContractFeatureInfo.getRegionId(),advertContractFeatureInfo.getCityId(),orgModels,modelMap);
		modelMap.put("isModify", request.getParameter("isModify"));
		  if(request.getParameterMap().containsKey(CommUtil.MSG_KEY)){ //如果包含提示信息
				String msg=TIP_MAP.get(request.getParameter(CommUtil.MSG_KEY));
				modelMap.put("msg",msg);
		  }
		return new ModelAndView(VIEW_REQUEST_SHOW_EDIT_ADVFEATURE, modelMap);
}
	/**
	 * 删除上传的图片，文件
	 * */
	public void delPic(HttpServletRequest request, HttpServletResponse response,AdvertContractFeatureInfo advertContractFeatureInfo) throws Exception{
		String filePath = request.getSession().getServletContext().getRealPath("/")+advertContractFeatureInfo.getContractUrl();
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		JSONObject json = new JSONObject();
		json.put("success", "success1");
		IOUtil.writeJSON(json, response.getOutputStream());
	}
	private static Map<String, String> TIP_MAP;
	{
		TIP_MAP=new HashMap<String, String>();
		TIP_MAP.put("uadvF","修改映前广告合同成功");
		TIP_MAP.put("aadvF","新增映前广告合同成功");
		TIP_MAP.put("sadvFE_name","该映前广告合同名称已经存在！");
		TIP_MAP.put("sadvFE_code","该映前广告合同编号已经存在！");
		TIP_MAP.put("splF","执行单已确认");
		TIP_MAP.put("dateF", "该影院已存在有效期与当前合同有效期重叠的合同");
	}
	public ModelAndView saveAdvFeature(HttpServletRequest request,HttpServletResponse response,AdvertContractFeatureInfo advertContractFeatureInfo)throws GenericRuntimeException{
		 List<OrgModel> orgModels = initOrgModels(request);
		 String msgKey="uadvF";
		 
		 //判定该合同名称和合同编号是否已经存在
		 Map paramsMap = new HashMap();
		 paramsMap.put("advertContractFeatureInfoKey",advertContractFeatureInfo);
		 List<AdvertContractFeatureInfo> advertContractFeatureInfoList=advertContractFeatureService.findAdvertContractFeatureList(paramsMap);
		 for(AdvertContractFeatureInfo varAdvertContractFeatureInfo:advertContractFeatureInfoList){
			 if(advertContractFeatureInfo.getName().equals(varAdvertContractFeatureInfo.getName())&&!varAdvertContractFeatureInfo.getId().equals(advertContractFeatureInfo.getId())){
				 msgKey="sadvFE_name";
				 break;
			 }
			 if(advertContractFeatureInfo.getCode().equals(varAdvertContractFeatureInfo.getCode())&&!varAdvertContractFeatureInfo.getId().equals(advertContractFeatureInfo.getId())){
				 msgKey="sadvFE_code";
				 break;
			 }
		 }
		 if(StringUtils.isBlank(advertContractFeatureInfo.getId())){ //新增的时候
			 //查编码
			 List<VAdvFeatureInfo> advertFeaList=advertContractFeatureService.findVAdvFeaturesCode();
			String code= getContractCode(advertFeaList);
			advertContractFeatureInfo.setCode(code);
			 advertContractFeatureInfo.setCreateUserId(orgModels.get(0).getName());
			 advertContractFeatureInfo.setCreateDate(new Date());
			 advertContractFeatureInfo.setOrgCode(JudgementAuthority.getUserInfo(request).getOrgName());
			 advertContractFeatureInfo.setVersion(null);
			 advertContractFeatureInfo.setPerformenceStatus(Dictionary.ADVCONTRACT_PERFORMENCE_STATUS_NOTISSUE);
			 advertContractFeatureInfo.setAdvertType(Dictionary.ADVERT_TYPE_YC);
			 if("0".equals(String.valueOf(advertContractFeatureInfo.getContractAmount()))){
				 advertContractFeatureInfo.setAdvertType(Dictionary.ADVERT_TYPE_WC);
			 }
			 if(advertContractFeatureInfo.getRegionId()==null || "".equals(advertContractFeatureInfo.getRegionId())){
				 advertContractFeatureInfo.setRegionId("JY");
			 }
			 if(!"sadvFE_name".equals(msgKey)&&!"sadvFE_code".equals(msgKey)&&!"dateF".equals(msgKey)){
				 msgKey="aadvF";
			 }
			 
		 }
		 advertContractFeatureInfo.setUpdateUserId(orgModels.get(0).getName());
		 advertContractFeatureInfo.setUpdateDate(new Date());
		 //验证合同时间是否重叠
		 if(advertContractFeatureService.checkIsExistsOverlapping(paramsMap)){
			 msgKey = "dateF";
		 }
		if(!"sadvFE_name".equals(msgKey)&&!"sadvFE_code".equals(msgKey)&&!"dateF".equals(msgKey)){
			advertContractFeatureService.saveAdvFeature(advertContractFeatureInfo);
		}
		String logsString = "[ name="+advertContractFeatureInfo.getName()+",code="+advertContractFeatureInfo.getCode()+"  ]"; 
		CommonWriteLog4j.writeLog4j(OperLogService.getInstance(), orgModels, BUSINESS_TYPE.WRITE, AdvertContractFeatureLogicImpl.class, LOG_TYPE.OPER_NORMAL,logsString,null);
		 if((!"sadvFE_name".equals(msgKey)&&!"sadvFE_code".equals(msgKey)&&!"dateF".equals(msgKey))){ //没有错误
			 return new ModelAndView(new RedirectView("editAdvFeature.do?"+CommUtil.MSG_KEY+"="+msgKey+"&id="+advertContractFeatureInfo.getId()));
		 }else{
			 if(StringUtils.isBlank(advertContractFeatureInfo.getId())){
				 advertContractFeatureInfo.setVersion(null);
			 }
			 request.setAttribute("msg", TIP_MAP.get(msgKey));
			 return  editAdvFeature(request, response, advertContractFeatureInfo);
		 }
	}
	private List<VAdvFeatureInfo> getUniqueCustomer(List<VAdvFeatureInfo> vAdvFeatureInfosPageResult){
		Set set=new HashSet();
		List<VAdvFeatureInfo> list=new ArrayList<VAdvFeatureInfo>();
		for(VAdvFeatureInfo vAdvFeatureInfo:vAdvFeatureInfosPageResult){
			String key=vAdvFeatureInfo.getCustomerId()+"-"+vAdvFeatureInfo.getCustomerType();
			if(!set.contains(key)){
				list.add(vAdvFeatureInfo);
				set.add(key);
			}
		}
		return list;
	}
	
	public static final String VIEW_REQUEST_QUERY_ADVFEATURE="view_request_query_advFeature";
	
	
	public ModelAndView queryAdvFeatureList(HttpServletRequest request,HttpServletResponse response,VAdvFeatureInfo vAdvFeatureInfo)throws GenericRuntimeException {
		VAdvFeatureInfo vAdvFeatureInfoParam = new VAdvFeatureInfo();
		String customerId= request.getParameter("customerId");
		vAdvFeatureInfoParam.setCustomerId(customerId);
		String isAgentContractString  = request.getParameter("isAgentContract");	//是否代理商合同  2201-代理商  2202-非代理商
		vAdvFeatureInfoParam.setIsAgentContract(isAgentContractString);					//设置是否是代理商合同
		vAdvFeatureInfoParam.setIsRelationCinemaContract(request.getParameter("isRelationCinemaContract"));//是否关联影城合同
		vAdvFeatureInfoParam.setCustomerType(String.valueOf(Dictionary.CHOOSECUSTOMERTYPE_AGENT));//用户选择的是代理商合同
		vAdvFeatureInfoParam.setCurrentPageNumber(vAdvFeatureInfo.getCurrentPageNumber());
		vAdvFeatureInfoParam.setPageSize(GlobalConstant.PAGE_SIZE);
		vAdvFeatureInfoParam.setPageNavigationURL(CommUtil.getUrl(request));
		if(StringUtils.isNotBlank(vAdvFeatureInfo.getCode())){
			vAdvFeatureInfoParam.setCode(vAdvFeatureInfo.getCode());
		}
		if(StringUtils.isNotBlank(vAdvFeatureInfo.getName())){
			vAdvFeatureInfoParam.setName(vAdvFeatureInfo.getName());
		}
		if(null != vAdvFeatureInfo.getOrgRegion() && StringUtils.isNotBlank(vAdvFeatureInfo.getOrgRegion().getRegionId())){
			OrgRegionInfo region=new OrgRegionInfo();
			region.setRegionId(vAdvFeatureInfo.getOrgRegion().getRegionId());
			vAdvFeatureInfoParam.setOrgRegion(region);
		}
		if(null != vAdvFeatureInfo.getOrgCity() && StringUtils.isNotBlank(vAdvFeatureInfo.getOrgCity().getCityId())){
			OrgCityInfo city=new OrgCityInfo();
			city.setCityId(vAdvFeatureInfo.getOrgCity().getCityId());
			vAdvFeatureInfoParam.setOrgCity(city);
		}
		if(null != vAdvFeatureInfo.getOrgCinema() && StringUtils.isNotBlank(vAdvFeatureInfo.getOrgCinema().getCinemaId())){
			OrgCinemaInfo cinema=new OrgCinemaInfo();
			cinema.setCinemaId(vAdvFeatureInfo.getOrgCinema().getCinemaId());
			vAdvFeatureInfoParam.setOrgCinema(cinema);
		}
		
		Map paramMap = new HashMap();
		paramMap.put("vAdvFeatureInfoKey", vAdvFeatureInfoParam);
		List<OrgModel> orgModels = initOrgModels(request);
		paramMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		String orgCode = JudgementAuthority.getUserInfo(request).getOrgName();
		paramMap.put("orgCodeKey", orgCode);
		Page<VAdvFeatureInfo, VAdvFeature> pageResult = advertContractFeatureService.findVAdvFeatures(paramMap);
		List<VAdvFeatureInfo>  vAdvFeatureInfosPageResult = advertContractFeatureService.findVAdvFeaturesNotPage(paramMap);//得到合同表中的所有客户名称
		pageResult.setPageNavigationURL(vAdvFeatureInfoParam.getPageNavigationURL());
		//取出是否外包合同的数据
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.IS_OUT_SOURING_CONTRACT); // 得到是否外包合同
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> dictionaryOutSourcingList = dictionaryService.findDataDictionarys(dictionaryMap);
		Map modelMap = new HashMap();
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(paramMap);
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions, IOUtil.getConfig()));
		modelMap.put("dictionaryOutSourcingList", dictionaryOutSourcingList);
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("vAdvFeatureInfo", vAdvFeatureInfo);
		modelMap.put("orgmodel",orgModel);
		modelMap.put("vAdvFeatureInfosPageResult", getUniqueCustomer(vAdvFeatureInfosPageResult)); 	// 2.展示查询条件中的所有客户名称
		return new ModelAndView(VIEW_REQUEST_QUERY_ADVFEATURE, modelMap);
	}
	public static final String VIEW_REQUEST_CONFIRM_PERFORMENCELIST_FEATURE="view_request_confirm_performenceListFeature";
	

	/**
	 * @Description: TODO(执行广告合同)
	 * @Title:       queryExecuteAdvFeatureList 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param vAdvFeatureInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-8-30 下午06:19:45
	 * @throws
	 */

	public ModelAndView requestPerformenceListFeature(HttpServletRequest request,HttpServletResponse response,PerformenceListFeatureInfo performenceListFeatureInfo)throws GenericRuntimeException {
		performenceListFeatureInfo=advertContractFeatureService.getPerformenceListFeature(performenceListFeatureInfo.getId());
		//取出监播证据提供方式的数据
		Map documentsOfferTypeMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfoOther = new DataDictionaryInfo();
		dataDictionaryInfoOther.setFDataId(Dictionary.DOCUMENTS_OFFER_TYPE); // 得到监播证据提供方式
		documentsOfferTypeMap.put("dataDictionaryInfoKey", dataDictionaryInfoOther);
		List<DataDictionaryInfo> documentsOfferTypeList = dictionaryService.findDataDictionarys(documentsOfferTypeMap);
		Map modelMap = new HashMap();
		modelMap.put("documentsOfferTypeList", documentsOfferTypeList);
		modelMap.put("performenceListFeatureInfo", performenceListFeatureInfo);
		return new ModelAndView(VIEW_REQUEST_CONFIRM_PERFORMENCELIST_FEATURE, modelMap);
	}
	/**
	 * 提交确认执行单
	 */
	public ModelAndView confirmPerformenceListFeature(HttpServletRequest request,HttpServletResponse response,PerformenceListFeatureInfo performenceListFeatureInfo)throws GenericRuntimeException{
		 List<OrgModel> orgModels = initOrgModels(request);
		 performenceListFeatureInfo.setSignUser(orgModels.get(0).getName());
		 performenceListFeatureInfo.setSignTime(new Date());
		 Map paramsMap = new HashMap();
		 paramsMap.put("performenceListFeatureInfoKey",performenceListFeatureInfo);
		 paramsMap.put("status",Dictionary.PERFORMENCE_STATUS_CONFIRM);
		 advertContractFeatureService.confirmPerformenceListFeature(paramsMap);
		 String logsString = "[code="+performenceListFeatureInfo.getCode()+"  ]"; 
		 CommonWriteLog4j.writeLog4j(OperLogService.getInstance(), orgModels, BUSINESS_TYPE.WRITE, AdvertContractFeatureLogicImpl.class, LOG_TYPE.OPER_NORMAL,logsString,null);
		 String msgKey="splF";
		 return new ModelAndView(new RedirectView("requestPerformenceListFeature.do?"+CommUtil.MSG_KEY+"="+msgKey+"&id="+performenceListFeatureInfo.getId()));
	}

	public ModelAndView queryExecuteAdvFeatureList(HttpServletRequest request,HttpServletResponse response,VAdvFeatureInfo vAdvFeatureInfo)throws GenericRuntimeException {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		//得到合同表中的所有客户名称
		List<VAdvFeatureInfo>  vAdvFeatureInfosPageResult = advertContractFeatureService.findVAdvFeaturesNotPage(params);
		//执行单状态
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.PERFORMENCE_STATUS);
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> dictionaryList = dictionaryService.findDataDictionarys(dictionaryMap);
		
		Map vAdvFeatureMap = new HashMap();
		String status = request.getParameter("excuteId");//获取执行单状态
		vAdvFeatureMap.put(GlobalConstant.USER_CODE_KEY,orgModels);
		vAdvFeatureInfo.setPageSize(GlobalConstant.PAGE_SIZE);// 每页显示10条数据
		vAdvFeatureInfo.setPageNavigationURL(CommUtil.getUrl(request));
		vAdvFeatureMap.put("vAdvFeatureInfo", vAdvFeatureInfo);
		vAdvFeatureMap.put("status", status);
		
		String signTime = request.getParameter("signTime");
		vAdvFeatureMap.put("signTime", signTime);
		
		String regionid = request.getParameter("orgRegion.regionId");
		String cityid = request.getParameter("orgCity.cityId");
		String cinemaid = request.getParameter("orgCinema.cinemaId");
		
		vAdvFeatureMap.put("regionid",regionid);
		vAdvFeatureMap.put("cityid",cityid);
		vAdvFeatureMap.put("cinemaid",cinemaid);
		
		//分页查询
		Page<VAdvFeatureInfo, Object[]> pageResult = advertContractFeatureService.findExcuteAdvFeatures(vAdvFeatureMap);
		pageResult.setPageNavigationURL(vAdvFeatureInfo.getPageNavigationURL());
		
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		
		//页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("vAdvFeatureInfosPageResult", getUniqueCustomer(vAdvFeatureInfosPageResult)); //客户名称
		modelMap.put("status", dictionaryList);
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("vAdvFeatureInfo", vAdvFeatureInfo);
		modelMap.put("regionid", regionid);
		modelMap.put("cityid", cityid);
		modelMap.put("cinemaid", cinemaid);
		modelMap.put("statu", status);
		modelMap.put("signTime", signTime);
		modelMap.put("orgmodel",orgModel);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		
		setQueryParamesJson(request, response, modelMap);

		return new ModelAndView(VIEW_REQUEST_EXCUTE_ADVFEATURE, modelMap);
	}
	
	/**
	 * @Description: TODO(下发映前广告执行单)
	 * @Title:       modifyExecuteAdvFeature 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param vAdvFeatureInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-8-31 上午10:19:45
	 * @throwsPerformenceListFeaturePerformenceListFeature
	 */
	
	public ModelAndView modifyExecuteAdvFeature(HttpServletRequest request,HttpServletResponse response,PerformenceListFeatureInfo advertContractFeatureInfo)throws GenericRuntimeException {
		Map params = new HashMap();
		
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
//		//得到合同表中的所有客户名称
//		List<VAdvFeatureInfo>  vAdvFeatureInfosPageResult = advertContractFeatureService.findVAdvFeaturesNotPage(params);
		//监播证据提供方式
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.DOCUMENTS_OFFER_TYPE);
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> dictionaryList = dictionaryService.findDataDictionarys(dictionaryMap);
		String status = request.getParameter("status");
		String name = request.getParameter("name");
		//
		
		
		//通过合同ID查询合同信息
		if(StringUtils.isNotBlank(advertContractFeatureInfo.getId())&&request.getAttribute("msg")==null){  //如果没有错误信息
			advertContractFeatureInfo=performenceListFeatureService.getPerformenceListFeatureInfo(advertContractFeatureInfo.getId());
		}

		Map param = new HashMap();
		AdvertContractFeatureInfo advertContractFeature = new AdvertContractFeatureInfo();
		advertContractFeature.setCinemaId(advertContractFeatureInfo.getCinemaId());
		param.put("advertContractFeatureInfoKey", advertContractFeature);
		List<AdvertContractFeatureInfo> contractList = advertContractFeatureService.findContract(param);
		
		//页面数据显示
		Map modelMap = new HashMap();
		initRegionAndCityAndCinema(advertContractFeatureInfo.getRegionId(),advertContractFeatureInfo.getCityId(),orgModels,modelMap);
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
//		modelMap.put("vAdvFeatureInfosPageResult", getUniqueCustomer(vAdvFeatureInfosPageResult)); //客户名称
		modelMap.put("type", dictionaryList);//监播证据提供方式
		modelMap.put("contracts", contractList);//合同数据
//		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("vAdvFeatureInfo", advertContractFeatureInfo);
		modelMap.put("status", status);
		modelMap.put("name", name);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		
		String flag=request.getParameter("flag");
		if("showModify".equals(flag)){
			return new ModelAndView(VIEW_REQUEST_MODIFY_ADVCONTRACT_SHOW, modelMap);
		}else{
			return new ModelAndView(VIEW_REQUEST_MODIFY_ADVCONTRACT, modelMap);
		}
	}
	
	
	/**
	 * @Description: TODO(下发映前广告执行单)
	 * @Title:       addExecuteAdvFeature 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param vAdvFeatureInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-09-05 上午12:29:11
	 * @throws
	 */
	
	public ModelAndView addExecuteAdvFeature(HttpServletRequest request,HttpServletResponse response,AdvertContractFeatureInfo advertContractFeatureInfo)throws GenericRuntimeException {
		List<AdvertContractFeatureInfo> contractList = advertContractFeatureService.findContractList();
		Map params = new HashMap();
		
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		Map modelMap = new HashMap();
		modelMap.put("contracts", contractList);//合同数据
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("vAdvFeatureInfo", advertContractFeatureInfo);
		modelMap.put("orgmodel",orgModel);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		return new ModelAndView(VIEW_REQUEST_ADD_ADVCONTRACT,modelMap);
	}
	
	
	/**
	 * 根据影院ID,获取合同
	 * @param request
	 * @param response
	 * @param advertContractFeatureInfo
	 * @return
	 * @throws IOException 
	 */
	public void chooseContract(HttpServletRequest request, HttpServletResponse response,AdvertContractFeatureInfo advertContractFeatureInfo) throws IOException{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		params.put("advertContractFeatureInfoKey", advertContractFeatureInfo);
		JSONArray jsonArray = JSONArray.fromObject(advertContractFeatureService.findContract(params));
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}
	
	/**
	 * @Description: TODO(保存广告合同)
	 * @Title:       deleteContract 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param vAdvFeatureInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-8-30 下午04:19:11
	 * @throws
	 */
	public ModelAndView saveContract(HttpServletRequest request,HttpServletResponse response,PerformenceListFeatureInfo pfi)throws Exception {
		Map params = new HashMap();
		List<OrgModel> listmodelList=initOrgModels(request);
		String contractId = request.getParameter("contractId");
		String[] cinemaIdList=pfi.getCinemaId().split(";");
		String[] regionList=pfi.getRegionId().split(";");
		String[] cityList=pfi.getCityId().split(";");
		String[] featureUrlList=pfi.getFeatureUrl().split(";");
		String[] contractIdList=contractId.split(";");
		params.put("listmodelList", listmodelList);
		String id = request.getParameter("userid");
		params.put("id", id);
		boolean res=false;
		boolean flag=false;
		String showMsg="";
//		进行多次保存
		for (int i = 0; i < cinemaIdList.length; i++) {
			String code = ContractConstant.getContractCode();
			params.put("code", code);
			PerformenceListFeatureInfo temp=new PerformenceListFeatureInfo();
			temp.setFeatureUrl(featureUrlList[i]);
			temp.setRegionId(regionList[i]);
			temp.setCityId(cityList[i]);
			temp.setCinemaId(cinemaIdList[i]);
			temp.setRemark(pfi.getRemark());
			temp.setRemarkConfirm(pfi.getRemarkConfirm());
			params.put("contractId", contractIdList[i]);
			params.put("performenceListFeatureInfo", temp);
			String[] parma=new String[2];
			parma[0]=contractIdList[i];
			parma[1]=cinemaIdList[i];
			flag=performenceListFeatureService.checkPerformenceListFeature(parma);
			if(flag){
				OrgCinemaInfo orgCinemaInfo=orgCinemaService.getCinemaInfoById(cinemaIdList[i]);
				showMsg=orgCinemaInfo.getCinemaName();
				break;
			}
			res= advertContractFeatureService.saveContract(params);
		}
		JSONObject json = new JSONObject();
		if(flag){
			json.put("failure", showMsg+"已有映前广告执行单存在，但未执行请重选关联合同！");
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		if(res){
			json.put("success",Constant.SAVE_SUCCESS);//删除用户信息成功
			IOUtil.writeJSON(json, response.getOutputStream());
		}else{
			json.put("failure", Constant.SAVE_FAIL);
			IOUtil.writeJSON(json, response.getOutputStream());
		}
		return null;
	}
	
	/**
	 * @Description: TODO(删除广告合同)
	 * @Title:       deleteContract 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param vAdvFeatureInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-8-30 下午04:19:11
	 * @throws
	 */
	public ModelAndView deleteContract(HttpServletRequest request,HttpServletResponse response,PerformenceListFeatureInfo vAdvFeatureInfo)throws Exception {
		Map params = new HashMap();
		params.put("vAdvFeatureInfo", vAdvFeatureInfo);
		
		JSONObject json = new JSONObject();
//		
		boolean res = advertContractFeatureService.deleteContract(params);
		if(res){
			json.put("success",Constant.DELETE_SUCCESS);//删除用户信息成功
			IOUtil.writeJSON(json, response.getOutputStream());
		}else{
			json.put("failure", Constant.DELETE_FAIL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		return null;
	}
	
	/**
	 * 文件上传 (只能上传PDF的文件)
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		json.put("success",Constant.FILE_UPLOAD_FAIL);
		String result = FileUpLoadUtil.fileUpload(request, FileUpLoadUtil.AGENT_CONTRACT_IMAGE);
		if(result.equals(FileUpLoadUtil.DOCUMENT_IMAGE_SIZE_RESULT)){
			json.put("success",Constant.FILE_UPLOAD_TOO_LARGES5);
		}else if(result.equals(FileUpLoadUtil.FILE_UPLOAD_SIZE_RESULT)){
			json.put("success",Constant.FILE_UPLOAD_SIZE_IS_ZERO);
		}else if(result.equals(FileUpLoadUtil.AGENT_CONTRACT_IMAGE_RESULT)){
			json.put("success",Constant.NOT_PDF);
		}else{
			json.put("url",result);
			json.put("success",Constant.FILE_UPLOAD_SUCCESS);
		}
		String agent = request.getHeader("User-Agent");
		if(agent.contains("MSIE")){
			response.setHeader("Charset","UTF-8");
			response.setContentType("text/html;charset=UTF-8");
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 删除上传的图片，文件
	 * */
	public void delPic(HttpServletRequest request, HttpServletResponse response,PerformenceListFeatureInfo pf) throws Exception{
		String filePath = request.getSession().getServletContext().getRealPath("/")+pf.getFeatureUrl();
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		JSONObject json = new JSONObject();
		json.put("success", "success1");
		IOUtil.writeJSON(json, response.getOutputStream());
	}

	public static final String VIEW_QUERY_CONFIRM_PERFORMENCELIST_FEATURE="view_query_confirm_performenceListFeature";
	/**
	 * 查询确认执行单
	 */
	public ModelAndView queryPerformenceListFeature(HttpServletRequest request,HttpServletResponse response,PerformenceListFeatureInfo performenceListFeatureInfo)throws GenericRuntimeException {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		
		String regionid = request.getParameter("orgRegion.regionId");
		String cityid = request.getParameter("orgCity.cityId");
		String cinemaid = request.getParameter("orgCinema.cinemaId");
		performenceListFeatureInfo.setRegionId(regionid);
		performenceListFeatureInfo.setCityId(cityid);
		performenceListFeatureInfo.setCinemaId(cinemaid);
		
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		params.put("performenceListFeatureInfokey", performenceListFeatureInfo);
		performenceListFeatureInfo.setPageSize(GlobalConstant.PAGE_SIZE); // 10条数据
		performenceListFeatureInfo.setPageNavigationURL(CommUtil.getUrl(request)); 
		Page<PerformenceListFeatureInfo, PerformenceListFeature>  pageResult=advertContractFeatureService.findPerformenceListFeatures(params);
		pageResult.setPageNavigationURL(performenceListFeatureInfo.getPageNavigationURL());
		
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		// 页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgmodel", orgModel);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("pageResult", pageResult); // 列表数据
		initRegionAndCityAndCinema(performenceListFeatureInfo.getRegionId(),performenceListFeatureInfo.getCityId(),orgModels,modelMap);
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.PERFORMENCE_STATUS);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> performenceListFeatureStatusList = dictionaryService.findDataDictionarys(params);
		modelMap.put("performenceListFeatureStatusList", performenceListFeatureStatusList);
		modelMap.put("performenceListFeature", performenceListFeatureInfo); //回显查询条件
		//setQueryParamesJson(request, response, modelMap);
		return new ModelAndView(VIEW_QUERY_CONFIRM_PERFORMENCELIST_FEATURE, modelMap);
	}
	
	/**
	 * 自动获合同编码
	 * @param list
	 * @author milihua
	 */
	public String getContractCode(List<VAdvFeatureInfo> advertResourceInfos){
		//合同编号自动生成JY-MEDIA【A】-20120000
		StringBuffer code =new StringBuffer();
		code.append("JY-MEDIA【B】-");
		//得到年
		Calendar cal=Calendar.getInstance(); 
		//得到年
		String year=cal.get(Calendar.YEAR)+"";
		code.append(year);
	    String code1="0";
		if(Utils.isNotEmpty(advertResourceInfos)){
			for(int i = 0;i<advertResourceInfos.size();i++){
				VAdvFeatureInfo info = advertResourceInfos.get(i);
				if(info.getCode().contains("-")){
					String[] resourceCodes1 = info.getCode().split("-");
					String code2 = resourceCodes1[resourceCodes1.length-1];
					String yearTemp=code2.substring(0,4);
					//如果年份一样就在原来的数字上加1
					if(year.equals(yearTemp)){
						code2=code2.substring(code2.length()-4);
						if(Integer.parseInt(code2) > Integer.parseInt(code1)){
							code1 = code2;
						}
					}
				}
			}
		}
		code1 =Integer.parseInt(code1)+1+"";
		if(code1.length()<4){
			DecimalFormat df = new DecimalFormat("0000");
			code1=df.format(Integer.parseInt(code1));
			code.append(code1);
		}else{
			code.append(code1);
		}
        
		return code.toString();
	}
	
}