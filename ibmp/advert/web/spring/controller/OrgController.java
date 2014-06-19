package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.kd.ais.security.entity.UserTab;
import com.kd.ais.security.service.IUserManagerService;
import com.kingdee.ais.core.constant.GlobalConstant;
import com.kingdee.ais.core.constant.GlobalConstant.AREA_LEVEL;
import com.kingdee.ais.core.entity.OrgModel;
import com.kingdee.ais.core.util.Utils;
import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.StringUtil;
import com.kingdee.ais.ibmp.business.org.service.IOrgCinemaService;
import com.kingdee.ais.ibmp.business.org.service.IOrgCityService;
import com.kingdee.ais.ibmp.business.org.service.IOrgProvinceService;
import com.kingdee.ais.ibmp.business.org.service.IOrgRegionService;
import com.kingdee.ais.ibmp.model.vo.org.OrgCinemaInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgCityInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgProvinceInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgRegionInfo;
import com.kingdee.ais.ibmp.model.vo.org.OrgZTree;
import com.kingdee.ais.ibmp.model.vo.org.UserOrgInfo;
import com.kingdee.ais.ibmp.view.common.JudgementAuthority;
import com.kingdee.ais.ibmp.view.util.IOUtil;

/**
 * 组织查询
 * 
 * @author ldw
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrgController extends MultiActionControllerImpl {
	private IOrgRegionService	orgRegionService;
	private IOrgProvinceService	orgProvinceService;
	private IOrgCityService		orgCityService;
	private IOrgCinemaService	orgCinemaService;
	private IUserManagerService	userManagerService;

	/**
	 * 组织管理页
	 */
	public static final String	VIEW_SHOW_ORGANIZATION_SUCCESS		= "views_request_show_organization_success";
	public static final String	VIEW_SELECT_ORGANIZATION_SUCCESS	= "views_request_select_organization_success";

	public void setUserManagerService(IUserManagerService userManagerService) {
		this.userManagerService = userManagerService;
	}

	public void setOrgRegionService(IOrgRegionService orgRegionService) {
		this.orgRegionService = orgRegionService;
	}

	public void setOrgProvinceService(IOrgProvinceService orgProvinceService) {
		this.orgProvinceService = orgProvinceService;
	}

	public void setOrgCityService(IOrgCityService orgCityService) {
		this.orgCityService = orgCityService;
	}

	public void setOrgCinemaService(IOrgCinemaService orgCinemaService) {
		this.orgCinemaService = orgCinemaService;
	}

	/**
	 * 大区
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public void queryOrgRegion(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		JSONArray jsonArray = JSONArray.fromObject(orgRegionService.findOrgRegions(params));
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}

	/**
	 * 省份
	 * 
	 * @param request
	 * @param response
	 * @param orgRegionInfo
	 * @return
	 * @throws IOException
	 */
	public void queryOrgProvince(HttpServletRequest request, HttpServletResponse response, OrgRegionInfo orgRegionInfo) throws IOException {
		Map params = new HashMap();
		params.put("orgRegionInfoKey", orgRegionInfo);
		JSONArray jsonArray = JSONArray.fromObject(orgProvinceService.findOrgProvinces(params));
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}

	/**
	 * @功能：根据用户的业务组织和区来确定城市
	 * @作者：dahuo
	 * @日期：2013-5-3
	 */
	public void queryOrgCity(HttpServletRequest request, HttpServletResponse response, OrgRegionInfo orgRegionInfo) throws IOException {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("orgRegionInfoKey", orgRegionInfo);
		setRegionCityCinemaLimit(request, params);

		JSONArray jsonArray = JSONArray.fromObject(orgCityService.findOrgCitysByRegionId(params));
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}

	/**
	 * 根据城市名称模糊查询城市以及大区、影院信息
	 * 
	 * @param request
	 * @param response
	 * @param orgProvinceInfo
	 * @return
	 * @throws IOException
	 */
	public void queryOrgCityByName(HttpServletRequest request, HttpServletResponse response, OrgCityInfo orgCityInfo) throws IOException {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		request.getCharacterEncoding();
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("orgCityInfoKey", orgCityInfo);
		setRegionCityCinemaLimit(request, params);
		JSONArray jsonArray = JSONArray.fromObject(orgCityService.findOrgCityByCityName(params));
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}

	/**
	 * 根据影院名称模糊查询城市以及大区、影院信息
	 * 
	 * @param request
	 * @param response
	 * @param orgProvinceInfo
	 * @return
	 * @throws IOException
	 */
	public void queryOrgCityByCinemaName(HttpServletRequest request, HttpServletResponse response, OrgCinemaInfo orgCinemaInfo) throws IOException {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		request.getCharacterEncoding();
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("orgCinemaInfoKey", orgCinemaInfo);
		setRegionCityCinemaLimit(request, params);
		JSONArray jsonArray = JSONArray.fromObject(orgCityService.findOrgCityByCinemaName(params));
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}

	/**
	 * 城市名称模糊查询
	 * 
	 * @param request
	 * @param response
	 * @param orgProvinceInfo
	 * @return
	 * @throws IOException
	 */
	public void queryOrgCityName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		setRegionCityCinemaLimit(request, params);
		json.put("cityNames", orgCityService.findOrgCityName(params));
		IOUtil.writeJSON(json, response.getOutputStream());
	}

	/**
	 * 影院名称模糊查询
	 * 
	 * @param request
	 * @param response
	 * @param orgProvinceInfo
	 * @return
	 * @throws IOException
	 */
	public void queryOrgCinemaName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map params = new HashMap();
		JSONObject json = new JSONObject();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		setRegionCityCinemaLimit(request, params);
		json.put("cinemaNames", orgCinemaService.findOrgCinemaName(params));
		IOUtil.writeJSON(json, response.getOutputStream());
	}

	/**
	 * @功能：根据用户的业务组织和城市来确定影城
	 * @作者：dahuo
	 * @日期：2013-5-3
	 */
	public void queryOrgCinema(HttpServletRequest request, HttpServletResponse response, OrgCityInfo orgCityInfo) throws IOException {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("orgCityInfoKey", orgCityInfo);
		setRegionCityCinemaLimit(request, params);
		JSONArray jsonArray = JSONArray.fromObject(orgCinemaService.findOrgCinemas(params));
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}

	/**
	 * 组织结构显示
	 * 
	 * @author daniel.yan
	 * @created 2012-5-21 10:48:09
	 * 
	 * @return
	 */
	public ModelAndView showOrganization(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView(VIEW_SHOW_ORGANIZATION_SUCCESS);
	}

	public void showOrgTree(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserTab userInfo=JudgementAuthority.getUserInfo(request);
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		if(userInfo.getUsername().equals("admin")){
		    orgModels.get(0).setLevel(AREA_LEVEL.JY);
		    UserOrgInfo userOrgInfo = orgRegionService.getUserOrgInfo(params);
		    userOrgInfo.setAreaLevel(null);
		    params.put("userOrgInfo", userOrgInfo);
		}else{
		    UserOrgInfo[] userOrgInfos = orgRegionService.getUserOrgInfos(params);
		    params.put("userOrgInfos", userOrgInfos);
		}
		JSONArray jsonArray = JSONArray.fromObject(orgRegionService.findOrgAuthorizedToZTree(params));
		// JSONArray jsonArray = JSONArray.fromObject(orgRegionService.findAllToZTree());
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}

	public ModelAndView selectOrganization(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView(VIEW_SELECT_ORGANIZATION_SUCCESS);
	}

	public void saveNode(HttpServletRequest request, HttpServletResponse response, OrgZTree orgZTree) throws Exception {
		String newOrgId = "";
		if (orgZTree.getSaveType() == 1) {// 新增
			if (orgZTree.getDepth() == 0) {
				OrgRegionInfo orgRegionInfo = orgZTree.toRegionInfo();
				orgRegionService.addOrgRegion(orgRegionInfo);
				newOrgId = orgRegionInfo.getRegionId();
			} else if (orgZTree.getDepth() == 1) {
				OrgProvinceInfo orgProvinceInfo = orgZTree.toProvinceInfo();
				orgProvinceService.addOrgProvince(orgProvinceInfo);
				newOrgId = orgProvinceInfo.getProvinceId();
			} else if (orgZTree.getDepth() == 2) {
				OrgCityInfo orgCityInfo = orgZTree.toCityInfo();
				orgCityService.addOrgCity(orgCityInfo);
				newOrgId = orgCityInfo.getCityId();
			} else if (orgZTree.getDepth() == 3) {
				OrgCinemaInfo orgCinemaInfo = orgZTree.toCinemaInfo();
				orgCinemaService.addOrgCinema(orgCinemaInfo);
				newOrgId = orgCinemaInfo.getCinemaId();
			}
		} else if (orgZTree.getSaveType() == 2) {// 修改
			if (orgZTree.getDepth() == 0) {
				orgRegionService.updateOrgRegion(orgZTree.toRegionInfo());
			} else if (orgZTree.getDepth() == 1) {
				orgProvinceService.updateOrgProvince(orgZTree.toProvinceInfo());
			} else if (orgZTree.getDepth() == 2) {
				orgCityService.updateOrgCity(orgZTree.toCityInfo());
			} else if (orgZTree.getDepth() == 3) {
				orgCinemaService.updateOrgCinema(orgZTree.toCinemaInfo());
			}
		} else if (orgZTree.getSaveType() == 3) {// 删除
			if (orgZTree.getDepth() == 0) {
				orgRegionService.updateEnabled(orgZTree.getId(), false);
			} else if (orgZTree.getDepth() == 1) {
				orgProvinceService.updateEnabled(orgZTree.getId(), false);
			} else if (orgZTree.getDepth() == 2) {
				orgCityService.updateEnabled(orgZTree.getId(), false);
			} else if (orgZTree.getDepth() == 3) {
				orgCinemaService.updateEnabled(orgZTree.getId(), false);
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", 1);
		jsonObject.put("newOrgId", newOrgId);
		IOUtil.writeJSON(jsonObject, response.getOutputStream());
	}

	/**
	 * 保存
	 * 
	 * @author daniel.yan
	 * @created 2012-5-22 15:28:52
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void saveTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String jsonData = request.getParameter("jsonData");
		System.out.println("jsonData: " + jsonData);
		if (StringUtil.isNotBlank(jsonData)) {
			JSONArray jsonArray = JSONArray.fromObject(jsonData);
			Map<String, Class> classMap = new HashMap<String, Class>();
			classMap.put("children", OrgZTree.class);
			OrgZTree[] trees = (OrgZTree[]) JSONArray.toArray(jsonArray, OrgZTree.class);
			Map<String, String> idMap = new HashMap<String, String>();
			for (OrgZTree orgZTree : trees) {// 新增
				if (orgZTree.getSaveType() == 1) {
					if (orgZTree.getDepth() == 0) {
						OrgRegionInfo orgRegionInfo = orgZTree.toRegionInfo();
						orgRegionService.addOrgRegion(orgRegionInfo);
						idMap.put(orgZTree.getId(), orgRegionInfo.getRegionId());
					} else if (orgZTree.getDepth() == 1) {
						OrgProvinceInfo orgProvinceInfo = orgZTree.toProvinceInfo();
						if (idMap.containsKey(orgZTree.getpId())) {
							orgProvinceInfo.getOrgRegion().setRegionId(idMap.get(orgZTree.getpId()));
						}
						orgProvinceService.addOrgProvince(orgProvinceInfo);
						idMap.put(orgZTree.getId(), orgProvinceInfo.getProvinceId());
					} else if (orgZTree.getDepth() == 2) {
						OrgCityInfo orgCityInfo = orgZTree.toCityInfo();
						if (idMap.containsKey(orgZTree.getpId())) {
							orgCityInfo.getOrgProvince().setProvinceId(idMap.get(orgZTree.getpId()));
						}
						orgCityService.addOrgCity(orgCityInfo);
						idMap.put(orgZTree.getId(), orgCityInfo.getCityId());
					} else if (orgZTree.getDepth() == 3) {
						OrgCinemaInfo orgCinemaInfo = orgZTree.toCinemaInfo();
						if (idMap.containsKey(orgZTree.getpId())) {
							orgCinemaInfo.getOrgCity().setCityId(idMap.get(orgZTree.getpId()));
						}
						orgCinemaService.addOrgCinema(orgCinemaInfo);
						idMap.put(orgZTree.getId(), orgCinemaInfo.getCinemaId());
					}
				} else if (orgZTree.getSaveType() == 2) {// 修改
					if (orgZTree.getDepth() == 0) {
						orgRegionService.updateOrgRegion(orgZTree.toRegionInfo());
					} else if (orgZTree.getDepth() == 1) {
						orgProvinceService.updateOrgProvince(orgZTree.toProvinceInfo());
					} else if (orgZTree.getDepth() == 2) {
						orgCityService.updateOrgCity(orgZTree.toCityInfo());
					} else if (orgZTree.getDepth() == 3) {
						orgCinemaService.updateOrgCinema(orgZTree.toCinemaInfo());
					}
				} else if (orgZTree.getSaveType() == 3) {// 删除
					if (orgZTree.getDepth() == 0) {
						orgRegionService.updateEnabled(orgZTree.getId(), false);
					} else if (orgZTree.getDepth() == 1) {
						orgProvinceService.updateEnabled(orgZTree.getId(), false);
					} else if (orgZTree.getDepth() == 2) {
						orgCityService.updateEnabled(orgZTree.getId(), false);
					} else if (orgZTree.getDepth() == 3) {
						orgCinemaService.updateEnabled(orgZTree.getId(), false);
					}
				}
			}
			response.getOutputStream().write("{\"result\":1}".getBytes("UTF-8"));

		} else {
			response.getOutputStream().write("{\"result\":1}".getBytes("UTF-8"));
		}
	}

	/**
	 * 判断是否可删除
	 * 
	 * @author daniel.yan
	 * @created 2012-5-22 15:28:41
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void canDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String code = request.getParameter("code");
		int depth = Utils.parseInt(request.getParameter("depth"));
		JSONObject jsonObject = new JSONObject();
		if (checkRoleExistsWithOrgCode(code, depth)) {
			jsonObject.put("result", 0);
		} else {
			jsonObject.put("result", 1);
		}

		IOUtil.writeJSON(jsonObject, response.getOutputStream());
	}

	private boolean checkRoleExistsWithOrgCode(String orgCode, int depth) {
		List<String> orgCodes = orgRegionService.findOrgCodes(orgCode, depth);
		return userManagerService.checkRoleExistsWithOrgCode(orgCodes.toArray(new String[orgCodes.size()]));
	}

	/**
	 * 根据省份查询城市
	 * 
	 * @param request
	 * @param response
	 * @param orgProvinceInfo
	 * @return
	 * @throws IOException
	 */
	public void queryOrgCityByProvinceId(HttpServletRequest request, HttpServletResponse response, OrgProvinceInfo orgProvinceInfo) throws IOException {
		Map params = new HashMap();
		List<OrgModel> orgModels = initOrgModels(request);
		params.put(GlobalConstant.USER_CODE_KEY, orgModels);
		params.put("orgProvinceInfoKey", orgProvinceInfo);
		JSONArray jsonArray = JSONArray.fromObject(orgCityService.findOrgCitys(params));
		IOUtil.writeJSONArray(jsonArray, response.getOutputStream());
	}
}
