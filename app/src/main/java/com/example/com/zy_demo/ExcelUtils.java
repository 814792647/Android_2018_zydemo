package com.example.com.zy_demo;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ExcelUtils {
    private Workbook wb;
    private Sheet sheet;
    private Row row;
    private Context context;
    private String[][] Output;

    public ExcelUtils(Context context){
        this.context = context;
    }

    public String[][] InputExcel(File file,int type){
        try {
            InputStream is = new FileInputStream(file);
            if(type == 1){
                wb = new XSSFWorkbook(is);
            }else{
                wb = new HSSFWorkbook(is);
            }
            sheet = wb.getSheetAt(0);
            int rounum = sheet.getLastRowNum();
            Output = new String[rounum][3];
            for(int i=0;i<rounum;i++){
                row = sheet.getRow(i+1);
                for (int j=0;j<3;j++){
                    row.getCell(j).setCellType(CellType.STRING);
                    Output[i][j] = row.getCell(j).getStringCellValue();
                }
            }

            String[][] temp = new String[rounum][3];
            int Max = 0;
            for(int j=0;j<rounum;j++){
                for(int i=0;i<rounum;i++) {
                    if(Integer.valueOf(Output[Max][1])<Integer.valueOf(Output[i][1])){
                        Max = i;
                    }
                }
                temp[j][0] = Output[Max][0];
                temp[j][1] = Output[Max][1];
                temp[j][2] = Output[Max][2];
                Output[Max][1] = "-1";
            }
            Output = temp;
            temp = null;
        }catch (Exception e){
        }
        return Output;
    }

    public void OutputExcel(String[][] person){
        try {
            Workbook outwb = new HSSFWorkbook();
            Sheet outsh = outwb.createSheet();
            Row outrow;
            Cell outcell;
            for(int i=0;i<person.length;i++){
                outrow = outsh.createRow(i);
                outcell = outrow.createCell(0);
                outcell.setCellValue("第"+String.valueOf(i+1)+"组");
                for (int j=0;j<person[i].length;j++){
                    outcell = outrow.createCell(j+1);
                    outcell.setCellValue(person[i][j]);
                }
            }
            FileOutputStream fs = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/比赛.xls");
            outwb.write(fs);
            fs.close();
            Toast.makeText(context,"导出完成",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context,"导出失败",Toast.LENGTH_SHORT).show();
        }
    }
}
