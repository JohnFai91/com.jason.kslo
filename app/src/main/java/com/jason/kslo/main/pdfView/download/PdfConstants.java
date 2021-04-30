package com.jason.kslo.main.pdfView.download;

class PdfConstants {


    // json {"url":"http://192.168.205.33:8080/Hello/app_v3.0.1_Other_20150116.apk","versionCode":2,"updateMessage":"版本更新信息"}

    static final String File_DOWNLOAD_URL = "url";

    static final String File_Name = "name";
    static final String File_VERSION_CODE = "version";
    static final String Update_Message = "msg";
    static final String Default_Page = "default";

    static final String File_DOWNLOAD_URL_2D = "2D_url";

    static final String File_Name_2D = "2D_name";
    static final String File_VERSION_CODE_2D = "2D_version";
    static final String Update_Message_2D = "2D_msg";
    static final String Default_Page_2D = "2D_default";

    static final String TAG = "UpdatePdfChecker";

    static final String UPDATE_NOTICE_URL = "https://github.com/JohnFai91/com.jason.kslo/raw/master/updateNotice.json";
    static final String UPDATE_SCHEDULE_URL = "https://raw.githubusercontent.com/JohnFai91/com.jason.kslo/master/updateSchedule.json";
}
