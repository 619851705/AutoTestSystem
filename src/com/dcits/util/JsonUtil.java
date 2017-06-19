package com.dcits.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

 
/** 
 *  ͨ�ö��json�ݹ��������û��Object���󣬻��Ǽ��ȸ��ӵĶ༶Ƕ��json�����������ķ�ʽ��ֱ�ӻ�ȡ�����
 *  ֧��String��Map��ArrayList��ArrayMap���ַ��ض�������ݻ�ȡ
 *  ʹ�÷�ʽ������json�㼶��ϵֱ��ʹ��: ���ڵ�.�ӽڵ�.��ڵ�
 *  @author ww
 */

@SuppressWarnings("rawtypes")
public class JsonUtil {
 
    //private static String jsonStr = "{\"ROOT\":{\"PHONE_NO\": \"18755189818\",\"LOGIN_NO\":\"A0AAA0021\",\"CHANNEL_TYPE\": \"1000\",\"ROUTE_NO\": \"18755189818\"}}";
    private static ObjectMapper mapper = new ObjectMapper();
    
    /**
     * ���������json����ȡָ���Ľڵ���Ϣ
     * mode=1,���ذ������нڵ����Ƶ�list
     * mode=0,���ذ������нڵ�����͵�list
     * mode=2,����keyΪ�ڵ�����,valueΪ�ڵ�ֵ��map
     * mode=3,���ذ������е�map
     * @param jsonStr
     * @param mode
     * @return
     */
    public static Object getJsonList(String jsonStr,int mode){
    	//���صĶ����ӽڵ��map����list
    	//modeΪ1=list,2=map
    	Map<String,String> jsonTreeMap=new HashMap<String,String>();
    	List<String> jsonTreeList=new ArrayList<String>();
    	List<String> jsonTreeType=new ArrayList<String>();
    	
    	try {
			Map maps = mapper.readValue(jsonStr, Map.class);
			viewJsonTree(maps,jsonTreeMap,jsonTreeList,jsonTreeType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		} 
		if(mode==1){
			return jsonTreeList;
		}else if(mode==2){
			return jsonTreeMap;
		}else if(mode==0){
			return jsonTreeType;			
		}else{
			Object a[] = {jsonTreeList,jsonTreeType,jsonTreeMap};
			return a;
		}
        
    }
    

    
    /** ����Ƕ��MapתJson  */
    public static String getObjectByJson(Object obj){
        String str = "";
        try {
            str = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            System.out.println("###[Error] getObjectToJson() "+e.getMessage());
        }
        return str;
    }
    /** ����Ƕ��Json�㼶չʾ  */
    
	public static Object viewJsonTree(Object m,Map<String,String> jsonTreeMap,List<String> jsonTreeList,List<String> jsonTreeType){
        if(m == null){ return null;}  
        try {
            Map mp = null;
            List ls = null;
            if(m instanceof Map || m instanceof LinkedHashMap){
                mp = (LinkedHashMap)m;
                for(Iterator ite = mp.entrySet().iterator(); ite.hasNext();){  
                    Map.Entry e = (Map.Entry) ite.next();  
                    if(e.getValue() instanceof String){
                        //System.out.println("[String]"+e.getKey()+" | " + e.getValue());
                        jsonTreeMap.put(e.getKey().toString(),e.getValue().toString());
                        jsonTreeList.add(e.getKey().toString()); 
                        jsonTreeType.add("String");
                    }else if(e.getValue() instanceof LinkedHashMap){
                        //System.out.println("{Map}"+ e.getKey()+" | "+e.getValue());
                    	jsonTreeList.add(e.getKey().toString());
                    	jsonTreeMap.put(e.getKey().toString(),"");
                    	jsonTreeType.add("Map");
                        viewJsonTree(e.getValue(),jsonTreeMap,jsonTreeList,jsonTreeType);
                    }else if(e.getValue() instanceof ArrayList){
                        //System.out.println("[Array]"+ e.getKey()+" | "+e.getValue());
                    	jsonTreeList.add(e.getKey().toString());
                    	jsonTreeMap.put(e.getKey().toString(),"");
                    	jsonTreeType.add("Array");
                        viewJsonTree(e.getValue(),jsonTreeMap,jsonTreeList,jsonTreeType);
                    }else if(e.getValue() instanceof Number){
                    	jsonTreeList.add(e.getKey().toString());
                    	jsonTreeMap.put(e.getKey().toString(),String.valueOf(e.getValue()));
                    	jsonTreeType.add("Number");
                        viewJsonTree(e.getValue(),jsonTreeMap,jsonTreeList,jsonTreeType);
                    }
                }     
            }
            if(m instanceof List || m instanceof ArrayList){
                ls = (ArrayList)m;
                for(int i=0;i<ls.size();i++){
                    if(ls.get(i) instanceof LinkedHashMap){
                        viewJsonTree(ls.get(i),jsonTreeMap,jsonTreeList,jsonTreeType);
                    }else if(ls.get(i) instanceof ArrayList){
                        viewJsonTree(ls.get(i),jsonTreeMap,jsonTreeList,jsonTreeType);
                    }   
                }
            }   
            //System.out.println();
        } catch (Exception e) {
            //System.out.println("###[Error] viewJsonTree() "+e.getMessage());
        	return null;
        }
        return null;
    }   
     
     
    private static int i = 0;
    /** ����Ƕ��Json��ȡObject����  */
   
	public static String getObjectByJson(String jsonStr,String argsPath,TypeEnum argsType){
        if(argsPath == null || argsPath.equals("") || argsType == null){return null;}
         
        Object obj = null;
        try {
            Map maps = mapper.readValue(jsonStr, Map.class);
           
            //����ȡ
            if(argsPath.indexOf(".") >= 0){
            	
                //��������Ӧ
                obj = getObject(maps,argsPath,argsType);
            }else{ //��һ���ȡ
                if(argsType == TypeEnum.string){
                    obj = maps.get(argsPath).toString();
                }else if(argsType == TypeEnum.map){
                    obj = maps.get(argsPath);
                }else if(argsType == TypeEnum.arrayList){
                    obj = maps.get(argsPath);
                }else if(argsType == TypeEnum.arrayMap){
                    obj = maps.get(argsPath);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("###[Error] getObjectByJson() "+e.getMessage());
            return null;
        }
        i = 0;
        if(obj!=null){
        	return obj.toString();
        }else{
        	return null;
        }
        
    }
    //�ݹ��ȡobject
	private static Object getObject(Object m,String key,TypeEnum type){
        if(m == null){return null;}
        Object o = null;
        Map mp = null;
        List ls = null;
        try {
            //����㼶�ݹ��������
            if(m instanceof Map || m instanceof LinkedHashMap){
                mp = (LinkedHashMap)m;
                for(Iterator ite = mp.entrySet().iterator(); ite.hasNext();){  
                    Map.Entry e = (Map.Entry) ite.next();  
                     
                    if(i<key.split("\\.").length && e.getKey().equals(key.split("\\.")[i])){
                        i++;
                        if(e.getValue() instanceof String){
                            if(i== key.split("\\.").length){
                                o = e.getValue();
                                return o;
                            }
                        }else if(e.getValue() instanceof LinkedHashMap){
                            if(i== key.split("\\.").length){
                                if(type == TypeEnum.map){
                                    o = e.getValue();
                                    return o;
                                }
                            }else{
                                o = getObject(e.getValue(),key,type);
                            }
                            return o;
                        }else if(e.getValue() instanceof ArrayList){
                            if(i== key.split("\\.").length){
                                if(type == TypeEnum.arrayList){
                                    o = e.getValue();
                                    return o;
                                }
                                if(type == TypeEnum.arrayMap){
                                    o = e.getValue();
                                    return o;
                                }
                            }else{
                                o = getObject(e.getValue(),key,type);
                            }
                            return o;
                        }
                    }
                }     
            }
            //����㼶�ݹ��������
            if(m instanceof List || m instanceof ArrayList){
                ls = (ArrayList)m;
                for(int i=0;i<ls.size();i++){
                    if(ls.get(i) instanceof LinkedHashMap){
                        if(i== key.split("\\.").length){
                            if(type == TypeEnum.map){
                                o = ls.get(i);
                                return o;
                            }
                        }else{
                            o = getObject(ls.get(i),key,type);
                        }
                        return o;
                    }else if(ls.get(i) instanceof ArrayList){
                        if(i== key.split("\\.").length){
                            if(type == TypeEnum.arrayList){
                                o = ls.get(i);
                                return o;
                            }
                            if(type == TypeEnum.arrayMap){
                                o = ls.get(i);
                                return o;
                            }
                        }else{
                            o = getObject(ls.get(i),key,type);
                        }
                        return o;
                    }   
                }
            }   
        } catch (Exception e) {
            System.out.println("###[Error] getObject() "+e.getMessage());
        }
         
        return o;
    }
     
     
    /*
     * Json���ݽ���������������ö��
     */
    public enum TypeEnum{
        /** �����ļ�ֵ�ԣ�ͨ��key��ȡvalus */
        string,
        /** ͨ��key��ȡ��Map���� */
        map,
        /** ͨ��key��ȡ��ArrayList���� */
        arrayList,
        /** ͨ��key��ȡ��ArrayMap������� */
        arrayMap;
        
    }
 
}