/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.qifu.base.SysMessageUtil;
import org.qifu.base.SysMsgConstants;
import org.qifu.base.dao.IBaseDAO;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryResult;
import org.qifu.base.model.SearchValue;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.service.BaseService;
import org.qifu.dao.ISysExprJobLogDAO;
import org.qifu.po.TbSysExprJobLog;
import org.qifu.service.ISysExprJobLogService;
import org.qifu.vo.SysExprJobLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("core.service.SysExprJobLogService")
@Scope("prototype")
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class SysExprJobLogServiceImpl extends BaseService<SysExprJobLogVO, TbSysExprJobLog, String> implements ISysExprJobLogService<SysExprJobLogVO, TbSysExprJobLog, String> {
	protected Logger logger=Logger.getLogger(SysExprJobLogServiceImpl.class);
	private ISysExprJobLogDAO<TbSysExprJobLog, String> sysExprJobLogDAO;
	
	public SysExprJobLogServiceImpl() {
		super();
	}

	public ISysExprJobLogDAO<TbSysExprJobLog, String> getSysExprJobLogDAO() {
		return sysExprJobLogDAO;
	}

	@Autowired
	@Resource(name="core.dao.SysExprJobLogDAO")
	@Required		
	public void setSysExprJobLogDAO(ISysExprJobLogDAO<TbSysExprJobLog, String> sysExprJobLogDAO) {
		this.sysExprJobLogDAO = sysExprJobLogDAO;
	}

	@Override
	protected IBaseDAO<TbSysExprJobLog, String> getBaseDataAccessObject() {
		return sysExprJobLogDAO;
	}

	@Override
	public String getMapperIdPo2Vo() {		
		return MAPPER_ID_PO2VO;
	}

	@Override
	public String getMapperIdVo2Po() {
		return MAPPER_ID_VO2PO;
	}
	
	private Map<String, Object> getQueryGridParameter(SearchValue searchValue) throws Exception {
		return super.getQueryParamHandler(searchValue).fullEquals4TextField("id").getValue();
	}		

	@Override
	public QueryResult<List<TbSysExprJobLog>> findGridResult(SearchValue searchValue, PageOf pageOf) throws ServiceException, Exception {
		if (searchValue==null || pageOf==null) {
			throw new ServiceException(SysMessageUtil.get(SysMsgConstants.SEARCH_NO_DATA));
		}
		Map<String, Object> params=this.getQueryGridParameter(searchValue);	
		int limit=Integer.parseInt(pageOf.getShowRow());
		int offset=(Integer.parseInt(pageOf.getSelect())-1)*limit;	
		QueryResult<List<TbSysExprJobLog>> result=this.sysExprJobLogDAO.findPageQueryResultByQueryName(
				"findSysExprJobLogPageGrid", params, offset, limit);
		pageOf.setCountSize(String.valueOf(result.getRowCount()));
		pageOf.toCalculateSize();
		return result;
	}

	@ServiceMethodAuthority(type={ServiceMethodType.DELETE})
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public int deleteAll() throws ServiceException, Exception {
		return this.sysExprJobLogDAO.deleteAll();
	}

}
