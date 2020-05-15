package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 

public class Utils {
	
	/*
	 * ѡ���ļ���λ��
	 */
	public static String folderPath(Stage stage) {
		DirectoryChooser directoryChooser=new DirectoryChooser();
		File file = directoryChooser.showDialog(stage);
		String path = file.getPath();
		return path;
	}
	
	/*
	 * ��ȡ�ļ�λ��
	 */
	public static File chooseFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open [name] File");
		fileChooser.getExtensionFilters().addAll(
			 new FileChooser.ExtensionFilter("EXCEL", "*.xlsx", "*.xls", "*.csv"),
			 new FileChooser.ExtensionFilter("文本", "*.txt")
		);
		File file = fileChooser.showOpenDialog(stage);
		return file;
	}
	
	/*
	 * �ļ�������
	 */
	public static boolean reName(File file, String name) {
		if (name == null || name.equals("")) {
			return false;
		}
		try {
			String path = file.getPath();
			String incomPathString = pathWithoutName(path);
			String suffixString = suffix(path);
			System.out.print(pathWithoutName(path));
			System.out.print(suffix(path));
			File newNameFile = new File(incomPathString +"\\"+ name+suffixString);
			file.renameTo(newNameFile);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/*
	 * �����������ļ�
	 */
	public static void batchRename(ArrayList<File> files,ArrayList<String> names) {
		if (files.size() != names.size()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText("文件数量不匹配");
			alert.setContentText("注意!文件数量与名字数量不匹配，多余的文件将不会被命名，多余的名字将不会被使用!");
			alert.showAndWait();
			int min = files.size() > names.size() ? names.size() : files.size();
			for(int i = 0; i < min; i++) {
				reName(files.get(i), names.get(i));
			}
		}else{
			int min = files.size();
			for(int i = 0; i < min; i++) {
				reName(files.get(i), names.get(i));
			}
		}
		
	}
	
	/*
	 * ������·����ȡ�����ļ����ľ���·��
	 */
	public static String pathWithoutName(String completePath) {
		return completePath.substring(0, completePath.lastIndexOf("\\"));
	}
	
	/*
	 * ������·����ȡ�����ļ���׺��
	 */
	public static String suffix(String completePath) {
		return completePath.substring(completePath.indexOf("."));
	}
	
	/*
	 * ����Excel
	 */
	public static void parseExcel(File file) {
		//ExcelRead�Զ���ȡUIController�е�nameData��ע��
		EasyExcel.read(file, new ExcelRead()).sheet().doRead();
	}
	
	/*
	 * �������½���EasyExcel���������ݽṹ
	 */
	public static Map<Integer, ArrayList<String>> parseEasyData(List<Map<Integer, String>> data) {
		Map<Integer, ArrayList<String>> newDataMap = new HashMap<Integer, ArrayList<String>>();
		Map<Integer, String> headMap = UIController.headMap;
		for (Map.Entry<Integer, String> entry: headMap.entrySet()) {
			ArrayList<String> nameArray = new ArrayList<String>();
			Integer key = entry.getKey();
//			String valueString = entry.getValue();
//			nameArray.add(valueString);
			newDataMap.put(key, nameArray);
		}
		
		for(int i = 0; i < data.size(); i++) {
			Map<Integer, String> item = data.get(i);
			for(Map.Entry<Integer, String> entry : data.get(i).entrySet()){
			    Integer mapKey = entry.getKey();
			    String mapValue = entry.getValue();
			    newDataMap.get(mapKey).add(mapValue);
//			    System.out.println(mapKey+":"+mapValue);
			}
		}
		
		return newDataMap;
	}
	
	/*
	 * ����txt�ĵ�
	 */
	public static ArrayList<String> parseTxt(File file) {
		ArrayList<String> names = new ArrayList<String>();
		try {
			InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),"UTF-8");
			  BufferedReader br = new BufferedReader(read);
			  String s = null;
			  while((s = br.readLine()) != null){
				  System.out.println(s);
				  names.add(s);
			  }
			  br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return names;
	}
	
	/*
	 * ��ȡ�ļ����ڵ������ļ�
	 */
	public static ArrayList<String> readAllFiles(String folderPath) {
		ArrayList<String> fileNames = new ArrayList<String>();
		File file = new File(folderPath);
		File[] files = file.listFiles();
		for (File item : files) {
			fileNames.add(item.getName());
		}
		return fileNames;
	}
	
	/*
	 * ArrayListתObservableList
	 */
	public static ObservableList<String> ALToOL(ArrayList<String> data) {
		ObservableList<String> newData = FXCollections.observableArrayList();
		for (String item : data) {
			newData.add(item);
		}
		return newData;
	}
	
	/*
	 * ObservableListתArrayList
	 */
	public static ArrayList<String> OLToAL(ObservableList<String> data) {
//		ObservableList<String> newData = FXCollections.observableArrayList();
		ArrayList<String> newData = new ArrayList<String>();
 		for (String item : data) {
			newData.add(item);
		}
		return newData;
	}
}
