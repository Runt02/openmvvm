package com.runt.open.mvvm.data;

/**
 * Created by Administrator on 2021/11/15 0015.
 */
public class ApkUpGradeResult extends HttpApiResult<ApkUpGradeResult.AppInfo> {

    public class AppInfo {
        //以下为声明的参数
        public int type, isEnable, isImportant, id, code;
        public String url,version, content, udate, remark;


        @Override
        public String toString() {
            return "ApkUpGrade{" +
                    "type=" + type + "," +
                    "isEnable=" + isEnable + "," +
                    "isImportant=" + isImportant + "," +
                    "id=" + id + "," +
                    "url=" + url + "," +
                    "version=" + version + "," +
                    "content=" + content + "," +
                    "udate=" + udate + "," +
                    '}';
        }

    }
}
