package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import com.kingdee.ais.core.constant.GlobalConstant;
import com.kingdee.ais.core.entity.OrgModel;
import com.kingdee.ais.core.pagination.Page;
import com.kingdee.ais.core.util.CommUtil;
import com.kingdee.ais.core.util.Utils;
import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.advert.service.IAdvertFacilityService;
import com.kingdee.ais.ibmp.business.dictionary.Dictionary;
import com.kingdee.ais.ibmp.business.dictionary.service.IAdvertIconService;
import com.kingdee.ais.ibmp.business.dictionary.service.IDictionaryService;
import com.kingdee.ais.ibmp.business.org.service.IOrgRegionService;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertFacility;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertResource;
import com.kingdee.ais.ibmp.model.pojo.advert.FloorPlan;
import com.kingdee.ais.ibmp.model.pojo.org.OrgCinema;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertFacilityInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertResourceInfo;
import com.kingdee.ais.ibmp.model.vo.advert.FloorPlanInfo;
import com.kingdee.ais.ibmp.model.vo.dictionary.AdvertIconInfo;
import com.kingdee.ais.ibmp.model.vo.dictionary.DataDictionaryInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCinemaInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCityInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgRegionInfo;
import com.kingdee.ais.ibmp.view.constant.Constant;
import com.kingdee.ais.ibmp.view.util.FileUpLoadUtil;
import com.kingdee.ais.ibmp.view.util.IOUtil;

/**
 * 广告设施管理
 * @author ldw
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdvertFacilityController extends MultiActionControllerImpl{
	
	private IOrgRegionService orgRegionService;
	private IAdvertFacilityService advertFacilityService;
	private IDictionaryService dictionaryService;
	private IAdvertIconService advertIconService;
	private static final String VIEW_ADVERTFACILITY_SHOW_ADD_SUCCESS = "AdvertFacilityController_showAddAdvertFacility_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_UPDATE_SUCCESS = "AdvertFacilityController_showUpdateAdvertFacility_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_UPDATE_DETAIL_SUCCESS = "AdvertFacilityController_showUpdateAdvertFacilityDetail_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_EXCEPTION_SUCCESS = "AdvertFacilityController_showGlobalException_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_UPDATE_FLOOR_PLAN_SUCCESS = "AdvertFacilityController_showUpdateFloorPlan_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_ADD_FLOOR_PLAN_SUCCESS = "AdvertFacilityController_showAddFloorPlan_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_DELETE_FLOOR_PLAN_URL_SUCCESS = "showUpdateFloorPlan.do";
	private static final String VIEW_ADVERTFACILITY_SHOW_UPDATE_FLOOR_PLAN_URL_SUCCESS = "AdvertFacilityController_showUpdateFloorPlanUrl_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_SET_ADVERTFACILITY_LOCATION_SUCCESS = "AdvertFacilityController_showSetAdvertFacilityLocation_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_ADD_ADVERT_RESOURCE_SUCCESS = "AdvertFacilityController_showAddAdvertResource_success";
	private static final String VIEW_ADVERTFACILITY_SHOW_FIX_ADVERT_FACILITY_SUCCESS = "AdvertFacilityController_showFixAdvertFacilityLocation_success";
	public void setOrgRegionService(IOrgRegionService orgRegionService) {
		this.orgRegionService = orgRegionService;
	}
	
	public void setDictionaryService(IDictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public void setAdvertFacilityService(
			IAdvertFacilityService advertFacilityService) {
		this.advertFacilityService = advertFacilityService;
	}

	public void setAdvertIconService(IAdvertIconService advertIconService) {
		this.advertIconService = advertIconService;
	}

	/**
	 * 进入新增广告设施页面
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView showAddAdvertFacility(HttpServletRequest request, HttpServletResponse response){
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
		data.setFDataId(Dictionary.ADVERT_FACILITY_STATUS);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityStatus = dictionaryService.findDataDictionarys(params);
//		AdvertFacilityInfo advertFacilityInfo = new AdvertFacilityInfo();
//		advertFacilityInfo.setPageSize(GlobalConstant.PAGE_SIZE);
//		advertFacilityInfo.setPageNavigationURL(CommUtil.getUrl(request));
//		params.put("advertFacilityKey", advertFacilityInfo);
//		Page<AdvertFacilityInfo, AdvertFacility> pageResult = advertFacilityService.findAdvertFacilitys(params);
//		List<AdvertFacilityInfo> advertFacilityInfos = pageResult.getPageVOItems();
//		String code = "01";
//		String code1="";
//		for(int i = 0;i<advertFacilityInfos.size();i++){
//			AdvertFacilityInfo arif = advertFacilityInfos.get(0);
//			AdvertFacilityInfo info = advertFacilityInfos.get(i);
//			String[] resourceCodes = arif.getFacilityCode().split("-");
//			String[] resourceCodes1 = info.getFacilityCode().split("-");
//			code = resourceCodes[resourceCodes.length-1];
//			code1 = resourceCodes1[resourceCodes.length-1];
//			
//			if(Integer.parseInt(code) > Integer.parseInt(code1)){
//				if(Integer.parseInt(code)+1 < 10){
//					code = Integer.parseInt(code)+1+"";
//					code="0"+code;
//				}else{
//					code =Integer.parseInt(code)+1+"";
//				}
//				
//			}else{
//				if(Integer.parseInt(code1)+1 < 10){
//					code = Integer.parseInt(code1)+1+"";
//					code="0"+code;
//				}else{
//					code =Integer.parseInt(code1)+1+"";
//				}
//				
//			}
//		}
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
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions, IOUtil.getConfig()));
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("advertFacilityStatus", advertFacilityStatus);
		modelMap.put("orgmodel",orgModel);
//		modelMap.put("code", code);
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_ADD_SUCCESS,modelMap);
	}
	
	public void getCode(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		String facilityType = request.getParameter("facilityType");
		String cinemaId = request.getParameter("cinemaId");
		OrgCinemaInfo orgCinema = new OrgCinemaInfo();
		orgCinema.setCinemaId(cinemaId);
		advertFacilityInfo.setOrgCinema(orgCinema);
		advertFacilityInfo.setFacilityType(Long.parseLong(facilityType));
		advertFacilityInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		advertFacilityInfo.setPageNavigationURL(CommUtil.getUrl(request));
		params.put("advertFacilityKey", advertFacilityInfo);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		
		
		Page<AdvertFacilityInfo, AdvertFacility> pageResult = advertFacilityService.findAdvertFacilitys(params);
		List<AdvertFacilityInfo> advertFacilityInfos = pageResult.getPageVOItems();
		String code = "01";
		String code1="";
		for(int i = 0;i<advertFacilityInfos.size();i++){
			AdvertFacilityInfo arif = advertFacilityInfos.get(0);
			AdvertFacilityInfo info = advertFacilityInfos.get(i);
			String[] resourceCodes = arif.getFacilityCode().split("-");
			String[] resourceCodes1 = info.getFacilityCode().split("-");
			code = resourceCodes[resourceCodes.length-1];
			code1 = resourceCodes1[resourceCodes.length-1];
			if(Integer.parseInt(code) > Integer.parseInt(code1)){
				if(Integer.parseInt(code)+1 < 10){
					code = Integer.parseInt(code)+1+"";
					code="0"+code;
				}else{
					code =Integer.parseInt(code)+1+"";
				}
			}else{
				if(Integer.parseInt(code1)+1 < 10){
					code = Integer.parseInt(code1)+1+"";
					code="0"+code;
				}else{
					code =Integer.parseInt(code1)+1+"";
				}
			}
		}
		List list = new ArrayList();
		list.add(code);
		JSONArray jsonArray = JSONArray.fromObject(list);
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}
	/**
	 * 新增广告设施
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 * @throws IOException
	 */
	public ModelAndView addAdvertFacility(HttpServletRequest request, HttpServletResponse response,
			AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("cinemaId"));
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("regionId"));
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);
		advertFacilityInfo.setOrgCity(orgCityInfo);
		advertFacilityInfo.setOrgRegion(orgRegionInfo);
		params.put("advertFacilityKey", advertFacilityInfo);
		if(advertFacilityService.findAdvertFacilityCode(params)){
			json.put("code_fail",Constant.ADVERT_FICILITY_CODE_EXSIT);
		}else if(advertFacilityService.addAdvertFacility(params)){
			json.put("success",Constant.ADD_SUCCESS);
		}else{
			json.put("code_fail",Constant.ADD_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 进入广告设施维护列表页面
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 */
	public ModelAndView showUpdateAdvertFacility(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo){
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		
		params.put("advertFacilityKey", advertFacilityInfo);
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
	
		data.setFDataId(Dictionary.ADVERT_FACILITY_STATUS);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityStatus = dictionaryService.findDataDictionarys(params);
		
		advertFacilityInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		advertFacilityInfo.setPageNavigationURL(CommUtil.getUrl(request));
		
		//分页条件
		if(StringUtils.isNotBlank(request.getParameter("selectAdvertFacilityTypeName"))){
			advertFacilityInfo.setFacilityType(Long.valueOf(request.getParameter("selectAdvertFacilityTypeName")));
		}
		if(StringUtils.isNotBlank(request.getParameter("selectRegionName"))){
			OrgRegionInfo orgRegion = new OrgRegionInfo();
			orgRegion.setRegionId(request.getParameter("selectRegionName"));
			advertFacilityInfo.setOrgRegion(orgRegion);
		}
		if(StringUtils.isNotBlank(request.getParameter("selectCityName"))){
			OrgCityInfo orgCity = new OrgCityInfo();
			orgCity.setCityId(request.getParameter("selectCityName"));
			advertFacilityInfo.setOrgCity(orgCity);
		}
		if(StringUtils.isNotBlank(request.getParameter("selectCinemaName"))){
			OrgCinemaInfo orgCinema = new OrgCinemaInfo();
			orgCinema.setCinemaId(request.getParameter("selectCinemaName"));
			advertFacilityInfo.setOrgCinema(orgCinema);
		}
		if(StringUtils.isNotBlank(request.getParameter("selectAdvertFacilityStatusName"))){
			advertFacilityInfo.setFacilityStatus(Long.valueOf(request.getParameter("selectAdvertFacilityStatusName")));
		}

		Page<AdvertFacilityInfo, AdvertFacility> pageResult = advertFacilityService.findAdvertFacilitys(params);
		pageResult.setPageNavigationURL(advertFacilityInfo.getPageNavigationURL());
		OrgModel orgModel = this.getModelsByUserCode(request, orgModels);
		
		Map modelMap = new HashMap();
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("advertFacilityStatus", advertFacilityStatus);
		modelMap.put("pageResult", pageResult);
		modelMap.put("advertFacilityInfo",advertFacilityInfo);
		modelMap.put("orgmodel",orgModel);
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_UPDATE_SUCCESS,modelMap);
	}
	
	/**
	 * 进入广告设施详情页面
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 */
	public ModelAndView showUpdateAdvertFacilityDetail(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo){
		Map params = new HashMap();
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
	
		data.setFDataId(Dictionary.ADVERT_FACILITY_STATUS);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityStatus = dictionaryService.findDataDictionarys(params);
		
		params.put("advertFacilityKey", advertFacilityInfo);
		AdvertFacilityInfo facilityInfo = advertFacilityService.findAdvertFacility(params);
		Map modelMap = new HashMap();
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("advertFacilityStatus", advertFacilityStatus);
		modelMap.put("advertFacilityInfo", facilityInfo);
		modelMap.put("opType", request.getParameter("opType"));
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_UPDATE_DETAIL_SUCCESS, modelMap);
	}
	
	/**
	 * 跳转到异常页面
	 * @return
	 */
	public ModelAndView showGlobalException(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_EXCEPTION_SUCCESS);
	}
	
	/**
	 * 维护广告设施
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 * @throws IOException
	 */
	public ModelAndView updateAdvertFacility(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		JSONObject json = new JSONObject();
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("cinemaId"));
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("regionId"));
		advertFacilityInfo.setOrgCinema(orgCinemaInfo);
		advertFacilityInfo.setOrgCity(orgCityInfo);
		advertFacilityInfo.setOrgRegion(orgRegionInfo);
		params.put("advertFacilityKey", advertFacilityInfo);
		if(advertFacilityService.findAdvertFacilityCodeAndId(params)){
			if(advertFacilityService.updateAdvertFacility(params)){
				json.put("success", Constant.UPDATE_SUCCESS);
			}else{
				json.put("code_fail", Constant.UPDATE_FAIL);
			}
		}else{
			if(advertFacilityService.findAdvertFacilityCode(params)){
				json.put("code_fail",Constant.ADVERT_FICILITY_CODE_EXSIT);
			}else if(advertFacilityService.updateAdvertFacility(params)){
				json.put("success", Constant.UPDATE_SUCCESS);
			}else{
				json.put("code_fail", Constant.UPDATE_FAIL);
			}
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 删除广告设施
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView deleteAdvertFacility(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		JSONObject json = new JSONObject();
		params.put("advertFacilityKey", advertFacilityInfo);
		advertFacilityInfo = advertFacilityService.findAdvertFacility(params);
		if(advertFacilityInfo ==null){
			json.put("code_fail", Constant.DELETE_EXSIT);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		if(advertFacilityInfo.getFacilityStatus()==Dictionary.ADVERT_FACILITY_STATUS_USING){
			json.put("code_fail", Constant.DELETE_ADVERT_FACILITY_FAIL2);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		if(advertFacilityService.checkAdvertResourceByFacilityId(params)){
			json.put("code_fail", Constant.DELETE_ADVERT_FACILITY_FAIL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		if(advertFacilityService.deleteAdvertFacility(params)){
			json.put("success", Constant.DELETE_SUCCESS);
		}else{
			json.put("code_fail", Constant.DELETE_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 进入维护影城平面图页面
	 * @param request
	 * @param response
	 * @param floorPlanInfo
	 * @return
	 */
	public ModelAndView showUpdateFloorPlan(HttpServletRequest request, HttpServletResponse response,FloorPlanInfo floorPlanInfo){
		
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		//分页条件
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		if(StringUtils.isNotBlank(request.getParameter("selectRegionName"))){
			OrgRegionInfo orgRegion = new OrgRegionInfo();
			orgRegion.setRegionId(request.getParameter("selectRegionName"));
			floorPlanInfo.setOrgRegion(orgRegion);
		}
		if(StringUtils.isNotBlank(request.getParameter("selectCityName"))){
			OrgCityInfo orgCity = new OrgCityInfo();
			orgCity.setCityId(request.getParameter("selectCityName"));
			floorPlanInfo.setOrgCity(orgCity);
		}
		if(StringUtils.isNotBlank(request.getParameter("selectCinemaName"))){
			OrgCinemaInfo orgCinema = new OrgCinemaInfo();
			orgCinema.setCinemaId(request.getParameter("selectCinemaName"));
			floorPlanInfo.setOrgCinema(orgCinema);
		}
	
		
//		floorPlanInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		floorPlanInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		floorPlanInfo.setPageNavigationURL(CommUtil.getUrl(request));
		
		params.put("floorPlanKey", floorPlanInfo);
		
		String picStatus = request.getParameter("picStatus");
		params.put("picStatusKey", picStatus);
//		OrgCinemaInfo cinemaInfo = new OrgCinemaInfo();
//		cinemaInfo.setPageSize(GlobalConstant.PAGE_SIZE);
//		cinemaInfo.setPageNavigationURL(CommUtil.getUrl(request));
//		params.put("cinemaInfoKey", cinemaInfo);
		
		Page<FloorPlanInfo,Object[]> pageResult = advertFacilityService.findFloorPlans(params);
		pageResult.setPageNavigationURL(floorPlanInfo.getPageNavigationURL());
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		
		Map modelMap = new HashMap();
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("pageResult", pageResult);
		modelMap.put("floorPlanInfo",floorPlanInfo);
		modelMap.put("picStatus", picStatus);
		modelMap.put("orgmodel",orgModel);
		
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_UPDATE_FLOOR_PLAN_SUCCESS,modelMap);
	}
	
	/**
	 * 进入上传影城平面图页面
	 * @param request
	 * @param response
	 * @param floorPlanInfo
	 * @return
	 */
	public ModelAndView showAddFloorPlan(HttpServletRequest request, HttpServletResponse response,FloorPlanInfo floorPlanInfo){
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		Map modelMap = new HashMap();
		modelMap.put("orgRegions", orgRegions);
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_ADD_FLOOR_PLAN_SUCCESS,modelMap);
	}
	
	/**
	 * 文件上传
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ModelAndView fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject json = new JSONObject();
		json.put("msg_code",Constant.FILE_UPLOAD_FAIL);
		String result = FileUpLoadUtil.fileUpload(request, FileUpLoadUtil.CINEMA_IMAGE);
		if(result.equals(FileUpLoadUtil.CINEMA_IMAGE_SIZE_RESULT)){
			json.put("msg_code",Constant.FILE_UPLOAD_TOO_LARGE);
		}else if(result.equals(FileUpLoadUtil.FILE_UPLOAD_SIZE_RESULT)){
			json.put("msg_code",Constant.FILE_UPLOAD_SIZE_IS_ZERO);
		}else if(result.equals(FileUpLoadUtil.CINEMA_IMAGE_RESULT)){
			json.put("msg_code",Constant.NOT_JPG_JPEG_PNG);
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
	 * 新增影城平面图
	 * @param request
	 * @param response
	 * @param floorPlanInfo
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView addFloorPlan(HttpServletRequest request, HttpServletResponse response,FloorPlanInfo floorPlanInfo) throws IOException{
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		JSONObject json = new JSONObject();
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("cinemaId"));
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("regionId"));
		
		floorPlanInfo.setOrgRegion(orgRegionInfo);
		floorPlanInfo.setOrgCity(orgCityInfo);
		floorPlanInfo.setOrgCinema(orgCinemaInfo);
		
		params.put("floorPlanKey", floorPlanInfo);
		if(advertFacilityService.floorPlanExsit(params)){
			json.put("code_fail", Constant.FLOOR_PLAN_EXSIT);
		}else if(advertFacilityService.addFloorPlan(params)){
				json.put("success", Constant.ADD_SUCCESS);
		}else{
				json.put("code_fail", Constant.ADD_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 删除影城平面图URL
	 * @param request
	 * @param response
	 * @param floorPlanInfo
	 * @return
	 */
	public ModelAndView deleteFloorPlanUrl(HttpServletRequest request, HttpServletResponse response,FloorPlanInfo floorPlanInfo){
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("floorPlanKey", floorPlanInfo);
		advertFacilityService.deleteFloorPlanUrl(params);
		return new ModelAndView(new RedirectView(VIEW_ADVERTFACILITY_SHOW_DELETE_FLOOR_PLAN_URL_SUCCESS,true));
	}
	
	/**
	 * 进入修改影城平面图URL页面
	 * @param request
	 * @param response
	 * @param floorPlanInfo
	 * @return
	 */
	public ModelAndView showUpdateFloorPlanUrl(HttpServletRequest request, HttpServletResponse response,FloorPlanInfo floorPlanInfo){
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		String cinemaId = request.getParameter("cinemaId");
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		if(StringUtils.isNotBlank(cinemaId)){
			orgCinemaInfo.setCinemaId(cinemaId);
			floorPlanInfo.setOrgCinema(orgCinemaInfo);
		}
//		OrgCinemaInfo cinemaInfo = new OrgCinemaInfo();
//		cinemaInfo.setPageSize(GlobalConstant.PAGE_SIZE);
//		cinemaInfo.setPageNavigationURL(CommUtil.getUrl(request));
//		params.put("cinemaInfoKey", cinemaInfo);
		
		params.put("floorPlanKey", floorPlanInfo);
		if(floorPlanInfo.getFloorPlanId()==null || "".equals(floorPlanInfo.getFloorPlanId())){
			Page<FloorPlanInfo,Object[]> pageResult = advertFacilityService.findFloorPlans(params);
			floorPlanInfo = pageResult.getPageVOItems().get(0);
		}else{
			floorPlanInfo = advertFacilityService.findFloorPlan(params);
		}
		
		Map modelMap = new HashMap();
		modelMap.put("floorPlanInfo", floorPlanInfo);
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_UPDATE_FLOOR_PLAN_URL_SUCCESS,modelMap);
	}
	
	/**
	 * 修改影城平面图URL
	 * @param request
	 * @param response
	 * @param floorPlanInfo
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView updateFloorPlanUrl(HttpServletRequest request, HttpServletResponse response,FloorPlanInfo floorPlanInfo) throws IOException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		OrgCinemaInfo orgCinemaInfo = new OrgCinemaInfo();
		orgCinemaInfo.setCinemaId(request.getParameter("cinemaId"));
		OrgCityInfo orgCityInfo = new OrgCityInfo();
		orgCityInfo.setCityId(request.getParameter("cityId"));
		OrgRegionInfo orgRegionInfo = new OrgRegionInfo();
		orgRegionInfo.setRegionId(request.getParameter("regionId"));
	
		floorPlanInfo.setOrgRegion(orgRegionInfo);
		floorPlanInfo.setOrgCity(orgCityInfo);
		floorPlanInfo.setOrgCinema(orgCinemaInfo);
		
		params.put("floorPlanKey", floorPlanInfo);
		if(advertFacilityService.floorPlanExsit(params)){
			if(advertFacilityService.updateFloorPlanUrl(params)){
				json.put("success", Constant.UPDATE_SUCCESS);
				
			}else{
				json.put("code_fail", Constant.SAVE_FAIL);
			}
		}else{
//			json.put("code_fail", Constant.CINEMA_NOT_EXSIT);
			if(advertFacilityService.addFloorPlan(params)){
				json.put("success", Constant.SAVE_SUCCESS);
			}else{
				json.put("code_fail", Constant.SAVE_FAIL);
			}
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 进入设置广告设施位置页面
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 */
	public ModelAndView showSetAdvertFacilityLocation(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo){
		
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		Map params = new HashMap();
		
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		advertFacilityInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		advertFacilityInfo.setPageNavigationURL(CommUtil.getUrl(request));
		
		//分页条件
		if(StringUtils.isNotBlank(request.getParameter("selectRegionName"))){
			OrgRegionInfo orgRegion = new OrgRegionInfo();
			orgRegion.setRegionId(request.getParameter("selectRegionName"));
			advertFacilityInfo.setOrgRegion(orgRegion);
		}
		if(StringUtils.isNotBlank(request.getParameter("selectCityName"))){
			OrgCityInfo orgCity = new OrgCityInfo();
			orgCity.setCityId(request.getParameter("selectCityName"));
			advertFacilityInfo.setOrgCity(orgCity);
		}
		if(StringUtils.isNotBlank(request.getParameter("selectCinemaName"))){
			OrgCinemaInfo orgCinema = new OrgCinemaInfo();
			orgCinema.setCinemaId(request.getParameter("selectCinemaName"));
			advertFacilityInfo.setOrgCinema(orgCinema);
		}
		
		params.put("advertFacilityKey", advertFacilityInfo);
		Page<AdvertFacilityInfo, AdvertFacility> pageResult = advertFacilityService.findAdvertFacilitysAndResourceTotal(params);
		pageResult.setPageNavigationURL(advertFacilityInfo.getPageNavigationURL());
		
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		
		Map modelMap = new HashMap();
		FloorPlanInfo floorPlanInfo = advertFacilityService.findFloorPlanByRegionId(params);
		modelMap.put("orgRegions", orgRegions);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		modelMap.put("pageResult", pageResult);
		modelMap.put("advertFacilityType", advertFacilityType);
		modelMap.put("floorPlanInfo", floorPlanInfo);
		modelMap.put("advertFacilityInfo", advertFacilityInfo);
		modelMap.put("orgmodel", orgModel);
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_SET_ADVERTFACILITY_LOCATION_SUCCESS,modelMap);
	}
	
	/**
	 * 进入广告资源配置页面
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 */
	public ModelAndView showAddAdvertResource(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo){
		Map params = new HashMap();
		advertFacilityInfo.setPageSize(GlobalConstant.PAGE_SIZE);
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		
		params.put("advertFacilityKey", advertFacilityInfo);
		advertFacilityInfo = advertFacilityService.findAdvertFacility(params);
		advertFacilityInfo.setPageNavigationURL(CommUtil.getUrl(request));
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADVERT_RESOURCE_STATUS);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertResourceStatus = dictionaryService.findDataDictionarys(params);
		
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarys(params);
		
		Page<AdvertResourceInfo, AdvertResource> pageResult = advertFacilityService.findAdvertResources(params);
		pageResult.setPageNavigationURL(advertFacilityInfo.getPageNavigationURL());
		List<AdvertResourceInfo> advertResourceInfos = pageResult.getPageVOItems();
		String code = getAdvertCode(advertResourceInfos);
		
		Map modelMap = new HashMap();
		modelMap.put("advertFacilityInfo", advertFacilityInfo);
		modelMap.put("advertResourceStatus", advertResourceStatus);
		modelMap.put("pageResult", pageResult);
		modelMap.put("code",code);
		modelMap.put("advertFacilityType", advertFacilityType);
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_ADD_ADVERT_RESOURCE_SUCCESS,modelMap);
	}
	
	
	/**
	 * 自动获取广告资源编码
	 * @param list
	 * @author milihua
	 */
	public String getAdvertCode(List<AdvertResourceInfo> advertResourceInfos){
		String code = "01";
		String code1="";
		if(Utils.isNotEmpty(advertResourceInfos)){
			AdvertResourceInfo arif = advertResourceInfos.get(0);
	        String[] resourceCodes = arif.getResourceCode().split("-");
	        code = resourceCodes[resourceCodes.length-1];
			for(int i = 0;i<advertResourceInfos.size();i++){
				//AdvertResourceInfo arif = advertResourceInfos.get(0);
				AdvertResourceInfo info = advertResourceInfos.get(i);
				//String[] resourceCodes = arif.getResourceCode().split("-");
				String[] resourceCodes1 = info.getResourceCode().split("-");
				//code = resourceCodes[resourceCodes.length-1];
				code1 = resourceCodes1[resourceCodes1.length-1];
				
				if(Integer.parseInt(code) < Integer.parseInt(code1)){
					code = code1;
				}
			}
			if(Integer.parseInt(code)+1 < 10){
				code = Integer.parseInt(code)+1+"";
				code="0"+code;
			}else{
				code =Integer.parseInt(code)+1+"";
			}
		}
        
		return code;
	}
	
	/**
	 * 新增资源
	 * @param request
	 * @param response
	 * @param advertResourceInfo
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView addAdvertResource(HttpServletRequest request, HttpServletResponse response,AdvertResourceInfo advertResourceInfo) throws IOException{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		JSONObject json = new JSONObject();
		AdvertFacilityInfo advertFacilityInfo = new AdvertFacilityInfo();
		advertFacilityInfo.setFacilityId(request.getParameter("facilityId"));
		advertResourceInfo.setAdvertFacility(advertFacilityInfo);
		params.put("advertResourceKey", advertResourceInfo);
		if(advertFacilityService.findAdvertResourceByCode(params)){
			json.put("code_fail", Constant.ADVERT_RESOURCE_CODE_EXSIT);
		}else if(advertFacilityService.addAdvertResource(params)){
			json.put("success", Constant.ADD_SUCCESS);
		}else{
			json.put("code_fail", Constant.ADD_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 删除资源
	 * @param request
	 * @param response
	 * @param advertResourceInfo
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView deleteAdvertResource(HttpServletRequest request, HttpServletResponse response,AdvertResourceInfo advertResourceInfo) throws IOException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("advertResourceKey", advertResourceInfo);
		if(!advertFacilityService.findAdvertResourceById(params)){
			json.put("code_fail", Constant.DELETE_EXSIT);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		if(advertFacilityService.checkAdvertPlanByResourceId(params)){
			json.put("code_fail", Constant.DELETE_ADVERT_RESOURCE_FAIL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		if(advertFacilityService.deleteAdvertResource(params)){
			json.put("success", Constant.DELETE_SUCCESS);
		}else{
			json.put("code_fail", Constant.DELETE_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	
	/**
	 * 修改资源价格
	 * @param request
	 * @param response
	 * @param advertResourceInfo
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView updateResourcePrice(HttpServletRequest request, HttpServletResponse response,AdvertResourceInfo advertResourceInfo) throws IOException{
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("advertResourceKey", advertResourceInfo);
		if(!advertFacilityService.findAdvertResourceById(params)){
			json.put("code_fail", Constant.UPDATE_FAIL_NOT_EXSIT);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}
		if(advertFacilityService.updateResourcePrice(params)){
			json.put("success", Constant.UPDATE_SUCCESS);
		}else{
			json.put("success", Constant.UPDATE_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	/**
	 * 进入固定广告设施位置页面
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @return
	 */
	public ModelAndView showFixAdvertFacilityLocation(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo){
		Map params = new HashMap();
		List<AdvertIconInfo> advertIcons = advertIconService.findAdvertIcon();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
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
		//影城信息
		FloorPlanInfo floorPlanInfo= advertFacilityService.findFloorPlanByRegionId(params);
		
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		int typePages = 1;
		if(request.getParameter("typePages")!=null){
			typePages = Integer.valueOf(request.getParameter("typePages"));
			params.put("typePagesKey", typePages);
		}else{
			params.put("typePagesKey", 1);
		}
		//类型分页
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarysByPages(params);
		long typeTotalPages = dictionaryService.countDataDictionary(params);
		
		//设施状态
		data.setFDataId(Dictionary.ADVERT_FACILITY_STATUS);
		params.put("dataDictionaryInfoKey", data);
		List<DataDictionaryInfo> advertFacilityStatus = dictionaryService.findDataDictionarys(params);
		
		Map modelMap = new HashMap();
		//展示设施tab
		long tag = 0;
		if(request.getParameter("tag")!=null){
			modelMap.put("tag", request.getParameter("tag"));
			tag = Long.valueOf(request.getParameter("tag"));
		}else{
			modelMap.put("tag", Dictionary.ADVERT_FACILITY_DZHBDX);
			tag = Dictionary.ADVERT_FACILITY_DZHBDX;
		}
		int firstResult = 1;
		params.put("iconFirstResultKey", firstResult);
		advertFacilityInfo.setFacilityType(tag);
		params.put("advertFacilityKey", advertFacilityInfo);
		//已固定位置的设施
		List<AdvertFacilityInfo> advertFacilityInfoWithLocations = advertFacilityService.findAdvertFacilityWithLocation(params);
		//未固定位置的设施
		List<AdvertFacilityInfo> advertFacilityInfoNotSetLocations = advertFacilityService.findAdvertFacilityNotSetLocation(params);
		modelMap.put("floorPlanInfo", floorPlanInfo);
		modelMap.put("advertFacilityType",advertFacilityType);
		modelMap.put("advertFacilityTypeJSON", JSONArray.fromObject(advertFacilityType));
		modelMap.put("advertFacilityStatus", advertFacilityStatus);
		modelMap.put("advertFacilityInfoWithLocationsJSON", JSONArray.fromObject(advertFacilityInfoWithLocations));
		modelMap.put("advertFacilityInfoNotSetLocationsJSON", JSONArray.fromObject(advertFacilityInfoNotSetLocations));
		modelMap.put("iconTotalPages",advertFacilityService.countAdvertFacilityNotSetLocation(params));
		modelMap.put("typeTotalPages",typeTotalPages);
		modelMap.put("typePages",typePages);
		modelMap.put("advertIconsJSON", JSONArray.fromObject(advertIcons));
		return new ModelAndView(VIEW_ADVERTFACILITY_SHOW_FIX_ADVERT_FACILITY_SUCCESS,modelMap);
	}
	
	/**
	 * 设施图标分页
	 * @param request
	 * @param response
	 * @param floorPlanInfo
	 * @throws IOException
	 */
	public void findAdvertFacilitysForIconPage(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		//JSONObject json = new JSONObject();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);

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
		int firstResult = Integer.valueOf(request.getParameter("iconPages"));
		params.put("iconFirstResultKey",firstResult);
		List<AdvertFacilityInfo> advertFacilityInfoNotSetLocations = advertFacilityService.findAdvertFacilityNotSetLocation(params);
		//json.put("advertFacilityInfoNotSetLocations", JSONArray.fromObject(advertFacilityInfoNotSetLocations));
		IOUtil.writeJSONArray(JSONArray.fromObject(advertFacilityInfoNotSetLocations), response.getOutputStream());
	}
	
	/**
	 * 设施类型分页
	 * @param request
	 * @param response
	 * @param floorPlanInfo
	 * @throws IOException
	 */
	public void findAdvertFacilityTypeForPage(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo) throws IOException{
		Map params = new HashMap();
		
		DataDictionaryInfo data = new DataDictionaryInfo();
		data.setFDataId(Dictionary.ADERVERT_FACILITY_TYPE);
		params.put("dataDictionaryInfoKey", data);
		int firstResult = Integer.valueOf(request.getParameter("typePages"));
		params.put("typePagesKey",firstResult);
		List<DataDictionaryInfo> advertFacilityType = dictionaryService.findDataDictionarysByPages(params);
		IOUtil.writeJSONArray(JSONArray.fromObject(advertFacilityType), response.getOutputStream());
	}
	
	/**
	 * 更新广告设施位置
	 * @param request
	 * @param response
	 * @param advertFacilityInfo
	 * @throws IOException
	 */
	public void updateAdvertFacilityLocation(HttpServletRequest request, HttpServletResponse response,AdvertFacilityInfo advertFacilityInfo) throws IOException{
		JSONObject json = new JSONObject();
		Map params = new HashMap();
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("locationsKey", request.getParameter("locations"));
		if(advertFacilityService.updateAdvertFacilityLocation(params)){
			json.put("success", Constant.ADVERT_FACILITY_LOCATION_SUCCESS);
		}else{
			json.put("code_fail",Constant.ADVERT_FACILITY_LOCATION_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream());
	}
	
	/**
	 * 删除上传的图片，文件
	 * */
	public void delPic(HttpServletRequest request, HttpServletResponse response,FloorPlanInfo floorPlanInfo) throws Exception{
		String filePath = request.getSession().getServletContext().getRealPath("/")+floorPlanInfo.getFloorPlanUrl();
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		JSONObject json = new JSONObject();
		json.put("success", "success1");
		IOUtil.writeJSON(json, response.getOutputStream());
	}
}
