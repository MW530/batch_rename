package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import application.UIController;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;

public class ExcelRead extends AnalysisEventListener<Map<Integer, String>> {

	 List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
	
	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		// TODO Auto-generated method stub
		UIController.nameData = list;
	}

	@Override
	public void invoke(Map<Integer, String> data, AnalysisContext arg1) {
		// TODO Auto-generated method stub
		JSON.toJSONString(data+"------------------");
		list.add(data);
	}

	@Override
	public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
		// TODO Auto-generated method stub
		super.invokeHeadMap(headMap, context);
		UIController.headMap = headMap;
	}
	
}
