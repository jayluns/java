package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

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
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.kd.ais.security.entity.UserTab;
import com.kingdee.ais.core.constant.GlobalConstant;
import com.kingdee.ais.core.entity.OrgModel;
import com.kingdee.ais.core.pagination.Page;
import com.kingdee.ais.core.util.CommUtil;
import com.kingdee.ais.core.util.Utils;
import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.advert.service.IAvertDiscountSerice;
import com.kingdee.ais.ibmp.business.org.service.IOrgCinemaService;
import com.kingdee.ais.ibmp.business.org.service.IOrgCityService;
import com.kingdee.ais.ibmp.business.org.service.IOrgRegionService;
import com.kingdee.ais.ibmp.model.pojo.advert.AdvertDiscount;
import com.kingdee.ais.ibmp.model.vo.advert.AdvertDiscountInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCinemaInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCityInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgRegionInfo;
import com.kingdee.ais.ibmp.view.common.JudgementAuthority;
import com.kingdee.ais.ibmp.view.constant.Constant;
import com.kingdee.ais.ibmp.view.util.IOUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdvDiscountController extends MultiActionControllerImpl{
	private static final Logger logger = Logger.getLogger(AdvDiscountController.class);
	
	private IOrgRegionService orgRegionService;
	public IOrgCityService getOrgCityService() {
		return orgCityService;
	}
	public void setOrgCityService(IOrgCityService orgCityService) {
		this.orgCityService = orgCityService;
	}


	private IAvertDiscountSerice avertDiscountService;
	private IOrgCinemaService orgCinemaService;
	private IOrgCityService orgCityService;
	
	public IOrgCinemaService getOrgCinemaService() {
		return orgCinemaService;
	}
	public void setOrgCinemaService(IOrgCinemaService orgCinemaService) {
		this.orgCinemaService = orgCinemaService;
	}
	public IAvertDiscountSerice getAvertDiscountService() {
		return avertDiscountService;
	}
	public void setAvertDiscountService(IAvertDiscountSerice avertDiscountService) {
		this.avertDiscountService = avertDiscountService;
	}
	public IOrgRegionService getOrgRegionService() {
		return orgRegionService;
	}
	public void setOrgRegionService(IOrgRegionService orgRegionService) {
		this.orgRegionService = orgRegionService;
	}


	private static final String VIEW_SET_DISCOUNT_SUCCESS = "views_set_discount_success";
	private static final String VIEW_ADD_DISCOUNT_LIST = "view_add_discount_list";
	private static final String VIEW_MODIFY_USERDISCOUNT = "view_modify_userdiscount";
	
	/**
	 * @Title: setDiscount
	 * @Description: TODO(用户折扣设置)
	 * @param @param request
	 * @param @param response
	 * @param @param advertDiscountInfo
	 * @param @return
	 * @param @throws Exception
	 * @return ModelAndView
	 * @author milihua
	 * @date 2012-08-21上午10:19:33
	 */
	public ModelAndView setDiscount(HttpServletRequest request,HttpServletResponse response,AdvertDiscountInfo advertDiscountInfo) throws Exception{
		Map advDiscountmap = new HashMap();
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		
		
		advDiscountmap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		
		advertDiscountInfo.setPageSize(GlobalConstant.PAGE_SIZE);// 每页显示10条数据
		advertDiscountInfo.setPageNavigationURL(CommUtil.getUrl(request));
		advDiscountmap.put("advertDiscountInfo", advertDiscountInfo);
//		advDiscountmap.put("listorgmodelskey", listorgModels);
		
		String regionid = request.getParameter("orgRegion.regionId");
		String cityid = request.getParameter("orgCity.cityId");
		String cinemaid = request.getParameter("orgCinema.cinemaId");
		
		advDiscountmap.put("regionid",regionid);
		advDiscountmap.put("cityid",cityid);
		advDiscountmap.put("cinemaid",cinemaid);
		
		
		
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		
		UserTab userTab = JudgementAuthority.getUserInfo(request);
		
		advDiscountmap.put(GlobalConstant.USER_KEY, userTab);
		
		//分页查询
		Page<AdvertDiscountInfo, Object[]> pageResult = avertDiscountService.findADiscountListnInfos(advDiscountmap);
		pageResult.setPageNavigationURL(advertDiscountInfo.getPageNavigationURL());
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		//页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("orgmodel",orgModel);
		modelMap.put("regionid",regionid);
		modelMap.put("cityid",cityid);
		modelMap.put("cinemaid",cinemaid);
		modelMap.put("advertDiscountInfo",advertDiscountInfo);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		setQueryParamesJson(request, response, modelMap);
		return new ModelAndView(VIEW_SET_DISCOUNT_SUCCESS, modelMap);
		
	}
	
	/**
	 * @Title: queryUserInfo
	 * @Description: TODO(查询用户)
	 * @param @param request
	 * @param @param response
	 * @param @param advertDiscountInfo
	 * @param @return
	 * @param @throws Exception
	 * @return ModelAndView
	 * @author milihua
	 * @date 2012-08-24上午10:26:12
	 */
	public ModelAndView queryUserInfo(HttpServletRequest request,HttpServletResponse response,AdvertDiscountInfo advertDiscountInfo) throws Exception{
		Map advDiscountmap = new HashMap();
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		
		
		advDiscountmap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		
		advertDiscountInfo.setPageSize(GlobalConstant.PAGE_SIZE);// 每页显示10条数据
		advertDiscountInfo.setPageNavigationURL(CommUtil.getUrl(request));
		advDiscountmap.put("advertDiscountInfo", advertDiscountInfo);
		
		String regionid = request.getParameter("orgRegion.regionId");
		String cityid = request.getParameter("orgCity.cityId");
		String cinemaid = request.getParameter("orgCinema.cinemaId");
		String name = request.getParameter("userName");
		
		advDiscountmap.put("regionid",regionid);
		advDiscountmap.put("cityid",cityid);
		advDiscountmap.put("cinemaid",cinemaid);
		advDiscountmap.put("userName",name);
		
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		
		UserTab userTab = JudgementAuthority.getUserInfo(request);
		advDiscountmap.put(GlobalConstant.USER_KEY, userTab);
		//分页查询
		Page<AdvertDiscountInfo, Object[]> pageResult = avertDiscountService.queryUserInfos(advDiscountmap);
		pageResult.setPageNavigationURL(advertDiscountInfo.getPageNavigationURL());
		
		//页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("orgmodel",orgModel);
		modelMap.put("regionid",regionid);
		modelMap.put("cityid",cityid);
		modelMap.put("cinemaid",cinemaid);
		modelMap.put("advertDiscountInfo", advertDiscountInfo);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		
		return new ModelAndView(VIEW_ADD_DISCOUNT_LIST, modelMap);
		
	}
	
	
	
	/**
	 * @Title:       setParamForOrgCodes 
	 * @Description: TODO() 
	 * @author       milihua
	 * @param        @param request    
	 * @return       List<OrgModel>   
	 * @throws 
	 */ 
	private List<OrgModel> setParamForOrgCodes(HttpServletRequest request) {
		String regionid = request.getParameter("selectRegionName");
		String cityid = request.getParameter("selectCityName");
		String cinemaid = request.getParameter("selectCinemaName");
		
		List<OrgModel> listModels=new ArrayList<OrgModel>();
		
		if(Utils.isNotEmpty(regionid)&&Utils.isNotEmpty(cityid)&&(Utils.isNotEmpty(cinemaid))){
			OrgCinemaInfo orgcinemainfo =orgCinemaService.getCinemaInfoById(cinemaid);
			 listModels =orgRegionService.findOrgModelBycodes(orgcinemainfo.getCinemaCode());
		}else if(Utils.isNotEmpty(regionid)&&Utils.isNotEmpty(cityid)&&Utils.isEmpty(cinemaid)){
			OrgCityInfo orgcityinfo=orgCityService.getOrgCityInfoById(cityid);
			 listModels=orgRegionService.findOrgModelBycodes(orgcityinfo.getCityCode());
		}else if(Utils.isNotEmpty(regionid)&&Utils.isEmpty(cityid)&&Utils.isEmpty(cinemaid)){
			OrgRegionInfo regioninfo=orgRegionService.getOrgRegionInfoById(regionid);
			 listModels=orgRegionService.findOrgModelBycodes(regioninfo.getRegionCode());
		}else if(Utils.isEmpty(regionid)&&Utils.isEmpty(cityid)&&Utils.isEmpty(cinemaid)){
			 listModels=null;
		}
		return listModels;
	}
	
	/**
	 * 
	 * @Title:       to_request_addDiscountView 
	 * @Description: TODO(跳转新增页面) 
	 * @author       milihua
	 * @param        @param request
	 * @param        @param response
	 * @param        @return
	 * @param        @throws Exception    
	 * @return       ModelAndView   
	 * @throws
	 */
	public ModelAndView to_request_addDiscountView(HttpServletRequest request,
			HttpServletResponse response,AdvertDiscountInfo advertDiscountInfo) throws Exception {

		Map advDiscountmap = new HashMap();
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
//		
		advDiscountmap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		
		UserTab userTab = JudgementAuthority.getUserInfo(request);
		
		advDiscountmap.put(GlobalConstant.USER_KEY, userTab);
		
		advertDiscountInfo.setPageSize(GlobalConstant.PAGE_SIZE);// 每页显示10条数据
		advertDiscountInfo.setPageNavigationURL(CommUtil.getUrl(request));
		advDiscountmap.put("advertDiscountInfo", advertDiscountInfo);
		
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		
		//分页查询
		Page<AdvertDiscountInfo, Object[]> pageResult = avertDiscountService.queryUserInfos(advDiscountmap);
		pageResult.setPageNavigationURL(advertDiscountInfo.getPageNavigationURL());
		OrgModel orgModel=getModelsByUserCode(request, orgModels);
		//页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("orgmodel",orgModel);
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		return new ModelAndView(VIEW_ADD_DISCOUNT_LIST, modelMap);
		
	}
	
	/**
	 * @Title: addDiscount
	 * @Description: TODO(增加用户折扣)
	 * @param @param request
	 * @param @param response
	 * @param @param advertDiscountInfo
	 * @param @return
	 * @param @throws Exception
	 * @return ModelAndView
	 * @author milihua
	 * @date 2012-08-21
	 */
	public ModelAndView addDiscount(HttpServletRequest request,HttpServletResponse response,AdvertDiscountInfo advertDiscountInfo) throws Exception{
		List<OrgModel> listmodelList=initOrgModels(request);
		Map params = new HashMap();
		
		params.put("advertDiscountInfokey", advertDiscountInfo);
		params.put("listmodelListkey", listmodelList);
		
		String selectResource = request.getParameter("selectResource");
		params.put("selectResourceKey", selectResource);
		JSONObject json = new JSONObject();
		boolean res = avertDiscountService.saveDiscount(params);
		if (!res) {
			json.put("failure", Constant.ADD_FAIL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}else {
			json.put("success", Constant.ADD_SUCCESS);
			IOUtil.writeJSON(json, response.getOutputStream());
		}
//		setDiscount(request,response,advertDiscountInfo);
		return null;
	}
	
	/**
	 * @Description: TODO(删除用户信息)
	 * @Title:       deleteUserInfo 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertDiscountInfo
	 * @param 		 @return
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-8-22 下午06:19:45
	 * @throws
	 */
	public ModelAndView deleteUserDiscount(HttpServletRequest request, HttpServletResponse response, AdvertDiscountInfo advertDiscountInfo)throws Exception {
		Map params = new HashMap(); 
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		
		params.put("advertDiscountInfokey", advertDiscountInfo);
		
		JSONObject json = new JSONObject();
//		UserTab userTab = new UserTab();
//		userTab.setId(userVo.getId());
//		userManagerService.deleteUserInfoByUserId(userTab);
		boolean res = avertDiscountService.deleteUserDiscount(advertDiscountInfo);
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
	 * @Description: TODO(根据用户userId修改用户折扣信息)
	 * @Title:       modifyDiscount 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param advertDiscountInfo
	 * @param 		 @throws Exception
	 * @return       ModelAndView
	 * @author       milihua  
	 * @date         2012-8-22 下午 21:19:45
	 * @throws
	 */
	public ModelAndView findDiscountInfoByUsrtId(HttpServletRequest request,HttpServletResponse response,AdvertDiscountInfo advertDiscountInfo) throws Exception{
		Map modelMap = new HashMap();
		UserTab user = avertDiscountService.findUserTabByUserId(advertDiscountInfo.getUserId());
		modelMap.put("advertDiscountInfo", advertDiscountInfo);
		modelMap.put("user", user);
		return new ModelAndView(VIEW_MODIFY_USERDISCOUNT, modelMap);
	}
	
	/**
	 * @Description: TODO(更新用户折扣 )
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param userVo
	 * @param 		 @throws Exception
	 * @return       null
	 * @author       milihua  
	 * @date         2012-8-22 下午 22:21:45
	 * @throws
	 */
	public ModelAndView updateDiscount(HttpServletRequest request,HttpServletResponse response,AdvertDiscountInfo advertDiscountInfo) throws Exception{
		List<OrgModel> listmodelList=initOrgModels(request);
		Map params = new HashMap();
		
		params.put("advertDiscountInfokey", advertDiscountInfo);
		params.put("listmodelListkey", listmodelList);
		JSONObject json = new JSONObject();
		String select = request.getParameter("select");
		String selectResource = request.getParameter("selectResource");
		params.put("selectKey", select);
		params.put("selectResourceKey", selectResource);
		boolean res = avertDiscountService.updateDiscount(params);
		if (!res) {
			json.put("failure", Constant.UPDATE_FAIL);
			IOUtil.writeJSON(json, response.getOutputStream());
			return null;
		}else {
			json.put("success", Constant.UPDATE_SUCCESS);
			IOUtil.writeJSON(json, response.getOutputStream());
		}
		return null;
	}
	
	/**
	 * 查询--总部用户
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView findUserInfosByJY(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);  //获取用户权限集合
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		
		Page<AdvertDiscountInfo, Object[]> pageResult = avertDiscountService.findUserInfosByJY();
//		pageResult.setPageNavigationURL(advertDiscountInfo.getPageNavigationURL());
		
//		List<UserVo> users = new ArrayList<UserVo>();
//		users = userManagerService.findUserInfosByJY();
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		//页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		
//		if(Utils.isNotEmpty(pageResult)){
//			IOUtil.writeJSONArray(JSONArray.fromObject(pageResult,IOUtil.getConfigForUser()), response.getOutputStream());
//		}
		return new ModelAndView(VIEW_ADD_DISCOUNT_LIST, modelMap);
		
		
	}
	
	/**
	 * 查询大区的用户
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView findUserInfosByRegionId(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);  //获取用户权限集合
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		// 初始化区域,城市,影院
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		String regionId = request.getParameter("regionId");
		Page<AdvertDiscountInfo, Object[]> pageResult = null;
		if(StringUtils.isNotBlank(regionId)){
			pageResult = avertDiscountService.findUserInfosByRegionId(regionId);
			if(pageResult!=null){
				IOUtil.writeJSONArray(JSONArray.fromObject(pageResult,IOUtil.getConfigForUser()), response.getOutputStream());
			}
		}

//		//页面数据显示
//		Map modelMap = new HashMap();
//		modelMap.put("pageResult", pageResult); // 列表数据
//		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
//		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		return null;
	}
	
	/**
	 * 城市的用户
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView findUserInfosByCityId(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);  //获取用户权限集合
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
//		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		String cityId = request.getParameter("cityId");
		Page<AdvertDiscountInfo, Object[]> pageResult = null;
		if(StringUtils.isNotBlank(cityId)){
			pageResult = avertDiscountService.findUserInfosByCityId(cityId);
			if(pageResult!=null){
				IOUtil.writeJSONArray(JSONArray.fromObject(pageResult,IOUtil.getConfigForUser()), response.getOutputStream());
			}
		}
		return null;
	}	
	
	/**
	 * 根据组织机构-影院ID查询用户
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public ModelAndView showUserList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String cinemaId = request.getParameter("cinemaId");
		Page<AdvertDiscountInfo, Object[]> pageResult = null;
		if(StringUtils.isNotBlank(cinemaId)){
			pageResult = avertDiscountService.findUserInfosByCinemaId(cinemaId);
//			if(pageResult!=null){
//				IOUtil.writeJSONArray(JSONArray.fromObject(pageResult,IOUtil.getConfigForUser()), response.getOutputStream());
//			}
		}

		//页面数据显示
		// 初始化区域,城市,影院
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);  //获取用户权限集合
		params.put(GlobalConstant.USER_CODE_KEY,orgModels);
		List<OrgRegionInfo> orgRegions = orgRegionService.findOrgRegions(params);
		Map modelMap = new HashMap();
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("orgRegions", orgRegions);//区域，城市，影院数据
		modelMap.put("orgRegionsJSON", JSONArray.fromObject(orgRegions,IOUtil.getConfig()));
		return new ModelAndView(VIEW_ADD_DISCOUNT_LIST, modelMap);
//		return null;
	}	
}
