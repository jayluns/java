package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.text.SimpleDateFormat;
import com.kd.ais.common.CommonConstant.BUSINESS_TYPE;
import com.kd.ais.common.CommonConstant.LOG_TYPE;
import com.kd.ais.common.CommonWriteLog4j;
import com.kd.ais.log.service.OperLogService;
import com.kingdee.ais.core.constant.GlobalConstant;
import com.kingdee.ais.core.entity.OrgModel;
import com.kingdee.ais.core.exception.GenericRuntimeException;
import com.kingdee.ais.core.pagination.Page;
import com.kingdee.ais.core.util.CommUtil;
import com.kingdee.ais.core.util.DateUtil;
import com.kingdee.ais.core.util.Utils;
import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.advert.logic.impl.AdvCustomerLogicImpl;
import com.kingdee.ais.ibmp.business.advert.service.AdvAgentContractService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvAgentService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvCustomerService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvResourcePlanService;
import com.kingdee.ais.ibmp.business.advert.service.PerformenceListService;
import com.kingdee.ais.ibmp.business.dictionary.Dictionary;
import com.kingdee.ais.ibmp.business.dictionary.service.IDictionaryService;
import com.kingdee.ais.ibmp.business.org.service.IOrgCinemaService;
import com.kingdee.ais.ibmp.business.org.service.IOrgCityService;
import com.kingdee.ais.ibmp.business.org.service.IOrgRegionService;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertContract;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertCustomer;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertFacility;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertResource;
import com.kingdee.ais.ibmp.model.pojo.advert.Agent;
import com.kingdee.ais.ibmp.model.pojo.advert.AgentContract;
import com.kingdee.ais.ibmp.model.pojo.advert.PerformenceList;
import com.kingdee.ais.ibmp.model.pojo.advert.VAdvContract;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertContactInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertContractInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertCustomerInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertPlanInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertResourceInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertResourcePlanInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AgentInfo;
import com.kingdee.ais.ibmp.model.vo.advert.JinyiInfo;
import com.kingdee.ais.ibmp.model.vo.advert.PerformenceListInfo;
import com.kingdee.ais.ibmp.model.vo.advert.ResourceInfo;
import com.kingdee.ais.ibmp.model.vo.advert.VAdvContractInfo;
import com.kingdee.ais.ibmp.model.vo.dictionary.DataDictionaryInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCinemaInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCityInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgRegionInfo;
import com.kingdee.ais.ibmp.view.common.JudgementAuthority;
import com.kingdee.ais.ibmp.view.constant.AdvCustomerConstant;
import com.kingdee.ais.ibmp.view.constant.AdvPerformenceConstant;
import com.kingdee.ais.ibmp.view.constant.AdvResourcePlanConstant;
import com.kingdee.ais.ibmp.view.constant.Constant;
import com.kingdee.ais.ibmp.view.util.FileUpLoadUtil;
import com.kingdee.ais.ibmp.view.util.IOUtil;
import com.kingdee.ais.ibmp.view.util.NumberFormatUtil;

 
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdvCustomerController extends MultiActionControllerImpl {
	
	IAdvCustomerService advCustomerService = null;
	IOrgRegionService orgRegionService=null;
	IAdvResourcePlanService advResourcePlanService=null;
	IAdvAgentService advAgentService = null;
	IOrgCityService orgCityService=null;
	IOrgCinemaService orgCinemaService=null;
	private AdvAgentContractService advAgentContractService;
	
	public void setAdvAgentContractService(
			AdvAgentContractService advAgentContractService) {
		this.advAgentContractService = advAgentContractService;
	}
	public IAdvAgentService getAdvAgentService() {
		return advAgentService;
	}
	public void setAdvAgentService(IAdvAgentService advAgentService) {
		this.advAgentService = advAgentService;
	}
	public IAdvResourcePlanService getAdvResourcePlanService() {
		return advResourcePlanService;
	}
	public void setAdvResourcePlanService(
			IAdvResourcePlanService advResourcePlanService) {
		this.advResourcePlanService = advResourcePlanService;
	}
	public IOrgRegionService getOrgRegionService() {
		return orgRegionService;
	}
	public void setOrgRegionService(IOrgRegionService orgRegionService) {
		this.orgRegionService = orgRegionService;
	}
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
	private PerformenceListService performenceListService;
	
	public PerformenceListService getPerformenceListService() {
		return performenceListService;
	}
	public void setPerformenceListService(
			PerformenceListService performenceListService) {
		this.performenceListService = performenceListService;
	}
	//新增代理商信息二级页面
	public static final String VIEW_REQUEST_ADDAGENTINFO_SUCCESS  = "view_request_addagentinfo_success";  //新增代理商信息-页面请求成功页面
	public static final String VIEW_REDIRECT_ADDAGENTINFO_SUCCESS = "view_redirect_addagentinfo_success"; //新增代理商信息保存成功后重新定向的页面
	public static final String VIEW_SHOWCURRENTADVAGENTINFO_SUCCESS = "requestAddAdvAgentInfo.do"; 		  //跳转到当前页面的请求
	public static final String VIEW_QUERY_ADVCUSTOMERINFO_LIST_SUCCESS = "view_query_advcustomerinfo_list_success";//维护广告客户资料-左边菜单请求得到的列表页面
	public static final String VIEW_QUERY_ADVCUSTOMERINFO_DETAILOPERATION_SUCCESS = "view_query_advcustomerinfo_detailoperation_success";//维护广告客户资料-列表页面点击操作详情的页面
	// 新增客户资料信息二级页面
	public static final String VIEW_REQUEST_ADDCUSTOMERTINFO_SUCCESS = "view_request_addcustomertinfo_success";// 请求跳转到新增广告客户资料页面
	// 维护广告合同页面 @author seven 2012年4月9日11:44:56
	public static final String VIEW_REQUEST_MAINTAIN_ADVCONTRACT = "view_request_maintain_advcontract_success";
	//录入广告合同页面 seven
	public static final String VIEW_REQUEST_ADD_ADVCONTRACT="view_request_add_advcontract";
	//选择方案页面 seven
	public  static final String VIEW_REQUEST_SELECT_PLAN="view_request_select_plan";
	//修改广告合同页面 seven
	public static final String VIEW_REQUEST_UPDATE_ADVCONTRACT ="view_request_update_advcontract";
	//执行广告合同页面
	public static final String VIEW_REQUEST_DO_ADVCONTRACT="view_request_do_advcontract_success";
	//预览执行单
	public static final String VIEW_REQUEST_PRE_PERFORMANCE="view_request_pre_performance_success";
	//映前广告执行单
	public static final String VIEW_REQUEST_GEN_PERFORMANCE="view_request_gen_performance";
	//页面跳转
	public static final String VIEW_FLAG="view_flag";
	//查看方案里包含的资源住处
	public static final String VIEW_REQUEST_PRE_RESOURCE="view_request_pre_resource";
	/**
	 * <p> 左边菜单请求跳转到新增广告客户资料信息页面方法
	 * @param request   current HTTP request
	 * @param response  current HTTP responsepu
	 * @throws Exception
	 */
	public ModelAndView requestAddAdvCustomberInfo(HttpServletRequest request,HttpServletResponse response,AdvertCustomerInfo advertCustomerInfo)
		throws GenericRuntimeException{
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		Map modelMap = new HashMap();
		//得到广告客户资料编号
		long  advCustomerNumber = AdvertCodeController.getAdvertCode(AdvCustomerConstant.ADV_CUSTOMER_NUMBER_HEAD_PREFIX, AdvCustomerConstant.ADV_CUSTOMER_NUMBER_TYPE);
	    String finaladvCustomercodeNumber = AdvCustomerConstant.ADV_CUSTOMER_NUMBER_HEAD_PREFIX + "-" + NumberFormatUtil.advAgentNumberFormat(advCustomerNumber);
		modelMap.put("advCustomerInfo", advertCustomerInfo);
		modelMap.put("advCustomercodeNumber", finaladvCustomercodeNumber); //生成代理商编号
		return new ModelAndView(VIEW_REQUEST_ADDCUSTOMERTINFO_SUCCESS, modelMap); 
	}
	
	
	/**
	 * @Title:       addAdvCustomerInfo 
	 * @Description: TODO(保存广告客户资料方法) 
	 * @param        @param request
	 * @param        @param response
	 * @param        @param advertCustomerInfo
	 * @param        @return
	 * @param        @throws GenericRuntimeException
	 * @param        @throws IOException 
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-5 下午01:26:01
	 * @throws
	 */
	public ModelAndView addAdvCustomerInfo(HttpServletRequest request, HttpServletResponse response, AdvertCustomerInfo advertCustomerInfo) throws GenericRuntimeException, IOException {
		Map advcustomerMap = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		advcustomerMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		JSONObject json = new JSONObject();
		String[] allcontactNames = null;
		if(! StringUtils.isBlank(request.getParameter("allcontactNames"))){
			allcontactNames  	 = request.getParameter("allcontactNames").split(",");
		}
		String[] allcontactTels  		= request.getParameter("allcontactTels").split(",");
		String[] allcontactEmails  		= request.getParameter("allcontactEmails").split(",");
		String[] allcontactPhones  		= request.getParameter("allcontactPhones").split(",");
		String[] allcontactTypes  		= request.getParameter("allcontactTypes").split(",");
		String[] allcontractPositions	= request.getParameter("allcontractPositions").split(",");
		
		List<AdvertContactInfo> AdvertContactInfoList = new ArrayList<AdvertContactInfo>();
		Set<AdvertContactInfo>  AdvertContactInfoSet = new HashSet<AdvertContactInfo>();
		
		if(allcontactNames != null){
			for(int i = 0; i < allcontactNames.length; i++){
				AdvertContactInfo advinfo = new AdvertContactInfo();
					//字段循环处理
					for(int j = 1; j <= 6 ; j++ ){
						switch (j) {
						case 1:
							if(i < allcontactNames.length ){
							  advinfo.setContactName(allcontactNames[i]);
							}
							break;
						case 2:
							if(i < allcontactTels.length ){
							  advinfo.setContactTel(allcontactTels[i]);
							}
							break;
						case 3:
							if(i < allcontactEmails.length ){
								advinfo.setContactEmail(allcontactEmails[i]);
							}
							break;
						case 4:
							if(i < allcontactPhones.length ){
								 advinfo.setContactPhone(allcontactPhones[i]);
							}
							break;	
						case 5:
							if(i < allcontactTypes.length ){
								 advinfo.setContactType(allcontactTypes[i]);
							}
							break;	
						case 6:
							if(i < allcontractPositions.length ){
								 advinfo.setContractPosition(allcontractPositions[i]);
							}
							break;
						default:
							break;
						}
					}
				AdvertContactInfoList.add(advinfo);
				AdvertContactInfoSet.add(advinfo);
			}
		}
		advcustomerMap.put("keyAdvCustomerInfo", advertCustomerInfo);
		advcustomerMap.put("keyAdvContractInfoList", AdvertContactInfoList);
		advcustomerMap.put("keyAdvContractInfoSet", AdvertContactInfoSet);
		//判断公司名称是否存在
		boolean flag=advCustomerService.findAdvCustomerBycustomerName(advcustomerMap);
		if(!flag){
			json.put("failure",Constant.ADD_CUSTOMERNAME);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		
		//保存主表数据
		boolean addCustomerinfoTag = advCustomerService.addAdvCustomerInfo(advcustomerMap);
		//AdvertCodeController.addAdvertCode(AdvCustomerConstant.ADV_CUSTOMER_NUMBER_HEAD_PREFIX, AdvCustomerConstant.ADV_CUSTOMER_NUMBER_TYPE);
		
		if(addCustomerinfoTag){
			json.put("success",Constant.ADD_SUCCESS);
		}else{
			json.put("failure",Constant.ADD_FAILURE);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
}
	
	/**
	 * @Title:       queryAdvCustomerInfoList 
	 * @Description: TODO(维护广告客户资料模块-左边菜单请求得到的列表数据方法) 
	 * @param        @param request
	 * @param        @param response
	 * @param        @param advertCustomerInfo
	 * @param        @return
	 * @param        @throws GenericRuntimeException 
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-5 下午02:57:03
	 * @throws
	 */
	public ModelAndView  queryAdvCustomerInfoList(HttpServletRequest request, HttpServletResponse response, AdvertCustomerInfo advertCustomerInfo ) throws GenericRuntimeException {
	
		Map advcustomerMap = new HashMap();
		advertCustomerInfo.setPageSize(GlobalConstant.PAGE_SIZE); 	//10条数据
		advertCustomerInfo.setPageNavigationURL(CommUtil.getUrl(request)); 
		advcustomerMap.put("keyAdvCustomerInfo", advertCustomerInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		advcustomerMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		// 分页查询
		Page<AdvertCustomerInfo,AdvertCustomer>  pageResult = advCustomerService.findAdvCustomerInfos(advcustomerMap);
		pageResult.setPageNavigationURL(advertCustomerInfo.getPageNavigationURL());
	    
		//页面数据显示
	   Map modelMap = new HashMap();
	   modelMap.put("pageResult", pageResult); // 列表数据
	   modelMap.put("advertCustomerInfo", advertCustomerInfo);
	
	   return new ModelAndView(VIEW_QUERY_ADVCUSTOMERINFO_LIST_SUCCESS, modelMap);
	}
	
	
	/**
	 * @Title:       queryAdvCustomerInfoDetail 
	 * @Description: TODO(按照uuid 查询广告客户资料记录的详细信息) 
	 * @param        @param request
	 * @param        @param response
	 * @param        @param advertCustomerInfo
	 * @param        @return
	 * @param        @throws GenericRuntimeException 
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-5 下午03:43:56
	 * @throws
	 */
	public ModelAndView queryAdvCustomerInfoDetail(HttpServletRequest request, HttpServletResponse response, AdvertCustomerInfo advertCustomerInfo) throws GenericRuntimeException {
		//JSONObject json = null;
		Map advcustomerMap = new HashMap();
		advcustomerMap.put("keyAdvCustomerInfo", advertCustomerInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		advcustomerMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		AdvertCustomerInfo advagentInfoReturn = advCustomerService.findAdvCustomerInfoByCustomerId(advcustomerMap);
		List<AdvertContactInfo>  customerContactInfoList  = advCustomerService.findAdvCustomerContactInfoByCustomerId(advcustomerMap);
		
		JSONObject json  = new JSONObject();
		if(!customerContactInfoList.isEmpty()){
			for (int i = 0; i < customerContactInfoList.size(); i++) {
				JSONObject contactjaon  = new JSONObject();
				String contactname = customerContactInfoList.get(i).getContactName();
				if (StringUtils.isBlank(contactname)) {
					contactname = "";
				}
				contactjaon.put("contactName", ""+contactname+"");
				String contacttel = customerContactInfoList.get(i).getContactTel();
				if (StringUtils.isBlank(contacttel)) {
					contacttel = "";
				}
				contactjaon.put("contactTel", ""+contacttel+"");
				String contactemail = customerContactInfoList.get(i).getContactEmail();
				if (StringUtils.isBlank(contactemail)) {
					contactemail = "";
				}
				contactjaon.put("contactEmail", ""+contactemail+"");
				String contactphone = customerContactInfoList.get(i).getContactPhone();
				if (StringUtils.isBlank(contactphone)) {
					contactphone = "";
				}
				contactjaon.put("contactPhone", ""+contactphone+"");
				String contactType = customerContactInfoList.get(i).getContactType();
				if (StringUtils.isBlank(contactType)) {
					contactType = "";
				}
				contactjaon.put("contactType", ""+contactType+"");
				String contactposition = customerContactInfoList.get(i).getContractPosition();
				if (StringUtils.isBlank(contactposition)) {
					contactposition = "";
				}
				contactjaon.put("contractPosition", ""+contactposition+"");
				json.put(i, contactjaon);
			}
		}
		Map modelMap = new HashMap();
	    modelMap.put("advcustomerInfoReturn", advagentInfoReturn);
	    modelMap.put("customerContactInfoList", customerContactInfoList);
	    modelMap.put("customerContactInfoJson", json.toString());
	    return new ModelAndView(VIEW_QUERY_ADVCUSTOMERINFO_DETAILOPERATION_SUCCESS, modelMap);
   }

 	
	/**
	 * @throws Exception  
	 * @Title:       updateAdvCustomerInfo 
	 * @Description: TODO(按照uuid更新广告客户资料信息) 
	 * @param        @param request
	 * @param        @param response
	 * @param        @param advertCustomerInfo
	 * @param        @return
	 * @param        @throws GenericRuntimeException
	 * @param        @throws IOException 
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-9 下午03:59:30
	 * @throws
	 */
	public ModelAndView updateAdvCustomerInfo(HttpServletRequest request, HttpServletResponse response, AdvertCustomerInfo advertCustomerInfo) throws Exception {
	
		JSONObject json = new JSONObject();
		Map adcustomertMap = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		adcustomertMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		String[] allcontactNames = null;
		//if(! StringUtils.isBlank(request.getParameter("allcontactNames"))){
			allcontactNames  	 = request.getParameter("allcontactNames").split(",");
		//}
		String[] allcontactTels  		= request.getParameter("allcontactTels").split(",");
		String[] allcontactEmails  		= request.getParameter("allcontactEmails").split(",");
		String[] allcontactPhones  		= request.getParameter("allcontactPhones").split(",");
		String[] allcontactTypes  		= request.getParameter("allcontactTypes").split(",");
		String[] allcontractPositions	= request.getParameter("allcontractPositions").split(",");
		
		List<AdvertContactInfo> AdvertContactInfoList = new ArrayList<AdvertContactInfo>();
		
		if(allcontactNames != null){
			for(int i = 0; i < allcontactNames.length; i++){
				AdvertContactInfo advinfo = new AdvertContactInfo();
					//字段循环处理
					for(int j = 1; j <= 6 ; j++ ){
						switch (j) {
						case 1:
							if(i < allcontactNames.length ){
							  advinfo.setContactName(allcontactNames[i]);
							}
							break;
						case 2:
							if(i < allcontactTels.length ){
							  advinfo.setContactTel(allcontactTels[i]);
							}
							break;
						case 3:
							if(i < allcontactEmails.length ){
								advinfo.setContactEmail(allcontactEmails[i]);
							}
							break;
						case 4:
							if(i < allcontactPhones.length ){
								 advinfo.setContactPhone(allcontactPhones[i]);
							}
							break;	
						case 5:
							if(i < allcontactTypes.length ){
								 advinfo.setContactType(allcontactTypes[i]);
							}
							break;	
						case 6:
							if(i < allcontractPositions.length ){
								 advinfo.setContractPosition(allcontractPositions[i]);
							}
							break;
						default:
							break;
						}
					}
				AdvertContactInfoList.add(advinfo);
			}
		}
		adcustomertMap.put("keyAdvContractInfoList", AdvertContactInfoList);
		adcustomertMap.put("keyAdvCustomerInfo", advertCustomerInfo);
		
		//按照uuid更新表数据记录
		boolean updateTag = advCustomerService.updateAdvCustomerInfoByCustomerId(adcustomertMap);
		if(updateTag){
			json.put("success",Constant.UPDATE_SUCCESS);
		}else{
			json.put("failure",Constant.UPDATE_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
	   return  null;
   }
	

	/**
	 * 维护广告客户合同模块-左边菜单请求跳转方法
	 * @time 2012年4月9日11:45:55
	 * @author Seven
	 * @param request
	 * @param response
	 * @param advertCustomerInfo
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView requestMaintainAdvconTract(HttpServletRequest request,HttpServletResponse response, AdvertCustomerInfo advertCustomerInfo)
			throws GenericRuntimeException {
		Map modelMap = new HashMap();
		HttpSession session = request.getSession();
		//从session中拿到标志,此标志是增加或者修改成功
		String flag = (String)session.getAttribute("flag1");
		session.removeAttribute("flag1");
		//查询客户,用于下拉框
		Map parames = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		parames.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		List<AdvertCustomerInfo> advertCustomerInfosList = advCustomerService.findAdvCustomer(parames);
		modelMap.put("advCustomerInfo",advertCustomerInfosList);
		AdvertContractInfo advertContractInfo= new AdvertContractInfo();
		Map advCustomerMap = new HashMap();
		//设置用于传入dao中的VO,此VO用来传参数
		String customerId= request.getParameter("customerId");
		if(!StringUtils.isBlank(customerId)){
			advertCustomerInfo.setCustomerId(customerId);
			request.setAttribute("customerId", customerId);
		}
		advertContractInfo.setAdvertCustomer(advertCustomerInfo);
//		advertContractInfo.setCurrentPageNumber(advertCustomerInfo.getCurrentPageNumber());
//		advertContractInfo.setPageSize(GlobalConstant.PAGE_SIZE);
//		advertContractInfo.setPageNavigationURL(CommUtil.getUrl(request));
		//试图 
		VAdvContractInfo vAdvContractInfo = new VAdvContractInfo();
		vAdvContractInfo.setCurrentPageNumber(advertCustomerInfo.getCurrentPageNumber());
		vAdvContractInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		vAdvContractInfo.setPageNavigationURL(CommUtil.getUrl(request));
		//放入map
		advCustomerMap.put("advertContractInfo", advertContractInfo);
		advCustomerMap.put("vAdvContractInfokey", vAdvContractInfo);
		advCustomerMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		String orgCodeValue = JudgementAuthority.getUserInfo(request).getOrgName();
		advCustomerMap.put("orgCodeValue", orgCodeValue);
		// 分页查询
		//Page<AdvertContractInfo, AdvertContract> pageResult = advCustomerService.advertContractInfos(advCustomerMap);
		Page<VAdvContractInfo, VAdvContract> pageResult = advCustomerService.findVAdvertContracts(advCustomerMap);
		
		List<VAdvContractInfo>  vAdvContractInfosPageResult = advCustomerService.findVAdvContractsNotPage(advCustomerMap);
		
		
		pageResult.setPageNavigationURL(vAdvContractInfo.getPageNavigationURL());
		// 页面数据显示
		modelMap.put("pageResult", pageResult); // 1.列表数据展示
		modelMap.put("vAdvContractInfosPageResult", getUniqueCustomer(vAdvContractInfosPageResult)); // 2.展示查询条件中的所有客户名称
		modelMap.put("flag", flag);
		modelMap.put("advertContractInfo", advertContractInfo);
		//取出是否外包合同的数据
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.IS_OUT_SOURING_CONTRACT); // 得到是否外包合同
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> dictionaryOutSourcingList = dictionaryService.findDataDictionarys(dictionaryMap);
		//初始化广告客户合同的合同类型
		Map customertypeMap = new HashMap();
		DataDictionaryInfo daDictionaryInfo = new DataDictionaryInfo();
		daDictionaryInfo.setFDataId(Dictionary.ADV_CUSTOMER_CONTRACTTYPE); //广告客户合同的合同类型
		customertypeMap.put("dataDictionaryInfoKey", daDictionaryInfo);
		List<DataDictionaryInfo> customerTypeList = dictionaryService.findDataDictionarys1(customertypeMap);
		modelMap.put("dictionaryOutSourcingList", dictionaryOutSourcingList);
		modelMap.put("customerTypeList", customerTypeList);
		return new ModelAndView(VIEW_REQUEST_MAINTAIN_ADVCONTRACT, modelMap);
	}
	private List<VAdvContractInfo> getUniqueCustomer(List<VAdvContractInfo> vAdvContractInfosPageResult){
		Set set=new HashSet();
		List<VAdvContractInfo> list=new ArrayList<VAdvContractInfo>();
		for(VAdvContractInfo vAdvContractInfo:vAdvContractInfosPageResult){
			String key=vAdvContractInfo.getCustomerId()+"-"+vAdvContractInfo.getCustomerContractCustomertype();
			if(!set.contains(key)){
				list.add(vAdvContractInfo);
				set.add(key);
			}
		}
		return list;
	}
	/**
	 * 点击录入广告合同-进入广告录入页面方法<p>
	 * 点击选择方案后也会执行这个方法
	 * @author Seven
	 * @time 2012-4-10 15:16:36
	 * @param request
	 * @param response
	 * @param advertCustomerInfo
	 * @return
	 * @author yue_gao  修改
	 * @date   2012-5-11 13:44:36
	 * @throws GenericRuntimeException
	 */
	public ModelAndView requestAddAdvconTract(HttpServletRequest request,HttpServletResponse response, AdvertContractInfo advertContractInfo)
			throws GenericRuntimeException {
		HttpSession session=request.getSession();
		String update = request.getParameter("update");//标志是否是从修改页面跳转进去的,如果是.还是返回修改页面
		Map modelMap = new HashMap();
		//这三个从页面得到的参数,用于跳转后显示
		String planId= request.getParameter("planId");
		String price= request.getParameter("price");
		String planName = request.getParameter("planName");
		String remark= request.getParameter("remark");
		AdvertPlanInfo planInfo = new AdvertPlanInfo();
		planInfo.setPlanId(planId);
		planInfo.setPlanName(planName);
		planInfo.setRemark(remark);
		//查询金逸信息,因为唯一,所以无参数
		JinyiInfo jinyiInfo = advCustomerService.queryJinyi();
		Map parames = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		parames.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		String customerContractId=request.getParameter("customerContractId");
		String key=null;
		if(StringUtils.isBlank(customerContractId)){
			key="advertContractInfo";
		}else{
			key="advertContractInfo-"+customerContractId;
		}
		//advertContractInfo这是方便用于,选择方案时,传递大对象.传递时用session存储
		AdvertContractInfo tmp=null;
		tmp=(AdvertContractInfo)session.getAttribute(key);
		if(tmp!=null){
			advertContractInfo=tmp;
		}
		if(null==advertContractInfo||"1".equals(request.getParameter("flagEnter"))){
			advertContractInfo=new AdvertContractInfo();
		}
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
		// 页面数据显示
		advertContractInfo.setJinyi(jinyiInfo);
		advertContractInfo.setAdvertPlan(planInfo);
		if(StringUtils.isNotBlank(price)){
			advertContractInfo.setContractAmount((BigDecimal.valueOf(Double.valueOf(price))));
			advertContractInfo.setContractAmountVo(price);
		}
		//用于选择合同开始时间和结束时间
		Map params = new HashMap();
		AdvertPlanInfo aPlanInfo = new AdvertPlanInfo();
		aPlanInfo.setPlanId(planId);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		params.put("aInfoKey", aPlanInfo);
		List<AdvertResourceInfo> resourceInfo = advCustomerService.findAdvertResourcesByPlanId(params);
		if(null !=resourceInfo && resourceInfo.size()>0 && StringUtils.isNotBlank(resourceInfo.get(0).getUseDate())){
			/*if(resourceInfo.size()==1){
				String[] useDate=resourceInfo.get(0).getUseDate().replace("<br/>", "").split(",");
				try {
					advertContractInfo.setCustomerContractStartTime(DateUtil.StringTodate3(useDate[0]));
					advertContractInfo.setCustomerContractEndTime(DateUtil.StringTodate3(useDate[useDate.length-1]));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(resourceInfo.size()>1){*/
				String useDate="";
				for(int i=0;i<resourceInfo.size();i++){
					if(i>0){
						useDate+=",";
					}
					if(StringUtils.isNotBlank(resourceInfo.get(i).getUseDate())){
						useDate +=resourceInfo.get(i).getUseDate();
					}
					
					
					
				}
				String[] useDateForSort=useDate.replace("<br/>", "").split(",");
				 Arrays.sort(useDateForSort, new Comparator(){public int compare(Object o1,Object o2)  
			        {      
					 //降序  
					 int flag=1;
					 	try {
							if(DateUtil.StringTodate3(o1.toString()).before(DateUtil.StringTodate3(o2.toString())))
								flag=-1;
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
						return flag;
			  
			//如果o1在前面，则升序  
			        }  
			        });
				 	try {
						advertContractInfo.setCustomerContractStartTime(DateUtil.StringTodate3(useDateForSort[0]));
						advertContractInfo.setCustomerContractEndTime(DateUtil.StringTodate3(useDateForSort[useDateForSort.length-1]));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		//	}
			/*String[] useDate=resourceInfo.get(0).getUseDate().replace("<br/>", "").split(",");
			if(useDate.length==1){
				try {
					advertContractInfo.setCustomerContractStartTime(DateUtil.StringTodate3(useDate[0]));
					advertContractInfo.setCustomerContractEndTime(DateUtil.StringTodate3(useDate[0]));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					advertContractInfo.setCustomerContractStartTime(DateUtil.StringTodate3(useDate[0]));
					advertContractInfo.setCustomerContractEndTime(DateUtil.StringTodate3(useDate[useDate.length-1]));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
		}
		modelMap.put("advertContractInfo", advertContractInfo);
		//这三个date主要用来页面展现,因为date型在页面的格式不好控制 
		if (advertContractInfo.getCustomerContractEndTime()!=null) {
			modelMap.put("date3", sdf.format(advertContractInfo.getCustomerContractEndTime()));
		}
		if (advertContractInfo.getCustomerContractSignTime()!=null) {
			modelMap.put("date1", sdf.format(advertContractInfo.getCustomerContractSignTime()));
		}
		if (advertContractInfo.getCustomerContractStartTime()!=null) {
			modelMap.put("date2", sdf.format(advertContractInfo.getCustomerContractStartTime()));
		}
		//用于方案编号的展现
		if(null!=planId&&!"".equals(planId)){
			Map advplanMap = new HashMap();
			AdvertPlanInfo  apio =  new AdvertPlanInfo();
			apio.setPlanId(planId);
			advplanMap.put("advertPlanKey", apio);
			//根据方案ID，查找方案详细信息
			AdvertPlanInfo  advertPlanInfo = advResourcePlanService.findAdvertPlanInfo(advplanMap);
			modelMap.put("plan", advertPlanInfo);
		}
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		//付款方式
		dataDictionaryInfo.setFDataId(Dictionary.CUSTOMER_PAYMENT); 
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> paymentList = dictionaryService.findDataDictionarys(dictionaryMap);
		modelMap.put("paymentList", paymentList);
		//初始化广告客户合同的合同类型
		Map customertypeMap = new HashMap();
		DataDictionaryInfo daDictionaryInfo = new DataDictionaryInfo();
		daDictionaryInfo.setFDataId(Dictionary.ADV_CUSTOMER_CONTRACTTYPE); //广告客户合同的合同类型
		customertypeMap.put("dataDictionaryInfoKey", daDictionaryInfo);
		List<DataDictionaryInfo> customerTypeList = dictionaryService.findDataDictionarys1(customertypeMap);
		modelMap.put("customerTypeList", customerTypeList);
		//初始化选择客户类型
		Map choosecustomerTypeMap = new HashMap();
		DataDictionaryInfo dictionaryInfo = new DataDictionaryInfo();
		dictionaryInfo.setFDataId(Dictionary.CHOOSECUSTOMERTYPE); //选择客户类型
		choosecustomerTypeMap.put("dataDictionaryInfoKey", dictionaryInfo);
		List<DataDictionaryInfo> chooseCustomerTypeList = dictionaryService.findDataDictionarys(choosecustomerTypeMap);
		modelMap.put("chooseCustomerTypeList", chooseCustomerTypeList);
		initRegionAndCityAndCinema(advertContractInfo,orgModels,modelMap);
		String isModify = request.getParameter("isModify");
		modelMap.put("isModify", isModify);
		
		if (update!=null&&"1".equals(update)) {
			return new ModelAndView(VIEW_REQUEST_UPDATE_ADVCONTRACT, modelMap);
		}
		return new ModelAndView(VIEW_REQUEST_ADD_ADVCONTRACT, modelMap);
	}
	/**
	 * 选择方案页面跳转
	 * @author Seven
	 * @time 2012-4-11 13:49:05
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws GenericRuntimeException
	 * @throws ParseException 
	 */
	public ModelAndView requestSelectPlan(HttpServletRequest request,HttpServletResponse response, AdvertPlanInfo advertPlanInfo)
	throws GenericRuntimeException, ParseException {
		try{
		Map advCustomerMap = new HashMap();
		String update = request.getParameter("update");
		String radioPlanId = request.getParameter("radioPlanId");
		String returnUrl = request.getParameter("returnUrl");
		advertPlanInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		advertPlanInfo.setPageNavigationURL(CommUtil.getUrl(request));
		advCustomerMap.put("advertPlanInfo", advertPlanInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		advCustomerMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		String isQuery = request.getParameter("isQuery");
		String isHandsel = request.getParameter("isHandsel");
		advCustomerMap.put("isHandsel", isHandsel);
		advCustomerMap.put("customerContractType", request.getParameter("customerContractType"));
		// 分页查询
		if("1".equals(isQuery)){ //如果是执行查询的话
			String selectRegionName = request.getParameter("selectRegionName");
			String selectCityName = request.getParameter("selectCityName");
			String selectCinemaName = request.getParameter("selectCinemaName");
			String planNameTxt = request.getParameter("planNameTxt");
			advCustomerMap.put("selectRegionName", selectRegionName);
			advCustomerMap.put("selectCityName", selectCityName);
			advCustomerMap.put("selectCinemaName", selectCinemaName);
			advCustomerMap.put("planNameTxt", planNameTxt);
		}
		Page<AdvertPlanInfo,Object[]> pageResult = advCustomerService.findAdvPlansNoOccupation(advCustomerMap);
		pageResult.setPageNavigationURL(advertPlanInfo.getPageNavigationURL());
		AdvertContractInfo newObj=new AdvertContractInfo();
		//方案页面中的查询和分页不做处理
		if(!request.getParameterMap().containsKey("radioPlanId")){
			if(null!=update&&!"".equals(update)){
				newObj=inAdvertContractInfo2Session(request);
			}else{
				newObj=inAdvertContractInfo(request);
				HttpSession session = request.getSession();
				session.setAttribute("advertContractInfo", newObj);
			}
		}
		// 页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("update", update);
		modelMap.put("isModify", request.getParameter("isModify"));
		modelMap.put("radioPlanId", radioPlanId);
		modelMap.put("returnUrl", returnUrl);
		modelMap.put("isHandsel", isHandsel);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(advCustomerMap);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("orgRegions", orgRegions);
		String customerContractId=request.getParameter("customerContractId");
		modelMap.put("customerContractId", customerContractId);
		//modelMap.put("isInitializeAgentOrCustomerInfo", "initial");//是否初始化代理商或者是客户资料的地址,电话传真等信息
		// 页面数据显示
		return new ModelAndView(VIEW_REQUEST_SELECT_PLAN, modelMap);
		}catch(Exception ex){
			ex.printStackTrace();
			return new ModelAndView(VIEW_REQUEST_SELECT_PLAN, null);
		}
	}
	
	
	/**
	 * 新增以及修改合同方法
	 * @author Seven
	 * @time 2012-4-11 11:46:02
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView createAdvContract(HttpServletRequest request,HttpServletResponse response, AdvertContractInfo advertContractInfo)
			throws Exception {
		//清空所有保存的AdvertContractInfo
		Enumeration e=request.getSession().getAttributeNames();
		while(e.hasMoreElements()){
			String attrName=e.nextElement().toString();
			if(attrName.contains("advertContractInfo-")){
				request.getSession().removeAttribute(attrName); //清空session中所有的广告合同对象，避免内存泄露
			}
		}
		request.getSession().removeAttribute("advertContractInfo");
		//通过request里还有的值,放入inAdvertContractInfo方法进行对象封装
		AdvertContractInfo newObj = inAdvertContractInfo(request);//获取前面页面传过来的值

		Map advcustomerMap = new HashMap();
		
		advcustomerMap.put("keyAdvCustomerInfo", newObj);

		List<VAdvContractInfo> contractCodeList=advCustomerService.findVAdvContractsCode(newObj.getCustomerContractType());
		
		String contractCode=getContractCode(contractCodeList,newObj.getCustomerContractType());
		
		newObj.setCustomerContractCode(contractCode);
		
		Map modelMap = new HashMap();
		//将对象持久化,返回BOOLEAN
		Map mapObj = new HashMap();
		if(null==newObj.getCustomerContractStartTime() || "null".equals(newObj.getCustomerContractStartTime().toString())){
			newObj.setCustomerContractStartTime(StringTodate(request.getParameter("datepicker2")));
		}
		if(null==newObj.getCustomerContractEndTime() || "null".equals(newObj.getCustomerContractEndTime().toString())){
			newObj.setCustomerContractEndTime(StringTodate(request.getParameter("datepicker1")));
		}
		mapObj.put("newObj", newObj);
		advertContractInfo.setCustomerContractStartTime(StringTodate(request.getParameter("datepicker2")));
		advertContractInfo.setCustomerContractEndTime(StringTodate(request.getParameter("datepicker1")));
		
		
		
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		mapObj.put(GlobalConstant.USER_CODE_KEY, orgModels);
		String orgCodeValue = JudgementAuthority.getUserInfo(request).getOrgName();
		mapObj.put("orgCodeValue", orgCodeValue);
		//保存合同之前先判断下用户选择的客户资料信息在表中是否存在,防止冗余数据(可能包含代理商信息)
		/**
		 * 1.如果用户选择的是代理商 就查询代理商的表
		 * 2.如果用户选择的是广告客户资料的 就查询客户资料信息的表
		 */
		boolean tag = false;
		Map parames = new HashMap();
		AdvertCustomerInfo ac = new AdvertCustomerInfo();
		ac.setCustomerId(newObj.getAdvertCustomer().getCustomerId());
		parames.put(GlobalConstant.USER_CODE_KEY, orgModels);
		parames.put("customerInfotKey",ac);
		Long customertype = Long.valueOf(newObj.getCustomerContractCustomertype());//选择客户类型 代理商 || 非代理商
		if (customertype == Dictionary.CHOOSECUSTOMERTYPE_NOTAGENT) {//非代理商
			AdvertCustomerInfo  acif = advCustomerService.findAdvCustomerById(parames);
			if (acif != null && acif.getCustomerCode() != null) {
				tag = true;
			}else{
				tag = false;
			}
		}else if(customertype == Dictionary.CHOOSECUSTOMERTYPE_AGENT){//代理商
			AgentInfo agentInfo = new AgentInfo();
			agentInfo.setAgentId(newObj.getAdvertCustomer().getCustomerId());
			parames.put("keyAdvAgentInfo", agentInfo);
			AgentInfo  aInfo = advAgentService.findAdvAgentInfoByAgentIdHQL(parames);
			if (aInfo != null && aInfo.getAgentCode() != null) {
				tag = true;
			}else{
				tag = false;
			}
		}
		if (tag) {
//			boolean res = advCustomerService.isExsitContract(advertContractInfo.getCustomerContractCode());
//			if(res){
//				modelMap.put("advertContractInfo", newObj);
//				//msg放入session,跳转页面时,弹出窗口
//				modelMap.put("flag1", "合同编号已存在");
//				HttpSession session = request.getSession();
//				session.setAttribute("addContractSuccessInfoFlag", "合同编号已存在");
//				//失败跳转
//				return new ModelAndView(new RedirectView("requestAddAdvconTract.do",true));
//				//return new ModelAndView(VIEW_REQUEST_ADD_ADVCONTRACT, modelMap);
//			}
			boolean s = advCustomerService.addCustomerContract(mapObj);//持久化对象
			if (s) {
				//成功后的操作
				modelMap.put("advertContractInfo", newObj);
				HttpSession session = request.getSession();
				session.setAttribute("addContractSuccessInfoFlag", "保存成功");
				return new ModelAndView(new RedirectView("requestAddAdvconTract.do",true));
			}else {
				modelMap.put("advertContractInfo", newObj);
				//msg放入session,跳转页面时,弹出窗口
				modelMap.put("flag1", "保存失败");
				//失败跳转
				return new ModelAndView(VIEW_FLAG, modelMap);
			}
		}else{
			return new ModelAndView(GlobalConstant.SYSTEM_EXCEPTION);
		}
		
	}
	
	public ModelAndView isExsit(HttpServletRequest request,HttpServletResponse response, AdvertContractInfo advertContractInfo) throws Exception {
		boolean res = advCustomerService.isExsitContract(advertContractInfo.getCustomerContractCode().trim());
		JSONObject json = new JSONObject();
		if(res){
			json.put("failure", "合同编号已存在");
			IOUtil.writeJSON(json, response.getOutputStream()); 
			
		}else{
			json.put("success", "success");
			IOUtil.writeJSON(json, response.getOutputStream()); 
			return null;
		}
		return null;
	}
	
	public ModelAndView isExpire(HttpServletRequest request,HttpServletResponse response, AdvertContractInfo advertContractInfo) throws Exception {
//		boolean res = advCustomerService.isExsitContract(advertContractInfo.getCustomerContractCode().trim());
		Map params = new HashMap();
		params.put("agentIdKey", advertContractInfo.getCustomerId());
		Agent  agent = advAgentService.findAdvAgentInfoById(params);
		Set<AgentContract> agentContracts = agent.getAgentContracts();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		JSONObject json = new JSONObject();
		
		for(AgentContract agentContract:agentContracts){
			String agentContractStartTime = sdf.format(agentContract.getAgentContractStartTime());
			String agentContractEndTime = sdf.format(agentContract.getAgentContractEndTime());
			Integer agentContractStartTimeToInt = Integer.valueOf(agentContractStartTime);
			Integer agentContractEndTimeToInt = Integer.valueOf(agentContractEndTime);
			Integer startDateTemp = Integer.valueOf(startDate);
			Integer endDateTemp = Integer.valueOf(endDate);
			if(agentContractStartTimeToInt<startDateTemp || agentContractEndTimeToInt>endDateTemp){
				json.put("failure", "该代理商所签代理商合同时间不符合该广告合同起止时间！");
				IOUtil.writeJSON(json, response.getOutputStream()); 
				return null;
			}
		}
//		if(res){
//			json.put("failure", "合同编号已存在");
//			IOUtil.writeJSON(json, response.getOutputStream()); 
//			
//		}else{
//			json.put("success", "success");
//			IOUtil.writeJSON(json, response.getOutputStream()); 
//			return null;
//		}
		return null;
	}
	
	/**
	 * 维护广告客户合同-点击修改时候修改广告合同信息方法
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView updateAdvContract(HttpServletRequest request,HttpServletResponse response, AdvertContractInfo advertContractInfo)
			throws Exception {
		//通过request里还有的值,放入inAdvertContractInfo方法进行对象封装
		AdvertContractInfo newObj = inAdvertContractInfo(request);
		Map modelMap = new HashMap();
		//将对象持久化,返回BOOLEAN
		Map map = new HashMap();
		if(null==newObj.getCustomerContractStartTime() || "null".equals(newObj.getCustomerContractStartTime().toString())){
			newObj.setCustomerContractStartTime(StringTodate(request.getParameter("datepicker2")));
		}
		if(null==newObj.getCustomerContractEndTime() || "null".equals(newObj.getCustomerContractEndTime().toString())){
			newObj.setCustomerContractEndTime(StringTodate(request.getParameter("datepicker1")));
		}
		if(null==newObj.getContractType() || "null".equals(newObj.getContractType())){
			newObj.setContractType(request.getParameter("customerContractType"));
		}
		map.put("advertContractInfokey", newObj);
		
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		map.put(GlobalConstant.USER_CODE_KEY, orgModels);
		boolean s = advCustomerService.updateCustomerContract(map);
		
		if (s) {
			//msg放入session,跳转页面时,弹出窗口
			modelMap.put("advertContractInfo", newObj);
			request.setAttribute("flag1","修改成功");
			/*HttpSession session = request.getSession();
			session.setAttribute("flag1", "修改成功");*/
			//return queryAdvContractList(request,response,new AdvertContractInfo());
			//获取当前页面的 ContractId 和 isAgentContract 
			String customerContractId = request.getParameter("customerContractId");
			//isAgentContract 这个值在update-advcontract.jsp页面上的隐藏域
			AdvertContractInfo aci = new AdvertContractInfo();
			aci.setCustomerContractId(customerContractId);
			return queryAdvContract(request,response,aci);
		}else {
			//msg放入session,跳转页面时,弹出窗口
			modelMap.put("advertContractInfo", newObj);
			modelMap.put("flag1", "修改失败");
			return new ModelAndView(VIEW_FLAG, modelMap);
		}
	}
	
	/**
	 * 合同数据装载通用方法
	 * @param advertContractInfo
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	public AdvertContractInfo inAdvertContractInfo(HttpServletRequest request,AdvertContractInfo contract) throws ParseException{
		/**
		 * 1.广告代理商信息或者是广告客户资料信息转换4个字段
		 */
		AdvertCustomerInfo custTemp = new AdvertCustomerInfo();
		String customerId = request.getParameter("customerId");
		String customerAddress = request.getParameter("customerAddress");
		String customerFax = request.getParameter("customerFax");
		String customerTel = request.getParameter("customerTel");
		Long customertype = Long.valueOf(request.getParameter("customerContractCustomertype"));//选择客户类型 代理商 || 广告客户资料 
		if (customertype == Dictionary.CHOOSECUSTOMERTYPE_NOTAGENT) {
			custTemp.setCustomerId(customerId);				//广告代理商或者是广告客户资料信息的uuid
			custTemp.setCustomerAddress(customerAddress);	//甲方地址 
			custTemp.setCustomerFax(customerFax);			//甲方传真
			custTemp.setCustomerTel(customerTel);			//甲方电话
		}else if(customertype == Dictionary.CHOOSECUSTOMERTYPE_AGENT){//如果是代理商需要设置代理商的uuid
			custTemp.setCustomerId(customerId);		
			custTemp.setCustomerAddress(customerAddress);	//甲方地址 
			custTemp.setCustomerFax(customerFax);			//甲方传真
			custTemp.setCustomerTel(customerTel);			//甲方电话
		}
		/**
		 * 2.方案信息
		 */
		AdvertPlanInfo planTemp = new AdvertPlanInfo();
		String planId = request.getParameter("planId");
		planTemp.setPlanId(planId);
		/**
		 * 3.jinyi信息8个字段
		 */
		JinyiInfo jinyiTemp = new JinyiInfo();
		String jinyiId = request.getParameter("jinyiId");
		String companyName = request.getParameter("companyName");
		String address = request.getParameter("address");
		String tel = request.getParameter("tel");
		String fax = request.getParameter("fax");
		String accountName = request.getParameter("accountName");
		String bank = request.getParameter("bank");
		String accountNumber = request.getParameter("accountNumber");
		jinyiTemp.setJinyiId(jinyiId);
		jinyiTemp.setCompanyName(companyName);
		jinyiTemp.setAddress(address);
		jinyiTemp.setTel(tel);
		jinyiTemp.setFax(fax);
		jinyiTemp.setAccountName(accountName);
		jinyiTemp.setAccountNumber(accountNumber);
		jinyiTemp.setBank(bank);
		/**
		 * 广告客户合同信息
		 */
		String advertContent = request.getParameter("advertContent");	//广告内容
		BigDecimal contractAmount= null;
		if(request.getParameter("contractAmount")==null||request.getParameter("contractAmount").equals("")){
			contractAmount = new BigDecimal(0);
		}else{
			contractAmount = new BigDecimal(request.getParameter("contractAmount"));
		}
		String contractManager=request.getParameter("contractManager");
		String customerContractCode=request.getParameter("customerContractCode");
		String endTime=request.getParameter("customerContractEndTime");
		//将其设置为yyyy-MM-dd HH:mm:ss的形式，避免出现少1天的情况.
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=" 11:11:11";
		Date customerContractEndTime = null;
		if(StringUtils.isNotEmpty(endTime)){
			customerContractEndTime=sdf.parse(endTime+time);
		}
		
		String startTime=request.getParameter("customerContractStartTime");
		Date customerContractStartTime = null;
		if(StringUtils.isNotEmpty(startTime)){
			customerContractStartTime=sdf.parse(startTime+time);
		}
		
		String customerContractId 	= request.getParameter("customerContractId");	//合同uuid
		String customerContractName = request.getParameter("customerContractName"); //合同名称
		String customerContractUrl	= request.getParameter("customerContractUrl");	//合同的url地址
		String signTime				= request.getParameter("customerContractSignTime");//合同签订时间
		java.util.Date customerContractSignTime = signTime == null? null:sdf.parse(signTime+time);
		String payment	= request.getParameter("payment");//付款方式 
		String remark = request.getParameter("remark");
		String receipt = request.getParameter("receipt");
		contract.setReceipt(receipt);
		contract.setCustomerContractId(customerContractId);//合同ID
		contract.setCustomerContractCode(customerContractCode);//合同code 
		contract.setCustomerContractName(customerContractName);//合同名称
		contract.setCustomerContractType(request.getParameter("customerContractType"));//合同类型
		contract.setCustomerContractCustomertype(request.getParameter("customerContractCustomertype"));//选择客户类型:代理商 || 非代理商
		//甲方地址 电话 传真 uuid 已经有了
		contract.setContractManager(contractManager); //甲方经办人
		contract.setCustomerContractSignTime(customerContractSignTime); //合同签订时间
		contract.setCustomerContractStartTime(customerContractStartTime);//合同有效期开始
		contract.setCustomerContractEndTime(customerContractEndTime);//合同有效期结束
		//方案信息-在下边的对象里面
		contract.setContractAmount(contractAmount);//合同金额
		contract.setPayment(Long.valueOf(payment));//付款方式
		contract.setAdvertContent(advertContent);//广告内容
		contract.setRegionId(request.getParameter("selectRegionName"));//区域
		contract.setCityId(request.getParameter("selectCityName"));//城市
		contract.setCinameId(request.getParameter("selectCinemaName"));//影院名称
		contract.setCinameContactname(request.getParameter("cinameContactname"));//影城联系人 
		contract.setCinameContacttel(request.getParameter("cinameContacttel")); //影城联系人电话
		contract.setRemark(remark);	//备注
		contract.setCustomerContractUrl(customerContractUrl);//合同文件上传地址 
		List<OrgModel> orgModels = initOrgModels(request);
		contract.setCreateUserId(orgModels.get(0).getName()); //创建人
		contract.setJinyi(jinyiTemp);
		contract.setAdvertCustomer(custTemp);	//广告代理商或者是广告客户信息
		contract.setAdvertPlan(planTemp);		//方案信息
		contract.setPerformenceStatus(request.getParameter("performenceStatus"));
		String cinemaIds = request.getParameter("cinemaIds");
		contract.setCinemaIds(cinemaIds);
		return contract;
	}
	
	public AdvertContractInfo inAdvertContractInfo(HttpServletRequest request) throws ParseException{
		return inAdvertContractInfo(request,new AdvertContractInfo());
	}
	
	public AdvertContractInfo inAdvertContractInfo2Session(HttpServletRequest request) throws ParseException{
		String customerContractId=request.getParameter("customerContractId");
		AdvertContractInfo advertContractInfo=(AdvertContractInfo)request.getSession().getAttribute("advertContractInfo-"+customerContractId);
		if(null==advertContractInfo)advertContractInfo=new AdvertContractInfo();
		return inAdvertContractInfoNojinyi(request,advertContractInfo);
	}
	
	public AdvertContractInfo inAdvertContractInfoNojinyi(HttpServletRequest request,AdvertContractInfo contract) throws ParseException{
		//客户数据
		AdvertCustomerInfo custTemp=new AdvertCustomerInfo();
		String customerId=request.getParameter("customerId");
		String customerAddress=request.getParameter("customerAddress");
		String customerFax=request.getParameter("customerFax");
		String customerTel=request.getParameter("customerTel");
		custTemp.setCustomerId(customerId);
		custTemp.setCustomerAddress(customerAddress);
		custTemp.setCustomerFax(customerFax);
		custTemp.setCustomerTel(customerTel);
		String advertContent=request.getParameter("advertContent");
		BigDecimal contractAmount=request.getParameter("contractAmount")==null?new BigDecimal(0):new BigDecimal(request.getParameter("contractAmount"));
		String contractManager=request.getParameter("contractManager");
		String endTime=request.getParameter("customerContractEndTime");
		//将其设置为yyyy-MM-dd HH:mm:ss的形式，避免出现少1天的情况.
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=" 11:11:11";
		java.util.Date customerContractEndTime=endTime==null?null:sdf.parse(endTime+time);
		String startTime=request.getParameter("customerContractStartTime");
		java.util.Date customerContractStartTime=startTime==null?null:sdf.parse(startTime+time);
		String customerContractId=request.getParameter("customerContractId");
		String customerContractName=request.getParameter("customerContractName");
		String customerContractUrl=request.getParameter("customerContractUrl");
		String signTime=request.getParameter("customerContractSignTime");
		java.util.Date customerContractSignTime=signTime==null?null:sdf.parse(signTime+time);
		String isOutsourcingContract=request.getParameter("isOutsourcingContract");
		String payment=request.getParameter("payment");
		String remark=request.getParameter("remark");
		String receipt = request.getParameter("receipt");
		if(!StringUtils.isBlank(advertContent))contract.setAdvertContent(advertContent);
		contract.setContractAmount(contractAmount);
		if(!StringUtils.isBlank(contractManager))contract.setContractManager(contractManager);
		List<OrgModel> orgModels = initOrgModels(request);
		contract.setCreateUserId(orgModels.get(0).getName());
		contract.setCustomerContractEndTime(customerContractEndTime);
		contract.setCustomerContractStartTime(customerContractStartTime);
		contract.setCustomerContractId(customerContractId);
		contract.setReceipt(receipt);
		contract.setCustomerContractType(request.getParameter("customerContractType"));//合同类型
		if(!StringUtils.isBlank(customerContractName))contract.setCustomerContractName(customerContractName);
		if(!StringUtils.isBlank(customerContractUrl))contract.setCustomerContractUrl(customerContractUrl);
		contract.setCustomerContractSignTime(customerContractSignTime);
		if(!StringUtils.isBlank(payment))contract.setPayment(Long.valueOf(payment));
		contract.setRemark(remark);
		contract.setAdvertCustomer(custTemp);
		if(!StringUtils.isBlank(request.getParameter("performenceStatus")))contract.setPerformenceStatus(request.getParameter("performenceStatus"));
		return contract;
	}
	
	/**
	 * 用于异步加载客户信息<br/>
	 * 下拉框值改变时.加载新数据
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView requestAddAdvconTract2(HttpServletRequest request,HttpServletResponse response, AdvertCustomerInfo advertCustomerInfo)
			throws GenericRuntimeException {
		
		//拿到页面传入的customerId
		String customerId = advertCustomerInfo.getCustomerId();
		AdvertCustomerInfo CustomerInfo = new AdvertCustomerInfo();
		AdvertCustomerInfo customerInfot = new AdvertCustomerInfo();
		customerInfot.setCustomerId(customerId);
		Map params = new HashMap();
		params.put("customerInfotKey", customerInfot);
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		 
		//通过id查询客户信息
		if (!"".equals(customerId) && customerId != "") {
			CustomerInfo = advCustomerService.findAdvCustomerById(params);
		}
		//把对象转为json数组
		JSONArray json =JSONArray.fromObject(CustomerInfo);
		try {
			//写入Stream
			IOUtil.writeJSONArray(json, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 维护广告合同-按照客户名称,关联影城,是否代理商合同等条件查询分页的方法
	 * @author Seven
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView queryAdvContractList(HttpServletRequest request,HttpServletResponse response, AdvertContractInfo advertContractInfo)
			throws GenericRuntimeException {
		Map advCustomerMap = new HashMap();
		String flag =request.getParameter("flag");
		String customerId= request.getParameter("customerId");
		AdvertCustomerInfo advertCustomerInfo= new AdvertCustomerInfo();
		advertCustomerInfo.setCustomerId(customerId);
		advertContractInfo.setAdvertCustomer(advertCustomerInfo);
		//试图
		VAdvContractInfo vAdvContractInfo = new VAdvContractInfo();
		vAdvContractInfo.setCustomerContractCode(advertContractInfo.getCustomerContractCode());
		vAdvContractInfo.setCustomerContractName(advertContractInfo.getCustomerContractName());
		//vAdvContractInfo.setCurrentPageNumber(advertCustomerInfo.getCurrentPageNumber());
		/**
		 * 客户名称查询条件设置
		 */
		//vAdvContractInfo.setCustomerName(customerId);
		vAdvContractInfo.setCustomerId(customerId);
		String isAgentContractString  = request.getParameter("isAgentContract");	//是否代理商合同  2201-代理商  2202-非代理商
		vAdvContractInfo.setIsAgentContract(isAgentContractString);					//设置是否是代理商合同
		vAdvContractInfo.setIsRelationCinemaContract(request.getParameter("isRelationCinemaContract"));//是否关联影城合同
		vAdvContractInfo.setCustomerContractCustomertype(String.valueOf(Dictionary.CHOOSECUSTOMERTYPE_AGENT));//用户选择的是代理商合同
		
		vAdvContractInfo.setCurrentPageNumber(advertContractInfo.getCurrentPageNumber());
		vAdvContractInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		vAdvContractInfo.setPageNavigationURL(CommUtil.getUrl(request));
		vAdvContractInfo.setCustomerContractType(advertContractInfo.getCustomerContractType());
//		advertContractInfo.setPageSize(GlobalConstant.PAGE_SIZE);
//		advertContractInfo.setPageNavigationURL(CommUtil.getUrl(request));
		//advCustomerMap.put("advertContractInfo", advertContractInfo);
		advCustomerMap.put("vAdvContractInfokey", vAdvContractInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		advCustomerMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		String orgCodeValue = JudgementAuthority.getUserInfo(request).getOrgName();
		advCustomerMap.put("orgCodeValue", orgCodeValue);
		// 分页查询
		//Page<AdvertContractInfo, AdvertContract> pageResult = advCustomerService.advertContractInfos(advCustomerMap);
		Page<VAdvContractInfo, VAdvContract> pageResult = advCustomerService.findVAdvertContracts(advCustomerMap);
		List<VAdvContractInfo>  vAdvContractInfosPageResult = advCustomerService.findVAdvContractsNotPage(advCustomerMap);//得到合同表中的所有客户名称
		pageResult.setPageNavigationURL(vAdvContractInfo.getPageNavigationURL());
		Map parames = new HashMap();
		parames.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		// 页面数据显示
		Map modelMap = new HashMap();
		//取出是否外包合同的数据
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.IS_OUT_SOURING_CONTRACT); // 得到是否外包合同
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> dictionaryOutSourcingList = dictionaryService.findDataDictionarys(dictionaryMap);
		modelMap.put("dictionaryOutSourcingList", dictionaryOutSourcingList);
					  
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("flag", flag);
		modelMap.put("advCustomerInfo", advCustomerService.findAdvCustomer(parames));//得到客户列表,用于下拉 废弃掉
		modelMap.put("vAdvContractInfosPageResult", getUniqueCustomer(vAdvContractInfosPageResult)); 	// 2.展示查询条件中的所有客户名称
		modelMap.put("advertContractInfo", advertContractInfo);
		modelMap.put("isAgentContractString", isAgentContractString);
		modelMap.put("isRelationCinemaContract",request.getParameter("isRelationCinemaContract"));
		request.setAttribute("customerId", customerId);
		//初始化广告客户合同的合同类型
		Map customertypeMap = new HashMap();
		DataDictionaryInfo daDictionaryInfo = new DataDictionaryInfo();
		daDictionaryInfo.setFDataId(Dictionary.ADV_CUSTOMER_CONTRACTTYPE); //广告客户合同的合同类型
		customertypeMap.put("dataDictionaryInfoKey", daDictionaryInfo);
		List<DataDictionaryInfo> customerTypeList = dictionaryService.findDataDictionarys1(customertypeMap);
		modelMap.put("customerTypeList", customerTypeList);
		return new ModelAndView(VIEW_REQUEST_MAINTAIN_ADVCONTRACT, modelMap);
	}

	/**
	 * 维护广告客户合同<p>
	 * 根据代理商或者是客户的uuid查询单个合同信息<p>
	 * @author Seven
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView queryAdvContract(HttpServletRequest request, HttpServletResponse response, AdvertContractInfo advertContractInfo) throws Exception {
		String contractId = advertContractInfo.getCustomerContractId();
		List<OrgModel> orgModels = initOrgModels(request);
		//如果用户是从广告合同维护列表页面点击进去的，而不是从选择方案页面点返回
		if(!request.getParameterMap().containsKey("returnSource")){
			Enumeration e=request.getSession().getAttributeNames();
			while(e.hasMoreElements()){
				String attrName=e.nextElement().toString();
				if(attrName.contains("advertContractInfo-")){ 
					request.getSession().removeAttribute(attrName); //清空session中所有的广告合同对象，避免内存泄露
				}
			}
		}
		Map params = new HashMap();
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		//通过合同ID查询合同信息
		AdvertContractInfo aContractInfo = new AdvertContractInfo();
		aContractInfo.setCustomerContractId(contractId);
		params.put("contractIdKey", aContractInfo);
		AdvertContractInfo contractInfo=null;
		if(!request.getParameterMap().containsKey("returnSource")){
			contractInfo= advCustomerService.queryAdvContract(params);
			request.getSession().setAttribute("advertContractInfo-"+contractId, contractInfo);
		}else{
			//用于选择方案后，返回时展现原先录入的数据
			contractInfo=(AdvertContractInfo)request.getSession().getAttribute("advertContractInfo-"+contractId);
		}
		Map modelMap = new HashMap();
		Map parames = new HashMap();
		parames.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		String isAgentContract = request.getParameter("isAgentContract");
		
		/**
		 * 1.如果是代理商的合同需要查询代理商的表信息 
		 * 2.如果是非代理商的合同需要查询客户表信息 
		 */
		if (StringUtils.isNotBlank(isAgentContract)) {
			if (Long.valueOf(isAgentContract) == Dictionary.CHOOSECUSTOMERTYPE_AGENT) {//查询代理商的所有名称
				List<AgentInfo>  agentInfosList = queryAllAdvAgentInfoNames(request,response,new AgentInfo());
				List<AdvertCustomerInfo> acfList = new ArrayList<AdvertCustomerInfo>();
				//将代理商信息 转化为类似客户资料信息的对象
				for (int i = 0; i < agentInfosList.size(); i++) {
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
				modelMap.put("advCustomerInfo",acfList );
			}else if (Long.valueOf(isAgentContract) == Dictionary.CHOOSECUSTOMERTYPE_NOTAGENT) {
				List<AdvertCustomerInfo> agentOrCustomerInfos =	advCustomerService.findAdvCustomer(parames);//得到客户列表,用于下拉 
				modelMap.put("advCustomerInfo",agentOrCustomerInfos );
			}
		}
	    modelMap.put("advertContractInfo", contractInfo);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (contractInfo.getCustomerContractSignTime()!=null) {
			modelMap.put("date1", sdf.format(contractInfo.getCustomerContractSignTime()));
		}
		if (contractInfo.getCustomerContractStartTime()!=null) {
			modelMap.put("date2", sdf.format(contractInfo.getCustomerContractStartTime()));
		}
	    if (contractInfo.getCustomerContractEndTime()!=null) {
			modelMap.put("date3", sdf.format(contractInfo.getCustomerContractEndTime()));
			
		}
		AdvertPlanInfo  advertPlanInfo = contractInfo.getAdvertPlan();
		modelMap.put("plan", advertPlanInfo);
		//取出广告类型的数据
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		//付款方式
		dataDictionaryInfo.setFDataId(Dictionary.CUSTOMER_PAYMENT); 
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> paymentList = dictionaryService.findDataDictionarys(dictionaryMap);
		modelMap.put("paymentList", paymentList);
		//初始化广告客户合同的合同类型
		Map customertypeMap = new HashMap();
		DataDictionaryInfo daDictionaryInfo = new DataDictionaryInfo();
		daDictionaryInfo.setFDataId(Dictionary.ADV_CUSTOMER_CONTRACTTYPE); //广告客户合同的合同类型
		customertypeMap.put("dataDictionaryInfoKey", daDictionaryInfo);
		List<DataDictionaryInfo> customerTypeList = dictionaryService.findDataDictionarys1(customertypeMap);
		modelMap.put("customerTypeList", customerTypeList);
		//初始化选择客户类型
		Map choosecustomerTypeMap = new HashMap();
		DataDictionaryInfo dictionaryInfo = new DataDictionaryInfo();
		dictionaryInfo.setFDataId(Dictionary.CHOOSECUSTOMERTYPE); //选择客户类型
		choosecustomerTypeMap.put("dataDictionaryInfoKey", dictionaryInfo);
		List<DataDictionaryInfo> chooseCustomerTypeList = dictionaryService.findDataDictionarys(choosecustomerTypeMap);
		modelMap.put("chooseCustomerTypeList", chooseCustomerTypeList);
		initRegionAndCityAndCinema(contractInfo,orgModels,modelMap);
		modelMap.put("isModify", request.getParameter("isModify"));
		//初始化广告客户合同的合同类型
	    return new ModelAndView(VIEW_REQUEST_UPDATE_ADVCONTRACT, modelMap);
   }
	
	private void initRegionAndCityAndCinema(AdvertContractInfo contractInfo,List<OrgModel> orgModels,Map modelMap){
		//初始化区域
		Map orgRegionParamsMap = new HashMap();
		orgRegionParamsMap.put(GlobalConstant.USER_CODE_KEY,orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(orgRegionParamsMap);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("orgRegions", orgRegions);
		//初始化城市
		if(!StringUtils.isBlank(contractInfo.getRegionId())){
			Map cityParamsMap = new HashMap();
			cityParamsMap.put(GlobalConstant.USER_CODE_KEY,orgModels);
			OrgRegionInfo orgRegionInfo=new OrgRegionInfo();
			orgRegionInfo.setRegionId(contractInfo.getRegionId());
			cityParamsMap.put("orgRegionInfoKey",orgRegionInfo);
			List<OrgCityInfo> orgCitys=orgCityService.findOrgCitysByRegionId(cityParamsMap);
			modelMap.put("orgCitys", orgCitys);
		}
		//初始化影院
		if(!StringUtils.isBlank(contractInfo.getCityId())){
			Map cinemaParamsMap = new HashMap();
			cinemaParamsMap.put(GlobalConstant.USER_CODE_KEY,orgModels);
			OrgCityInfo orgCityInfo=new OrgCityInfo();
			orgCityInfo.setCityId(contractInfo.getCityId());
			cinemaParamsMap.put("orgCityInfoKey",orgCityInfo);
			List<OrgCinemaInfo> orgCinemas=orgCinemaService.findOrgCinemas(cinemaParamsMap);
			modelMap.put("orgCinemas", orgCinemas);
		}
	}
	
	/**
	 * 查询合同信息,用于执行合同
	 * @author Seven
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView queryDoAdvContractList(HttpServletRequest request, HttpServletResponse response, AdvertContractInfo advertContractInfo)
			throws GenericRuntimeException {
		HttpSession session = request.getSession();
		//得到保存的标志,
		String flag = session.getAttribute("flag")==null?null:(String)session.getAttribute("flag");
		//得到后马上释放
		session.removeAttribute("flag");
		Map advCustomerMap = new HashMap();
		//得到页面customerid
		String customerId= request.getParameter("customerId");
		//装载用于传参的对象
		/*AdvertCustomerInfo advertCustomerInfo= new AdvertCustomerInfo();
		advertCustomerInfo.setCustomerId(customerId);
		advertContractInfo.setAdvertCustomer(advertCustomerInfo);
		advertContractInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		advertContractInfo.setPageNavigationURL(CommUtil.getUrl(request));*/
		//试图 
		VAdvContractInfo vAdvContractInfo = new VAdvContractInfo();
		vAdvContractInfo.setCurrentPageNumber(advertContractInfo.getCurrentPageNumber());
		vAdvContractInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		vAdvContractInfo.setPageNavigationURL(CommUtil.getUrl(request));
		//vAdvContractInfo.setCustomerName(customerId);
		vAdvContractInfo.setCustomerId(customerId);
		advCustomerMap.put("vAdvContractInfokey", vAdvContractInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		advCustomerMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		advCustomerMap.put("advertContractInfo", advertContractInfo); 
		
		String orgCodeValue = JudgementAuthority.getUserInfo(request).getOrgName();
		advCustomerMap.put("orgCodeValue", orgCodeValue);
		// 分页查询
		//Page<AdvertContractInfo, AdvertContract> pageResult = advCustomerService.advertContractInfos(advCustomerMap);
		Page<VAdvContractInfo, VAdvContract> pageResult = advCustomerService.findVAdvertContracts(advCustomerMap);
		List<VAdvContractInfo>  vAdvContractInfosPageResult = advCustomerService.findVAdvContractsNotPage(advCustomerMap);
		pageResult.setPageNavigationURL(vAdvContractInfo.getPageNavigationURL());
		//pageResult.setPageNavigationURL(advertContractInfo.getPageNavigationURL());
		Map parames = new HashMap();
		parames.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		// 页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("vAdvContractInfosPageResult", getUniqueCustomer(vAdvContractInfosPageResult)); // 2.展示查询条件中的所有客户名称
		//modelMap.put("advCustomerInfo", advCustomerService.findAdvCustomer(parames));//得到客户列表,用于下拉
		modelMap.put("flag", flag);
		modelMap.put("advertContractInfo", advertContractInfo);
		request.setAttribute("customerId", customerId);
		//取出是否外包合同的数据
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.IS_OUT_SOURING_CONTRACT); // 得到是否外包合同
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> dictionaryOutSourcingList = dictionaryService.findDataDictionarys(dictionaryMap);
		modelMap.put("dictionaryOutSourcingList", dictionaryOutSourcingList);
		return new ModelAndView(VIEW_REQUEST_DO_ADVCONTRACT, modelMap);
	}
	
	public ModelAndView queryDoContractList(HttpServletRequest request, HttpServletResponse response, AdvertContractInfo advertContractInfo)
	throws GenericRuntimeException {
		HttpSession session = request.getSession();
		//得到保存的标志,
		String flag = session.getAttribute("flag")==null?null:(String)session.getAttribute("flag");
		//得到后马上释放
		session.removeAttribute("flag");
		VAdvContractInfo vAdvContractInfo = new VAdvContractInfo();
		vAdvContractInfo.setCurrentPageNumber(advertContractInfo.getCurrentPageNumber());
		vAdvContractInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		vAdvContractInfo.setPageNavigationURL(CommUtil.getUrl(request));
		String customerId= request.getParameter("customerId");
		//vAdvContractInfo.setCustomerName(customerId);
		vAdvContractInfo.setCustomerId(customerId);
		Map advCustomerMap = new HashMap();
		advCustomerMap.put("vAdvContractInfokey", vAdvContractInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		advCustomerMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		String orgCodeValue = JudgementAuthority.getUserInfo(request).getOrgName();
		advCustomerMap.put("orgCodeValue", orgCodeValue);
		// 分页查询
		Page<VAdvContractInfo, VAdvContract> pageResult = advCustomerService.findVAdvertContracts(advCustomerMap);
		List<VAdvContractInfo>  vAdvContractInfosPageResult = advCustomerService.findVAdvContractsNotPage(advCustomerMap);
		pageResult.setPageNavigationURL(vAdvContractInfo.getPageNavigationURL());
		Map parames = new HashMap();
		parames.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		// 页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("vAdvContractInfosPageResult", getUniqueCustomer(vAdvContractInfosPageResult)); // 2.展示查询条件中的所有客户名称
		//modelMap.put("advCustomerInfo", advCustomerService.findAdvCustomer(parames));//得到客户列表,用于下拉
		modelMap.put("flag", flag);
		request.setAttribute("customerId", customerId);
		//取出是否外包合同的数据
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.IS_OUT_SOURING_CONTRACT); // 得到是否外包合同
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> dictionaryOutSourcingList = dictionaryService.findDataDictionarys(dictionaryMap);
		modelMap.put("dictionaryOutSourcingList", dictionaryOutSourcingList);
		return new ModelAndView(VIEW_REQUEST_DO_ADVCONTRACT, modelMap);
		}
	
	/**
	 * 执行广告合同 列表中详情操作的方法
	 * 生成执行单
	 * @author Seven
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView queryResourceList(HttpServletRequest request,HttpServletResponse response, AdvertPlanInfo advertPlanInfo)
			throws GenericRuntimeException {
		String planId=request.getParameter("planId");
		String content=request.getParameter("content");
		String contractForm=request.getParameter("contractForm");
		String contractType=request.getParameter("contractType");
		String performenceStatus=request.getParameter("performenceStatus");
		String contractId=request.getParameter("contractId");
		//是否到期
		String isUpToTime=request.getParameter("isUpToTime");
		request.setAttribute("isUpToTime", isUpToTime);
		try {
		request.getSession().setAttribute("planId",planId);
			
		//通过planId查询资源
		AdvertPlanInfo aInfo = new AdvertPlanInfo();
		aInfo.setPlanId(planId);
		Map params = new HashMap();
		params.put("aInfoKey", aInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		List<AdvertResourceInfo> resourceInfo = advCustomerService.findAdvertResourcesByPlanId(params);
		//Map<String,String> map = new HashMap<String,String>();
		//List<AdvertResourceInfo> resourceInfo = advCustomerService.findAdvertResourcesByPlanId(planId);
		List<ResourceInfo> list = new  ArrayList<ResourceInfo>();//resourceinfo是为了页面设置的VO
		String cinemaName = "";
		if(Utils.isEmpty(resourceInfo)){
//			HttpSession session = request.getSession();
//			session.setAttribute("flag", "该资源数据关联出现问题,不能进入下一步!");
			
			ResourceInfo sourceInfo = new ResourceInfo();
			List<AdvertResourceInfo> sonList = new ArrayList<AdvertResourceInfo>();
			//对查询出来资源以map中的影院分list储存
//			for (int i = 0; i < resourceInfo.size(); i++) {
//				//如果map中的影院等于list中的影院.则放入一个list
//				if (entry.getValue().equals(resourceInfo.get(i).getAdvertFacility().getOrgCinema().getCinemaId())) {
//					sonList.add(resourceInfo.get(i));
//				}
//			}
			//装载对象
//			sourceInfo.setCinema(entry.getKey());
//			cinemaName = entry.getKey();
			sourceInfo.setContractContent(content);
			sourceInfo.setContractForm(contractForm);
			
			//转换合同类型为显示S_DATE_NAME;
			DataDictionaryInfo  infosd=new DataDictionaryInfo();
			Map maps=new HashMap();
			infosd.setFDataId(Dictionary.ADVERT_TYPE);
			maps.put("dataDictionaryInfoKey",infosd);
			List<DataDictionaryInfo> infoAtlist=dictionaryService.findDataDictionarys(maps);
			if(Utils.isNotEmpty(contractType)){
				for(DataDictionaryInfo info:infoAtlist){
				if(Long.parseLong(contractType)==info.getSDataId()){
					sourceInfo.setContractType(info.getSDataName());
					break;
				}
			  }
			}
			sourceInfo.setContractId(contractId);
//			sourceInfo.setList(sonList);
			//如果已经下发，则需要查询出对应记录的监播证据提供方式
			if("已下发".equals(performenceStatus)){
				Map<String,String> otherParams=new HashMap<String,String>();
				otherParams.put("customerContractId", contractId);
//				AdvertResourceInfo rsi=(AdvertResourceInfo)sonList.get(0); //随便取一条资源记录
//				otherParams.put("resourceId",rsi.getResourceId());
				Page<PerformenceListInfo, PerformenceList> page=performenceListService.getPefermenceList1(otherParams);
				if(!page.getPageEntityItems().isEmpty()){
					PerformenceList performenceList=page.getPageEntityItems().get(0);
					sourceInfo.setDocumentsOfferTypeId(performenceList.getDocumentsOfferType());
					sourceInfo.setPerformenceCode(performenceList.getPerformenceCode());
					sourceInfo.setRemark(performenceList.getRemark());
				}
			}else{
				//生成执行单编号
				long perId=AdvertCodeController.getAdvertCode(AdvPerformenceConstant.ADV_PERFORMENCE_NUMBER_HEAD_PREFIX, AdvPerformenceConstant.ADV_PERFORMENCE_NUMBER_TYPE);
				String finaladvcodeNumber = AdvPerformenceConstant.ADV_PERFORMENCE_NUMBER_HEAD_PREFIX + "-" + NumberFormatUtil.advAgentNumberFormat(perId);
				sourceInfo.setPerformenceCode(finaladvcodeNumber);
			}
			list.add(sourceInfo);
			
//			return queryDoAdvContractList(request,response,new AdvertContractInfo());
		}else{
		//取出广告设施类型的数据
			Map dictionaryMap = new HashMap();
			DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
			dataDictionaryInfo.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE); // 得到广告设施类型
			dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
			List<DataDictionaryInfo> dictionaryAdvTypeList = dictionaryService.findDataDictionarys(dictionaryMap);
			//设置广告类型名称
			for(AdvertResourceInfo otherResourceInfo:resourceInfo){
				for(DataDictionaryInfo otherDataDictionaryInfo:dictionaryAdvTypeList){
					if(otherDataDictionaryInfo.getSDataId()==otherResourceInfo.getAdvertFacility().getFacilityType()){
						otherResourceInfo.getAdvertFacility().setFacilityTypeName(otherDataDictionaryInfo.getSDataName());
						break;
					}
				}
			}
			Map<String,String> map = new HashMap<String,String>();
		
			//对资源所属的影院去重
			for (int i = 0; i < resourceInfo.size(); i++) {
				map.put(resourceInfo.get(i).getAdvertFacility().getOrgRegion().getRegionName()+"-"+resourceInfo.get(i).getAdvertFacility().getOrgCity().getCityName()+"-"+resourceInfo.get(i).getAdvertFacility().getOrgCinema().getCinemaName(), resourceInfo.get(i).getAdvertFacility().getOrgCinema().getCinemaId());
			}
			//按影院分批资源
	//		List<ResourceInfo> list = new  ArrayList<ResourceInfo>();//resourceinfo是为了页面设置的VO
			
			//循环MAP,拿出来的数据
			for(Map.Entry<String, String> entry: map.entrySet()) {
				ResourceInfo sourceInfo = new ResourceInfo();
				List<AdvertResourceInfo> sonList = new ArrayList<AdvertResourceInfo>();
				//对查询出来资源以map中的影院分list储存
				for (int i = 0; i < resourceInfo.size(); i++) {
					//如果map中的影院等于list中的影院.则放入一个list
					if (entry.getValue().equals(resourceInfo.get(i).getAdvertFacility().getOrgCinema().getCinemaId())) {
						sonList.add(resourceInfo.get(i));
					}
				}
				//装载对象
				sourceInfo.setCinema(entry.getKey());
				cinemaName = entry.getKey();
				sourceInfo.setContractContent(content);
				sourceInfo.setContractForm(contractForm);
				
				//转换合同类型为显示S_DATE_NAME;
				DataDictionaryInfo  infosd=new DataDictionaryInfo();
				Map maps=new HashMap();
				infosd.setFDataId(Dictionary.ADVERT_TYPE);
				maps.put("dataDictionaryInfoKey",infosd);
				List<DataDictionaryInfo> infoAtlist=dictionaryService.findDataDictionarys(maps);
				if(Utils.isNotEmpty(contractType)){
					for(DataDictionaryInfo info:infoAtlist){
					if(Long.parseLong(contractType)==info.getSDataId()){
						sourceInfo.setContractType(info.getSDataName());
						break;
					}
				  }
				}
				sourceInfo.setContractId(contractId);
				sourceInfo.setList(sonList);
				//如果已经下发，则需要查询出对应记录的监播证据提供方式
				if("已下发".equals(performenceStatus)){
					Map<String,String> otherParams=new HashMap<String,String>();
					otherParams.put("customerContractId", contractId);
					AdvertResourceInfo rsi=(AdvertResourceInfo)sonList.get(0); //随便取一条资源记录
					otherParams.put("resourceId",rsi.getResourceId());
					Page<PerformenceListInfo, PerformenceList> page=performenceListService.getPefermenceList(otherParams);
					if(!page.getPageEntityItems().isEmpty()){
						PerformenceList performenceList=page.getPageEntityItems().get(0);
						sourceInfo.setDocumentsOfferTypeId(performenceList.getDocumentsOfferType());
						sourceInfo.setPerformenceCode(performenceList.getPerformenceCode());
						sourceInfo.setRemark(performenceList.getRemark());
					}
				}else{
					//生成执行单编号
					long perId=AdvertCodeController.getAdvertCode(AdvPerformenceConstant.ADV_PERFORMENCE_NUMBER_HEAD_PREFIX, AdvPerformenceConstant.ADV_PERFORMENCE_NUMBER_TYPE);
					String finaladvcodeNumber = AdvPerformenceConstant.ADV_PERFORMENCE_NUMBER_HEAD_PREFIX + "-" + NumberFormatUtil.advAgentNumberFormat(perId);
					sourceInfo.setPerformenceCode(finaladvcodeNumber);
				}
				list.add(sourceInfo);
			} 
		}
		// 页面数据显示
		String code = "";
		if(Utils.isNotEmpty(list.get(0).getList())){
			code = list.get(0).getList().get(0).getAdvertFacility().getFacilityCode();
		}
		 
		Map modelMap = new HashMap();
		modelMap.put("lists", list);
		modelMap.put("code", code);
		modelMap.put("performenceStatus", performenceStatus);
		//取出监播证据提供方式的数据
		Map documentsOfferTypeMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfoOther = new DataDictionaryInfo();
		dataDictionaryInfoOther.setFDataId(Dictionary.DOCUMENTS_OFFER_TYPE); // 得到监播证据提供方式
		documentsOfferTypeMap.put("dataDictionaryInfoKey", dataDictionaryInfoOther);
		List<DataDictionaryInfo> documentsOfferTypeList = dictionaryService.findDataDictionarys(documentsOfferTypeMap);
		modelMap.put("documentsOfferTypeList", documentsOfferTypeList);
		 HttpSession session = request.getSession();
		 session.setAttribute("resourceList", list);
		 /**
		  * for log4j
		  */
		 String PerformenceCode = "";//执行单编号
		 if (!list.isEmpty()) {
			 PerformenceCode = list.get(0).getPerformenceCode();
		 }
		 String logsString = "[ AdverContent="+content+",AdvertType="+contractType+
		 ",AdvertStyle="+contractForm+
			",cinemaName="+cinemaName+",documentsOfferType="+Dictionary.DOCUMENTS_OFFER_TYPE+",PerformenceCode="+PerformenceCode+"  ]"; 
			CommonWriteLog4j.writeLog4j(OperLogService.getInstance(), orgModels, BUSINESS_TYPE.WRITE, AdvCustomerLogicImpl.class, LOG_TYPE.OPER_NORMAL, 
					logsString, null);
		 
		return new ModelAndView(VIEW_REQUEST_PRE_PERFORMANCE, modelMap);
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession session = request.getSession();
			session.setAttribute("flag", "该数据关联异常,不能进入下一步!");
			return queryDoAdvContractList(request,response,new AdvertContractInfo());
		}
	}
	
	/**
	 * <p>执行广告合同模块-合同列表中-详情操作-下发执行单对应的方法</p>
	 * 保存执行单
	 * @author seven
	 * @param request
	 * @param response
	 * @param advertPlanInfote
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView doPerformance(HttpServletRequest request,HttpServletResponse response, AdvertPlanInfo advertPlanInfo) throws GenericRuntimeException {
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		//从页面直接拿的值
		String contractId=request.getParameter("contractId");
		 String[] docoff=request.getParameterValues("docoff");
		 Map modelMap = new HashMap();
		 HttpSession session = request.getSession();
		 String planid=(String) session.getAttribute("planId");
		 boolean flag=false;
		 //拿resourceList,拿完后,释放(此list可在queryResourceList方法中找到)
		 List<ResourceInfo> list=(List<ResourceInfo>)session.getAttribute("resourceList");
		 session.removeAttribute("resourceList");
		 Date date=new java.util.Date();
		 //这一层循环拿出List<ResourceInfo>
		 for (int i = 0; i < list.size(); i++) {
			 
			 List<AdvertResourceInfo> list2 = list.get(i).getList();
			 //此层循环拿出RESOURCEINFO
			 if(Utils.isNotEmpty(list2)){
				 for (int j = 0; j < list2.size(); j++) {
				 //remark为页面循环中的textArea控件
				 String remark="remark";
				 //在获取值时,也进行循环得到id
				 remark=remark+(i+1);
				 //装载对象 
				 PerformenceList performenceList = new PerformenceList();
				 performenceList.setRemark(request.getParameter(remark));
				 AdvertResource resource = new AdvertResource();
				 resource.setResourceId(list2.get(j).getResourceId());
				 String code=list2.get(j).getAdvertFacility().getOrgCinema().getCinemaCode();
				 performenceList.setOrgCode(code);
				 performenceList.setAdvertResource(resource);
				 AdvertContract advertContract = new AdvertContract();
				 advertContract.setCustomerContractId(contractId);
				 performenceList.setAdvertContract(advertContract);
				 assert(!StringUtils.isBlank(docoff[i]));
				 performenceList.setDocumentsOfferType(Long.parseLong(docoff[i]));
				 performenceList.setCreateDate(date);
				 performenceList.setCreateUserId(orgModels.get(0).getName());
				//remark为页面循环中的hidden控件
				 String performenceCode="performenceCode";
				//在获取值时,也进行循环得到值
				 performenceCode=performenceCode+(i+1);
				 performenceList.setPerformenceCode(request.getParameter(performenceCode));
				 performenceList.setPerformenceId(null);
				 //保存对象
				 Map map=new HashMap();
				 map.put("performenceListkey", performenceList);
				 map.put(GlobalConstant.USER_CODE_KEY, orgModels);
				 flag =advCustomerService.savePerformance(map);
				 }
			 }else{
				 String remark="remark";
				 //在获取值时,也进行循环得到id
				 remark=remark+(i+1);
				 //装载对象 
				 PerformenceList performenceList = new PerformenceList();
				 performenceList.setRemark(request.getParameter(remark));
//				 AdvertResource resource = new AdvertResource();
//				 resource.setResourceId(list2.get(j).getResourceId());
//				 String code=list2.get(j).getAdvertFacility().getOrgCinema().getCinemaCode();
//				 performenceList.setOrgCode(code);
//				 performenceList.setAdvertResource(resource);
				 AdvertContract advertContract = new AdvertContract();
				 advertContract.setCustomerContractId(contractId);
				 performenceList.setAdvertContract(advertContract);
				 assert(!StringUtils.isBlank(docoff[i]));
				 performenceList.setDocumentsOfferType(Long.parseLong(docoff[i]));
				 performenceList.setCreateDate(date);
				 performenceList.setCreateUserId(orgModels.get(0).getName());
				//remark为页面循环中的hidden控件
				 String performenceCode="performenceCode";
				//在获取值时,也进行循环得到值
				 performenceCode=performenceCode+(i+1);
				 performenceList.setPerformenceCode(request.getParameter(performenceCode));
				 performenceList.setPerformenceId(null);
				 //保存对象
				 Map map=new HashMap();
				 map.put("performenceListkey", performenceList);
				 map.put(GlobalConstant.USER_CODE_KEY, orgModels);
				 flag =advCustomerService.savePerformance(map);
			 }
		}
		 if (flag) {
				boolean res=false;
				boolean res2=false;
				//yong_jiang 加入一下代码 ，下发参数后更新T_ADVERT_RESOURCE_PLAN中间表资源的使用状态;
				res = updateRelationSourceStatus(request,planid, res);
				
				res2 = updateContractStatus(contractId,res2,orgModels);
				
				if(res && res2)
				//modelMap.put("flag", "成功");	
				session.setAttribute("flag", "执行单下发成功");
				return new ModelAndView(new RedirectView("queryDoAdvContractList.do",true));
			}else { 
				modelMap.put("flag", "失败");
				return new ModelAndView(VIEW_FLAG, modelMap);
			}
	}
	
	
/**
 * @Description: TODO(更新广告合同对应的执行单状态-新建的时候状态为'未下发',更新的时候需要更新为'已下发'的状态)
 * @Title:       updateContractStatus 
 * @param 		 @param contractId
 * @param 		 @param res2
 * @param 		 @param listmdel
 * @param 		 @return
 * @return       boolean
 * @author       heng_gang  
 * @date         2012-5-10 上午11:15:31
 * @throws
 */
private boolean updateContractStatus(String contractId, boolean res2, List<OrgModel> listmdel) {
	Map params = new HashMap();
	params.put(GlobalConstant.USER_CODE_KEY, listmdel);
	AdvertContractInfo aInfo = new AdvertContractInfo();
	aInfo.setCustomerContractId(contractId);
	params.put("contractIdKey", aInfo);
	params.put(GlobalConstant.USER_CODE_KEY, listmdel);
	AdvertContractInfo info = advCustomerService.queryAdvContract(params);
	Map map = new HashMap();
	map.put("advertContractInfokey", info);
	//获取用户权限集合
	map.put(GlobalConstant.USER_CODE_KEY, listmdel);
	return advCustomerService.updateContractStatus(map);
}
	
	/**
	 *  更新资源方案表的状态
	 * @param planid
	 * @param res
	 * @return
	 */
	private boolean updateRelationSourceStatus(HttpServletRequest request,String planid, boolean res) {
		if(Utils.isNotEmpty(planid)){
			List<OrgModel> orgModels = initOrgModels(request);
			Map params = new HashMap();
			AdvertResourcePlanInfo planinfo = new AdvertResourcePlanInfo();
			planinfo.setPlanId(planid);
			params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
			params.put("planinfoKEY", planinfo);
			List<AdvertResourcePlanInfo> listPlaninfo = advResourcePlanService.findAdvResourcePlanRelationByPlanId(params);
			res = advResourcePlanService.updateAdvertResourcePlanInfoList(listPlaninfo,orgModels);
		}
		return res;
	}
	
	
	/**
	 * 下发映前执行单跳转
	 * @param request
	 * @param response
	 * @param advertPlanInfo
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView requestBeforePerformance(HttpServletRequest request,HttpServletResponse response, AdvertPlanInfo advertPlanInfo)
			throws GenericRuntimeException {
		Map modelMap = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		modelMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(modelMap);//初始化下拉框的值
		modelMap.put("orgRegions", orgRegions);
		return new ModelAndView(VIEW_REQUEST_GEN_PERFORMANCE, modelMap);
	}
	
	/**
	 * 保存映前执行单
	 * @param request
	 * @param response
	 * @param advertPlanInfo
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView requestSaveBeforePerformance(HttpServletRequest request,HttpServletResponse response, AdvertPlanInfo advertPlanInfo)
			throws GenericRuntimeException {
		//uid为页面中,添加的tr个数
		int uid = Integer.parseInt(request.getParameter("uid"));
		List<OrgModel> orgModels = initOrgModels(request);

		//拿到remark值
		String remark=request.getParameter("remark");
		//上传控件拿到文件路径
		String fileUrl=request.getParameter("floorPlanFileUploadHidden");
		boolean flag=false;
		for (int i = 1; i <= uid; i++) {
			//循环拿到影院的值
			String cinema=request.getParameter("cinemaId"+i);
			//如果这一列被删除,则执行下一列
			if (cinema==null||"".equals(cinema)) {
				continue;
			}
			String city=request.getParameter("cityId"+i);
			//通过影院和城市查询资源
			AdvertResource aResource = advCustomerService.findAdvertResourcesByCityCinema(cinema,city,orgModels);
			
			
			 PerformenceList performenceList = new PerformenceList();
			 performenceList.setRemark(remark);
			 AdvertResource resource = new AdvertResource();
			 resource.setResourceId(aResource.getResourceId());
			 AdvertFacility facility = new AdvertFacility();
			 facility.setFacilityId(aResource.getAdvertFacility().getFacilityId());
			 resource.setAdvertFacility(facility);
			 performenceList.setAdvertResource(resource);
			 AdvertContract advertContract = new AdvertContract();
			 //现在写死,此为虚拟合同
			 advertContract.setCustomerContractId("4028908c36a5f4120136a5fcc1a60007");
			 performenceList.setAdvertContract(advertContract);
			 performenceList.setDocumentsUrl(fileUrl);
			 if (fileUrl!=null&&!"".equals(fileUrl)) {
				performenceList.setDocumentsStatus("已上传");
			}
			 performenceList.setCreateDate(new java.util.Date());
			 performenceList.setCreateUserId(orgModels.get(0).getName());
			 performenceList.setPerformenceCode(AdvertCodeController.getAdvertCode(AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX, AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_TYPE)+"");
			 performenceList.setPerformenceId(null);
			 Map map=new HashMap();
			 
			 map.put("performenceListkey", performenceList);
			 map.put(GlobalConstant.USER_CODE_KEY, orgModels);
			 flag = advCustomerService.savePerformance(map);
			 
		}
		Map modelMap = new HashMap();
		 if (flag) {
				modelMap.put("flag", "成功");
				return new ModelAndView(VIEW_REQUEST_GEN_PERFORMANCE, modelMap);
			}else {
				modelMap.put("flag", "失败");
				return new ModelAndView(VIEW_FLAG, modelMap);
			}
	}
	
	/**
	 * 文件上传 (支持格式不做限制)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView fileUpload(HttpServletRequest request,HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		json.put("success", Constant.FILE_UPLOAD_FAIL);
		//文件上传通用方法
		String result = FileUpLoadUtil.fileUpload(request, FileUpLoadUtil.DOCUMENT_FILE);
		json.put("url", result);
		json.put("success", Constant.FILE_UPLOAD_SUCCESS);
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 文件上传 (只能上传PDF的文件)
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView pdfFileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		json.put("success",Constant.FILE_UPLOAD_FAIL);
		//文件上传通用方法
		String result = FileUpLoadUtil.fileUpload(request, FileUpLoadUtil.AGENT_CONTRACT_IMAGE);
		//文件太大,上传失败
		if(result.equals(FileUpLoadUtil.DOCUMENT_IMAGE_SIZE_RESULT)){
			json.put("success",Constant.FILE_UPLOAD_TOO_LARGES5);
			//文件不为预期的文件如PDF
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
	 * 查看方案包括的资源
	 * @author Seven
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws GenericRuntimeException
	 */
	public ModelAndView queryResourceListForPreview(HttpServletRequest request,HttpServletResponse response, AdvertPlanInfo advertPlanInfo)
			throws GenericRuntimeException {
		String planId = request.getParameter("planId");
		String planName = request.getParameter("planName");
		//通过planId查询资源
		Map params = new HashMap();
		AdvertPlanInfo aPlanInfo = new AdvertPlanInfo();
		aPlanInfo.setPlanId(planId);
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		params.put("aInfoKey", aPlanInfo);
		List<AdvertResourceInfo> resourceInfo = advCustomerService.findAdvertResourcesByPlanId(params);
		params.clear();
		params.put("advertPlanKey", aPlanInfo);
		//通过方案ID得到方案名称和编号
		aPlanInfo=advResourcePlanService.findAdvertPlanInfo(params);
		// 页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("resourceInfo", resourceInfo);
		modelMap.put("planId", planId);
		modelMap.put("planCode", aPlanInfo.getPlanCode());
		modelMap.put("planName", aPlanInfo.getPlanName());
		return new ModelAndView(VIEW_REQUEST_PRE_RESOURCE, modelMap);
	}
	
	
	
	/**
	 * @Description: TODO(查询所有代理商信息-新增广告客户合同时候使用的方法 )
	 * @Title:       queryAllAdvAgentInfo  
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertPlanInfo
	 * @param 		 @return
	 * @param 		 @throws GenericRuntimeException
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-5-11 下午02:43:59
	 * @throws		 Exception
	 */
	public ModelAndView queryAllAdvAgentInfo(HttpServletRequest request,HttpServletResponse response, AgentInfo agentInfo)throws Exception {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		List<AgentContract> agentContractList=advAgentContractService.findAdvAgentContractInfoByTime();
		List<AgentInfo> agList = new ArrayList<AgentInfo>();
		for (int i = 0; i < agentContractList.size(); i++) {
			AgentInfo ag = new AgentInfo();
			ag.setAgentId(agentContractList.get(i).getAgent().getAgentId());
			ag.setAgentName(agentContractList.get(i).getAgent().getAgentName());
			agList.add(ag);
		}
		/*List<AgentInfo>  agentInfosList = advAgentService.queryList(params);//需要配置spring的配置bean文件  
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
		}*/
		IOUtil.writeJSONArray(JSONArray.fromObject(agList), response.getOutputStream());
		return null; 
	}
	
	/**
	 * 删除上传的图片，文件
	 * */
	public void delAttachment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String filePath = request.getSession().getServletContext().getRealPath("/")+request.getParameter("filePath");
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		JSONObject json = new JSONObject();
		json.put("success", "success1");
		IOUtil.writeJSON(json, response.getOutputStream());
	}
	
	public List<AgentInfo> queryAllAdvAgentInfoNames(HttpServletRequest request,HttpServletResponse response, AgentInfo agentInfo)throws Exception {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
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
	
	/**
	 * @Description: TODO(查询所有的客户资料信息-新增广告客户合同时候使用的方法)
	 * @Title:       queryAllAdvCustomerInfo 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertCustomerInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-5-11 下午04:40:59
	 * @throws
	 */
	public ModelAndView  queryAllAdvCustomerInfo(HttpServletRequest request,HttpServletResponse response, AdvertCustomerInfo advertCustomerInfo)throws Exception {
		List<OrgModel> orgModels = initOrgModels(request);
		Map parames = new HashMap();
		parames.put(GlobalConstant.USER_CODE_KEY, orgModels);//数据权限
		List<AdvertCustomerInfo>  advertCustomerInfos  = advCustomerService.findAdvCustomer(parames);//得到客户列表,用于下拉
		List<AdvertCustomerInfo> advList = new ArrayList<AdvertCustomerInfo>();
		for (int i = 0; i < advertCustomerInfos.size(); i++) {
			AdvertCustomerInfo ac = new AdvertCustomerInfo();
			ac.setCustomerId(advertCustomerInfos.get(i).getCustomerId());
			ac.setCustomerName(advertCustomerInfos.get(i).getCustomerName());
			ac.setCustomerCode(advertCustomerInfos.get(i).getCustomerCode());
			advList.add(ac);
		}
		IOUtil.writeJSONArray(JSONArray.fromObject(advList), response.getOutputStream());
		return null; 
	}
	
	/**
	 * @Description: TODO(根据代理商的uuid查询出代理商的相关信息-甲方地址,电话,传真)
	 * @Title:       queryAdvAgentInfoByAgentId 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param agentInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-5-11 下午05:27:00
	 * @throws
	 */
	public ModelAndView queryAdvAgentInfoByAgentId(HttpServletRequest request,HttpServletResponse response, AgentInfo agentInfo)throws Exception {
		Map modelMap = new HashMap();
		AgentInfo ai = new AgentInfo();
		ai.setAgentId(request.getParameter("customerId"));
		modelMap.put("keyAdvAgentInfo", ai);
		AgentInfo agentInfoo = advAgentService.findAdvAgentInfoByAgentId(modelMap);
		AgentInfo ag = new AgentInfo();
		ag.setAgentAddress(agentInfoo.getAgentAddress());
		ag.setAgentTel(agentInfoo.getAgentTel());
		ag.setAgentFax(agentInfoo.getAgentFax());
		ag.setContactName(agentInfoo.getContactName());
		ag.setAgentCode(agentInfoo.getAgentCode());
		ag.setAgentId(agentInfoo.getAgentId());
		ag.setAgentName(agentInfoo.getAgentName());
		//ag.setAgentAccount(agentInfoo.getAgentAccount());
		//ag.setAgentBank(agentInfoo.getAgentBank());
		//ag.setAgentLevel(agentInfoo.getAgentLevel());
		IOUtil.writeJSONArray(JSONArray.fromObject(ag), response.getOutputStream());
		return null; 
	}
	
	
	/**
	 * 用于异步加载客户信息<br/>
	 * 下拉框值改变时.加载新数据
	 * @param request
	 * @param response
	 * @param advertContractInfo
	 * @return
	 * @throws GenericRuntimeException
	 * @throws IOException 
	 */
	public ModelAndView queryAdvCustomerInfoByCustomerId(HttpServletRequest request,HttpServletResponse response, AdvertCustomerInfo advertCustomerInfo) throws GenericRuntimeException, IOException {
		String customerId = advertCustomerInfo.getCustomerId();
		AdvertCustomerInfo customerInfot = new AdvertCustomerInfo();
		customerInfot.setCustomerId(customerId);
		Map params = new HashMap();
		params.put("customerInfotKey", customerInfot);
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		//通过id查询客户信息
		AdvertCustomerInfo CustomerInfo = advCustomerService.findAdvCustomerById(params);
		AdvertCustomerInfo ac = new AdvertCustomerInfo();
		ac.setCustomerAddress(CustomerInfo.getCustomerAddress());
		ac.setCustomerTel(CustomerInfo.getCustomerTel());
		ac.setCustomerFax(CustomerInfo.getCustomerFax());
		ac.setContactName(CustomerInfo.getContactName());
		IOUtil.writeJSONArray(JSONArray.fromObject(ac), response.getOutputStream());
		return null;
	}
	
	/**
	 * 自动获合同编码
	 * @param list
	 * @author milihua
	 */
	public String getContractCode(List<VAdvContractInfo> advertResourceInfos,String type){
		//合同编号自动生成JY-MEDIA【A】-20120000
		StringBuffer code =new StringBuffer();
		code.append("JY-MEDIA【");
		if("2103".equals(type)){
			code.append("A");
		}else if("2104".equals(type)){
			code.append("B");
		}else if("2105".equals(type)){
			code.append("C");
		}else if("2106".equals(type)){
			code.append("D");
		}else if("2107".equals(type)){
			code.append("F");
		}else if("2108".equals(type)){
			code.append("B");
		}
		code.append("】-");
		//得到年
		Calendar cal=Calendar.getInstance();   
		String year=cal.get(Calendar.YEAR)+"";
		code.append(year);
	    String code1="0";
		if(Utils.isNotEmpty(advertResourceInfos)){
			for(int i = 0;i<advertResourceInfos.size();i++){
				VAdvContractInfo info = advertResourceInfos.get(i);
				if(info.getCustomerContractCode().contains("-")){
					String[] resourceCodes1 = info.getCustomerContractCode().split("-");
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
	
	public  Date StringTodate(String dataStr){		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			return df.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}	
}