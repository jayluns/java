package com.kingdee.ais.ibmp.view.advert.web.spring.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.kingdee.ais.core.constant.GlobalConstant;
import com.kingdee.ais.core.entity.OrgModel;
import com.kingdee.ais.core.exception.GenericRuntimeException;
import com.kingdee.ais.core.pagination.Page;
import com.kingdee.ais.core.util.CommUtil;
import com.kingdee.ais.core.web.spring3.controller.MultiActionControllerImpl;
import com.kingdee.ais.ibmp.business.advert.service.AdvAgentContractService;
import com.kingdee.ais.ibmp.business.advert.service.IAdvAgentService;
import com.kingdee.ais.ibmp.business.dictionary.Dictionary;
import com.kingdee.ais.ibmp.business.dictionary.service.IDictionaryService;
import com.kingdee.ais.ibmp.model.pojo.advert.AgentContract;
import com.kingdee.ais.ibmp.model.vo.advert.AgentContractInfo;
import com.kingdee.ais.ibmp.model.vo.advert.AgentInfo;
import com.kingdee.ais.ibmp.model.vo.advert.JinyiInfo;
import com.kingdee.ais.ibmp.model.vo.dictionary.DataDictionaryInfo;
import com.kingdee.ais.ibmp.view.constant.Constant;
import com.kingdee.ais.ibmp.view.util.FileUpLoadUtil;
import com.kingdee.ais.ibmp.view.util.IOUtil;

/**
 * AdvAgentContractController: 广告代理商合同控制类.
 * @since webapp 1.0
 * @version 1.0, 04/09/2012
 * @author yong_jiang <br/>
 * @see MultiActionControllerImpl
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdvAgentContractController extends MultiActionControllerImpl {
	private static final Logger logger = Logger.getLogger(AdvAgentContractController.class);

	private AdvAgentContractService advAgentContractService;
	private IAdvAgentService advAgentService;
	private IDictionaryService dictionaryService;

	public IDictionaryService getDictionaryService() {
		return dictionaryService;
	}
	public void setDictionaryService(IDictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}
	public IAdvAgentService getAdvAgentService() {
		return advAgentService;
	}
	public void setAdvAgentService(IAdvAgentService advAgentService) {
		this.advAgentService = advAgentService;
	}

	// 新增代理商信息二级页面
	public static final String VIEW_REQUEST_ADDAGENTCONTRACT_SUCCESS = "request_addagentcontract_success"; // 新增代理商广告信息-页面请求成功页面
	public static final String VIEW_REDIRECT_ADDAGENTCONTRACT_SUCCESS = "redirect_addagentcontract_success"; // 新增代理商广告信息保存成功后重新定向的页面
	public static final String VIEW_SHOWCURRENTADVAGENTCONTRACT_SUCCESS = "requestAddAdvAgentContractInfo.do"; // 跳转到当前页面的请求
	// 维护代理商信息二级页面
	public static final String VIEW_QUERY_ADVAGENTCONTRACT_LIST_SUCCESS = "views_query_advagentcontract_list_success";// 查询代理商广告信息得到列表
	public static final String VIEW_JUMP_ADVAGENTCONTRACT_LIST_SUCCESS = "views_jump_advagentcontract_list_success"; // 跳转到列表页面
	public static final String VIEW_QUERY_ADVAGENTCONTRACT_DETAILOPERATION_SUCCESS = "views_query_advagentcontract_detailoperation_success";// 查询代理商广告详细信息-详情操作页面
	public static final String VIEW_QUERY_ADVAGENTCONTRACT_HASEXPIRED_SUCCESS = "views_querys_advagentcontract_hasexpired_success";// 查询代理商广告详细信息-详情操作页面

	public AdvAgentContractService getAdvAgentContractService() {
		return advAgentContractService;
	}
	public void setAdvAgentContractService(
			AdvAgentContractService advAgentContractService) {
		this.advAgentContractService = advAgentContractService;
	}

	/**
	 * <p>
	 * 录入代理商广告信息-菜单-请求跳转得到的页面
	 * @param request  current HTTP request
	 * @param response current HTTP response
	 * @throws Exception
	 */
	public ModelAndView requestAddAdvAgentcontract(HttpServletRequest request,
			HttpServletResponse response, AgentContractInfo agentInfo)
			throws Exception {
		if (logger.isInfoEnabled()) {
			logger.debug("requestAddAdvAgentcontract() - start"); //$NON-NLS-1$
		}
		List<OrgModel> orgModels = initOrgModels(request);
		Map modelMap = new HashMap();
		modelMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		List<DataDictionaryInfo> paymentlist=null;
		/**
		 * 得到代理商
		 */
		List<AgentInfo> agentList = advAgentService.queryList(modelMap);
		JinyiInfo jinyiInfo = advAgentService.queryJinYi();
		/**
		 * 得到付款方式
		 */
		Map modelMaps = new HashMap();
		DataDictionaryInfo dictionary=new DataDictionaryInfo();
		dictionary.setFDataId(Dictionary.AGENT_PAYMENT);
		modelMaps.put("dataDictionaryInfoKey", dictionary);
		paymentlist=dictionaryService.findDataDictionarys(modelMaps);
		modelMap.put("agentList", agentList);
		modelMap.put("paymentlist", paymentlist);
		modelMap.put("jinyiInfo", jinyiInfo);
		if (logger.isDebugEnabled()) {
			logger.debug("requestAddAdvAgentcontract() - end"); //$NON-NLS-1$
		}
		return new ModelAndView(VIEW_REQUEST_ADDAGENTCONTRACT_SUCCESS, modelMap);
	}

	/**
	 * 根据下拉列选项异步展现代理商信息
	 * @param request
	 * @param response
	 * @param agentInfo
	 * @return ModelAndView
	 * @throws Exception 
	 */
	@SuppressWarnings("unused")
	public ModelAndView togetAgeninfomation(HttpServletRequest request,
			HttpServletResponse response, AgentInfo agentInfo)
			throws Exception {
		JSONObject json = new JSONObject();
		AgentInfo agentinfos = advAgentService.findAdvAgentInfoByName(agentInfo.getAgentName());
		//转化代理商级别		
		DataDictionaryInfo dicleveinfos=new DataDictionaryInfo();
		if(agentinfos!=null){
			if(agentinfos.getAgentLevel()!=0L){
				Map maplv=new HashMap();
				DataDictionaryInfo dinfo=new DataDictionaryInfo();
				dinfo.setSDataId(agentinfos.getAgentLevel());
				maplv.put("datasDictionaryInfoKey", dinfo);
				dicleveinfos=dictionaryService.findLevelBySDateId2(maplv);
			}
			if(StringUtils.isNotBlank(agentinfos.getAgentAddress())){
				json.put("address", agentinfos.getAgentAddress());
			}
			if(StringUtils.isNotBlank(agentinfos.getAgentFax())){
				json.put("fax", agentinfos.getAgentFax());
			}
			
			if(StringUtils.isNotBlank(agentinfos.getAgentTel())){
				json.put("tel", agentinfos.getAgentTel());
			}
		}
		
		
		
		//這里的值一定要在数据字典中存在，否则代理商级别将不会显示
		if(dicleveinfos!=null){
		  json.put("level", dicleveinfos.getSDataName());
		}
		if(agentinfos != null) {
			json.put("success", "success");
		}else{
			json.put("failure", "fail");
		}
		IOUtil.writeJSON(json, response.getOutputStream());
		return null;
	}

	/**
	 * @Title: createAdvAgentContractInfo
	 * @Description: TODO(录入代理商合同)
	 * @param @return
	 * @return ModelAndView
	 * @author yong_jiang
	 * @date 2012-4-10
	 * @throws
	 */
	public ModelAndView createAdvAgentContractInfo(HttpServletRequest request,
			HttpServletResponse response, AgentContractInfo agentcraInfo)
			throws GenericRuntimeException, IOException {
		List<OrgModel> orgModels = initOrgModels(request);
		Map advagentMap = new HashMap();
		Map jugeMap = new HashMap();
		JSONObject json = new JSONObject();
		String agentId = request.getParameter("agentid");
		String companyName=request.getParameter("company");
		jugeMap.put("keyagentContractInfo", agentcraInfo);
		//检查code是否存在
		boolean res=advAgentContractService.jugeAgentContractCode(jugeMap);
		if(res){
			json.put("failure", Constant.ADVERT_CONTRACT_CODE_ISEXSIT);
			IOUtil.writeJSON(json, response.getOutputStream()); 
			return null;
		}
		//设置新增的Jinyi
		JinyiInfo jyinfo = setJiyi(companyName);//本方公司信息是固定的
		//设置代理商
		setAgent(agentcraInfo, agentId, jyinfo);
		
		advagentMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		advagentMap.put("keyagentContractInfo", agentcraInfo);
		advAgentContractService.addAdvAgentContractInfo(advagentMap);
		json.put("success", Constant.ADD_SUCCESS);
		IOUtil.writeJSON(json, response.getOutputStream());
		if(logger.isInfoEnabled())
		logger.info("[用户:xxx]"+" || "+"进行了录入代理商合同操作  || 执行的具体类和方法"+"||"+"["+AdvAgentContractController.class.getName()+".createAdvAgentContractInfo()"+"]");
		return null;
	}
	private void setAgent(AgentContractInfo agentcraInfo, String agentId,
			JinyiInfo jyinfo) {
		AgentInfo agentinfo = new AgentInfo();
		agentinfo.setAgentId(agentId);
		agentcraInfo.setAgent(agentinfo);
		agentcraInfo.setJinyi(jyinfo);
	}
	private JinyiInfo setJiyi(String companyName) {
		JinyiInfo jyinfo=new JinyiInfo();
		jyinfo.setCompanyName(companyName);
		jyinfo.setJinyiId("223");
		jyinfo.setAddress("广州市花城大道39号君玥公馆D栋2楼");
		return jyinfo;
	}

	
	/**
	 * @Title: getAdvAgentLevel
	 * @Description: TODO(初始化代理商合同状态)
	 * @param @return
	 * @return List<DataDictionaryInfo>
	 * @author milihua
	 * @date 2012-11-26
	 * @throws
	 */
	public List<DataDictionaryInfo> getAgentContractStatus() {
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.AGENT_CONTRACT_STATUS); // 得到代理商合同状态
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> advAgentContractList = dictionaryService.findDataDictionarys(dictionaryMap);
		return advAgentContractList;
	}
	
	// 根据条件查询出广告代理商信息-展示在列表中
	public ModelAndView queryAdvAgentContractInfoList(
			HttpServletRequest request, HttpServletResponse response,
			AgentInfo agentInfo) throws GenericRuntimeException {
		List<OrgModel> orgModels = initOrgModels(request);
		
		// 初始化代理商级别数据
		List<DataDictionaryInfo> dictionaryAdvAgentLevelList = getAdvAgentLevel();
		// 初始化代理商合同状态
		List<DataDictionaryInfo> advAgentContractList = getAgentContractStatus();

		// 设置界面对象
		String lev =  (String) request.getParameter("agentLevel");
		String name = (String) request.getParameter("contractname");
		String contractstatus = request.getParameter("contractstatus");
		String agentContractCode = request.getParameter("agentContractCode");//合同编号
		String agentContractName = request.getParameter("agentContractName");//合同名称
		String agentContractStartTime = request.getParameter("agentContractStartTime");//合同有效期开始
		String agentContractEndTime = request.getParameter("agentContractEndTime");//合同有效期结束
		if (lev != null)
			agentInfo.setAgentLevel(Long.parseLong(lev));
		if (name != null)
			agentInfo.setContactName(name);
		if(contractstatus != null){
			agentInfo.setContractstatus(contractstatus);
		}
		Map advagentMap = new HashMap();
		agentInfo.setPageSize(GlobalConstant.PAGE_SIZE); // 10条数据
		agentInfo.setPageNavigationURL(CommUtil.getUrl(request));
		advagentMap.put("keyAdvAgentInfo", agentInfo); // key 与dao层的一致
		advagentMap.put(GlobalConstant.USER_CODE_KEY, orgModels);
		advagentMap.put("contractstatus", contractstatus);
		advagentMap.put("agentContractCode", agentContractCode);
		advagentMap.put("agentContractName", agentContractName);
		advagentMap.put("agentContractStartTime", agentContractStartTime);
		advagentMap.put("agentContractEndTime", agentContractEndTime);
		// 分页查询
		Page<AgentContractInfo, AgentContract> pageResult = advAgentContractService.findAdvAgentContractInfos(advagentMap);
		pageResult.setPageNavigationURL(agentInfo.getPageNavigationURL());		
	
		// 页面数据显示
		Map modelMap = new HashMap();
		modelMap.put("dictionaryAdvAgentLevelList", dictionaryAdvAgentLevelList); // 取出代理商级别数据
		modelMap.put("pageResult", pageResult); // 列表数据
		modelMap.put("advAgentInfo", agentInfo);
		modelMap.put("agentContractCode", agentContractCode);
		modelMap.put("agentContractName", agentContractName);
		modelMap.put("agentContractStartTime", agentContractStartTime);
		modelMap.put("agentContractEndTime", agentContractEndTime);
		modelMap.put("advAgentContractList", advAgentContractList);//代理商合同状态
		return new ModelAndView(VIEW_JUMP_ADVAGENTCONTRACT_LIST_SUCCESS,modelMap);
	}

	/**
	 * @Title: getAdvAgentLevel
	 * @Description: TODO(初始化代理商级别)
	 * @param @return
	 * @return List<DataDictionaryInfo>
	 * @author yong_jiang
	 * @date 2012-4-10
	 * @throws
	 */
	public List<DataDictionaryInfo> getAdvAgentLevel() {
		Map dictionaryMap = new HashMap();
		DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
		dataDictionaryInfo.setFDataId(Dictionary.AGENT_LEVEL); // 得到代理商级别
		dictionaryMap.put("dataDictionaryInfoKey", dataDictionaryInfo);
		List<DataDictionaryInfo> dictionaryAdvAgentLevelList = dictionaryService.findDataDictionarys(dictionaryMap);
		return dictionaryAdvAgentLevelList;
	}

	/**
	 * @throws Exception 
	 * @Title: queryAdvAgentContractDetail
	 * @Description: TODO(查询代理商合同详情列表)
	 * @param @return
	 * @return ModelAndView
	 * @author yong_jiang
	 * @date 2012-4-10
	 * @throws
	 */
	public ModelAndView queryAdvAgentContractDetail(HttpServletRequest request, HttpServletResponse response, AgentContractInfo agentContractInfo)
			throws Exception {
		Map advagentMap = new HashMap();
		Map modelMap = new HashMap();
		 List<OrgModel> orgModels = initOrgModels(request);
		 advagentMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		//设置界面信息
		advagentMap.put("keyAgentContractInfo", agentContractInfo);
		// 根据uuid查询表数据记录
		AgentContractInfo agentContractInfos = advAgentContractService.findAdvAgentContractInfoById(advagentMap);
		
		// 获取代理商
		List<AgentInfo> agentList = advAgentService.queryList(advagentMap);
		
		JinyiInfo jinyiInfo = advAgentService.queryJinYi();
		
		//获取付款方式
		List<DataDictionaryInfo> paymentlist = getPayList();
		
		//获取代理商级别
		DataDictionaryInfo dicleveinfos = paselAgentlevel(agentContractInfos);
		
		//数据封装到map中，用来展现
		beanShow(modelMap, agentContractInfos, agentList, paymentlist,
				dicleveinfos);
		modelMap.put("jinyiInfo",jinyiInfo);
		if("1901".equals(agentContractInfo.getAgentContractStatus())||"1902".equals(agentContractInfo.getAgentContractStatus())){
			return new ModelAndView(VIEW_QUERY_ADVAGENTCONTRACT_HASEXPIRED_SUCCESS, modelMap);
		}else{
			return new ModelAndView(VIEW_QUERY_ADVAGENTCONTRACT_DETAILOPERATION_SUCCESS, modelMap);
		}
	}
	
	private void beanShow(Map modelMap, AgentContractInfo agentContractInfos,
			List<AgentInfo> agentList, List<DataDictionaryInfo> paymentlist,
			DataDictionaryInfo dicleveinfos) {
		// 设置选择乙方的时,带出的信息
		modelMap.put("agentList", agentList);	
		modelMap.put("agentContractInfos", agentContractInfos);
		modelMap.put("paymentlist", paymentlist);
		modelMap.put("agentInfos", agentContractInfos.getAgent());
		modelMap.put("dicleveinfos", dicleveinfos);
	}
	
	//获取付款方式列表
	private List<DataDictionaryInfo> getPayList() {
		List<DataDictionaryInfo> paymentlist=null;
		Map map=new HashMap();
		DataDictionaryInfo dinfso=new DataDictionaryInfo();
		dinfso.setFDataId(Dictionary.AGENT_PAYMENT);
		map.put("dataDictionaryInfoKey", dinfso);
		paymentlist=dictionaryService.findDataDictionarys(map);
		return paymentlist;
	}
	private DataDictionaryInfo paselAgentlevel(
			AgentContractInfo agentContractInfos) {
		//代理商级别的转换
		DataDictionaryInfo dicleveinfos=new DataDictionaryInfo();
		if(agentContractInfos.getAgent().getAgentLevel()!=0L){
			Map maplv=new HashMap();
			DataDictionaryInfo dinfo=new DataDictionaryInfo();
			dinfo.setSDataId(agentContractInfos.getAgent().getAgentLevel());
			maplv.put("datasDictionaryInfoKey", dinfo);
			dicleveinfos=dictionaryService.findLevelBySDateId2(maplv);
		}
		return dicleveinfos;
	}

	/**
	 * @Title: updateAdvAgentContractInfo
	 * @Description: TODO(更新理商合同详情)
	 * @param @return
	 * @return ModelAndView
	 * @author yong_jiang
	 * @date 2012-4-10
	 * @throws
	 */
	public ModelAndView updateAdvAgentContractInfo(HttpServletRequest request,
			HttpServletResponse response, AgentContractInfo agentcraInfo)
			throws GenericRuntimeException, IOException {
		List<OrgModel> orgModels = initOrgModels(request);
		Map advagentMap = new HashMap();
		JSONObject json = new JSONObject();
		String agentId = request.getParameter("agentid");
		setparams(agentcraInfo, orgModels, advagentMap, agentId);
		//更新操作
		boolean updateTag =advAgentContractService.updateAdvAgentContractInfoByAgentId(advagentMap);
		if(updateTag){
			json.put("success",Constant.UPDATE_SUCCESS);
		}else{
			json.put("failure",Constant.UPDATE_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		if(logger.isInfoEnabled())
			logger.info("[用户:xxx]"+" || "+"进行了更新理商合同详情操作   || 执行的具体类和方法"+"||"+"["+AdvAgentContractController.class.getName()+".createAdvAgentContractInfo()"+"]");
		return null;
	}
	
	//设置advagentMap中的参数
	private void setparams(AgentContractInfo agentcraInfo,
		List<OrgModel> orgModels, Map advagentMap, String agentId) {
		AgentInfo agentinfo = new AgentInfo();
		agentinfo.setAgentId(agentId);
		agentcraInfo.setAgent(agentinfo);
		advagentMap.put("keyagentContractInfo", agentcraInfo);
		advagentMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
	}
	
	
	/**
	 * @Title: updateAgentContractStatus
	 * @Description: TODO(更新代理商合同状态)
	 * @param @return
	 * @return ModelAndView
	 * @author yong_jiang
	 * @date 2012-4-10
	 * @throws
	 */
	public ModelAndView updateAgentContractStatus(HttpServletRequest request,
			HttpServletResponse response, AgentContractInfo agentcraInfo)
			throws GenericRuntimeException, IOException {
		List<OrgModel> orgModels = initOrgModels(request);
		Map advagentMap = new HashMap();
		JSONObject json = new JSONObject();
		advagentMap.put("keyagentContractInfo", agentcraInfo);
		advagentMap.put(GlobalConstant.USER_CODE_KEY, orgModels);//写日志操作
		//更新操作
		boolean updateTag =advAgentContractService.updateAdvAgentContractStatus(advagentMap);
		if(updateTag){
			json.put("success",Constant.UPDATE_STATUS_SUCCESS);
		}else{
			json.put("failure",Constant.UPDATE_STATUS_FAIL);
		}
		IOUtil.writeJSON(json, response.getOutputStream()); 
		if(logger.isInfoEnabled())
		logger.info("[用户:xxx]"+" || "+"进行了更新代理商合同状态操作   || 执行的具体类和方法"+"||"+"["+AdvAgentContractController.class.getName()+".updateAgentContractStatus()"+"]");
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
	 * 模糊查询代理商名称
	 * @param request
	 * @param response
	 * @param agentInfo
	 * @throws IOException 
	 */
	public void findAgentName(HttpServletRequest request, HttpServletResponse response, AgentInfo agentInfo) throws IOException{
		JSONObject json = new JSONObject();
		Map params = new HashMap();
		params.put("agentNameKey", agentInfo.getAgentName());
		json.put("agents", advAgentService.findAgentName(params));
		IOUtil.writeJSON(json,response.getOutputStream());
	} 
	
	/**
	 * 删除上传的图片，文件
	 * */
	public void delPic(HttpServletRequest request, HttpServletResponse response,AgentContractInfo aci) throws Exception{
		String filePath = request.getSession().getServletContext().getRealPath("/")+aci.getAgentContractUrl();
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		JSONObject json = new JSONObject();
		json.put("success", "success1");
		IOUtil.writeJSON(json, response.getOutputStream());
	}

}