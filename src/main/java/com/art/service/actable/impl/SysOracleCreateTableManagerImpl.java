package com.art.service.actable.impl;

import com.art.mapper.actable.dao.system.CreateMysqlTablesMapper;
import com.art.service.actable.SysOracleCreateTableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目启动时自动扫描配置的目录中的model，根据配置的规则自动创建或更新表
 * 该逻辑只适用于mysql，其他数据库尚且需要另外扩展，因为sql的语法不同
 *
 * @author sunchenbin
 * @version 2016年6月23日 下午5:58:12
 */
@Transactional
@Service("sysOracleCreateTableManager")
public class SysOracleCreateTableManagerImpl implements SysOracleCreateTableManager {

	private static final Logger	log	= LoggerFactory.getLogger(SysOracleCreateTableManagerImpl.class);

	@Autowired
	private CreateMysqlTablesMapper	createMysqlTablesMapper;

	/**
	 * 要扫描的model所在的pack
	 */
	@Value("#{configProperties['mybatis.model.pack']}")	
	private String pack;
	
	/**
	 * 自动创建模式：update表示更新，create表示删除原表重新创建
	 */
	@Value("#{configProperties['mybatis.table.auto']}")
	private String tableAuto;

	/**
	 * 读取配置文件的三种状态（创建表、更新表、不做任何事情）
	 */
	public void createOracleTable(){
		
		// 不做任何事情
		if("none".equals(tableAuto)){
			log.info("配置mybatis.table.auto=none，不需要做任何事情");
			return;
		}
		
		// TODO: 暂时还没有写
	}
}
