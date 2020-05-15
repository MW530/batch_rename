package application;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import com.alibaba.fastjson.JSON;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import application.Utils;


public class UIController implements Initializable{

	
	public static List<Map<Integer, String>> nameData;
	public static Map<Integer, String> headMap;
	private ArrayList<String> fileNames;
	
	
	private ArrayList<String> txtNames;
	private ArrayList<String> excelNameNoKey;
	private Map<Integer, ArrayList<String>> excelName;
	
	
	private Map<String, ObservableList<String>> ListData;
	private String fileFolderPath;
	private String workType = "重命名模式";
	@FXML
	private Pane root;
	
	private Stage rootStage;
	
	/*
	 * 左侧组件
	 */
	@FXML
	private TextField chartTF;
	@FXML
	private ChoiceBox<String> chartColCB;
	@FXML
	private ListView<String> chartList;
	
	/*
	 * 中间组件
	 */
	@FXML
	private TextField FilePathTF;
	@FXML
	private ListView<String> FileListView;
 	@FXML
 	private WebView FilePreview;
 	
	/*
	 * 右侧组件
	 */
	@FXML
	private ChoiceBox<String> TypeCB;
	@FXML
	private TextField PrefexTF;
	@FXML
	private Button submitB;
	
	
	/*
	 * 事件方法
	 */
	@FXML
	protected  void chartChoose(MouseEvent e) {
		rootStage = (Stage)root.getScene().getWindow();
		File file = Utils.chooseFile(rootStage);
		if (Utils.suffix(file.getName()).equals(".xlsx") || Utils.suffix(file.getName()).equals(".xls") || Utils.suffix(file.getName()).equals(".csv")) {
			Utils.parseExcel(file);
			excelName = Utils.parseEasyData(nameData);
			System.out.print(JSON.toJSONString(excelName)+"\n");
		}else if(Utils.suffix(file.getName()).equals(".txt")) {
			txtNames = Utils.parseTxt(file);
			System.out.print(JSON.toJSONString(txtNames)+"\n");
		}
		chartTF.setText(file.getPath());
		System.out.print("选择表格"+file.getName()+"\n");
		setChartCB();
		makeListData();
		if(headMap.size() != 0) {
			setListData(headMap.get(0), ListData, null, chartList);	
			excelNameNoKey = Utils.OLToAL(ListData.get(headMap.get(0)));
		}
	}
	@FXML
	protected void chooseFolder(MouseEvent e) {
		rootStage = (Stage)root.getScene().getWindow();
		String folderPath = Utils.folderPath(rootStage);
		fileNames = Utils.readAllFiles(folderPath);
		System.out.print("选择文件夹位置"+folderPath);
		fileFolderPath = folderPath;
		FilePathTF.setText(folderPath);
		ObservableList<String> fileObList = Utils.ALToOL(fileNames);
		setListData(null, null, fileObList, FileListView);
	}

	@FXML
	protected void submit(ActionEvent e) {
		rootStage = (Stage)root.getScene().getWindow();
		ArrayList<File> files = new ArrayList<File>();
		ArrayList<String> names = new ArrayList<String>();
		try {
			switch (workType) {
			case "重命名模式":
				names = excelNameNoKey;
				break;
			case "前缀模式":
				String preGap = PrefexTF.getText();
				for (int i = 0; i < fileNames.size(); i++) {
					String newNameString = excelNameNoKey.get(i) + preGap + fileNames.get(i);
					names.add(newNameString);
				}
				break;
				
			case "后缀模式":
				String sufGap = PrefexTF.getText();
				for (int i = 0; i < fileNames.size(); i++) {
					String newNameString = fileNames.get(i)  + sufGap +  excelNameNoKey.get(i);
					names.add(newNameString);
				}
				break;
			default:
				names = excelNameNoKey;
				break;
			}
			
			for (String path : fileNames) {
				File tempFile = new File(fileFolderPath+"\\"+path);
				files.add(tempFile);
			}
			System.out.println(JSON.toJSONString(fileNames));
			System.out.println(JSON.toJSONString(names));
			Utils.batchRename(files, names);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("提示");
			alert.setHeaderText("成功!");
			alert.setContentText("已成功命名!");
			alert.showAndWait();
		} catch (Exception e2) {
			// TODO: handle exception
			System.out.println(JSON.toJSONString(fileNames));
			System.out.println(JSON.toJSONString(names));
			Utils.batchRename(files, names);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("提示");
			alert.setHeaderText("失败!");
			alert.setContentText("命名失败!");
			alert.showAndWait();
		}
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println(chartColCB);
		chartColCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				System.out.println(oldValue+"---"+newValue);
				if(oldValue == null) return;
				excelNameNoKey = Utils.OLToAL(ListData.get(newValue));
				setListData(newValue, ListData, null, chartList);
			}
		});
		
		TypeCB.setItems(FXCollections.observableArrayList("重命名模式", "前缀模式", "后缀模式"));
		TypeCB.setValue("重命名模式");
		
		TypeCB.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				workType = newValue;
			}
		});
	}
	
	
	
	private void setChartCB() {
		ObservableList cursors = FXCollections.observableArrayList();
		for (Entry<Integer, String> entry : headMap.entrySet()) {
			cursors.add(entry.getValue());
		}
		chartColCB.setItems(cursors);
		if(headMap.containsKey(0)) {
			chartColCB.setValue(headMap.get(0));
		}
	}
	
	private void makeListData() {
		ListData = new HashMap<String, ObservableList<String>>();
		for (Entry<Integer, ArrayList<String>> temp : excelName.entrySet()) {
			String newKey = headMap.get(temp.getKey());
			ObservableList<String> newArray =  Utils.ALToOL(temp.getValue());
			ListData.put(newKey, newArray);
		}
	}
	
	private void setListData(String key, Map<String, ObservableList<String>> ListMap, ObservableList<String> list, ListView<String> listView) {
		if(key == null) {
			listView.setItems(list);
		}else {
			listView.setItems(ListMap.get(key));
		}
	}
	
}
