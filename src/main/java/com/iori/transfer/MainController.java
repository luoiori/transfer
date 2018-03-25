package com.iori.transfer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @ResponseBody
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String download(@RequestParam("file") MultipartFile file,HttpServletResponse response ,HttpServletRequest request) {
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        if (!file.isEmpty()) {
            try {
                List<Staff> staffs = readData(file);
                Workbook wb = export(staffs,year,month);
                // 往客户端传送文件流
                response.setContentType("application/x-msdownload");
                try {
                    String filename = year+"年"+month+"月指纹记录.xlsx";
                    response.setHeader("Content-Disposition",
                            "attachment;filename=" + new String(URLDecoder.decode(filename, "UTF-8").getBytes(), "iso-8859-1"));
                    // 最终写入文件
                    wb.write(response.getOutputStream());
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "上传成功";
        } else {
            return "上传失败，因为文件是空的.";
        }


    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "upload";
    }

    private List<Staff> readData(MultipartFile file) throws IOException {
        List<Staff> staffs = new ArrayList<Staff>();
        File tmpFile = File.createTempFile("tmp",null);
        file.transferTo(tmpFile);
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(tmpFile));
        XSSFSheet sheet1 = workbook.getSheetAt(0);
        XSSFRow row = sheet1.getRow(1);
        int monthdays = 0;
        for(int i = 0;i<row.getPhysicalNumberOfCells();i++){
            Cell cell = row.getCell(i);
            if(cell != null) {
                cell.setCellType(CellType.STRING);
                String value = cell.getStringCellValue();
                if (value != null && value != "" && isNum(value)) {
                    if (Integer.parseInt(value) > monthdays) {
                        monthdays = Integer.parseInt(value);
                    }
                }
            }
        }

        for(int i =2;i<sheet1.getPhysicalNumberOfRows();i++){
            XSSFRow staffRow = sheet1.getRow(i);
            Staff staff = new Staff();
            staff.setDayNums(monthdays);
            Cell staffNameCell = staffRow.getCell(1);
            if(staffNameCell!=null){
                String name = staffNameCell.getStringCellValue();
                if(name!=null && name != ""){
                    staff.setName(name);
                }
            }
            for(int j = 0;j<monthdays;j++){
                Cell dayCell = staffRow.getCell(2+2*j);
                Day day = new Day();
                if(dayCell!=null){
                    dayCell.setCellType(CellType.STRING);
                    day.setDay(j+1);
                    day.setAm(dayCell.getStringCellValue());
                }
                Cell nextCell = staffRow.getCell(2*j+3);
                if(nextCell!=null){
                    nextCell.setCellType(CellType.STRING);
                    day.setPm(nextCell.getStringCellValue());
                }
                staff.getDays().set(j,day);
            }
            staffs.add(staff);
        }
        return staffs;

    }

    private boolean isNum(String s){
        return s.matches("[0-9]+");
    }

    public XSSFWorkbook export(List<Staff> staffs,String year,String month) {
        String yearMonth = year+"-"+month+"-";
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            // 1
            XSSFSheet sheet1 = workbook.createSheet();
            int bodyRowIndex = 0;
            int bodyColumbIndex = 0;
            XSSFRow row = sheet1.createRow(bodyRowIndex++);
            String[] header = {"部门","姓名","登记号码","日期时间","机器号","编号","比对方式","卡号"};
            for(int i=0;i<header.length;i++) {
                Cell cell = row.createCell(bodyColumbIndex++);
                cell.setCellValue(header[i]);
            }
            for(int i=0;i<staffs.size();i++){
                Staff staff = staffs.get(i);
                for(Day day:staff.getDays()) {

                    if(day.getAm()!=null && day.getAm()!=""&& isDate(day.getAm())) {
                        XSSFRow staffRow = sheet1.createRow(bodyRowIndex++);
                        int col = 0;
                        Cell cell = staffRow.createCell(col++);
                        cell.setCellValue("总公司");

                        cell = staffRow.createCell(col++);
                        cell.setCellValue(staff.getName());

                        cell = staffRow.createCell(col++);
                        cell.setCellValue("2");

                        cell = staffRow.createCell(col++);
                        cell.setCellValue(yearMonth+day.getDay()+" "+day.getAm());

                        cell = staffRow.createCell(col++);
                        cell.setCellValue("1");

                        cell = staffRow.createCell(col++);
                        cell.setCellValue("");

                        cell = staffRow.createCell(col++);
                        cell.setCellValue("指纹");
                    }
                    if(day.getPm()!=null && day.getPm()!=""&& isDate(day.getPm())) {
                        int col = 0;
                        XSSFRow staffRow = sheet1.createRow(bodyRowIndex++);

                        Cell cell  = staffRow.createCell(col++);
                        cell.setCellValue("总公司");

                        cell = staffRow.createCell(col++);
                        cell.setCellValue(staff.getName());

                        cell = staffRow.createCell(col++);
                        cell.setCellValue("2");

                        cell = staffRow.createCell(col++);
                        cell.setCellValue(yearMonth+day.getDay()+" "+day.getPm());

                        cell = staffRow.createCell(col++);
                        cell.setCellValue("1");

                        cell = staffRow.createCell(col++);
                        cell.setCellValue("");

                        cell = staffRow.createCell(col++);
                        cell.setCellValue("指纹");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workbook;
    }

    private boolean isDate(String am) {
        for(char c:am.toCharArray()){
            if( !(Character.isDigit(c) || c==':' || c=='.')){
                return false;
            }
        }

       return true;

    }


}
