/**
 * OGPaySystem
 */
package com.og.platform.pay.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.og.platform.pay.common.domain.SMSCodeInfo;
import com.payinfo.net.database.ConnectionResource;
import com.payinfo.net.database.IJuiceDBHandler;

/**
 * @author ogplayer.com
 *
 * 2013年10月25日
 */
public class CodeHelperDAO extends ConnectionResource {
	private static IJuiceDBHandler<SMSCodeInfo> HANDLER_CODE = new IJuiceDBHandler<SMSCodeInfo>() {
		@Override
		public SMSCodeInfo handler(ResultSet rs) throws SQLException {
			SMSCodeInfo info = new SMSCodeInfo();
			info.setCodeNO(rs.getString("code_no"));
			info.setCodeType(rs.getInt("code_type"));
			info.setPriority(rs.getInt("priority"));
			info.setPrice(rs.getInt("price"));
			info.setSmsPort(rs.getString("sms_port"));
			info.setSmsContent(rs.getString("sms_content"));
			info.setUrl(rs.getString("url"));
			info.setProvinceType(rs.getInt("province_type"));
			info.setProvinceHide(rs.getString("province_hide"));
			info.setProvinceOpen(rs.getString("province_open"));
			info.setOpen(rs.getBoolean("is_open"));
			info.setProvider(rs.getString("provider"));
			info.setKey(rs.getString("key"));
			info.setConfirmNum(rs.getInt("confirm_num"));
			info.setConfirmContent(rs.getString("confirm_conten"));
			info.setUseLimit(rs.getInt("use_limit"));
			info.setExt(rs.getBoolean("is_ext"));
			info.setCompany(rs.getString("company"));
			return info;
		}
	};
	
	/** 根据codeNo查找对应的通道*/
	public SMSCodeInfo getCodeInfo(String codeNO) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_sms_code_info where code_no=? and is_open>0");
		return queryForObject(sql.toString(), HANDLER_CODE, codeNO);
	}
	
	/** 根据运营商重建所有通道信息*/
	public List<SMSCodeInfo> getAllSMSCodes(int codeType) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_sms_code_info where code_type=? and is_open>0 order by id");
		return queryForList(sql.toString(), HANDLER_CODE, codeType);
	}
	
	/** 根据绑定通道和运营商选择可支付通道 */
	public List<SMSCodeInfo> getDirectSMSCodes(int codeType, String billId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from t_sms_code_info where code_type=? and code_no in ");
		sql.append(billId).append(" order by id");
		return queryForList(sql.toString(), HANDLER_CODE, codeType);
	}
}
