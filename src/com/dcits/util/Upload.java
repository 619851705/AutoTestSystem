package com.dcits.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
public class Upload {
    /**
     * ��ͨ�����ϴ��ļ�����
     * 
     * @param fa
     *            �ļ�����
     * @param fna
     *            �ļ���������
     * @return �ϴ��ļ���·���ַ���
     */
    public String commUpload(File[] fa, String[] fna) {
        String paths = "";
        ActionContext ac = ActionContext.getContext();
        ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);
        String rp = sc.getRealPath("/"), folder = "", newNm = "", ext = "";
        // String rp = "/data/web/static/", folder = "", newName = "";
        File dir = null, destFile = null;
        InputStream is = null;
        OutputStream os = null;
        for(int i = 0; i < fa.length; ++i) {
            ext = fna[i].substring(fna[i].lastIndexOf(".")).toLowerCase();
            if(".jpg.png.bmp.gif".indexOf(ext) != -1) {
                folder = newPath(1, rp);
            } else if(".flv".equals(ext)) {
                folder = newPath(2, rp);
            } else {
                folder = newPath(3, rp);
            }
            newNm = newName(fna[i]);
            dir = new File(folder);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            destFile = new File(dir, newNm);
            try {
                is = new FileInputStream(fa[i]);
                os = new FileOutputStream(destFile);
                byte[] buffer = new byte[400];
                int length = 0;
                while((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                paths += dir + newNm +",";
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                    os.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        paths = paths.substring(0, paths.length()-1);
        return paths;
    }
    /**
     * �����ϴ��ļ�����
     * 
     * @param fa
     *            �ļ�
     * @param fna
     *            �ļ�����
     * @return �ϴ��ļ���·��
     */
    @SuppressWarnings("unused")
	public String singleUpload(File fa, String fna,int fileKind) {
        String path = "";
        ActionContext ac = ActionContext.getContext();
        ServletContext sc = (ServletContext) ac.get(ServletActionContext.SERVLET_CONTEXT);
        String rp = sc.getRealPath("/"), folder = "", newNm = "", ext = "";
        // String rp = "/data/web/static/", folder = "", newName = "";
        File dir = null, destFile = null;
        InputStream is = null;
        OutputStream os = null;
        ext = fna.substring(fna.lastIndexOf(".")).toLowerCase();
      /*  if(".jpg.png.bmp.gif.jepg".indexOf(ext) != -1) {
            folder = newPath(1, rp);
        } else if(".flv.mp4.3gp.avi".equals(ext)) {
            folder = newPath(2, rp);
        } else {
            folder = newPath(3, rp);
        }*/
        //newNm = newName(fna);
        folder = newPath(fileKind,rp);
        dir = new File(folder);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        destFile = new File(dir, fna);
        try {
            is = new FileInputStream(fa);
            os = new FileOutputStream(destFile);
            byte[] buffer = new byte[400];
            int length = 0;
            while((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            path = dir + newNm;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
    /**
     * �ϴ��ļ�·��
     * 
     * @param fType
     *            �ļ����1Ϊ��ͨ�ű��ļ���0Ϊ�����ű��ļ�
     * @param rp
     *            �ϴ��ļ�·��
     * @return �ϴ��ļ���·��
     */
    @SuppressWarnings("unused")
	public String newPath(int fType, String rp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String dts = sdf.format(new java.util.Date());
        switch(fType) {
            case 1 :
                rp += "files/rubyScript/";
                break;
            case 0 :
                rp += "files/publicScript/";
                break;
            default :
                rp += "files/other/";
                break;
        }
        return rp;
    }
    /**
     * �������ϴ��ļ�
     * 
     * @param srcName
     *            �ϴ��ļ����ļ���
     * @return ����������ļ���
     */
    public String newName(String srcName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String ext = srcName.substring(srcName.lastIndexOf(".")), dt = sdf.format(new java.util.Date()), rd = Math.round(Math.random() * 900) + 100 + "";
        return dt + rd + ext;
    }
}