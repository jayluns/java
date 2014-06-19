package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.ModelAndView;

import com.kingdee.ais.core.constant.GlobalConstant;
import com.kingdee.ais.core.entity.OrgModel;
import com.kingdee.ais.core.exception.GenericRuntimeException;
import com.kingdee.ais.core.pagination.Page;
import com.kingdee.ais.core.util.CommUtil;
import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.advert.service.IAdvAgentNumberService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvAgentService;
import com.kingdee.ais.ibmp.business.dictionary.Dictionary;
import com.kingdee.ais.ibmp.business.dictionary.service.IDictionaryService;
import com.kingdee.ais.ibmp.model.pojo.advert.Agent;
import com.kingdee.ais.ibmp.model.vo.advert.AgentInfo;
import com.kingdee.ais.ibmp.model.vo.dictionary.DataDictionaryInfo;
import com.kingdee.ais.ibmp.view.constant.AdvAgentConstant;
import com.kingdee.ais.ibmp.view.constant.Constant;
import com.kingdee.ais.ibmp.view.util.IOUtil;
import com.kingdee.ais.ibmp.view.util.NumberFormatUtil;

/**
 * AdvAgentController: 广告代理商控制类
 * @since   webapp 1.0
 * @version 1.0, 02/28/2012
 * @author  yue_gao <br/>
 * @see     MultiActionControllerImpl
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdvAgentController extends MultiActionControllerImpl {
	
	IAdvAgentService   advAgentService = null;
	IDictionaryService dictionaryService = null;
	IAdvAgentNumberService advAgentNumberService = null;
	//新增代理商信息二级页面
	public static final String VIEW_REQUEST_ADDAGENTINFO_SUCCESS  = "view_request_addagentinfo_success";  //新增代理商信息-页面请求成功页面
	public static final String VIEW_REDIRECT_ADDAGENTINFO_SUCCESS = "view_redirect_addagentinfo_success"; //新增代理商信息保存成功后重新定向的页面
	public static final String VIEW_SHOWCURRENTADVAGENTINFO_SUCCESS = "requestAddAdvAgentInfo.do"; 		  //跳转到当前页面的请求
	//维护代理商信息二级页面
	public static final String VIEW_QUERY_ADVAGENTINFO_LIST_SUCCESS = "view_query_advagentinfo_list_success";//查询代理商信息得到列表
	public static final String VIEW_JUMP_ADVAGENTINFO_LIST_SUCCESS  = "view_jump_advagentinfo_list_success"; //跳转到列表页面
	public static final String VIEW_QUERY_ADVAGENTINFO_DETAILOPERATION_SUCCESS  = "view_query_advagentinfo_detailoperation_success";//查询代理商详细信息-详情操作页面
	
	
	/**
	 * @Title:       getAdvAgentLevel 
	 * @Description: TODO(初始化代理商级别) 
	 * @param        @return 
	 * @return       List<DataDictionaryInfo>
	 * @author       yue_gao  
	 * @date         2012-3-31 上午11:02:18
	 * @throws
	 */
	public  List<DataDictionaryInfo>  getAdvAgentLevel(){
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.AGENT_LEVEL);            //得到代理商级别
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo); 
		List<DataDictionaryInfo> dictionaryAdvAgentLevelList = dictionaryService.findDataDictionarys(dictionaryMap);
		return dictionaryAdvAgentLevelList;
	}
	/**
	 * <p>新增代理商信息-菜单-请求跳转得到的页面
	 * @param request   current HTTP request
	 * @param response  current HTTP response
	 * @throws Exception
	 */
	public ModelAndView requestAddAdvAgentInfo(HttpServletRequest request,HttpServletResponse response,AgentInfo agentInfo)throws GenericRuntimeException{
		Map modelMap = new HashMap();
		List<DataDictionaryInfo> dictionaryAdvAgentLevelList = getAdvAgentLevel();	//得到代理商级别
		/**
		 * 得到代理商编号,如果第一次要初始化一个值
		 */
	    long  advagentNumber = AdvertCodeController.getAdvertCode(AdvAgentConstant.AGENT_NUMBER_HEAD_PREFIX, AdvAgentConstant.AGENT_NUMBER_TYPE);
	    String finaladvcodeNumber = AdvAgentConstant.AGENT_NUMBER_HEAD_PREFIX + "-" + NumberFormatUtil.advAgentNumberFormat(advagentNumber);
		modelMap.put("agentInfo", agentInfo);
		modelMap.put("dictionaryAdvAgentLevelList", dictionaryAdvAgentLevelList);	//取出代理商级别数据
		modelMap.put("advcodeNumber", finaladvcodeNumber);							//生成代理商编号
		return new ModelAndView(VIEW_REQUEST_ADDAGENTINFO_SUCCESS, modelMap); 
	}
	
	/**
	 * 新增广告代理商信息方法<br/>
	 * 1.接受页面传过来的command数据<br/>
	 * 2.添加新纪录到数据库中 <br/>
	 * 3.提示保存成功,跳转到本页面
	 * @param request   current HTTP request
	 * @param response  current HTTP response
	 * @return ModelAndView
	 * @throws Exception 
	 */
	public ModelAndView createAdvAgentInfo(HttpServletRequest request, HttpServletResponse response, AgentInfo agentInfo) throws Exception {
		List<OrgModel> orgModels = initOrgModels(request);
		Map advagentMap = new HashMap();
		JSONObject json = new JSONObject();
		advagentMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//当前用户信息
		advagentMap.put("keyAdvAgentInfo", agentInfo);
		boolean res=advAgentService.jugeAgentNameExist(advagentMap);
		if(res){
			advAgentService.addAdvAgentInfo(advagentMap);
			json.put("success",Constant.ADD_SUCCESS);
		}else{
			json.put("failure",Constant.ADVERT_CONTRACT_NAME_EXSIT);
		}
		
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}
	
	
	/**
	 * @Title:       queryAdvAgentInfoList 
	 * @Description: TODO(根据条件查询出广告代理商信息-展示在列表中) 
	 * @param        @param request
	 * @param        @param response
	 * @param        @param agentInfo
	 * @param        @return
	 * @param        @throws GenericRuntimeException 
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-5 下午01:28:07
	 * @throws		 Exception
	 */
	public ModelAndView queryAdvAgentInfoList(HttpServletRequest request, HttpServletResponse response, AgentInfo agentInfo)throws Exception {
		//初始化代理商级别数据
		List<DataDictionaryInfo> dictionaryAdvAgentLevelList = getAdvAgentLevel();
		
		//获取用户权限集合
		List<OrgModel> orgModels = initOrgModels(request);
		Map advagentMap = new HashMap();
		agentInfo.setPageSize(GlobalConstant.PAGE_SIZE); //10条数据
		agentInfo.setPageNavigationURL(CommUtil.getUrl(request)); 
		advagentMap.put("keyAdvAgentInfo", agentInfo);	//key 与dao层的一致
		//分页查询
		advagentMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		Page<AgentInfo, Agent>  pageResult = advAgentService.findAdvAgentInfos(advagentMap);
		pageResult.setPageNavigationURL(agentInfo.getPageNavigationURL());
		//页面数据显示
	    Map modelMap = new HashMap();
		
	    modelMap.put("dictionaryAdvAgentLevelList", dictionaryAdvAgentLevelList);	//取出代理商级别数据
	    modelMap.put("pageResult", pageResult);										// 列表数据
	    modelMap.put("advAgentInfo", agentInfo);
		return new ModelAndView(VIEW_QUERY_ADVAGENTINFO_LIST_SUCCESS, modelMap);
	}
	
	//点击左边的菜单直接跳转过来的方法-跳转进来的时候 列表没有数据可以直接跳转,如果要是想展示数据就 要查询下返回结果集才可以
	public ModelAndView requestAdvAgentInfoList(HttpServletRequest request, HttpServletResponse response, AgentInfo agentInfo)throws GenericRuntimeException {
		return new ModelAndView(VIEW_JUMP_ADVAGENTINFO_LIST_SUCCESS);
	}
	

	/**
	 * @Title:       queryAdvAgentInfoDetail 
	 * @Description: TODO(按照uuid 查询记录的详细信息) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param agentInfo
	 * @param 		 @return
	 * @param 		 @throws GenericRuntimeException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @throws Exception 
	 * @date         2012-4-12 下午06:09:20
	 */
	public ModelAndView queryAdvAgentInfoDetail(HttpServletRequest request, HttpServletResponse response, AgentInfo agentInfo) throws Exception {
		//初始化代理商级别数据
		List<DataDictionaryInfo> dictionaryAdvAgentLevelList = getAdvAgentLevel();
		Map advagentMap = new HashMap();
		advagentMap.put("keyAdvAgentInfo", agentInfo);
		//按照uuid查询表数据记录
		AgentInfo advagentInfoReturn =  advAgentService.findAdvAgentInfoByAgentId(advagentMap);
	    Map modelMap = new HashMap();
	    modelMap.put("advagentInfoReturn", advagentInfoReturn);
	    modelMap.put("dictionaryAdvAgentLevelList", dictionaryAdvAgentLevelList);
	    return new ModelAndView(VIEW_QUERY_ADVAGENTINFO_DETAILOPERATION_SUCCESS, modelMap);
   }
		
	/**
	 * @Title:       updateAdvAgentInfo 
	 * @Description: TODO(按照uuid更新代理商数据 版本+1) 
	 * @param 		 @param request
	 * @param 		 @param response
	 * @param 		 @param agentInfo
	 * @param 		 @throws GenericRuntimeException
	 * @param 		 @throws IOException
	 * @return       ModelAndView
	 * @author       yue_gao  
	 * @date         2012-4-12 下午06:09:43
	 * @throws		 Exception
	 */
	public ModelAndView updateAdvAgentInfo(HttpServletRequest request, HttpServletResponse response, AgentInfo agentInfo) throws Exception {
		List<OrgModel> orgModels = initOrgModels(request);
		JSONObject json = new JSONObject();
		Map advagentMap = new HashMap();
		advagentMap.put("keyAdvAgentInfo", agentInfo);
		advagentMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		//按照uuid更新表数据记录
		boolean updateTag = advAgentService.updateAdvAgentInfoByAgentId(advagentMap);
		if(updateTag){
			json.put("success",Constant.UPDATE_SUCCESS);
		}else{
			json.put("failure",Constant.UPDATE_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
	    return  null;
   }
	

	public IAdvAgentService getAdvAgentService() {
		return advAgentService;
	}
	public void setAdvAgentService(IAdvAgentService advAgentService) {
		this.advAgentService = advAgentService;
	}
	public IDictionaryService getDictionaryService() {
		return dictionaryService;
	}
	public void setDictionaryService(IDictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	public IAdvAgentNumberService getAdvAgentNumberService() {
		return advAgentNumberService;
	}
	public void setAdvAgentNumberService(
			IAdvAgentNumberService advAgentNumberService) {
		this.advAgentNumberService = advAgentNumberService;
	}
	
	
}