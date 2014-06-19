package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.kd.ais.common.CommonConstant.BUSINESS_TYPE;
import com.kd.ais.common.CommonConstant.LOG_TYPE;
import com.kd.ais.common.CommonWriteLog4j;
import com.kd.ais.log.service.OperLogService;
import com.kd.ais.security.entity.UserTab;
import com.kingdee.ais.core.constant.GlobalConstant;
import com.kingdee.ais.core.entity.OrgModel;
import com.kingdee.ais.core.exception.GenericRuntimeException;
import com.kingdee.ais.core.pagination.Page;
import com.kingdee.ais.core.util.CommUtil;
import com.kingdee.ais.core.util.DateUtil;
import com.kingdee.ais.core.util.DozerMapperSingleton;
import com.kingdee.ais.core.util.Utils;
import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.StringUtil;
import com.kingdee.ais.ibmp.business.advert.logic.impl.PerformenceListLogicImpl;
import com.kingdee.ais.ibmp.business.advert.service.IAdvCustomerService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvResourcePlanService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvertFacilityService;
import com.kingdee.ais.ibmp.business.advert.service.IAvertDiscountSerice;
import com.kingdee.ais.ibmp.business.advert.service.PerformenceListService;
import com.kingdee.ais.ibmp.business.dictionary.Dictionary;
import com.kingdee.ais.ibmp.business.dictionary.service.IAdvertIconService;
import com.kingdee.ais.ibmp.business.dictionary.service.IDictionaryService;
import com.kingdee.ais.ibmp.business.org.service.IOrgCinemaService;
import com.kingdee.ais.ibmp.business.org.service.IOrgCityService;
import com.kingdee.ais.ibmp.business.org.service.IOrgRegionService;
import com.kingdee.ais.ibmp.model.AlertSourceParamBean;
import com.kingdee.ais.ibmp.model.PerformenceParamBean;
import com.kingdee.ais.ibmp.model.bean.AdvertResourceBean;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertContract;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertDiscount;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertPlan;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertResource;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertContractInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertDateInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertDiscountInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertFacilityInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertPlanInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertResourceInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertResourcePlanInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertResourcePlanTempInfo;
import com.kingdee.ais.ibmp.model.vo.advert.FloorPlanInfo;
import com.kingdee.ais.ibmp.model.vo.advert.PerformenceListInfo;
import com.kingdee.ais.ibmp.model.vo.dictionary.AdvertIconInfo;
import com.kingdee.ais.ibmp.model.vo.dictionary.DataDictionaryInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCinemaInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCityInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgRegionInfo;
import com.kingdee.ais.ibmp.view.common.JudgementAuthority;
import com.kingdee.ais.ibmp.view.constant.AdvResourcePlanConstant;
import com.kingdee.ais.ibmp.view.constant.Constant;
import com.kingdee.ais.ibmp.view.util.FileUpLoadUtil;
import com.kingdee.ais.ibmp.view.util.IOUtil;
import com.kingdee.ais.ibmp.view.util.NumberFormatUtil;
import com.opensymphony.xwork2.ActionContext;

 
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdvInputController extends MultiActionControllerImpl {

	private static final Logger logger = Logger.getLogger(AdvInputController.class);

	private IDictionaryService dictionaryService;
	private IAdvertFacilityService advertFacilityService;
	private IOrgRegionService orgRegionService;
	IAdvResourcePlanService advResourcePlanService = null; 
	private IAvertDiscountSerice avertDiscountService;
	public IAvertDiscountSerice getAvertDiscountService() {
		return avertDiscountService;
	}
	public void setAvertDiscountService(IAvertDiscountSerice avertDiscountService) {
		this.avertDiscountService = avertDiscountService;
	}

	private PerformenceListService performenceListService;
	private IAdvertIconService advertIconService;
	private IAdvCustomerService advCustomerService;
	private IOrgCityService orgCityService;
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
	public IOrgRegionService getOrgRegionService() {
		return orgRegionService;
	}

	private IOrgCinemaService orgCinemaService;
	
	public void setAdvCustomerService(IAdvCustomerService advCustomerService) {
		this.advCustomerService = advCustomerService;
	}
	public PerformenceListService getPerformenceListService() {
		return performenceListService;
	}
	public void setPerformenceListService(
			PerformenceListService performenceListService) {
		this.performenceListService = performenceListService;
	}
	public IAdvResourcePlanService getAdvResourcePlanService() {
		return advResourcePlanService;
	}
	public void setAdvResourcePlanService(
			IAdvResourcePlanService advResourcePlanService) {
		this.advResourcePlanService = advResourcePlanService;
	}

	public void setAdvertFacilityService(
		IAdvertFacilityService advertFacilityService) {
	this.advertFacilityService = advertFacilityService;
	}
	
	public void setDictionaryService(IDictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public void setOrgRegionService(IOrgRegionService orgRegionService) {
		this.orgRegionService = orgRegionService;
	}

	public void setAdvertIconService(IAdvertIconService advertIconService) {
		this.advertIconService = advertIconService;
	}

	


	//创建广告资源方案(列表和图片两种方式)
	public static final String VIEW_REQUEST_CREATEADVPLANTAB_SUCCESS = "view_request_createadvplantab_success";//请求创建广告资源方案tab页面
	public static final String VIEW_REQUEST_CREATEADVPLANLIST_SUCCESS = "view_request_createadvplanlist_success";//请求创建广告资源方案-列表页面
	public static final String VIEW_REQUEST_CREATEADVPLANPIC_SUCCESS = "view_request_createadvplanpic_success";  //请求创建广告资源方案-图片页面
	public static final String VIEW_REQUEST_QUERYADVALLRESOURCEBYFACILITYID_SUCCESS = "view_request_queryadvallresourcebyfacilityid_success";//请求列表方式创建广告资源方案-iframe页面
	public static final String VIEW_REQUEST_QUERYADVALLRESOURCEBYFACILITYID_FORMAINTAIN_SUCCESS = "view_request_queryadvallresourcebyfacilityid_formaintain_success";//请求列表方式维护广告资源方案-iframe页面
	
	public static final String VIEW_FINDPRINTPAGE_SUCCESS ="AdvInputController_findPrintPage_success";//打印页面
	
	public static final String VIEW_REQUEST_QUERYADVPLANINFO_SUCCESS = "view_resourdate_set";//资源时间设置页面
			 
	//维护广告资源方案
	public static final String VIEW_QUERY_ADVRESOURCEPLANINFO_LIST_SUCCESS = "view_query_advresourceplaninfo_list_success";//请求维护广告资源方案-列表页面
	//维护广告资源方案详情
	public static final String VIEW_QUERY_ADVRESOURCEPLANINFO_DETAIL_SUCCESS = "AdvInputController_queryAdvResourcePlanInfoDetail_success";
	//维护广告资源方案-方案详情页面-tab页面-列表方案详情页面(方案列表页面包括资源列表的iframe_resource,资源列表的iframe又包括资源使用日期的iframe_resourceUseDate)
	public static final String VIEW_QUERYADVRESOURCEPLANDETAILBYPLANID_SUCCESS = "view_queryadvresourceplandetailbyplanid_success";
	//维护广告资源方案-列表方式维护方案-资源使用日期的列表页面
	public static final String VIEW_MAINTAIN_ADVPLAN_RESOURCEUASDATE_SUCCESS = "view_maintain_advplan_resourceuasdate_success";
	
	
	//进入图片维护页面
	public static final String VIEW_QUERY_ADVRESOURCEPLANINFO_DETAIL_BY_PIC_SUCCESS ="AdvInputController_showUpdateAdvertResourcePlanDetailByPic_success";
	
	//查询广告位
	public static final String VIEW_ADVINPUT_SHOW_ADVERTFACILITY_SUCCESS = "AdvInputController_showFindAdvertFacility_success";
	
	// 查询确认单
	public static final String VIEW_REQUEST_ENTERORDERSLIST_SUCCESS = "views_request_enterorderslist_success";
	public static final String VIEW_REQUEST_ENTERORDERSLIST_HASPERIED__SUCCESS = "views_request_enterorderslistdetail_hasperied_success";
	public static final String VIEW_REQUEST_ENTERORDERSLISTDETAIL__SUCCESS = "views_request_enterorderslistdetail_success";
	//新增广告资源方案-得到广告位使用情况列表页面
	public static final String VIEW_ADD_ADVPLAN_RESOURCEUASDATE_SUCCESS = "view_add_advplan_resourceuasdate_success";
	
	//资源锁定时间设置页面
	public static final  String VIEW_RESOURDATE_SET_SUCCESS = "view_resourdate_set";
	
	//广告资源方案审批页面
	public static final  String VIEW_QUERY_ADVERTRESOURVEPLANSP_SUCCESS = "view_advert_resourceplan";
	
	//广告资源方案审批详情页面
	public static final  String VIEW_ADVERTRESOURVEPLANSP_DETAILS = "view_advert_resourceplan_detail";
	
	/**
	 * <p> 左边菜单请求跳转到创建广告资源方案-Tab页面方法
	 * @param request   current HTTP request
	 * @param response  current HTTP response
	 * @throws Exception
	 */
	public ModelAndView  requestCreateAdvPlanTab(HttpServletRequest request,HttpServletResponse response) throws GenericRuntimeException{
		return new ModelAndView(VIEW_REQUEST_CREATEADVPLANTAB_SUCCESS); 
	}
	
	/**
	 * <p>请求-列表方式创建广告资源方案的页面
	 * @param request   current HTTP request
	 * @param response  current HTTP response
	 * @throws Exception
	 */
	public ModelAndView  requestCreateAdvPlanList(HttpServletRequest request,HttpServletResponse response,AdvertPlanInfo advertPlanInfo ) throws GenericRuntimeException{
		Map advplanlistMap = new HashMap();
		Map advfacilityTypeMap = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		advfacilityTypeMap.put(GlobalConstant.USER_CODE_KEY,orgModels);
		//得到广告资源方案编号
		long  advResourcePlanNumber = AdvertCodeController.getAdvertCode(AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX, AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_TYPE);
		String finaladvResourcePlancodeNumber = AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX + "-" + NumberFormatUtil.advAgentNumberFormat(advResourcePlanNumber);
		//初始化区域,城市,影院名称
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(advfacilityTypeMap);
		
		BigDecimal discount = advResourcePlanService.findDiscountByName(advfacilityTypeMap);
		if(discount==null)discount=new BigDecimal(10);
		boolean resourceDateIsNull = getResourceDate();
		
		//初始化广告设施类型
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		advfacilityTypeMap.put("dataDictionaryInfoKey", data);
		OrgModel orgModel = this.getModelsByUserCode(request, orgModels);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(advfacilityTypeMap);
		advplanlistMap.put("advPlanInfo", advertPlanInfo);
		advplanlistMap.put("advResourcePlanNumber", finaladvResourcePlancodeNumber);
		advplanlistMap.put("orgRegions", orgRegions);
		advplanlistMap.put("discount", discount);
		advplanlistMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		advplanlistMap.put("advertFacilityType", advertFacilityType);
		advplanlistMap.put("orgmodel",orgModel);
		advplanlistMap.put("resourceDateIsNull", resourceDateIsNull);
		return new ModelAndView(VIEW_REQUEST_CREATEADVPLANLIST_SUCCESS,advplanlistMap); 
	}
	
	/**
	 * @Description: TODO(按照区域,城市,影院名称,广告设施类型 查询广告设施编号) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertFacilityInfo
	 * @param 		 @return
	 * @param 		 @throws GenericRuntimeException
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-23 下午12:38:07
	 */
	public ModelAndView  queryAdvfalicityNumberByAreaCityCinemaAndfalicityType(HttpServletRequest request,HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo ) throws GenericRuntimeException, IOException{
		Map params = new HashMap(); 
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("cinemaId"));	//影院名称id
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));			//城市id
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("regionId"));	//区域id
		advertFacilityInfo.setOrgRegion(orgRegionInfo);					//区域
		advertFacilityInfo.setOrgCity(orgCityInfo);						//城市
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);					//影院名称
		advertFacilityInfo.setFacilityType(Long.parseLong(request.getParameter("falicitytypeId")));//广告设施类型
		params.put("advertFacilityKey", advertFacilityInfo);
		List<AdvertFacilityInfo> advertFacilityInfos = advertFacilityService.findAdvertFacilityNumberByAreaCityCinemaAndFacilityType(params); 
		List<AdvertFacilityInfo> advertFacilityInfosNumber = new ArrayList<AdvertFacilityInfo>();
		if (!advertFacilityInfos.isEmpty()) {
			for (int i = 0; i < advertFacilityInfos.size(); i++) {
				AdvertFacilityInfo afi = new AdvertFacilityInfo();
				afi.setFacilityId(advertFacilityInfos.get(i).getFacilityId());		//uuid
				afi.setFacilityCode(advertFacilityInfos.get(i).getFacilityCode());	//广告设施编码
				afi.setFacilityType(advertFacilityInfos.get(i).getFacilityType());	//广告设施类型
				advertFacilityInfosNumber.add(afi);
			}
		}
		//JSONArray.fromObject(advertFacilityInfos);
		IOUtil.writeJSONArray(JSONArray.fromObject(advertFacilityInfosNumber), response.getOutputStream());
		return null;
	}
	
	
	/**
	 * @Title:       queryAdvAllResourceByFacilityId 
	 * @Description: TODO(按照广告设施编号(uuid)查询广告资源数据) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertFacilityInfo
	 * @param 		 @throws GenericRuntimeException
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-17 下午02:10:08
	 * @throws
	 */
	public ModelAndView  queryAdvAllResourceByFacilityId(HttpServletRequest request,HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo ) throws GenericRuntimeException, IOException{
		Map params = new HashMap(); 
		Map resourceMap = new HashMap(); 
		Map resourceCodeMap = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		String queryType = request.getParameter("queryType");			//增加和维护的方案资源列表页不是同一个,加一个queryType来区分
		String planId = request.getParameter("planId");
		String flag = request.getParameter("flag");
		String edit = request.getParameter("edit");
		String currentPrice = request.getParameter("currentPrice");
		/*OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("regionId"));	//影院名称id
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));			//城市id
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("cinemaId"));	//区域id
		advertFacilityInfo.setOrgRegion(orgRegionInfo);					//区域
		advertFacilityInfo.setOrgCity(orgCityInfo);						//城市
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);					//影院名称
		*/
		//查询资源之前先更新一下更新标志-得到蓝色
		/*AdvertPlanInfo aInfo = new AdvertPlanInfo();
		aInfo.setPlanId(request.getParameter("planId"));
		resourcestateMap.put("advertPlanKeyByPlanId", aInfo);
		advResourcePlanService.updateAdvPlanResourceStateByPlanId(resourcestateMap);*/
		String planCode = request.getParameter("planCode");
		advertFacilityInfo.setFacilityId(request.getParameter("facilityId"));//广告设施编号对应的uuid
		params.put("advertFacilityKey", advertFacilityInfo);
		
		params.put("planCode",planCode);
		List<AdvertResourceInfo> resourceList = null;
		if(!StringUtils.isBlank(advertFacilityInfo.getFacilityId())){
			if(advertFacilityInfo.getFacilityId().split(",").length==1){
				resourceList = advertFacilityService.findAdvertResourcesList(params);
			}//else{
				//resourceList = advertFacilityService.findAdvertResourcesListAll(params);
			//}
		}
		JSONArray arrayUseDate = new JSONArray();
		StringBuffer sbd = new StringBuffer();
		AdvertResourcePlanInfo api = new AdvertResourcePlanInfo();
		String date = "";
		String rq = "";
		List<AdvertResourcePlanInfo> advertResourcePlanInfos = new ArrayList<AdvertResourcePlanInfo>();
		if(StringUtils.isNotBlank(planId)){
			sbd.append("[{");
			AdvertResourcePlanInfo info = new AdvertResourcePlanInfo();
			
			info.setPlanId(planId);
			params.put("planinfoKEY", info);
			advertResourcePlanInfos = advResourcePlanService.findAdvResourcePlanRelationByPlanId(params);
			if(Utils.isNotEmpty(advertResourcePlanInfos)){
				api = advertResourcePlanInfos.get(0);
				String[] usedate = api.getUseDate().split(",");
				date = usedate[0];
				String[] rqs = date.split("-");
				rq = rqs[1];
				
			}
			params.put("planCodeKey", request.getParameter("planCode"));
			//根据方案编号查询临时表相关数据
			List<AdvertResourcePlanTempInfo>  advertResourcePlanTempInfosList = advResourcePlanService.findAdvertResourcePlanTempsByPlanCode(params);
			String resourceIds = checkListByResourceId(advertResourcePlanTempInfosList,advertResourcePlanInfos);
			for(AdvertResourcePlanInfo resourcePlanInfo:advertResourcePlanInfos){
				if(!resourceIds.contains(resourcePlanInfo.getResourceId())){
					params.put("resourceId",resourcePlanInfo.getResourceId());
					sbd.append("},{\"resourceId\":"+ "\""+resourcePlanInfo.getResourceId()+"\"" +","
							+"\"resourceCode\":"+ "\""+advResourcePlanService.findResourceCode(params)+"\"" +","
							+"\"useDate\":"+ "\""+resourcePlanInfo.getUseDate()+"\"" +","
							+"\"useDateToDay\":"+ "\""+null+"\"" +","
							+"\"resourceUpdatestatus\":"+ "\""+ resourcePlanInfo.getResourceUpdatestatus()+"\"" +","
							+"\"resourceStatus\":"+ "\""+Dictionary.ADVERT_RESOURCE_STATUS_SELECTED+"\"},{"
							);
				}/*else{
					params.put("resourceId",resourcePlanInfo.getResourceId());
					sbd.append("},{\"resourceId\":"+ "\""+resourcePlanInfo.getResourceId()+"\"" +","
							+"\"resourceCode\":"+ "\""+advResourcePlanService.findResourceCode(params)+"\"" +","
							+"\"useDate\":"+ "\""+resourcePlanInfo.getUseDate()+"\"" +","
							+"\"useDateToDay\":"+ "\""+null+"\"" +","
							+"\"resourceUpdatestatus\":"+ "\""+ resourcePlanInfo.getResourceUpdatestatus()+"\"" +","
							+"\"resourceStatus\":"+ "\""+Dictionary.ADVERT_RESOURCE_STATUS_UNUSED+"\"},{"
							);
				}*/
			}
			sbd.append("}]");
			arrayUseDate = arrayUseDate.fromObject(sbd.toString());
		}
		/**
		 * 按照resourceList里面的资源编号查询T_ADVERT_RESOURCE_PLAN中间表返回该资源的使用的使用日期
		 */
		JSONArray arrayDate = new JSONArray();
	
		if(resourceList != null){
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("[");
			boolean biaoji = true;
			
			
			for (int i = 0; i < resourceList.size(); i++) {
				String resourceCode = resourceList.get(i).getResourceCode().trim();
				AdvertResourcePlanInfo advrp = new AdvertResourcePlanInfo();
				//用资源code得到资源的uuid
				//Map advresourcecodeMap = new HashMap(); 
				//AdvertResourceInfo ario = new AdvertResourceInfo();
				//ario.setResourceCode(resourceCode);
				//advresourcecodeMap.put("AdvertResourceInfoCodeKey", ario);
				//AdvertResource are = advResourcePlanService.findAdvertResourceIdByResourceCode(advresourcecodeMap);
				//if (are != null) {
				//  resourceId = are.getResourceId();
				//}
				advrp.setResourceId(resourceList.get(i).getResourceId());
				resourceCodeMap.put("AdvResourceInfoByCodeKey", advrp);
				resourceCodeMap.put("queryType", queryType);
				List<AdvertResourcePlanInfo> advRsourcePlaninfoList = advResourcePlanService.findAdvResourcePlanRelationByResourceCode(resourceCodeMap);
				//已选定的临时表数据
				AdvertResourcePlanTempInfo tempInfo = new AdvertResourcePlanTempInfo();
				tempInfo.setResourceId(resourceList.get(i).getResourceId());
				tempInfo.setPlanCode(request.getParameter("planCode"));
				resourceCodeMap.put("AdvResourcePlanTempKey", tempInfo);
				resourceCodeMap.put("currentPrice", currentPrice);
				boolean flags = true;
				List<AdvertResourcePlanTempInfo> advRsourcePlanTempinfos = advResourcePlanService.findAdvertResourcePlanTemps(resourceCodeMap);
//				if ("2".equals(queryType)) {
//					advRsourcePlanTempinfos = advResourcePlanService.findAdvertResourcePlanTemps(resourceCodeMap);
//				}
				
					/**
					 * 拼装json对象返回前端页面使用改变日期颜色
					 */
					if (advRsourcePlaninfoList != null && advRsourcePlaninfoList.size() > 0) {
						for (int j = 0; j < advRsourcePlaninfoList.size(); j++) {
							//String resourcecode = "";
							//用资源uuid得到资源的编码code
							//Map advMap = new HashMap(); 
							//AdvertResourceInfo advrio = new AdvertResourceInfo();
							//advrio.setResourceId(advRsourcePlaninfoList.get(j).getResourceId());
							//advMap.put("AdvertResourceInfoCodeByIdKey", advrio);
							//AdvertResource arce = advResourcePlanService.findAdvertResourceCodeByResourceId(advMap);
							//if (arce != null) {
							//  resourcecode = arce.getResourceCode();
							//}
							String day = "" ;
							if (!StringUtils.isBlank(advRsourcePlaninfoList.get(j).getUseDate())) {
								//处理date日期为day数据开始
								String[] dateStrings = advRsourcePlaninfoList.get(j).getUseDate().split(","); 
								/*for (int k = 0; k < dateStrings.length; k++) {
									if (dateStrings[k].length() > 1) {
										if (k == dateStrings.length -1) {
											day += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length());
										}else{
											day += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length())+",";//截取后两位
										}
									}
								}*/
							}
							stringBuffer.append("{");
							
							if ("2".equals(queryType)) {
								
								if(StringUtils.isNotBlank(planId)){
									if(advRsourcePlaninfoList.get(j).getResourceStatus() == Dictionary.ADVERT_RESOURCE_STATUS_PREORDER && planId.equals(advRsourcePlaninfoList.get(j).getPlanId())){
										advRsourcePlaninfoList.get(j).setResourceStatus(Dictionary.ADVERT_RESOURCE_STATUS_SELECTED);
									}
									if(advRsourcePlaninfoList.get(j).getPlanId().equals(planId)){
										String tempDay = "";
										if(advRsourcePlanTempinfos!=null && advRsourcePlanTempinfos.size()>0){
											int k = 0;
											for(AdvertResourcePlanTempInfo info:advRsourcePlanTempinfos){
												if(info.getResourceId().equals(advRsourcePlaninfoList.get(j).getResourceId())){
													if (!StringUtils.isBlank(info.getUseDate())) {
														if(advRsourcePlanTempinfos.size()>1){
															k++;
															biaoji = false;
															//stringBuffer.append("{");
															stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
																	+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
																	+"\"useDate\":"+ "\""+info.getUseDate()+"\"" +","
																	+"\"useDateToDay\":"+ "\""+tempDay+"\"" +","
																	+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
																	+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""
																	);
															if(k==advRsourcePlanTempinfos.size()){
																stringBuffer.append("},");
															}else{
																stringBuffer.append("},{");
															}
															
															flags = false;
														}else{
															//处理date日期为day数据开始
															String[] dateStrings = info.getUseDate().split(","); 
															/*for (int k = 0; k < dateStrings.length; k++) {
																if (dateStrings[k].length() > 1) {
																	if (k == dateStrings.length -1) {
																		tempDay += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length());
																	}else{
																		tempDay += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length())+",";//截取后两位
																	}
																}
															}*/
															biaoji = false;
															//stringBuffer.append("{");
															stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
																	+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
																	+"\"useDate\":"+ "\""+info.getUseDate()+"\"" +","
																	+"\"useDateToDay\":"+ "\""+tempDay+"\"" +","
																	+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
																	+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""
																	);
														}
														}
														
												}else{
													biaoji = false;
													//stringBuffer.append("{");
													stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
															+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
															+"\"useDate\":"+ "\""+advRsourcePlaninfoList.get(j).getUseDate()+"\"" +","
															+"\"useDateToDay\":"+ "\""+day+"\"" +","
															+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
															+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""+","
															);
												}
											}
										}else{
											biaoji = false;
											//stringBuffer.append("{");
											stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
													+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
													+"\"useDate\":"+ "\""+advRsourcePlaninfoList.get(j).getUseDate()+"\"" +","
													+"\"useDateToDay\":"+ "\""+day+"\"" +","
													+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
													+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""
													);
											}
									}else{
									//	stringBuffer.append("{");
										flags = true;
										if(advRsourcePlaninfoList.get(j).getEnable()==0){
											biaoji = false;
											
											stringBuffer.append("},{\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
													+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
													+"\"useDate\":"+ "\""+advRsourcePlaninfoList.get(j).getUseDate()+"\"" +","
													+"\"useDateToDay\":"+ "\""+day+"\"" +","
													+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
													+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\"},{"
													);
										}
									}
								}
							}else{
								biaoji = false;
								
								//stringBuffer.append("{");
								stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
										+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
										+"\"useDate\":"+ "\""+advRsourcePlaninfoList.get(j).getUseDate()+"\"" +","
										+"\"useDateToDay\":"+ "\""+day+"\"" +","
										+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
										+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""
										);
							}
							if(j == advRsourcePlaninfoList.size() -1 && advRsourcePlanTempinfos.size()<= 0){
								stringBuffer.append("}");	
							}else{
//								if(biaoji){
//									stringBuffer.append("{");
//								}
								if(flags){
									stringBuffer.append("},");
								}
								
								
							}
						}
						
					}//json  end 
					if(advRsourcePlanTempinfos != null && advRsourcePlanTempinfos.size()>0){
						for (int j = 0; j < advRsourcePlanTempinfos.size(); j++) {
							String day = "" ;
							if (!StringUtils.isBlank(advRsourcePlanTempinfos.get(j).getUseDate())) {
								//处理date日期为day数据开始
								String[] dateStrings = advRsourcePlanTempinfos.get(j).getUseDate().split(","); 
								for (int k = 0; k < dateStrings.length; k++) {
									if (dateStrings[k].length() > 1) {
										if (k == dateStrings.length -1) {
											day += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length());
										}else{
											day += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length())+",";//截取后两位
										}
									}
								}
							}
							if (j == 0 && advRsourcePlaninfoList != null && advRsourcePlaninfoList.size() > 0){
								stringBuffer.append(",{");
							}else{
								stringBuffer.append("{");
							}
							stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlanTempinfos.get(j).getResourceId()+"\"" +","
									+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
									+"\"useDate\":"+ "\""+advRsourcePlanTempinfos.get(j).getUseDate()+"\"" +","
									+"\"useDateToDay\":"+ "\""+day+"\"" +","
									+"\"resourceUpdatestatus\":"+ "\""+ Dictionary.ADVERT_RESOURCE_STATUS_SELECTED+"\"" +","
									+"\"resourceStatus\":"+ "\""+ Dictionary.ADVERT_RESOURCE_STATUS_SELECTED+"\""
									);
							if(j == advRsourcePlanTempinfos.size()-1){
								stringBuffer.append("}");
							}else{
								stringBuffer.append("},");
							}
						}
					}
					
					if(i == resourceList.size()-1){
					}else{
						stringBuffer.append(",");
					}
			}
			stringBuffer.append("]");
			if(stringBuffer!=null && !"".equals(stringBuffer.toString())){
				arrayDate = JSONArray.fromObject(stringBuffer.toString());
			}
		}
		/**
		 * 提供添加到列表中的json对象
		 */
		StringBuffer sb = new StringBuffer();
		if (resourceList != null) {
			sb.append("[");
			for (int i = 0; i < resourceList.size(); i++) {
				sb.append("{");
				sb.append("\"bianhao\":"+ "\""+resourceList.get(i).getResourceCode()+"\"" +","
						//+"\"shiyongriqi\":"+ "\""+resourceList.get(i).getUseDate()+"\"" +","
						+"\"quyu\":"+ "\""+resourceList.get(i).getAdvertFacility().getOrgRegion().getRegionName()+"\"" +","
						+"\"chengshi\":"+ "\""+resourceList.get(i).getAdvertFacility().getOrgCity().getCityName()+"\"" +","
						+"\"yingyuan\":"+ "\""+resourceList.get(i).getAdvertFacility().getOrgCinema().getCinemaName()+"\""
						);
				if(i==resourceList.size()-1){
					sb.append("}");	
				}else{
					sb.append("},");
				}
			}
			sb.append("]");
		}
		JSONArray array = new JSONArray();
		if(sb!=null && !"".equals(sb.toString())){
		  array = JSONArray.fromObject(sb.toString());
		}
		
		boolean resourceDateIsNull = getResourceDate();
		//IOUtil.writeJSONArray(JSONArray.fromObject(resourceList), response.getOutputStream());
		resourceMap.put("resourceList", resourceList);
		resourceMap.put("resourceListJson", array);
		resourceMap.put("arrayDateJson", arrayDate);
		resourceMap.put("arrayUseDate", arrayUseDate);
		resourceMap.put("rq", rq);
		resourceMap.put("planId", request.getParameter("planId"));		//维护方案中资源列表和资源使用日期列表中使用
		resourceMap.put("planCode", request.getParameter("planCode"));	//维护方案中资源列表和资源使用日期列表中使用
		resourceMap.put("isOccupyByContract", request.getParameter("isOccupyByContract"));
		resourceMap.put("flag", flag);
		resourceMap.put("edit", edit);
		String resourceCode = request.getParameter("resourceCode1");
		//String currentPrice = request.getParameter("currentPrice");
		String useDate = request.getParameter("useDates");
		String useDates = "";
		String planTempId = request.getParameter("planTempId");
		if(StringUtils.isNotBlank(useDate)){
			useDates = StringUtil.replaceBlank(useDate).replace("</br>", ",").replace("<br>", ",");
		}
		resourceMap.put("currentPrice", currentPrice);
		resourceMap.put("planTempId", planTempId);
		resourceMap.put("useDates", useDates);
		resourceMap.put("resourceCode", resourceCode);
		resourceMap.put("resourceDateIsNull", resourceDateIsNull);
//		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		UserTab user = avertDiscountService.findUserTabByUserId(JudgementAuthority.getUserInfo(request).getId());
		resourceMap.put("level", user.getSelectresource());
		
		Map paramsAdvresourcePlaninfoForMainTianMap = new HashMap();
		AdvertResourcePlanInfo advertResourcePlanInfo=new AdvertResourcePlanInfo();
		advertResourcePlanInfo.setPlanId(planId);
		paramsAdvresourcePlaninfoForMainTianMap.put("AdvResourceInfoByPlanIdKey", advertResourcePlanInfo);
		paramsAdvresourcePlaninfoForMainTianMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		//按照方案ID 查询中间表得到list
		List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(paramsAdvresourcePlaninfoForMainTianMap);
		String temp = "";
		for(AdvertResourcePlanInfo infos : advertResourcePlanInfosList){
			temp += infos.getResourceCode()+","+infos.getUseDate()+";";
		}
		resourceMap.put("temp", temp);
		if ("2".equals(queryType)) {
			return new ModelAndView(VIEW_REQUEST_QUERYADVALLRESOURCEBYFACILITYID_FORMAINTAIN_SUCCESS,resourceMap);
		}else{
			return new ModelAndView(VIEW_REQUEST_QUERYADVALLRESOURCEBYFACILITYID_SUCCESS,resourceMap);
		}
	}
	
	
	/**
	 * @Title:       queryAdvAllResourceByFacilityId 
	 * @Description: TODO(按照广告设施编号(uuid)查询广告资源数据) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertFacilityInfo
	 * @param 		 @throws GenericRuntimeException
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-17 下午02:10:08
	 * @throws
	 */
	public ModelAndView  queryAdvAllResourceByFacilityIds(HttpServletRequest request,HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo ) throws GenericRuntimeException, IOException{
		Map params = new HashMap(); 
		Map resourceMap = new HashMap(); 
		Map resourceCodeMap = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		String queryType = request.getParameter("queryType");			//增加和维护的方案资源列表页不是同一个,加一个queryType来区分
		String planId = request.getParameter("planId");
		String flag = request.getParameter("flag");
		String edit = request.getParameter("edit");
		String currentPrice = request.getParameter("currentPrice");
		
		String planCode = request.getParameter("planCode");
		advertFacilityInfo.setFacilityId(request.getParameter("facilityId"));//广告设施编号对应的uuid
		params.put("advertFacilityKey", advertFacilityInfo);
		
		params.put("planCode",planCode);
		List<AdvertResourceInfo> resourceList = null;
		if(!StringUtils.isBlank(advertFacilityInfo.getFacilityId())){
			if(advertFacilityInfo.getFacilityId().split(",").length==1){
				resourceList = advertFacilityService.findAdvertResourcesList(params);
			}//else{
				//resourceList = advertFacilityService.findAdvertResourcesListAll(params);
			//}
		}
		
		
		
		
		JSONArray arrayUseDate = new JSONArray();
		StringBuffer sbd = new StringBuffer();
		AdvertResourcePlanInfo api = new AdvertResourcePlanInfo();
		String date = "";
		String rq = "";
		List<AdvertResourcePlanInfo> advertResourcePlanInfos = new ArrayList<AdvertResourcePlanInfo>();
		if(StringUtils.isNotBlank(planId)){
			sbd.append("[{");
			AdvertResourcePlanInfo info = new AdvertResourcePlanInfo();
			
			info.setPlanId(planId);
			params.put("planinfoKEY", info);
			advertResourcePlanInfos = advResourcePlanService.findAdvResourcePlanRelationByPlanId(params);
			if(Utils.isNotEmpty(advertResourcePlanInfos)){
				api = advertResourcePlanInfos.get(0);
				String[] usedate = api.getUseDate().split(",");
				date = usedate[0];
				String[] rqs = date.split("-");
				rq = rqs[1];
				
			}
			params.put("planCodeKey", request.getParameter("planCode"));
			//根据方案编号查询临时表相关数据
			List<AdvertResourcePlanTempInfo>  advertResourcePlanTempInfosList = advResourcePlanService.findAdvertResourcePlanTempsByPlanCode(params);
			String resourceIds = checkListByResourceId(advertResourcePlanTempInfosList,advertResourcePlanInfos);
			if(Utils.isEmpty(advertResourcePlanTempInfosList)){
				for(AdvertResourcePlanInfo resourcePlanInfo:advertResourcePlanInfos){
					if(!resourceIds.contains(resourcePlanInfo.getResourceId())){
						params.put("resourceId",resourcePlanInfo.getResourceId());
						sbd.append("},{\"resourceId\":"+ "\""+resourcePlanInfo.getResourceId()+"\"" +","
								+"\"resourceCode\":"+ "\""+advResourcePlanService.findResourceCode(params)+"\"" +","
								+"\"useDate\":"+ "\""+resourcePlanInfo.getUseDate()+"\"" +","
								+"\"useDateToDay\":"+ "\""+null+"\"" +","
								+"\"resourceUpdatestatus\":"+ "\""+ resourcePlanInfo.getResourceUpdatestatus()+"\"" +","
								+"\"resourceStatus\":"+ "\""+Dictionary.ADVERT_RESOURCE_STATUS_SELECTED+"\"},{"
								);
					}/*else{
						params.put("resourceId",resourcePlanInfo.getResourceId());
						sbd.append("},{\"resourceId\":"+ "\""+resourcePlanInfo.getResourceId()+"\"" +","
								+"\"resourceCode\":"+ "\""+advResourcePlanService.findResourceCode(params)+"\"" +","
								+"\"useDate\":"+ "\""+resourcePlanInfo.getUseDate()+"\"" +","
								+"\"useDateToDay\":"+ "\""+null+"\"" +","
								+"\"resourceUpdatestatus\":"+ "\""+ resourcePlanInfo.getResourceUpdatestatus()+"\"" +","
								+"\"resourceStatus\":"+ "\""+Dictionary.ADVERT_RESOURCE_STATUS_UNUSED+"\"},{"
								);
					}*/
				}
			}else{
				for(AdvertResourcePlanTempInfo resourcePlanInfo:advertResourcePlanTempInfosList){
					if(resourceIds.contains(resourcePlanInfo.getResourceId())){
						params.put("resourceId",resourcePlanInfo.getResourceId());
						sbd.append("},{\"resourceId\":"+ "\""+resourcePlanInfo.getResourceId()+"\"" +","
								+"\"resourceCode\":"+ "\""+advResourcePlanService.findResourceCode(params)+"\"" +","
								+"\"useDate\":"+ "\""+resourcePlanInfo.getUseDate()+"\"" +","
								+"\"useDateToDay\":"+ "\""+null+"\"" +","
								+"\"resourceUpdatestatus\":"+ "\""+1201+"\"" +","
								+"\"resourceStatus\":"+ "\""+Dictionary.ADVERT_RESOURCE_STATUS_SELECTED+"\"},{"
								);
					}/*else{
						params.put("resourceId",resourcePlanInfo.getResourceId());
						sbd.append("},{\"resourceId\":"+ "\""+resourcePlanInfo.getResourceId()+"\"" +","
								+"\"resourceCode\":"+ "\""+advResourcePlanService.findResourceCode(params)+"\"" +","
								+"\"useDate\":"+ "\""+resourcePlanInfo.getUseDate()+"\"" +","
								+"\"useDateToDay\":"+ "\""+null+"\"" +","
								+"\"resourceUpdatestatus\":"+ "\""+ resourcePlanInfo.getResourceUpdatestatus()+"\"" +","
								+"\"resourceStatus\":"+ "\""+Dictionary.ADVERT_RESOURCE_STATUS_UNUSED+"\"},{"
								);
					}*/
				}
			}
			
			sbd.append("}]");
			arrayUseDate = arrayUseDate.fromObject(sbd.toString());
		}
		/**
		 * 按照resourceList里面的资源编号查询T_ADVERT_RESOURCE_PLAN中间表返回该资源的使用的使用日期
		 */
		JSONArray arrayDate = new JSONArray();
	
		if(resourceList != null){
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("[");
			boolean biaoji = true;
			
			
			for (int i = 0; i < resourceList.size(); i++) {
				String resourceCode = resourceList.get(i).getResourceCode().trim();
				AdvertResourcePlanInfo advrp = new AdvertResourcePlanInfo();
				//用资源code得到资源的uuid
				//Map advresourcecodeMap = new HashMap(); 
				//AdvertResourceInfo ario = new AdvertResourceInfo();
				//ario.setResourceCode(resourceCode);
				//advresourcecodeMap.put("AdvertResourceInfoCodeKey", ario);
				//AdvertResource are = advResourcePlanService.findAdvertResourceIdByResourceCode(advresourcecodeMap);
				//if (are != null) {
				//  resourceId = are.getResourceId();
				//}
				advrp.setResourceId(resourceList.get(i).getResourceId());
				resourceCodeMap.put("AdvResourceInfoByCodeKey", advrp);
				resourceCodeMap.put("queryType", queryType);
				List<AdvertResourcePlanInfo> advRsourcePlaninfoList = advResourcePlanService.findAdvResourcePlanRelationByResourceCode(resourceCodeMap);
				//已选定的临时表数据
				AdvertResourcePlanTempInfo tempInfo = new AdvertResourcePlanTempInfo();
				tempInfo.setResourceId(resourceList.get(i).getResourceId());
				tempInfo.setPlanCode(request.getParameter("planCode"));
				resourceCodeMap.put("AdvResourcePlanTempKey", tempInfo);
				resourceCodeMap.put("currentPrice", currentPrice);
				boolean flags = true;
				List<AdvertResourcePlanTempInfo> advRsourcePlanTempinfos = advResourcePlanService.findAdvertResourcePlanTemps(resourceCodeMap);
//				if ("2".equals(queryType)) {
//					advRsourcePlanTempinfos = advResourcePlanService.findAdvertResourcePlanTemps(resourceCodeMap);
//				}
				
					/**
					 * 拼装json对象返回前端页面使用改变日期颜色
					 */
					if (advRsourcePlaninfoList != null && advRsourcePlaninfoList.size() > 0) {
						for (int j = 0; j < advRsourcePlaninfoList.size(); j++) {
							String day = "" ;
							if (!StringUtils.isBlank(advRsourcePlaninfoList.get(j).getUseDate())) {
								//处理date日期为day数据开始
								String[] dateStrings = advRsourcePlaninfoList.get(j).getUseDate().split(","); 
								/*for (int k = 0; k < dateStrings.length; k++) {
									if (dateStrings[k].length() > 1) {
										if (k == dateStrings.length -1) {
											day += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length());
										}else{
											day += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length())+",";//截取后两位
										}
									}
								}*/
							}
							stringBuffer.append("{");
							
							if ("2".equals(queryType)) {
								
								if(StringUtils.isNotBlank(planId)){
									if(advRsourcePlaninfoList.get(j).getResourceStatus() == Dictionary.ADVERT_RESOURCE_STATUS_PREORDER && planId.equals(advRsourcePlaninfoList.get(j).getPlanId())){
										advRsourcePlaninfoList.get(j).setResourceStatus(Dictionary.ADVERT_RESOURCE_STATUS_SELECTED);
									}
									if(advRsourcePlaninfoList.get(j).getPlanId().equals(planId)){
										String tempDay = "";
										if(advRsourcePlanTempinfos!=null && advRsourcePlanTempinfos.size()>0){
											int k = 0;
											for(AdvertResourcePlanTempInfo info:advRsourcePlanTempinfos){
												if(info.getResourceId().equals(advRsourcePlaninfoList.get(j).getResourceId())){
													if (!StringUtils.isBlank(info.getUseDate())) {
														if(advRsourcePlanTempinfos.size()>1){
															k++;
															biaoji = false;
															//stringBuffer.append("{");
															stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
																	+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
																	+"\"useDate\":"+ "\""+info.getUseDate()+"\"" +","
																	+"\"useDateToDay\":"+ "\""+tempDay+"\"" +","
																	+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
																	+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""
																	);
															if(k==advRsourcePlanTempinfos.size()){
																stringBuffer.append("},");
															}else{
																stringBuffer.append("},{");
															}
															
															flags = false;
														}else{
															//处理date日期为day数据开始
															String[] dateStrings = info.getUseDate().split(","); 
															/*for (int k = 0; k < dateStrings.length; k++) {
																if (dateStrings[k].length() > 1) {
																	if (k == dateStrings.length -1) {
																		tempDay += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length());
																	}else{
																		tempDay += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length())+",";//截取后两位
																	}
																}
															}*/
															biaoji = false;
															//stringBuffer.append("{");
															stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
																	+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
																	+"\"useDate\":"+ "\""+info.getUseDate()+"\"" +","
																	+"\"useDateToDay\":"+ "\""+tempDay+"\"" +","
																	+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
																	+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""
																	);
														}
													}
														
												}/*else{
													biaoji = false;
													//stringBuffer.append("{");
													stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
															+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
															+"\"useDate\":"+ "\""+advRsourcePlaninfoList.get(j).getUseDate()+"\"" +","
															+"\"useDateToDay\":"+ "\""+day+"\"" +","
															+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
															+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""+","
															);
												}*/
											}
										}/*else{
											biaoji = false;
											//stringBuffer.append("{");
											stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
													+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
													+"\"useDate\":"+ "\""+advRsourcePlaninfoList.get(j).getUseDate()+"\"" +","
													+"\"useDateToDay\":"+ "\""+day+"\"" +","
													+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
													+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""
													);
											}*/
									}else{
									//	stringBuffer.append("{");
										flags = true;
										if(advRsourcePlaninfoList.get(j).getEnable()==0){
											biaoji = false;
											
											stringBuffer.append("},{\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
													+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
													+"\"useDate\":"+ "\""+advRsourcePlaninfoList.get(j).getUseDate()+"\"" +","
													+"\"useDateToDay\":"+ "\""+day+"\"" +","
													+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
													+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\"},{"
													);
										}
									}
								}
							}else{
								biaoji = false;
								
								//stringBuffer.append("{");
								stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceId()+"\"" +","
										+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
										+"\"useDate\":"+ "\""+advRsourcePlaninfoList.get(j).getUseDate()+"\"" +","
										+"\"useDateToDay\":"+ "\""+day+"\"" +","
										+"\"resourceUpdatestatus\":"+ "\""+ advRsourcePlaninfoList.get(j).getResourceUpdatestatus()+"\"" +","
										+"\"resourceStatus\":"+ "\""+advRsourcePlaninfoList.get(j).getResourceStatus()+"\""
										);
							}
							if(j == advRsourcePlaninfoList.size() -1 && advRsourcePlanTempinfos.size()<= 0){
								stringBuffer.append("}");	
							}else{
//								if(biaoji){
//									stringBuffer.append("{");
//								}
								if(flags){
									stringBuffer.append("},");
								}
								
								
							}
						}
						
					}//json  end 
					if(advRsourcePlanTempinfos != null && advRsourcePlanTempinfos.size()>0){
						for (int j = 0; j < advRsourcePlanTempinfos.size(); j++) {
							String day = "" ;
							if (!StringUtils.isBlank(advRsourcePlanTempinfos.get(j).getUseDate())) {
								//处理date日期为day数据开始
								String[] dateStrings = advRsourcePlanTempinfos.get(j).getUseDate().split(","); 
								for (int k = 0; k < dateStrings.length; k++) {
									if (dateStrings[k].length() > 1) {
										if (k == dateStrings.length -1) {
											day += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length());
										}else{
											day += dateStrings[k].substring(dateStrings[k].length()-2, dateStrings[k].length())+",";//截取后两位
										}
									}
								}
							}
							if (j == 0 && advRsourcePlaninfoList != null && advRsourcePlaninfoList.size() > 0){
								stringBuffer.append(",{");
							}else{
								stringBuffer.append("{");
							}
							stringBuffer.append("\"resourceId\":"+ "\""+advRsourcePlanTempinfos.get(j).getResourceId()+"\"" +","
									+"\"resourceCode\":"+ "\""+resourceCode+"\"" +","
									+"\"useDate\":"+ "\""+advRsourcePlanTempinfos.get(j).getUseDate()+"\"" +","
									+"\"useDateToDay\":"+ "\""+day+"\"" +","
									+"\"resourceUpdatestatus\":"+ "\""+ Dictionary.ADVERT_RESOURCE_STATUS_SELECTED+"\"" +","
									+"\"resourceStatus\":"+ "\""+ Dictionary.ADVERT_RESOURCE_STATUS_SELECTED+"\""
									);
							if(j == advRsourcePlanTempinfos.size()-1){
								stringBuffer.append("}");
							}else{
								stringBuffer.append("},");
							}
						}
					}
					
					if(i == resourceList.size()-1){
					}else{
						stringBuffer.append(",");
					}
			}
			stringBuffer.append("]");
			if(stringBuffer!=null && !"".equals(stringBuffer.toString())){
				arrayDate = JSONArray.fromObject(stringBuffer.toString());
			}
		}
		/**
		 * 提供添加到列表中的json对象
		 */
		StringBuffer sb = new StringBuffer();
		if (resourceList != null) {
			sb.append("[");
			for (int i = 0; i < resourceList.size(); i++) {
				sb.append("{");
				sb.append("\"bianhao\":"+ "\""+resourceList.get(i).getResourceCode()+"\"" +","
						//+"\"shiyongriqi\":"+ "\""+resourceList.get(i).getUseDate()+"\"" +","
						+"\"quyu\":"+ "\""+resourceList.get(i).getAdvertFacility().getOrgRegion().getRegionName()+"\"" +","
						+"\"chengshi\":"+ "\""+resourceList.get(i).getAdvertFacility().getOrgCity().getCityName()+"\"" +","
						+"\"yingyuan\":"+ "\""+resourceList.get(i).getAdvertFacility().getOrgCinema().getCinemaName()+"\""
						);
				if(i==resourceList.size()-1){
					sb.append("}");	
				}else{
					sb.append("},");
				}
			}
			sb.append("]");
		}
		JSONArray array = new JSONArray();
		if(sb!=null && !"".equals(sb.toString())){
		  array = JSONArray.fromObject(sb.toString());
		}
		
		
//		//点滴编辑时往临时表插入数据，对临时表进行操作。防止没有真正保存而对正式表进行操作
//		Map advertResourcePlanInfoMap = new HashMap();
//		AdvertResourcePlanInfo planInfo = new AdvertResourcePlanInfo();
//		planInfo.setPlanId(planId);
//		planInfo.setPlanCode(planCode);
//		advertResourcePlanInfoMap.put("AdvResourceInfoByPlanIdKey", planInfo);
////		List<OrgModel> orgModels = initOrgModels(request);
//		advertResourcePlanInfoMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
//				
//		//按照方案ID 查询中间表得到list
//		List<AdvertResourcePlanInfo>  advertResourcePlanInfosLists = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(advertResourcePlanInfoMap);
//		if(Utils.isNotEmpty(advertResourcePlanInfosLists)){
//			advertResourcePlanInfoMap.put("listKey", advertResourcePlanInfosLists);
//			advResourcePlanService.saveResourcePlanToResourcePlanTemp(advertResourcePlanInfoMap);
//		}
		
		boolean resourceDateIsNull = getResourceDate();
		//IOUtil.writeJSONArray(JSONArray.fromObject(resourceList), response.getOutputStream());
		resourceMap.put("resourceList", resourceList);
		resourceMap.put("resourceListJson", array);
		resourceMap.put("arrayDateJson", arrayDate);
		resourceMap.put("arrayUseDate", arrayUseDate);
		resourceMap.put("rq", rq);
		resourceMap.put("planId", request.getParameter("planId"));		//维护方案中资源列表和资源使用日期列表中使用
		resourceMap.put("planCode", request.getParameter("planCode"));	//维护方案中资源列表和资源使用日期列表中使用
		resourceMap.put("isOccupyByContract", request.getParameter("isOccupyByContract"));
		resourceMap.put("flag", flag);
		resourceMap.put("edit", edit);
		String resourceCode = request.getParameter("resourceCode1");
		//String currentPrice = request.getParameter("currentPrice");
		String useDate = request.getParameter("useDates");
		String useDates = "";
		String planTempId = request.getParameter("planTempId");
		if(StringUtils.isNotBlank(useDate)){
			useDates = StringUtil.replaceBlank(useDate).replace("</br>", ",").replace("<br>", ",");
		}
		resourceMap.put("currentPrice", currentPrice);
		resourceMap.put("planTempId", planTempId);
		resourceMap.put("useDates", useDates);
		resourceMap.put("resourceCode", resourceCode);
		resourceMap.put("resourceDateIsNull", resourceDateIsNull);
//		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		UserTab user = avertDiscountService.findUserTabByUserId(JudgementAuthority.getUserInfo(request).getId());
		resourceMap.put("level", user.getSelectresource());
		
		Map paramsAdvresourcePlaninfoForMainTianMap = new HashMap();
		AdvertResourcePlanInfo advertResourcePlanInfo=new AdvertResourcePlanInfo();
		advertResourcePlanInfo.setPlanId(planId);
		paramsAdvresourcePlaninfoForMainTianMap.put("AdvResourceInfoByPlanIdKey", advertResourcePlanInfo);
		paramsAdvresourcePlaninfoForMainTianMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		//按照方案ID 查询中间表得到list
		List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(paramsAdvresourcePlaninfoForMainTianMap);
		String temp = "";
		for(AdvertResourcePlanInfo infos : advertResourcePlanInfosList){
			temp += infos.getResourceCode()+","+infos.getUseDate()+";";
		}
		resourceMap.put("temp", temp);
		if ("2".equals(queryType)) {
			return new ModelAndView(VIEW_REQUEST_QUERYADVALLRESOURCEBYFACILITYID_FORMAINTAIN_SUCCESS,resourceMap);
		}else{
			return new ModelAndView(VIEW_REQUEST_QUERYADVALLRESOURCEBYFACILITYID_SUCCESS,resourceMap);
		}
	}
	
	/**
	 * 校验fDates是否有日期与sDates相同
	 * @param fDates
	 * @param sDates
	 * @return
	 */
	public String checkIncludeDate(String[] fDates,String sDates){
		StringBuffer str = new StringBuffer();
		int count =0;
		for(String fDate:fDates){
			if(!sDates.contains(fDate)){
				if(count==0){
					str.append(fDate);
					count++;
				}else{
					str.append(","+fDate);
				}
			}
		}
		return str.toString();
	}
	
	//列表方式创建广告资源方案-iframe page 跳转
	/*public ModelAndView  requestCreateAdvPlanListForIFrameSonPage(){
		
	}*/
	
	/**
	 * <p>请求-图片方式创建广告资源方案的页面
	 * @param request   current HTTP request
	 * @param response  current HTTP response
	 * @throws Exception
	 */
	public ModelAndView  requestCreateAdvPlanPic(HttpServletRequest request,HttpServletResponse response,AdvertPlanInfo advertPlanInfo ) throws GenericRuntimeException{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		//得到广告资源方案编号
	//	long  advResourcePlanNumber = AdvertCodeController.getAdvertCode(AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX, AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_TYPE);
	//	String finaladvResourcePlancodeNumber = AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX + "-" + NumberFormatUtil.advAgentNumberFormat(advResourcePlanNumber);
		String finaladvResourcePlancodeNumber=request.getParameter("planCode");
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
		
		BigDecimal discount = advResourcePlanService.findDiscountByName(params);
		if(discount==null)discount=new BigDecimal(10);
		OrgModel orgModel = this.getModelsByUserCode(request, orgModels);

		UserTab user = avertDiscountService.findUserTabByUserId(JudgementAuthority.getUserInfo(request).getId());
		
		Map advplanlistMap = new HashMap();
		advplanlistMap.put("finaladvResourcePlancodeNumber", finaladvResourcePlancodeNumber);
		advplanlistMap.put("advertFacilityType", advertFacilityType);
		advplanlistMap.put("advertFacilityTypeJSON", JSONArray.fromObject(advertFacilityType));
		advplanlistMap.put("orgRegions", orgRegions);
		advplanlistMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		advplanlistMap.put("discount", discount);
		advplanlistMap.put("orgmodel",orgModel);
		advplanlistMap.put("level",user.getSelectresource());//判断是否可以使用特殊资源
		return new ModelAndView(VIEW_REQUEST_CREATEADVPLANPIC_SUCCESS,advplanlistMap); 
	}
	
	/**
	 * @Title:       queryAdvResourcePlanList 
	 * @Description: TODO(维护广告资源方案-左边菜单请求得到的广告资源列表方法) 
	 * @param        @param request
	 * @param        @param response
	 * @param        @param advertPlanInfo
	 * @param        @return
	 * @param        @throws GenericRuntimeException 
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-11 上午09:19:33
	 */
	public ModelAndView  queryAdvResourcePlanList(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo ) throws GenericRuntimeException {
		Map advresourceplanMap = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		advresourceplanMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		advertPlanInfo.setPageSize(GlobalConstant.PAGE_SIZE); 	//10条数据
		advertPlanInfo.setPageNavigationURL(CommUtil.getUrl(request)); 
		advresourceplanMap.put("advPlanInfo", advertPlanInfo);
		advresourceplanMap.put("enable",request.getParameter("enable"));
		// 分页查询
		Page<AdvertPlanInfo,AdvertPlan>  pageResult = advResourcePlanService.findAdvResourcePlanInfos(advresourceplanMap);
		pageResult.setPageNavigationURL(advertPlanInfo.getPageNavigationURL());

		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.PLAN_STATUS);
		advresourceplanMap.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> planStatus = dictionaryService.findDataDictionarys(advresourceplanMap);
		//页面数据显示
	   Map modelMap = new HashMap();
	   modelMap.put("pageResult", pageResult); 					//列表数据
	   modelMap.put("advertPlanInfo", advertPlanInfo);
	   modelMap.put("planStatus", planStatus);
	   return new ModelAndView(VIEW_QUERY_ADVRESOURCEPLANINFO_LIST_SUCCESS, modelMap);
	}
	
	/**
	 * @Title:       deleteAdvResourcePlanInfo 
	 * @Description: TODO(
	 * 删除资源方案的方法,合同-方案-资源
	 * 1.只要如果合同表中有方案id 就不能删除方案(不管合同是不是在执行和合同已经到期)
	 * 2.如果广告合同表中没有方案id(就是该方案没有被使用)可以删除
	 * 3.在删除方案的时1.清空该方案中资源的使用日期 2.设置该方案中使用的所有资源为空闲中
	 * 4.删除方案表中的方案记录) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertPlanInfo
	 * @param 		 @throws GenericRuntimeException
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-12 下午05:01:04
	 */
	public ModelAndView  deleteAdvResourcePlanInfo(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo ) throws GenericRuntimeException, IOException {
		JSONObject json = new JSONObject();
		Map advresourceplanMap = new HashMap();
		advresourceplanMap.put("advPlanInfo", advertPlanInfo);
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		advresourceplanMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		//delete advresourcePlan
		boolean deleteAdvResourcePlanTag = advResourcePlanService.deleteAdvResourcePlan(advresourceplanMap);
		AdvertContract advertContract   = advResourcePlanService.findAdvContractByPlanId(advresourceplanMap);
		if(deleteAdvResourcePlanTag){
			json.put("success",Constant.ADVERT_FACILITY_LOCATION_SUCCESS);
		}else{
			json.put("failure",Constant.ADVERT_FACILITY_LOCATION_FAIL);
			if(advertContract != null){
				json.put("customerContractCode", advertContract.getCustomerContractCode()); //合同编号
				json.put("customerContractName", advertContract.getCustomerContractName()); //合同名称
			}
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		return null;
	}
	
	/**
	 * @Title:       addAdvResourcePlanInfoThroughList 
	 * @Description: TODO(列表方式创建广告资源方案) 
	 * @param 		 @param advertPlanInfo
	 * @param 		 @throws GenericRuntimeException
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-13 下午02:24:51
	 */
	public ModelAndView addAdvResourcePlanInfoThroughList(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo) throws GenericRuntimeException, IOException {
		Map advresourcePlanMap = new HashMap();
		Map advplancodMap = new HashMap();
		Map advplannameMap = new HashMap();
		Map advresourceplancodeMap = new HashMap();
		Map advplanidAndresourceIdAndUseDateMap = new HashMap();
		JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		advresourcePlanMap.put("keyAdvResourcePlanInfoThroughList", advertPlanInfo);
		/**
		 * 检验方案编号是否存在
		 */
		AdvertPlanInfo apicode = new AdvertPlanInfo();
		apicode.setPlanCode(advertPlanInfo.getPlanCode());
		advplancodMap.put("advertPlanKey", apicode);
		boolean codeTag = advResourcePlanService.checkAdvertPlanCode(advplancodMap);
		/**
		 * 检验方案名称是否存在
		 */
		AdvertPlanInfo apiname = new AdvertPlanInfo();
		apiname.setPlanName(advertPlanInfo.getPlanName().trim());
		advplannameMap.put("advertPlanKey", apiname);
		boolean nameTag = advResourcePlanService.checkAdvertPlanName(advplannameMap);
		/**
		 * 接收页面传过来的多个资源和多个资源对应使用日期的数据
		 * 1.向方案表写数据 T_ADVERT_PLAN
		 * 2.根据资源编号resourceCodes 查询 T_ADVERT_RESOURCE表得到资源的资源的uuid(为list)
		 * 3.在向T_ADVERT_RESOURCE_PLAN表插入数据之前先根据S-GZ-WJS-ZDGG-03-01查询T_ADVERT_RESOURCE_PLAN表
		 *	 如果没有数据,表示新增。直接插入数据
		 *	需要判断当前用户选择的日期有没有在别的方案中使用。如2011-1-1有没有在别的方案中被使用，有的话给出提示
		 *	//如果有数据,说明别的方案在使用S-GZ-WJS-ZDGG-03-01这个资源的某个日期,这个时候需要判断当前用户选择的
		 * 3.向中间表写数据 T_ADVERT_RESOURCE_PLAN 插入资源的uuid,使用日期,资源的状态(预定)
		 * 
		 */
		String[] resourceCodes = null;
		if(! StringUtils.isBlank(request.getParameter("resourceCodes"))){
			resourceCodes  	 = request.getParameter("resourceCodes").split(",");		//资源编号
		}
		String[] regionNames  		= request.getParameter("regionNames").split(",");	//区域
		String[] cityNames  		= request.getParameter("cityNames").split(",");		//城市
		String[] cinemaNames  		= request.getParameter("cinemaNames").split(",");	//影院名称
		String[] useDates  			= request.getParameter("useDates").split(",");		//使用日期
		List<AdvertResourcePlanInfo> advResourcePlanInfoList = new ArrayList<AdvertResourcePlanInfo>(); //关系表使用日期的list
		if(resourceCodes != null){
			for(int i = 0; i < resourceCodes.length; i++){
				AdvertResourceInfo advertResourceInfo = new AdvertResourceInfo();
				AdvertResourcePlanInfo advertResourcePlanInfo = new AdvertResourcePlanInfo();
				String advresourceId = "";	
					//字段循环处理
					for(int j = 1; j <= 2 ; j++ ){
						switch (j) {
						case 1:
							if(i < resourceCodes.length ){
							  advertResourceInfo.setResourceCode(resourceCodes[i]);
							  /**
							   * 按照资源code 返回资源的uuid
							   */
							  AdvertResourceInfo ari = new AdvertResourceInfo();
							  ari.setResourceCode(resourceCodes[i]);// resourceCodes[i]
							  advresourceplancodeMap.put("AdvertResourceInfoCodeKey", ari);
							  AdvertResource are = advResourcePlanService.findAdvertResourceIdByResourceCode(advresourceplancodeMap);
								  if (are != null) {
									advresourceId = are.getResourceId();
								  }
							}
							break;
						case 2:
							if(i < useDates.length ){
							  advertResourcePlanInfo.setResourceId(advresourceId);
							  advertResourcePlanInfo.setUseDate(StringUtil.replaceBlank(useDates[i]));//2012-1-1</br>2012-1-2</br>
							}
							break;
						default:
							break;
						}
					}
					advResourcePlanInfoList.add(advertResourcePlanInfo);
			}
		}
		//多个资源循环判断是否有使用日期重复占用
		String resultString ="";
		for (int i = 0; i < advResourcePlanInfoList.size(); i++) {
			//使用日期和资源uuid advResourcePlanInfoListKey
			advplanidAndresourceIdAndUseDateMap.put("advertResourcePlanKey", advResourcePlanInfoList.get(i)); 
			AdvertPlanInfo api = new AdvertPlanInfo();
			api.setPlanCode(advertPlanInfo.getPlanCode());
			advplanidAndresourceIdAndUseDateMap.put("advertPlanKey", api);	//方案编号
			resultString = advResourcePlanService.checkResourceUseDate(advplanidAndresourceIdAndUseDateMap);
		}
		//存在一个资源编号查出多条记录，但是使用日期不一样
		if(codeTag){
			json.put("add_advresourceplan_repeatcode",Constant.ADD_ADV_RESOURCEPLAN_THROUGH_LIST_CODE_FAIL);//列表方式保存方案,方案编号已经存在
		}else if(nameTag){
		   json.put("add_advresourceplan_repeatname",Constant.ADD_ADV_RESOURCEPLAN_THROUGH_LIST_NAME_FAIL);//列表方式保存方案,方案名称已经存在	
		}else{
				//可以保存方案-但是需要校验日期
			if ( StringUtil.isNotBlank(resultString)) {
 				//说明有别的方案在占用
				json.put("add_advresourceplan_repeatdate",resultString);
			}else{
				/**
				 * 说明没有方案在占用当前用户选择的日期,这个时候可以保存,先保存方案,在service层里面再保存关系表数据
				 */
				advresourcePlanMap.put("advResourcePlanRelationInfoListKey", advResourcePlanInfoList);//关系表的数据map
				
				advresourcePlanMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
				boolean addResourcePlaninfoThroughListTag = advResourcePlanService.addAdvResourcePlanInfoThroughList(advresourcePlanMap);
				
				if(addResourcePlaninfoThroughListTag){
					json.put("success",Constant.ADD_ADV_RESOURCEPLAN_SUCCESS);
				}else{
					json.put("failure",Constant.ADD_ADV_RESOURCEPLAN_FAIL);
				}
			}//resultString else end 
		}// 校验方案编号和方案名称是否重复结束
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 维护广告资源方案-列表方式开始
	 */
	/**
	 * 按照方案ID 查询出方案的基本信息
	 * @param request
	 * @param response
	 * @param AdvertPlanInfo
	 * @return
	 */
	public ModelAndView queryAdvResourcePlanInfoDetailByPlanId(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo )throws IOException{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		Map advfacilityTypeMap = new HashMap();
		params.put("advertPlanKey", advertPlanInfo);
		AdvertPlanInfo  apInfo = advResourcePlanService.findAdvertPlanInfo(params);
		AdvertResourcePlanInfo resourcePlanInfo = advResourcePlanService.findAdvertResourcePlanWithPrice(params);
		BigDecimal discount = advResourcePlanService.findDiscountByName(params);
		//0 ：字体红色        1：不变色
		String getRed = "1";
		if(discount == null){
			discount=new BigDecimal(10);
			if(resourcePlanInfo.getCurrentPriceTotal()!=null){
				if(resourcePlanInfo.getCurrentPriceTotal().compareTo(resourcePlanInfo.getCostPriceTotal())==-1){
					getRed = "0";
				}
			}
		}
		if(discount != null){
			if(resourcePlanInfo.getCurrentPriceTotal()!=null){
				BigDecimal currentPriceTotal = resourcePlanInfo.getCostPriceTotal().divide(new BigDecimal(10)).multiply(discount);
				if(resourcePlanInfo.getCurrentPriceTotal().compareTo(currentPriceTotal)==-1){
					getRed = "0";
				}
			}
		}
		
		//初始化区域,城市,影院名称-维护广告资源方案-列表方式使用
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		//初始化广告设施类型-维护广告资源方案-列表方式使用
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		advfacilityTypeMap.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(advfacilityTypeMap);
		
		Map paramsAdvresourcePlaninfoForMainTianMap = new HashMap();
		AdvertResourcePlanInfo advertResourcePlanInfo = new AdvertResourcePlanInfo();
		advertResourcePlanInfo.setPlanId(advertPlanInfo.getPlanId());
		paramsAdvresourcePlaninfoForMainTianMap.put("AdvResourceInfoByPlanIdKey", advertResourcePlanInfo);
		paramsAdvresourcePlaninfoForMainTianMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		//按照方案ID 查询中间表得到list
		List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(paramsAdvresourcePlaninfoForMainTianMap);
		AdvertResourcePlanInfo advertResourcePlanInfos = new AdvertResourcePlanInfo();
		if(Utils.isNotEmpty(advertResourcePlanInfosList)){
			advertResourcePlanInfos = advertResourcePlanInfosList.get(0);
		}
		
		String advertFacilityId = queryAdvfalicityNumberByAreaCityCinemaAndfalicityTypes(request,response,advertResourcePlanInfos);
		List<AdvertFacilityInfo> list= queryAdvfalicityNumberByAreaCityCinemaAndfalicityList(request,response,advertResourcePlanInfos);
		
		Map modelMap = new HashMap();
		
		String flag = request.getParameter("flag");
		initRegionAndCityAndCinema(advertResourcePlanInfos.getAreaCode(),advertResourcePlanInfos.getCityCode(),orgModels,modelMap);
		modelMap.put("advplaninfo", apInfo);
		modelMap.put("getRed", getRed);
		modelMap.put("resourcePlanInfo", resourcePlanInfo);
		modelMap.put("advertResourcePlanInfos",advertResourcePlanInfos);
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("discount", discount);
		modelMap.put("advertFacilityId", advertFacilityId);
		modelMap.put("list", list);
		modelMap.put("flag", flag);
		return new ModelAndView(VIEW_QUERYADVRESOURCEPLANDETAILBYPLANID_SUCCESS,modelMap);
	}
	
	
	public String  queryAdvfalicityNumberByAreaCityCinemaAndfalicityTypes(HttpServletRequest request,HttpServletResponse response,AdvertResourcePlanInfo advertResourcePlanInfos ) throws GenericRuntimeException, IOException{
		Map params = new HashMap(); 
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(advertResourcePlanInfos.getCinemaCode()==null?"":advertResourcePlanInfos.getCinemaCode());	//影院名称id
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(advertResourcePlanInfos.getCityCode()==null?"":advertResourcePlanInfos.getCityCode());			//城市id
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(advertResourcePlanInfos.getAreaCode()==null?"":advertResourcePlanInfos.getAreaCode());	//区域id
		AdvertFacilityInfo advertFacilityInfo = new AdvertFacilityInfo();
		advertFacilityInfo.setOrgRegion(orgRegionInfo);					//区域
		advertFacilityInfo.setOrgCity(orgCityInfo);						//城市
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);					//影院名称
		advertFacilityInfo.setFacilityType(advertResourcePlanInfos.getFacilityType()==null?Long.parseLong("0"):advertResourcePlanInfos.getFacilityType());//广告设施类型
		params.put("advertFacilityKey", advertFacilityInfo);
		List<AdvertFacilityInfo> advertFacilityInfos = advertFacilityService.findAdvertFacilityNumberByAreaCityCinemaAndFacilityType(params); 
		String advertFacilityId = "";
		if (!advertFacilityInfos.isEmpty()) {
			for (int i = 0; i < advertFacilityInfos.size(); i++) {
				if(advertResourcePlanInfos.getFacilityType().equals(advertFacilityInfos.get(i).getFacilityType())&&advertResourcePlanInfos.getFacilityCode().equals(advertFacilityInfos.get(i).getFacilityCode())){
//					afi.setFacilityId(advertFacilityInfos.get(i).getFacilityId());		//uuid
//					afi.setFacilityCode(advertFacilityInfos.get(i).getFacilityCode());	//广告设施编码
//					afi.setFacilityType(advertFacilityInfos.get(i).getFacilityType());	//广告设施类型
//					advertFacilityInfosNumber.add(afi);
					advertFacilityId = advertFacilityInfos.get(i).getFacilityId();
					return advertFacilityId;
				}
				
			}
		}
		//JSONArray.fromObject(advertFacilityInfos);
//		IOUtil.writeJSONArray(JSONArray.fromObject(advertFacilityInfosNumber), response.getOutputStream());
		return advertFacilityId;
	}
	
	public List  queryAdvfalicityNumberByAreaCityCinemaAndfalicityList(HttpServletRequest request,HttpServletResponse response,AdvertResourcePlanInfo advertResourcePlanInfos ) throws GenericRuntimeException, IOException{
		Map params = new HashMap(); 
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(advertResourcePlanInfos.getCinemaCode()==null?"":advertResourcePlanInfos.getCinemaCode());	//影院名称id
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(advertResourcePlanInfos.getCityCode()==null?"":advertResourcePlanInfos.getCityCode());			//城市id
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(advertResourcePlanInfos.getAreaCode()==null?"":advertResourcePlanInfos.getAreaCode());	//区域id
		AdvertFacilityInfo advertFacilityInfo = new AdvertFacilityInfo();
		advertFacilityInfo.setOrgRegion(orgRegionInfo);					//区域
		advertFacilityInfo.setOrgCity(orgCityInfo);						//城市
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);					//影院名称
		advertFacilityInfo.setFacilityType(advertResourcePlanInfos.getFacilityType()==null?Long.parseLong("0"):advertResourcePlanInfos.getFacilityType());//广告设施类型
		params.put("advertFacilityKey", advertFacilityInfo);
		List<AdvertFacilityInfo> advertFacilityInfos = advertFacilityService.findAdvertFacilityNumberByAreaCityCinemaAndFacilityType(params); 
		List<AdvertFacilityInfo> advertFacilityInfosNumber = new ArrayList<AdvertFacilityInfo>();
		String advertFacilityId = "";
		if (!advertFacilityInfos.isEmpty()) {
			for (int i = 0; i < advertFacilityInfos.size(); i++) {
					AdvertFacilityInfo afi = new AdvertFacilityInfo();
					afi.setFacilityId(advertFacilityInfos.get(i).getFacilityId());		//uuid
					afi.setFacilityCode(advertFacilityInfos.get(i).getFacilityCode());	//广告设施编码
					afi.setFacilityType(advertFacilityInfos.get(i).getFacilityType());	//广告设施类型
					advertFacilityInfosNumber.add(afi);
//					advertFacilityId = advertFacilityInfos.get(i).getFacilityId();
					
				
				
			}
		}
		//JSONArray.fromObject(advertFacilityInfos);
//		IOUtil.writeJSONArray(JSONArray.fromObject(advertFacilityInfosNumber), response.getOutputStream());
		return advertFacilityInfosNumber;
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
	
	/**
	 * @Description: TODO(维护资源方案-资源使用日期的列表方法,按照方案的id 查询资源使用日期,得到列表) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertPlanInfo
	 * @param 		 @return
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-23 下午03:25:20
	 */
	public ModelAndView queryAdvAllResourceUseDateByPlanId(HttpServletRequest request, HttpServletResponse response, AdvertResourcePlanInfo advertResourcePlanInfo ){
		Map paramsAdvresourcePlaninfoForMainTianMap = new HashMap();
		String planId = request.getParameter("planId");
		advertResourcePlanInfo.setPlanId(planId);
		paramsAdvresourcePlaninfoForMainTianMap.put("AdvResourceInfoByPlanIdKey", advertResourcePlanInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		paramsAdvresourcePlaninfoForMainTianMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		
		
		//按照方案ID 查询中间表得到list
		//List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(paramsAdvresourcePlaninfoForMainTianMap);
		List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = new ArrayList<AdvertResourcePlanInfo>();
		
		String planCode = request.getParameter("planCode");
		paramsAdvresourcePlaninfoForMainTianMap.put("planCodeKey", planCode);
		//根据方案编号查询临时表相关数据
		
		List<AdvertResourcePlanTempInfo>  advertResourcePlanTempInfosList = advResourcePlanService.findAdvertResourcePlanTempsByPlanCode(paramsAdvresourcePlaninfoForMainTianMap);
		if(Utils.isEmpty(advertResourcePlanTempInfosList)){
			advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(paramsAdvresourcePlaninfoForMainTianMap);
		}
		List<AdvertResourcePlanInfo>  resourcePlanInfos = new ArrayList<AdvertResourcePlanInfo>();
		List<AdvertResourcePlanInfo>  resourcePlanInfosForRemove = new ArrayList<AdvertResourcePlanInfo>();
		if(advertResourcePlanTempInfosList!=null && advertResourcePlanTempInfosList.size()>0){
			//中间表与临时表之间重复的resourceId
			String resourceIds = checkListByResourceId(advertResourcePlanTempInfosList,advertResourcePlanInfosList);
			String[] resourceIdList = resourceIds.split("-");
			for(AdvertResourcePlanTempInfo tempInfo:advertResourcePlanTempInfosList){
				AdvertResourcePlanInfo info = new AdvertResourcePlanInfo();
				info.setArea(tempInfo.getRegionName());
				info.setPlanId(planId);
				info.setCinemaName(tempInfo.getCinemaName());
				info.setCity(tempInfo.getCityName());
				info.setResourceCode(tempInfo.getResourceCode());
				info.setUseDate(tempInfo.getUseDate());
				info.setCurrentPrice(tempInfo.getCurrentPrice());
				info.setPrice(tempInfo.getPrice());
				info.setCostPrice(tempInfo.getCostPrice());
				info.setDays(tempInfo.getDays());
				info.setResourceId(tempInfo.getResourceId());
				info.setFacilityCode(tempInfo.getFacilityCode());
				info.setFacilityType(tempInfo.getFacilityType());
				resourcePlanInfos.add(info);
//				for(String resourceId:resourceIdList){
//					if(StringUtils.isNotBlank(resourceId) && tempInfo.getResourceId().equals(resourceId)){
//						resourcePlanInfosForRemove.add(findAdvertResourcePlan(advertResourcePlanInfosList,resourceId));
//					}
//				}
			}
		}
//		advertResourcePlanInfosList.removeAll(resourcePlanInfosForRemove);
		advertResourcePlanInfosList.addAll(resourcePlanInfos);
		Map advplanMap = new HashMap();
		AdvertPlanInfo  apio =  new AdvertPlanInfo();
		apio.setPlanId(advertResourcePlanInfo.getPlanId());
		advplanMap.put("advertPlanKey", apio);
		//根据方案ID，查找方案详细信息
		AdvertPlanInfo  advPlanInfoByPlanId = advResourcePlanService.findAdvertPlanInfo(advplanMap);
		String remark = "";
		String spyj = "";
		String status = "";
		if (advPlanInfoByPlanId != null) {
			remark = advPlanInfoByPlanId.getRemark();
			spyj = advPlanInfoByPlanId.getSpyj();
			status = advPlanInfoByPlanId.getStatus().toString();
		}
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		paramsAdvresourcePlaninfoForMainTianMap.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(paramsAdvresourcePlaninfoForMainTianMap);
		Map modelMap = new HashMap();
		modelMap.put("advresourceUseDateList", advertResourcePlanInfosList);
		modelMap.put("remark", remark);
		modelMap.put("spyj", spyj);
		modelMap.put("status", status);
		modelMap.put("planCode",planCode);
		modelMap.put("advertFacilityType",advertFacilityType);
		modelMap.put("isOccupyByContract",request.getParameter("isOccupyByContract"));
		modelMap.put("flag", request.getParameter("flag"));
		
//		//点滴编辑时往临时表插入数据，对临时表进行操作。防止没有真正保存而对正式表进行操作
//				Map advertResourcePlanInfoMap = new HashMap();
//				AdvertResourcePlanInfo planInfo = new AdvertResourcePlanInfo();
//				planInfo.setPlanId(planId);
//				planInfo.setPlanCode(planCode);
//				advertResourcePlanInfoMap.put("AdvResourceInfoByPlanIdKey", planInfo);
////				List<OrgModel> orgModels = initOrgModels(request);
//				advertResourcePlanInfoMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
//						
//				//按照方案ID 查询中间表得到list
//				List<AdvertResourcePlanInfo>  advertResourcePlanInfosLists = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(advertResourcePlanInfoMap);
//				if(Utils.isNotEmpty(advertResourcePlanInfosLists)){
//					advertResourcePlanInfoMap.put("listKey", advertResourcePlanInfosLists);
//					advResourcePlanService.saveResourcePlanToResourcePlanTemp(advertResourcePlanInfoMap);
//				}
		
		UserTab user = avertDiscountService.findUserTabByUserId(JudgementAuthority.getUserInfo(request).getId());
		modelMap.put("level", user.getSelectresource());
		return new ModelAndView(VIEW_MAINTAIN_ADVPLAN_RESOURCEUASDATE_SUCCESS,modelMap);
	}
	
	/**
	 * 在List<AdvertResourcePlan>查询对应的resourceId 并返回该对象
	 * @param resourcePlans
	 * @param resourceId
	 * @return
	 */
	public AdvertResourcePlanInfo findAdvertResourcePlan(List<AdvertResourcePlanInfo> resourcePlans,String resourceId){
		AdvertResourcePlanInfo planInfo = new AdvertResourcePlanInfo();
		for(AdvertResourcePlanInfo resourcePlan:resourcePlans){
			if(resourcePlan.getResourceId().equals(resourceId)){
				return resourcePlan;
			}
		}
		return planInfo;
	}
	
	/**
	 * 验证List<AdvertResourcePlanTemp> 与  List<AdvertResourcePlan>直接是否有资源编码相同的 并返回
	 * @return
	 */
	public String checkListByResourceId(List<AdvertResourcePlanTempInfo> tempInfos,List<AdvertResourcePlanInfo> resourcePlanInfos){
		String result = "";
		for(AdvertResourcePlanTempInfo temp:tempInfos){
			for(AdvertResourcePlanInfo resourcePlan:resourcePlanInfos){
				if(temp.getResourceId().equals(resourcePlan.getResourceId())){
					if(StringUtils.isNotBlank(result)){
						result+="-"+temp.getResourceId();
					}else{
						result+= temp.getResourceId();
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * @throws IOException 
	 * @Description: TODO(维护广告资源方案-维护功能中-添加到列表的方法) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertResourcePlanInfo
	 * @param 		 @return
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-23 下午09:32:56 
	 * @throws
	 */
	public ModelAndView addAdvResourceUseDateToList(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo ) throws IOException{
		Map advresourcePlanMap = new HashMap();
		//Map advplannameMap = new HashMap();
		Map advplanidAndresourceIdAndUseDateMap = new HashMap();
		JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		/**
		 * 检验方案名称是否存在
		 */
		/*AdvertPlanInfo apiname = new AdvertPlanInfo();
		apiname.setPlanName(advertPlanInfo.getPlanName().trim());
		advplannameMap.put("advertPlanKey", apiname);
		boolean nameTag = advResourcePlanService.checkAdvertPlanName(advplannameMap);*/
		
		/*String[] resourceCodes = null;
		if(! StringUtils.isBlank(request.getParameter("resourceCodes"))){
			resourceCodes  	 = request.getParameter("resourceCodes").split(",");		//资源编号
		}
		String usedate  = request.getParameter("resourceUseDates");
		if (usedate.startsWith(",")) {
			usedate = usedate.substring(1, usedate.length());
		}
		String[] resourceUseDates = usedate.split(",");//使用日期*/	
	
		List<AdvertResourcePlanInfo> advResourcePlanInfoList = new ArrayList<AdvertResourcePlanInfo>(); //关系表使用日期的list
		String jsondate = request.getParameter("jsondate");
		JSONArray  jsonArray = JSONArray.fromObject(jsondate);
		for (int i = 0; i < jsonArray.size(); i++) {
			 JSONObject jsonObject = jsonArray.getJSONObject(i);
			 String bianhao= jsonObject.getString("bianhao");
			 String shiyongriqi= jsonObject.getString("shiyongriqi");
			 
			 String advresourceId ="";
			 AdvertResourceInfo ari = new AdvertResourceInfo();
			  ari.setResourceCode(bianhao);
			  Map advppMap = new HashMap();
			  advppMap.put("AdvertResourceInfoCodeKey", ari);
			  AdvertResource are = advResourcePlanService.findAdvertResourceIdByResourceCode(advppMap);
				  if (are != null) {
					advresourceId = are.getResourceId();
				  }
			 
			 	AdvertResourcePlanInfo advertResourcePlanInfos = new AdvertResourcePlanInfo();
			 	advertResourcePlanInfos.setPlanId(advertPlanInfo.getPlanId());
				advertResourcePlanInfos.setResourceId(advresourceId);
				advertResourcePlanInfos.setUseDate(shiyongriqi);//2012-1-1</br>2012-1-2</br>
				advResourcePlanInfoList.add(advertResourcePlanInfos);
		}
		
		//System.out.println("advResourcePlanInfoList==" + advResourcePlanInfoList);
		/*if(resourceCodes != null){
			for(int i = 0; i < resourceCodes.length; i++){
				AdvertResourceInfo advertResourceInfo = new AdvertResourceInfo();
				AdvertResourcePlanInfo advertResourcePlanInfos = new AdvertResourcePlanInfo();
				String advresourceId = "";	
					//字段循环处理
				String date ="";
					for(int j = 1; j <= 2 ; j++ ){
						switch (j) {
						case 1:
							if(i < resourceCodes.length ){
							  advertResourceInfo.setResourceCode(resourceCodes[i]);
							  *//*
							   * 按照资源code 返回资源的uuid
							   *//*
							  AdvertResourceInfo ari = new AdvertResourceInfo();
							  ari.setResourceCode(resourceCodes[i]);// resourceCodes[i]
							  Map advppMap = new HashMap();
							  advppMap.put("AdvertResourceInfoCodeKey", ari);
							  AdvertResource are = advResourcePlanService.findAdvertResourceIdByResourceCode(advppMap);
								  if (are != null) {
									advresourceId = are.getResourceId();
								  }
							}
							break;
						case 2:
							if(i < resourceUseDates.length ){
								 date = resourceUseDates[i];
 								for (int k = 0; k < resourceUseDates.length; k++) {
 									if (k == resourceUseDates.length-1) { 
 										date += resourceUseDates[k];
 									}else{
 										date += resourceUseDates[k]+"<br/>";
 									}
 								}
							}
							break;
						default:
							break;
						}
					}
					advertResourcePlanInfos.setPlanId(advertPlanInfo.getPlanId());
					advertResourcePlanInfos.setResourceId(advresourceId);
					advertResourcePlanInfos.setUseDate(date);//2012-1-1</br>2012-1-2</br>
					advResourcePlanInfoList.add(advertResourcePlanInfos);
			}
		}*/
		//多个资源循环判断是否有使用日期重复占用
		String resultString ="";
		for (int i = 0; i < advResourcePlanInfoList.size(); i++) {
			advplanidAndresourceIdAndUseDateMap.put("advertResourcePlanKey", advResourcePlanInfoList.get(i)); //使用日期和资源uuid advResourcePlanInfoListKey
			AdvertPlanInfo api = new AdvertPlanInfo();
			api.setPlanCode(advertPlanInfo.getPlanCode());
			advplanidAndresourceIdAndUseDateMap.put("advertPlanKey", api);		//方案编号
			resultString = advResourcePlanService.checkResourceUseDate(advplanidAndresourceIdAndUseDateMap);
		}
		/*存在一个资源编号查出多条记录，但是使用日期不一样
		if(nameTag){
		   json.put("maintain_advresourceplan_repeatname",Constant.MAINTAIN_ADV_RESOURCEPLAN_THROUGH_LIST_NAME_FAIL);//列表方式维护方案,方案名称已经存在	
		}else{*/
			
		//可以更新方案-但是需要校验日期
			if ( StringUtil.isNotBlank(resultString)) {
 				//说明有别的方案在占用
				json.put("add_advresourceplan_repeatdate",resultString);
			}else{
				/**
				 * 说明没有方案在占用当前用户选择的日期,这个时候可以保存,先更新方案,在service层里面再保存关系表数据
				 */
				advresourcePlanMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
				advresourcePlanMap.put("advResourcePlanRelationInfoListKey", advResourcePlanInfoList);//关系表的数据map
				boolean maintainResourcePlaninfoThroughListTag = advResourcePlanService.updateAdvResourcePlanInfoThroughList(advresourcePlanMap);
				if(maintainResourcePlaninfoThroughListTag){
					json.put("success",Constant.MAINTAIN_ADV_RESOURCEPLAN_SUCCESS);
				}else{
					json.put("failure",Constant.MAINTAIN_ADV_RESOURCEPLAN_FAIL);
				}
			}//resultString else end 
		//}// 校验方案编号和方案名称是否重复结束
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
  }
	
	
	/**
	 * @Description: TODO(按照方案id更新方案信息) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertPlanInfo
	 * @param 		 @return
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-24 下午04:00:01
	 * @throws
	 */
	public ModelAndView updatePlaninfoByPlanId(HttpServletRequest request, HttpServletResponse response,AdvertPlanInfo advertPlanInfo ) throws IOException{
		Map paramsMap = new HashMap();
		JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		/**
		 * 检验方案名称是否存在
		 */
		/*AdvertPlanInfo apiname = new AdvertPlanInfo(); 
		apiname.setPlanName(advertPlanInfo.getPlanName().trim());
		paramsupdateadvplanMap.put("advertPlanKey", apiname);
		boolean nameTag = advResourcePlanService.checkAdvertPlanName(paramsupdateadvplanMap);*/
		paramsMap.put("advertPlanKeyByPlanId", advertPlanInfo);
		paramsMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		boolean mantainplannameTag = advResourcePlanService.updateAdvPlaninfoByPlanId(paramsMap);
		/*if (nameTag) {
			json.put("planname_exist",Constant.MAINTAIN_ADV_PLAN_PLANNAMEEXIST);
		}*/
		if(mantainplannameTag){
			json.put("maintain_success",Constant.MAINTAIN_ADV_PLAN_SUCCESS);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 维护广告资源方案-列表方式结束
	 */
	
	/**
	 * 进入查询广告位页面
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 * @author ldw
	 */
	public ModelAndView showFindAdvertFacility(HttpServletRequest request, HttpServletResponse response, AdvertFacilityInfo advertFacilityInfo ){
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
		
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		Map modelMap = new HashMap();
		List<OrgModel> orgModelsRegion=(List<OrgModel>)request.getSession().getAttribute(GlobalConstant.USER_CODE_KEY);
		for (int i = 0; i < orgModelsRegion.size(); i++) {
			OrgModel obj=orgModelsRegion.get(i);
			if("REGION".equals(obj.getLevel().toString())){
				modelMap.put("OrgRegion", obj.getOrgId());
			}else if("CITY".equals(obj.getLevel().toString())){
				modelMap.put("OrgCity", obj.getOrgId());
			}else if("CINEMA".equals(obj.getLevel().toString())){
				modelMap.put("OrgCinema", obj.getOrgId());
			}
		}
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("orgmodel",orgModel);
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("advertFacilityInfo", advertFacilityInfo);
		modelMap.put("advertFacilityTypeJSON", JSONArray.fromObject(advertFacilityType));
		return new ModelAndView(VIEW_ADVINPUT_SHOW_ADVERTFACILITY_SUCCESS,modelMap);
	}
	
	/**
	 * 查询广告位
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 * @author ldw
	 * @deprecated
	 */
	public ModelAndView findAdvertFacilityLocation(HttpServletRequest request, HttpServletResponse response, AdvertFacilityInfo advertFacilityInfo ){
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		List<AdvertIconInfo> advertIcons = advertIconService.findAdvertIcon();
		
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("selectCinemaName"));
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("selectCityName"));
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("selectRegionName"));
		
		advertFacilityInfo.setOrgRegion(orgRegionInfo);
		advertFacilityInfo.setOrgCity(orgCityInfo);
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);
		
		if(StringUtils.isNotBlank(request.getParameter("selectAdvertFacilityTypeName"))){
			advertFacilityInfo.setFacilityType(Long.valueOf(request.getParameter("selectAdvertFacilityTypeName")));
		}
		params.put("advertFacilityKey", advertFacilityInfo);
		FloorPlanInfo floorPlanInfo= advertFacilityService.findFloorPlanByRegionId(params);
		
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
		
		
		List<AdvertFacilityInfo> advertFacilityInfos = advertFacilityService.findAdvertFacilityWithResourceUseType(params);
		
		Map modelMap = new HashMap();
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("advertFacilityTypeJSON", JSONArray.fromObject(advertFacilityType));
		modelMap.put("floorPlanInfo", floorPlanInfo);
		modelMap.put("advertFacilityInfosJSON", JSONArray.fromObject(advertFacilityInfos));
		modelMap.put("advertIconsJSON", JSONArray.fromObject(advertIcons));
		return new ModelAndView(VIEW_ADVINPUT_SHOW_ADVERTFACILITY_SUCCESS,modelMap);
	}
	
	/**
	 * 图片创建方案-AJAX查询广告位
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 * @throws IOException 
	 */
	public void findAdvertFacilityLocationForAjax(HttpServletRequest request, HttpServletResponse response, AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		JSONObject json = new JSONObject();
		List<AdvertIconInfo> advertIcons = advertIconService.findAdvertIcon();
		
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("cinemaId"));
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("regionId"));
		
		advertFacilityInfo.setOrgRegion(orgRegionInfo);
		advertFacilityInfo.setOrgCity(orgCityInfo);
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);
		
		params.put("advertFacilityKey", advertFacilityInfo);
		
		FloorPlanInfo floorPlanInfo= advertFacilityService.findFloorPlanByRegionId(params);
		List<AdvertFacilityInfo> advertFacilityInfos = advertFacilityService.findAdvertFacilityWithResourceUseType(params);
		json.put("advertFacilityInfos", advertFacilityInfos);
		json.put("floorPlanInfo", floorPlanInfo);
		json.put("advertIcons", advertIcons);
		IOUtil.writeJSON(json, response.getOutputStream());
	}
	
	/**
	 * 图片维护方案-AJAX查询广告位
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 * @throws IOException 
	 */
	public void findAdvertFacilityLocationForUpdate(HttpServletRequest request, HttpServletResponse response, AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		JSONObject json = new JSONObject();
		List<AdvertIconInfo> advertIcons = advertIconService.findAdvertIcon();
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("cinemaId"));
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("regionId"));
		
		advertFacilityInfo.setOrgRegion(orgRegionInfo);
		advertFacilityInfo.setOrgCity(orgCityInfo);
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);
		
		params.put("advertFacilityKey", advertFacilityInfo);
		params.put("planId", request.getParameter("planId"));
		FloorPlanInfo floorPlanInfo= advertFacilityService.findFloorPlanByRegionId(params);
		List<AdvertFacilityInfo> advertFacilityInfos = advResourcePlanService.findAdvertFacilityWithResourceUseType(params);
		json.put("advertFacilityInfos", advertFacilityInfos);
		json.put("floorPlanInfo", floorPlanInfo);
		json.put("advertIcons", advertIcons);
		IOUtil.writeJSON(json, response.getOutputStream());
	}
	
	/**
	 * 根据设施ID查询资源信息
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @throws IOException
	 */
	public void findAdvertResourceByFacilityId(HttpServletRequest request, HttpServletResponse response, AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		params.put("advertFacilityKey", advertFacilityInfo);
		List<AdvertResourceInfo> advertResourceInfos = advertFacilityService.findAdvertResourceByFacilityId(params);
		IOUtil.writeJSONArray(JSONArray.fromObject(advertResourceInfos), response.getOutputStream());
	}
	
	/**
	 * 根据资源ID，方案ID，查找不在该方案的资源使用日期，并将日期格式化成时间段
	 * @param request
	 * @param response
	 * @param advertResourcePlanInfo
	 * @throws IOException
	 * @throws ParseException
	 */
	public void findAdvertResourcePlan(HttpServletRequest request, HttpServletResponse response,AdvertResourcePlanInfo advertResourcePlanInfo) throws IOException, ParseException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		params.put("advertResourcePlanKey", advertResourcePlanInfo);
		params.put("planCode",request.getParameter("planCode"));
		advertResourcePlanInfo = advResourcePlanService.findAdvertResourcePlan(params);
		json.put("advertResourcePlanInfo", advertResourcePlanInfo);
		IOUtil.writeJSON(json, response.getOutputStream());
	}
	
	/**
	 * 根据设施ID查询资源信息，并根据方案ID，查出该资源在该方案中的使用日期
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @throws IOException
	 */
	public void findAdvertResourceByFacilityIdWithUseDate(HttpServletRequest request, HttpServletResponse response, AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		params.put("advertFacilityKey", advertFacilityInfo);
		params.put("planId",request.getParameter("planId"));
		List<AdvertResourceInfo> advertResourceInfos = advResourcePlanService.findAdvertResourceWithUseDate(params);
		IOUtil.writeJSONArray(JSONArray.fromObject(advertResourceInfos), response.getOutputStream());
	}
	
	/**
	 * 根据资源ID查询资源使用日期
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @throws IOException
	 */
	public void findAdvertResourceUseDate(HttpServletRequest request, HttpServletResponse response, AdvertResourcePlanInfo advertResourcePlanInfo) throws IOException{
		Map params = new HashMap();
		params.put("advertResourcePlanKey", advertResourcePlanInfo);
		List<AdvertResourceInfo> advertResourceInfos = advertFacilityService.findAdvertResourceByFacilityId(params);
		IOUtil.writeJSONArray(JSONArray.fromObject(advertResourceInfos), response.getOutputStream());
	}
	
	/**
	 * 判断日期间隔是否超过13个月
	 * @param useDates
	 * @return true 超过  false 未超过
	 */
	public boolean checkUseDate(String[] useDates){
		List<String> list = new ArrayList<String>();
		for(String str:useDates){
			list.add(str.trim()); 
		}
		if(list.size()<=1){
			return false;
		}
		Collections.sort(list);
		String[] fDate = list.get(0).split("-");
		int fYear = Integer.valueOf(fDate[0]);
		int fMonth = Integer.valueOf(fDate[1]);
		int fDay = Integer.valueOf(fDate[2]);
		
		String[] sDate = list.get(list.size()-1).split("-");
		int sYear = Integer.valueOf(sDate[0]);
		int sMonth = Integer.valueOf(sDate[1]);
		int sDay = Integer.valueOf(sDate[2]);
		if(fYear-2 > sYear || fYear + 2 < sYear){
			return true;
		}
		
		if(fYear - 2 == sYear && fYear > sYear){
			if(fMonth + 12 + 12 - sMonth > 13){
				return true;
			}
			if(fMonth + 12 + 12 - sMonth > 13 && sDay > fDay){
				return true;
			}
		}
		
		if(fYear + 2 == sYear && fYear < sYear){
			if(12 - fMonth + 12 + sMonth > 13){
				return true;
			}
			if(12 - fMonth + 12 + sMonth == 13 && sDay > fDay){
				return true;
			}
		}
		
		if(fYear - 1 == sYear && fYear > sYear){
			if(fMonth + 12 - sMonth > 13){
				return true;
			}
			if(fMonth + 12 - sMonth == 13 && sDay > fDay){
				return true;
			}
		}
		if(fYear + 1 == sYear && fYear < sYear){
			if(12 - fMonth + sMonth > 13){
				return true;
			}
			if(12 - fMonth + sMonth == 13 && sDay > fDay){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 图标方式新增方案
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @throws IOException
	 */
	public ModelAndView addAdvertPlanByPic(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo) throws IOException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		params.put("advertPlanKey", advertPlanInfo);
		AdvertResourcePlanInfo advertResourcePlanInfo = new AdvertResourcePlanInfo();
		String useDate = request.getParameter("useDate");
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		
		
		//判断日期重复
		if(StringUtils.isNotBlank(useDate)){
			int result = 0;
			//useDate = StringUtil.replaceBlank(useDate.trim());
			//String[] useDates = useDate.split("<br/>");
			String[] useDates = useDate.split(",");
			//String[] str = useDate.split("<br/>");
			String[] str = useDate.split(",");
			for(int i=0;i<useDates.length;i++){
				int count = 0;
				for(int j=0;j<str.length;j++){
					if(useDates[i].contains(str[j])){
						count++;
					}
				}
				if(count>1){
					result++;
				}
			}
			if(result>0){
				json.put("code_fail", Constant.ADVERT_RESOURCE_USE_DATE_REPEAT);
				IOUtil.writeJSON(json, response.getOutputStream());
				return null;
			}
			
			if(checkUseDate(useDates)){
				json.put("code_fail", Constant.ADVERT_RESOURCE_USE_DATE_OUT);
				IOUtil.writeJSON(json, response.getOutputStream());
				return null;
			}
			advertResourcePlanInfo.setUseDate(useDate);
		}else{
			json.put("code_fail", Constant.ADVERT_RESOURCE_USE_DATE_IS_NULL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		
		if(StringUtils.isNotBlank(request.getParameter("resourceId"))){
			advertResourcePlanInfo.setResourceId(request.getParameter("resourceId"));
		}else{
			json.put("code_fail", Constant.ADVERT_RESOURCE_IS_NULL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		BigDecimal currentPricePer = StringUtils.isNotBlank(request.getParameter("currentPricePer"))
		?new BigDecimal(request.getParameter("currentPricePer")):new BigDecimal(0);
		advertResourcePlanInfo.setCurrentPrice(currentPricePer);
		advertResourcePlanInfo.setCostPrice(new BigDecimal(request.getParameter("costPricePer")));
		advertResourcePlanInfo.setFacilityCode(request.getParameter("facilityCode"));
		advertResourcePlanInfo.setFacilityType(Long.valueOf(request.getParameter("facilityType")));
		
		params.put("advertResourcePlanKey", advertResourcePlanInfo);
		//查找出资源状态
		//long resourceStatus = advResourcePlanService.findAdvResourceStatus(params);
		//存在有2种可能
		if(advResourcePlanService.checkAdvertPlanCode(params)){
			//当前页继续配置方案
			if(advResourcePlanService.checkAdvertPlanCodeAndName(params)){
				//资源在使用中和预定中需判断资源日期是否被占用，没有被占用则允许继续占用
				String result = advResourcePlanService.checkResourceUseDate(params);
				if(StringUtils.isNotBlank(result)){
					json.put("useMsg", result);
				}else{
					advResourcePlanService.addAdvResourcePlanByPicType2(params);
					json.put("success", Constant.ADVERT_RESOURCE_PLAN_SAVE_SUCCESS);
				}
			}else{
				json.put("code_fail", Constant.ADVERT_PLAN_CODE_EXSIT);
			}
		}else{
			if(advResourcePlanService.checkAdvertPlanName(params)){
				json.put("code_fail", Constant.ADVERT_PLAN_NAME_EXSIT);
			}else{
				//资源在使用中和预定中需判断资源日期是否被占用，没有被占用则允许继续占用
				String result = advResourcePlanService.checkResourceUseDate(params);
				if(StringUtils.isNotBlank(result)){
					json.put("useMsg", result);
				}else{
					advResourcePlanService.addAdvResourcePlanByPic(params);
					json.put("success", Constant.ADVERT_RESOURCE_PLAN_SAVE_SUCCESS);
				}
			}
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 图标方式维护方案
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @throws IOException
	 */
	public ModelAndView updateAdvertPlanByPic(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo) throws IOException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		params.put("advertPlanKey", advertPlanInfo);
		AdvertResourcePlanInfo advertResourcePlanInfo = new AdvertResourcePlanInfo();
		String useDate = request.getParameter("useDate");
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		//判断日期重复
		if(StringUtils.isNotBlank(useDate)){
			int result = 0;
			useDate = StringUtil.replaceBlank(useDate.trim());
			String[] useDates = useDate.split(",");
			String[] str = useDate.split(",");
			for(int i=0;i<useDates.length;i++){
				int count = 0;
				for(int j=0;j<str.length;j++){
					if(useDates[i].contains(str[j])){
						count++;
					}
				}
				if(count>1){
					result++;
				}
			}
			if(result>0){
				json.put("code_fail", Constant.ADVERT_RESOURCE_USE_DATE_REPEAT);
				IOUtil.writeJSON(json, response.getOutputStream());
				return null;
			}
			if(checkUseDate(useDates)){
				json.put("code_fail", Constant.ADVERT_RESOURCE_USE_DATE_OUT);
				IOUtil.writeJSON(json, response.getOutputStream());
				return null;
			}
			advertResourcePlanInfo.setUseDate(useDate);
		}else{
			json.put("code_fail", Constant.ADVERT_RESOURCE_USE_DATE_IS_NULL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		
		if(StringUtils.isNotBlank(request.getParameter("resourceId"))){
			advertResourcePlanInfo.setResourceId(request.getParameter("resourceId"));
		}else{
			json.put("code_fail", Constant.ADVERT_RESOURCE_IS_NULL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		BigDecimal currentPricePer = StringUtils.isNotBlank(request.getParameter("currentPricePer"))
		?new BigDecimal(request.getParameter("currentPricePer")):new BigDecimal(0);
		advertResourcePlanInfo.setCurrentPrice(currentPricePer);
		advertResourcePlanInfo.setCostPrice(new BigDecimal(request.getParameter("costPricePer")));
		advertResourcePlanInfo.setFacilityCode(request.getParameter("facilityCode"));
		advertResourcePlanInfo.setFacilityType(Long.valueOf(request.getParameter("facilityType")));
		params.put("advertResourcePlanKey", advertResourcePlanInfo);
		//查找出资源状态
		//long resourceStatus = advResourcePlanService.findAdvResourceStatus(params);
		
		if(advResourcePlanService.checkAdvertPlanCode(params)){
			//当前页继续配置方案
			if(advertPlanInfo.getStatus()==Dictionary.PLAN_STATUS_SHENPIWEITONGGUO
					|| advertPlanInfo.getStatus()==Dictionary.PLAN_STATUS_YSX){
				long  advResourcePlanNumber = AdvertCodeController.getAdvertCode(AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX, AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_TYPE);
				String finaladvResourcePlancodeNumber = AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX + "-" + NumberFormatUtil.advAgentNumberFormat(advResourcePlanNumber);
				params.put("newPlanCode", finaladvResourcePlancodeNumber);
				String result = advResourcePlanService.checkResourceUseDate(params);
				if(StringUtils.isNotBlank(result)){
					json.put("useMsg", result);
				}else{
					advResourcePlanService.updateAdvResourcePlanByPic(params);
					json.put("success", Constant.ADVERT_RESOURCE_PLAN_SAVE_SUCCESS);
				}
			}else{
				if(!advResourcePlanService.checkAdvertPlanNameByPlanId(params)){
					//需要先判断资源是否是被该方案占用，如果是则仍需判断该资源的其他使用日期是否被其他方案占用
					//没有被占用则允许继续占用
					String result = advResourcePlanService.checkResourceUseDate(params);
					if(StringUtils.isNotBlank(result)){
						json.put("useMsg", result);
					}else{
						advResourcePlanService.updateAdvResourcePlanByPic(params);
						json.put("success", Constant.ADVERT_RESOURCE_PLAN_SAVE_SUCCESS);
					}
				}else{
					json.put("code_fail", Constant.ADVERT_PLAN_NAME_EXSIT);
				}
			}
		}else{
			json.put("code_fail", Constant.ADVERT_PLAN_CODE_IS_NOT_EXSIT);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 进入维护方案详情页面
	 * @param request
	 * @param response
	 * @param advertPlanInfo
	 * @return
	 */
	public ModelAndView queryAdvResourcePlanInfoDetail(HttpServletRequest request,HttpServletResponse response,AdvertPlanInfo advertPlanInfo){
		Map modelMap = new HashMap();
		Map resourcestateMap = new HashMap();
		AdvertPlanInfo aInfo = new AdvertPlanInfo();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		resourcestateMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		aInfo.setPlanId(request.getParameter("planId"));
		aInfo.setPlanCode(advertPlanInfo.getPlanCode());
		String flag = request.getParameter("flag");
		resourcestateMap.put("advertPlanKeyByPlanId", aInfo);
		advResourcePlanService.updateAdvPlanResourceStateByPlanId(resourcestateMap);
		modelMap.put("planId", advertPlanInfo.getPlanId());
		modelMap.put("flag", flag);
		final String isEdit = "yes";
		//点滴编辑时往临时表插入数据，对临时表进行操作。防止没有真正保存而对正式表进行操作
		Map advertResourcePlanInfoMap = new HashMap();
		AdvertResourcePlanInfo advertResourcePlanInfo = new AdvertResourcePlanInfo();
		advertResourcePlanInfo.setPlanId(advertPlanInfo.getPlanId());
		advertResourcePlanInfo.setPlanCode(advertPlanInfo.getPlanCode());
		advertResourcePlanInfoMap.put("AdvResourceInfoByPlanIdKey", advertResourcePlanInfo);
//		List<OrgModel> orgModels = initOrgModels(request);
		advertResourcePlanInfoMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		
		//按照方案ID 查询中间表得到list
		List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(advertResourcePlanInfoMap);
		if(Utils.isNotEmpty(advertResourcePlanInfosList)){
			advertResourcePlanInfoMap.put("listKey", advertResourcePlanInfosList);
			advertResourcePlanInfoMap.put("isEdit",isEdit);
			advResourcePlanService.saveResourcePlanToResourcePlanTemp(advertResourcePlanInfoMap);
		}
		
		return new ModelAndView(VIEW_QUERY_ADVRESOURCEPLANINFO_DETAIL_SUCCESS,modelMap);
	}
	
	/**
	 * 进入维护方案详情页面--图片方式
	 * @param request
	 * @param response
	 * @param advertPlanInfo
	 * @return
	 */
	public ModelAndView showUpdateAdvertResourcePlanDetailByPic(HttpServletRequest request,HttpServletResponse response,AdvertPlanInfo advertPlanInfo){
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		params.put("advertPlanKey", advertPlanInfo);
		advertPlanInfo = advResourcePlanService.findAdvertPlanInfo(params);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
		
		BigDecimal discount = advResourcePlanService.findDiscountByName(params);
		if(discount==null)discount=new BigDecimal(10);
		// 0 无需审核  1 需要审核
		String isOut = "0";
		if(advertPlanInfo.getPrice().compareTo(
				advertPlanInfo.getCostPrice().divide(new BigDecimal(10)).multiply(advertPlanInfo.getDiscount()))==-1){
			isOut = "1";
		}
		String regionId=request.getParameter("regionId");
		String cityId=request.getParameter("cityId");
		String cinemaId=request.getParameter("cinemaId");
		String falicitytypeId=request.getParameter("falicitytypeId");
		String readonlyFlag=request.getParameter("readonlyFlag");
	
		OrgModel orgModel = this.getModelsByUserCode(request, orgModels);
		
		UserTab user = avertDiscountService.findUserTabByUserId(JudgementAuthority.getUserInfo(request).getId());
		
		Map modelMap = new HashMap();
		modelMap.put("advertPlanInfo", advertPlanInfo);
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("advertFacilityTypeJSON", JSONArray.fromObject(advertFacilityType));
		modelMap.put("isOut",isOut);
		modelMap.put("discount", discount);
		modelMap.put("orgmodel",orgModel);
		
		modelMap.put("level",user.getSelectresource());//判断是否可以使用特殊资源
		
		modelMap.put("regionId", regionId);
		modelMap.put("cityId", cityId);
		modelMap.put("cinemaId", cinemaId);
		modelMap.put("falicitytypeId", falicitytypeId);
		modelMap.put("readonlyFlag", readonlyFlag);
		return new ModelAndView(VIEW_QUERY_ADVRESOURCEPLANINFO_DETAIL_BY_PIC_SUCCESS,modelMap);
	}
	
	/**
	 * 维护方案--图片方式
	 * 查看资源排期
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @throws IOException 
	 */
	public void addInList(HttpServletRequest request,HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("cinemaId"));
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("regionId"));
		
		advertFacilityInfo.setOrgRegion(orgRegionInfo);
		advertFacilityInfo.setOrgCity(orgCityInfo);
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);
		params.put("advertFacilityKey", advertFacilityInfo);
		params.put("planId", request.getParameter("planId"));
		List<AdvertResourceBean> resourceBeans =  advResourcePlanService.findAdvertResource(params);
		JSONArray jsonArray = JSONArray.fromObject(resourceBeans);
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}
	
	
	/**
	 * @Title: queryAdvPerformenceList
	 * @Description: TODO(执行确认单-左边菜单请求得到的确认单列表方法)
	 * @param @param request
	 * @param @param response
	 * @param @param advertPlanInfo
	 * @param @return
	 * @param @throws GenericRuntimeException
	 * @return ModelAndView
	 * @author yong_jiang
	 * @date 2012-4-12 上午09:19:33
	 */
	public ModelAndView queryAdvPerformenceList(HttpServletRequest request,HttpServletResponse response,PerformenceListInfo performenceListInfo)throws GenericRuntimeException {
		Map formenceMap = new HashMap();
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
	
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);

		
		//封装查询
		PerformenceParamBean bean = setParamsBean(request);
		formenceMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		formenceMap.put("performenceListInfokey", performenceListInfo);

		performenceListInfo.setPageSize(GlobalConstant.PAGE_SIZE); // 10条数据
		performenceListInfo.setPageNavigationURL(CommUtil.getUrl(request));
		// 分页查询
		Page<PerformenceListInfo, Object[]> pageResult = null;
		try {
			pageResult = performenceListService.findAPerformenceListnInfos(formenceMap, bean);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		if(Utils.isNotEmpty(pageResult))
		pageResult.setPageNavigationURL(performenceListInfo.getPageNavigationURL());
		// 页面数据显示
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgmodel", orgModel);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("bean", bean);
		setQueryParamesJson(request, response, modelMap);
		return new ModelAndView(VIEW_REQUEST_ENTERORDERSLIST_SUCCESS, modelMap);
	}
	
	//封装查询条件
	private PerformenceParamBean setParamsBean(HttpServletRequest request) {
		// 获取界面的参数
		PerformenceParamBean bean = null;
		String regionname = request.getParameter("orgRegion.regionId");
		String cityname = request.getParameter("orgCity.cityId");
		String cinemaname = request.getParameter("orgCinema.cinemaId");
		String enterorder = request.getParameter("enterorder");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String enterorders = request.getParameter("enterorder");
		if (Utils.isNotEmpty(regionname) || Utils.isNotEmpty(cityname)
				|| Utils.isNotEmpty(cinemaname) || Utils.isNotEmpty

				(enterorder) || Utils.isNotEmpty(starttime)
				|| Utils.isNotEmpty(endtime) || Utils.isNotEmpty(enterorders)) {
			bean = new PerformenceParamBean();
		}
		if (!StringUtils.isEmpty(regionname))
			bean.setRegionid(regionname);

		if (!StringUtils.isEmpty(cinemaname))
			bean.setCinemaid(cinemaname);

		if (!StringUtils.isEmpty(cityname))
			bean.setCityid(cityname);

		if (!StringUtils.isEmpty(enterorders))
			bean.setEnterorders(Long.parseLong(enterorders));

		if (!StringUtils.isEmpty(starttime))
			try {
				bean.setParamDownStarttimes(DateUtil.stringToDate(starttime));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		if (!StringUtils.isEmpty(endtime))
			try {
				bean.setParamDownEndtimes(DateUtil.stringToDate(endtime));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		return bean;
	}

	
	
	/***
	 * 
	 * @Title:       queryPerformenceListInfoDetail 
	 * @Description: TODO(执行单列表中详情操作-查询执行单方法) 
	 * @param        @param request
	 * @param        @param response
	 * @param        @param performenceListInfo
	 * @param        @return
	 * @param        @throws Exception    
	 * @return       ModelAndView    
	 * @author       yong_jiang  
	 * @date         2012-5-11 上午11:11:58 
	 * @throws
	 */
	public ModelAndView queryPerformenceListInfoDetail(HttpServletRequest request, HttpServletResponse response,PerformenceListInfo performenceListInfo) throws Exception {
		Map performenceListMap = new HashMap();
		 List<OrgModel> orgModels = initOrgModels(request);
		performenceListMap.put("keyperformenceListInfo", performenceListInfo);
		// 获取confirm-performSingle-list.jsp界面参数
		String regionname = request.getParameter("regionName");
		if(IOUtil.getEncoding(regionname).equalsIgnoreCase("iso-8859-1"))
		regionname=new String(regionname.getBytes("iso-8859-1"),"utf-8"); //避免乱码问题
		String cityname = request.getParameter("cityName");
		if(IOUtil.getEncoding(cityname).equalsIgnoreCase("iso-8859-1"))
		cityname=new String(cityname.getBytes("iso-8859-1"),"utf-8"); //避免乱码问题
		String cinemaname = request.getParameter("cinemaName");
		if(IOUtil.getEncoding(cinemaname).equalsIgnoreCase("iso-8859-1"))
		cinemaname=new String(cinemaname.getBytes("iso-8859-1"),"utf-8"); //避免乱码问题
		String performenceCode = request.getParameter("performenceCode");
		String documentsOfferType = request.getParameter("documentsOfferType");
		String performenceStatus = request.getParameter("performenceStatus");
		String customerContractId=request.getParameter("customerContractId");
		
		//初始化监播证据提供方式
		Map map=new HashMap();
		DataDictionaryInfo  infosd=new DataDictionaryInfo();
		infosd.setFDataId(Dictionary.DOCUMENTS_OFFER_TYPE);
		map.put("dataDictionaryInfoKey",infosd);
		List<DataDictionaryInfo> listdate=dictionaryService.findDataDictionarys(map);
		
		//初始设施资源名称
		List<DataDictionaryInfo> advertFacilityType = initAllFacilityType();

		//广告合同查询
		AdvertContractInfo newcontract = selectAdvContract(customerContractId);
		
		//获取广告合同对象的相关参数以便封装实体传入下个界面
		String advertContent =newcontract.getAdvertContent();
		String advertType = newcontract.getAdvertType();
		String advertForm = newcontract.getAdvertForm();
		String planId=newcontract.getAdvertPlan().getPlanId();
		
		//根据performenceCode查询执行单，然后级联出所有相关数据
		List<AdvertResourceInfo> resouceinfoList = getAdvertResourceInfoListBycode(performenceCode);

		// 组装发布影城
		String fbcinemaname = regionname + "-" + cityname + "-" + cinemaname;
		// 封装参数到AlertSourceParamBean对象中
		AlertSourceParamBean alertSourceBean = filltoAlterSourceBean(map,
				infosd, performenceCode, documentsOfferType, advertContent,
				advertType, advertForm, fbcinemaname);

		//处理advertResourceInfolist,给T_ADVERT_RESOURCE_PLAN中的usedate记录到AdvertResourceInfo中来,這里需要用到planid，
		//所以之前需查询到广告合同对象,是以便這里查询;
		List<AdvertResourceInfo> advertResourceInfolist = dealAdcertResourcelist(
				planId, resouceinfoList);
		
		//封装参数备注
		PerformenceListInfo info=new PerformenceListInfo();
		info.setPerformenceCode(performenceCode);
		Map m2=new HashMap();
		m2.put("keyinfo", info);
		List<PerformenceListInfo> listinfo=performenceListService.getPerformenceListInfoBycode(m2);
		String remark="";
		if(Utils.isNotEmpty(listinfo)){
			remark=listinfo.get(0).getRemark();
		}
		
		performenceListMap.put("advertResourceInfolist", advertResourceInfolist);
		//performenceListMap.put("advertResourceInfolistJSON",JSONArray.fromObject(advertResourceInfolist,IOUtil.getConfigForResoruce()));
		performenceListMap.put("alertSourceBean", alertSourceBean);
		//performenceListMap.put("alertSourceBeanJSON", JSONArray.fromObject(alertSourceBean));
		performenceListMap.put("listdate", listdate);
		performenceListMap.put("advertFacilityType", advertFacilityType);
		//performenceListMap.put("advertFacilityTypeJSON", JSONArray.fromObject(advertFacilityType));
		performenceListMap.put("remark", remark);
		performenceListMap.put("signUser", listinfo.get(0).getSignUser());
		performenceListMap.put("signDate", listinfo.get(0).getPerformanceTime());
		performenceListMap.put("performenceCode", performenceCode);
		performenceListMap.put("customerContractId", customerContractId);
		
		String logsString = "[ performenceCode="+performenceCode+",AdverContent="+alertSourceBean.getAdverContent()+",AdvertType="+alertSourceBean.getAdvertType()+",AdvertStyle="+alertSourceBean.getAdvertStyle()+
		",fbcinemaname="+fbcinemaname+",documentsOfferType="+documentsOfferType+"  ]";
		CommonWriteLog4j.writeLog4j(OperLogService.getInstance(), orgModels, BUSINESS_TYPE.WRITE, PerformenceListLogicImpl.class, LOG_TYPE.OPER_NORMAL, 
				logsString, null);
		
		// 根据执行单状态跳转到相应的页面
		if ("1801".equals(performenceStatus)) {// 已确认
			return new ModelAndView(VIEW_REQUEST_ENTERORDERSLIST_HASPERIED__SUCCESS,performenceListMap);

		} else if ("1802".equals(performenceStatus)) {//未确认

			return new ModelAndView(VIEW_REQUEST_ENTERORDERSLISTDETAIL__SUCCESS,performenceListMap);

		}
		
		
		// 默认让他跳转到已确认;
		return new ModelAndView(VIEW_REQUEST_ENTERORDERSLIST_HASPERIED__SUCCESS,performenceListMap);

	}
	
	public ModelAndView findPrintPage(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		String performenceCode = request.getParameter("performenceCode");
		String customerContractId=request.getParameter("customerContractId");
		
		//初始设施资源名称
		List<DataDictionaryInfo> advertFacilityType = initAllFacilityType();
		
		//广告合同查询
		AdvertContractInfo newcontract = selectAdvContract(customerContractId);
		String planId=newcontract.getAdvertPlan().getPlanId();
		//根据performenceCode查询执行单，然后级联出所有相关数据
		List<AdvertResourceInfo> resouceinfoList = getAdvertResourceInfoListBycode(performenceCode);
		List<AdvertResourceInfo> advertResourceInfolist = dealAdcertResourcelist(
				planId, resouceinfoList);
		
		//封装参数备注
		PerformenceListInfo info=new PerformenceListInfo();
		info.setPerformenceCode(performenceCode);
		Map m2=new HashMap();
		m2.put("keyinfo", info);
		List<PerformenceListInfo> listinfo=performenceListService.getPerformenceListInfoBycode(m2);
		String remark="";
		if(Utils.isNotEmpty(listinfo)){
			remark=listinfo.get(0).getRemark();
		}
		
		Map modelMap = new HashMap();
		String releaseCinema = request.getParameter("releaseCinema");
		if(IOUtil.getEncoding(releaseCinema).equalsIgnoreCase("iso-8859-1"))
		releaseCinema = new String(releaseCinema.getBytes("iso-8859-1"),"utf-8"); //避免乱码问题
		
		String advertStyle = request.getParameter("advertStyle");
		if(IOUtil.getEncoding(advertStyle).equalsIgnoreCase("iso-8859-1"))
		advertStyle = new String(advertStyle.getBytes("iso-8859-1"),"utf-8"); //避免乱码问题
		
		String advertType = request.getParameter("advertType");
		if(IOUtil.getEncoding(advertType).equalsIgnoreCase("iso-8859-1"))
		advertType = new String(advertType.getBytes("iso-8859-1"),"utf-8"); //避免乱码问题
		
		String advertContent = request.getParameter("advertContent");
		if(IOUtil.getEncoding(advertContent).equalsIgnoreCase("iso-8859-1"))
		advertContent = new String(advertContent.getBytes("iso-8859-1"),"utf-8"); //避免乱码问题
		
		String document = request.getParameter("document");
		if(IOUtil.getEncoding(document).equalsIgnoreCase("iso-8859-1"))
		document = new String(document.getBytes("iso-8859-1"),"utf-8"); //避免乱码问题
		
		modelMap.put("advertResourceInfolist",advertResourceInfolist);
		modelMap.put("releaseCinema",releaseCinema);
		modelMap.put("advertStyle",advertStyle);
		modelMap.put("advertType",advertType);
		modelMap.put("advertContent",advertContent);
		modelMap.put("document",document);
		modelMap.put("advertFacilityType",advertFacilityType);
		modelMap.put("remark", remark);
		modelMap.put("signUser", request.getParameter("signUser"));
		modelMap.put("signDate", request.getParameter("signDate"));
		modelMap.put("performenceCode",performenceCode);
		return new ModelAndView(VIEW_FINDPRINTPAGE_SUCCESS,modelMap);
	}
	
	//处理advertResourceInfolist
	private List<AdvertResourceInfo> dealAdcertResourcelist(String planId,
			List<AdvertResourceInfo> resouceinfoList) {
		Map dealmapParams=new HashMap();
		dealmapParams.put("planIdkey", planId);
		dealmapParams.put("listkey", resouceinfoList);
		List<AdvertResourceInfo> advertResourceInfolist= advertFacilityService.dealAdvertResourcesList(dealmapParams);
		return advertResourceInfolist;
	}
	//初始化广告设施类型
	private List<DataDictionaryInfo> initAllFacilityType() {
		DataDictionaryInfo data = new DataDictionaryInfo();
		Map params2=new HashMap();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params2.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params2);
		return advertFacilityType;
	}
	
	//根据执行单编号返回所有使用的资源
	private List<AdvertResourceInfo> getAdvertResourceInfoListBycode(
			String performenceCode) {
		PerformenceListInfo info=new PerformenceListInfo();
		info.setPerformenceCode(performenceCode);
		Map m=new HashMap();
		m.put("keyinfo", info);
		List<PerformenceListInfo> listinfo=performenceListService.getPerformenceListInfoBycode(m);
		//声明封装参数，最终用来放置PerformenceListInfo中的AdvertResourceInfo对象
		List<AdvertResourceInfo> resouceinfoList=new ArrayList();
		for(PerformenceListInfo performence:listinfo){
			AdvertResourceInfo resouceinfo=new AdvertResourceInfo();
			DozerMapperSingleton.getInstance().map(performence.getAdvertResource(), resouceinfo);
			resouceinfoList.add(resouceinfo);
		}
		return resouceinfoList;
	}
	
	//封装AlertSourceParamBean中的所有参数,(AlertSourceParamBean，存放一些界面的值)
	private AlertSourceParamBean filltoAlterSourceBean(Map map,
			DataDictionaryInfo infosd, String performenceCode,
			String documentsOfferType, String advertContent, String advertType,
			String advertForm, String fbcinemaname) {
		AlertSourceParamBean alertSourceBean = new AlertSourceParamBean();
		alertSourceBean.setReleaseCinema(fbcinemaname);
		alertSourceBean.setPefomerceCode(performenceCode);
		if(Utils.isNotEmpty(documentsOfferType)){
			alertSourceBean.setDocumentsOfferType(Long.parseLong(documentsOfferType));
		}
		//转换合同成name显示
		setAdvertTypes(map, infosd, advertType, alertSourceBean);
		alertSourceBean.setPerformenceCode(performenceCode);
		alertSourceBean.setAdverContent(advertContent);
		alertSourceBean.setAdvertStyle(advertForm);
		return alertSourceBean;
	}
	
	//转换合同类型
	private void setAdvertTypes(Map map, DataDictionaryInfo infosd,
			String advertType, AlertSourceParamBean alertSourceBean) {
		infosd.setFDataId(Dictionary.ADVERT_TYPE);
		map.put("dataDictionaryInfoKey",infosd);
		List<DataDictionaryInfo> infoAtlist=dictionaryService.findDataDictionarys(map);
		if(Utils.isNotEmpty(advertType)){
			for(DataDictionaryInfo infod:infoAtlist){
			if(Long.parseLong(advertType)==infod.getSDataId()){
				alertSourceBean.setAdvertType(infod.getSDataName());
				break;
			}
		  }
		}
	}
	
	
	/**
	 * @Title:       selectAdvContract 
	 * @Description: TODO(广告合同查询) 
	 * @param        @param customerContractId
	 * @param        @return    
	 * @return       AdvertContractInfo    
	 * @author       yong_jiang  
	 * @date         2012-5-15 下午08:28:26 
	 * @throws
	 */
	private AdvertContractInfo selectAdvContract(String customerContractId) {
		AdvertContractInfo aContractInfo = new AdvertContractInfo();
		aContractInfo.setCustomerContractId(customerContractId);
		Map params = new HashMap();
		params.put("contractIdKey", aContractInfo);
		AdvertContractInfo newcontract=null;
		newcontract=advCustomerService.queryAdvContract(params);
		return newcontract;
	}

	/**
	 * @Description: TODO(确认执行单模块-更新执行单状态)
	 * @Title:       updatePeformenceStatus 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param performenceListInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       yong_jiang  
	 * @date         2012-5-7 下午05:37:18
	 * @throws
	 */
	public ModelAndView updatePeformenceStatus(HttpServletRequest request,HttpServletResponse response,PerformenceListInfo performenceListInfo) throws Exception {
		Map performenceListMap = new HashMap();
		JSONObject json = new JSONObject();
		List<OrgModel> orgModels = initOrgModels(request);
		performenceListMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		performenceListMap.put("keyperformenceListInfo", performenceListInfo);
		
		boolean updateTag = performenceListService.updatePefermenceStatus(performenceListMap);
		if (updateTag) {
			json.put("success", Constant.ADVERT_PEFORMENCE_SUCCESS);
		} else {
			json.put("failure", Constant.ADVERT_PEFORMENCE_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		if(logger.isInfoEnabled()){
			logger.info("[用户:xxx]"+" || "+"进行了执行确认单操作  || 执行的具体类和方法"+"||"+"["+AdvInputController.class.getName()+".updatePeformenceStatus()"+"]");
		}
		return null;
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
		json.put("msg_code", Constant.FILE_UPLOAD_FAIL);
		String result = FileUpLoadUtil.fileUpload(request,FileUpLoadUtil.DOCUMENT_FILE);
		if(FileUpLoadUtil.AGENT_CONTRACT_IMAGE_RESULT.equals(result)){
			json.put("msg_code", Constant.NOT_PDF);
		}else if(FileUpLoadUtil.FILE_UPLOAD_SIZE_RESULT.equals(result)){
			json.put("msg_code", Constant.FILE_UPLOAD_SIZE_IS_ZERO);
		}
		if(FileUpLoadUtil.DOCUMENT_IMAGE_SIZE_RESULT.equals(result)){
			json.put("msg_code", Constant.FILE_UPLOAD_TOO_LARGES5);
		}

		String performenceCode = request.getParameter("performenceCode");
		if(result.contains("document")){
			try {
				json.put("url", result);
				json.put("msg_code", Constant.FILE_UPLOAD_SUCCESS);
				updateDucmentstatus(request,response,performenceCode,result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
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
	 * @Description: TODO(更新监播文件的状态)
	 * @Title:       updateDucmentstatus 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param performenceid
	 * @param 		 @param result
	 * @param 		 @throws Exception
	 * @return       void
	 * @author       yong_jiang  
	 * @date         2012-5-7 下午05:46:20
	 * @throws
	 */
	public void updateDucmentstatus(HttpServletRequest request,HttpServletResponse response, String performenceCode,String result) throws Exception {
		//JSONObject json = new JSONObject();
		Map performenceListMap = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		performenceListMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		performenceListMap.put("url", result);
		performenceListMap.put("performencecode", performenceCode);
		performenceListService.updateDucmentstatus(performenceListMap);
		 if(logger.isInfoEnabled()){
			logger.info("[用户:xxx]"+" || "+"进行了更新监控对象操作  || 执行的具体类和方法"+"||"+"["+AdvInputController.class.getName()+".updateDucmentstatus()"+"]");
		 }
		 //json.put("success", "success");
		 //IOUtil.writeJSON(json, response.getOutputStream());
	}
	
	/**
	 * @Description: TODO(删除资源信息)
	 * @Title:       deleteResource 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertResourcePlanTempInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-10-16 下午04:19:45
	 * @throws
	 */
	public ModelAndView deleteResource(HttpServletRequest request, HttpServletResponse response, AdvertResourcePlanTempInfo advertResourcePlanTempInfo)throws Exception {
		Map params = new HashMap(); 
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		
		params.put("advertResourcePlanTempInfokey", advertResourcePlanTempInfo);
		
		JSONObject json = new JSONObject();
//		UserTab userTab = new UserTab();
//		userTab.setId(userVo.getId());
//		userManagerService.deleteUserInfoByUserId(userTab);
		boolean res = advResourcePlanService.deleteResource(params);
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
	 * @Description: TODO(删除资源信息)
	 * @Title:       deleteResource 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertResourcePlanTempInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-10-16 下午04:19:45
	 * @throws
	 */
	public ModelAndView deleteResourcePlan(HttpServletRequest request, HttpServletResponse response, AdvertResourcePlanInfo advertResourcePlanInfo)throws Exception {
		Map params = new HashMap(); 
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		
		params.put("advertResourcePlanInfokey", advertResourcePlanInfo);
//		AdvertResourcePlanTempInfo advertResourcePlanTempInfo = new AdvertResourcePlanTempInfo();
//		String resourceCode = request.getParameter("resourceCode");
//		String planCode = request.getParameter("planCode");
//		advertResourcePlanTempInfo.setResourceCode(resourceCode);
//		advertResourcePlanTempInfo.setPlanCode(planCode);
		
		
		JSONObject json = new JSONObject();
//		UserTab userTab = new UserTab();
//		userTab.setId(userVo.getId());
//		userManagerService.deleteUserInfoByUserId(userTab);
		boolean res = advResourcePlanService.deleteResourcePlan(params);
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
	 * @Description: TODO(新增方案-按照方案id查询该方案下面的所有资源和资源的使用日期)
	 * @Title:       queryAdvAllResourceUseDateByPlanIdadd 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertResourcePlanInfo
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-25 下午06:45:37
	 */
	public ModelAndView queryAdvAllResourceUseDateByPlanIdadd(HttpServletRequest request, HttpServletResponse response, AdvertResourcePlanInfo advertResourcePlanInfo ){
		Map params = new HashMap();
		String plancode = request.getParameter("planCode");
		params.put("planCodeKey", plancode);
	 	//Map advplan2Map = new HashMap();
		//AdvertPlanInfo  api =  new AdvertPlanInfo();
		//api.setPlanCode(plancode);
		//advplan2Map.put("advertPlanKey", api);
		//advplan2Map.put("planCodeKey", plancode);
		//String planId = "";
		//if (StringUtils.isNotBlank(plancode)) {
		//	AdvertPlanInfo  advPlanInfoByPlanCode = advResourcePlanService.findAdvertPlanInfoByCode(advplan2Map);//更具方案code查询方案详情信息
		//	if (advPlanInfoByPlanCode != null) {
		//		planId = advPlanInfoByPlanCode.getPlanId();
		//	}
		//}
		//System.out.println("plancode==" + plancode); 
		//String planId = "";
		//advertResourcePlanInfo.setPlanId(planId);
		//paramsAdvresourcePlaninfoForMainTianMap.put("AdvResourceInfoByPlanIdKey", advertResourcePlanInfo);
		//按照方案ID 查询中间表得到list
		//List<OrgModel> orgModels = initOrgModels(request);
		//paramsAdvresourcePlaninfoForMainTianMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		
		List<AdvertResourcePlanTempInfo>  advertResourcePlanTempInfosList = advResourcePlanService.findAdvertResourcePlanTempsByPlanCode(params);
		//Map advplanMap = new HashMap();
		//AdvertPlanInfo  apio =  new AdvertPlanInfo();
		//apio.setPlanId(planId);
		//advplanMap.put("advertPlanKey", apio);
		//根据方案ID，查找方案详细信息-得到备注
		//String remark = "";
		//if (StringUtils.isNotBlank(planId)) {
		//	AdvertPlanInfo  advPlanInfoByPlanId = advResourcePlanService.findAdvertPlanInfo(advplanMap);
		//	if (advPlanInfoByPlanId != null) {
		//		remark = advPlanInfoByPlanId.getRemark();
		//	}
		//}
		
		
		boolean isNull = getResourceDate();
		
		boolean isSetDiscount = getDiscount(request);
		DataDictionaryInfo data = new DataDictionaryInfo();
		
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
		Map modelMap = new HashMap();
		modelMap.put("advresourceUseDateListadd", advertResourcePlanTempInfosList);
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("resourDate", isNull);
		modelMap.put("isSetDiscount", isSetDiscount);
		
		//modelMap.put("remark", remark);
		return new ModelAndView(VIEW_ADD_ADVPLAN_RESOURCEUASDATE_SUCCESS,modelMap);
	}
	
	private boolean getResourceDate(){
		
		AdvertDateInfo advertDateInfo = new AdvertDateInfo();
		List<AdvertDateInfo> advertDateInfoList = advResourcePlanService.advResourDateSet();
		if(!advertDateInfoList.isEmpty()){
			advertDateInfo = advertDateInfoList.get(0);
		}
		Long resourDate = advertDateInfo.getResourceDate();
		boolean isNull = true;
		if(resourDate==null){
			isNull = false;
		}
		return isNull;
	}public AdvInputController() {
		// TODO Auto-generated constructor stub
	}
	
	private boolean getDiscount(HttpServletRequest request){
		String userid = JudgementAuthority.getUserInfo(request).getId();
		List<AdvertDiscount> advertDiscountInfos = avertDiscountService.getDiscountsByUserId(userid);
												   
		if(advertDiscountInfos.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 文件上传
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView planFileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		json.put("msg_code",Constant.FILE_UPLOAD_FAIL);
		String result = FileUpLoadUtil.fileUpload(request, FileUpLoadUtil.ADVERT_PLAN_IMAGE);
		if(result.equals(FileUpLoadUtil.TEAMCONT_IMAGE_RESULT)){
			json.put("msg_code",Constant.NOT_PDF);
		}else if(result.equals(FileUpLoadUtil.FILE_UPLOAD_SIZE_RESULT)){
			json.put("msg_code",Constant.FILE_UPLOAD_SIZE_IS_ZERO);
		}else if(result.equals(FileUpLoadUtil.DOCUMENT_IMAGE_SIZE_RESULT)){
			json.put("msg_code",Constant.FILE_UPLOAD_TOO_LARGES5);
		}else{
			json.put("url",result);
			json.put("msg_code",Constant.FILE_UPLOAD_SUCCESS);
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
	 * 列表方式--添加到列表
	 * @param request
	 * @param response
	 * @param advertPlanInfo
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView addAdvertResourcePlanTemp(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo ) throws IOException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		List<AdvertResourcePlanTempInfo> advResourcePlanTempInfos = new ArrayList<AdvertResourcePlanTempInfo>();
		//解析使用日期
		String jsondate = request.getParameter("jsondate");
		String textareaOld=request.getParameter("textareaOld");
		String way=request.getParameter("way");
		String edit = request.getParameter("tag");
		
		JSONArray  jsonArray = JSONArray.fromObject(jsondate);
		for (int i = 0; i < jsonArray.size(); i++) {
			 JSONObject jsonObject = jsonArray.getJSONObject(i);
			 AdvertResourcePlanTempInfo info = new AdvertResourcePlanTempInfo();
			 AdvertResourceInfo ari = new AdvertResourceInfo();
			 ari.setResourceCode(jsonObject.getString("bianhao"));
			 params.put("AdvertResourceInfoCodeKey", ari);
			 AdvertResource are = advResourcePlanService.findAdvertResourceIdByResourceCode(params);
			 if(are != null){
				 info.setResourceId(are.getResourceId()); 
			 }
			 info.setResourceCode(jsonObject.getString("bianhao"));
			 info.setUseDate(jsonObject.getString("shiyongriqi"));
			 info.setUseDate1(jsonObject.getString("shiyongriqi1"));
//			 info.setPlanTempId(jsonObject.getString("planTempId"));
			
			 if(textareaOld!=null && !"".equals(textareaOld)){
				String[] use_date=info.getUseDate().split("<br/>");
				int result=0;
				String result_str="";
				for (int j = 0; j < use_date.length; j++) {
					boolean falg = textareaOld.contains(use_date[j]);
					if(falg){
						result =result+1;
						result_str=use_date[j];
						break;
					}
				}
				if(result>0){
					json.put("failure",Constant.ADVERT_RESOURCE_USE_DATE_REPEAT+",资源编号:"+are.getResourceCode()+"已被日期:"+result_str+"占用");
					IOUtil.writeJSON(json, response.getOutputStream()); 
					return null;
				}else{
					if(checkUseDate(use_date)){
						json.put("code_fail", Constant.ADVERT_RESOURCE_USE_DATE_OUT);
						IOUtil.writeJSON(json, response.getOutputStream());
						return null;
					}
				}
			 }
			//如果是图片创建方式，需要和列表创建的日期进行合并
			 if("pic".equals(way)){
				if(advertPlanInfo!=null && !"".equals(advertPlanInfo.getPlanCode())){
					Map map=new HashMap(); 
					map.put("planCodeKey", advertPlanInfo.getPlanCode());
					List<AdvertResourcePlanTempInfo> tempInfoList=advResourcePlanService.findAdvertResourcePlanTempsByPlanCode(map);
					if(null != tempInfoList && tempInfoList.size()>0){
						for (int j = 0; j < tempInfoList.size(); j++) {
							AdvertResourcePlanTempInfo temp=tempInfoList.get(j);
							//如果ResourceCode一致把useDate取出来合并在一起;
							if(info.getResourceCode().equals(temp.getResourceCode())){
								String[] oldDate=temp.getUseDate().split("</br>");
								String strDate="";
								for (int k = 0; k < oldDate.length; k++) {
									if(!info.getUseDate().contains(oldDate[k])){
										strDate +=","+oldDate[k];
									}
								}
								info.setUseDate(info.getUseDate()+strDate);
							}
						}
					}
				}
			 }
			 if(jsonObject.get("jiage")!=null && StringUtils.isNotBlank(jsonObject.get("jiage").toString()))
			 info.setCostPrice(new BigDecimal(jsonObject.getDouble("jiage")));
			 if(jsonObject.get("youhuijia")!=null && StringUtils.isNotBlank(jsonObject.get("youhuijia").toString())){
				 info.setCurrentPrice(new BigDecimal(jsonObject.getDouble("youhuijia")));
			 }else{
				 info.setCurrentPrice(info.getCostPrice());
			 }
			 if(advertPlanInfo!=null && StringUtils.isNotEmpty(advertPlanInfo.getPlanCode())){
				 info.setPlanCode(advertPlanInfo.getPlanCode());
			 }
			 info.setRegionName(request.getParameter("regionName"));
			 info.setCityName(request.getParameter("cityName"));
			 info.setCinemaName(request.getParameter("cinemaName"));
			 info.setFacilityCode(request.getParameter("facilityCode"));
			 info.setFacilityType(Long.valueOf(request.getParameter("facilityType")));
			 info.setCreateUserId(orgModels.get(0).getName());
			 info.setUpdateUserId(orgModels.get(0).getName());
			 advResourcePlanTempInfos.add(info);
		}
		
		AdvertResourcePlanInfo planInfo = new AdvertResourcePlanInfo();
		Map paramsAdvresourcePlaninfoForMainTianMap = new HashMap();
		planInfo.setPlanId(advertPlanInfo.getPlanId());
		paramsAdvresourcePlaninfoForMainTianMap.put("AdvResourceInfoByPlanIdKey", planInfo);
//		List<OrgModel> orgModels = initOrgModels(request);
		paramsAdvresourcePlaninfoForMainTianMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		
		
		//按照方案ID 查询中间表得到list
		List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(paramsAdvresourcePlaninfoForMainTianMap);
		params.put("advResourcePlanTempInfos",advResourcePlanTempInfos);
		params.put("AdvertPlanKey", advertPlanInfo);
		params.put("editKey", edit);
		String planTempId = request.getParameter("planTempId");
		params.put("planTempId", planTempId);
		params.put("listKey", advertResourcePlanInfosList);
		boolean flag = advResourcePlanService.updateAdvertResourcePlanTemp(params);
		if(flag){
			json.put("success",Constant.ADD_ADV_RESOURCEPLAN_ADD_SUCCESS);
		}else{
			json.put("failure",Constant.ADD_ADV_RESOURCEPLAN_FAILED);
		}
		
		BigDecimal discount = advResourcePlanService.findDiscountByName(params);
		
		String status = request.getParameter("flag");
		if(StringUtils.isNotBlank(status)&&status.equals("edit")){
			//维护
		
		}else{
			//新增
			AdvertResourcePlanTempInfo info = advResourcePlanService.findAdvertResourcePlanTemp(params);
		//	json.put("costPriceTotal", info.getCostPriceTotal());
		//	json.put("currentPriceTotal", info.getCurrentPriceTotal());
			//0 ：字体红色        1：不变色
			if(discount == null){
				if(info.getCurrentPriceTotal().compareTo(info.getCostPriceTotal())==-1){
					json.put("checkPrice", "0");
				}else{
					json.put("checkPrice", "1");
				}
			}
			if(discount != null){
				BigDecimal currentPriceTotal = info.getCostPriceTotal().divide(new BigDecimal(10)).multiply(discount);
				
				if(info.getCurrentPriceTotal().compareTo(currentPriceTotal)==-1){
					json.put("checkPrice", "0");
				}else{
					json.put("checkPrice", "1");
				}
			}
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		return null;
	}
	
	/**
	 * @Description: TODO(新增方案中 添加到列表的方法) 
	 * @Title:       addAdvResourceUseDateToListadd 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertPlanInfo
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-25 下午06:41:15
	 */
	public ModelAndView addAdvResourceUseDateToListadd(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo ) throws IOException{
		Map advresourcePlanMap = new HashMap();
		
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		advresourcePlanMap.put(GlobalConstant.USER_CODE_KEY, orgModels);

		JSONObject json = new JSONObject();
		List<AdvertResourcePlanInfo> advResourcePlanInfoList = new ArrayList<AdvertResourcePlanInfo>(); //关系表使用日期的list
		//解析使用日期
		String jsondate = request.getParameter("jsondate");
		JSONArray  jsonArray = JSONArray.fromObject(jsondate);
		String planId = "";
		for (int i = 0; i < jsonArray.size(); i++) {
			 JSONObject jsonObject = jsonArray.getJSONObject(i);
			 String bianhao= jsonObject.getString("bianhao");
			 String shiyongriqi= jsonObject.getString("shiyongriqi");
			 
			 String advresourceId ="";
			 AdvertResourceInfo ari = new AdvertResourceInfo();
			  ari.setResourceCode(bianhao);
			  Map advppMap = new HashMap();
			  advppMap.put("AdvertResourceInfoCodeKey", ari);
			  AdvertResource are = advResourcePlanService.findAdvertResourceIdByResourceCode(advppMap);
				  if (are != null) {
					advresourceId = are.getResourceId();
				  }
			 
			 	AdvertResourcePlanInfo advertResourcePlanInfos = new AdvertResourcePlanInfo();
			 	//由方案code得到方案id
			 	String code = advertPlanInfo.getPlanCode();
			 	Map advplanMap = new HashMap();
				AdvertPlanInfo  apio =  new AdvertPlanInfo();
				apio.setPlanCode(code);
				advplanMap.put("advertPlanKey", apio);
				
				if (StringUtils.isNotBlank(code)) {
					AdvertPlanInfo  advPlanInfoByPlanCode = advResourcePlanService.findAdvertPlanInfoByCode(advplanMap);//更具方案code查询方案详情信息
					if (advPlanInfoByPlanCode != null) {
						planId = advPlanInfoByPlanCode.getPlanId();
					}
				}
			 	advertResourcePlanInfos.setPlanId(planId);//根据前面页面传过来的code 得到的方案id
				advertResourcePlanInfos.setResourceId(advresourceId);
				advertResourcePlanInfos.setUseDate(shiyongriqi);//2012-1-1</br>2012-1-2</br>
				advResourcePlanInfoList.add(advertResourcePlanInfos);
		}
		//System.out.println("advResourcePlanInfoList==" + advResourcePlanInfoList);
		//多个资源循环判断是否有使用日期重复占用
		//String resultString ="";
		/*for (int i = 0; i < advResourcePlanInfoList.size(); i++) {
			advplanidAndresourceIdAndUseDateMap.put("advertResourcePlanKey", advResourcePlanInfoList.get(i)); //使用日期和资源uuid advResourcePlanInfoListKey
			AdvertPlanInfo api = new AdvertPlanInfo();
			api.setPlanCode(advertPlanInfo.getPlanCode());
			advplanidAndresourceIdAndUseDateMap.put("advertPlanKey", api);		//方案编号
			resultString = advResourcePlanService.checkResourceUseDate(advplanidAndresourceIdAndUseDateMap);
		}*/
		/**
		 * 说明没有方案在占用当前用户选择的日期,这个时候可以保存,先更新方案,在service层里面再保存关系表数据
		 */
		AdvertPlanInfo advertPlanInfo2 = new AdvertPlanInfo();
		advertPlanInfo2.setPlanCode(advertPlanInfo.getPlanCode());
		advertPlanInfo2.setPlanId(planId);
		advresourcePlanMap.put("keyplaninfoadd", advertPlanInfo2);
		advresourcePlanMap.put("advResourcePlanRelationInfoListKey", advResourcePlanInfoList);//关系表的数据map
		boolean addResourcePlaninfoThroughListTag = advResourcePlanService.updateAdvResourcePlanInfoThroughListadd(advresourcePlanMap);
		
		//添加到列表可以不提示
		if(addResourcePlaninfoThroughListTag){
			json.put("success",Constant.ADD_ADV_RESOURCEPLAN_ADD_SUCCESS);
		}else{
			json.put("failure",Constant.ADD_ADV_RESOURCEPLAN_FAILED);
		}
		//根据方案code，查找方案详细信息-得到备注
		/*Map advplanMap = new HashMap();
		Map modelMap = new HashMap();
		AdvertPlanInfo  apio =  new AdvertPlanInfo();
		apio.setPlanCode(advertPlanInfo.getPlanCode());
		advplanMap.put("advertPlanKey", apio);
		String planId = "";
		if (StringUtils.isNotBlank(advertPlanInfo.getPlanCode())) {
			AdvertPlanInfo  advPlanInfoByPlanCode = advResourcePlanService.findAdvertPlanInfoByCode(advplanMap);//更具方案code查询方案详情信息
			if (advPlanInfoByPlanCode != null) {
				planId = advPlanInfoByPlanCode.getPlanId();
			}
			modelMap.put("advPlanInfoByPlanCode", advPlanInfoByPlanCode);
		}
		modelMap.put("advresourceUseDateListadd", advertResourcePlanInfosList);
		modelMap.put("planId", planId);*/
		IOUtil.writeJSON(json, response.getOutputStream()); 
		//return new ModelAndView(VIEW_REQUEST_QUERYADVPLANINFO_SUCCESS,modelMap);
		return null;
  }

	/*//返回advplan信息
	public ModelAndView findadvplaninfo(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo ){
		Map advplanMap = new HashMap();
		String planCode = request.getParameter("planCode");
		paramsAdvresourcePlaninfoForMainTianMap.put("AdvResourceInfoByPlanIdKey", advertResourcePlanInfo);
		//按照方案ID 查询中间表得到list
		List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(paramsAdvresourcePlaninfoForMainTianMap);
		Map advplanMap = new HashMap();
		AdvertPlanInfo  apio =  new AdvertPlanInfo();
		apio.setPlanCode(planCode);
		advplanMap.put("advertPlanKey", apio);
		//根据方案code，查找方案详细信息-得到备注
		Map modelMap = new HashMap();
		String planId = "";
		if (StringUtils.isNotBlank(planCode)) {
			AdvertPlanInfo  advPlanInfoByPlanCode = advResourcePlanService.findAdvertPlanInfoByCode(advplanMap);//更具方案code查询方案详情信息
			if (advPlanInfoByPlanCode != null) {
				planId = advPlanInfoByPlanCode.getPlanId();
			}
			modelMap.put("advPlanInfoByPlanCode", advPlanInfoByPlanCode);
		}
		//modelMap.put("advresourceUseDateListadd", advertResourcePlanInfosList);
		modelMap.put("planId", planId);
		return new ModelAndView(VIEW_REQUEST_QUERYADVPLANINFO_SUCCESS,modelMap);
	}*/
	
	
	/**
	 * 列表方式保存方案信息-更新方案信息和资源的使用状态
	 * 
	 */
	public ModelAndView saveAdvPlanInfoByList(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo ) throws IOException{
		JSONObject json = new JSONObject();
		//通过plancode得到planid
		//根据方案code，查找方案详细信息-得到备注
		Map saveadvplanMap = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		
		AdvertPlanInfo  apio =  new AdvertPlanInfo();
		apio.setPlanCode(advertPlanInfo.getPlanCode()); 
		saveadvplanMap.put("advertPlanKey", apio);
		String planId = "";
		if (StringUtils.isNotBlank(advertPlanInfo.getPlanCode())) {
			AdvertPlanInfo  advPlanInfoByPlanCode = advResourcePlanService.findAdvertPlanInfoByCode(saveadvplanMap);//更具方案code查询方案详情信息
			if (advPlanInfoByPlanCode != null) {
				planId = advPlanInfoByPlanCode.getPlanId();
			}
		}
		
		List<AdvertResourcePlanInfo> advplaninfolist = new ArrayList<AdvertResourcePlanInfo>();
		//解析资源编号
		String[] resourceCodes = null;
		if(! StringUtils.isBlank(request.getParameter("resourceCodes"))){
			resourceCodes  	 = request.getParameter("resourceCodes").split(",");		//资源编号
		}
		if(resourceCodes!=null){
			
			for (int i = 0; i < resourceCodes.length; i++) {
				AdvertResourcePlanInfo advplaninfo = new AdvertResourcePlanInfo();
				  //按照资源code 返回资源的uuid
				  String resourceId ="";
				  Map advMap = new HashMap();
				  AdvertResourceInfo ari = new AdvertResourceInfo();
				  ari.setResourceCode(resourceCodes[i]);
				  advMap.put("AdvertResourceInfoCodeKey", ari);
				  AdvertResource are = advResourcePlanService.findAdvertResourceIdByResourceCode(advMap);
					  if (are != null) {
						  resourceId = are.getResourceId();
					  }
				advplaninfo.setResourceId(resourceId);
				advplaninfolist.add(advplaninfo);	
			}

		}
		/**
		 * 1.更新方案信息
		 * 2.更新当前方案下面的资源状态
		 */
		Map advrsaverPlanMap = new HashMap();
		advrsaverPlanMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		AdvertPlanInfo saveadvertPlanInfo = new AdvertPlanInfo();
		saveadvertPlanInfo.setPlanId(planId);
		saveadvertPlanInfo.setPlanName(advertPlanInfo.getPlanName());
		saveadvertPlanInfo.setRemark(advertPlanInfo.getRemark());
		advrsaverPlanMap.put("keyplaninfoadd", saveadvertPlanInfo);
		advrsaverPlanMap.put("advResourcePlanRelationInfoListKey", advplaninfolist);//关系表的数据map
		boolean saveadvplantag = advResourcePlanService.saveAdvResourcePlanInfoThroughList(advrsaverPlanMap);
		if(saveadvplantag){
			json.put("success",Constant.SAVE_ADV_RESOURCEPLAN_ADD_SUCCESS);
		}else{
			json.put("failure",Constant.SAVE_ADV_RESOURCEPLAN_FAILED);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		return null;
	}
	
	/**
	 * 列表方式--创建广告资源方案
	 * @param request
	 * @param response
	 * @param advertPlanInfo
	 * @return
	 * @throws IOException
	 */
	public ModelAndView createAdvertPlanByList(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo) throws IOException{
		JSONObject json = new JSONObject();
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("advertPlanKey", advertPlanInfo);
		params.put("isOut", request.getParameter("isOut"));
		params.put("currentPriceTotal", request.getParameter("currentPriceTotal"));
		params.put("costPriceTotal", request.getParameter("costPriceTotal"));
		params.put("flag", request.getParameter("flag"));
		params.put("cinemaId", request.getParameter("cinemaId"));
		String resultTip = advResourcePlanService.checkResourceUseDateByList(params);
		
		if(StringUtils.isNotBlank(resultTip)){
			json.put("code_fail", resultTip);
			IOUtil.writeJSON(json, response.getOutputStream()); 
			return null;
		}
		
//		if(advResourcePlanService.checkAdvertPlanName(params)){
//			json.put("code_fail", Constant.ADVERT_PLAN_NAME_EXSIT);
//			IOUtil.writeJSON(json, response.getOutputStream()); 
//			return null;
//		}
		
		if(advResourcePlanService.checkAdvertPlanCode(params)){
			json.put("code_fail", Constant.ADVERT_PLAN_CODE_EXSIT);
			IOUtil.writeJSON(json, response.getOutputStream()); 
			return null;
		}
		
		if(advResourcePlanService.addAdvertPlan(params)){
			json.put("success",Constant.ADD_ADV_RESOURCEPLAN_SUCCESS);
		}else{
			json.put("code_fail",Constant.ADD_ADV_RESOURCEPLAN_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		return null;
	}
	
	/**
	 * 列表方式--维护广告资源方案
	 * @param request
	 * @param response
	 * @param advertPlanInfo
	 * @return
	 * @throws IOException
	 */
	public ModelAndView updateAdvertPlanByList(HttpServletRequest request, HttpServletResponse response, AdvertPlanInfo advertPlanInfo) throws IOException{
		JSONObject json = new JSONObject();
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("advertPlanKey", advertPlanInfo);
		params.put("isOut", request.getParameter("isOut"));
		params.put("currentPriceTotal", request.getParameter("currentPriceTotal"));
		params.put("costPriceTotal", request.getParameter("costPriceTotal"));
		params.put("flag", request.getParameter("flag"));
		params.put("cinemaId", request.getParameter("cinemaId"));
		
		String resultTip = advResourcePlanService.checkResourceUseDateByList(params);
		
		if(StringUtils.isNotBlank(resultTip)){
			json.put("code_fail", resultTip);
			IOUtil.writeJSON(json, response.getOutputStream()); 
			return null;
		}
		
		if(advertPlanInfo.getStatus()==Dictionary.PLAN_STATUS_SHENPIWEITONGGUO
				|| advertPlanInfo.getStatus()==Dictionary.PLAN_STATUS_YSX || advertPlanInfo.getStatus()==Dictionary.PLAN_STATUS_WXSP){
			long  advResourcePlanNumber = AdvertCodeController.getAdvertCode(AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX, AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_TYPE);
			String finaladvResourcePlancodeNumber = AdvResourcePlanConstant.ADV_RESOURCEPLAN_NUMBER_HEAD_PREFIX + "-" + NumberFormatUtil.advAgentNumberFormat(advResourcePlanNumber);
			params.put("newPlanCode", finaladvResourcePlancodeNumber);
		}else{
			if(advResourcePlanService.checkAdvertPlanNameByPlanId(params)){
				json.put("code_fail", Constant.ADVERT_PLAN_NAME_EXSIT);
				IOUtil.writeJSON(json, response.getOutputStream()); 
				return null;
			}
		}
		
		if(advResourcePlanService.updateAdvertPlan(params)){
			json.put("success",Constant.MAINTAIN_ADV_PLAN_SUCCESS);
		}else{
			json.put("code_fail",Constant.MAINTAIN_ADV_RESOURCEPLAN_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		return null;
	}
	
	/**
	 * 删除临时表数据
	 * @param request
	 * @param response
	 * @param advertResourcePlanTempInfo
	 */
	public void deleteAdvertResourcePlanTemp(HttpServletRequest request, HttpServletResponse response, AdvertResourcePlanTempInfo advertResourcePlanTempInfo){
		Map params = new HashMap();
		params.put("AdvertResourcePlanTempInfoKey", advertResourcePlanTempInfo);
		advResourcePlanService.deleteAdvertResourcePlanTemp();
		if(StringUtils.isNotBlank(advertResourcePlanTempInfo.getPlanCode()))
			advResourcePlanService.deleteAdvertResourcePlanTempByPlanCode(params);
	}
	
	/**
	 * 往资源方案插入记录
	 * @param request
	 * @param response
	 * @param advertResourcePlanInfo
	 */
	public void saveResourcePlanToResourcePlanTemp(HttpServletRequest request, HttpServletResponse response, AdvertResourcePlanInfo advertResourcePlanInfo){
		Map advertResourcePlanInfoMap = new HashMap();
		advertResourcePlanInfoMap.put("AdvResourceInfoByPlanIdKey", advertResourcePlanInfo);
		List<OrgModel> orgModels = initOrgModels(request);
		advertResourcePlanInfoMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		
		//按照方案ID 查询中间表得到list
		List<AdvertResourcePlanInfo>  advertResourcePlanInfosList = advResourcePlanService.findAdvResourcePlanRelationInfosByPlanId(advertResourcePlanInfoMap);
		if(Utils.isNotEmpty(advertResourcePlanInfosList)){
			advertResourcePlanInfoMap.put("listKey", advertResourcePlanInfosList);
			advResourcePlanService.saveResourcePlanToResourcePlanTemp(advertResourcePlanInfoMap);
		}
	}
	
	/**
	 * @Description: TODO(创建广告资源方案-维护广告资源方案中公用的方法)</p>
	 * <p>限制用户选择的日期不能超过13个月</p>
	 * @Title:       checkUseDateIsOutofRange 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @return
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-5-10 下午05:51:12
	 * @throws
	 */
	public ModelAndView checkUseDateIsOutofRange(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		String resourceUseDate = request.getParameter("resourceUseDate");
		String[] usedates = resourceUseDate.split(",");
			
		boolean  checkUsedateTag = checkUseDate(usedates);
		
		if(checkUsedateTag){
			json.put("outofrange",Constant.USEDATE_OUTOFRANGE);
		}else{
			json.put("notoutofrange",Constant.USEDATE_NOTOUTOFRANGE);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		return null;	
	}	
		
	public ModelAndView getBack(HttpServletRequest request, HttpServletResponse response,AdvertPlanInfo info) throws IOException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		params.put("advertPlanKey",info);
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		if(advResourcePlanService.updateAdvertPlanStatus(params)){
			json.put("success",Constant.ADVERT_FACILITY_LOCATION_SUCCESS);
		}else{
			json.put("code_fail",Constant.ADVERT_FACILITY_LOCATION_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		return null;	
	}	
		
	/**
	 * @Title: aveResourDate
	 * @Description: TODO(资源锁定时间设置)
	 * @param @param request
	 * @param @param response
	 * @param @param advertDateInfo
	 * @param @return
	 * @param @throws Exception
	 * @return ModelAndView
	 * @author milihua
	 * @date 2012-08-24
	 */
	
	public ModelAndView resourDateLockSet (HttpServletRequest request, HttpServletResponse response,AdvertDateInfo info) throws IOException{
		Map modelMap = new HashMap();
		List<AdvertDateInfo> advertDateInfoList = advResourcePlanService.advResourDateSet();
		if(!advertDateInfoList.isEmpty()){
			info = advertDateInfoList.get(0);
		}
		modelMap.put("advertDateInfo", info);
		return new ModelAndView(VIEW_REQUEST_QUERYADVPLANINFO_SUCCESS,modelMap);
	}
	
	/**
	 * @Title: aveResourDate
	 * @Description: TODO(资源锁定时间设置)
	 * @param @param request
	 * @param @param response
	 * @param @param advertDateInfo
	 * @param @return
	 * @param @throws Exception
	 * @return ModelAndView
	 * @author milihua
	 * @date 2012-08-26
	 */
	public ModelAndView saveResourDate(HttpServletRequest request,HttpServletResponse response,AdvertDateInfo advertDateInfo) throws Exception{
		List<OrgModel> listmodelList=initOrgModels(request);
		Map params = new HashMap();
		
		params.put("advertDateInfo", advertDateInfo);
		params.put("listmodelList", listmodelList);
		JSONObject json = new JSONObject();
		boolean res = advResourcePlanService.saveResourDate(params);
		if (!res) {
			json.put("failure", Constant.SAVE_FAIL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}else {
			json.put("success", Constant.SAVE_SUCCESS);
			IOUtil.writeJSON(json, response.getOutputStream());
		}
		
		return null;
	}
	
	/**
	 * @Title: advResourcePlanSp
	 * @Description: TODO(广告资源方案审批)
	 * @param @param request
	 * @param @param response
	 * @param @param advertPlanInfo
	 * @param @return
	 * @param @throws Exception
	 * @return ModelAndView
	 * @author milihua
	 * @date 2012-08-27
	 */
	public ModelAndView advResourcePlanSp(HttpServletRequest request,HttpServletResponse response,AdvertPlanInfo advertPlanInfo) throws Exception{
		Map advresourceplanMap = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		advresourceplanMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		advertPlanInfo.setPageSize(GlobalConstant.PAGE_SIZE); 	//10条数据
		advertPlanInfo.setPageNavigationURL(CommUtil.getUrl(request)); 
		advresourceplanMap.put("advPlanInfo", advertPlanInfo);
		// 分页查询
		Page<AdvertPlanInfo,AdvertPlan>  pageResult = advResourcePlanService.findAdvResourcePlanSpInfos(advresourceplanMap);
		pageResult.setPageNavigationURL(advertPlanInfo.getPageNavigationURL());

		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.PLAN_STATUS);
		advresourceplanMap.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> planStatus = dictionaryService.findDataDictionarys(advresourceplanMap);

		//页面数据显示
	   Map modelMap = new HashMap();
	   modelMap.put("pageResult", pageResult); 					//列表数据
	   modelMap.put("advertPlanInfo", advertPlanInfo);
	   modelMap.put("planStatus", planStatus);
	   return new ModelAndView(VIEW_QUERY_ADVERTRESOURVEPLANSP_SUCCESS,modelMap);
	}
	
	/**
	 * @Title: showEditdvertFacilityDetail
	 * @Description: TODO(广告资源方案审批详情)
	 * @param @param request
	 * @param @param response
	 * @param @param advertPlanInfo
	 * @param @return
	 * @param @throws Exception
	 * @return ModelAndView
	 * @author milihua
	 * @date 2012-08-28
	 */
	public ModelAndView showEditdvertFacilityDetail(HttpServletRequest request,HttpServletResponse response,AdvertPlanInfo advertPlanInfo) throws Exception{
		Map advresourceplanMap = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		advresourceplanMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		advertPlanInfo.setPageSize(GlobalConstant.PAGE_SIZE); 	//10条数据
		advertPlanInfo.setPageNavigationURL(CommUtil.getUrl(request)); 
		advresourceplanMap.put("advPlanInfo", advertPlanInfo);
		// 分页查询
		List<AdvertPlanInfo>  pageResult = advResourcePlanService.findAvertFacilityDetailList(advresourceplanMap);
//		pageResult.setPageNavigationURL(advertPlanInfo.getPageNavigationURL());

		Map params = new HashMap();
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);

		//页面数据显示
	   Map modelMap = new HashMap();
	   modelMap.put("pageResult", pageResult); 					//列表数据
	   modelMap.put("advertPlanInfo", advertPlanInfo);
	   modelMap.put("advertFacilityType", advertFacilityType);
		return new ModelAndView(VIEW_ADVERTRESOURVEPLANSP_DETAILS,modelMap);
	}
	
	/**
	 * @Title: agree
	 * @Description: TODO(广告资源方案审批详情)
	 * @param @param request
	 * @param @param response
	 * @param @param advertPlanInfo
	 * @param @return
	 * @param @throws Exception
	 * @return ModelAndView
	 * @author milihua
	 * @date 2012-08-29
	 */
	public ModelAndView agree(HttpServletRequest request,HttpServletResponse response,AdvertPlanInfo advertPlanInfo) throws Exception{
		Map params = new HashMap();
		
		List<OrgModel> listmodelList=initOrgModels(request);
		
		String operate = request.getParameter("operate");
		
		params.put("advertPlanInfo", advertPlanInfo);
		params.put("listmodelList", listmodelList);
		params.put("operate", operate);
		JSONObject json = new JSONObject();
		boolean res = advResourcePlanService.addOperate(params);
		if (!res) {
			json.put("failure", Constant.ADVERT_FACILITY_LOCATION_FAIL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}else {
			json.put("success", Constant.ADVERT_FACILITY_LOCATION_SUCCESS);
			IOUtil.writeJSON(json, response.getOutputStream());
		}
		return null;
	}
	
	/**
	 * 删除上传的图片，文件
	 * */
	public void delPic(HttpServletRequest request, HttpServletResponse response,AdvertPlanInfo advertPlanInfo) throws Exception{
		String filePath = request.getSession().getServletContext().getRealPath("/")+advertPlanInfo.getUrl();
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		JSONObject json = new JSONObject();
		json.put("success", "success1");
		IOUtil.writeJSON(json, response.getOutputStream());
	}
	
	public static final String VIEW_ABATE_ADVPLAN = "view_abate_advplan";
	
	public ModelAndView abatePlan(HttpServletRequest request, HttpServletResponse response,AdvertPlanInfo advertPlanInfo) throws Exception{
		Map modelMap = new HashMap();
		modelMap.put("advertPlanInfo", advertPlanInfo);
		return new ModelAndView(VIEW_ABATE_ADVPLAN, modelMap);
	}
	
}